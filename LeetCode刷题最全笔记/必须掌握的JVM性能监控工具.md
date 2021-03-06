# 必须掌握的==JVM性能监控工具==

### 引言

前一段时间面阿里，碰到了这样的问题。
面试官：“JVM有关指令了解吗？”
我：“调优参数吗，也会一些，==-Xms设置初始堆大小，-Xmx设置最大堆大小==......”
面试官：“你可能理解错我的意思了，我想知道的是相关指令。比如查看jvm进程情况，内存占用，GC情况等。”
我：“额，指令不太了解，就知道有一个==内置的工具Jconsole能查看Java线程使用情况，是否持有死锁==。”
面试官：“对，其他的工具呢，比如jps，jstack，jstat，jmap有了解吗？”
完了，当时学的时候只是简单的过了一遍，没想到还会被考到.......面试官对于我的知识储备应该大打折扣了。

关于JVM的性能监测工具还是挺重要的，不仅故障排查的时候需要用到，做压测的时候也需要查看Java进程的有关参数！（==阿里面试官比较关注服务器QPS以及压测，只要你提到了，基本上都会问==）话不多说，开始学习。

### 一、监控工具介绍

关于性能监控这块的工具有linux的top指令及查看进程相关指令，jinfo，jps，jstat，jmap，jstack，jconsole。

先简单介绍一下
**top指令**：查看当前所有进程的使用情况，CPU占有率，内存使用情况，服务器负载状态等参数。除此之外它还是个交互命令，使用可参考[完全解读top](https://zhuanlan.zhihu.com/p/36995443)。
**jps**：与linux上的ps类似，用于查看有权访问的虚拟机的进程，可以查看本地运行着几个java程序，并显示他们的进程号。当未指定hostid时，默认查看本机jvm进程。
**jinfo**：可以输出并修改运行时的java 进程的一些参数。
**jstat**：可以用来监视jvm内存内的各种堆和非堆的大小及其内存使用量。
**jstack**：堆栈跟踪工具，一般用于查看某个进程包含线程的情况。
**jmap：**打印出某个java进程（使用pid）内存内的所有对象的情况。一般用于查看内存占用情况。
**jconsole**：一个java GUI监视工具，可以以图表化的形式显示各种数据。并可通过远程连接监视远程的服务器的jvm进程。

### 二、监测工具的使用

下面把常用的指令罗列了一下。

top

![img](https://pic1.zhimg.com/v2-1518046615d07b95d7f6dcf651d4c74c_r.jpg)top指令

jps

指令格式：jps [options] [hostid]
**jps -l**
**输出应用程序主类完整package名称或jar完整名称**
jps -v
列出jvm的启动参数

![img](https://pic4.zhimg.com/v2-1f433bf513187bd0f49c73b955737db7_r.jpg)jps -l

jinfo

指令格式：jinfo [ option ] pid
jinfo pid
输出全部参数和系统属性
jinfo pid -flags pid
只输出参数

![img](https://pic3.zhimg.com/v2-feae7686adbd6ccd74fbe1052024e0be_r.jpg)jinfo -flags

jstat

指令格式：jstat [options] [pid] [间隔时间/毫秒] [查询次数]
**jstat -gcutil pid 1000 100**
**1000毫秒统计一次gc情况，统计100次**
jstat -class pid
类加载统计，输出加载和未加载class的数量及其所占空间的大小
jstat -compiler pid
编译统计，输出编译和编译失败数量及失败类型与失败方法

![img](https://pic3.zhimg.com/v2-1eec4dc6d93af948e987671beb29e8aa_r.jpg)jstat -gcutil 1000

jstack

指令格式：jstack [options] [pid]
jstack -l pid
查看jvm线程的运行状态，是否有死锁等信息

jmap

指令格式：jmap [ option ] pid
jmap [ option ] executable core
产生核心dump的Java可执行文件，dump就是堆的快照，内存镜像
jmap [ option ] [server-id@]remote-hostname-or-IP
通过可选的唯一id与调试的远程服务器主机名进行操作

jmap -histo:live pid
输出堆中活动的对象以及大小
**jmap -heap pid**
**查看堆的使用状况信息**
jmap -permstat pid
打印进程的类加载器和类加载器加载的持久代对象信息

![img](https://pic1.zhimg.com/v2-9986099dc4252cdda30d124d12d3b0cc_r.jpg)jmap -heap

Jconsole

bin目录下的工具，支持远程连接，可以查看JVM的概述，内存，线程等详细情况。

![img](https://pic3.zhimg.com/v2-79c78a02b119794c1719c5502c1aab5e_r.jpg)Jconsole监测

### 总结

监测工具很多，光看一遍是记不住的，关键是多敲！本文只是对JVM的指令进行简单的介绍。如果要深入，每一种工具背后都能引发出好多知识点，具体的用法还是按实际情况决定，但基本常用的这些记住了面试应该问题不大。