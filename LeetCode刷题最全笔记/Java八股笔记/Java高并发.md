## Java高并发

* [1.什么是进程](#1什么是进程)
* [2.什么是线程](#2什么是线程)
* [3.进程间如何通讯](#3进程间如何通讯)
* [4.线程间如何通讯](#4线程间如何通讯)
* [5.同步和异步有何不同，在什么情况下分别使用它们？举例说明](#5同步和异步有何不同在什么情况下分别使用它们举例说明)
* [6.进程调度算法](#6进程调度算法)
* [7.Java中Unsafe类详解](#7java中unsafe类详解)
* [8.如何测试并发量？](#8如何测试并发量)
* [9.有三个线程T1，T2，T3，怎么确保它们按顺序执行？](#9有三个线程t1t2t3怎么确保它们按顺序执行)
* [10.什么是线程调度器(Thread Scheduler)和时间分片(Time Slicing)？](#10什么是线程调度器thread-scheduler和时间分片time-slicing)
* [11.数据库死锁？](#11数据库死锁)
* [12.什么是锁顺序死锁？](#12什么是锁顺序死锁)
* [13.死锁的避免与诊断？](#13死锁的避免与诊断)
* [14.常见的并发容器？](#14常见的并发容器)
* [15.常见的同步工具类？](#15常见的同步工具类)
* [16.Nginx多进程模型是如何实现高并发的？](#16nginx多进程模型是如何实现高并发的)
* [17.CopyOnWriteArrayList](#17copyonwritearraylist)
* [18.AQS](#18aqs)
* [19.Java里的阻塞队列](#19java里的阻塞队列)
* [20.Fork/Join框架](#20forkjoin框架)

#### 1.什么是进程

进程是指运行中的应用程序，每个进程都有自己独立的地址空间（内存空间）。
比如用户点击桌面的IE浏览器，就启动了一个进程，操作系统就会为该进程分配独立的地址空间。当用户再次点击左边的IE浏览器，又启动了一个进程，操作系统将为新的进程分配新的独立的地址空间。目前操作系统都支持多进程。

#### 2.什么是线程

进程是表示自愿分配的基本单位。而线程则是进程中执行运算的最小单位，即执行处理机调度的基本单位。通俗来讲：一个程序有一个进程，而一个进程可以有多个线程。

#### 3.进程间如何通讯

管道(pipe)

管道是一种半双工的通信方式，数据只能单向流动，而且只能在具有亲缘关系的进程间使用。进程的亲缘关系通常是指父子进程关系
有名管道(namedpipe)

有名管道也是半双工的通信方式，但是它云溪无亲缘关系进程间的通信。
信号量(semaphore)

信号量是一个计数器，可以用来控制多个进程对共享资源的访问。它常作为一种锁机制，防止某进程正在访问共享资源时，其他进程也访问该资源。因此，主要作为进程间以及同一进程内不同线程之间的同步手段。
消息队列（messagequeue）

消息队列里有消息的链表，存放在内核中并由消息队列标识符标识。消息队列克服了信号传递消息少、管道只能承载无格式字节流以及缓冲区大小受限等缺点
信号（signal）

信号是一种比较复杂的通信方式，用于通知接收进程某个事件已经发生
共享内存（shared memory）

共享内存就是映射一段能被其他进程所访问的内存，这段共享内存由一个进程创建，但多个进程都可以访问。共享内存是最快的IPC方式，它是针对其他进程间通信方式运行效率低而专门设计的。它往往与其他通信机制，如信号量配合使用，来实现进程间的同步和通信。
套接字（socket）

套接口也是一种进程间通信机制，以其他通信机制不同的是，它可用于不同进程间的通信

#### 4.线程间如何通讯

锁机制：包括互斥锁、条件变量、读写锁

互斥锁提供了以排他方式防止数据结构被并发修改的方法
读写锁允许多个线程同时读共享数据，而对写操作是互斥的
条件变量可以以原子的方式阻塞进程，直到某个特定条件为真为止。对条件的测试是在互斥锁的保护下进行的。条件变量始终与互斥锁一起使用。
信号量机制：包括无名线程信号量和命名线程信号量

信号机制：类似进程间的信号处理
线程间的通信目的只要是用于新城同步，所以线程没有像进程通信中的用于数据交换的通信机制。

#### 5.同步和异步有何不同，在什么情况下分别使用它们？举例说明

如果数据将在线程间共享。例如：正在写的数据以后可能会被另一个线程读到，或者正在读的数据可能已经被另一个线程写过了，那么这些数据就是共享数据，必须进行同步存取
当应用程序在对象上调用了一个需要花费很长时间来执行的方法，并且不希望让程序等待方法的返回时，就应该使用异步编程，在很多情况下采用异步途径往往更有效。
同步交互：指发送一个请求，需要等待返回，然后才能发送下一个请求，有个等待的过程
异步交互：指发送一个请求，不需要等待返回，随时可以再发送下一个请求，即不需要等待。
区别：一个需要等待，一个不需要等待

#### 6.进程调度算法

**实时系统**：FIFO(First Input First Output，先进先出算法)，SJF(Shortest Job First，最短作业优先算法)，SRTF(Shortest Remaining Time First，最短剩余时间优先算法）。
**交互式系统**：RR(Round Robin，时间片轮转算法)，HPF(Highest Priority First，最高优先级算法)，多级队列，最短进程优先，保证调度，彩票调度，公平分享调度。

#### 7.Java中Unsafe类详解

1. 通过Unsafe类可以分配内存，可以释放内存；类中提供的3个本地方法allocateMemory、reallocateMemory、freeMemory分别用于分配内存，扩充内存和释放内存，与C语言中的3个方法对应。
2. 可以定位对象某字段的内存位置，也可以修改对象的字段值，即使它是私有的；
3. 挂起与恢复:将一个线程进行挂起是通过park方法实现的，调用 park后，线程将一直阻塞直到超时或者中断等条件出现。unpark可以终止一个挂起的线程，使其恢复正常。整个并发框架中对线程的挂起操作被封装在 LockSupport类中，LockSupport类中有各种版本pack方法，但最终都调用了Unsafe.park()方法。
4. cas
   [Java中Unsafe类详解](http://blog.csdn.net/bluetjs/article/details/52758095)

#### 8.如何测试并发量？

可以使用apache提供的ab工具测试。

#### 9.有三个线程T1，T2，T3，怎么确保它们按顺序执行？

在多线程中有多种方法让线程按特定顺序执行，你可以用线程类的join()方法在一个线程中启动另一个线程，另外一个线程完成该线程继续执行。为了确保三个线程的顺序你应该先启动最后一个(T3调用T2，T2调用T1)，这样T1就会先完成而T3最后完成。

#### 10.什么是线程调度器(Thread Scheduler)和时间分片(Time Slicing)？

　线程调度器是一个操作系统服务，它负责为Runnable状态的线程分配CPU时间。一旦我们创建一个线程并启动它，它的执行便依赖于线程调度器的实现。

　　时间分片是指将可用的CPU时间分配给可用的Runnable线程的过程。分配CPU时间可以基于线程优先级或者线程等待的时间。线程调度并不受到Java虚拟机控制，所以由应用程序来控制它是更好的选择（即最好不要让你的程序依赖于线程的优先级）。

#### 11.数据库死锁？

在执行一个事务时可能要获取多个锁，一直持有锁到事务提交，如果A事务需要获取的锁在另一个事务B中，且B事务也在等待A事务所持有的锁，那么两个事务之间就会发生死锁。但数据库死锁比较少见，数据库会加以干涉死锁问题，牺牲一个事务使得其他事务正常执行。

#### 12.什么是锁顺序死锁？

两个线程试图以不同的顺序获得相同的锁，那么可能发发生死锁。比如转账问题，由from账户向to账户转账，假设每次我们先同步from对象，再同步to账户，然后执行转账操作，貌似没什么问题。如果这时候to账户同时向from账户转账，那么两个线程可能要永久等待了。

#### 13.死锁的避免与诊断？

 如果一个线程最多只能获取一个锁，那么就不会发生锁顺序死锁了。如果确实需要获取多个锁，锁的顺序可以按照某种规约，比如两个资源的id值，程序按规约保证获取锁的顺序一致。或者可以使用显式的锁Lock，获取锁的时候设置超时时间，超时后可以重新发起，以避免发生死锁。

#### 14.常见的并发容器？

ConcurrentHashMap：使用了分段锁，锁的粒度变得更小，多线程访问时，可能都不存在锁的竞争，所以大大提高了吞吐量。简单对比来看，就好比数据库上用行锁来取代表锁，行锁无疑带来更大的并发。
CopyOnWriteArrayList：写入时复制，多线程访问时，彼此不会互相干扰或被修改的线程所干扰，当然copy时有开销的，尤其时列表元素庞大，且写入操作频繁时，所以仅当迭代操作远远大于修改操作时，才应该考虑使用。
BlockingQueue：阻塞队列提供了可阻塞的put和take方法，当队列已经满了，那么put操作将阻塞到队列可用，当队列为空时，take操作会阻塞到队列里有数据。有界的队列是一种强大的资源管理器，可以在程序负荷过载时保护应用，可作为一种服务降级的策略。阻塞队列还提供offer操作，当数据无法加入队列时，返回失败状态，给应用主动处理负荷过载带来更多灵活性。

#### 15.常见的同步工具类？

 CountDownLatch：递减计数器闭锁，直到达到某个条件时才放行，多线程可以调用await方法一直阻塞，直到计数器递减为零。比如我们连接zookeeper，由于连接操作是异步的，所以可以使用countDownLatch创建一个计数器为1的锁，连接挂起，当异步连接成功时，调用countDown通知挂起线程；再比如5V5游戏竞技，只有房间人满了才可以开始游戏。
FutureTask：带有计算结果的任务，在计算完成时才能获取结果，如果计算尚未完成，则阻塞 get
方法。FutureTask将计算结果从执行线程传递到获取这个结果的线程。
Semaphore：信号量，用来控制同时访问某个特定资源的数量，只有获取到许可acquire，才能够正常执行，并在完成后释放许可，acquire会一致阻塞到有许可或中断超时。使用信号量可以轻松实现一个阻塞队列。
CyclicBarrier：类似于闭锁，它可以阻塞一组线程，只有所有线程全部到达以后，才能够继续执行，so线程必须相互等待。这在并行计算中是很有用的，将一个问题拆分为多个独立的子问题，当线程到达栅栏时，调用await等待，一直阻塞到所有参与线程全部到达，再执行下一步任务。



#### 16.Nginx多进程模型是如何实现高并发的？

进程数与并发数不存在很直接的关系。这取决取server采用的工作方式。如果一个server采用一个进程负责一个request的方式，那么进程数就是并发数。那么显而易见的，就是会有很多进程在等待中。等什么？最多的应该是等待网络传输。

 

Nginx的异步非阻塞工作方式正是利用了这点等待的时间。在需要等待的时候，这些进程就空闲出来待命了。因此表现为少数几个进程就解决了大量的并发问题。apache是如何利用的呢，简单来说：同样的4个进程，如果采用一个进程负责一个request的方式，那么，同时进来4个request之后，每个进程就负责其中一个，直至会话关闭。期间，如果有第5个request进来了。就无法及时反应了，因为4个进程都没干完活呢，因此，一般有个调度进程，每当新进来了一个request，就新开个进程来处理。nginx不这样，每进来一个request，会有一个worker进程去处理。但不是全程的处理，处理到什么程度呢？处理到可能发生阻塞的地方，比如向上游（后端）服务器转发request，并等待请求返回。那么，这个处理的worker不会这么傻等着，他会在发送完请求后，注册一个事件：“如果upstream返回了，告诉我一声，我再接着干”。于是他就休息去了。此时，如果再有request进来，他就可以很快再按这种方式处理。而一旦上游服务器返回了，就会触发这个事件，worker才会来接手，这个request才会接着往下走。由于web server的工作性质决定了每个request的大部份生命都是在网络传输中，实际上花费在server机器上的时间片不多。这是几个进程就解决高并发的秘密所在。webserver刚好属于网络io密集型应用，不算是计算密集型。异步，非阻塞，使用epoll，和大量细节处的优化。也正是nginx之所以然的技术基石。

#### 17.CopyOnWriteArrayList

CopyOnWriteArrayList : 写时加锁，当添加一个元素的时候，将原来的容器进行copy，复制出一个新的容器，然后在新的容器里面写，写完之后再将原容器的引用指向新的容器，而读的时候是读旧容器的数据，所以可以进行并发的读，但这是一种弱一致性的策略。
使用场景：CopyOnWriteArrayList适合使用在读操作远远大于写操作的场景里，比如缓存。

#### 18.AQS

1. AQS使用一个int成员变量来表示同步状态，通过内置的FIFO队列来完成获取资源线程的排队工作。

```
private volatile int state;//共享变量，使用volatile修饰保证线程可见性
```

- 1

1. 2种同步方式：独占式，共享式。独占式如ReentrantLock，共享式如Semaphore，CountDownLatch，组合式的如ReentrantReadWriteLock

2. 节点的状态
   CANCELLED，值为1，表示当前的线程被取消；
   SIGNAL，值为-1，表示当前节点的后继节点包含的线程需要运行，也就是unpark；
   CONDITION，值为-2，表示当前节点在等待condition，也就是在condition队列中；
   PROPAGATE，值为-3，表示当前场景下后续的acquireShared能够得以执行；
   值为0，表示当前节点在sync队列中，等待着获取锁。

3. 模板方法模式

   　protected boolean tryAcquire(int arg) : 独占式获取同步状态，试着获取，成功返回true，反之为false

   　protected boolean tryRelease(int arg) ：独占式释放同步状态，等待中的其他线程此时将有机会获取到同步状态；

   　protected int tryAcquireShared(int arg) ：共享式获取同步状态，返回值大于等于0，代表获取成功；反之获取失败；

   　protected boolean tryReleaseShared(int arg) ：共享式释放同步状态，成功为true，失败为false

   AQS维护一个共享资源state，通过内置的FIFO来完成获取资源线程的排队工作。该队列由一个一个的Node结点组成，每个Node结点维护一个prev引用和next引用，分别指向自己的前驱和后继结点。双端双向链表。

   1. 独占式:乐观的并发策略
      **acquire**
      　a.首先tryAcquire获取同步状态，成功则直接返回；否则，进入下一环节；
      b.线程获取同步状态失败，就构造一个结点，加入同步队列中，这个过程要保证线程安全；
      　c.加入队列中的结点线程进入自旋状态，若是老二结点（即前驱结点为头结点），才有机会尝试去获取同步状态；否则，当其前驱结点的状态为SIGNAL，线程便可安心休息，进入阻塞状态，直到被中断或者被前驱结点唤醒。
      **release**
      release的同步状态相对简单，需要找到头结点的后继结点进行唤醒，若后继结点为空或处于CANCEL状态，从后向前遍历找寻一个正常的结点，唤醒其对应线程。

4. 共享式:
   共享式地获取同步状态.同步状态的方法tryAcquireShared返回值为int。
   a.当返回值大于0时，表示获取同步状态成功，同时还有剩余同步状态可供其他线程获取；
   　b.当返回值等于0时，表示获取同步状态成功，但没有可用同步状态了；
   　c.当返回值小于0时，表示获取同步状态失败。

5. AQS实现公平锁和非公平锁
   非公平锁中，那些尝试获取锁且尚未进入等待队列的线程会和等待队列head结点的线程发生竞争。公平锁中，在获取锁时，增加了isFirst(current)判断，当且仅当，等待队列为空或当前线程是等待队列的头结点时，才可尝试获取锁。
   　[Java并发包基石-AQS详解](https://blog.csdn.net/u012998254/article/details/Java并发包基石-AQS详解)

#### 19.Java里的阻塞队列

### 7个阻塞队列。分别是

ArrayBlockingQueue ：一个由数组结构组成的有界阻塞队列。
LinkedBlockingQueue ：一个由链表结构组成的有界阻塞队列。
PriorityBlockingQueue ：一个支持优先级排序的无界阻塞队列。
DelayQueue：一个使用优先级队列实现的无界阻塞队列。
SynchronousQueue：一个不存储元素的阻塞队列。
LinkedTransferQueue：一个由链表结构组成的无界阻塞队列。
LinkedBlockingDeque：一个由链表结构组成的双向阻塞队列。

### 添加元素

Java中的阻塞队列接口BlockingQueue继承自Queue接口。BlockingQueue接口提供了3个添加元素方法。
add：添加元素到队列里，添加成功返回true，由于容量满了添加失败会抛出IllegalStateException异常
offer：添加元素到队列里，添加成功返回true，添加失败返回false
put：添加元素到队列里，如果容量满了会阻塞直到容量不满

### 删除方法

3个删除方法
poll：删除队列头部元素，如果队列为空，返回null。否则返回元素。
remove：基于对象找到对应的元素，并删除。删除成功返回true，否则返回false
take：删除队列头部元素，如果队列为空，一直阻塞到队列有元素并删除

#### 20.Fork/Join框架

Fork/Join框架是Java 7提供的一个用于并行执行任务的框架，是一个把大任务分割成若干个小任务，最终汇总每个小任务结果后得到大任务结果的框架。Fork/Join框架要完成两件事情：

　　1.任务分割：首先Fork/Join框架需要把大的任务分割成足够小的子任务，如果子任务比较大的话还要对子任务进行继续分割

　　2.执行任务并合并结果：分割的子任务分别放到双端队列里，然后几个启动线程分别从双端队列里获取任务执行。子任务执行完的结果都放在另外一个队列里，启动一个线程从队列里取数据，然后合并这些数据。

　　在Java的Fork/Join框架中，使用两个类完成上述操作

　　1.ForkJoinTask:我们要使用Fork/Join框架，首先需要创建一个ForkJoin任务。该类提供了在任务中执行fork和join的机制。通常情况下我们不需要直接集成ForkJoinTask类，只需要继承它的子类，Fork/Join框架提供了两个子类：

　　　　a.RecursiveAction：用于没有返回结果的任务

　　　　b.RecursiveTask:用于有返回结果的任务

　　2.ForkJoinPool:ForkJoinTask需要通过ForkJoinPool来执行

　　任务分割出的子任务会添加到当前工作线程所维护的双端队列中，进入队列的头部。当一个工作线程的队列里暂时没有任务时，它会随机从其他工作线程的队列的尾部获取一个任务(工作窃取算法)。
Fork/Join框架的实现原理
　　ForkJoinPool由ForkJoinTask数组和ForkJoinWorkerThread数组组成，ForkJoinTask数组负责将存放程序提交给ForkJoinPool，而ForkJoinWorkerThread负责执行这

#### 参考链接 

https://blog.csdn.net/weixin_41050155/article/details/88047556

https://www.cnblogs.com/jjunior/p/14113248.html

http://www.bjpowernode.com/tutorial_baseinterviewquestions/228.html

https://www.cnblogs.com/yang-yutao/p/11553044.html

https://blog.csdn.net/qq_20980207/article/details/98846287
