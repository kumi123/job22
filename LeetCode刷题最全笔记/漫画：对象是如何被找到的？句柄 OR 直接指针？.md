# 漫画：对象是如何被找到的？句柄 OR 直接指针？



![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f49eec9aa?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f4ade4556?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f4b3a820e?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f4b5f527f?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f4d735ce6?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f4f0d871d?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img]()

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f673b7614?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f6b658893?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f6f1aca88?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f6fda5541?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f704b2dfc?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)





![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f7deaab5b?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)





![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f88116c57?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f894c9e40?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f8e1079d6?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f914f77f2?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1f97109ae7?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

![img](https://user-gold-cdn.xitu.io/2020/6/30/17302d1fa1714388?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



> 小贴士：想要使用并定位 Java 对象，就要用到 Java 虚拟机栈（Java Virtual Machine Stack），它描述的是 Java 方法执行的线程内存模型：每个方法被执行的时候，Java 虚拟机都会同步创建一个栈帧（Stack Frame）用于存储局部变量表、操作数栈、动态连接、方法出口等信息。每一个方法被调用直至执行完毕的过程，就对应着一个栈帧在虚拟机栈中从入栈到出栈的过程。

## 代码解读

以下面代码为例，来说明对象定位的过程：

```
class Bus extends Car {
    private String code;
    private String color;
    Bus(String code, String color) {
        this.code = code;
        this.color = color;
    }
    // 省略其他方法...
}
public class ReferenceTest {
    Bus myBus = new Bus("Java中文社群", "蓝色");
}
复制代码
```

以官方默认的 HotSpot 虚拟机来说， `myBus` 就是存储在本地变量表中 reference 类型的变量， `new Bus("Java中文社群", "蓝色")` 就是存储在 Java 堆中的对象实例数据，它存储了此实体类的所有字段信息，例如 `code="Java中文社群"` 以及 `color="蓝色"` 等信息，而 Java 堆中的还存储着对象类型数据的地址，它存储的是对象的类型信息，还有它的父类信息等。

## 总结

由于 reference 类型在《Java虚拟机规范》里面只规定了它是一个指向对象的引用，并没有定义这个引用应该通过什么方式去定位、访问到堆中对象的具体位置，所以对象访问方式也是由虚拟机实现而定的，主流的访问方式主要有使用句柄和直接指针两种：

- ==如果使用句柄访问的话，Java 堆中将可能会划分出一块内存来作为句柄池，reference 中存储的就是对象的句柄地址，而句柄中包含了对象实例数据与类型数据各自具体的地址信息；==
- ==如果使用直接指针访问的话，Java 堆中对象的内存布局就必须考虑如何放置访问类型数据的相关信息，reference 中存储的直接就是对象地址，如果只是访问对象本身的话，就不需要多一次间接访问的开销。==

因此使用句柄来访问的最大好处就是 reference 中存储的是稳定句柄地址，在对象被移动（垃圾收集时移动对象是非常普遍的行为）时只会改变句柄中的实例数据指针，而 reference 本身不需要被修改。使用直接指针访问速度更快，但如果对象被移动则需要修改 reference 本身。

由于对象访问在 Java 中非常频繁，因此这类开销积少成多也是一项极为可观的执行成本，所以官方默认的 HotSpot 虚拟机采用的就是「**直接指针**」来定位对象的。