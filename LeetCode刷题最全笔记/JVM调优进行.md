# 前言

JVM调优是争取高薪必须要掌握的一项技能，但是许多程序员在工作中很少遇到去JVM调优的情况，在这篇文章中，我介绍了一些调优工具以及调优的思路，希望对大家有所帮助。

# （一）调优工具

## 1.1 jmap

**查看实例个数以及占用内存信息**，最后一位表示进程id，可以用jps命令查看

```
jmap -histo pid
```

![图片](https://mmbiz.qpic.cn/mmbiz_png/TtgsXZeib3BDrPGNnEwZa40GvsgqNlJ1ZBSHMriaBtmR3oibUmAVC9kia9Py0aTQW3LeYJzK3Tdg3QRsGF22ySG5tw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

num：代表序号 instances：代表实例数量 bytes：代表占用空间大小 classname：代表类的名称

**查看堆的使用情况**：

```
jmap -heap pid
```

通过该命令可以看到垃圾回收器，堆的参数以及堆的使用情况等信息。

![图片](https://mmbiz.qpic.cn/mmbiz_png/TtgsXZeib3BDrPGNnEwZa40GvsgqNlJ1ZeJrHZgyqN06KKk3MrU0KYr5e9GgzvWuNxCxKlZgovIwVGw9ia7dLueg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

==**堆内存dump**==

```
jmap -dump:format=b,file=D:/demo.hprof pid
```

通过命令将JVM运行文件拷贝出来，生成dump文件后，可以用JDK自带的可视化分析工具分析它，命令行下输入

```
jvisualvm
```

自动打开一个可视化窗口，将我们生成的文件装入：

![图片](https://mmbiz.qpic.cn/mmbiz_png/TtgsXZeib3BDrPGNnEwZa40GvsgqNlJ1Zkn4KdE2LPeO6HHzon1rgzlfN46dbvHeFqWvHlzu010vhVtASgxIldw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

所有信息都可以在可视化页面中看到

![图片](https://mmbiz.qpic.cn/mmbiz_png/TtgsXZeib3BDrPGNnEwZa40GvsgqNlJ1Z7WTbJ76ynAIddTKcT7C3QkxUfcgH6q1gs5jmMkic5BrdcVDNx5xP3IA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

通过Jvm参数可设置内存溢出后自动导出Dump文件：

```
-XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\jvm.dump
```

## 1.2 Jstack

通过==Jstack命令加进程的id即可查找死锁==，首先写一段代码制造一个死锁

```
public class DeadLockTest {
    public static Object lock1=new Object();
    public static Object lock2=new Object();

    public static void main(String[] args) {
        new Thread(()->{
            synchronized (lock1){
                try {
                    System.out.println(Thread.currentThread().getName()+"begin");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (lock2){
                    System.out.println(Thread.currentThread().getName()+"end");
                }
            }
        }).start();

        new Thread(()->{
            synchronized (lock2){
                try {
                    System.out.println(Thread.currentThread().getName()+"begin");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (lock1){
                    System.out.println(Thread.currentThread().getName()+"end");
                }
            }
        }).start();
    }
}
```

这段代码会制造第一个线程锁住lock1等待锁lock2，第二个线程锁住lock2等待锁lock1的死锁。

首先通过==jps命令查到进程号，我这里是15820，接着使用jstack命令==：

```
jstack 15820
```

通过该命令可以很轻松地找到死锁。

![图片](https://mmbiz.qpic.cn/mmbiz_png/TtgsXZeib3BDrPGNnEwZa40GvsgqNlJ1Zib3BSSJYArOZoukP3DJd8nVU2miaSDDHWUL3BACwfnwKm4lT7pAeRib6Q/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

## 1.3 Jinfo

==查看正在运行的Java项目参数**查看jvm的参数**：==

```
jinfo -flags pid
```

可以查看到jvm的所有参数

![图片](https://mmbiz.qpic.cn/mmbiz_png/TtgsXZeib3BDrPGNnEwZa40GvsgqNlJ1Zibbs6o0bgBodhHicISC2ldibZlwFkUiblY3lib14jAXiaKujplfe0hhTdSyg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**查看Java系统参数**

```
jinfo -sysprops pid
```

可以查看到JDK版本、位置等信息。

## 1.==4 jvisualvm==

可视化的JVM监控工具，上面所讲到的这些命令，都可以直接在jvisualvm中看到可视化数据，但是这个工具在生产环境中需要谨慎使用，因为会占用一定资源。

# （三）Jstat

jstat其实也是JVM自带的一个调优工具，但是我这里把他单独拿出来是因为正式对==JVM调优中，这条命令是最常用的。==

==jstat命令可以查看堆内存各部分的使用量，以及加载类的数量等==。

## 2.1 ==垃圾回收统计==

评估内存使用及GC压力情况

```
jstat -gc pid
```

我执行这段代码后，出现了一串数据

![图片](https://mmbiz.qpic.cn/mmbiz_png/TtgsXZeib3BDrPGNnEwZa40GvsgqNlJ1Z1CcSwLeL6VVFhQyoZHSFjGkmPGVG19HBf6rQYM5SRyf01FL8SgH5ag/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

所有参数的解释都放在下面了，结合垃圾回收一起看，就能懂了。

```
S0C:第一个Survivor大小（kb）
S1C:第二个Survivor大小
S0U:第一个Survivor区的使用大小
S1U:第二个Survivor区的使用大小
EC:eden区大小
EU:eden区的使用大小
OC:老年代大小
OU:老年代使用大小
MC:方法区大小（元空间）
MU:方法区使用大小
CCSC:压缩类空间大小
CCSU:压缩类空间使用大小
YGC:YoungGC次数
YGCT:YoungGC时间（s）
FGC:FullGC次数
FGCT:FullGC时间（s）
GCT:总的GC时间（s）
```

## 2.2 ==垃圾回收比例统计==

给出各个区的使用比例：

```
jstat -gcutil pid
```

参数介绍：

```
S0：第一个Survivor区当前使用比例
S1：第二个Survivor区当前使用比例
E：eden区使用比例
O：老年代使用比例
M：元数据区使用比例
CCS：压缩使用比例
YGC：YoungGC次数
FGC：FullGC次数
FGCT：FullGC消耗时间
GCT：垃圾回收消耗总时间
```

## 2.3 堆内存统计

统计堆内存的使用情况

```
jstat -gccapacity pid
```

介绍一下参数：

```
NGCMN：新生代最小容量
NGCMX：新生代最大容量
NGC：当前新生代容量
S0C：第一个Survivor区大小
S1C：第二个Survivor区的大小
EC：eden区的大小
OGCMN：老年代最小容量
OGCMX：老年代最大容量
OGC：当前老年代大小
OC：当前老年代大小
MCMN：最小元数据容量
MCMX：最大元数据容量
MC：当前元数据空间大小
CCSMN：最小压缩类空间大小
CCSMX：最大压缩类空间大小
CCSC：当前压缩类空间大小
YGC：YoungGC次数
FGC：FullGC次数
```

## 2.4 新生代垃圾回收统计

统计新生代垃圾回收的数据

```
jstat -gcnew pid
```

介绍参数：

```
S0C：第一个Survivor区的大小
S1C：第二个Survivor区的大小
S0U：第一个Survivor区的使用大小
S1U：第二个Survivor区的使用大小
TT：对象在新生代存活的次数
MTT：对象在新生代存活的最大次数
DSS：期望的幸存区大小
EC：eden区的大小
EU：eden区的使用大小
YGC：年轻代垃圾回收次数
YGCT：年轻代垃圾回收消耗时间
```

## 2.5 新生代内存统计

统计新生代内存的使用情况

```
jstat -gcnewcapacity pid
```

介绍参数：

```
NGCMN：新生代最小容量
NGCMX：新生代最大容量
NGC：当前新生代容量
S0CMX：第一个Survivor区最大容量
S0C：第一个Survivor区大小
S1CMX：第二个Survivor区最大容量
S1C：第二个Survivor区大小
ECMX：eden区最大容量
EC：当前eden区大小
YGC：年轻代垃圾回收次数
FGC：老年代回收次数
```

## 2.6 老年代垃圾回收统计

统计老年代垃圾回收的数据

```
jstat -gcold pid
```

参数介绍：

```
MC：方法区大小
MU：方法区使用大小
CCSC：压缩类空间大小
CCSU：压缩类空间使用大小
OC：老年代大小
OU：老年代使用大小
YGC：年轻代垃圾回收次数
FGC：老年代垃圾回收次数
FGCT：老年代垃圾回收消耗时间
GCT：垃圾回收消耗总时间
```

## 2.7 老年代内存统计

统计老年代内存的使用情况

```
jstat -gcoldcapacity pid
```

参数介绍：

```
OGCMN：老年代最小容量
OGCMX：老年代最大容量
OGC：当前老年代大小
OC：老年代大小
YGC：年轻代垃圾回收次数
FGC：老年代垃圾回收次数
FGCT：老年代垃圾回收消耗时间
GCT：垃圾回收消耗总时间
```

## 2.8 元空间统计

统计元数据空间的情况

```
jstat -gcmetacapacity pid
```

参数介绍

```
MCMN：最小元数据容量
MCMX：最大元数据容量
MC：当前元数据空间大小
CCSMN：最小压缩类空间大小
CCSMX：最大压缩类空间大小
CCSC：当前压缩类空间大小
YGC：年轻代垃圾回收次数
FGC：老年代垃圾回收次数
FGCT：老年代垃圾回收消耗时间
GCT：垃圾回收消耗总时间
```

# （三）JVM运行情况分析思路

上面的这些知识调优的工具，我们除了要了解这些工具的含义之外，还需要知道这些如何使用这些工具去分析JVM的运行情况。

## 3.1 分析年轻代对象增长速率

==我们都知道新对象的产生会在eden区，因此我们可以通过下面的命令：==

```
jstat -gc pid 5000 10
```

==每5秒执行一次，执行10次，然后观察这50秒内eden区增加的趋势，即可知道年轻代对象增长的速率。==

## 3.2 分析YGC情况

==jstat -gc命令中展示了YGCT和YGC，通过YGCT/YGC可以算出YGC的平均耗时。通过每隔一段时间输出一次我们也能观察出YGC的频率。==

## 3.3 YGC后对象存活情况

==每次YGC过后，eden区数量会大幅减少，而survivor和老年代的数量会增加，这样我们就能计算出每次每次YGC后存活的对象数量，以及推断老年代对象增长的速率。==

## 3.4 分析FGC情况

分析FGC的思路和分析YGC一样，通过增长速率推断FGC频率，通过计算FGCT/FGC计算平均每次FGC耗时。

==优化思路在于：**尽量减少FGC的次数**，避免频繁FGC对JVM性能的影响，因此尽量别让对象进入老年代，也就是每次YGC后存活的对象尽量少于Survivor的50%。==

# （四）GC日志

==有时候系统突然运行缓慢无法找到原因时，我们可以把GC日志都打印出来，然后去分析gc日志中的关键性指标。==

通过增加JVM参数的方式打印gc日志：

```
-XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -Xloggc:./gc.log
```

GC日志中会把每一次GC的情况都打印出来，因此所有的GC都可以被分析到。

在GC日志中，有关GC日志的参数，我接下来也会专门写一篇来介绍，这样更加能让大家更加清楚一些。

我们还可以用一些工具比如**GCeasy**帮助我们去分析GC日志的情况。

# （五）总结

整篇文章主要从工具出发，提供给大家GC调优的一个思路，但是GC调优又是一个很需要经验的事情。需要对垃圾回收器、Java内存分代等有很清晰的认知。对于当前工作环境中无法接触到调优的程序员们来说，需要做的就是把工具和思路牢牢掌握，等真正遇到的时候也不会一头雾水。我是鱼仔，我们下期再见！