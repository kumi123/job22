# 19.LRU Cache的实现、应用和题解

### **Cache缓存**

我们先来认识一下`cache`以及`cache`在现实中的应用，`cache`的话就叫做缓存，比如之前文章提到过的 `Fibonacci`数列以及爬楼梯问题，解这些题就需要做一个所谓的**记忆化搜索**，其实我们就建列一个`cache`，你可以用数组来表示，Python可以直接用@LRU Cache 来写。那么缓存到底是什么呢？在现实中其实应用很多。

1. 记忆
2. 钱包 - 储物柜
3. 代码模块

> 比如说我们人类的记忆，其实很多时候就是一个缓存，很多东西我们会记在纸上，写在书籍里面，因为我们怕我们记不住，好处就是说书上那些东西永远会存在，但是它问题就在于你要把它载入到你的脑子里面来，需要花不少的时间，经常用的东西肯就记者我们脑子里面，它的速度就特别快，但是问题就是说经常会有误或者是说会遗忘，这就是人脑记忆所做成一个缓存的问题。

`Cache`本身的话，从CPU里面的话就讲来很多了，就比如说我们刚接触计算的时候对硬件比较感兴趣，当时特别出名的就是英特尔的处理器。

 ![img](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d678598a883140269bee201183351f6e~tplv-k3u1fbpfcp-zoom-1.image)

它当时就 L1 的 cache，L2 的 cache，L3 的 cache，它就有吧所谓的三级缓存。

  ![img](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d92352ffcaf34bdd8e7e80b949b16a96~tplv-k3u1fbpfcp-zoom-1.image)

[Understanding the Meltdown exploit – in my own simple words](https://www.sqlpassion.at/archive/2018/01/06/understanding-the-meltdown-exploit-in-my-own-simple-words/)

在这里它是四核的一个CPU，每一个核里面就有 L1 D-Cache、L1 l-Cache、L2 Cache、L3 Cache，指的是最常用的数据，马上要给CPU的计算模块进行计算处理的，就放在L1里面，另外的话更多一点的淘汰下来，次之不太常用的就放在 L1 l-Cache，再次之就放在 L2 Cache里面，最后的话放在 L3 Cache里面，当然外面的话就是内存，它的速度的话一个比一个快，但是它的体积也就是能存的数据的多少，肯定是L3 Cache是最大的，L1 D-Cache是最小的。这就是它所谓的缓存机制。

### LRU Cache

那么说到缓存的话，它的基本特性主要有两点：

**两个要素：大小、替换策略**

- 第一点的话就是缓存总体的大小是多少？如果缓存非常大的话就类似于CPU的缓存抵一个内存，有1G的缓存的话，那么很多东西就只管往里面存就行了。对于人的话就是这个人的记性特别好。
- 第二个的话它的替换策略，也就是说我 L1 最快的，但是因为容量有限不够装了，那么我们要把哪一些不常用的把它放在后面来，以及我们怎么鉴别哪些信息是不常用的，这就是要得到所谓的替换算法了。

**Hash Table + Double LinkedList** 那么基于LRU Cache的话，它的替换算法就是 LRU 这三个字代表着它的含义，那么指的是 `least recent use`，指的是**最近最少使用的**就把它昂在最后去淘汰它一般来说，它的实现最后的话是用哈希表再加一个双向链表来实现，这样的的一个结构会是O(1)的查询时间复杂度，也就是说这个元素到底是否存在，直接可以在哈希表里面O(1)的时间可以查到，同时的话你要进行修改和更新的话，具体存元素是存在Double LinkedList里面去，也可以用O(1)的时间去进行修改和更新。

- O(1)查询
- O(1)修改、更新

**LRU Cache 工作示例**

  ![img](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/daac046588de4ed9b8821b7a92e0003a~tplv-k3u1fbpfcp-zoom-1.image)

这个是 LRU Cache，就是这么一段内存放在这个地方，当然它底层的数据结构，就是前面所说的一个双向链表，当然还要配一个哈希表在这个地方。上图就是整个 LRU Cache它的更新原则，那么LRU的话指的是 `least recently used` 就是最近最少被使用的元素就被淘汰出去。

**替换策略**

- LFU - least frequently used （最少使用）
- LRU - least recently used （最近最少使用）

替换算法总览：https://en.wikipedia.org/wiki/Cache_replacement_policies  ![img](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/872e97841e2c4b63a8208112adfc4828~tplv-k3u1fbpfcp-zoom-1.image)

### 实战题目

[146.LRU 缓存机制](https://leetcode-cn.com/problems/lru-cache/#/)

 ![img](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/217a7a5e727a464a98ffed689a2edb9c~tplv-k3u1fbpfcp-zoom-1.image)

实现本题的两种操作，需要用到一个哈希表和一个双向链表。在面试中，面试官一般会期望读者能够自己实现一个简单的双向链表，而不是使用语言自带的、封装好的数据结构。在 Python 语言中，有一种结合了哈希表与双向链表的数据结构 OrderedDict，只需要短短的几行代码就可以完成本题。在 Java 语言中，同样有类似的数据结构 LinkedHashMap。 ![img](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/668abce2192449158d73a36955e000f3~tplv-k3u1fbpfcp-zoom-1.image)

**方法一：Java语言自带的LinkedHashMap**

```
class LRUCache extends LinkedHashMap<Integer, Integer>{
    private int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75F, true);
        this.capacity = capacity;
    }
    
    public int get(int key) {
        return super.getOrDefault(key, -1);
    }
    
    public void put(int key, int value) {
        super.put(key, value);
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
        return size() > capacity;
    }
}
复制代码
```

**方法二：哈希表+双向链表**

**算法** LRU 缓存机制可以通过哈希表辅以双向链表实现，我们用一个哈希表和一个双向链表维护所有在缓存中的键值对。

- 双向链表按照被使用的顺序存储了这些键值对，靠近头部的键值对是最近使用的，而靠靠近尾部的键值对是最久魏使用的。
- 哈希表即为普通的哈希映射（HashMap），通过缓存数据的键映射其在双向链表中的位置。

这样以来，我们首先使用哈希表进行定位，找出缓存项在双向链表中的位置，随后将其移动到双向链表的头部，即可在`O(1)`的时间内完成 `get` 或者 `put` 操作。具体方法如下：

- 对于 

```
  get
```

   操作，首先判断 

```
  key
```

   是否存在：

  - 如果 key 不存在，则返回 -1；
  - 如果 key 存在，则 key 对应的结点是最近被使用的结点。通过哈希表定位到该节点在双向链表中的位置，并将其移动到双向链表的头部，最后返回该结点的值。

- 对于 

```
  put
```

   ，首先判断 

```
  key
```

   是否存在：

  - 如果 key 不存在，使用 key 和 value 创建一个新的结点，在双向链表的头部添加该结点，并将 key 和该结点添加进哈希表中。然后判断双向链表的结点是否超出容量，如果超出容量，则删除双向链表的尾部结点，并删除哈希表中对应的项；
  - 如果 key 存在，则与 get 操作类似，先通过哈希表定位，再将对应的结点的值更新为 value，并将该结点移动到双向链表的头部。

上述各项操作中，访问哈希表的时间复杂度为`O(1)`, 在双向链表的头部添加结点、在双向链表的尾部删除结点的复杂度也为`O(1)`。而将一个结点移动到双向链表的头部，可以分成「删除该结点」和「在双向链表的头部添加结点」两步操作，都可以在`O(1)`时间内完成。

//思路非常清晰

```java
class LRUCache {
    class DLinkedNode {//定义双向链表 存储具体的数据 有key 有value key连接着hashmap的查找
        int key;
        int value;
        DLinkedNode prev;//双端链表前一个
        DLinkedNode next;//双端链表后一个
        public DLinkedNode() {}
        //有参构造
        public DLinkedNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
	//利用hashmap 查询双端链表节点
    private Map<Integer, DLinkedNode> cache = new HashMap<Integer, DLinkedNode>();//注意泛型
    private int size;//定义真实尺寸
    private int capacity;//定义阈值容量
    private DLinkedNode head, tail;//定义双端链表的头和尾部 使用哨兵节点比较easy写代码

    //初始化 有参构造
    public LRUCache(int capacity) {
        this.size = 0;//初始化size=0
        this.capacity = capacity;// 初始化具体的
        // 使用伪头部和伪尾部结点
        head = new DLinkedNode();//初始化
        tail = new DLinkedNode();
        head.next = tail;//两者关系
        tail.prev = head;
    }
    
    public int get(int key) {
        //首先找到这个双向节点 不存在返回 存在就移到前边去 返回值
        DLinkedNode node = cache.get(key);
        if (node == null) {
            return -1;
        }
        // 如果 key 存在，先通过哈希表定位，再移到头部
        moveToHead(node);
        return node.value;
    }
    
    public void put(int key, int value) {
        DLinkedNode node = cache.get(key);//找到双向节点
        //如果不存在 ①新建一个节点②添加hash表映射当中③移动到头部④size++
        //⑤判断size是否大于容量 如果成立 删除末尾元素 删除末尾hash映射 size--
        if (node == null) {
            // 如果 key 不存在，创建一个新的结点
            DLinkedNode newNode = new DLinkedNode(key, value);
            // 添加进哈希表
            cache.put(key, newNode);
            // 添加至双向链表的头部
            addToHead(newNode);
            ++size;
            if (size > capacity) {
                // 如果超出容量，删除双向链表的尾部结点
                DLinkedNode tail = removeTail();
                // 删除哈希表中对应的项
                cache.remove(tail.key);
                --size;
            }
        } else {
            // 如果 key 存在，先通过哈希表定位，再修改value，并移动到头部
            node.value = value;
            moveToHead(node);
        }
    }

    
    //定义两个辅助函数 第一个是在头部添加数据
    private void addToHead(DLinkedNode node) {
        //更改链表指向  四个指向要进行更新 顺序无所谓
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
    //第二个是删除指定的双向链表节点

    private void removeNode(DLinkedNode node) {
        //删除的话两个指向要更新 无所谓顺序
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
 //移动到头部 等于 先删除 再加到头部
    private void moveToHead(DLinkedNode node) {
        removeNode(node);
        addToHead(node);
    }
 //删除尾部 直接删除尾部就可以   最好返回尾部的节点   
    private DLinkedNode removeTail() {
        DLinkedNode res = tail.prev;
        removeNode(res);
        return res;
    }
}
复制代码
LRUCache cache = new LRUCache( 2 /* 缓存容量 */ );

cache.put(1, 1);
cache.put(2, 2);
cache.get(1);       // 返回  1
cache.put(3, 3);    // 该操作会使得关键字 2 作废
cache.get(2);       // 返回 -1 (未找到)
cache.put(4, 4);    // 该操作会使得关键字 1 作废
cache.get(1);       // 返回 -1 (未找到)
cache.get(3);       // 返回  3
cache.get(4);       // 返回  4
复制代码
```

**复杂度分析**

- 时间复杂度：对于 put 和 get 都是 。
- 空间复杂度：，因为哈希表和双向链表最多存储  个元素。

