## ThreadLocal定义

先看JDK关于ThreadLocal的类注释：

> This class provides thread-local variables. These variables differ from their normal counterparts in that each thread that accesses one (via its get or set method) has its own, independently initialized copy of the variable. ThreadLocal instances are typically private static fields in classes that wish to associate state with a thread (e.g., a user ID or Transaction ID).

翻译过来大概的意思为：==ThreadLocal提供了线程内部的局部变量；每个线程都有自己的，独立的初始化变量副本；ThreadLocal实例通常是类中的private static字段，该类一般在线程状态相关（或线程上下文）中使用。==

> Each thread holds an implicit reference to its copy of a thread-local variable as long as the thread is alive and the ThreadLocal instance is accessible; after a thread goes away, all of its copies of thread-local instances are subject to garbage collection (unless other references to these copies exist).

翻译过来大概的意思为：只要线程处于活动状态且ThreadLocal实例是可访问的状态下，每个线程都持有对其线程局部变量副本的隐式引用；在线程消亡后，ThreadLocal实例s的所有副本都将进行垃圾回收（除非存在对这些副本的其他引用）。

## 一些应用场景

==1、多线程下使用日志追踪，如Logback或Log4j的MDC组件==
==2、在事务中，connection绑定到当前线程来保证这个线程中的数据库操作用的是同一个connection==
3、dubbo的RpcContext的实现：<RpcContext是一个临时状态记录器，当接收到RPC请求，或发起RPC请求时，RpcContext的状态都会变化>
4、现在的分布式trace系统中的traceId、spanId的传递等
5、web前台的请求参数，在同一线程内多个方法之间隐式传递
。。。

## ThreadLocal API的使用

一个简单的demo：

```
public class TraceContext {

    private static final ThreadLocal<String> traceIdHolder = new ThreadLocal<String>() {

        @Override
        protected String initialValue() {//①
            return UUID.randomUUID().toString().replaceAll("-", "");
        }
    };

    public static void setTraceId(String traceId) {
        traceIdHolder.set(traceId);//②
    }

    public static String getTraceId() {
        return traceIdHolder.get();//③
    }

    public static void removeTraceId() {
        traceIdHolder.remove();//④
    }
}
```

思考：ThreadLocal类型的traceIdHolder一般被修饰为static、final、private，就是traceIdHolder在被使用的时候为单例不可变（这不是常见的单例饱汉模式么）。如果traceIdHolder定义为多实例会怎么样？

## ThreadLocal实现解读

以下以JDK1.8实现解读

==ThreadLocal的构造函数为空：`public ThreadLocal() {}`==

==ThreadLocal的set方法：==

```
public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
}

ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}
```

==获取当前线程的ThreadLocalMap，有直接设置value、没有新建==
==ThreadLocal的get方法：==

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

==从当前线程的ThreadLocalMap中查找Entry，如果不必为null返回value，否则设置初值并返回setInitialValue()==

==ThreadLocal的remove()方法：==

```
public void remove() {
     ThreadLocalMap m = getMap(Thread.currentThread());
     if (m != null)
         m.remove(this);
 }
```

从当前线程的ThreadLocalMap中删除

ThreadLocal的setInitialValue()方法：

```
private T setInitialValue() {
    T value = initialValue();//未覆盖就是null
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
    return value;
}
```

和set类似

查看Thread的threadLocals的字段定义：`ThreadLocal.ThreadLocalMap threadLocals = null;`

查看ThreadLocal的内部类ThreadLocalMap的定义：

虽然ThreadLocalMap命名含有'Map'，但和Map接口没任何关系。ThreadLocalMap底层是一个的散列表（可扩容的数组），并采用开放地址法来解决hash冲突。

![clipboard.png]()

ThreadLocalMap.Entry定义：

```
static class Entry extends WeakReference<ThreadLocal<?>> {
    /** The value associated with this ThreadLocal. */
    Object value;

    Entry(ThreadLocal<?> k, Object v) {
        super(k);
        value = v;
    }
}
```

这里先不管为啥使用WeakReference定义。稍后讨论

ThreadLocalMap.set方法：

```
 private void set(ThreadLocal<?> key, Object value) {

    // We don't use a fast path as with get() because it is at
    // least as common to use set() to create new entries as
    // it is to replace existing ones, in which case, a fast
    // path would fail more often than not.

    Entry[] tab = table;
    int len = tab.length;
    int i = key.threadLocalHashCode & (len-1);

    for (Entry e = tab[i];
         e != null;
         e = tab[i = nextIndex(i, len)]) {
        ThreadLocal<?> k = e.get();

        if (k == key) {
            e.value = value;
            return;
        }

        if (k == null) {
            replaceStaleEntry(key, value, i);
            return;
        }
    }

    tab[i] = new Entry(key, value);
    int sz = ++size;
    if (!cleanSomeSlots(i, sz) && sz >= threshold)
        rehash();
}
```

每个ThreadLocal对象都有一个hash值threadLocalHashCode，每初始化一个ThreadLocal对象，hash值就增加一个固定的大小0x61c88647。
定义如下：

```
private final int threadLocalHashCode = nextHashCode();

/**
 * The next hash code to be given out. Updated atomically. Starts at
 * zero.
 */
private static AtomicInteger nextHashCode =
    new AtomicInteger();

/**
 * The difference between successively generated hash codes - turns
 * implicit sequential thread-local IDs into near-optimally spread
 * multiplicative hash values for power-of-two-sized tables.
 */
private static final int HASH_INCREMENT = 0x61c88647;

/**
 * Returns the next hash code.
 */
private static int nextHashCode() {
    return nextHashCode.getAndAdd(HASH_INCREMENT);
}
```

==ThreadLocalMap.set流程总结如下：==

1. ==根据当前ThreadLocal的hashCode mod table.length，计算出应插入的table位置下表i；==
2. ==如果table[i]的Entry不为null，==
   ==①、判断Entry.key == 当前的ThreadLocal对象？相等覆盖旧值 退出==
   ==②、如果Entry.key为null，将执行删除两个null槽之间的所有的过期的stale的entry，并把当前的位置i上初始化一个Entry对象，退出==
   ==③、继续查找下一个位置i++==
3. ==如果找到了一个位置k，table[k]为null，初始化一个Entry对象，新建一个这种值==

ThreadLocalMap.getEntry方法：

```
private Entry getEntry(ThreadLocal<?> key) {
    int i = key.threadLocalHashCode & (table.length - 1);
    Entry e = table[i];
    if (e != null && e.get() == key)
        return e;
    else
        return getEntryAfterMiss(key, i, e);
}

private Entry getEntryAfterMiss(ThreadLocal<?> key, int i, Entry e) {
    Entry[] tab = table;
    int len = tab.length;

    while (e != null) {
        ThreadLocal<?> k = e.get();
        if (k == key)
            return e;
        if (k == null)
            expungeStaleEntry(i);
        else
            i = nextIndex(i, len);
        e = tab[i];
    }
    return null;
}

private int expungeStaleEntry(int staleSlot) {
    Entry[] tab = table;
    int len = tab.length;

    // expunge entry at staleSlot
    tab[staleSlot].value = null;
    tab[staleSlot] = null;
    size--;

    // Rehash until we encounter null
    Entry e;
    int i;
    for (i = nextIndex(staleSlot, len);
         (e = tab[i]) != null;
         i = nextIndex(i, len)) {
        ThreadLocal<?> k = e.get();
        if (k == null) {
            e.value = null;
            tab[i] = null;
            size--;
        } else {
            int h = k.threadLocalHashCode & (len - 1);
            if (h != i) {
                tab[i] = null;

                // Unlike Knuth 6.4 Algorithm R, we must scan until
                // null because multiple entries could have been stale.
                while (tab[h] != null)
                    h = nextIndex(h, len);
                tab[h] = e;
            }
        }
    }
    return i;
}
```

ThreadLocalMap.getEntry流程总结如下：

1. ==根据当前ThreadLocal的hashCode mod table.length，计算直接索引的位置i，如果e不为null并且key相同则返回e。==
2. ==如果e为null，返回null==
3. ==如果e不为空且key不相同，则查找下一个位置，继续查找比较，直到e为null退出==
4. ==在查找的过程中如果发现e不为空，且e的k为空的话，删除当前槽和下一个null槽之间的所有过期entry对象==

ThreadLocalMap.remove方法：

```
private void remove(ThreadLocal<?> key) {
    Entry[] tab = table;
    int len = tab.length;
    int i = key.threadLocalHashCode & (len-1);
    for (Entry e = tab[i];
         e != null;
         e = tab[i = nextIndex(i, len)]) {
        if (e.get() == key) {
            e.clear();
            expungeStaleEntry(i);
            return;
        }
    }
}
```

==ThreadLocalMap.remove流程总结如下：==

1. ==计算直接索引的位置i，如果table[i]的entry e不为null，且key比较相等，则执行删除，把table[i]=null，table[i].value = null;然后删除当前槽和下一个null槽之间的所有过期entry对象==
2. ==查找下一个位置，i++，直到table[i]的entry e为null退出==

==总结ThreadLocalMap：==

1. ==散列采用开放地址，线性探测，在hash冲突较大的时候效率低下==
2. ==ThreadLocalMap的set、get、remove操作中都带有删除过期元素的操作，类似缓存的lazy淘汰==



## 关于ThreadLocal的内存泄漏

以下分析转自知乎作者winwill2012，[链接](https://www.zhihu.com/question/23089780/answer/62097840)：我觉得是这样的

如上图，T==hreadLocalMap使用ThreadLocal的弱引用作为key，如果一个ThreadLocal没有外部强引用引用他，那么系统gc的时候，这个ThreadLocal势必会被回收，====这样一来，ThreadLocalMap中就会出现key为null的Entry，就没有办法访问这些key为null的Entry的value，如果当前线程再迟迟不结束的话，这些key为null的Entry的value就会一直存在一条强引用链:==

ThreadRef -> Thread -> ThreaLocalMap -> Entry -> value

==永远无法回收，造成内存泄露。==

因此==在使用ThreadLocal的时候要手动调用remove方法，防止内存泄漏。==

JDK建议将==ThreadLocal变量定义成private static的，这样的话ThreadLocal的生命周期就更长（类的静态属性引用的对象为GCRoots），由于一直存在ThreadLocal的强引用，所以ThreadLocal也就不会被回收，也就能保证任何时候都能根据ThreadLocal的弱引用访问到Entry的value值，然后remove它，防止内存泄露==。

我觉的JDK建议将ThreadLocal变量定义成private static的还有个可能原因是：单例，ThreadLocal对象是无状态的，无含义的，声明同一类型的ThreadLocal对象多实例，浪费ThreadLocalMap的存储空间且对象更容易引起内存泄漏。

