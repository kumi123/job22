# 大厂面试题：你知道 JUC 中的 Semaphore、CyclicBarrier、CountDownLatch 吗

# （一）概述

资源的分配方式有两种，一种是独占，比如之前讲的 ReentrantLock，另外一种是共享，即我们今天将要学习的 **Semaphore**、**CyclicBarrier** 以及 **CountDownLatch**。这些都是 JUC 包中的类。



# （二）Semaphore

==Semaphore 是信号量的意思，作用是控制访问特定资源的线程数量。==

其核心 API 为：



```
semaphore.acquire();semaphore.release();
```

这么说可能比较模糊，下面我举个例子。



Semaphore 就好比游乐园中的某个游乐设施的管理员，用来控制同时玩这个游乐设施的人数。比如跳楼机只能坐十个人，就设置 Semaphore 的 **permits** 等于 10。



每当有一个人来时，==首先判断 **permits** 是否大于 0，如果大于 0，就把一个许可证给这个人，同时自己的 permits 数量减一。==



==如果 **permits** 数量等于 0 了，其他人再想进来时就只能排队了。==



当一个人玩好之后，这个人把许可证还给 Semaphore，permits 加 1，正在排队的人再来竞争这一个许可证。



下面通过代码来演示这样一个场景



```java
public class SemaphoreTest {
    public static void main(String[] args) {
        //创建permits等于2
        Semaphore semaphore=new Semaphore(2);
        //开五个线程去执行PlayGame
        for (int i = 0; i < 5; i++) {
            new Thread(new PlayGame(semaphore)).start();
        }
    }

    static class PlayGame extends Thread{
        Semaphore semaphore;
        public PlayGame(Semaphore semaphore){
            this.semaphore=semaphore;
        }
        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName()+"获得一个许可证");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName()+"释放一个许可证");
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```

复制代码



在这里设置 ==Semaphore 的 permit 等于 2，表示同时只有两个线程可以执行，然后开五个线程，在执行前通过`semaphore.acquire();`获取 permit，执行后通过`semaphore.release();`归还 permit。==



![img](https://static001.geekbang.org/infoq/88/886fe326f0f7ddc397aefeb266b30e19.png)



通过结果可以观察到，==每次最多只会有两个线程执行 PlayGame 。==



# （三）Semaphore 原理

## 3.1 默认非公平锁

Semaphore 默认创建的是一个非公平锁：



![img](https://static001.geekbang.org/infoq/29/29ca346dd20d3534118cbe4be9a8ab5b.png)



## 3.2 Semaphore 源码分析

Semaphore 的实现方式和 ReentrantLock 十分类似。



首先==定义一个内部类 Sync 继承 AbstractQueuedSynchronizer==



![img](https://static001.geekbang.org/infoq/fc/fc8331017e7424babf8ea9e8c474ec9a.png)



从 Sync 的构造方法中可以看到，初始化时==设置 state 等于 permits，在讲 ReentrantLock 的时候，state 用来存储重入锁的次数，在 Semaphore 中 state 用来存储资源的数量。==



Semaphore 的核心方法是 acquire 和 release，当执行 acquire 方法时，sync 会执行一个获取一个共享资源的操作：



![img](https://static001.geekbang.org/infoq/b7/b78ff6a2999b24bc8ca41ca57eb2e3cc.png)



`acquire() 方法`

==核心是判断剩余数量是否大于 0，如果是的话就通过 cas 操作去获取资源，否则就进入队列中等待==

`release() 方法`

==当执行 release 方法时，sync 会执行一个将一个共享资源放回去的 cas 操作==



![img](https://static001.geekbang.org/infoq/69/693294906cf942998d59246118ee7557.png)



# （四）CountDownLatch

==countdownlatch 能够让一个线程等待其他线程工作完成之后再执行。==



countdownlatch 通过一个计数器来实现，初始值是指定的数量，每当一个线程完成自己的任务后，计数器减一，当计数器为 0 时，执行最后的等待线程。



其核心 API 为



```
CountDownLatch.countDown();
CountDownLatch.await();
```

复制代码



下面来看代码示例：



==设定 countDownLatch 初始值为 2，定义两个线程分别执行对应的方法，方法执行完毕后再执行`countDownLatch.countDown();` 这两个方法执行的过程中，主线程被`countDownLatch.await();`阻塞，只有等到其他线程都执行完毕之后才可执行。==



```java
public class CountDownLatchTest {
    
    public static void main(String[] args) throws InterruptedException {
        //设定初始值为2
        CountDownLatch countDownLatch=new CountDownLatch(2);
        //执行两个任务
        new Thread(new Task1(countDownLatch)).start();
        new Thread(new Task2(countDownLatch)).start();
        //在两个任务执行完之后才会执行await方法之后的代码
        countDownLatch.await();
        System.out.println("其余两个线程执行完之后执行");
    }

    private static class Task1 implements Runnable {
        private CountDownLatch countDownLatch;
        public Task1(CountDownLatch countDownLatch) {
            this.countDownLatch=countDownLatch;
        }

        @Override
        public void run() {
            System.out.println("执行任务一");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                if (countDownLatch!=null){
                    //执行完毕后调用countDown
                    countDownLatch.countDown();
                }
            }
        }
    }

    private static class Task2 implements Runnable {
        private CountDownLatch countDownLatch;
        public Task2(CountDownLatch countDownLatch) {
            this.countDownLatch=countDownLatch;
        }

        @Override
        public void run() {
            System.out.println("执行任务二");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                if (countDownLatch!=null){
                    //执行完毕后调用countDown
                    countDownLatch.countDown();
                }
            }
        }
    }
}
```

复制代码



效果如下：



![img](https://static001.geekbang.org/infoq/6a/6a4f3f2b38a4c8af1f8767b910c69ca7.png)



# （五）CyclicBarrier

==栅栏屏障，让一组线程到达一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续运行。==

其核心 API 为：



```
cyclicBarrier.await();
```

复制代码

==和 countdownlatch 的区别在于，countdownlatch 是一个线程等待其他线程执行完毕后再执行，CyclicBarrier 是每一个线程等待所有线程执行完毕后，再执行。==



==看代码，初始化 cyclicBarrier 为 3，两个子线程和一个主线程执行完时都会被阻塞在`cyclicBarrier.await();`代码前，等三个线程都执行完毕后再执行接下去的代码。==



```java
public class CyclicBarrierTest {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        CyclicBarrier cyclicBarrier=new CyclicBarrier(3);
        System.out.println("执行主线程");
        new Thread(new Task1(cyclicBarrier)).start();
        new Thread(new Task2(cyclicBarrier)).start();
        cyclicBarrier.await();
        System.out.println("三个线程都执行完毕，继续执行主线程");
    }

    private static class Task1 implements Runnable {
        private CyclicBarrier cyclicBarrier;
        public Task1(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier=cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("执行任务一");
            try {
                Thread.sleep(2000);
                cyclicBarrier.await();
                System.out.println("三个线程都执行完毕，继续执行任务一");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Task2 implements Runnable {
        private CyclicBarrier cyclicBarrier;
        public Task2(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier=cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("执行任务二");
            try {
                Thread.sleep(2000);
                cyclicBarrier.await();
                System.out.println("三个线程都执行完毕，继续执行任务二");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
```

复制代码



结果如下：



![img](https://static001.geekbang.org/infoq/ef/ef9f933f2d5dae00d9c7f3d29945e246.png)



cyclicBarrier 还可以重复执行，而不需要重新去定义。



```java
public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
    CyclicBarrier cyclicBarrier=new CyclicBarrier(3);
    //第一次
    System.out.println("执行主线程");
    new Thread(new Task1(cyclicBarrier)).start();
    new Thread(new Task2(cyclicBarrier)).start();
    cyclicBarrier.await();
    System.out.println("三个线程都执行完毕，继续执行主线程");
    //第二次
    System.out.println("执行主线程");
    new Thread(new Task1(cyclicBarrier)).start();
    new Thread(new Task2(cyclicBarrier)).start();
    cyclicBarrier.await();
}
```

复制代码



# （六）总结

归根结底，Semaphore、CyclicBarrier、CountDownLatch 三个类都是对 AQS 中资源共享的应用，学懂 AQS 之后，你会发现 JUC 包中的类变得不难了。好了，我们下期再见！