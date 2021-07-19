## 面试被问AQS、ReentrantLock



# （一）AQS概述

Java并发编程的核心在于java.concurrent.util包，juc中大多数同步器的实现都围绕了一个**公共的行为**，比如==等待队列、条件队列、独占获取、共享获取等，这个行为的抽象就是基于AbstractQueuedSynchronized（AQS）。AQS定义了多线程访问共享资源的同步器框架。==

简单来讲，**AQS就好比一个行为准则**，而并发包中的大多数同步器在这个准则下实现。

AQS具备以下的几个特性：**==阻塞等待队列、共享/独占、公平/非公平、可重入、允许中断。==**

如果你点开JUC发源码，会发现大量同步器的实现，比如：Lock、Latch、Barrier等都基于AQS实现。

# （二）几个重要的知识点

在AQS中，我们需要记住几个重要的知识点：

![图片](https://mmbiz.qpic.cn/mmbiz_png/TtgsXZeib3BCDpWsZliaaycA9pfL6K6yGlbu371dHibJibe6v3vPI7qR8m3hyOnfM3jV7kpyMg4ILFuGZmqYvRibDLA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)在这里插入图片描述

1、AQS的实现通常是定义==内部类**Sync**继承AQS==，将同步器的所有调用都映射到Sync对应的方法上。

2、AQS内部有个属性叫==**state**，表示资源的可用状态==。state有三种访问方式getState()、setState()、compareAndSetState()

3、AQS定义了两种资源的共享方式：==独占（**Exclusive**）如ReentrantLock==、==共享（**Share**）如Semaphore或CountDownLatch==

4、AQS中定义了==**同步等待队列**，用于存放等待线程的一个队列。==

这几个知识点会在后面的内容中使用到。

# （三）ReentrantLock

我们通过ReentrantLock这个示例来更深入的了解AQS。我会通过上面四个知识点去讲解ReentrantLock中AQS的使用。

1、首先进入ReentrantLock的源码内部，直接就能看到ReentrantLock中定义的内部类Sync

![图片](https://mmbiz.qpic.cn/mmbiz_png/TtgsXZeib3BCDpWsZliaaycA9pfL6K6yGlUX3sGRAgRgaFfPDiczEA7U2ibPVBVNOJfTibCUuCPwp5s3EzSOPqLWqgg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)在这里插入图片描述

Sync继承了AQS，按AQS去指定同步规则。

2、既然继承了AQS，==ReentrantLock内部也相当于有了**state**，这个state用来记录上锁的次数，ReentrantLock是个可重入锁，如果多次上锁，state会记录上锁的次数，需要释放同样次数的锁才算把锁释放完。==

3、==ReentrantLock的资源是独占的，AbstractQueuedSynchronized继承了一个叫**AbstractOwnableSynchronizer**的抽象类：==

![图片](https://mmbiz.qpic.cn/mmbiz_png/TtgsXZeib3BCDpWsZliaaycA9pfL6K6yGlH9xBiaiay51Ytlwu3bwglUQxzJQOs166TZw0dfZAuw3a7zJFm2rmrxDQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

在这个类中，==有个变量叫exclusiveOwnerThread，这个变量记录着当前是哪个线程独占了锁。==

4、同步等待队列：由于==ReentrantLock是个独占的锁，当有一个线程在使用这个锁的时候，其他线程就要到队列中去等待，这个队列是**一种基于双向链表的队列**==（类CLH队列），节点中存放线程信息。

![图片](https://mmbiz.qpic.cn/mmbiz_png/TtgsXZeib3BCDpWsZliaaycA9pfL6K6yGl21oUTr1FLXCwl3Tr5ib8FEA8FHzbqPkXOVSUFglDVwiaRy4iccKITIR0w/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)在这里插入图片描述

# （四）可重入锁

在介绍AQS时，==我们讲到了AQS中有个状态值state，这个值用来判断当前资源的可用状态。可重入锁的意思就是对一个对象可以实现多次加锁，state就用来记录加锁的次数==。下面写一段代码：

```
public class ReentrantLockTest {
    //定义全局的锁对象
    private static final Lock lock=new ReentrantLock(true);
    public static int count=0;
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                testlock();
            }
        },"线程A").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                testlock();
            }
        },"线程B").start();
    }

    private static void testlock() {
        lock.lock();
        count++;
        System.out.println(Thread.currentThread().getName()+"第一次加锁"+count);
        lock.lock();
        count++;
        System.out.println(Thread.currentThread().getName()+"第二次加锁"+count);
        count--;
        lock.unlock();
        System.out.println(Thread.currentThread().getName()+"第一次解锁"+count);
        count--;
        lock.unlock();
        System.out.println(Thread.currentThread().getName()+"第二次解锁"+count);
    }
}
```

生成两个线程，让他们去执行testlock方法，然后在testlock方法的开始和结束加锁，保证同时只有一个线程可以执行里面的方法。最后的结果是线程有序执行：

![图片](https://mmbiz.qpic.cn/mmbiz_png/TtgsXZeib3BCDpWsZliaaycA9pfL6K6yGlic7CITPfT6reiaF1Wtj6Nk9DS3Noegb9zTicJIMD5vUQ95yqCwbR1T4aw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

在代码中，我们进行了两次lock，这就是可重入锁。我们通过断点调试，来分析第二次加锁后lock中的值，下面给出了说明。

![图片](https://mmbiz.qpic.cn/mmbiz_png/TtgsXZeib3BCDpWsZliaaycA9pfL6K6yGlMDQ5HfoxuMTeSyyYgO0OSGsDKZRvpw1lpwmgemd49T9gia1NdOmEU2Q/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)在这里插入图片描述

# （五）公平锁与非公平锁

我们在用构造方法创建ReentrantLock的时候，可以传入一个boolean类型的参数，true或false

```
private static final Lock lock=new ReentrantLock(true);
```

这里的true和false代表了创建的ReentrantLock对象是公平锁还是非公平锁

![图片](https://mmbiz.qpic.cn/mmbiz_png/TtgsXZeib3BCDpWsZliaaycA9pfL6K6yGlzcWVM11cHQUUiaeKSrYtRw6IQpQ7ZGqQGhJdx8pLQT9asnNKVhyeydg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

通过上文的学习，我们知道当有线程在使用锁的时候，其他线程是处于等待队列中的，而一旦锁被释放后，==**他会去唤醒等待队列中的第一个锁**：==







==如果是公平锁，当有新的线程来的时候，他会先去看看等待队列中有没有等待的线程，如果有，**则乖乖跑到最后去排队**。==

==如果是非公平锁，当有新的线程来的时候，直接看state的状态，如果发现是0，**不管等待队列有没有等待的线程，直接去和被唤醒的锁竞争**，如果竞争失败了，则乖乖跑到队列最后去排队，否则就直接占有锁。==