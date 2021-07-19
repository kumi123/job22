# ConcurrentHashMap为什么线程安全？哪些点保证了？

答：**数组初始化**的时候**自旋**来保证一定可以初始化成功，然后通过 **CAS 设置 SIZECTL 变量的值，来保证同一时刻只能有一个线程对数组进行初始化**，CAS 成功之后，还会**再次判断当前数组是否已经初始化完成**，如果已经初始化完成，就不会再次初始化；

**新增槽点**时通过**自旋**保证一定新增成功，然后通过**CAS来新增，如果遇到槽点有值，通过锁住当前槽点或红黑树的根节点**；

**扩容时**通过**锁住原数组的槽点，设置转移节点，以及自旋**等操作来保证线程安全。

ConcurrentHashMap 在 put 方法上的整体思路：

1. ==如果数组为空，初始化，初始化完成之后，走 2；==
2. ==计算当前槽点有没有值，没有值的话，cas 创建，失败继续自旋（for 死循环），直到成功，槽点有值的话，走 3；==
3. ==如果槽点是转移节点(正在扩容)，就会一直自旋等待扩容完成之后再新增，不是转移节点走4；==
4. ==槽点有值的，先锁定当前槽点，保证其余线程不能操作，如果是链表，新增值到链表的尾部，如果是红黑树，使用红黑树新增的方法新增；==保存链表长度，看看是否需要转换成红黑树
5. ==新增完成之后 check 需不需要扩容，需要的话去扩容。==

![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/5018cb817c254c3b9f847c247dc3dafc~tplv-k3u1fbpfcp-zoom-1.image)![点击并拖拽以移动]()
 具体源码如下：

```
        final V putVal (K key, V value,boolean onlyIfAbsent){
            if (key == null || value == null) throw new NullPointerException();
            //通过hashcode计算 hash
            int hash = spread(key.hashCode());
            int binCount = 0;
            for (Node<K, V>[] tab = table; ; ) {
                Node<K, V> f;
                int n, i, fh;
                //table为空，进行初始化工作，调用initTable
                if (tab == null || (n = tab.length) == 0)
                    tab = initTable();
                    //如果当前索引位置没有值，直接创建
                else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
                    //cas 在 i 位置创建新的元素，当 i 位置是空时，即能创建成功，结束 for 自循，否则继续自旋
                    if (casTabAt(tab, i, null,
                            new Node<K, V>(hash, key, value, null)))
                        break; // no lock when adding to empty bin
                }
                //如果当前槽点是转移节点，表示该槽点正在扩容，就会一直等待扩容完成
                //转移节点的 hash 值是固定的，都是 MOVED
                else if ((fh = f.hash) == MOVED)
                    tab = helpTransfer(tab, f);
                    //槽点上有值的
                else {
                    V oldVal = null;
                    //锁定当前槽点，其余线程不能操作，保证了安全
                    synchronized (f) {
                        //这里再次判断 i 索引位置的数据没有被修改
                        //binCount 被赋值的话，说明走到了修改表的过程里面
                        if (tabAt(tab, i) == f) {
                            //链表
                            if (fh >= 0) {
                                binCount = 1;
                                for (Node<K, V> e = f; ; ++binCount) {
                                    K ek;
                                    //值有的话，直接返回
                                    if (e.hash == hash &&
                                            ((ek = e.key) == key ||
                                                    (ek != null && key.equals(ek)))) {
                                        oldVal = e.val;
                                        if (!onlyIfAbsent)
                                            e.val = value;
                                        break;
                                    }
                                    Node<K, V> pred = e;
                                    //把新增的元素赋值到链表的最后，退出自旋
                                    if ((e = e.next) == null) {
                                        pred.next = new Node<K, V>(hash, key,
                                                value, null);
                                        break;
                                    }
                                }
                            }
                            //红黑树，这里没有使用 TreeNode,使用的是 TreeBin，TreeNode 只是红黑树的一个节点
                            //TreeBin 持有红黑树的引用，并且会对其加锁，保证其操作的线程安全
                            else if (f instanceof TreeBin) {
                                Node<K, V> p;
                                binCount = 2;
                                //满足 if 的话，把老的值给 oldVal
                                //在 putTreeVal 方法里面，在给红黑树重新着色旋转的时候
                                //会锁住红黑树的根节点
                                if ((p = ((TreeBin<K, V>) f).putTreeVal(hash, key,
                                        value)) != null) {
                                    oldVal = p.val;
                                    if (!onlyIfAbsent)
                                        p.val = value;
                                }
                            }
                        }
                    }
                    //binCount 不为空，并且 oldVal 有值的情况，说明已经新增成功了
                    if (binCount != 0) {
                        // 链表是否需要转化成红黑树
                        if (binCount >= TREEIFY_THRESHOLD)
                            treeifyBin(tab, i);
                        if (oldVal != null)
                            return oldVal;
                        //这一步几乎走不到。槽点已经上锁，只有在红黑树或者链表新增失败的时候
                        //才会走到这里，这两者新增都是自旋的，几乎不会失败
                        break;
                    }
                }
            }
            //check 容器是否需要扩容，如果需要去扩容，调用 transfer 方法去扩容
            //如果已经在扩容中了，check 有无完成
            addCount(1L, binCount);
            return null;
        }
复制代码
```

![点击并拖拽以移动]()

### 数组初始化时的线程安全

数组初始化时，==首先通过自旋来保证一定可以初始化成功==，然后==通过 CAS 设置 SIZECTL 变量的值，来保证同一时刻只能有一个线程对数组进行初始化==，==CAS 成功之后，还会再次判断当前数组是否已经初始化完成，如果已经初始化完成，就不会再次初始化==，通过==自旋 + CAS + 双重 check等手段保证了数组初始化时的线程安全==，源码如下：

```
  //初始化 table，通过对 sizeCtl 的变量赋值来保证数组只能被初始化一次
        private final Node<K, V>[] initTable () {
            Node<K, V>[] tab;
            int sc;
            //通过自旋保证初始化成功
            while ((tab = table) == null || tab.length == 0) {
                // 小于 0 代表有线程正在初始化，释放当前 CPU 的调度权，重新发起锁的竞争
                if ((sc = sizeCtl) < 0)
                    Thread.yield(); // lost initialization race; just spin
                    // CAS 赋值保证当前只有一个线程在初始化，-1 代表当前只有一个线程能初始化
                    // 保证了数组的初始化的安全性
                else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
                    try {
                        // 很有可能执行到这里的时候，table 已经不为空了，这里是双重 check
                        if ((tab = table) == null || tab.length == 0) {
                            // 进行初始化
                            int n = (sc > 0) ? sc : DEFAULT_CAPACITY;
                            @SuppressWarnings("unchecked")
                            Node<K, V>[] nt = (Node<K, V>[]) new Node<?, ?>[n];
                            table = tab = nt;
                            sc = n - (n >>> 2);
                        }
                    } finally {
                        sizeCtl = sc;
                    }
                    break;
                }
            }
            return tab;
        }
    }
复制代码
```

![点击并拖拽以移动]()

### 新增槽点值时的线程安全

==此时为了保证线程安全，做了四处优化：==

- ==通过自旋死循环保证一定可以新增成功。==
- ==当前槽点为空时，通过 CAS 新增。==
- ==当前槽点有值，锁住当前槽点。==
- ==红黑树旋转时，锁住红黑树的根节点，保证同一时刻，当前红黑树只能被一个线程旋转==

### ==扩容中时的线程安全==

- ==拷贝槽点时，会把原数组的槽点锁住；==

- ==拷贝成功之后，会把原数组的槽点设置成转移节点，这样如果有数据需要 put 到该节点时，发现该槽点是转移节点，会一直等待，直到扩容成功之后，才能继续 put，可以参考 put 方 法中的 helpTransfer 方法；==

- ==从尾到头进行拷贝，拷贝成功就把原数组的槽点设置成转移节点。==

- ==等扩容拷贝都完成之后，直接把新数组的值赋值给数组容器，之前等待 put 的数据才能继续 put。==

  

