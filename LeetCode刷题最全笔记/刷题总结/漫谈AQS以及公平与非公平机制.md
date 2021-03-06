# 漫谈AQS以及公平与非公平机制

> 对java并发机制熟悉的人都知道底层基本上都是基于AQS实现的,即==AbstractQueuedSynchronizer==,Doug Lea为了简化并发编程的实现过程,也从开发者角度帮我们从小心谨慎的做同步、唤醒操作中解放出来,让开发者非常容易的写出并发程序。像==CountDownLatch、Semaphore、ReentrantLock等等都是基于AQS==的实现。

##### AQS底层机制

底层的具体执行过程

- 它的底层机制就是通过并发的获取一个==`volatile`定义的state字段==,通过==控制对state状态的获取与释放来做到线程之间的同步==。

- 而且AQS里面又==维护了一个队列,可以实现线程排队机制,也可以做等待唤醒操作==。

- 队列里的==每一个节点都是有状态==的,分别为`==CANCELLED、SIGNAL、CONDITION、PROPAGATE==`,它是==通过一个`valatile`定义的`waitStatus`来判断节点是处于上述4个状态中的哪一个来分别作出响应==。

- AQS里面又分为排==它性机制和共享机制,如果是排它性机制,一个线程先获取到state,则其它线程必须等待直到这个资源被释放，而共享机制则一般是有多个资源,可以供多个线程获取.所以利用这种机制就可以实现像`Semaphore`这样的结构,控制在某一时刻的并发线程数==。

##### 怎么使用AQS

因为==AQS是一个抽象类,并且提供了5个空方法供子类覆盖==:



```java
    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }

    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }

    protected int tryAcquireShared(int arg) {
        throw new UnsupportedOperationException();
    }

    protected boolean tryReleaseShared(int arg) {
        throw new UnsupportedOperationException();
    }

    protected boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }
```

里面还有几个核心方法:



```java
    //直接通过CAS设置state
    protected final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    //判断是否有有其它线程在排队,公平性操作
    public final boolean hasQueuedPredecessors() {
        Node t = tail; // Read fields in reverse initialization order
        Node h = head;
        Node s;
        return h != t &&
            ((s = h.next) == null || s.thread != Thread.currentThread());
    }
```

所以如果==要使用AQS的话只需要继承它即可==,然后依据是排它性获取还是共享的方式获取资源,来覆盖指定的方法。一般都是以内部类的方式来做,因为它没法复用。

##### ==以ReetrantLock为例进行说明==

下面就以`ReetrantLock`为例进行一下说明:



```java
abstract static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -5179523762034025860L;

        //加锁的方法
        abstract void lock();

        //非公平获取锁
        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }

        //排它性获取资源
        protected final boolean tryRelease(int releases) {
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }

        protected final boolean isHeldExclusively() {
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        final ConditionObject newCondition() {
            return new ConditionObject();
        }

        final Thread getOwner() {
            return getState() == 0 ? null : getExclusiveOwnerThread();
        }

        final int getHoldCount() {
            return isHeldExclusively() ? getState() : 0;
        }

        final boolean isLocked() {
            return getState() != 0;
        }

        private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0);
        }
    }
```

它里==面持有一个静态内部类,继承自AQS.并且在内部类中自己又扩展了几个方法==,包括`lock`和`nonfairTryAcquire`.先看一下它的构造函数:



```java
    public ReentrantLock() {
        sync = new NonfairSync();
    }

     public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }
```

可知它默认是==非公平锁，也可以手动传入一个`fair=true`来启用公平锁（两种构造方法）。而`NonfairSync`和`FairSync`都是继承自`Sync`==.



```java
    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = 7316153563782823691L;
        final void lock() {
            if (compareAndSetState(0, 1))
                setExclusiveOwnerThread(Thread.currentThread());
            else
                acquire(1);
        }

        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    }

    static final class FairSync extends Sync {
        private static final long serialVersionUID = -3000897897090466540L;

        final void lock() {
            acquire(1);
        }

        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (!hasQueuedPredecessors() &&
                    compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    }
```

那到底==公平锁和非公平锁的区别在哪==？仔细看一下,==`NonfairSync`中的lock方法首先会通过CAS修改state,而`FairSync`则直接通过`acquire`获取。因为`acquire`最终还是通过CAS修改state,只是里面内置了排队机制.而非公平锁则直接修改state的值,无需排队。==

##### AQS的acquire方法剖析



```java
    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
```

首先==通过`tryAcquire`尝试获取资源,如果返回false,则通过`acquireQueued`开始排队==。因为`tryAcquire`是子类覆盖的,这里以公平锁的`tryAcquire`为例进行讲解。



```java
    protected final boolean tryAcquire(int acquires) {
            //获取当前线程
            final Thread current = Thread.currentThread();
           //获取state的值,因为是volatie修饰的,线程之间可见
            int c = getState();

            //如果state==0，则说明还没有线程获取到资源
            if (c == 0) {
                //如果没有线程排队,就尝试着通过CAS修改state的值为acquires
                //然后将当前线程设置到成员变量保存
                if (!hasQueuedPredecessors() &&
                    compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            //如果state!=0,说明有线程占用。则判断该线程是否为自身,这里主要做线程的重入。如果是自身,则将state加1.
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
   }
```



关于tryAcuire()注释的解释问题：

```
获取state的值,因为是volatie修饰的,线程之间可见
如果state==0，则说明还没有线程获取到资源
                //如果没有线程排队,就尝试着通过CAS修改state的值为acquires
                //然后将当前线程设置到成员变量保存
如果state!=0,说明有线程占用。则判断该线程是否为自身,这里主要做线程的重入。如果是自身,则将state加1.

```





==如果线程获取资源失败,则`tryAcquire`返回false,然后开始通过`addwaitor`将当前线程以排它性节点的方式加到队列的末尾（通过CAS），然后通过`acquireQueued`开始排队==。



```csharp
private Node addWaiter(Node mode) {
        // 将当前线程封装成node
        Node node = new Node(Thread.currentThread(), mode);
        // 如果尾节点非空
        Node pred = tail;
        if (pred != null) {
           // 将尾节点设置成当前node的前置节点(因为新插入的node将成为新的tail节点)
            node.prev = pred;
            // 通过cas将当前的tail节点设置成最新的node
            if (compareAndSetTail(pred, node)) {
                // 老的tail节点的next节点指向新的tail节点, 最终将新node放到链表的最末尾
                pred.next = node;
                return node;
            }
        }
        enq(node);
        return node;
    }
```

接下来是acquiredQueued方法:



```java
    //这里的node是当前线程，排它性节点
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {//死循环
                //获取到当前节点的前置节点
                final Node p = node.predecessor();
                //如果p节点是head,则说明在它之前没有线程在排队，
                //直接通过tryAcquire获取资源; 如果获取资源成功,则将node设置成头节点
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                //如果node的前置节点不是head,则通过shouldParkAfterFailedAcquire
                //方法进行设置节点属性、移位操作。
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())//暂停当前线程
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```

```
排队的过程好不好

这里的node是当前线程，排它性节点
//获取到当前节点的前置节点p
//如果p节点是head,则说明在它之前没有线程在排队
//直接通过tryAcquire获取资源; 如果获取资源成功,则将node设置成头节点


//如果node的前置节点不是head,则通过shouldParkAfterFailedAcquire
//方法进行设置节点属性、移位操作。其实就是根据这个前边节点的队列的具体的状态莱进行当前节点的正确插入

具体的操作如下边所示

首先获取前置节点的waitStatus,具有4种，分别为CANCELLED（1）、SIGNAL（-1）、CONDITION（-2）、PROPAGATE（-3）。



```



重点来分析下`shouldParkAfterFailedAcquire`方法:



```java
     private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        //首先获取前置节点的waitStatus,具有4种，分别为CANCELLED（1）、SIGNAL（-1）、CONDITION（-2）、PROPAGATE（-3）。
        int ws = pred.waitStatus;

        //如果前置节点的waitStatus是SIGNAL,即唤醒状态, 则返回true;
        //表示需要让前置节点先执行, 当前线程将阻塞自己(park)
        if (ws == Node.SIGNAL)
            return true;
        if (ws > 0) {//大于0,即取消状态,开始移位操作
            // 移动指针直到前置节点的waitStatus不是CANCELD状态
            // 剔除掉已经取消的节点
            do {
                //a. 将前置节点设置成前置节点的前置节点
                //b. 将当前节点的前置节点指向新的前置节点
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            //当node设置前置节点的下一个节点
            pred.next = node;
        } else {//如果是无状态、CONDITION、PROPAGATE状态, 则修改成SIGNAL
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }
```

==当`ws`大于0时(即前置节点为CANCELLED状态),则通过一个`do while`来进行左移操作(向head移动),剔除掉所有的CANCELLED节点,直到前置节点的状态不为CANCELLED; 然后将前置节点的下一个节点设置成当前节点(意味着中间被取消的节点全部被剔除了)==

![img](https://upload-images.jianshu.io/upload_images/7928684-7dba280ba324c4a7.png?imageMogr2/auto-orient/strip|imageView2/2/w/1119/format/webp)

AQS的节点移动

==如果`ws`<0的时候,即其它二个状态(CONDITION、PROPAGATE),通过`compareAndSetWaitStatus`将`pred`节点的`waitStatus`设置成`SIGNAL`状态，然后返回。==

总之,`shouldParkAfterFailedAcquire`方法的==作用就是判断当前的前置节点是否为`SIGNAL`状态,如果是,则直接将当前线程暂停。如果是CANCELLED节点,则通过循环将当前节点移动到非CANCELLED节点的前面。如果是其他两个节点,则将前置节点设置成`SIGNAL`状态即可。==
==如果`shouldParkAfterFailedAcquire`返回true,则说明当前节点的上一个节点为`SIGNAL`状态,则可以通过`parkAndCheckInterrupt`暂停线程。==





```java
    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }
```

##### ==AQS的release方法剖析==

还是以公平锁为例:



```java
      protected final boolean tryRelease(int releases) {
            //直接通过set修改state的值
            int c = getState() - releases;
            //如果不是当前线程,则不能释放锁操作,抛出一个监视器异常
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
    }
```

既然==有资源的获取,就有对应资源的释放,排它性方式是通过`release`方法来实现的==:



```java
    public final boolean release(int arg) {
        //尝试着释放一次
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
    }
```

==如果`tryRelease`释放资源成功,则判断head节点是否为空,如果不为空且waitStatus不为0,则通过`unparkSuccessor`唤醒下一个线程（释放完资源肯定要进行唤醒下一个等待线程）:==



```csharp
    private void unparkSuccessor(Node node) {
        //先获取head节点的状态
        int ws = node.waitStatus;
        if (ws < 0)//如果head节点小于0,则直接将其置0
            compareAndSetWaitStatus(node, ws, 0);

        //获取head的next节点
        Node s = node.next;
        //如果node的next节点为空或者是CANCELLED状态
        if (s == null || s.waitStatus > 0) {
            s = null;

            //从tail开始向head遍历,直到t为null或者t!=node
            for (Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0)
                    s = t;
        }

        //如果head的next节点不为空,则直接唤醒下一个节点
        if (s != null)
            LockSupport.unpark(s.thread);
    }
```

##### 共享式获取资源的实现

上面只是排它性获取和释放锁的模式,那共享式模式又是怎样呢？下面以`Semaphore`的公平性实现为例进行分析，里面也是持有一个Sync的实例,实现自AQS:



```cpp
      protected int tryAcquireShared(int acquires) {
            for (;;) {
                //判断队列是否有线程在排队
                if (hasQueuedPredecessors())
                    return -1;

                //如果没有在排队的线程,就将state减少,通过CAS设置进去
                int available = getState();
                int remaining = available - acquires;
                if (remaining < 0 ||
                    compareAndSetState(available, remaining))
                    return remaining;
            }
     }

     Sync(int permits) {
            setState(permits);
     }
```

说明可以通过构造函数指定state的大小。再来看一下`AQS`的`acquireSharedInterruptibly`方法；



```java
    public final void acquireSharedInterruptibly(int arg) {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (tryAcquireShared(arg) < 0)
            doAcquireSharedInterruptibly(arg);
    }
```

如果没有获取到资源,就通过`doAcquireSharedInterruptibly`来获取:



```java
     private void doAcquireSharedInterruptibly(int arg) {
        //以共享节点的模式封装成当前节点
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        if (interrupted)
                            selfInterrupt();
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```

发现流程和`acquireQueued`排队机制大致相同,只是node类型不同,一个是排它性节点，一个是共享是节点,这里就不再分析，接下来看看它怎么释放资源的。



```csharp
     private void doReleaseShared() {
        for (;;) {
            //将head节点保存起来
            Node h = head;
            //head节点不为空,且head!=tail，说明有排队节点
            if (h != null && h != tail) {
                int ws = h.waitStatus;
                //如果h的waitStatus为SIGNAL,则修改其状态为0,如果修改失败(进方法之前被其他线程已经唤醒,导致CAS失败),则跳过。否则就唤醒该节点。
                if (ws == Node.SIGNAL) {
                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                        continue;            // loop to recheck cases
                    unparkSuccessor(h);
                }
                //如果h节点状态为0,则通过CAS将其修改成PROPAGATE(-3)
                else if (ws == 0 &&
                         !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                    continue;                // loop on failed CAS
            }
            //如果head没有改变，还是原来的节点,则跳出循环。
            if (h == head)                   // loop if head changed
                break;
        }
    }
```

疑惑的是只有当`h == head`的时候才能跳出循环,到底是什么情况下会一直循环呢？原来是在其他线程获取到资源后会通过`setHeadAndPropagate`来重新更新`head`:



```csharp
    private void setHeadAndPropagate(Node node, int propagate) {
        Node h = head; // Record old head for check below
        setHead(node);
        //如果获取到了资源，或者head为空的时候,或者head的waitStatus<0的时候
        if (propagate > 0 || h == null || h.waitStatus < 0 ||
            (h = head) == null || h.waitStatus < 0) {
            //将当前节点的下一个节点不为空,并且是共享节点,则释放资源
            Node s = node.next;
            if (s == null || s.isShared())
                doReleaseShared();
        }
    }
```

##### 最后