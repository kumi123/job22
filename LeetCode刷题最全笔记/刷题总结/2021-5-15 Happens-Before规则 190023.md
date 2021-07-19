# Happens-Before规则

Happens-Before规则

# 1. 前言

从 ==JDK 5开始==，Java 使用新的 JSR-133 ==内存模型，使用 happens-before 的概念来阐述操作间的可见性==。

# 2. 定义

JSR-133 对Happens-Before 的定义：

> **Happens-Before Relationship** Two actions can be ordered by a happens-before relationship. If one action > happens-before another, then the ﬁrst is visible to and ordered before the second. It should be stressed that a happens-before relationship between two actions does not imply that those actions must occur in that order in a Java platform implementation. The happens-before relation mostly stresses orderings between two actions that conﬂict with each other, and deﬁnes when data races take place. There are a number of ways to induce a happens-before ordering, including:
>
> - Each action in a thread happens-before every subsequent action in that thread.
> - An unlock on a monitor happens-before every subsequent lock on that monitor.
> - A write to a volatile ﬁeld happens-before every subsequent read of that volatile.
> - A call to start() on a thread happens-before any actions in the started thread.
> - All actions in a thread happen-before any other thread successfully returns from a join() on that thread.
> - If an action a happens-before an action b, and b happens before an action c, then a happensbefore c.

**定义：** 如果==一个操作happens-before另一个操作，那么意味着第一个操作的结果对第二个操作可见==，==而且第一个操作的执行顺序将排在第二个操作的前面==。 两个操作之间存在happens-before关系，并==不意味着Java平台的具体实现必须按照happens-before关系指定的顺序来执行==。如果重排序之后的结果，与按照happens-before关系来执行的结果一致，那么这种重排序并不非法（也就是说，JMM允许这种重排序）。具体规则如下：

- 程序顺序规则：一个线程中的每个操作，happens-before于该线程中的任意后续操作。
- 监视器锁规则：对一个锁的解锁，happens-before于随后对这个锁的加锁。
- volatile变量规则：对一个volatile域的写，happens-before于任意后续对这个volatile域的读。
- 线程启动规则：如果线程A执行操作ThreadB.start()（启动线程B），那么A线程的ThreadB.start()操作happens-before于线程B中的任意操作。
- 线程终结规则：如果线程A执行操作ThreadB.join()并成功返回，那么线程B中的任意操作happens-before于线程A从ThreadB.join()操作成功返回。
- 传递性规则：如果A happens-before B，且B happens-before C，那么A happens-before C。

注：说明一下，网上搜出来有的是8条规则，我不知道还有两条哪儿来的，JSR-133 里面只有这六条。网上的还有下面两条：

- 线程中断操作：对线程interrupt()方法的调用，happens-before于被中断线程的代码检测到中断事件的发生，可以通过Thread.interrupted()方法检测到线程是否有中断发生。
- 对象终结规则：一个对象的初始化完成，happens-before于这个对象的finalize()方法的开始。

# 3. 再具体

==程序顺序规则==：==一段代码在单线程中执行的结果是有序的==。注意是==执行结果==，因为虚拟机、处理器会对指令进行重排序。虽然==重排序了，但是并不会影响程序的执行结果，所以程序最终执行的结果与顺序执行的结果是一致的==。故而这个规则只对**单线程**有效，在多线程环境下无法保证正确性。

==监视器锁规则==：这个规则比较好理解，==无论是在单线程环境还是多线程环境，一个锁处于被锁定状态，那么必须先执行unlock操作后面才能进行lock操作==。

volatile变量规则：这是一条比较重要的规则，它标志着volatile保证了线程可见性。通俗点讲就是==如果一个线程先去写一个volatile变量，然后一个线程去读这个变量，那么这个写操作一定是happens-before读操作的==。

线程启动规则：假定线程A在执行过程中，通过执行ThreadB.start()来启动线程B，那么==线程A对共享变量的修改在接下来线程B开始执行后确保对线程B可见==。

线程终结规则：假定==线程A在执行的过程中，通过制定ThreadB.join()等待线程B终止，那么线程B在终止之前对共享变量的修改在线程A等待返回后可见==。

==传递性规则：提现了happens-before原则具有传递性。==

**特别强调happens-hefore不能理解为“时间上的先后顺序”**。 我们来看如下代码：

```
public class VolatileTest {
    private int a = 0;
    private int getA() {
        return a;
    }
    private void setA(int a) {
        this.a = a;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            VolatileTest volatileTest = new VolatileTest();
            Thread thread1 = new Thread(() -> {
                volatileTest.setA(10);
            });
            thread1.start();

            Thread thread2 = new Thread(() -> {
                System.out.print(volatileTest.getA()+" ");
            });
            thread2.start();
        }
    }
}
复制代码
```

上面代码就是一组简单的setter/getter方法，现在假设现在有两个线程 thread1 和 thread2，线程 thread1 先(这里指时间上的先执行)执行setA(10)，然后线程 thread2 访问同一个对象的getA()方法，那么此时线程B收到的返回值是对少呢？

**答案：不确定**

```
0 0 0 0 10 0 10 10 10 0 10 0 10 10 10 10 10 0 10 10 0 0 0 10 0 10 10 10 0 10 0 10 10 10 0 10 10 0 10 10 10 0 0 10 10 0 10 0 10 10 10 10 10 10 10 10 10 10 0 0 0 10 10 0 10 0 10 0 0 0 10 10 0 10 10 10 10 10 10 10 10 10 10 10 0 10 10 10 0 10 10 10 10 10 0 10 0 10 0 0 
复制代码
```

虽然线程 thread1 在时间上先于线程 thread2 执行，但是由于代码完全不适用happens-before规则，因此我们无法确定先 thread2 收到的值时多少。也就是说上面代码是线程不安全的。

# 4. Happens-Before与JMM的关系



![JMM的设计图](https://user-gold-cdn.xitu.io/2019/11/29/16eb62243a4096fe?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



从图可以看出：

- JMM向程序员提供的happens-before规则能满足程序员的需求。JMM的happens-before规则不但简单易懂，而且也向程序员提供了足够强的内存可见性保证（有些内存可见性保证其实并不一定真实存在，比如上面的A happens-before B）。
- JMM对编译器和处理器的束缚已经尽可能少。从上面的分析可以看出，==JMM其实是在遵循一个基本原则：只要不改变程序的执行结果（指的是单线程程序和正确同步的多线程程序），编译器和处理器怎么优化都行。==例如，如果编译器经过细致的分析后，认定==一个锁只会被单个线程访问，那么这个锁可以被消除==。再如，如果编译器经过细致的分析后，==认定一个volatile变量只会被单个线程访问，那么编译器可以把这个volatile变量当作一个普通变量来对待。这些优化既不会改变程序的执行结果，又能提高程序的执行效率==。



![happens-before与JMM的关系](https://user-gold-cdn.xitu.io/2019/11/29/16eb62243ab06a04?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



一个happens-before规则对应于一个或多个编译器和处理器重排序规则。对于Java程序员来说，happens-before规则简单易懂，它避免Java程序员为了理解JMM提供的内存可见性保证而去学习复杂的重排序规则以及这些规则的具体实现方法.

# 5. 小结&参考资料

## 小结

时间先后顺序与happens-before原则之间基本没有太大的关系，所以我们在衡量并发安全问题的时候不要受到时间顺序的干扰，一切必须以happens-before原则为准。

简单的说，happens-before 规则就是为了让程序猿更好的理解 JMM 提供的内存可见性而编写的规则，让程序猿能避免去学习编译器和底层编译原理的重排序规则。