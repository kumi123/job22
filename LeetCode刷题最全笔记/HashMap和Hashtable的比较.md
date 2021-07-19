# HashMap和Hashtable的比较



## 在面试的时候，java集合最容易被问到的知识就是HashMap与Hashtable的比较，通常我们也很容易回答出一下几点：



（重新总结 主要是1.8和hashtable的差距）

1、==HashMap是线程不安全的，在多线程环境下会容易产生死循环，但是单线程环境下运行效率高；Hashtable线程安全的，很多方法都有synchronized修饰，但同时因为加锁导致单线程环境下效率较低。==

2、==HashMap允许有一个key为null，允许多个value为null；而Hashtable不允许key或者value为null。==



3、构造函数方面 ：可以看出HashMap的底层数组的长度必须为2^n，这样做的好处是为以后的hash算法做准备，而Hashtable底层数组的长度可以为任意值，Hashtable的默认构造函数底层数组长度为11（质数），另一个为16

4、hash函数方面：==HashMap的hash算法通过非常规的设计，将底层table长度设计为2^n（合数），这是HashMap的一处优化。它使用了&运算来代替%运算以减少性能上面的损耗。

Hashtable：

```java
int hash = key.hashCode();
//0x7FFFFFFF转换为10进制之后是Intger.MAX_VALUE,也就是2^31 - 1
int index = (hash & 0x7FFFFFFF) % tab.length;
```

很容易看出Hashtable的hash算法首先使得hash的值小于等于整型数的最大值，再通过%运算实现均匀散射。

5、扩容方面：jdk1.8对扩容进行优化，使得扩容不再需要进行链表的反转，只需要知道hashcode新增的bit位为0还是1。如果是0就在原索引位置，新增索引是1就在oldIndex+oldCap位置。HashTable就是很普通重新计算



6、 hashmap 用数组+链表+红黑树 而 hashtable 就是使用数组+链表

## **构造函数的比较**

HashMap：

```java
public HashMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " +
                                           initialCapacity);
    if (initialCapacity > MAXIMUM_CAPACITY)
        initialCapacity = MAXIMUM_CAPACITY;
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: " +
                                           loadFactor);
    this.loadFactor = loadFactor;
    this.threshold = tableSizeFor(initialCapacity);
}

//该方法返回大于等于cap的最小2次幂的整数
static final int tableSizeFor(int cap) {
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}
```

Hashtable：

```java
public Hashtable(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal Capacity: "+
                                           initialCapacity);
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal Load: "+loadFactor);

    if (initialCapacity==0)
        initialCapacity = 1;
    this.loadFactor = loadFactor;
    table = new Entry<?,?>[initialCapacity];
    threshold = (int)Math.min(initialCapacity * loadFactor, MAX_ARRAY_SIZE + 1);
}

public Hashtable() {
    this(11, 0.75f);
}
```

==可以看出HashMap的底层数组的长度必须为2^n，这样做的好处是为以后的hash算法做准备==，而==Hashtable底层数组的长度可以为任意值==，这就造成了当底层数组长度为合数的时候，Hashtable的hash算法散射不均匀，容易产生hash冲突。所以，可以清楚的看到==Hashtable的默认构造函数底层数组长度为11（质数）==，至于为什么Hashtable的底层数组用质数较好，请参考博文：[http://blog.csdn.net/liuqiyao_01/article/details/14475159](https://link.zhihu.com/?target=http%3A//blog.csdn.net/liuqiyao_01/article/details/14475159)；

## **Hash算法的比较**

## ==**hash算法的区别**==

HashMap：

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
//手写的，源码在不存在这一句，但是原理是类似的，详情可以去看putVal方法
int i = (table.length-1) & hash(key)
```

==HashMap的hash算法通过非常规的设计，将底层table长度设计为2^n（合数），这是HashMap的一处优化。它使用了&运算来代替%运算以减少性能上面的损耗。为何&运算可以代替%运算呢==？

**如果两个整数做&运算，实质是两个整数转换为2进制数后每一个bit位的分别做&运算，所以其最终的运算结果的值不会超过最小的那个数**，这个时候只需要搞清楚三点就能明白其实现原理：

1、合数2^ n转换为2进制的数之后，最高位为1其余的位数都为0，比如16-->10000，32-->100000。那么，2^ n-1转换为2进制后，所有的bit位都为1，比如31-->11111，127-->1111111。所以，hashcode与（2^n-1）做&运算每一个bit位都可以保持原来的值。

2、当hash()方法得到的值<=（table.length-1）,其运算结果就在[0,table.length-1]范围内均匀散射。当hash()方法得到的值小于table.length-1的时候，运算结果就是该方法的原值。当hash()方法得到的值等于table.length-1的的时候，运算结果为0。

3、当hash()得到的值>(table.length-1)，此时table.length-1为较小的数，所以&运算的结果还是在[0,table.length-1]之间。具体实现是这样的，table.length-1转化为2进制的数之后位数小于hash()方法得到的2进制数，所以它的高位只能用0去补齐，又由于&运算的特性，只要有一个为0那么都为0，所以此时相当于转化为情况1。

而hash()方法的具体作用是使得table的length较小的时候高低bit都能参与运算，具体分析请参考：[https://tech.meituan.com/java-hashmap.html](https://link.zhihu.com/?target=https%3A//tech.meituan.com/java-hashmap.html)

Hashtable：

```java
int hash = key.hashCode();
//0x7FFFFFFF转换为10进制之后是Intger.MAX_VALUE,也就是2^31 - 1
int index = (hash & 0x7FFFFFFF) % tab.length;
```

==很容易看出Hashtable的hash算法首先使得hash的值小于等于整型数的最大值，再通过%运算实现均匀散射。==

==**由于计算机是底层的运算是基于2进制的，所以HashMap的hash算法使用&运算代替%运算，在运算速度上明显HashMap的hash算法更优**。==

## **扩容的机制的区别**

因为无论是HasHMap或者HashTable的扩容都是基于底层的hash算法的，所以将扩容机制放在hash算法部分讲。

**HashMap扩容：**

```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // 将阈值扩大为2倍
    }
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {               // 当threshold的为0的使用默认的容量，也就是16
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
        //新建一个数组长度为原来2倍的数组
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { 
                    //HashMap在JDK1.8的时候改善了扩容机制，原数组索引i上的链表不需要再反转。
                    // 扩容之后的索引位置只能是i或者i+oldCap（原数组的长度）
                    // 所以我们只需要看hashcode新增的bit为0或者1。
                   // 假如是0扩容之后就在新数组索引i位置，新增为1，就在索引i+oldCap位置
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        // 新增bit为0，扩容之后在新数组的索引不变
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        else {  //新增bit为1，扩容之后在新数组索引变为i+oldCap（原数组的长度）
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        //数组索引位置不变，插入原索引位置
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        //数组索引位置变化为j + oldCap
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```

从源码中可以看出，HashMap数组的扩容的整体思想就是创建一个长度为原先2倍的数组。然后对原数组进行遍历和复制。只不过==jdk1.8对扩容进行优化，使得扩容不再需要进行链表的反转，只需要知道hashcode新增的bit位为0还是1。如果是0就在原索引位置，新增索引是1就在oldIndex+oldCap位置。==

可能有些人对新增bit位感到困惑。在这里解释一下，这里的新增指的是有效bit位。在上面说到过，两个整数做&运算，转换为2进制的后，看bit位较短的那个数。**也就是说bit位较长的数与bit位较短的数做&运算，多出来的bit需要用0来补齐，由于是&运算（只有一个为0那么其结果就为0），所以，新增的0位不是有效的bit位**。对应于hash算法来说，通常hashcode的值比较大（转换为2进制数后bit为较多），扩容之后将数组的长度扩大为2倍，那么n（数组的长度），转换为2进制数后相较于未扩容之前的n多增加了一个1的有效bit位。简化版的例子如下：

> 初始容量为16，那么15转换为二进制数位1111，现在进行一次扩容之后容量变为32，那么31转换为2进制是为11111。现有两个key，一个hashcode为107转换为二进制数后为1101011，另一个的hashcode是379转换为二进制数后为101111011。在容量为16的时候，这两个key，具体计算索引过程为：
> 0001111 & 1101011 = 1011 000001111 & 101111011 = 1011 转换为10进制数后都为11。
> 现在来看一下扩容之后两个key的索引：
> 0011111 & 1101011 = 1011 000011111 & 101111011 = 11011 一个对应的索引仍然是11，而另一个却变为27（27 = 11+16）

**Hashtable扩容：**

```java
protected void rehash() {
    int oldCapacity = table.length;
    Entry<?,?>[] oldMap = table;

    // overflow-conscious code
    int newCapacity = (oldCapacity << 1) + 1;
    if (newCapacity - MAX_ARRAY_SIZE > 0) {
        if (oldCapacity == MAX_ARRAY_SIZE)
            // Keep running with MAX_ARRAY_SIZE buckets
            return;
        newCapacity = MAX_ARRAY_SIZE;
    }
    Entry<?,?>[] newMap = new Entry<?,?>[newCapacity];

    modCount++;
    threshold = (int)Math.min(newCapacity * loadFactor, MAX_ARRAY_SIZE + 1);
    table = newMap;

    for (int i = oldCapacity ; i-- > 0 ;) {
        for (Entry<K,V> old = (Entry<K,V>)oldMap[i] ; old != null ; ) {
            Entry<K,V> e = old;
            old = old.next;

            int index = (e.hash & 0x7FFFFFFF) % newCapacity;
            //使用头插法将链表反序
            e.next = (Entry<K,V>)newMap[index];
            newMap[index] = e;
        }
    }
}
```

==Hashtable的扩容将先创建一个长度为原长度2倍的数组，再使用头插法将链表进行反序。==

## **结构的区别**

HashMap在jdk1.8在原先的数组+链表的结构进行了优化，将实现结构变为数组+链表+红黑树，有关红黑树的文章详细请参考博文：[http://www.cnblogs.com/skywang12345/p/3245399.html](https://link.zhihu.com/?target=http%3A//www.cnblogs.com/skywang12345/p/3245399.html)。这里只需要之后，红黑树是近似平衡树。做这样的优化，是为了防止在一个哈希桶位置链表过长，影响get等方法的时间。详细分析用put方法来做举例：

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    //HashMap在构造方法中只是设置了一些参数，只有到put方法才会创建底层数组
    //这使用的是懒加载策略
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        else if (p instanceof TreeNode)
            // 节点为红黑树节点，那么使用红黑树的插入方式
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    //默认情况下当一条链的节点个数大于8的时候就需要转换为红黑树节点
                    //当然对底层数组的长度也有要求，最低长度为64，否则会先进行扩容
                    if (binCount >= TREEIFY_THRESHOLD - 1) 
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    //put一个元素后检查size是否大于阈值，大于则需要进行扩容
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

==从源码中可以看出基于HashMap为了防止链表过长影响get等方法的性能，在一条链表节点元素大于8的时候，会将链表封装成红黑树。==

再来看一下Hashtable

```java
public synchronized V put(K key, V value) {
    // Make sure the value is not null
    if (value == null) {
        throw new NullPointerException();
    }

    // Makes sure the key is not already in the hashtable.
    Entry<?,?> tab[] = table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    @SuppressWarnings("unchecked")
    Entry<K,V> entry = (Entry<K,V>)tab[index];
    for(; entry != null ; entry = entry.next) {
        if ((entry.hash == hash) && entry.key.equals(key)) {
            V old = entry.value;
            entry.value = value;
            return old;
        }
    }

    addEntry(hash, key, value, index);
    return null;
}

private void addEntry(int hash, K key, V value, int index) {
    modCount++;

    Entry<?,?> tab[] = table;
    if (count >= threshold) {
        // Rehash the table if the threshold is exceeded
        rehash();

        tab = table;
        hash = key.hashCode();
        index = (hash & 0x7FFFFFFF) % tab.length;
    }

    // Creates the new entry.
    @SuppressWarnings("unchecked")
    Entry<K,V> e = (Entry<K,V>) tab[index];
    tab[index] = new Entry<>(hash, key, value, e);
    count++;
}
```

==可以看出Hashtable到了jdk1.8了内部结构并没有实质优化，继续使用数组+链表的方式实现。==

## **总结**

可以看出到jdk1.8 HashMap和Hashtable的区别越来越大，HashMap相较与之前的jdk做了很多的优化，最重要的是在内部实现结构上引进了红黑数还有扩容上的优化。Hashtable作为jdk1.2遗留下来的类，到jdk1.8没有大改，**所以对数据的一致性要求较低的话可以使用ConcurrentHashMap来替代Hashtable**。