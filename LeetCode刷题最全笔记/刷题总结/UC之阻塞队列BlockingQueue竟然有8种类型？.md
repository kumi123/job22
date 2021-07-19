## UC之阻塞队列==BlockingQueue==竟然有8种类型？



![图片](https://mmbiz.qpic.cn/mmbiz_png/x0aJCHEALOUEVOib8Yria0GxW5Uibjib4VNo8MxcQMtDibicyibNhEd9DicZb2PEjmQySRicRUZoWgicNEZxciap8ibcLpfC3w/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

# 前言

- [阻塞队列 — ArrayBlockingQueue源码分析](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995273&idx=1&sn=4a1a5141ee7071399e090602703a4ecf&scene=21#wechat_redirect)
- [阻塞队列 — LinkedBlockingQueue源码分析](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995345&idx=1&sn=4d6a6a23c3d995b9760db9c7e9da08e9&scene=21#wechat_redirect)
- [阻塞队列 —PriorityBlockingQueue源码分析](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995409&idx=1&sn=10ac66fc404fa4b031d2f6bf5aa320bd&scene=21#wechat_redirect)
- [阻塞队列 — DelayQueue源码分析](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995451&idx=1&sn=1ed57f8fb2a19a31f0435cfcad37eb0f&scene=21#wechat_redirect)
- [阻塞队列 — SynchronousQueue源码分析](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995504&idx=1&sn=2582353e4fc972fb59d18eba22a1ad61&scene=21#wechat_redirect)
- [阻塞队列 — LinkedTransferQueue源码分析](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995551&idx=1&sn=d24d96195642583b9dac5d1d89a7bc62&scene=21#wechat_redirect)
- [阻塞队列 — LinkedBlockingDeque源码分析](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995638&idx=1&sn=f5d89fbfd76e026b626f6a2561c15836&scene=21#wechat_redirect)
- [阻塞队列 — DelayedWorkQueue源码分析](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995722&idx=1&sn=6f3d9525cf2be6b671b0091b5a105234&scene=21#wechat_redirect)

队列是一种特殊的线性表，是一种先进先出（FIFO）的数据结构。它只允许在表的前端（front）进行删除操作，而在表的后端（rear）进行插入操作。进行插入操作的端称为队尾，进行删除操作的端称为队头。队列中没有元素时，称为空队列。

下面是Queue类的继承关系图：

![图片](https://mmbiz.qpic.cn/mmbiz_png/x0aJCHEALOUEVOib8Yria0GxW5Uibjib4VNoYaiaCJSpicrs1yflGVHLaUeTugGq3c2KkyuaZ4E4J36vnUj3geJUaHEw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

## Queue

![图片](https://mmbiz.qpic.cn/mmbiz_png/x0aJCHEALOUEVOib8Yria0GxW5Uibjib4VNoXicjWic64tGnRuJicRuXzyvCD7KRo6vnbIoSBBtgVMnc1N7ibLqv4pmGvA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
Queue：队列的上层接口，提供了插入、删除、获取元素这3种类型的方法，而且对每一种类型都提供了两种方式，先来看看插入方法：

- ==add(E e)：插入元素到队尾，插入成功返回true，没有可用空间抛出异常 IllegalStateException。==
- ==offer(E e)：插入元素到队尾，插入成功返回true，否则返回false。==

==add和offer作为插入方法的唯一不同就在于队列满了之后的处理方式。add抛出异常，而offer返回false。==

再来看看删除和获取元素方法（和插入方法类似）：

- ==remove()：获取并移除队首的元素==，该方法和poll方法的不同之处在于，如果队列为空该方法==会抛出异常==，而poll不会。
- ==poll()：获取并移除队首的元素==，如果队列为空，==返回null==。
- ==element()==：获取队列首的元素，该方法和peek方法的不同之处在于，如果队列为空该方法会抛出异常，而peek不会。
- ==peek()==：获取队列首的元素，如果==队列为空，返回null==。

如果队列是空，remove和element方法会抛出异常，而poll和peek返回null。

> Queue 是单向队列，为了提供更强大的功能，JDK在1.6的时候新增了一个双向队列Deque，用来实现更灵活的队列操作。

## Deque

![图片](https://mmbiz.qpic.cn/mmbiz_png/x0aJCHEALOUEVOib8Yria0GxW5Uibjib4VNoejkyXTsqHllQ46eu0fbJAmSk7BgH88X5B8lOJ7XuEDFOiciczZeFibQ3A/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)Deque在Queue的基础上，增加了以下几个方法：

- addFirst(E e)：在前端插入元素，异常处理和add一样；
- addLast(E e)：在后端插入元素，和add一样的效果；
- offerFirst(E e)：在前端插入元素，异常处理和offer一样；
- offerLast(E e)：在后端插入元素，和offer一样的效果；
- removeFirst()：移除前端的一个元素，异常处理和remove一样；
- removeLast()：移除后端的一个元素，和remove一样的效果；
- pollFirst()：移除前端的一个元素，和poll一样的效果；
- pollLast()：移除后端的一个元素，异常处理和poll一样；
- getFirst()：获取前端的一个元素，和element一样的效果；
- getLast()：获取后端的一个元素，异常处理和element一样；
- peekFirst()：获取前端的一个元素，和peek一样的效果；
- peekLast()：获取后端的一个元素，异常处理和peek一样；
- removeFirstOccurrence(Object o)：从前端开始移除第一个是o的元素；
- removeLastOccurrence(Object o)：从后端开始移除第一个是o的元素；
- push(E e)：和addFirst一样的效果；
- pop()：和removeFirst一样的效果。

可以发现，其实很多方法的效果都是一样的，只不过名字不同。比如Deque为了实现Stack的语义，定义了push和pop两个方法。

# BlockingQueue阻塞队列

**BlockingQueue（阻塞队列）**，==在Queue的基础上实现了阻塞等待的功能==。它是==JDK 1.5中加入的接口==，它是指这样的一个队列：==当生产者向队列添加元素但队列已满时，生产者会被阻塞；当消费者从队列移除元素但队列为空时，消费者会被阻塞==。

**BlockingQueue** ，是java.util.concurrent 包提供的用于解决并发 **生产者 — 消费者** 问题的最有用的类，很好的解决了多线程中，如何高效安全“传输”数据的问题。它的特性是==在任意时刻只有一个线程可以进行take或者put操作，并且 BlockingQueue 提供类超时 return null 的机制==，在许多生产场景里都可以看到这个工具的身影。

## 总体认识

一般我们用到的阻塞队列有哪些？可以通过下面一个类图来总体看下：

![图片](https://mmbiz.qpic.cn/mmbiz_png/x0aJCHEALOUEVOib8Yria0GxW5Uibjib4VNokZqPhwwibV8LEYDlJyMzs9wGl7iajqAfKzibOp4A3SRicUYZvBVOZUqznQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)可以看到 BlockingQueue 是一个接口，继承它的另外还有两个接口 BlockingDeque（双端队列）、TransferQueue（两个线程之间传递元素)。

阻塞队列的成员如下：

| **队列**                  | **有界性**             | **锁**                                           | **数据结构**                                                 |
| :------------------------ | :--------------------- | :----------------------------------------------- | :----------------------------------------------------------- |
| ==ArrayBlockingQueue==    | ==bounded(有界)==      | ==加锁（公平锁/非公平锁、全局锁）==              | ==数组==                                                     |
| ==LinkedBlockingQueue==   | ==optionally-bounded== | ==加锁（添加和获取独立的锁）==                   | ==单链表==                                                   |
| ==PriorityBlockingQueue== | ==unbounded==          | ==加锁（只有一个锁，入队永远成功，出队会阻塞）== | ==数组（默认长度11，可扩容），底层采用的堆结构实现（二叉堆）== |
| DelayQueue                | unbounded              | 加锁                                             | 数组（可扩容）                                               |
| SynchronousQueue          | bounded                | 无锁（CAS）                                      | 队列（公平策略）、栈（非公平策略）                           |
| LinkedTransferQueue       | unbounded              | 无锁（自旋+CAS）                                 | 双重数据结构或双重队列                                       |
| LinkedBlockingDeque       | unbounded              | 加锁                                             | 双向链表                                                     |
| DelayWorkQueue            | unbounded              | 加锁                                             | 数组（初始长度16，可扩容），底层采用的堆结构实现（二叉堆）   |

## 队列类型

1. ==无限队列（unbounded queue）— 几乎可以无限增长==
2. ==有限队列（bounded queue）— 定义了最大容量==

## 队列数据结构

队列实质就是一种存储数据的结构

- ==通常用链表或者数组实现==
- 一般而言队列具备FIFO先进先出的特性，当然也有双端队列（Deque）优先级队列
- 主要操作：入队（Enqueue）与 出对（Dequeue）

![图片](https://mmbiz.qpic.cn/mmbiz_png/x0aJCHEALOUEVOib8Yria0GxW5Uibjib4VNoddqb5kMr0Agp5JvPnCJnFBxKtxNRRBGtvZa36haxzWJCBZy2ibuYRibw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)常见的5种阻塞队列

- ==ArrayBlockingQueue：一个由数组结构组成的有界阻塞队列。==
- ==LinkedBlockingQueue：一个由链表结构组成的有界阻塞队列。==
- ==PriorityBlockingQueue：一个支持优先级排序的无界阻塞队列。==
- SynchronousQueue：一个不存储元素的阻塞队列。
- ==DelayQueue：一个使用优先级队列实现的无界阻塞队列。==

## BlockingQueue API

BlockingQueue的核心方法

```
public interface BlockingQueue<E> extends Queue<E> {

    //将给定元素设置到队列中，如果设置成功返回true, 否则返回false。如果是往限定了长度的队列中设置值，推荐使用offer()方法。
    boolean add(E e);

    //将给定的元素设置到队列中，如果设置成功返回true, 否则返回false. e的值不能为空，否则抛出空指针异常。
    boolean offer(E e);

    //将元素设置到队列中，如果队列中没有多余的空间，该方法会一直阻塞，直到队列中有多余的空间。
    void put(E e) throws InterruptedException;

    //将给定元素在给定的时间内设置到队列中，如果设置成功返回true, 否则返回false.
    boolean offer(E e, long timeout, TimeUnit unit)
        throws InterruptedException;

    //从队列中获取值，如果队列中没有值，线程会一直阻塞，直到队列中有值，并且该方法取得了该值。
    E take() throws InterruptedException;

    //在给定的时间里，从队列中获取值，时间到了直接调用普通的poll方法，为null则直接返回null。
    E poll(long timeout, TimeUnit unit)
        throws InterruptedException;

    //获取队列中剩余的空间。
    int remainingCapacity();

    //从队列中移除指定的值。
    boolean remove(Object o);

    //判断队列中是否拥有该值。
    public boolean contains(Object o);

    //将队列中值，全部移除，并发设置到给定的集合中。
    int drainTo(Collection<? super E> c);

    //指定最多数量限制将队列中值，全部移除，并发设置到给定的集合中。
    int drainTo(Collection<? super E> c, int maxElements);
}
```

BlockingQueue 接口的所有方法可以分为两大类：负责**向队列添加元素的方法**和 **检索这些元素的方法**。在队列满/空的情况下，来自这两个组的每个方法的行为都不同。

### 添加元素

| **方法**                                    | **说明**                                                     |
| :------------------------------------------ | :----------------------------------------------------------- |
| ==add()==                                   | ==如果插入成功则返回 true，否则抛出 IllegalStateException 异常== |
| ==put()==                                   | ==将指定的元素插入队列，如果队列满了，那么会阻塞直到有空间插入== |
| ==offer()==                                 | ==如果插入成功则返回 true，否则返回 false==                  |
| ==offer(E e, long timeout, TimeUnit unit)== | ==尝试将元素插入队列，如果队列已满，那么会阻塞直到有空间插入== |

### 检索元素

| **方法**                              | **说明**                                                     |
| :------------------------------------ | :----------------------------------------------------------- |
| ==take()==                            | ==获取队列的头部元素并将其删除，如果队列为空，则阻塞并等待元素变为可用== |
| ==poll(long timeout, TimeUnit unit)== | ==检索并删除队列的头部，如有必要，等待指定的等待时间以使元素可用，如果超时，则返回 null== |

**BlockingQueue**最重要的也就是关于阻塞等待的几个方法，而这几个方法正好可以用来实现**生产-消费的模型**。

# ArrayBlockingQueue

**ArrayBlockingQueue** 由数组支持的有界阻塞队列，队列基于数组实现，容量大小在创建 ArrayBlockingQueue 对象时已经定义好。此队列按照先进先出（FIFO）的原则对元素进行排序。支持公平锁和非公平锁，默认采用非公平锁。

**ArrayBlockingQueue** 内部由ReentrantLock来实现线程安全，由Condition的await和signal来实现等待唤醒的功能。它的数据结构是数组，准确的说是一个循环数组（可以类比一个圆环），所有的下标在到达最大长度时自动从0继续开始。

> 深入理解**ArrayBlockingQueue**可以阅读[《阻塞队列 — ArrayBlockingQueue源码分析》](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995273&idx=1&sn=4a1a5141ee7071399e090602703a4ecf&scene=21#wechat_redirect)

![图片](data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==)

# LinkedBlockingQueue

**LinkedBlockingQueue** 由链表节点支持的可选有界队列，是一个基于链表的无界队列（理论上有界），队列按照先进先出的顺序进行排序。LinkedBlockingQueue不同于ArrayBlockingQueue，它如果不指定容量，默认为 Integer.MAX_VALUE，也就是无界队列。所以为了避免队列过大造成机器负载或者内存爆满的情况出现，我们在使用的时候建议手动传一个队列的大小。

**LinkedBlockingQueue** 内部由单链表实现，只能从head取元素，从tail添加元素。添加元素和获取元素都有独立的锁，也就是说LinkedBlockingQueue是读写分离的，读写操作可以并行执行。LinkedBlockingQueue采用可重入锁(ReentrantLock)来保证在并发情况下的线程安全。

向无限队列添加元素的所有操作都将永远不会阻塞，[**注意这里不是说不会加锁保证线程安全**]，因此它可以增长到非常大的容量。

使用无限 BlockingQueue 设计生产者 - 消费者模型时最重要的是 消费者应该能够像生产者向队列添加消息一样快地消费消息。否则，内存可能会填满，然后就会得到一个 OutOfMemory 异常。

> 深入理解**LinkedBlockingQueue**可以阅读[《阻塞队列 — LinkedBlockingQueue源码分析》](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995345&idx=1&sn=4d6a6a23c3d995b9760db9c7e9da08e9&scene=21#wechat_redirect)

![图片](data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==)

# PriorityBlockingQueue

**PriorityBlockingQueue** 优先级队列，线程安全（添加、读取都进行了加锁）、无界、读阻塞的队列，底层采用的堆结构实现（二叉树），默认是小根堆，最小的或者最大的元素会一直置顶，每次获取都取最顶端的数据。可以实现优先出队。最特别的是它只有一个锁，入队操作永远成功，而出队只有在空队列的时候才会进行线程阻塞。可以说有一定的应用场景吧，比如：有任务要执行，可以对任务加一个优先级的权重，这样队列会识别出来，对该任务优先进行出队。

> 深入理解**PriorityBlockingQueue**可以阅读[《阻塞队列 —PriorityBlockingQueue源码分析》](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995409&idx=1&sn=10ac66fc404fa4b031d2f6bf5aa320bd&scene=21#wechat_redirect)

![图片](https://mmbiz.qpic.cn/mmbiz_png/x0aJCHEALOUEVOib8Yria0GxW5Uibjib4VNo27ehb5jTFc4xsKtice3pCeypqLPCCCpkJON5E9dbrzWLvbcCG9o29aQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

# DelayQueue

**DelayQueue** 由优先级支持的、基于时间的调度队列，内部使用非线程安全的优先队列(PriorityQueue)实现，而无界队列基于数组的扩容实现。在创建元素时，可以指定多久才能从队列中获取当前元素。只有延时期满后才能从队列中获取元素。

> 深入理解**DelayQueue**可以阅读[《阻塞队列 — DelayQueue源码分析》](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995451&idx=1&sn=1ed57f8fb2a19a31f0435cfcad37eb0f&scene=21#wechat_redirect)

![图片](data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==)

# SynchronousQueue

**SynchronousQueue** 一个不存储元素的阻塞队列，每一个 put 操作必须等待 take 操作，否则不能继续添加元素。支持公平锁和非公平锁2种策略来访问队列。默认是采用非公平性策略访问队列。公平性策略底层使用了类似队列的数据结构，而非公平策略底层使用了类似栈的数据结构。SynchronousQueue的吞吐量高于LinkedBlockingQueue和ArrayBlockingQueue。

> 深入理解**SynchronousQueue**可以阅读[《阻塞队列 — SynchronousQueue源码分析》](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995504&idx=1&sn=2582353e4fc972fb59d18eba22a1ad61&scene=21#wechat_redirect)

![图片](https://mmbiz.qpic.cn/mmbiz_png/x0aJCHEALOUEVOib8Yria0GxW5Uibjib4VNohlCvy4SgmEhiaOiaibrNwtIe5AxXCqcvezcoQicJYRWGh5zNHo9yjjAkxw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

# LinkedTransferQueue

**LinkedTransferQueue** 是一个由链表结构组成的无界阻塞传输队列，它是一个很多队列的结合体（ConcurrentLinkedQueue，[LinkedBlockingQueue](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995345&idx=1&sn=4d6a6a23c3d995b9760db9c7e9da08e9&scene=21#wechat_redirect)，[SynchronousQueue](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995504&idx=1&sn=2582353e4fc972fb59d18eba22a1ad61&scene=21#wechat_redirect)），在除了有基本阻塞队列的功能（但是这个阻塞队列没有使用锁）之外；队列实现了TransferQueue接口重写了transfer 和 tryTransfer 方法，这组方法和SynchronousQueue公平模式的队列类似，具有匹配的功能。

> 深入理解**LinkedTransferQueue**可以阅读[《阻塞队列 — LinkedTransferQueue源码分析》](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995551&idx=1&sn=d24d96195642583b9dac5d1d89a7bc62&scene=21#wechat_redirect)

![图片](data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==)

# LinkedBlockingDeque

**LinkedBlockingDeque** 一个由于链表结构组成的双向阻塞队列，队列头部和尾部都可以添加和移除元素，多线程并发时，可以将锁的竞争对多降到一半。

> 深入理解**LinkedBlockingDeque**可以阅读[《阻塞队列 — LinkedBlockingDeque源码分析》](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995638&idx=1&sn=f5d89fbfd76e026b626f6a2561c15836&scene=21#wechat_redirect)

![图片](https://mmbiz.qpic.cn/mmbiz_jpg/x0aJCHEALOUEVOib8Yria0GxW5Uibjib4VNoGAghBvEaQibexGBdb99ibklZbuKAT9D2tcU8DM77RWYODYD1VZyB4TDw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

# DelayedWorkQueue

**DelayedWorkQueue** 也是一种设计为定时任务的延迟队列，其实现原理和DelayQueue 基本一样，核心数据结构是二叉最小堆的优先队列，队列满时会自动扩容，不过是将优先级队列和DelayQueue的实现过程迁移到本身方法体中，从而可以在该过程当中灵活的加入定时任务特有的方法调用。

> 深入理解**DelayedWorkQueue**可以阅读[《阻塞队列 — DelayedWorkQueue源码分析》](https://mp.weixin.qq.com/s?__biz=MzA4NjI1MTkyNw==&mid=2449995722&idx=1&sn=6f3d9525cf2be6b671b0091b5a105234&scene=21#wechat_redirect)

![图片](data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==)

# 对比分析

## LinkedBlockingQueue与ArrayBlockingQueue区别

- ==队列大小有所不同，ArrayBlockingQueue是有界的初始化必须指定大小，而LinkedBlockingQueue可以是有界的也可以是无界的(Integer.MAX_VALUE)，对于后者而言，当添加速度大于移除速度时，在无界的情况下，可能会造成内存溢出等问题。==
- ==数据存储容器不同，ArrayBlockingQueue采用的是数组作为数据存储容器，而LinkedBlockingQueue采用的则是以Node节点作为连接对象的链表。==
- ==由于ArrayBlockingQueue采用的是数组的存储容器，因此在插入或删除元素时不会产生或销毁任何额外的对象实例，而LinkedBlockingQueue则会生成一个额外的Node对象。这可能在长时间内需要高效并发地处理大批量数据的时，对于GC可能存在较大影响。==
- ==两者的实现队列添加或移除的锁不一样，ArrayBlockingQueue实现的队列中的锁是没有分离的，即添加操作和移除操作采用的同一个ReenterLock锁，而LinkedBlockingQueue实现的队列中的锁是分离的，其添加采用的是putLock，移除采用的则是takeLock，这样能大大提高队列的吞吐量，也意味着在高并发的情况下生产者和消费者可以并行地操作队列中的数据，以此来提高整个队列的并发性能==。

## LinkedTransferQueue和SynchronousQueue（公平模式）区别

- LinkedTransferQueue 和SynchronousQueue 其实基本是差不多的，两者都是无锁带阻塞功能的队列，都是使用的双重队列；
- SynchronousQueue 通过内部类Transferer 来实现公平和非公平队列，在LinkedTransferQueue 中没有公平与非公平的区分；
- LinkedTransferQueue 实现了TransferQueue接口，该接口定义的是带阻塞操作的操作，相比SynchronousQueue 中的Transferer 功能更丰富。
- SynchronousQueue 中放数据操作和取数据操作都是阻塞的，当队列中的操作和本次操作不匹配时，线程会阻塞，直到匹配的操作到来。LinkedTransferQueue 是无界队列，放数据操作不会阻塞，取数据操作如果没有匹配操作可能会阻塞，通过参数决定是否阻塞（ASYNC,SYNC,NOW,TIMED）。

## LinkedBlockingDeque与LinkedList区别

```
package com.niuh.deque;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/*
 *   LinkedBlockingDeque是“线程安全”的队列，而LinkedList是非线程安全的。
 *
 *   下面是“多个线程同时操作并且遍历queue”的示例
 *   (1) 当queue是LinkedBlockingDeque对象时，程序能正常运行。
 *   (2) 当queue是LinkedList对象时，程序会产生ConcurrentModificationException异常。
 *
 */
public class LinkedBlockingDequeRunner {

    // TODO: queue是LinkedList对象时，程序会出错。
    // private static Queue<String> queue = new LinkedList<String>();
    private static Queue<String> queue = new LinkedBlockingDeque<String>();

    public static void main(String[] args) {

        // 同时启动两个线程对queue进行操作！
        new MyThread("A").start();
        new MyThread("B").start();
    }

    private static void printAll() {
        String value;
        Iterator iter = queue.iterator();
        while (iter.hasNext()) {
            value = (String) iter.next();
            System.out.print(value + ", ");
        }
        System.out.println();
    }

    private static class MyThread extends Thread {
        MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            int i = 0;
            while (i++ < 6) {
                // “线程名” + "-" + "序号"
                String val = Thread.currentThread().getName() + i;
                queue.add(val);
                // 通过“Iterator”遍历queue。
                printAll();
            }
        }
    }
}
```

**输出结果**：

```
A1, 
A1, A2, 
A1, A2, A3, 
A1, A2, A3, A4, 
A1, A2, A3, A4, A5, 
A1, A2, A3, A4, A5, A6, 
A1, A2, A3, A4, A5, A6, B1, 
A1, A2, A3, A4, A5, A6, B1, B2, 
A1, A2, A3, A4, A5, A6, B1, B2, B3, 
A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, 
A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, B5, 
A1, A2, A3, A4, A5, A6, B1, B2, B3, B4, B5, B6, 
```

> **结果说明**：示例程序中，启动两个线程(线程A和线程B)分别对LinkedBlockingDeque进行操作:
>
> - 以线程A而言，它会先获取“线程名”+“序号”，然后将该字符串添加到LinkedBlockingDeque中；
> - 接着，遍历并输出LinkedBlockingDeque中的全部元素。
> - 线程B的操作和线程A一样，只不过线程B的名字和线程A的名字不同。
> - 当queue是LinkedBlockingDeque对象时，程序能正常运行。
> - 如果将queue改为LinkedList时，程序会产生ConcurrentModificationException异常。

# BlockingQueue应用

## 多线程生产者-消费者示例

接下来我们创建一个由两部分组成的程序：生产者 ( Producer ) 和消费者 ( Consumer ) 。

### 生产者（Producer）

生产者将生成一个 0 到 100 的随机数(十全大补丸的编号)，并将该数字放在 BlockingQueue 中。我们将创建 16 个线程（潘金莲）用于生成随机数并使用 put() 方法阻塞，直到队列中有可用空间。

> 需要记住的重要一点是，我们需要阻止我们的消费者线程无限期地等待元素出现在队列中。

从生产者(潘金莲)向消费者(武大郎)发出信号的好方法是，不需要处理消息，而是发送称为毒 （ poison ） 丸 （ pill ） 的特殊消息。我们需要发送尽可能多的毒 （ poison ） 丸 （ pill ） ，因为我们有消费者(武大郎)。然后当消费者从队列中获取特殊的毒 （ poison ） 丸 （ pill ）消息时，它将优雅地完成执行。

以下生产者的代码：

```
package com.niuh.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 生产者（Producer）
 **/
@Slf4j
public class NumbersProducer implements Runnable {
    private BlockingQueue<Integer> numbersQueue;
    private final int poisonPill;
    private final int poisonPillPerProducer;

    public NumbersProducer(BlockingQueue<Integer> numbersQueue, int poisonPill, int poisonPillPerProducer) {
        this.numbersQueue = numbersQueue;
        this.poisonPill = poisonPill;
        this.poisonPillPerProducer = poisonPillPerProducer;
    }

    public void run() {
        try {
            generateNumbers();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void generateNumbers() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            numbersQueue.put(ThreadLocalRandom.current().nextInt(100));
            log.info("潘金莲-{}号,给武大郎的泡药！", Thread.currentThread().getId());
        }

        /*while (true) {
            numbersQueue.put(ThreadLocalRandom.current().nextInt(100));
            if (false) {
                break;
            }
        }*/

        for (int j = 0; j < poisonPillPerProducer; j++) {
            numbersQueue.put(poisonPill);
            log.info("潘金莲-{}号,往武大郎的药里放入第{}颗毒丸！", Thread.currentThread().getId(), j + 1);
        }
    }
}
```

我们的生成器构造函数将 BlockingQueue 作为参数，用于协调生产者和使用者之间的处理，我们看到方法generateNumbers() 将 100 个元素（生产100副药给武大郎吃）放入队列中。它还需要有毒 （ poison ） 丸 （ pill ） （潘金莲给武大郎下毒）消息，以便知道在执行完成时放入队列的消息类型。该消息需要将 poisonPillPerProducer 次放入队列中。

### 消费者（Consumer）

每个消费者将使用 take() 方法从 BlockingQueue 获取一个元素，因此它将阻塞，直到队列中有一个元素。从队列中取出一个 Integer 后，它会检查该消息是否是毒 （ poison ） 丸 （ pill ）（武大郎看潘金莲有没有下毒） ，如果是，则完成一个线程的执行。否则，它将在标准输出上打印出结果以及当前线程的名称。

```
package com.niuh.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

/**
 * 消费者（Consumer）
 **/
@Slf4j
public class NumbersConsumer implements Runnable {
    private BlockingQueue<Integer> queue;
    private final int poisonPill;

    public NumbersConsumer(BlockingQueue<Integer> queue, int poisonPill) {
        this.queue = queue;
        this.poisonPill = poisonPill;
    }

    public void run() {
        try {
            while (true) {
                Integer number = queue.take();
                if (number.equals(poisonPill)) {
                    return;
                }
                log.info("武大郎-{}号,喝药-编号:{}", Thread.currentThread().getId(), number);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

需要注意的重要事项是队列的使用。与生成器构造函数中的相同，队列作为参数传递。我们可以这样做，是因为 BlockingQueue 可以在线程之间共享而无需任何显示同步。

### 验证测试

既然我们有生产者和消费者，我们就可以开始我们的计划。我们需要定义队列的容量，并将其设置为 10个元素。我们创建4 个生产者线程，并且创建等于可用处理器数量的消费者线程：

```
package com.niuh.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 多线程生产者-消费者示例
 **/
public class Main {

    public static void main(String[] args) {
        int BOUND = 10;
        int N_PRODUCERS = 16;
        int N_CONSUMERS = Runtime.getRuntime().availableProcessors(); //=8
        int poisonPill = Integer.MAX_VALUE;
        int poisonPillPerProducer = N_CONSUMERS / N_PRODUCERS; // =0
        int mod = N_CONSUMERS % N_PRODUCERS;//0+8=8

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(BOUND);

        //潘金莲给武大郎熬药
        for (int i = 1; i < N_PRODUCERS; i++) {
            new Thread(new NumbersProducer(queue, poisonPill, poisonPillPerProducer)).start();
        }

        //武大郎开始喝药
        for (int j = 0; j < N_CONSUMERS; j++) {
            new Thread(new NumbersConsumer(queue, poisonPill)).start();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //潘金莲开始投毒，武大郎喝完毒药GG
        new Thread(new NumbersProducer(queue, poisonPill, poisonPillPerProducer + mod)).start();
    }

}
```

BlockingQueue 是使用具有容量的构造创建的。我们正在创造 4 个生产者和 N 个消费者（武大郎）。我们将我们的毒 （ poison ） 丸 （ pill ）消息指定为 Integer.MAX_VALUE，因为我们的生产者在正常工作条件下永远不会发送这样的值。这里要注意的最重要的事情是 BlockingQueue 用于协调它们之间的工作。