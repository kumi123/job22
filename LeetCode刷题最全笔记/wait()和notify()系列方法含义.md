一.wait()和notify()系列方法含义

- wait()方法是让当前线程等待的，即让线程释放了对共享对象的锁。

- wait(long timeout)方法可以指定一个超时时间，过了这个时间如果没有被notify()唤醒，则函数还是会返回。如果传递一个负数timeout会抛出IllegalArgumentException异常。

- notify()方法会让调用了wait()系列方法的一个线程释放锁，并通知其它正在等待（调用了wait()方法）的线程得到锁。


- notifyAll()方法会唤醒所有在共享变量上由于调用wait系列方法而被挂起的线程。


注意：

- 调用wait()、notify()方法时，当前线程必须要成功获得锁（必须写在同步代码块锁中），否则将抛出异常。
- 只对当前单个共享变量生效，多个共享变量需要多次调用wait()方法。
- 如果线程A调用wait()方法后处于堵塞状态时，其他线程中断（在其他线程调用A.interrupt()方法）A线程，则会抛出InterruptExcption异常而返回并终止。
- 理论内容就这些，下面将上述内容用实例展示给大家，并一步一步带着大家分析和实现这两个方法，多线程中这两个方法会让程序跳跃执行，所以一定要搞清楚代码的执行流程。

二.标准代码示例
1.代码实现内容流程描述：

- 创建两个线程Thread0和Thread1。
- 让Thread0执行wait()方法。
- 此时Thread1得到锁，再让Thread1执行notify()方法释放锁。
- 此时Thread0得到锁，Thread0会自动从wait()方法之后的代码，继续执行。
- 通过上述流程，我们就可以清楚的看到，wait()和notify()各自是怎么工作的了，也可以知道两者是怎么配合的了。

2.代码实现：

```java
public class ThreadWaitAndNotify {
// 创建一个将被两个线程同时访问的共享对象
public static Object object = new Object();

// Thread0线程，执行wait()方法
static class Thread0 extends Thread {

	@Override
	public void run() {
		synchronized (object) {
			System.out.println(Thread.currentThread().getName() + "初次获得对象锁，执行中，调用共享对象的wait()方法...");
			try {
				// 共享对象wait方法，会让线程释放锁。
				object.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + "再次获得对象锁，执行结束");
		}
	}

}

// Thread1线程，执行notify()方法
static class Thread1 extends Thread {

	@Override
	public void run() {
		synchronized (object) {
			// 线程共享对象，通过notify()方法，释放锁并通知其他线程可以得到锁
			object.notify();
			System.out.println(Thread.currentThread().getName() + "获得对象锁，执行中，调用了共享对象的notify()方法");
		}
	}
}

// 主线程
public static void main(String[] args) {
	Thread0 thread0 = new Thread0();
	Thread1 thread1 = new Thread1();
	thread0.start();
	try {
		// 保证线程Thread0中的wait()方法优先执行，再执线程Thread1的notify()方法
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	thread1.start();
}
```

```java
3.运行结果

Thread-0初次获得对象锁，执行中，调用共享对象的wait()方法...
Thread-1获得对象锁，执行中，调用了共享对象的notify()方法
Thread-0再次获得对象锁，执行结束
```

4.运行流程详解：
从执行的结果中，要明白线程的执行顺序：

- Thread0调用了wait()方法后，会释放掉对象锁并暂停执行后续代码，即从wait()方法之后到run()方法结束的代码，都将立即暂停执行，这就是wait()方法在线程中的作用。
- CPU会将对象锁分配给一直等候的Thread1线程，Thread1执行了notify()方法后，会通知其他正在等待线程（Thread0）得到锁，但会继续执行完自己锁内的代码之后，才会交出锁的控制权。
- 因为本例只有两个线程，所以系统会在Thread1交出对象锁控制权后（Synchronized代码块中代码全部执行完后），把锁的控制权给Thread0（若还有其他线程，谁得到锁是随机的，完全看CPU心情），Thread0会接着wait()之后的代码，继续执行到Synchronized代码块结束，将对象锁的控制权交还给CPU。