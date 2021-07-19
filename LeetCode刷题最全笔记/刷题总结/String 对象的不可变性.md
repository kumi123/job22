### String 对象的不可变性

从我们知道`String`对象的那一刻起，我想大家都知道了`String`对象是不可变的。那它不可变是怎么做到的呢？`Java` 这么做能带来哪些好处？我们一起来简单的探讨一下，先来看看`String` 对象的一段源码：

```
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** The value is used for character storage. */
    private final char value[];

    /** Cache the hash code for the string */
    private int hash; // Default to 0

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -6849794470754667710L;
    }
复制代码
```

从这段源码中可以看出，`String`类用了 final 修饰符，我们知道当一个类被 final 修饰时，表明这个类不能被继承，所以`String`类不能被继承。这是`String`不可变的第一点

再往下看，用来存储字符串的`char value[]`数组被`private` 和`final`修饰，我们知道对于一个被`final`的基本数据类型的变量，则其数值一旦在初始化之后便不能更改。这是`String`不可变的第二点。

Java 公司为什么要将`String`设置成不可变的，主要从以下三方面考虑：

- 1、保证 String 对象的安全性。假设 String 对象是可变的，那么 String 对象将可能被恶意修改。
- 2、保证 hash 属性值不会频繁变更，确保了唯一性，使得类似 HashMap 容器才能实现相应的 key-value 缓存功能。
- 3、可以实现字符串常量池


