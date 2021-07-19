# [jconsole和jstack定位死锁问题](https://segmentfault.com/a/1190000039138241)



## 什么是死锁

> 死锁问题是多线程特有的问题，它可以被认为是线程间切换消耗系统性能的一种极端情况。 在死锁时，线程间相互等待资源，而又不释放自身的资源，导致无穷无尽的等待，其结果是系统任务永远无法执行完成。 死锁问题是在多线程开发中应该坚决避免和杜绝的问题.

## 死锁示例代码

```
package com.rumenz.learn.deadLock;

public class RumenzThread implements Runnable{
    int a,b;

    public RumenzThread(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
       //Integer.valueOf(a) 包装成对象
        synchronized (Integer.valueOf(a)){
            try{
                //睡眠3秒,增加死锁的几率
                Thread.sleep(3000);

            }catch (Exception e){
                e.printStackTrace();
            }
            synchronized (Integer.valueOf(b)){
                System.out.println("a+b="+(a+b));
            }
        }

    }
}

package com.rumenz.learn.deadLock;

public class DeadLock {

    public static void main(String[] args) {
        new Thread(new RumenzThread(1, 2)).start();
        new Thread(new RumenzThread(2, 1)).start();

    }
}
```

## 运行程序使用`jstack -l pid`来定位死锁

### 先找到死锁程序的进程id

```
> jps
56993 Jps
56636 Launcher
57066 DeadLock  //这个就是死锁的进程
```

### 使用`jstack -l 57066`来定位死锁

```
> jstack -l 57066


Found one Java-level deadlock:
=============================
"Thread-1":
  waiting to lock monitor 0x00007fbe6d80de18 (object 0x000000076ab33988, a java.lang.Integer),
  which is held by "Thread-0"
"Thread-0":
  waiting to lock monitor 0x00007fbe6d8106a8 (object 0x000000076ab33998, a java.lang.Integer),
  which is held by "Thread-1"

Java stack information for the threads listed above:
===================================================
"Thread-1":
    at com.rumenz.learn.deadLock.RumenzThread.run(RumenzThread.java:27)
    - waiting to lock <0x000000076ab33988> (a java.lang.Integer)
    - locked <0x000000076ab33998> (a java.lang.Integer)
    at java.lang.Thread.run(Thread.java:748)
"Thread-0":
    at com.rumenz.learn.deadLock.RumenzThread.run(RumenzThread.java:27)
    - waiting to lock <0x000000076ab33998> (a java.lang.Integer)
    - locked <0x000000076ab33988> (a java.lang.Integer)
    at java.lang.Thread.run(Thread.java:748)

Found 1 deadlock. //发现一个死锁
```

> `RumenzThread.java:27` 定位到大概的代码文件位置。

## `jconsole`定位死锁问题

- 找到死锁进程

![image-20210131232115142](https://segmentfault.com/img/remote/1460000039138243)

- 链接,不安全的链接

  ![image-20210131232205381](https://segmentfault.com/img/remote/1460000039138246)

- 选择线程

  ![image-20210131232346154](https://segmentfault.com/img/remote/1460000039138245)

- 点击检测死锁

  ![image-20210131232434316](https://segmentfault.com/img/remote/1460000039138244)