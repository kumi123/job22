## ThreadLocal就是这么简单

原创 Java3y [Java3y](javascript:void(0);) *2018-04-03*

收录于话题

\#三歪教你多线程

12个

# 前言

今天要研究的是ThreadLocal，这个我在一年前学习JavaWeb基础的时候接触过一次，当时在baidu搜出来的第一篇博文ThreadLocal，在评论下很多开发者认为那博主理解错误，给出了很多有关的链接来指正(可原博主可能没上博客了，一直没做修改)。我也去学习了一番，可惜的是当时还没有记录的习惯，直到现在仅仅记住了一些当时学过的皮毛。

因此，做一些技术的记录是很重要的～同时，ThreadLocal也是面试非常常见的面试题，对Java开发者而言也是一个必要掌握的知识点～

当然了，如果我有写错的地方请大家多多包涵，欢迎在评论下留言指正～

# 一、什么是ThreadLocal

> 声明：本文使用的是JDK 1.8

首先我们来看一下JDK的文档介绍：

```
/**
 * This class provides thread-local variables.  These variables differ from
 * their normal counterparts in that each thread that accesses one (via its
 * {@code get} or {@code set} method) has its own, independently initialized
 * copy of the variable.  {@code ThreadLocal} instances are typically private
 * static fields in classes that wish to associate state with a thread (e.g.,
 * a user ID or Transaction ID).
 * 
 * <p>For example, the class below generates unique identifiers local to each
 * thread.
 * A thread's id is assigned the first time it invokes {@code ThreadId.get()}
 * and remains unchanged on subsequent calls.
 */      
```

结合我的总结可以这样理解：==ThreadLocal提供了线程的局部变量，每个线程都可以通过`set()`和`get()`来对这个局部变量进行操作，但不会和其他线程的局部变量进行冲突，**实现了线程的数据隔离**～。==

简要言之：==往ThreadLocal中填充的变量属于**当前**线程，该变量对其他线程而言是隔离的。==

# 二、为什么要学习ThreadLocal？

从上面可以得出：==ThreadLocal可以让我们拥有当前线程的变量，那这个作用有什么用呢？==？？

## 2.1管理Connection

**最典型的是管理数据库的Connection：**当时在学JDBC的时候，为了方便操作写了一个简单数据库连接池，需要数据库连接池的理由也很简单，==频繁创建和关闭Connection是一件非常耗费资源的操作，因此需要创建数据库连接池～==

那么，数据库连接池的连接怎么管理呢？？我们==交由ThreadLocal来进行管理。为什么交给它来管理呢？？ThreadLocal能够实现**当前线程的操作都是用同一个Connection，保证了事务！**==

当时候写的代码：

```
public class DBUtil {
    //数据库连接池
    private static BasicDataSource source;

    //为不同的线程管理连接
    private static ThreadLocal<Connection> local;


    static {
        try {
            //加载配置文件
            Properties properties = new Properties();

            //获取读取流
            InputStream stream = DBUtil.class.getClassLoader().getResourceAsStream("连接池/config.properties");

            //从配置文件中读取数据
            properties.load(stream);

            //关闭流
            stream.close();

            //初始化连接池
            source = new BasicDataSource();

            //设置驱动
            source.setDriverClassName(properties.getProperty("driver"));

            //设置url
            source.setUrl(properties.getProperty("url"));

            //设置用户名
            source.setUsername(properties.getProperty("user"));

            //设置密码
            source.setPassword(properties.getProperty("pwd"));

            //设置初始连接数量
            source.setInitialSize(Integer.parseInt(properties.getProperty("initsize")));

            //设置最大的连接数量
            source.setMaxActive(Integer.parseInt(properties.getProperty("maxactive")));

            //设置最长的等待时间
            source.setMaxWait(Integer.parseInt(properties.getProperty("maxwait")));

            //设置最小空闲数
            source.setMinIdle(Integer.parseInt(properties.getProperty("minidle")));

            //初始化线程本地
            local = new ThreadLocal<>();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        //获取Connection对象
        Connection connection = source.getConnection();

        //把Connection放进ThreadLocal里面
        local.set(connection);

        //返回Connection对象
        return connection;
    }

    //关闭数据库连接
    public static void closeConnection() {
        //从线程中拿到Connection对象
        Connection connection = local.get();

        try {
            if (connection != null) {
                //恢复连接为自动提交
                connection.setAutoCommit(true);

                //这里不是真的把连接关了,只是将该连接归还给连接池
                connection.close();

                //既然连接已经归还给连接池了,ThreadLocal保存的Connction对象也已经没用了
                local.remove();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
```

同样的，Hibernate对Connection的管理也是采用了相同的手法(使用ThreadLocal，当然了Hibernate的实现是更强大的)～

## 2.2避免一些参数传递

**避免一些参数的传递的理解**可以参考一下Cookie和Session：

- 每当我访问一个页面的时候，浏览器都会帮我们从硬盘中找到对应的Cookie发送过去。
- 浏览器是十分聪明的，不会发送别的网站的Cookie过去，只带当前网站发布过来的Cookie过去

浏览器就相当于我们的ThreadLocal，它仅仅会发送我们当前浏览器存在的Cookie(ThreadLocal的局部变量)，不同的浏览器对Cookie是隔离的(Chrome,Opera,IE的Cookie是隔离的【在Chrome登陆了，在IE你也得重新登陆】)，同样地：线程之间ThreadLocal变量也是隔离的….

那上面避免了参数的传递了吗？？其实是避免了。Cookie并不是我们手动传递过去的，并不需要写`<input name= cookie/>`来进行传递参数…

在编写程序中也是一样的：日常中我们要去办理业务可能会有很多地方用到身份证，各类证件，每次我们都要掏出来很麻烦

```
    // 咨询时要用身份证，学生证，房产证等等....
    public void consult(IdCard idCard,StudentCard studentCard,HourseCard hourseCard){

    }

    // 办理时还要用身份证，学生证，房产证等等....
    public void manage(IdCard idCard,StudentCard studentCard,HourseCard hourseCard) {

    }

    //......
```

而如果用了ThreadLocal的话，ThreadLocal就相当于一个机构，ThreadLocal机构做了记录你有那么多张证件。用到的时候就不用自己掏了，问机构拿就可以了。

在咨询时的时候就告诉机构：来，把我的身份证、房产证、学生证通通给他。在办理时又告诉机构：来，把我的身份证、房产证、学生证通通给他。…

```
    // 咨询时要用身份证，学生证，房产证等等....
    public void consult(){

        threadLocal.get();
    }

    // 办理时还要用身份证，学生证，房产证等等....
    public void takePlane() {
        threadLocal.get();
    }
```

这样是不是比自己掏方便多了。

当然了，ThreadLocal可能还会有其他更好的作用，如果知道的同学可在评论留言哦～～～

# 三、ThreadLocal实现的原理

想要更好地去理解ThreadLocal，那就得翻翻它是怎么实现的了～～～

> 声明：本文使用的是JDK 1.8

首先，我们来看一下==ThreadLocal的set()方法==，因为我们一般使用都是==new完对象，就往里边set对象了==

```
    public void set(T value) {

        // 得到当前线程对象
        Thread t = Thread.currentThread();

        // 这里获取ThreadLocalMap
        ThreadLocalMap map = getMap(t);

        // 如果map存在，则将当前线程对象t作为key，要存储的对象作为value存到map里面去
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }
```

上面有个ThreadLocalMap，我们去看看这是什么？

```
static class ThreadLocalMap {

        /**
         * The entries in this hash map extend WeakReference, using
         * its main ref field as the key (which is always a
         * ThreadLocal object).  Note that null keys (i.e. entry.get()
         * == null) mean that the key is no longer referenced, so the
         * entry can be expunged from table.  Such entries are referred to
         * as "stale entries" in the code that follows.
         */
        static class Entry extends WeakReference<ThreadLocal<?>> {
            /** The value associated with this ThreadLocal. */
            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }
        //....很长
}
```

通过上面我们可以发现的是==**ThreadLocalMap是ThreadLocal的一个内部类。用Entry类来进行存储**==

==我们的**值都是存储到这个Map上的，key是当前ThreadLocal对象**！==

==如果该Map不存在，则初始化一个：==

```
    void createMap(Thread t, T firstValue) {
        t.threadLocals = new ThreadLocalMap(this, firstValue);
    }
```

==如果该Map存在，则**从Thread中获取**！==

```
    /**
     * Get the map associated with a ThreadLocal. Overridden in
     * InheritableThreadLocal.
     *
     * @param  t the current thread
     * @return the map
     */
    ThreadLocalMap getMap(Thread t) {
        return t.threadLocals;
    }
```

Thread维护了ThreadLocalMap变量

```
    /* ThreadLocal values pertaining to this thread. This map is maintained
     * by the ThreadLocal class. */
    ThreadLocal.ThreadLocalMap threadLocals = null
```

从上面又可以看出，**ThreadLocalMap是在ThreadLocal中使用内部类来编写的，但对象的引用是在Thread中**！

于是我们可以总结出：**==Thread为每个线程维护了ThreadLocalMap这么一个Map，而ThreadLocalMap的key是LocalThread对象本身，value则是要存储的对象==**

有了上面的基础，我们看get()方法就一点都不难理解了：

```
    public T get() {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }
```

## 3.1ThreadLocal原理总结

1. ==每个Thread维护着一个ThreadLocalMap的引用==
2. ==ThreadLocalMap是ThreadLocal的内部类，用Entry来进行存储==
3. ==调用ThreadLocal的set()方法时，实际上就是往ThreadLocalMap设置值，key是ThreadLocal对象，值是传递进来的对象==
4. ==调用ThreadLocal的get()方法时，实际上就是往ThreadLocalMap获取值，key是ThreadLocal对象==
5. ==**ThreadLocal本身并不存储值**，它只是**作为一个key来让线程从ThreadLocalMap获取value**。==

==正因为这个原理，所以ThreadLocal能够实现“数据隔离”，获取当前线程的局部变量值，不受其他线程影响～==

# 四、避免内存泄露

我们来看一下ThreadLocal的对象关系引用图：

![图片](https://mmbiz.qpic.cn/mmbiz_jpg/2BGWl1qPxib1a3zqXxNAm6O584NJmiar2AVN56p77CkAyxjvhyaZ27HORCZbFeOc1zHcXI1e7CqJqaYiahbW5LBEw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

ThreadLocal内存泄漏的根源是：**由于ThreadLocalMap的生命周期跟Thread一样长，如果没有手动删除对应key就会导致内存泄漏，而不是因为弱引用**。

想要避免内存泄露就要**手动remove()掉**！

# 五、总结

ThreadLocal这方面的博文真的是数不胜数，随便一搜就很多很多～站在前人的肩膀上总结了这篇博文～

最后要记住的是:**ThreadLocal设计的目的就是为了能够在当前线程中有属于自己的变量，并不是为了解决并发或者共享变量的问题**

如果看得不够过瘾，觉得不够深入的同学可参考下面的链接，很多的博主还开展了一些扩展知识，我就不一一展开了～

参考博文：

- http://blog.xiaohansong.com/2016/08/06/ThreadLocal-memory-leak/
- https://www.cnblogs.com/zhangjk1993/archive/2017/03/29/6641745.html#_label2
- http://www.cnblogs.com/dolphin0520/p/3920407.html
- http://www.cnblogs.com/dolphin0520/p/3920407.html
- http://www.iteye.com/topic/103804
- https://www.cnblogs.com/xzwblog/p/7227509.html
- https://blog.csdn.net/u012834750/article/details/71646700
- https://blog.csdn.net/winwill2012/article/details/71625570
- https://juejin.im/post/5a64a581f265da3e3b7aa02d