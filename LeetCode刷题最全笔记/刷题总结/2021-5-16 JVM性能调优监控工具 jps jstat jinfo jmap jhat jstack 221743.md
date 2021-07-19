# JVM性能调优监控工具 jps jstat jinfo jmap jhat jstack

 发表于 2020-03-28  更新于 2020-07-26  分类于 [jvm](http://weikeqin.com/categories/jvm/)  阅读次数：  阅读次数： 348

> 最近测试环境总是莫名的出问题，然后部署服务的容器里除了服务打印的日志，没有其它信息，想看看是什么原因导致服务很卡，是不是有哪块代码占用内存高。
> 代码执行缓慢、OutOfMemoryError，内存不足、内存泄露、线程死锁、锁争用（Lock Contention）、Java进程消耗CPU过高 都可以使用JDK的命令行工具排查。



# (1) JDK的命令行工具

> jps、jstat、jinfo、jmap、jhat、jstack、hprof
> jps : 虚拟机进程状况工具
> jstat : 虚拟机统计信息监视工具
> jinfo : Java 配置信息工具
> jmap : Java 内存映像工具
> jhat : 虚拟机堆转储快照分析工具
> jstack : Java 堆栈跟踪工具

> jps将打印所有正在运行的 Java 进程。
> jstat允许用户查看目标 Java 进程的类加载、即时编译以及垃圾回收相关的信息。它常用于检测垃圾回收问题以及内存泄漏问题。
> jmap允许用户统计目标 Java 进程的堆中存放的 Java 对象，并将它们导出成二进制文件。
> jinfo将打印目标 Java 进程的配置参数，并能够改动其中 manageabe 的参数。
> jstack将打印目标 Java 进程中各个线程的栈轨迹、线程状态、锁状况等信息。它还将自动检测死锁。
> jcmd则是一把瑞士军刀，可以用来实现前面除了jstat之外所有命令的功能。

Java虚拟机的监控及诊断工具-GUI

> JConsole : Java 监视与管理控制台
> VisualVM : 多合一故障处理工具
> eclipse MAT
> JMC
> JITWatch



# (2) 虚拟机进程状况工具 jps (JVM Process Status Tool)

> jps主要用来输出JVM中运行的进程状态信息。
> jps命令使用Java启动器来查找传递给main方法的类名和参数。

```
[wkq@VM_77_25_centos ~]$ jps -help
usage: jps [-help]
       jps [-q] [-mlvV] [<hostid>]

Definitions:
    <hostid>:      <hostname>[:<port>]
[wkq@VM_77_25_centos ~]$
```

## (2.1) jps命令

> jps命令用于输出JVM中运行的进程状态信息。可以获取到进程的pid、全限定名、传入main方法的参数、传入JVM的参数 等。

> 语法格式
>
> ```
> jps [ options ] [ hostid ]
> ```

如果不指定hostid就默认为当前主机或服务器。

详细信息见 `man jps`

### (2.1.1) jps 不加参数

> 在本地主机上搜索检测到的JVM。

```
[wkq@VM_77_25_centos ~]$ jps
14916 Jps
13050 Elasticsearch
[wkq@VM_77_25_centos ~]$
[wkq@VM_77_25_centos ~]$
```



### (2.1.2) jps -q

> 不输出类名、Jar名和传入main方法的参数

```
[wkq@VM_77_25_centos ~]$ jps -q
15059
13050
[wkq@VM_77_25_centos ~]$
```

### (2.1.3) jps -m

> 显示传递给main方法的参数。对于嵌入式JVM，输出可能为null。

```
[wkq@VM_77_25_centos ~]$ jps -m
15092 Jps -m
13050 Elasticsearch -d
[wkq@VM_77_25_centos ~]$
```

### (2.1.4) jps -l

> 输出main类或Jar的全限定名

```
[wkq@VM_77_25_centos ~]$ jps -l
15139 sun.tools.jps.Jps
13050 org.elasticsearch.bootstrap.Elasticsearch
[wkq@VM_77_25_centos ~]$
```

### (2.1.5) jps -v

> 显示传递给JVM的参数。

```
[wkq@VM_77_25_centos ~]$ jps -v
15157 Jps -Denv.class.path=.:/home/wkq/software/jdk1.8.0_172/lib/dt.jar:/home/wkq/software/jdk1.8.0_172/lib/tools.jar -Dapplication.home=/home/wkq/software/jdk1.8.0_172 -Xms8m
13050 Elasticsearch -Xms1g -Xmx1g -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -Des.networkaddress.cache.ttl=60 -Des.networkaddress.cache.negative.ttl=10 -XX:+AlwaysPreTouch -Xss1m -Djava.awt.headless=true -Dfile.encoding=UTF-8 -Djna.nosys=true -XX:-OmitStackTraceInFastThrow -Dio.netty.noUnsafe=true -Dio.netty.noKeySetOptimization=true -Dio.netty.recycler.maxCapacityPerThread=0 -Dlog4j.shutdownHookEnabled=false -Dlog4j2.disable.jmx=true -Djava.io.tmpdir=/tmp/elasticsearch-5035355569386013893 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=data -XX:ErrorFile=logs/hs_err_pid%p.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+PrintGCApplicationStoppedTime -Xloggc:logs/gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=32 -XX:GCLogFileSize=64m -Des.path.home=/home/wkq/software/elasticsearch-6.6.2 -Des.path.conf=/home/wkq/software/elasticsearch-6.6.2/config -Des.distribution.flavor=default -Des.distribution.type=tar
[wkq@VM_77_25_centos ~]$
```

### (2.1.6) jps -V

> 禁止输出类名，JAR全限定名和传递给main方法的参数的输出，从而产生仅本地JVM标识符的列表。

```
[wkq@VM_77_25_centos ~]$ jps -V
15204 Jps
13050 Elasticsearch
[wkq@VM_77_25_centos ~]$
```

## (2.2) 常用jps命令

### (2.2.1) jps -lm

> 显示 pid、全限定名、传递给main方法的参数

```
[wkq@VM_77_25_centos ~]$ jps -lm
17539 sun.tools.jps.Jps -lm
13050 org.elasticsearch.bootstrap.Elasticsearch -d
[wkq@VM_77_25_centos ~]$
[wkq@VM_77_25_centos ~]$
```

### (2.2.2) jps -lvm

> 显示 pid、全限定名、传递给main方法的参数、传递给JVM的参数

```
[wkq@VM_77_25_centos ~]$ jps -lvm
13050 org.elasticsearch.bootstrap.Elasticsearch -d -Xms1g -Xmx1g -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -Des.networkaddress.cache.ttl=60 -Des.networkaddress.cache.negative.ttl=10 -XX:+AlwaysPreTouch -Xss1m -Djava.awt.headless=true -Dfile.encoding=UTF-8 -Djna.nosys=true -XX:-OmitStackTraceInFastThrow -Dio.netty.noUnsafe=true -Dio.netty.noKeySetOptimization=true -Dio.netty.recycler.maxCapacityPerThread=0 -Dlog4j.shutdownHookEnabled=false -Dlog4j2.disable.jmx=true -Djava.io.tmpdir=/tmp/elasticsearch-5035355569386013893 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=data -XX:ErrorFile=logs/hs_err_pid%p.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+PrintGCApplicationStoppedTime -Xloggc:logs/gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=32 -XX:GCLogFileSize=64m -Des.path.home=/home/wkq/software/elasticsearch-6.6.2 -Des.path.conf=/home/wkq/software/elasticsearch-6.6.2/config -Des.distribution.flavor=default -Des.distribution.type=tar
17567 sun.tools.jps.Jps -lvm -Denv.class.path=.:/home/wkq/software/jdk1.8.0_172/lib/dt.jar:/home/wkq/software/jdk1.8.0_172/lib/tools.jar -Dapplication.home=/home/wkq/software/jdk1.8.0_172 -Xms8m
[wkq@VM_77_25_centos ~]$
```



# (3) 虚拟机统计信息监视工具 jstat (JVM Statics Monitoring Tool)

> jstat是用于监视虚拟机各种运行状态信息的命令行工具。

## (3.1) jstat命令

> 语法格式
>
> ```
> jstat [ generalOption | outputOptions vmid [ interval[s|ms] [ count ] ]
> ```

详细信息见 `man jstat`

### (3.1.1) jstat命令

```
[wkq@VM_77_25_centos ~]$ jstat -help
Usage: jstat -help|-options
       jstat -<option> [-t] [-h<lines>] <vmid> [<interval> [<count>]]

Definitions:
  <option>      An option reported by the -options option
  <vmid>        Virtual Machine Identifier. A vmid takes the following form:
                     <lvmid>[@<hostname>[:<port>]]
                Where <lvmid> is the local vm identifier for the target
                Java virtual machine, typically a process id; <hostname> is
                the name of the host running the target Java virtual machine;
                and <port> is the port number for the rmiregistry on the
                target host. See the jvmstat documentation for a more complete
                description of the Virtual Machine Identifier.
  <lines>       Number of samples between header lines.
  <interval>    Sampling interval. The following forms are allowed:
                    <n>["ms"|"s"]
                Where <n> is an integer and the suffix specifies the units as
                milliseconds("ms") or seconds("s"). The default units are "ms".
  <count>       Number of samples to take before terminating.
  -J<flag>      Pass <flag> directly to the runtime system.
[wkq@VM_77_25_centos ~]$
[wkq@VM_77_25_centos ~]$ jstat -options
-class
-compiler
-gc
-gccapacity
-gccause
-gcmetacapacity
-gcnew
-gcnewcapacity
-gcold
-gcoldcapacity
-gcutil
-printcompilation
[wkq@VM_77_25_centos ~]$
```



### (3.1.1) jstat -class 13050

```
[wkq@VM_77_25_centos ~]$ jstat -class 13050
Loaded  Bytes  Unloaded  Bytes     Time
 15177 27005.8       41    49.7      24.44
[wkq@VM_77_25_centos ~]$
```

### (3.1.2) jstat -compiler 13050

```
[wkq@VM_77_25_centos ~]$  jstat -compiler 13050
Compiled Failed Invalid   Time   FailedType FailedMethod
    7469      0       0    43.18          0
[wkq@VM_77_25_centos ~]$
```

### (3.1.3) jstat -gc 13050

```
[wkq@VM_77_25_centos ~]$ jstat -gc 13050
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
8512.0 8512.0  0.0    24.5  68160.0   6380.0   963392.0   61694.4   77732.0 71385.7 11984.0 9862.8     69    2.112   6      0.286    2.397
[wkq@VM_77_25_centos ~]$
```

| 参数 | 描述                                              | 原文                                                  |
| ---- | ------------------------------------------------- | ----------------------------------------------------- |
| S0C  | 年轻代中第一个survivor 的容量 (kB)                | Current survivor space 0 capacity (kB).               |
| S1C  | 年轻代中第二个survivor 的容量 (kB)                | Current survivor space 1 capacity (kB).               |
| S0U  | 年轻代中第一个survivor 已使用空间 (kB)            | Survivor space 0 utilization (kB).                    |
| S1U  | 年轻代中第二个survivor已使用空间 (kB)             | Survivor space 1 utilization (kB).                    |
| EC   | 年轻代中 Eden space 的容量 (kB)                   | Current eden space capacity (kB).                     |
| EU   | 年轻代中Eden space 目前已使用空间 (kB)            | Eden space utilization (kB).                          |
| OC   | Old space的容量 (kB)                              | Current old space capacity (kB).                      |
| OU   | Old space 已使用空间 (kB)                         | Old space utilization (kB).                           |
| MC   | Metaspace 的容量 (kB)                             | Metaspace capacity (kB).                              |
| MU   | Metaspace 已使用空间 (kB)                         | Metacspace utilization (kB).                          |
| CCSC | Compressed class spcage 容量 (kB)                 | Compressed class space capacity (kB).                 |
| CCSU | Compressed class spcage 已使用空间 (kB)           | Compressed class space used (kB).                     |
| YGC  | 从应用程序启动到采样时年轻代中gc次数              | Number of young generation garbage collection events. |
| YGCT | 从应用程序启动到采样时年轻代中gc所用时间 (s)      | Young generation garbage collection time.             |
| FGC  | 从应用程序启动到采样时old代 (全gc) gc次数         | Number of full GC events.                             |
| FGCT | 从应用程序启动到采样时old代 (全gc) gc所用时间 (s) | Full garbage collection time.                         |
| GCT  | 从应用程序启动到采样时gc用的总时间 (s)            | Total garbage collection time.                        |

### (3.1.4) jstat -gccapacity 13050

```
[wkq@VM_77_25_centos ~]$ jstat -gccapacity 13050
 NGCMN    NGCMX     NGC     S0C   S1C       EC      OGCMN      OGCMX       OGC         OC       MCMN     MCMX      MC     CCSMN    CCSMX     CCSC    YGC    FGC
 85184.0  85184.0  85184.0 8512.0 8512.0  68160.0   963392.0   963392.0   963392.0   963392.0      0.0 1116160.0  77732.0      0.0 1048576.0  11984.0     69     6
[wkq@VM_77_25_centos ~]$
```

### (3.1.5) jstat -gccause 13050

```
[wkq@VM_77_25_centos ~]$ jstat -gccause 13050
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT    LGCC                 GCC
  0.00   0.29  11.90   6.40  91.84  82.30     69    2.112     6    0.286    2.397 Allocation Failure   No GC
[wkq@VM_77_25_centos ~]$
```

### (3.1.6) jstat -gcmetacapacity 13050

```
[wkq@VM_77_25_centos ~]$ jstat -gcmetacapacity 13050
   MCMN       MCMX        MC       CCSMN      CCSMX       CCSC     YGC   FGC    FGCT     GCT
       0.0  1116160.0    77732.0        0.0  1048576.0    11984.0    69     6    0.286    2.397
[wkq@VM_77_25_centos ~]$
```

### (3.1.7) jstat -gcnew 13050

```
[wkq@VM_77_25_centos ~]$ jstat -gcnew 13050
 S0C    S1C    S0U    S1U   TT MTT  DSS      EC       EU     YGC     YGCT
8512.0 8512.0    0.0   24.5  6   6 4256.0  68160.0  13229.3     69    2.112
[wkq@VM_77_25_centos ~]$
```

### (3.1.8) jstat -gcnewcapacity 13050

```
[wkq@VM_77_25_centos ~]$ jstat -gcnewcapacity 13050
  NGCMN      NGCMX       NGC      S0CMX     S0C     S1CMX     S1C       ECMX        EC      YGC   FGC
   85184.0    85184.0    85184.0   8512.0   8512.0   8512.0   8512.0    68160.0    68160.0    69     6
[wkq@VM_77_25_centos ~]$
```

### (3.1.9) jstat -gcold 13050

```
[wkq@VM_77_25_centos ~]$ jstat -gcold 13050
   MC       MU      CCSC     CCSU       OC          OU       YGC    FGC    FGCT     GCT
 77732.0  71385.7  11984.0   9862.8    963392.0     61694.4     69     6    0.286    2.397
```

### (3.1.10) jstat -gcoldcapacity 13050

```
[wkq@VM_77_25_centos ~]$ jstat -gcoldcapacity 13050
   OGCMN       OGCMX        OGC         OC       YGC   FGC    FGCT     GCT
   963392.0    963392.0    963392.0    963392.0    69     6    0.286    2.397
[wkq@VM_77_25_centos ~]$
```

### (3.1.11) jstat -gcutil 13050

> 垃圾回收状态摘要

```
[wkq@VM_77_25_centos ~]$ jstat -gcutil 13050
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT
  0.00   0.29  24.37   6.40  91.84  82.30     69    2.112     6    0.286    2.397
[wkq@VM_77_25_centos ~]$
```

| 参数 | 描述                                            | 原文                                                         |
| ---- | ----------------------------------------------- | ------------------------------------------------------------ |
| S0   | 年轻代中第一个survivor 已使用的占当前容量百分比 | Survivor space 0 utilization as a percentage of the space’s current capacity. |
| S1   | 年轻代中第二个survivor 已使用的占当前容量百分比 | Survivor space 1 utilization as a percentage of the space’s current capacity. |
| E    | Eden space 中Eden 已使用的占当前容量百分比      | Eden space utilization as a percentage of the space’s current capacity. |
| O    | Old space 已使用的占当前容量百分比              | Old space utilization as a percentage of the space’s current capacity. |
| M    | Metaspace 已使用的占当前容量百分比              | Metaspace utilization as a percentage of the space’s current capacity. |
| CCS  | Compressed class 空间利用率                     | Compressed class space utilization as a percentage.          |
| YGC  | 从应用程序启动到采样时 young generation gc次数  | Number of young generation GC events.                        |
| YGCT | 从应用程序启动到采样时Young gc所用时间(s)       | Young generation garbage collection time.                    |
| FGC  | 从应用程序启动到采样时Full gc次数               | Number of full GC events.                                    |
| FGCT | 从应用程序启动到采样时Full gc所用时间(s)        | Full garbage collection time.                                |
| GCT  | 从应用程序启动到采样时gc用的总时间(s)           | Total garbage collection time.                               |

### (3.1.12) jstat -printcompilation 13050

```
[wkq@VM_77_25_centos ~]$ jstat -printcompilation 13050
Compiled  Size  Type Method
    7469     63    1 org/apache/logging/log4j/spi/AbstractLogger trace
[wkq@VM_77_25_centos ~]$
```



## (3.2) 常用jstat命令

### (3.2.1) jstat -gc -h5 -t 13050 1000 10

> 分析进程id为13050的gc情况，每隔1000ms打印一次记录，打印10次停止，每5行后打印指标头部
> -gc 查看gc情况
> -h5 每5行后打印指标头部
> -t 进程启动时间
> 13050 进程id 也就是linux的pid
> 1000 每隔1000ms打印一次
> 10 共打印10行

```
[wkq@VM_77_25_centos ~]$ jstat -gc -h5 -t 13050  1000 10
Timestamp        S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
       217492.5 8512.0 8512.0  0.0    77.3  68160.0  13154.8   963392.0   60728.2   77732.0 71386.5 11984.0 9862.9    133    2.921   8      0.947    3.868
       217493.7 8512.0 8512.0  0.0    77.3  68160.0  13154.8   963392.0   60728.2   77732.0 71386.5 11984.0 9862.9    133    2.921   8      0.947    3.868
       217494.7 8512.0 8512.0  0.0    77.3  68160.0  14055.9   963392.0   60728.2   77732.0 71386.5 11984.0 9862.9    133    2.921   8      0.947    3.868
       217495.7 8512.0 8512.0  0.0    77.3  68160.0  14055.9   963392.0   60728.2   77732.0 71386.5 11984.0 9862.9    133    2.921   8      0.947    3.868
       217496.7 8512.0 8512.0  0.0    77.3  68160.0  14055.9   963392.0   60728.2   77732.0 71386.5 11984.0 9862.9    133    2.921   8      0.947    3.868
Timestamp        S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
       217497.7 8512.0 8512.0  0.0    77.3  68160.0  14055.9   963392.0   60728.2   77732.0 71386.5 11984.0 9862.9    133    2.921   8      0.947    3.868
       217498.7 8512.0 8512.0  0.0    77.3  68160.0  14055.9   963392.0   60728.2   77732.0 71386.5 11984.0 9862.9    133    2.921   8      0.947    3.868
       217499.7 8512.0 8512.0  0.0    77.3  68160.0  14055.9   963392.0   60728.2   77732.0 71386.5 11984.0 9862.9    133    2.921   8      0.947    3.868
       217500.7 8512.0 8512.0  0.0    77.3  68160.0  14055.9   963392.0   60728.2   77732.0 71386.5 11984.0 9862.9    133    2.921   8      0.947    3.868
       217501.7 8512.0 8512.0  0.0    77.3  68160.0  14068.0   963392.0   60728.2   77732.0 71386.5 11984.0 9862.9    133    2.921   8      0.947    3.868
[wkq@VM_77_25_centos ~]$
```

> 我们可以比较 Java 进程的启动时间以及总 GC 时间（GCT 列），或者两次测量的间隔时间以及总 GC 时间的增量，来得出 GC 时间占运行时间的比例。如果该比例超过 20%，则说明目前堆的压力较大；如果该比例超过 90%，则说明堆里几乎没有可用空间，随时都可能抛出 OOM 异常。

### (3.2.2) jstat -gcutil -h5 -t 13050 1000 10

> 分析进程id为13050的gcutil情况，每隔1000ms打印一次记录，打印15次停止，每5行后打印指标头部
> -gcutil 查看gcutil情况
> -h5 每5行后打印指标头部
> -t 进程启动时间
> 13050 进程id 也就是linux的pid
> 1000 每隔1000ms打印一次
> 10 共打印10行

```
[wkq@VM_77_25_centos ~]$ jstat -gcutil -h5 -t 13050  1000 10
Timestamp         S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT
       217569.9   0.00   0.91  23.55   6.30  91.84  82.30    133    2.921     8    0.947    3.868
       217570.9   0.00   0.91  23.55   6.30  91.84  82.30    133    2.921     8    0.947    3.868
       217571.9   0.00   0.91  23.55   6.30  91.84  82.30    133    2.921     8    0.947    3.868
       217572.9   0.00   0.91  23.55   6.30  91.84  82.30    133    2.921     8    0.947    3.868
       217573.9   0.00   0.91  23.55   6.30  91.84  82.30    133    2.921     8    0.947    3.868
Timestamp         S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT
       217574.9   0.00   0.91  23.55   6.30  91.84  82.30    133    2.921     8    0.947    3.868
       217575.9   0.00   0.91  23.55   6.30  91.84  82.30    133    2.921     8    0.947    3.868
       217576.9   0.00   0.91  23.55   6.30  91.84  82.30    133    2.921     8    0.947    3.868
       217577.9   0.00   0.91  23.55   6.30  91.84  82.30    133    2.921     8    0.947    3.868
       217578.9   0.00   0.91  23.55   6.30  91.84  82.30    133    2.921     8    0.947    3.868
[wkq@VM_77_25_centos ~]$
```





# (4) Java配置信息工具 jinfo (Java Configuration Info )

> jinfo是实时查看和调整虚拟机各项参数的工具

## (4.1) jinfo命令

语法格式

```
jinfo [ option ] pid
```



```
[wkq@VM_77_25_centos ~]$ jinfo -help
Usage:
    jinfo [option] <pid>
        (to connect to running process)
    jinfo [option] <executable <core>
        (to connect to a core file)
    jinfo [option] [server_id@]<remote server IP or hostname>
        (to connect to remote debug server)

where <option> is one of:
    -flag <name>         to print the value of the named VM flag
    -flag [+|-]<name>    to enable or disable the named VM flag
    -flag <name>=<value> to set the named VM flag to the given value
    -flags               to print VM flags
    -sysprops            to print Java system properties
    <no option>          to print both of the above
    -h | -help           to print this help message
[wkq@VM_77_25_centos ~]$
[wkq@VM_77_25_centos ~]$ jinfo -option
Usage:
    jinfo [option] <pid>
        (to connect to running process)
    jinfo [option] <executable <core>
        (to connect to a core file)
    jinfo [option] [server_id@]<remote server IP or hostname>
        (to connect to remote debug server)

where <option> is one of:
    -flag <name>         to print the value of the named VM flag
    -flag [+|-]<name>    to enable or disable the named VM flag
    -flag <name>=<value> to set the named VM flag to the given value
    -flags               to print VM flags
    -sysprops            to print Java system properties
    <no option>          to print both of the above
    -h | -help           to print this help message
[wkq@VM_77_25_centos ~]$
```

如果遇到 `Error attaching to process: sun.jvm.hotspot.debugger.DebuggerException: Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for : Operation not permitted`，执行 `echo 0 | sudo tee /proc/sys/kernel/yama/ptrace_scope`命令即可

### (4.1.1) jinfo pid

```
[wkq@VM_77_25_centos ~]$ jinfo 13050
Attaching to process ID 13050, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.172-b11
Java System Properties:

jna.platform.library.path = /usr/lib64:/lib64:/usr/lib:/lib:/usr/lib64/dyninst:/usr/lib64/mysql
java.runtime.name = Java(TM) SE Runtime Environment
sun.boot.library.path = /home/wkq/software/jdk1.8.0_172/jre/lib/amd64
java.vm.version = 25.172-b11
es.path.home = /home/wkq/software/elasticsearch-6.6.2
log4j.shutdownHookEnabled = false
java.vendor.url = http://java.oracle.com/
java.vm.vendor = Oracle Corporation
path.separator = :
file.encoding.pkg = sun.io
java.vm.name = Java HotSpot(TM) 64-Bit Server VM
jna.loaded = true
sun.os.patch.level = unknown
user.country = US
sun.java.launcher = SUN_STANDARD
es.networkaddress.cache.negative.ttl = 10
jna.nosys = true
java.vm.specification.name = Java Virtual Machine Specification
user.dir = /home/wkq/software/elasticsearch-6.6.2
java.runtime.version = 1.8.0_172-b11
java.awt.graphicsenv = sun.awt.X11GraphicsEnvironment
java.endorsed.dirs = /home/wkq/software/jdk1.8.0_172/jre/lib/endorsed
os.arch = amd64
java.io.tmpdir = /tmp/elasticsearch-5035355569386013893
line.separator =

es.networkaddress.cache.ttl = 60
es.logs.node_name = elasticsearch_001_data
java.vm.specification.vendor = Oracle Corporation
os.name = Linux
io.netty.noKeySetOptimization = true
sun.jnu.encoding = ANSI_X3.4-1968
jnidispatch.path = /tmp/elasticsearch-5035355569386013893/jna-117789/jna8829728915470247279.tmp
java.library.path = /usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib
sun.nio.ch.bugLevel =
es.logs.cluster_name = elasticsearch_test
java.specification.name = Java Platform API Specification
java.class.version = 52.0
sun.management.compiler = HotSpot 64-Bit Tiered Compilers
os.version = 3.10.0-957.21.3.el7.x86_64
user.home = /home/wkq
user.timezone = Asia/Shanghai
java.awt.printerjob = sun.print.PSPrinterJob
file.encoding = UTF-8
java.specification.version = 1.8
es.distribution.type = tar
io.netty.recycler.maxCapacityPerThread = 0
user.name = wkq
es.logs.base_path = /home/wkq/software/elasticsearch-6.6.2/logs
java.class.path =  
es.path.conf = /home/wkq/software/elasticsearch-6.6.2/config
java.vm.specification.version = 1.8
java.home = /home/wkq/software/jdk1.8.0_172/jre
sun.java.command = org.elasticsearch.bootstrap.Elasticsearch -d
sun.arch.data.model = 64
io.netty.noUnsafe = true
user.language = en
java.specification.vendor = Oracle Corporation
awt.toolkit = sun.awt.X11.XToolkit
java.vm.info = mixed mode
java.version = 1.8.0_172
java.ext.dirs = /home/wkq/software/jdk1.8.0_172/jre/lib/ext:/usr/java/packages/lib/ext
sun.boot.class.path =  
java.awt.headless = true
java.vendor = Oracle Corporation
file.separator = /
java.vendor.url.bug = http://bugreport.sun.com/bugreport/
es.distribution.flavor = default
sun.io.unicode.encoding = UnicodeLittle
sun.cpu.endian = little
log4j2.disable.jmx = true
sun.cpu.isalist =

VM Flags:
Non-default VM flags: -XX:+AlwaysPreTouch -XX:CICompilerCount=2 -XX:CMSInitiatingOccupancyFraction=75 -XX:ErrorFile=null -XX:GCLogFileSize=67108864 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=null -XX:InitialHeapSize=1073741824 -XX:MaxHeapSize=1073741824 -XX:MaxNewSize=87228416 -XX:MaxTenuringThreshold=6 -XX:MinHeapDeltaBytes=196608 -XX:NewSize=87228416 -XX:NumberOfGCLogFiles=32 -XX:OldPLABSize=16 -XX:OldSize=986513408 -XX:-OmitStackTraceInFastThrow -XX:+PrintGC -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintTenuringDistribution -XX:ThreadStackSize=1024 -XX:+UseCMSInitiatingOccupancyOnly -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:+UseGCLogFileRotation -XX:+UseParNewGC
Command line:  -Xms1g -Xmx1g -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -Des.networkaddress.cache.ttl=60 -Des.networkaddress.cache.negative.ttl=10 -XX:+AlwaysPreTouch -Xss1m -Djava.awt.headless=true -Dfile.encoding=UTF-8 -Djna.nosys=true -XX:-OmitStackTraceInFastThrow -Dio.netty.noUnsafe=true -Dio.netty.noKeySetOptimization=true -Dio.netty.recycler.maxCapacityPerThread=0 -Dlog4j.shutdownHookEnabled=false -Dlog4j2.disable.jmx=true -Djava.io.tmpdir=/tmp/elasticsearch-5035355569386013893 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=data -XX:ErrorFile=logs/hs_err_pid%p.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+PrintGCApplicationStoppedTime -Xloggc:logs/gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=32 -XX:GCLogFileSize=64m -Des.path.home=/home/wkq/software/elasticsearch-6.6.2 -Des.path.conf=/home/wkq/software/elasticsearch-6.6.2/config -Des.distribution.flavor=default -Des.distribution.type=tar

[wkq@VM_77_25_centos ~]$
```

### (4.1.2) jinfo -flag MaxMetaspaceSize pid

```
[wkq@VM_77_25_centos ~]$ jinfo -flag MaxMetaspaceSize 13050
-XX:MaxMetaspaceSize=18446744073709547520
[wkq@VM_77_25_centos ~]$
```

### (4.1.3) jinfo -flag ThreadStackSize pid

```
[wkq@VM_77_25_centos ~]$ jinfo -flag ThreadStackSize 13050
-XX:ThreadStackSize=1024
[wkq@VM_77_25_centos ~]$
```

### (4.1.4) jinfo -flag MaxNewSize pid

```
[wkq@VM_77_25_centos ~]$ jinfo -flag MaxNewSize 13050
-XX:MaxNewSize=87228416
[wkq@VM_77_25_centos ~]$
```

### (4.1.5) jinfo -flag CMSInitiatingOccupancyFraction pid

```
[wkq@VM_77_25_centos ~]$ jinfo -flag CMSInitiatingOccupancyFraction 13050
-XX:CMSInitiatingOccupancyFraction=75
[wkq@VM_77_25_centos ~]$
```

### (4.1.6) 查看所有JVM参数 java -XX:+PrintFlagsInitial

```
[wkq@VM_77_25_centos ~]$ java -XX:+PrintFlagsInitial
[Global flags]
    uintx AdaptiveSizeDecrementScaleFactor          = 4                                   {product}
    uintx AdaptiveSizeMajorGCDecayTimeScale         = 10                                  {product}

...

     intx WorkAroundNPTLTimedWaitHang               = 1                                   {product}
    uintx YoungGenerationSizeIncrement              = 20                                  {product}
    uintx YoungGenerationSizeSupplement             = 80                                  {product}
    uintx YoungGenerationSizeSupplementDecay        = 8                                   {product}
    uintx YoungPLABSize                             = 4096                                {product}
     bool ZeroTLAB                                  = false                               {product}
     intx hashCode                                  = 5                                   {product}
[wkq@VM_77_25_centos ~]$
```

### (4.1.7) 查看所有支持动态修改的JVM参数 java -XX:+PrintFlagsInitial | grep manageable

```
[wkq@VM_77_25_centos ~]$ java -XX:+PrintFlagsInitial | grep manageable
     intx CMSAbortablePrecleanWaitMillis            = 100                                 {manageable}
     intx CMSTriggerInterval                        = -1                                  {manageable}
     intx CMSWaitDuration                           = 2000                                {manageable}
     bool HeapDumpAfterFullGC                       = false                               {manageable}
     bool HeapDumpBeforeFullGC                      = false                               {manageable}
     bool HeapDumpOnOutOfMemoryError                = false                               {manageable}
    ccstr HeapDumpPath                              =                                     {manageable}
    uintx MaxHeapFreeRatio                          = 70                                  {manageable}
    uintx MinHeapFreeRatio                          = 40                                  {manageable}
     bool PrintClassHistogram                       = false                               {manageable}
     bool PrintClassHistogramAfterFullGC            = false                               {manageable}
     bool PrintClassHistogramBeforeFullGC           = false                               {manageable}
     bool PrintConcurrentLocks                      = false                               {manageable}
     bool PrintGC                                   = false                               {manageable}
     bool PrintGCDateStamps                         = false                               {manageable}
     bool PrintGCDetails                            = false                               {manageable}
     bool PrintGCID                                 = false                               {manageable}
     bool PrintGCTimeStamps                         = false                               {manageable}
[wkq@VM_77_25_centos ~]$
```

### (4.1.7) 调整JVM参数-布尔类型

```
jinfo -flag [+|-]<name> PID
[wkq@VM_77_25_centos ~]$ jinfo -flag +PrintGC 13050
[wkq@VM_77_25_centos ~]$
[wkq@VM_77_25_centos ~]$ jinfo -flag +PrintGCDetails 13050
[wkq@VM_77_25_centos ~]$
```

> 如果没报错则代表生效，加完以后可以通过 `jinfo -flags 13050` 验证

### (4.1.8) 调整JVM参数-数字/字符串类型

```
jinfo -flag <name>=<value> PID
[wkq@VM_77_25_centos ~]$ jinfo -flag MaxHeapFreeRatio=65 13050
[wkq@VM_77_25_centos ~]$
```

> 没报错相当于修改成功，但是怎么验证是否生效，可以通过 `jinfo -flags 13050` 验证

```
[wkq@VM_77_25_centos ~]$ jinfo -flags 13050
Attaching to process ID 13050, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.172-b11
Non-default VM flags: -XX:+AlwaysPreTouch -XX:CICompilerCount=2 -XX:CMSInitiatingOccupancyFraction=75 -XX:ErrorFile=null -XX:GCLogFileSize=67108864 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=null -XX:InitialHeapSize=1073741824 -XX:MaxHeapFreeRatio=65 -XX:MaxHeapSize=1073741824 -XX:MaxNewSize=87228416 -XX:MaxTenuringThreshold=6 -XX:MinHeapDeltaBytes=196608 -XX:NewSize=87228416 -XX:NumberOfGCLogFiles=32 -XX:OldPLABSize=16 -XX:OldSize=986513408 -XX:-OmitStackTraceInFastThrow -XX:+PrintGC -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintTenuringDistribution -XX:ThreadStackSize=1024 -XX:+UseCMSInitiatingOccupancyOnly -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:+UseGCLogFileRotation -XX:+UseParNewGC
Command line:  -Xms1g -Xmx1g -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -Des.networkaddress.cache.ttl=60 -Des.networkaddress.cache.negative.ttl=10 -XX:+AlwaysPreTouch -Xss1m -Djava.awt.headless=true -Dfile.encoding=UTF-8 -Djna.nosys=true -XX:-OmitStackTraceInFastThrow -Dio.netty.noUnsafe=true -Dio.netty.noKeySetOptimization=true -Dio.netty.recycler.maxCapacityPerThread=0 -Dlog4j.shutdownHookEnabled=false -Dlog4j2.disable.jmx=true -Djava.io.tmpdir=/tmp/elasticsearch-5035355569386013893 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=data -XX:ErrorFile=logs/hs_err_pid%p.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+PrintGCApplicationStoppedTime -Xloggc:logs/gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=32 -XX:GCLogFileSize=64m -Des.path.home=/home/wkq/software/elasticsearch-6.6.2 -Des.path.conf=/home/wkq/software/elasticsearch-6.6.2/config -Des.distribution.flavor=default -Des.distribution.type=tar
[wkq@VM_77_25_centos ~]$
```

通过 `jinfo -flags 13050` 获取的结果可以看到

> 出现 Command failed in target VM 则表示这个flag参数不支持
>
> ```
> [wkq@VM_77_25_centos ~]$ jinfo -flag ErrorFile=/home/wkq/es_error_file 13050
> Exception in thread "main" com.sun.tools.attach.AttachOperationFailedException: flag 'ErrorFile' cannot be changed
> 
> 	at sun.tools.attach.LinuxVirtualMachine.execute(LinuxVirtualMachine.java:229)
> 	at sun.tools.attach.HotSpotVirtualMachine.executeCommand(HotSpotVirtualMachine.java:261)
> 	at sun.tools.attach.HotSpotVirtualMachine.setFlag(HotSpotVirtualMachine.java:234)
> 	at sun.tools.jinfo.JInfo.flag(JInfo.java:134)
> 	at sun.tools.jinfo.JInfo.main(JInfo.java:81)
> [wkq@VM_77_25_centos ~]$
> ```





# (5) Java内存映像工具 jmap

> Jmap是一个可以输出所有内存中对象的工具，甚至可以将VM 中的heap，以二进制输出成文本。打印出某个java进程（使用pid）内存内的，所有‘对象’的情况（如：产生那些对象，及其数量）。

## (5.1) jmap命令

```
[wkq@VM_77_25_centos ~]$ jmap -help
Usage:
    jmap [option] <pid>
        (to connect to running process)
    jmap [option] <executable <core>
        (to connect to a core file)
    jmap [option] [server_id@]<remote server IP or hostname>
        (to connect to remote debug server)

where <option> is one of:
    <none>               to print same info as Solaris pmap
    -heap                to print java heap summary
    -histo[:live]        to print histogram of java object heap; if the "live"
                         suboption is specified, only count live objects
    -clstats             to print class loader statistics
    -finalizerinfo       to print information on objects awaiting finalization
    -dump:<dump-options> to dump java heap in hprof binary format
                         dump-options:
                           live         dump only live objects; if not specified,
                                        all objects in the heap are dumped.
                           format=b     binary format
                           file=<file>  dump heap to <file>
                         Example: jmap -dump:live,format=b,file=heap.bin <pid>
    -F                   force. Use with -dump:<dump-options> <pid> or -histo
                         to force a heap dump or histogram when <pid> does not
                         respond. The "live" suboption is not supported
                         in this mode.
    -h | -help           to print this help message
    -J<flag>             to pass <flag> directly to the runtime system
[wkq@VM_77_25_centos ~]$
[wkq@VM_77_25_centos ~]$ jmap -options
Usage:
    jmap [option] <pid>
        (to connect to running process)
    jmap [option] <executable <core>
        (to connect to a core file)
    jmap [option] [server_id@]<remote server IP or hostname>
        (to connect to remote debug server)

where <option> is one of:
    <none>               to print same info as Solaris pmap
    -heap                to print java heap summary
    -histo[:live]        to print histogram of java object heap; if the "live"
                         suboption is specified, only count live objects
    -clstats             to print class loader statistics
    -finalizerinfo       to print information on objects awaiting finalization
    -dump:<dump-options> to dump java heap in hprof binary format
                         dump-options:
                           live         dump only live objects; if not specified,
                                        all objects in the heap are dumped.
                           format=b     binary format
                           file=<file>  dump heap to <file>
                         Example: jmap -dump:live,format=b,file=heap.bin <pid>
    -F                   force. Use with -dump:<dump-options> <pid> or -histo
                         to force a heap dump or histogram when <pid> does not
                         respond. The "live" suboption is not supported
                         in this mode.
    -h | -help           to print this help message
    -J<flag>             to pass <flag> directly to the runtime system
[wkq@VM_77_25_centos ~]$
```

> 更多详细信息参考 `man jmap`

### (5.1.1) jmap pid

```
[wkq@VM_77_25_centos ~]$ jmap 13050
Attaching to process ID 13050, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.172-b11
0x0000000000400000	7K	/home/wkq/software/jdk1.8.0_172/bin/java
0x00007fb911271000	49K	/home/wkq/software/jdk1.8.0_172/jre/lib/amd64/libmanagement.so
0x00007fb91167a000	86K	/usr/lib64/libgcc_s-4.8.5-20150702.so.1
0x00007fb911890000	251K	/home/wkq/software/jdk1.8.0_172/jre/lib/amd64/libsunec.so
0x00007fb9242c2000	112K	/home/wkq/software/jdk1.8.0_172/jre/lib/amd64/libnet.so
0x00007fb9244d9000	91K	/home/wkq/software/jdk1.8.0_172/jre/lib/amd64/libnio.so
0x00007fb94065d000	125K	/home/wkq/software/jdk1.8.0_172/jre/lib/amd64/libzip.so
0x00007fb940879000	60K	/usr/lib64/libnss_files-2.17.so
0x00007fb940a8c000	221K	/home/wkq/software/jdk1.8.0_172/jre/lib/amd64/libjava.so
0x00007fb940cb8000	64K	/home/wkq/software/jdk1.8.0_172/jre/lib/amd64/libverify.so
0x00007fb940ec7000	43K	/usr/lib64/librt-2.17.so
0x00007fb9410cf000	1115K	/usr/lib64/libm-2.17.so
0x00007fb9413d1000	16667K	/home/wkq/software/jdk1.8.0_172/jre/lib/amd64/server/libjvm.so
0x00007fb9423d2000	2068K	/usr/lib64/libc-2.17.so
0x00007fb942793000	19K	/usr/lib64/libdl-2.17.so
0x00007fb942997000	101K	/home/wkq/software/jdk1.8.0_172/lib/amd64/jli/libjli.so
0x00007fb942bad000	140K	/usr/lib64/libpthread-2.17.so
0x00007fb942dc9000	155K	/usr/lib64/ld-2.17.so
[wkq@VM_77_25_centos ~]$
```

### (5.1.2) jmap -dump:[live,] format=b, file=file_path

```
[wkq@VM_77_25_centos ~]$ jmap -dump:live,format=b,file=13050.log 13050
Dumping heap to /home/wkq/13050.log ...
Heap dump file created
[wkq@VM_77_25_centos ~]$
```

### (5.1.3) jmap -finalizerinfo pid

```
[wkq@VM_77_25_centos ~]$ jmap -finalizerinfo 13050
Attaching to process ID 13050, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.172-b11
Number of objects pending for finalization: 0
[wkq@VM_77_25_centos ~]$
```

### (5.1.4) jmap -heap pid

> jmap -heap pid
> jmap-J-d64 -heap pid

```
[wkq@VM_77_25_centos ~]$ jmap -heap 13050
Attaching to process ID 13050, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.172-b11

using parallel threads in the new generation.
using thread-local object allocation.
Concurrent Mark-Sweep GC

Heap Configuration:
   MinHeapFreeRatio         = 40
   MaxHeapFreeRatio         = 70
   MaxHeapSize              = 1073741824 (1024.0MB)
   NewSize                  = 87228416 (83.1875MB)
   MaxNewSize               = 87228416 (83.1875MB)
   OldSize                  = 986513408 (940.8125MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
New Generation (Eden + 1 Survivor Space):
   capacity = 78512128 (74.875MB)
   used     = 7022576 (6.6972503662109375MB)
   free     = 71489552 (68.17774963378906MB)
   8.94457477958055% used
Eden Space:
   capacity = 69795840 (66.5625MB)
   used     = 7022576 (6.6972503662109375MB)
   free     = 62773264 (59.86524963378906MB)
   10.061596794307512% used
From Space:
   capacity = 8716288 (8.3125MB)
   used     = 0 (0.0MB)
   free     = 8716288 (8.3125MB)
   0.0% used
To Space:
   capacity = 8716288 (8.3125MB)
   used     = 0 (0.0MB)
   free     = 8716288 (8.3125MB)
   0.0% used
concurrent mark-sweep generation:
   capacity = 986513408 (940.8125MB)
   used     = 62168384 (59.28839111328125MB)
   free     = 924345024 (881.5241088867188MB)
   6.301828591061582% used

22982 interned Strings occupying 3212304 bytes.
[wkq@VM_77_25_centos ~]$
```

### (5.1.5) jmap -histo pid

```
[wkq@VM_77_25_centos ~]$ jmap -histo 13050 | more

 num     #instances         #bytes  class name
----------------------------------------------
   1:        258021       20550728  [C
   2:        432469       13839008  java.util.HashMap$Node
   3:        228108        5474592  java.lang.String
   4:         38596        4839800  [Ljava.util.HashMap$Node;
   5:         86202        4137696  java.util.HashMap
   6:         21464        2574920  [B
   7:         74456        2382592  java.util.Collections$UnmodifiableMap
   8:         12306        2118456  [I
   9:         30839        1957320  [Ljava.lang.Object;
  10:         15798        1730136  java.lang.Class
  11:         52294        1673408  java.util.concurrent.ConcurrentHashMap$Node
  12:         24594         787008  java.util.AbstractList$Itr
  13:         49038         784608  org.elasticsearch.common.lucene.LoggerInfoStream$$Lambda$3048/931367447
  14:         24519         784608  org.elasticsearch.index.engine.Engine$$Lambda$3100/1060530393
  15:         23576         754432  java.lang.ref.WeakReference
  16:         30639         735336  java.util.Arrays$ArrayList
  17:         24519         588456  [Lorg.elasticsearch.common.lease.Releasable;
  18:         24519         588456  org.elasticsearch.index.engine.Engine$Searcher
  19:          8132         585504  java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask
  20:          9564         535584  java.lang.invoke.MemberName
  21:          6052         532576  java.lang.reflect.Method
  22:         33099         529584  java.lang.Object
```

### (5.1.6) jmap -clstats pid

```
[wkq@VM_77_25_centos ~]$ jmap -clstats 13050
Attaching to process ID 13050, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.172-b11
finding class loader instances ..
done.
computing per loader stat ..done.
please wait.. computing liveness.liveness analysis may be inaccurate ...
class_loader	classes	bytes	parent_loader	alive?	type

<bootstrap>	2353	4122324	  null  	live	<internal>
0x00000000c5fa9378	1	714	  null  	dead	org/elasticsearch/painless/lookup/PainlessLookupBuilder$BridgeLoader@0x00000001003f45d0
0x00000000c5fa9878	1	714	  null  	dead	org/elasticsearch/painless/lookup/PainlessLookupBuilder$BridgeLoader@0x00000001003f45d0
0x00000000c58b8708	0	0	0x00000000c544a3d0	dead	org/elasticsearch/plugins/ExtendedPluginsClassLoader@0x0000000100263e90
0x00000000c5ab86a8	1	1471	0x00000000c544a3d0	dead	sun/reflect/DelegatingClassLoader@0x000000010000a028
0x00000000c5ac4b28	11	20182	0x00000000c5c12600	dead	java/net/FactoryURLClassLoader@0x0000000100259738

...

total = 86	11582	19740221	    N/A    	alive=1, dead=85	    N/A
[wkq@VM_77_25_centos ~]$
```

> 省略部分详细信息





# (6) 虚拟机堆转储快照分析工具 jhat





# (7) Java堆栈跟踪工具 jstack (Java Stack Trace)

> jstack主要用来查看某个Java进程内的线程堆栈信息。
> 在实际运行中，往往一次 dump的信息，还不足以确认问题。建议产生三次 dump信息，如果每次 dump都指向同一个问题，我们才确定问题的典型性。

## (7.1) jstack命令

```
~]$ jstack -help
Usage:
    jstack [-l] <pid>
        (to connect to running process)
    jstack -F [-m] [-l] <pid>
        (to connect to a hung process)
    jstack [-m] [-l] <executable> <core>
        (to connect to a core file)
    jstack [-m] [-l] [server_id@]<remote server IP or hostname>
        (to connect to a remote debug server)

Options:
    -F  to force a thread dump. Use when jstack <pid> does not respond (process is hung)
    -m  to print both java and native frames (mixed mode)
    -l  long listing. Prints additional information about locks
    -h or -help to print this help message
[wkq@VM_77_25_centos ~]$
[wkq@VM_77_25_centos ~]$ jstack -options
Usage:
    jstack [-l] <pid>
        (to connect to running process)
    jstack -F [-m] [-l] <pid>
        (to connect to a hung process)
    jstack [-m] [-l] <executable> <core>
        (to connect to a core file)
    jstack [-m] [-l] [server_id@]<remote server IP or hostname>
        (to connect to a remote debug server)

Options:
    -F  to force a thread dump. Use when jstack <pid> does not respond (process is hung)
    -m  to print both java and native frames (mixed mode)
    -l  long listing. Prints additional information about locks
    -h or -help to print this help message
[wkq@VM_77_25_centos ~]$
```

### (7.1.1) jstack pid

```
[wkq@VM_77_25_centos ~]$ jstack  13050
2020-03-29 08:55:30
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.172-b11 mixed mode):

"Attach Listener" #48 daemon prio=9 os_prio=0 tid=0x00007fb90805b800 nid=0x1c9f waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"elasticsearch[elasticsearch_001_data][flush][T#1]" #47 daemon prio=5 os_prio=0 tid=0x00007fb93c693800 nid=0x376c waiting on condition [0x00007fb8fd520000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000c600d388> (a org.elasticsearch.common.util.concurrent.EsExecutors$ExecutorScalingQueue)
...
[wkq@VM_77_25_centos ~]$
```

### (7.1.2) jstack -l pid

```
[wkq@VM_77_25_centos ~]$ jstack -l 13050
2020-03-29 23:09:50
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.172-b11 mixed mode):

"elasticsearch[elasticsearch_001_data][generic][T#8]" #51 daemon prio=5 os_prio=0 tid=0x00007fb93df68000 nid=0x77cf waiting on condition [0x00007fb8ff934000]
   java.lang.Thread.State: TIMED_WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000c5ed5f70> (a org.elasticsearch.common.util.concurrent.EsExecutors$ExecutorScalingQueue)
	at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:215)


   Locked ownable synchronizers:
	- None

...

"Attach Listener" #48 daemon prio=9 os_prio=0 tid=0x00007fb90805b800 nid=0x1c9f waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

...

"VM Thread" os_prio=0 tid=0x00007fb93c0b5000 nid=0x32ff runnable

"Gang worker#0 (Parallel GC Threads)" os_prio=0 tid=0x00007fb93c01d000 nid=0x32fd runnable

"Concurrent Mark-Sweep GC Thread" os_prio=0 tid=0x00007fb93c040000 nid=0x32fe runnable

"VM Periodic Task Thread" os_prio=0 tid=0x00007fb93c103800 nid=0x3307 waiting on condition

JNI global references: 8827

[wkq@VM_77_25_centos ~]$
```





# (8) Java监视与管理控制台 JConsole (Java Monitoring and Managerment Console)





# (9) 多合一故障处理工具 VisuaIVM (All-in-One Java Troubleshooting Tool)

> VisuaIVM (All-in-One Java Troubleshooting Tool) 是到目前为止随JDK发布的功能最强大的运行监视和故障处理程序。





# References

[1] 《深入理解JAVA虚拟机: JVM高级特性与最佳实践》 周志明
[2] [JVM性能调优监控工具jps、jstack、jmap、jhat、jstat、hprof使用详解](https://my.oschina.net/feichexia/blog/196575)
[3] [JAVA JPS 命令详解](https://www.cnblogs.com/tulianghui/p/5914535.html)
[4] [jstat使用详解（分析JVM的使用情况）](https://blog.csdn.net/ouyang111222/article/details/53688986)
[5] [使用Java监控工具出现 Can’t attach to the process](https://www.cnblogs.com/duanxz/p/10240899.html)
[6] [JVM系列：jinfo命令详解](https://my.oschina.net/javamaster/blog/1833908)
[7] [jmap命令详解](https://blog.csdn.net/zhaozheng7758/article/details/8623530)
[8] [jstack命令详解](https://blog.csdn.net/zhaozheng7758/article/details/8623535)
[9] [深入拆解Java虚拟机 - 30 | Java虚拟机的监控及诊断工具（命令行篇）](https://time.geekbang.org/column/article/40520)
[10] [oracle monitoring-tools-and-commands](https://docs.oracle.com/en/java/javase/11/tools/monitoring-tools-and-commands.html)
[11] [深入拆解Java虚拟机 - 31 | Java虚拟机的监控及诊断工具（GUI篇）](https://time.geekbang.org/column/article/40821)
[12] [visualvm](https://visualvm.github.io/index.html)
[13] [jitwatch](https://github.com/AdoptOpenJDK/jitwatch)