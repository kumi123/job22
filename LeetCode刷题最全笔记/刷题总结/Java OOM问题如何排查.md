# [Java OOM问题如何排查](https://www.cnblogs.com/valjeanshaw/p/13130102.html)

@

目录

- OOM 问题
  - [什么是OOM](https://www.cnblogs.com/valjeanshaw/p/13130102.html#什么是oom)
  - [导致OOM问题的原因](https://www.cnblogs.com/valjeanshaw/p/13130102.html#导致oom问题的原因)
  - [排查手段](https://www.cnblogs.com/valjeanshaw/p/13130102.html#排查手段)
- 实战
  - [MAT分析](https://www.cnblogs.com/valjeanshaw/p/13130102.html#mat分析)



## OOM 问题

### 什么是OOM

OOM为out of memory的简称，来源于java.lang.OutOfMemoryError，指程序需要的内存空间大于系统分配的内存空间，OOM后果就是程序crash；可以通俗理解：程序申请内存过大，虚拟机无法满足，然后自杀了。

### 导致OOM问题的原因

为什么会没有内存了呢？原因不外乎有两点：

1）==分配的少了==：比如虚拟机本身可使用的内存（一般通过启动时的VM参数指定）太少。

2）应用用的太多，并且==用完没释放，浪费了==。此时就会造成内存泄露或者内存溢出。

**内存泄露**：==申请使用完的内存没有释放，导致虚拟机不能再次使用该内存，此时这段内存就泄露了，因为申请者不用了，而又不能被虚拟机分配给别人用。==

**内存溢出**：申请的内存超出了JVM能提供的内存大小，此时称之为溢出。

最常见的OOM情况有以下三种：

- java.lang.OutOfMemoryError: ==Java heap space== ------>java堆内存溢出，==此种情况最常见==，一般由于==内存泄露==或者==堆的大小设置不当==引起。==对于内存泄露，需要通过内存监控软件查找程序中的泄露代码==，而==堆大小可以通过虚拟机参数-Xms,-Xmx等修改==。
- java.lang.OutOfMemoryError: ==PermGen space== 或 java.lang.OutOfMemoryError：MetaSpace ------==java方法区，（java8 元空间）溢出==了，一般==出现于大量Class或者jsp页面，或者采用cglib等反射机制的==情况，因为==上述情况会产生大量的Class信息存储于方法区==。此种情况可以通过==更改方法区的大小==来解决，使用类似-XX:PermSize=64m -XX:MaxPermSize=256m的形式修改。另外，==过多的常量尤其是字符串也会导致方法区溢出==。
- java.lang==.StackOverflowError== ------> 不会抛OOM error，但也是比较常见的Java内存溢出。==JAVA虚拟机栈溢出==，一般是由于==程序中存在死循环或者深度递归调用造成的，栈大小设置太小也会出现此种溢出==。可以通过虚拟机参数-Xss来设置栈的大小。

### 排查手段

一般手段是：先==通过内存映像工具对Dump出来的堆转储快照进行分析==，==**重点是确认内存中的对象是否是必要的，也就是要先分清楚到底是出现了内存泄漏还是内存溢出==。**

- 如果是内存泄漏，可进一步通过工具查看==泄漏对象到GC Roots的引用链==。这样就能够找到==泄漏的对象是通过怎么样的路径与GC Roots相关联的导致垃圾回收机制无法将其回收==。掌握了泄漏对象的类信息和GC Roots引用链的信息，就可以比较准确地定位泄漏代码的位置。
- 如果==不存在泄漏==，那么就是内存中的对象确实必须存活着，那么此时就==需要通过虚拟机的堆参数（ -Xmx和-Xms）来适当调大参数==；从代码上检查是否存在某些对象存活时间过长、持有时间过长的情况，尝试减少运行时内存的消耗。

## 实战

> 接下来用一个简单的案例，展示OOM问题排查过程

```java
public class OomDemo {
    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        while(true){
            stringBuilder.append(System.currentTimeMillis());
        }
    }
}
```

- 执行代码时，通过设置JVM参数达到OOM的目的

```shell
 java -Xmx10m -Xms10m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=oom.hprof OomDemo 
```

-XX:+HeapDumpOnOutOfMemoryError和-XX:HeapDumpPath参数分别用于指定==发生OOM是否要导出堆以及导出堆的文件路径==

这个方法也可以通过jmap实时生成

```shell
jmap -dump:format=b,file=$java_pid.hprof     #java_pid为java进程ID
```

以上命令执行后，程序会出现如下错误：

```bash
java.lang.OutOfMemoryError: Java heap space
Dumping heap to oom.out ...
Heap dump file created [3196858 bytes in 0.016 secs]
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        at java.util.Arrays.copyOf(Arrays.java:3332)
        at java.lang.AbstractStringBuilder.ensureCapacityInternal(AbstractStringBuilder.java:124)
        at java.lang.AbstractStringBuilder.append(AbstractStringBuilder.java:700)
        at java.lang.StringBuilder.append(StringBuilder.java:214)
        at jvm.OomDemo.main(OomDemo.java:13)
```

### MAT分析

首先使用MAT打开刚刚导出的hprof文件，选择报告里的泄露嫌疑分析 Leak Suspects Report

![image-20200615114346988](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/20200615120709969.png)

可以看到有一个本地变量，站了总存储的92%，实际占用的是char[]，See stacktrace，可看到该对象所在线程的堆栈信息：

![image-20200615115212703](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/20200615120709969.png)

通过这儿，可以定位到发生OOM的代码段，至此，可根据具体代码具体分析。

![image-20200615115540598](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/20200615120709969.png)