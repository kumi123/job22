sleep和wait的区别是面试中一个非常常见的问题，因为从表象来看，好像sleep和wait都能使==线程处于阻塞状态，但是却有着本质上的却别==。这篇文章就来好好分析一下。

整体的区别其实是有四个：

==1、sleep是线程中的方法，但是wait是Object中的方法。==

==2、sleep方法不会释放lock，但是wait会释放，而且会加入到等待队列中。==

==3、sleep方法不依赖于同步器synchronized，但是wait需要依赖synchronized关键字。==

==4、sleep不需要被唤醒（休眠之后推出阻塞），但是wait需要（不指定时间需要被别人中断）。==

下面我们就根据这四个区别来分析。

### 一、sleep是线程方法，wait是Object方法

这个如何验证呢？我们还需要到jdk源码中看看。首先进入到Thread的源码中看一下，然后俺ctrl+O就可以查看方法列表。在最上面可以搜寻，我们输入“s”，就可以查看所有以s开头的方法了。

![图片](https://mmbiz.qpic.cn/mmbiz_png/N8scgexEBuLHVEiawrKRibTzmFaiaicrB9GbNAcPaCOL9Yj8ibJOPodJCmVRCvOUPpGviaicLuNTLlHk2tmmvTRhaHrow/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



我们会发现，sleep方法真实的在Thread线程类中。下面我们以同样的方法查看wait。

![图片](https://mmbiz.qpic.cn/mmbiz_png/N8scgexEBuLHVEiawrKRibTzmFaiaicrB9GbjWnNhiaEmX96vTblAyicDp5jdzv7EnFG2emXRbjmcK3Y02tAzJxYWYfg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



这是第一个区别很容易验证，下面我们来看第二个。

### 二、sleep不释放lock，wait会释放

这个如何验证呢？这就需要代码了。先看我们的sleep方法

```java
public class Test {
    private final static Object lock = new Object();
    public static void main(String[] args) {
        Stream.of("线程1","线程2").forEach(n->new Thread(n) {
            public void run(){
                Test.testSleep();
            }
        }.start());
    }
    //sleep方法休眠之后，
    private static void testSleep() {
        synchronized (lock) {
            try {
                System.out.println(Thread.currentThread().getName()
                                   +"正在执行");
                Thread.sleep(10_000);
                System.out.println(Thread.currentThread().getName()
                                   +"休眠结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```

我们看一下运行结果：

![图片](https://mmbiz.qpic.cn/mmbiz_png/N8scgexEBuLHVEiawrKRibTzmFaiaicrB9GbZuuPtx9MeZgoH55Zn4QN5pO5Zgfr2Rjx7c8OvRjEYfQ9hsLzDpcJxA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



在上面的结果中，线程2先获取了cpu资源，然后开始执行休眠，在休眠过程中线程1是没法执行的，必须要等待线程2结束之后才可以。这也就是说sleep方法不会释放锁，让其他线程进来。

然后我们测试一下wait方法。

```java
public class Test {
    private final static Object lock = new Object();
    public static void main(String[] args) {
        Stream.of("线程1", "线程2").forEach(n -> new Thread(n) {
            public void run() {
                Test.testWait();
            }
        }.start());
    }
    private static void testWait() {
        synchronized (lock) {
            try {
                System.out.println(Thread.currentThread().getName()
                                   + "正在执行");
                lock.wait(10_000);
                System.out.println(Thread.currentThread().getName()
                                   + "wait结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```

在上面的例子中，我们使用wait方法等待10秒钟，然后结束。我们看一下结果：

![图片](https://mmbiz.qpic.cn/mmbiz_png/N8scgexEBuLHVEiawrKRibTzmFaiaicrB9GbS2eIziaF7eGDCsL1HIo5JOcbHvcCBI4rWfTZI4q5AMnMsiab66iaibRpzg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



这个过程就验证了第二条区别，我们接下来看第三个。

### 三、sleep不依赖同步方法，wait需要

我们还是依次来验证。首先我们测试sleep方法。

```JAVA
public class Test2 {
    private final static Object lock = new Object();
    public static void main(String[] args) {
        Stream.of("线程1", "线程2").forEach(n -> new Thread(n) {
            public void run() {
                Test2.testSleep();
            }
        }.start());
    }
    private static void testSleep() {
        try {
            Thread.sleep(10_000);
            System.out.println("休眠结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

这个方法会依次运行，不会出现任何异常。然后我们主要是看wait方法。

```java
public class Test2 {
    private final static Object lock = new Object();
    public static void main(String[] args) {
        Stream.of("线程1", "线程2").forEach(n -> new Thread(n) {
            public void run() {
                Test2.testSleep();
            }
        }.start());
    }
    private static void testWait() {
        try {
            lock.wait(10_000);
            System.out.println("wait结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

我们运行一下，看一下结果：

![图片](https://mmbiz.qpic.cn/mmbiz_png/N8scgexEBuLHVEiawrKRibTzmFaiaicrB9Gbja7XokhMDibdAmJ7PdxrHyK4k1DJfNyib6quQv5qDxzdic5Aiar2dqQB5Q/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



OK，下面我们验证一下第四条区别：

### 四、sleep不需要被唤醒，wait需要

sleep方法很简单，我们主要关注wait方法。看代码：

首先我们定义两个方法，一个等待方法，一个唤醒方法。

```java
public class Test2 {
    private final static Object lock = new Object();
    private static void testWait() {
        synchronized (lock) {
            try {
                System.out.println("我一直在等待");
                lock.wait();
                System.out.println("wait被唤醒了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private static void notifyWait() {
        synchronized (lock) {
            try {
                Thread.sleep(5_000);
                lock.notify();
                System.out.println("休眠5秒钟唤醒wait");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```

然后再去测试一下：

```java
public class Test2 {
    private final static Object lock = new Object();
    public static void main(String[] args) {
        new Thread() {//这个线程一直在等待
            public void run() {
                Test2.testWait();
            }
        }.start();
        new Thread() {//这个线程准备去唤醒
            public void run() {
                Test2.notifyWait();
            }
        }.start();
    }
}
```

如果没有唤醒方法，那第一个线程就会处于一直等待的状态，第二个线程唤醒了之后就不再等待了。

![图片](https://mmbiz.qpic.cn/mmbiz_png/N8scgexEBuLHVEiawrKRibTzmFaiaicrB9Gb1MER0eibN8y4bhic6lw0ZYCjHUCcfB4QYUxqhRekcfDqKB78aDsia9DDg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)