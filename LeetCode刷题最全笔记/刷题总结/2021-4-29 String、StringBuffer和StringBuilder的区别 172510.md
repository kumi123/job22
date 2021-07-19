# [String、StringBuffer和StringBuilder的区别](https://segmentfault.com/a/1190000022038238)



#### 1.String长度不可变而StringBuffer和SringBuilder长度可变

#### 2.他们的运行速度不同 ：SringBuilder > StringBuffer > String

#### 3.SringBuilder **线程不安全** 和 StringBuffer线程安全

下面我来一个一个解释：

### 一.String长度不可变而StringBuffer和SringBuilder长度可变

废话不多说，先上源码！

如果你可以打开三个类的源码看一下你就明白了

![String.png](https://segmentfault.com/img/bVbEDi8)

![StringBuffer.png](https://segmentfault.com/img/bVbEDjc)

![StringBuilder.png](https://segmentfault.com/img/bVbEDjd)

![AbstractStringBuilder.png](https://segmentfault.com/img/bVbEDjo)
我们能看到，String这个类底层使用了==final修饰的长度不可变的字符数组，所以它长度不可变==

> ```
> private final char value[];
> ```

而==StringBuffer和StringBuilder 都继承自AbstractStringBuilder ，且AbstractStringBuilder底层使用的是可变字符数组，所以二者长度可变==。

> ```
> char[] value;
> ```

### 二.他们的运行速度不同 ：SringBuilder > StringBuffer > String

先来看这样一段代码

```
public class MainTest {
    public static void main(String[] args) {
        String  str = "abc";
        System.out.println(str);
        str = str + "cd";
        System.out.println(str);
    }
}
```

> 输出结果为：
>
> abc
>
> abcd

 整个程序运行完我们看似是str这个对象被更改了，在后面加上了一段新的字符，但这只是假象，因为我们刚才说过String类型的字符串长度是不可变的啊，其实JVM是先创建的了一个str对象，将“abc”赋值给str，然后在内存中又创建了第二个str对象，将第一个str对象中的“abc”与”de“相加再赋值给第二个str对象，此时Java虚拟机的垃圾回收机制开始其工作将第一个str对象回收。所以说String类型的字符串要完成这样”改变长度“的操作需要不断地创建再回收，创建再回收，无形中经过了很多步骤，而 StringBuffer和SringBuilder数组可变，直接可进行更改，所以要更快。

**而SringBuilder 为什么比 StringBuffer 要快呢？**

先来看源码：

![BuilderAppend.png](https://segmentfault.com/img/bVbEDjp)

![BufferAppend.png](https://segmentfault.com/img/bVbEDjq)
​ ==从图中可以看出**StringBuffer的append的方法都被toStringCache关键字修饰了**（不止图中这两个append方法包括StringBuffer源码中所有append重载方法都被toStringCache修饰了。）==

==**toStringCache关键字是给线程加锁，枷锁是会带来性能上的损耗的，故用SringBuilder 比 StringBuffer 要快==**

锁不懂先没关系，往下看！暂且理解为什么快。

### 三.SringBuilder **线程不安全** 和 StringBuffer线程安全

 线程安全不同的问题要和刚才的的思路连起来，正是因为有了toStringCache关键字修饰StringBuffer的append方法有，给线程加了锁加了锁所以线程安全。

 这样理解，**如果一个StringBuffer对象的字符串在字符串缓冲区被多个线程同时使用时，也就是多个线程同时操作，这样会有出现错误操作的概率，为了保证线程的安全性，进行加锁，这样会使同一时间只有一个线程获得权限，其他线程必须等待该线程结束并释放锁才能获得权限，**这样线程非常安全，虽然效率慢了点，但是当项目安全性要求很高时就必须用StringBuffer。单一线程下还是的用更快一点的SringBuilder 。