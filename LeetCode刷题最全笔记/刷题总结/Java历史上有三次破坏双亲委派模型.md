## 读者美团五面：Java历史上有三次破坏双亲委派模型，是哪三次？



![图片](https://mmbiz.qpic.cn/mmbiz_png/eSdk75TK4nFKOUjZxbnrDTM4NOKfEbbY8T5393jcAGd1kiaedaiajYplRJZ8pmPV8aLTps77x2fjE8BvEz9NyzmA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

其实不止三次，有四次。

今天我们就来盘一盘这个面试题，不过在说双亲委派模型之前，我们得先简单了解下类加载。

# 类加载

我们平常写的代码是保存在一个 `.java`文件里面，经过编译会生成`.class`文件，这个文件存储的就是字节码，如果要用上我们的代码，那就必须把它加载到 JVM 中。

![图片](https://mmbiz.qpic.cn/mmbiz_png/eSdk75TK4nFKOUjZxbnrDTM4NOKfEbbYfic4fULJx2oJg5VMNuez5Gb3szkRgIbz3ejjJmcmicnED4U0jA1DevbQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

当然，加载到 JVM 生成 class 对象的来源不一定得是`.class`文件，也可以来自网络等等，反正只要是符合 JVM 规范的都行。

而类加载的步骤主要分为：加载、链接、初始化。

## 加载

==其实就是找到字节流，然后将其加载到 JVM 中，生成类对象。这个阶段就是类加载器派上用场的阶段，等下我们再细说。==

## 链接

这个阶段是要让生成的类对象融入到 JVM 中，分别要经历以下三个步骤：

![图片](https://mmbiz.qpic.cn/mmbiz_png/eSdk75TK4nFKOUjZxbnrDTM4NOKfEbbYYfSlzchcpJy98iaiafJicSkCFP5XyPXRK8mrvPBeKIbD3Sa4QJaUxB5cQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

验证就是检验一下==加载的类是否满足 JVM 的约束条件，也就是判断是否合规==。

准备就是为==加载类的静态变量申请内存空间，并赋予初始值==，例如是 int 类型那初始值就是 0。

解析就是将==符号引用解析成为实际引用==，讲人话就是：例如 Yes 类里面引用了一个 XX 类，那一开始 Yes 类肯定不知道 XX 类在内存里面的地址，所以就先搞个符号引用替代一下，假装知道，等类加载解析的时候再找到 XX 类真正地址，做一个实际引用。

这就是解析要做的事情。还有一点，虽说把解析放到链接阶段里面，但是 JVM 规范并没有要求在链接过程中完成解析。

## 初始化

这个阶段就是为==常量字段赋值，然后执行静态代码块==，将一堆要执行的静态代码块方法==包装成 `clinit` 方法执行，这个方法会加锁，由 JVM 来保证 `clinit` 方法只会被执行一次==。

所以可以用一个内部静态类来实现延迟初始化的单例设计模式，同时保证了线程安全。

这个阶段完毕之后，类加载过程就 ok 了，可以投入使用啦，再来画个图汇总一下：

![图片](https://mmbiz.qpic.cn/mmbiz_png/eSdk75TK4nFKOUjZxbnrDTM4NOKfEbbYb3UJUjwUS4r41Rh91icic7ItYQJFSx9R0ROCeZWoh8xiaj6cDfIv14BnA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

# 双亲委派模型

加载阶段，需要用到类加载器来将 class 文件里面的内容搞到 JVM 中生成类对象。

## 那什么是双亲委派模型？

双亲委派模型用一句话讲就是子类加载器先让父类加载器去查找该类来加载，父类又继续请求它的父类直到最顶层，在父类加载器没有找到所请求的类的情况下，子类加载器才会尝试去加载，这样一层一层上去又下来。

![图片](https://mmbiz.qpic.cn/mmbiz_png/eSdk75TK4nFKOUjZxbnrDTM4NOKfEbbYsT6rT2trG4KnTxF6OWd0nxkibRf77ia9S2dHJkuo428VuRKCfgsuhufg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

每个类加载器都有固定的查找类的路径，在 JDK8 的时候一共有三种类加载器。

1. ==启动类加载器==(Bootstrap ClassLoader)，它是属于虚拟机自身的一部分，用 C++ 实现的，主要负责加载`<JAVA_HOME>\lib`目录中或被 -Xbootclasspath 指定的路径中的并且文件名是被虚拟机识别的文件。它是所有类加载器的爸爸。
2. ==扩展类加载器==(Extension ClassLoader)，它是 Java 实现的，独立于虚拟机，主要负责加载`<JAVA_HOME>\lib\ext`目录中或被 java.ext.dirs 系统变量所指定的路径的类库。
3. ==应用程序类加载器(==Application ClassLoader)，它是Java实现的，独立于虚拟机。主要负责加载用户类路径(classPath)上的类库，如果我们没有实现自定义的类加载器那这玩意就是我们程序中的默认加载器。

![图片](https://mmbiz.qpic.cn/mmbiz_png/eSdk75TK4nFKOUjZxbnrDTM4NOKfEbbY06Vz1lsJDicdlTE9sFCgQOusEm6FyZIBLTFFpfDzdIEcTiajrqXiaV1dA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

## 为什么要提出双亲委派模型？

其实就是为了让基础类得以正确地统一地加载。

从上面的图可以看出，如果你也定义了一个 `java.lang.Object`类，通过双亲委派模式是会把这个请求委托给启动类加载器，它扫描`<JAVA_HOME>\lib`目录就找到了 jdk 定义的 `java.lang.Object` 类来加载，所以压根不会加载你写的 `java.lang.Object`类，这就可以避免一些程序不小心或者有意的覆盖基础类。

至此我们已经清楚了什么是双亲委派，和为什么要双亲委派。接下来我们来看看三次破坏。

# 第一次破坏

在 ==jdk 1.2 之前==，那时候还==没有双亲委派模型==，不过已经有了 ClassLoader 这个抽象类，所以已经有人继承这个抽象类，==重写 loadClass 方法来实现用户自定义类加载器==。

而在 1.2 的时候要引入双亲委派模型，为了向前兼容， loadClass 这个方法还得保留着使之得以重写，新搞了个 findClass 方法让用户去重写，并呼吁大家不要重写 loadClass 只要重写 findClass。

这就是第一次对双亲委派模型的破坏，==因为双亲委派的逻辑在 loadClass 上，但是又允许重写 loadClass，重写了之后就可以破坏委派逻辑了==。

# 第二次破坏

第二次破坏指的是 JNDI、==JDBC 之类的情况==。

首先得知道什么是 ==SPI(Service Provider Interface)，它是面向拓展的==，也就是说我定义了个规矩，就是 SPI ，具体如何实现由扩展者实现。

像我们比较熟的 JDBC 就是如此。

MySQL 有 MySQL 的 JDBC 实现，Oracle 有 Oracle 的 JDBC 实现，我 Java 不管你内部如何实现的，反正你们这些数据库厂商都得统一按我这个来，这样我们 Java 开发者才能容易的调用数据库操作，所以在 ==Java 核心包里面定义了这个 SPI（jdbc的SPI）==。

而==核心包里面的类都是由启动类加载器去加载的，但它的手只能摸到`<JAVA_HOME>\lib`或Xbootclasspath指定的路径中，其他的它鞭长莫及。==

![图片](https://mmbiz.qpic.cn/mmbiz_png/eSdk75TK4nFKOUjZxbnrDTM4NOKfEbbYDF9X6KeFoaS3CSchOTWmhcjLaGfGibXrD1FcjjuU600iagaESGC3hG8w/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

而 ==JDBC 的实现类在我们用户定义的 classpath 中==，只能==由应用类加载器去加载，所以启动类加载器只能委托子类来加载数据库厂商们提供的具体实现，这就违反了自下而上的委托机制。==

具体解决办法是搞了个线程上下文类加载器，通过`setContextClassLoader()`默认情况就是应用程序类加载器，然后利用`Thread.current.currentThread().getContextClassLoader()`获得类加载器来加载。

这就是第二次破坏双亲委派模型。

# 第三次破坏

这次破坏是为了==满足热部署的需求==，不停机更新这对企业来说至关重要，毕竟停机是大事。

OSGI 就是==利用自定义的类加载器机制来完成模块化热部署==，而它==实现的类加载机制就没有完全遵循自下而上的委托，有很多平级之间的类加载器查找==，具体就不展开了，有兴趣可以自行研究一下。

这就是第三次破坏。

# 第四次破坏

在 JDK9 引入模块系统之后，类加载器的实现其实做了一波更新。

像扩展类加载器被重命名为平台类加载器，核心类加载归属了做了一些划分，平台类加载器承担了更多的类加载，上面提到的  -Xbootclasspath、java.ext.dirs 也都无效了，rt.jar 之类的也被移除，被整理存储在 jimage 文件中，通过新的 JRT 文件系统访问。

当收到类加载请求，会先判断该类在具名模块中是否有定义，如果有定义就自己加载了，没的话再委派给父类。

关于 JDK9 相关的知识点就不展开了，有兴趣的自行查阅。

所以这就是第四次破坏。

# 其他注意点

首先，虽说是子类父类，但是加载器之间的关系不是继承，而是组合。

![图片](https://mmbiz.qpic.cn/mmbiz_png/eSdk75TK4nFKOUjZxbnrDTM4NOKfEbbYpgnDhjZMYOpwAXC1NC6MqpjCWuCrnxibU51furvahc3jkz0MEeM9IEQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

看下代码就很清晰了，具体的逻辑如下：

![图片](https://mmbiz.qpic.cn/mmbiz_png/eSdk75TK4nFKOUjZxbnrDTM4NOKfEbbYtqtiaiaklsebnvicVDppv8BMOUwmImwfhwzlFovxFZk9F8RGDV1xRAKvw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

在 JVM 中，==类的唯一性是由类加载器实例和类的全限定名一同确定的==，也就是说即使是同一个类文件加载的类，用不同的类加载器实例加载，在 JVM 看来这也是两个类。

所以说类加载器还有命名空间的作用，我记得这个知识点也是一个面试题哟～