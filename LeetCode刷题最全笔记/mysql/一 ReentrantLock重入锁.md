# 一 ReentrantLock重入锁

重入锁的意思就是，线程thread1执行ReentrantLock的lock()方法获取到锁之后且没有释放锁，再次调用lock()方法的时候，不会阻塞，直接增加重入次数，具体的在哪里增加次数，后面源码分析会体现，我们看下下面代码

```java
public class ReentrantDemo {

    private Lock lock = new ReentrantLock();

    public void demo() {
        //获取锁
        lock.lock();
        System.out.println("begin demo");
        //执行demo2方法
        demo2();
        lock.unlock();
    }

    public void demo2() {
        //获取锁
        lock.lock();
        System.out.println("begin demo2");
        //释放锁
        lock.unlock();
    }

    public static void main(String[] args) {
        ReentrantDemo rd = new ReentrantDemo();
        //启动线程调用demo方法
        new Thread(rd::demo).start();
    }
}
123456789101112131415161718192021222324252627
```

从上面代码可以看出一个线程先调用demo()方法获取锁之后，又调用demo2()方法之后，没有出现阻塞；从下图结果可以看出。
![在这里插入图片描述](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/20190523174451996.png)

## 1.1 重入锁的目的

假如上面的情况，调用demo2的时候出现阻塞的话，就会出现死锁的情况，所以重入锁的目的是为了解决死锁的问题。下面我们从源码进行分析

------

# 二 ReentrantLock源码分析

## 2.1 引出AQS的UML图

一般我们使用ReentrantLock都是直接创建一个对象，例如下面代码

```java
Lock lock = new ReentrantLock();
1
```

下面我们看下ReentrantLock的构造函数

```java
	public ReentrantLock() {
        sync = new NonfairSync();
    }
123
```

从上面这个代码可以看出，我们要分析两个东西：一个是sync,一个是NofairSync（非公平锁）

```java
//ReentrantLock实现了Lock
public class ReentrantLock implements Lock, java.io.Serializable {
	//我们刚刚要找的sync字段
    private final Sync sync;
    //Sync继承了AbstractQueuedSynchronizer
    abstract static class Sync extends AbstractQueuedSynchronizer {
123456
```

1.sync分析:从上面源码可以看出sync是ReentrantLock内的属性，而且Sync是ReentrantLock的内部类，并且继承了AbstractQueuedSynchronizer，这个就是我们常常说的**AQS**，再进入AQS类看下

```java
public abstract class AbstractQueuedSynchronizer 
	extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
123
```

从上面源码可以看出AbstractQueuedSynchronizer继承AbstractOwnableSynchronizer,也就是AQS继承AOS（后面都用AQS代表AbstractQueuedSynchronizer，AOS代表AbstractOwnableSynchronizer），我们再看看还没分析的NofairSync

```java
public class ReentrantLock implements Lock, java.io.Serializable {
	//NonfairSync继承Sync
	static final class NonfairSync extends Sync {
123
```

2.NofairSync分析：从上面源码可以看出NonfairSync也是ReentrantLock的内部类，并且继承Sync，难怪刚刚new NonfairSync()可以直接赋值给sync
我们再看下ReentrantLock类的结构
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190523220844495.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)
**到这里我们可以总结下**：
1.ReentrantLock下面有三个内部类：Sync,NonfairSync,FairSync
2.AQS继承AOS
2.Sync继承AQS
3.NonfairSync（非公平锁）、FairSync（公平锁）分别继承Sync
那我们可以得出UML图
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190523222439798.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)
这里的UML图一定要有个整体的印象，因为后面分析的时候，很多方法跳转都和UML图有关

------

## 2.2 ReentrantLock.lock()分析

当线程执行lock()的时候，我们看看源码执行的是什么

```java
	//ReentrantLock类下的方法
	public void lock() {
        sync.lock();
    }
1234
```

原来又去调用了sync的lock()方法，前面我们已经知道ReentrantLock创建的时候，sync已经赋值为NonfairLock（非公平锁），那上面代码执行sync.lock()的时候，其实调用的是NonfairLock下的lock()方法，我们来看下代码

```java
static final class NonfairSync extends Sync {
        //调用此方法
        final void lock() {
        	//cas操作
            if (compareAndSetState(0, 1))
                setExclusiveOwnerThread(Thread.currentThread());
            else
                acquire(1);
        }
123456789
```

然后又调用了compareAndSetState(0,1)方法，就是大家常说CAS，而此方法位于父类AQS下，我们看看源码

```java
public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer 
	implements java.io.Serializable {
	//调用此方法
	protected final boolean compareAndSetState(int expect, int update) {
        //传进来的参数expect=0,update=1
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }
1234567
```

然后又调用了unsafe的compareAndSwapInt()的方法，到这里，我们又有几个疑问：
1.stateOffset是什么？
2.unsafe是什么？
3.compareAndSwapInt做了什么？

```java
public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    //初始化值为0
    private volatile int state;
  
  	private static final long stateOffset;
  	//获取state在内存中的偏移量
  	stateOffset = unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer.class.getDeclaredField("state"));
   	private static final Unsafe unsafe = Unsafe.getUnsafe();
12345678910
public final class Unsafe {
	//native方法，cas
	public final native boolean 
	compareAndSwapInt(Object this, long stateOffset, int expect, int update);
1234
```

解释下上面源码的意思
1.stateOffset：是state变量在内存中的偏移量，通过这个偏移量就可以拿到state的值
2.unsafe：sun.misc包下的，可以直接操作底层的原子性操作
3.compareAndSwapInt：这个方法很关键，这个方法的意思：

- 3.1 如果state==expect，那么就将state更新为update，并返回true；一开始state为0，我们传入的expect为0，update为1，两个数state,expect相等，则把state赋值为1，并且返回true；否则直接返回false
- 3.2 compareAndSwapInt 这个方法保证了原子性，也就是多个线程来执行这个方法，只有一个能成功,即返回true，并且state=1
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190525174648473.png)
  为了便于理解，下面用三个线程去分析AQS

```java
public class ReentrantDemo extends Thread {

    private ReentrantLock reentrantLock;

    public ReentrantDemo(ReentrantLock lock) {
        this.reentrantLock = lock;
    }

    @Override
    public void run() {
        reentrantLock.lock();
        //执行业务代码
        reentrantLock.unlock();
    }

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        ReentrantDemo thread1 = new ReentrantDemo(lock);
        ReentrantDemo thread2 = new ReentrantDemo(lock);
        ReentrantDemo thread3 = new ReentrantDemo(lock);
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
1234567891011121314151617181920212223242526
```

假如三个线程都调用了reentrantLock.lock()方法下的cas()方法，前面已经分析了，cas保证了原子性，所以只有一个线程能成功，这里假设thread1执行cas成功了，执行成功后，会将AQS的字段state设置为1，也就是下面这个流程
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190530152804513.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)
执行成功后，会继续执行setExclusiveOwnerThread(Thread.currentThread())方法，此方法位于AOS下，我们看下源码：

```java
public abstract class AbstractOwnableSynchronizer
    implements java.io.Serializable {
    //字段
    private transient Thread exclusiveOwnerThread;
    //执行的方法（方法修饰符为protected，只允许子类调用）
    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }
    ...
123456789
```

上面源码的意思就是给AOS下的字段exclusiveOwnerThread进行赋值，如下图
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190530152844433.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)
这里我们要记住这2个字段信息state=1,exclusiveOwnerThread=thread1，后面继续源码分析会用到
假如thread1正在执行业务代码，这个时候又有其他线程执行reentrantLock.lock()，我们看看源码会发生什么
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190601113159584.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)
这个时候cas肯定会失败，因为state为1，与expect为0比较，肯定不相等，那么就会执行acquire(1)方法,此方位位于AQS下

------

## 2.3 AQS.acquire

```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    //调用此方法
	public final void acquire(int arg) {
		//此时arg入参为1
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
    ···
1234567891011
```

这里是执行tryAcquire(arg)方法，也就是尝试获取锁的功能:
1.如果返回tryAcquire(arg)为true说明获取锁成功，也就是!tryAcquire(arg)为false，则不会执行后面的acquireQueued(addWaiter(Node.EXCLUSIVE), arg)方法了，直接执行业务代码去了
2.如果返回tryAcquire(arg)为false说明获取锁失败，也就是!tryAcquire(arg)为true，则会再去执行后面的acquireQueued(addWaiter(Node.EXCLUSIVE), arg)方法
所以我们下面要分析下tryAcquire(arg)方法做了什么

------

## 2.4 NonfairSync.tryAcquire

AQS下也有tryAcquire方法，子类NonfairSync对AQS的tryAcquire方法进行了重写，所以执行的是NonfairSync下的tryAcquire

```java
static final class NonfairSync extends Sync {
	protected final boolean tryAcquire(int acquires) {
			//入参acquires为1
            return nonfairTryAcquire(acquires);
    }
    ···   
123456
```

从上面源码看出，然后又调用了nonfairTryAcquire方法，此方法位于Sync中

------

## 2.5 Sync.nonfairTryAcquire(★)

```java
abstract static class Sync extends AbstractQueuedSynchronizer {
		final boolean nonfairTryAcquire(int acquires) {
			//入参acquires=1
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
        ···
123456789101112131415161718192021
```

因为线程的我们是不可控的，分析上面代码我们要考虑到好几种情况：

a) thread1执行完reentrantLock.unlock()方法后，然后thread2执行到nonfairTryAcquire这个方法
thread1执行完reentrantLock.unlock()方法，AQS下的state会设置为0，exclusiveOwnerThread设置为null，这个时候thread2执行的是下面这段代码

```java
			//入参acquires=1
            final Thread current = Thread.currentThread();
            //获取到AQS下state为0，并赋值给变量c
            int c = getState();
            if (c == 0) {//无锁状态
            	//这里又和之前的cas一样，
            	//把state赋值为1，exclusiveOwnerThread赋值为thread2,然后返回true
                if (compareAndSetState(0, acquires)) {
                	//保存当前获得锁的线程，下次再来的时候，不用尝试竞争锁
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
12345678910111213
```

这个时候state=1，exclusiveOwnerThread=thread2，然后返回true，tryAcquire(arg)为true，!tryAcquire(arg)为false，如下图
![tryAcquire(arg)](https://img-blog.csdnimg.cn/20190601151247657.png)
然后thread2去执行自己的业务代码了，这是第一种情况，下面分析另外一种情况：

------

b) thread1执行业务代码的时候，thread1又执行了reentrantLock.lock()方法然后进入到nonfairTryAcquire方法，这就是开头重入的情况，这个时候thread1执行的是下面的代码

```java
			//入参acquires=1
            final Thread current = Thread.currentThread();
            //获取的state=1,并赋值给c
            int c = getState();
            if (c == 0) {
            	···
            }
            //因为是重入，是同一个线程，所以会执行下面的代码
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                //执行完上面代码后，nextc=2
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                //将AQS下的state设置为2
                setState(nextc);
                return true;
            }
1234567891011121314151617
```

这个时候会把state赋值为2，也就是重入次数的一个标记，然后返回true，然后执行业务代码

------

c)假如thread1正在执行业务代码的时候（未释放锁），thread2来执行nonfairTryAcquire方法，上面两种情况都不符合，将直接返回false，也就是tryAcquire(arg)为false，则!tryAcquire(arg)为true，那么thread2先执行AQS下的addWaiter(Node.EXCLUSIVE)，再执行acquireQueued(addWaiter(Node.EXCLUSIVE), arg)方法。我们可以画一下thread2的时序图
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190603131120679.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)
到这里，我们要分析下thread1在执行业务代码时候，thread2执行到addWaiter方法做了啥，看下下面的源码(下图含Node类，因为方法中用到了Node类的字段和构造函数)

## 2.6 AQS.addWaiter(★)

```java
public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    //############### AQS内部类Node ################
    static final class Node {
    	static final Node EXCLUSIVE = null;
    	//前面节点
    	volatile Node prev;
    	//后面节点
    	volatile Node next;
    	//线程
    	volatile Thread thread;
    	//后面等待的节点
        Node nextWaiter;
    	//其中一个构造函数
    	Node(Thread thread, Node mode) {
            this.nextWaiter = mode;
            this.thread = thread;
        }
    }
    //############## AQS字段和方法 ##############
    //初始化为null
    private transient volatile Node tail;
    //因为Node.EXCLUSIVE=null，所以mode=null
	private Node addWaiter(Node mode) {
		//第一步
        Node node = new Node(Thread.currentThread(), mode);//1
        //此时tail=null
        Node pred = tail;
        //第二步
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        //第三步
        enq(node);
        return node;
    }
    ···

```

第一步：thread2调用addWaiter方法可以看出，新建了一个Node对象，假设地址是0x9527（这里我们用橙色表示Node类型）


![在这里插入图片描述](https://img-blog.csdnimg.cn/20190606192551602.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)
第二步：因为tail目前为null，那么pred=null，所以if(pred != null)不成立，不会执行if下面的语句
第三步：因为第二步的if不执行，那么会执行enq方法，并传入第一步刚刚创建好的node,也就是上图的0x9527，最后返回此节点node对象，下面我看看enq方法

## 2.7 AQS.enq(★)

```java
	//入参node为0x9527
	private Node enq(final Node node) {
        for (;;) {
            Node t = tail;
            if (t == null) { //第一步
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else {//第二步
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }
12345678910111213141516
```

第一步：第一次循环的时候，tail还是为null，那么会进入第一步的if语句，然后会执行compareAndSetHead(new Node())方法，首先会新建一个Node对象（这里我们假设地址为0x9528），如下图
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190606193110398.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)

```java
	private final boolean compareAndSetHead(Node update) {
		//入参为0x9528的Node对象
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }
1234
```

这里cas比较head的值与null是否相等，如果相等，则把head赋值为update（这里为0x9528）,然后返回true
否则，不赋值，直接返回false
由上面的图可以看出，head为null,那么cas成功，则把head赋值，即把引用地址指向0x9528，如下图：
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019060619400185.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)
然后再执行tail=head，那么tail也会指向0x9528,如下图

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190607182535418.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)
然后会进入for循环的第二次循环，也就是常说的自旋，如下图，会执行下图红框的代码
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019060719482186.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)
第二步：首先会新建一个Node节点t,然后把tail的引用赋值给t，如下图，t也指向了0x9528
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190608102832169.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)
后面会执行下面这个代码（这个node对象是之前的入参0x9527）

```java
node.prev=t
1
```

也就是将0x9527的prev指向t的引用，就出现下图的情况
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190608120243863.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)
接着会执行compareAndSetTail(t, node)，这里又是cas操作，t为0x9528，node为0x9527,我们看下源码

```java
	private final boolean compareAndSetTail(Node expect, Node update) {
		//expect为0x9528,update为0x9527,tail为0x9528
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }
1234
```

上面的代码为cas操作，具有原子性，是比较tail和expect是否相等，如果相等，则把tail更新为update,并返回true;否则不更新并返回false
从之前的分析可以看出，tail和expect都是0x9528那么，将tail更新为update，也就是指向0x9527并返回true，如下图：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190608122618580.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)
最后返回true，也就是执行下面图中的代码
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190608122956382.png)
此时t的引用为0x9528，然后会使得0x9528的next属性(Node类型)指向node(0x9527)，**到这里线程thread2被封装成Node节点并加入双向链表**，也就是下面这个图
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190608123605485.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA0NTIzODg=,size_16,color_FFFFFF,t_70)
然后执行return t之后，enq就执行结束了,然后会返回到addWaiter方法中,如下图

![在这里插入图片描述](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/20190608130832360.png)
然后会返回到acquire(arg)方法中，如下图
![在这里插入图片描述](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/20190608130832360.png)
接着会执行acquireQueued(addWaiter(Node.EXCLUSIVE), arg))方法，下面我们看看源码

## 2.8 AQS.acquireQueued

```java
	//node为0x9527，arg为1
	final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
            	//第一步
                final Node p = node.predecessor();
                //第二步
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null;
                    failed = false;
                    return interrupted;
                }
                //第三步
                //p为0x9528,node为0x9527
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

```

上面代码中主要的就是"死循环"中的代码
第一步：获取node节点的前节点，并赋值给p，这个源码就不看了，很简单；此时node节点为0x9527,他的前节点是0x9528，那么p的引用也是0x9528
第二步：首先判断p=head?因为head也是指向0x9528，那么p=head，是true，接着会执行tryAcquire(1)，这个方法我们前面分析过，就是再次尝试获取锁，因为此时thread1还在执行业务代码，没有释放锁，那么thread2肯定获取不到锁，那么会返回false，这个if语句下面的代码块不会执行，至于什么时候执行，我们后面会分析
第三步：这里有两个方法shouldParkAfterFailedAcquire和parkAndCheckInterrupt，shouldParkAfterFailedAcquire这个方法就是获取锁失败之后判断是否应该挂起线程，只有返回true之后才去执行后面的方法parkAndCheckInterrupt，parkAndCheckInterrupt这个方法是挂起线程，
下面我们分析shouldParkAfterFailedAcquire方法



```java
	//pred为0x9528,node为0x9527
	private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;
        //Node.SIGNAL为-1
        if (ws == Node.SIGNAL)
            return true;
        if (ws > 0) {
        	//当线程等待超时或者就会走这里，然后将此节点移除
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
             //pred为0x9528,ws为0，Node.SIGNAL为-1
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }
123456789101112131415161718
```

源码中出现了waitStatus属性，这个属性之前没有出现过，那么肯定初始化都为0，如下图
![在这里插入图片描述](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/20190609151557712.png)
那么前两个if语句都不符合条件，然后执行
compareAndSetWaitStatus方法，这里又是cas操作，看下源码：

```java
	private static final boolean compareAndSetWaitStatus(Node node,
                                                         int expect,
                                                         int update) {
    //node为0x9528，waitStatus为0，expect为0，update-1                                                  
	return unsafe.compareAndSwapInt(node, waitStatusOffset,expect, update);
    }
123456
```

然后会将waitStatus设置成-1，如下图：

![在这里插入图片描述](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/20190609155655493.png)
然后shouldParkAfterFailedAcquire返回false，接着在acquireQueued方法中的"死循环"又会执行shouldParkAfterFailedAcquire方法，然后返回true，然后执行下图
![在这里插入图片描述](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/20190609155655493.png)
接着执行parkAndCheckInterrupt方法，把当前线程挂起

```java
private final boolean parkAndCheckInterrupt() {
		//挂起当前线程
        LockSupport.park(this);
        //如果当前线程挂起之前由其他线程调用interrupted方法，那么唤醒之后会返回true,
        //如果没有调用interrupted方法，那么返回false
        return Thread.interrupted();
    }
1234567
```

到这里thread2就挂起了，如果thread3也来竞争锁的话，会出现下图情况
![在这里插入图片描述](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/20190609162506174.png)
最后thread2和thread3都会挂起