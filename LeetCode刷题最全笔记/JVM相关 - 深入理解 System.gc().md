# JVM相关 - 深入理解 System.gc()

> 本文基于 Java 17-ea，但是相关设计在 Java 11 之后是大致一样的

我们经常在面试中询问 `System.gc()` 究竟会不会**立刻**触发 **Full GC**，网上也有很多人给出了答案，但是这些答案都有些过时了。本文基于最新的 Java 的下一个即将发布的 LTS 版本 Java 17（ea）的源代码，深入解析 System.gc() 背后的故事。

## 为什么需要System.gc()

### 1. 使用并管理堆外内存的框架，需要 Full GC 的机制触发堆外内存回收

JVM 的内存，不止堆内存，还有其他很多块，通过 Native Memory Tracking 可以看到：

```
Native Memory Tracking:

Total: reserved=6308603KB, committed=4822083KB
-                 Java Heap (reserved=4194304KB, committed=4194304KB)
                            (mmap: reserved=4194304KB, committed=4194304KB) 
 
-                     Class (reserved=1161041KB, committed=126673KB)
                            (classes #21662)
                            (  instance classes #20542, array classes #1120)
                            (malloc=3921KB #64030) 
                            (mmap: reserved=1157120KB, committed=122752KB) 
                            (  Metadata:   )
                            (    reserved=108544KB, committed=107520KB)
                            (    used=105411KB)
                            (    free=2109KB)
                            (    waste=0KB =0.00%)
                            (  Class space:)
                            (    reserved=1048576KB, committed=15232KB)
                            (    used=13918KB)
                            (    free=1314KB)
                            (    waste=0KB =0.00%)
 
-                    Thread (reserved=355251KB, committed=86023KB)
                            (thread #673)
                            (stack: reserved=353372KB, committed=84144KB)
                            (malloc=1090KB #4039) 
                            (arena=789KB #1344)
 
-                      Code (reserved=252395KB, committed=69471KB)
                            (malloc=4707KB #17917) 
                            (mmap: reserved=247688KB, committed=64764KB) 
 
-                        GC (reserved=199635KB, committed=199635KB)
                            (malloc=11079KB #29639) 
                            (mmap: reserved=188556KB, committed=188556KB) 
 
-                  Compiler (reserved=2605KB, committed=2605KB)
                            (malloc=2474KB #2357) 
                            (arena=131KB #5)
 
-                  Internal (reserved=3643KB, committed=3643KB)
                            (malloc=3611KB #8683) 
                            (mmap: reserved=32KB, committed=32KB) 
 
-                     Other (reserved=67891KB, committed=67891KB)
                            (malloc=67891KB #2859) 
 
-                    Symbol (reserved=26220KB, committed=26220KB)
                            (malloc=22664KB #292684) 
                            (arena=3556KB #1)
 
-    Native Memory Tracking (reserved=7616KB, committed=7616KB)
                            (malloc=585KB #8238) 
                            (tracking overhead=7031KB)
 
-               Arena Chunk (reserved=10911KB, committed=10911KB)
                            (malloc=10911KB) 
 
-                   Tracing (reserved=25937KB, committed=25937KB)
                            (malloc=25937KB #8666) 
 
-                   Logging (reserved=5KB, committed=5KB)
                            (malloc=5KB #196) 
 
-                 Arguments (reserved=18KB, committed=18KB)
                            (malloc=18KB #486) 
 
-                    Module (reserved=532KB, committed=532KB)
                            (malloc=532KB #3579) 
 
-              Synchronizer (reserved=591KB, committed=591KB)
                            (malloc=591KB #4777) 
 
-                 Safepoint (reserved=8KB, committed=8KB)
                            (mmap: reserved=8KB, committed=8KB) 
复制代码
```

- ==Java Heap: 堆内存，即`-Xmx`限制的最大堆大小的内存。==
- ==Class：加载的类与方法信息，其实就是 metaspace，包含两部分： 一是 metadata，被`-XX:MaxMetaspaceSize`限制最大大小，另外是 class space，被`-XX:CompressedClassSpaceSize`限制最大大小==
- ==Thread：线程与线程栈占用内存，每个线程栈占用大小受`-Xss`限制，但是总大小没有限制。==
- ==Code：JIT 即时编译后（C1 C2 编译器优化）的代码占用内存，受`-XX:ReservedCodeCacheSize`限制==
- ==GC：垃圾回收占用内存，例如垃圾回收需要的 CardTable，标记数，区域划分记录，还有标记 GC Root 等等，都需要内存。这个不受限制，一般不会很大的。==
- Compiler：C1 C2 编译器本身的代码和标记占用的内存，这个不受限制，一般不会很大的
- Internal：命令行解析，JVMTI 使用的内存，这个不受限制，一般不会很大的
- Symbol: 常量池占用的大小，字符串常量池受`-XX:StringTableSize`个数限制，总内存大小不受限制
- Native Memory Tracking：内存采集本身占用的内存大小，如果没有打开采集（那就看不到这个了，哈哈），就不会占用，这个不受限制，一般不会很大的
- Arena Chunk：所有通过 arena 方式分配的内存，这个不受限制，一般不会很大的
- Tracing：所有采集占用的内存，如果开启了 JFR 则主要是 JFR 占用的内存。这个不受限制，一般不会很大的
- Logging，Arguments，Module，Synchronizer，Safepoint，Other，这些一般我们不会关心。

除了 Native Memory Tracking 记录的内存使用，还有两种内存 **Native Memory Tracking 没有记录**，那就是：

- Direct Buffer：直接内存
- MMap Buffer：文件映射内存

针对==除了堆内存以外，其他的内存，有些也是需要 GC 的==。例如：MetaSpace，CodeCache，Direct Buffer，MMap Buffer 等等。早期在 Java 8 之前的 JVM，对于这些内存回收的机制并不完善，很多情况下都需要 **FullGC** 扫描整个堆才能确定这些区域中哪些内存可以回收。

有一些框架，大量使用并管理了这些堆外空间。例如 netty 使用了 Direct Buffer，Kafka 和 RocketMQ 使用了 Direct Buffer 和 MMap Buffer。他们都是提前从系统申请好一块内存，之后管理起来并使用。在空间不足时，继续向系统申请，并且也会有缩容。例如 netty，在使用的 Direct Buffer 达到`-XX:MaxDirectMemorySize`的限制之后，则会先尝试将不可达的Reference对象加入Reference链表中，依赖Reference的内部守护线程触发可以被回收DirectByteBuffer关联的Cleaner的run()方法。如果内存还是不足， 则执行`System.gc()`，期望触发`full gc`，来回收堆内存中的`DirectByteBuffer`对象来触发堆外内存回收，如果还是超过限制，则抛出`java.lang.OutOfMemoryError`.

### 2. 使用了 ==WeakReference， SoftReference 的程序，需要相应的 GC 回收==。

对于 ==WeakReference，只要发生 GC，无论是 Young GC 还是 FullGC 就会被回收==。==SoftReference 只有在 FullGC 的时候才会被回收。==当我们程序想==主动对于这些引用进行回收的时候，需要能触发 GC 的方法，这就用到了`System.gc()`==。



## System.gc() 可以触发垃圾回收，full gc和轻量都有可能

### 3. 测试，学习 JVM 机制的时候

有些时候，我们为了测试，学习 JVM 的某些机制，需要让 JVM 做一次 GC 之后开始，这也会用到`System.gc()`。但是其实有更好的方法，后面你会看到。

## System.gc() 背后的原理

==`System.gc()`实际上调用的是`RunTime.getRunTime().gc()`:==

```
public static void gc() {
    Runtime.getRuntime().gc();
}
复制代码
```

这个方法是一个 ==native 方法：==

```
public native void gc();
复制代码
```

对应 JVM 源码：

```
JVM_ENTRY_NO_ENV(void, JVM_GC(void))
  JVMWrapper("JVM_GC");
  //如果没有将JVM启动参数 DisableExplicitGC 设置为 false，则执行 GC，GC 原因是 System.gc 触发，对应 GCCause::_java_lang_system_gc
  if (!DisableExplicitGC) {
    Universe::heap()->collect(GCCause::_java_lang_system_gc);
  }
JVM_END
复制代码
```

首先，==根据 DisableExplicitGC 这个 JVM 启动参数的状态，确定是否会 GC，如果需要 GC，不同 GC 会有不同的处理。==

### 1. ==G1== GC 的处理

如果是 `System.gc()` 触发的 GC，==G1 GC 会根据 ExplicitGCInvokesConcurrent 这个 JVM 参数决定是默认 GC （轻量 GC，YoungGC）还是 FullGC。==

参考代码[`g1CollectedHeap.cpp`](https://github.com/openjdk/jdk/blob/jdk-17+11/src/hotspot/share/gc/g1/g1CollectedHeap.cpp)：

```java
//是否应该并行 GC，也就是较为轻量的 GC，对于 GCCause::_java_lang_system_gc，这里就是判断 ExplicitGCInvokesConcurrent 这个 JVM 是否为 true
if (should_do_concurrent_full_gc(cause)) {
    return try_collect_concurrently(cause,
                                    gc_count_before,
                                    old_marking_started_before);
}// 省略其他这里我们不关心的判断分支
 else {
    //否则进入 full GC
    VM_G1CollectFull op(gc_count_before, full_gc_count_before, cause);
    VMThread::execute(&op);
    return op.gc_succeeded();
}
复制代码
```

### 2. ==ZGC== 的处理

==直接不处理，不支持通过 `System.gc()` 触发 GC。==

参考源码：[`zDriver.cpp`](https://github.com/openjdk/jdk/blob/jdk-17+11/src/hotspot/share/gc/z/zDriver.cpp)

```
void ZDriver::collect(GCCause::Cause cause) {
  switch (cause) {
  //注意这里的 _wb 开头的 GC 原因，这代表是 WhiteBox 触发的，后面我们会用到，这里先记一下
  case GCCause::_wb_young_gc:
  case GCCause::_wb_conc_mark:
  case GCCause::_wb_full_gc:
  case GCCause::_dcmd_gc_run:
  case GCCause::_java_lang_system_gc:
  case GCCause::_full_gc_alot:
  case GCCause::_scavenge_alot:
  case GCCause::_jvmti_force_gc:
  case GCCause::_metadata_GC_clear_soft_refs:
    // Start synchronous GC
    _gc_cycle_port.send_sync(cause);
    break;

  case GCCause::_z_timer:
  case GCCause::_z_warmup:
  case GCCause::_z_allocation_rate:
  case GCCause::_z_allocation_stall:
  case GCCause::_z_proactive:
  case GCCause::_z_high_usage:
  case GCCause::_metadata_GC_threshold:
    // Start asynchronous GC
    _gc_cycle_port.send_async(cause);
    break;

  case GCCause::_gc_locker:
    // Restart VM operation previously blocked by the GC locker
    _gc_locker_port.signal();
    break;

  case GCCause::_wb_breakpoint:
    ZBreakpoint::start_gc();
    _gc_cycle_port.send_async(cause);
    break;

  //对于其他原因，不触发GC，GCCause::_java_lang_system_gc 会走到这里
  default:
    // Other causes not supported
    fatal("Unsupported GC cause (%s)", GCCause::to_string(cause));
    break;
  }
}
复制代码
```

### 3. ==Shenandoah GC== 的处理

Shenandoah 的处理**和 G1 GC 的类似**，==先**判断是不是用户明确触发的 GC**==，然后==通过 DisableExplicitGC 这个 JVM 参数判断是否可以 GC==（其实这个是多余的，可以去掉，因为外层`JVM_ENTRY_NO_ENV(void, JVM_GC(void))`已经处理这个状态位了）。==如果可以，则请求 GC，阻塞等待 GC 请求被处理==。然后**==根据 ExplicitGCInvokesConcurrent 这个 JVM 参数决定是默认 GC （轻量并行 GC，YoungGC）还是 FullGC==**。

参考源码[`shenandoahControlThread.cpp`](https://github.com/openjdk/jdk/blob/jdk-17+11/src/hotspot/share/gc/shenandoah/shenandoahControlThread.cpp)

```
void ShenandoahControlThread::request_gc(GCCause::Cause cause) {
  assert(GCCause::is_user_requested_gc(cause) ||
         GCCause::is_serviceability_requested_gc(cause) ||
         cause == GCCause::_metadata_GC_clear_soft_refs ||
         cause == GCCause::_full_gc_alot ||
         cause == GCCause::_wb_full_gc ||
         cause == GCCause::_scavenge_alot,
         "only requested GCs here");
  //如果是显式GC（即如果是GCCause::_java_lang_system_gc，GCCause::_dcmd_gc_run，GCCause::_jvmti_force_gc，GCCause::_heap_inspection，GCCause::_heap_dump中的任何一个）
  if (is_explicit_gc(cause)) {
    //如果没有关闭显式GC，也就是 DisableExplicitGC 为 false
    if (!DisableExplicitGC) {
      //请求 GC
      handle_requested_gc(cause);
    }
  } else {
    handle_requested_gc(cause);
  }
}
复制代码
```

请求 GC 的代码流程是：

```
void ShenandoahControlThread::handle_requested_gc(GCCause::Cause cause) {
  MonitorLocker ml(&_gc_waiters_lock);
  //获取当前全局 GC id
  size_t current_gc_id = get_gc_id();
  //因为要进行 GC ，所以将id + 1
  size_t required_gc_id = current_gc_id + 1;
  //直到当前全局 GC id + 1 为止，代表 GC 执行了
  while (current_gc_id < required_gc_id) {
    //设置 gc 状态位，会有其他线程扫描执行 gc
    _gc_requested.set();
    //记录 gc 原因，根据不同原因有不同的处理策略，我们这里是 GCCause::_java_lang_system_gc
    _requested_gc_cause = cause;
    //等待 gc 锁对象 notify，代表 gc 被执行并完成
    ml.wait();
    current_gc_id = get_gc_id();
  }
}
复制代码
```

对于`GCCause::_java_lang_system_gc`，GC 的执行流程大概是：

```
bool explicit_gc_requested = _gc_requested.is_set() &&  is_explicit_gc(_requested_gc_cause);

//省略一些代码

else if (explicit_gc_requested) {
  cause = _requested_gc_cause;
  log_info(gc)("Trigger: Explicit GC request (%s)", GCCause::to_string(cause));

  heuristics->record_requested_gc();
  // 如果 JVM 参数 ExplicitGCInvokesConcurrent 为 true，则走默认轻量 GC
  if (ExplicitGCInvokesConcurrent) {
    policy->record_explicit_to_concurrent();
    mode = default_mode;
    // Unload and clean up everything
    heap->set_unload_classes(heuristics->can_unload_classes());
  } else {
    //否则，执行 FullGC
    policy->record_explicit_to_full();
    mode = stw_full;
  }
}
复制代码
```

## System.gc() 相关的 JVM 参数

### 1. ==DisableExplicitGC==

**说明**：==是否禁用**显式 GC**==，默认是不禁用的。对于 Shenandoah GC，**显式 GC** 包括：`GCCause::_java_lang_system_gc`，`GCCause::_dcmd_gc_run`，`GCCause::_jvmti_force_gc`，`GCCause::_heap_inspection`，`GCCause::_heap_dump`，对于其他 GC，仅仅限制`GCCause::_java_lang_system_gc`

**默认**：false

**举例**：如果想禁用显式 GC：`-XX:+DisableExplicitGC`

### 2. ==ExplicitGCInvokesConcurrent==

**说明**：==对于**显式 GC**，是执行轻量并行 GC （YoungGC）还是 FullGC，如果为 **true 则是执行轻量并行 GC （YoungGC），false 则是执行 FullGC**==

**默认**：false

**举例**：启用的话指定：`-XX:+ExplicitGCInvokesConcurrent`

其实，在设计上有人提出（[参考链接](https://bugs.openjdk.java.net/browse/JDK-8071770)）想将 ExplicitGCInvokesConcurrent 改为 true。但是目前并不是所有的 GC 都可以在轻量并行 GC 对 Java 所有内存区域进行回收，有些时候必须通过 FullGC。所以，目前这个参数还是默认为 false

### 3. 已过期的 ExplicitGCInvokesConcurrentAndUnloads 和使用 ClassUnloadingWithConcurrentMark 替代

如果**显式 GC**采用轻量并行 GC，那么无法执行 Class Unloading（类卸载），如果启用了类卸载功能，可能会有异常。所以通过这个状态位来标记在**显式 GC**时，即使采用轻量并行 GC，也要扫描进行类卸载。 `ExplicitGCInvokesConcurrentAndUnloads`目前已经过期了，用`ClassUnloadingWithConcurrentMark`替代

参考[BUG-JDK-8170388](https://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8170388)

## 如何灵活可控的主动触发各种 GC？

答案是通过 ==WhiteBox API==。但是这个不要在生产上面执行，仅仅用来测试 JVM 还有学习 JVM 使用。WhiteBox API 是 HotSpot VM 自带的白盒测试工具，将内部的很多核心机制的 API 暴露出来，用于白盒测试 JVM，压测 JVM 特性，以及辅助学习理解 JVM 并调优参数。WhiteBox API 是 Java 7 引入的，目前 Java 8 LTS 以及 Java 11 LTS（其实是 Java 9+ 以后的所有版本，这里只关心 LTS 版本，Java 9 引入了模块化所以 WhiteBox API 有所变化）都是有的。但是默认这个 API 并没有编译在 JDK 之中，但是他的实现是编译在了 JDK 里面了。所以如果想用这个 API，需要用户自己编译需要的 API，并加入 Java 的 BootClassPath 并启用 WhiteBox API。下面我们来用 WhiteBox API 来主动触发各种 GC。

**1. 编译 WhiteBox API**

将`https://github.com/openjdk/jdk/tree/master/test/lib`路径下的`sun`目录取出，编译成一个 jar 包，名字假设是 `whitebox.jar`

**2. 编写测试程序**

将 `whitebox.jar` 添加到你的项目依赖，之后写代码

```
public static void main(String[] args) throws Exception {
        WhiteBox whiteBox = WhiteBox.getWhiteBox();
        //执行young GC
        whiteBox.youngGC();
        System.out.println("---------------------------------");
        whiteBox.fullGC();
        //执行full GC
        whiteBox.fullGC();
        //保持进程不退出，保证日志打印完整
        Thread.currentThread().join();
}
复制代码
```

**3. 启动程序查看效果**

使用启动参数 `-Xbootclasspath/a:/home/project/whitebox.jar -XX:+UnlockDiagnosticVMOptions -XX:+WhiteBoxAPI -Xlog:gc` 启动程序。其中前三个 Flag 表示启用 WhiteBox API，最后一个表示打印 GC info 级别的日志到控制台。

我的输出：

```
[0.036s][info][gc] Using G1
[0.048s][info][gc,init] Version: 17-internal+0-adhoc.Administrator.jdk (fastdebug)
[0.048s][info][gc,init] CPUs: 16 total, 16 available
[0.048s][info][gc,init] Memory: 16304M
[0.048s][info][gc,init] Large Page Support: Disabled
[0.048s][info][gc,init] NUMA Support: Disabled
[0.048s][info][gc,init] Compressed Oops: Enabled (32-bit)
[0.048s][info][gc,init] Heap Region Size: 1M
[0.048s][info][gc,init] Heap Min Capacity: 512M
[0.048s][info][gc,init] Heap Initial Capacity: 512M
[0.048s][info][gc,init] Heap Max Capacity: 512M
[0.048s][info][gc,init] Pre-touch: Disabled
[0.048s][info][gc,init] Parallel Workers: 13
[0.048s][info][gc,init] Concurrent Workers: 3
[0.048s][info][gc,init] Concurrent Refinement Workers: 13
[0.048s][info][gc,init] Periodic GC: Disabled
[0.049s][info][gc,metaspace] CDS disabled.
[0.049s][info][gc,metaspace] Compressed class space mapped at: 0x0000000100000000-0x0000000140000000, reserved size: 1073741824
[0.049s][info][gc,metaspace] Narrow klass base: 0x0000000000000000, Narrow klass shift: 3, Narrow klass range: 0x140000000
[1.081s][info][gc,start    ] GC(0) Pause Young (Normal) (WhiteBox Initiated Young GC)
[1.082s][info][gc,task     ] GC(0) Using 12 workers of 13 for evacuation
[1.089s][info][gc,phases   ] GC(0)   Pre Evacuate Collection Set: 0.5ms
[1.089s][info][gc,phases   ] GC(0)   Merge Heap Roots: 0.1ms
[1.089s][info][gc,phases   ] GC(0)   Evacuate Collection Set: 3.4ms
[1.089s][info][gc,phases   ] GC(0)   Post Evacuate Collection Set: 1.6ms
[1.089s][info][gc,phases   ] GC(0)   Other: 1.3ms
[1.089s][info][gc,heap     ] GC(0) Eden regions: 8->0(23)
[1.089s][info][gc,heap     ] GC(0) Survivor regions: 0->2(4)
[1.089s][info][gc,heap     ] GC(0) Old regions: 0->0
[1.089s][info][gc,heap     ] GC(0) Archive regions: 0->0
[1.089s][info][gc,heap     ] GC(0) Humongous regions: 0->0
[1.089s][info][gc,metaspace] GC(0) Metaspace: 6891K(7104K)->6891K(7104K) NonClass: 6320K(6400K)->6320K(6400K) Class: 571K(704K)->571K(704K)
[1.089s][info][gc          ] GC(0) Pause Young (Normal) (WhiteBox Initiated Young GC) 7M->1M(512M) 7.864ms
[1.089s][info][gc,cpu      ] GC(0) User=0.00s Sys=0.00s Real=0.01s
---------------------------------
[1.091s][info][gc,task     ] GC(1) Using 12 workers of 13 for full compaction
[1.108s][info][gc,start    ] GC(1) Pause Full (WhiteBox Initiated Full GC)
[1.108s][info][gc,phases,start] GC(1) Phase 1: Mark live objects
[1.117s][info][gc,phases      ] GC(1) Phase 1: Mark live objects 8.409ms
[1.117s][info][gc,phases,start] GC(1) Phase 2: Prepare for compaction
[1.120s][info][gc,phases      ] GC(1) Phase 2: Prepare for compaction 3.031ms
[1.120s][info][gc,phases,start] GC(1) Phase 3: Adjust pointers
[1.126s][info][gc,phases      ] GC(1) Phase 3: Adjust pointers 5.806ms
[1.126s][info][gc,phases,start] GC(1) Phase 4: Compact heap
[1.190s][info][gc,phases      ] GC(1) Phase 4: Compact heap 63.812ms
[1.193s][info][gc,heap        ] GC(1) Eden regions: 1->0(25)
[1.193s][info][gc,heap        ] GC(1) Survivor regions: 2->0(4)
[1.193s][info][gc,heap        ] GC(1) Old regions: 0->3
[1.193s][info][gc,heap        ] GC(1) Archive regions: 0->0
[1.193s][info][gc,heap        ] GC(1) Humongous regions: 0->0
[1.193s][info][gc,metaspace   ] GC(1) Metaspace: 6895K(7104K)->6895K(7104K) NonClass: 6323K(6400K)->6323K(6400K) Class: 571K(704K)->571K(704K)
[1.193s][info][gc             ] GC(1) Pause Full (WhiteBox Initiated Full GC) 1M->0M(512M) 84.846ms
[1.202s][info][gc,cpu         ] GC(1) User=0.19s Sys=0.63s Real=0.11s
```