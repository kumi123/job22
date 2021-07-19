# jdk 1.7 HashMap死循环问题


**注：JDK1.8之后进行了优化，多线程不会造成死循环问题，但依旧线程不安全，容易造成数据丢失，多线程推荐使用ConcurrentHashMap**

# 原因分析

在了解来龙去脉之前先看HashMap的数据结构
在内部HashMap使用一个Entry数组保存key、value数据，当一对key、value被加入时，会通过一个hash算法得到数组下表index，算法很简单，根据key的hash值，对数组的大小取模hash&(length-1)，并把结果结果插入数组该位置，如果该位置上已经有元素了，就说明hash冲突，这样index位置生成链表。
如果存在hash冲突，最惨情况，就是所有元素定位在同一个位置，形成一个长长链表，这样get值时，最坏的情况需要遍历所有节点，性能变成了O(n)，所以元素的hash值算法和HashMap的初始大小很重要。
当插入一个新节点时，如果不存在相同的key，会判断当前内部元素是否达到阈值(默认是数组大小的0.75)，如果达到这个阈值，会对数组进行扩容，也会对链表中元素进行rehash。

## jdk1.7的put方法实现

- 判断key是否存在



```csharp
public V put(K key,V value){
    if(key == null){
        return putForNullKey(value);
    }    
    int hash = hash(key.hashCode());
    int i = indexFor(hash,table.length);
    // 如果key已经存在，则替换value，并返回旧值
    for(Entry<K,V> e = table[i];e != null;e =e.next){
        Object k;
        if(e.hash == hash &&((k = e.key) == key|| key.equals(k))){
            V oldValue = e.value;
            e.value = value;
            e.recordAccess(this);
            return oldValue;
        }
    }
    modCount++;
    //key不存在，则插入新的元素
    addEntry(hash,key,value,i);
    return null;
}
```

- 检查容量是否达到阈值



```csharp
void addEntry(int hash,K key,V value,int bucketIndex){
    Entry<K,V> e = table[bucketIndex];
    table[bucketIndex] = new Entry<>(hash,key,value,e);
    //检查是否达到扩容阈值
    if(size++ >= threshold){
        resize(2 * table.lengeth);
    }
}
//Entry的构造方法：
Entry(int h, K k, V v, Entry<K,V> n) {
            value = v;
            next = n;
            key = k;
            hash = h;
}      
```

- 扩容的实现



```cpp
void resize(int newCapacity){
    Entry[] oldTable=table;
    int oldCapaicty=oldTable.length;
    if(oldCapaicty == MAXIMUM_CAPACITY){
        threshold = Integer.MAX_VALUE;
        return;
    }
    Entry[] newTable=new Entry[newCapacity];
    transfer(newTable);
   table = newTable;
   threshold = (int)(newCapacity * loadFactor);
}
```

- transfer方法移动元素



```csharp
void transfer(Entry[] newTable){
    Entry[]src=table;
    int newCapacity=newTable.length;
    for(int j=0;j<src.length;j++){
        Entry<K,V> e=src[j];
        if(e!=null){
            src[j]=null;
            do{
                Entry<K,V> next=e.next;
               int i=indexfor(e.hash,newCapacity);
               e.next=newTable[i];
               newTable[i]=e;
               e=next; 
            }while(e!=null);
        }
    }
}
```

==移动逻辑很清晰，遍历原来table中每一个位置的链表，并对每一个元素进行重新hash，在新的newTable找到归宿，并插入。==





==综合来说，HashMap一次扩容的过程：==

==1、取当前table的2倍作为新table的大小==

==2、根据算出的新table的大小new出一个新的Entry数组来，名为newTable==

==3、轮询原table的每一个位置，将每个位置上连接的Entry，算出在新table上的位置，并以链表形式连接==

==4、原table上的所有Entry全部轮询完毕之后，意味着原table上面的所有Entry已经移到了新的table上，HashMap中的table指向newTable案例分析==

==假设HahsMap初始化大小为4，插入3个节点，不巧的是，这三个节点都hash到同一个位置，如果按照默认的负载因子，插入第三个节点就会扩容，为了验证效果，假设负载因子是1。==



![img](https://upload-images.jianshu.io/upload_images/15039238-68fdee014d740173?imageMogr2/auto-orient/strip|imageView2/2/w/572/format/webp)

image


插入第四个节点时，发生rehash，假设现在有两个线程同时进行，线程1和线程2，两个线程都会创建新的数组

![img](https://upload-images.jianshu.io/upload_images/15039238-56e06a48d71a971b?imageMogr2/auto-orient/strip|imageView2/2/w/281/format/webp)

image



- 假设**线程2**在执行到Entry<K,V> next=e.next;之后CPU时间片用完了，这变量e指向节点a。变量next指向节点b；

- 线程1

  继续执行，很不巧a，b，c节点rehash之后又在同一位置7，开始移动节点

  - 第一步移动节点a，如图

    ![img](https://upload-images.jianshu.io/upload_images/15039238-2c434bfb7c7d9567?imageMogr2/auto-orient/strip|imageView2/2/w/346/format/webp)

    image

  - 第二步移动节点b

    ![img](https://upload-images.jianshu.io/upload_images/15039238-68b39fb0dffd56dc?imageMogr2/auto-orient/strip|imageView2/2/w/436/format/webp)

    image

  - 注意

    ，这里的顺序是反过来的继续移动节点c

    ![img](https://upload-images.jianshu.io/upload_images/15039238-28a28709583cd597?imageMogr2/auto-orient/strip|imageView2/2/w/421/format/webp)

    image

- 这个时候

  线程1

  时间片用完，内部的table还没有设置成新的newTabel，

  线程2

  开始执行，这时内部引用关系如下：

  ![img](https://upload-images.jianshu.io/upload_images/15039238-21b9eecea72d0f1d?imageMogr2/auto-orient/strip|imageView2/2/w/205/format/webp)

  image

- 这时，**线程2**中变量e指向节点a，变量next指向节点b，开始执行循环体的剩余逻辑。



```ruby
Entry<K,V> next = e.next;
int i = indexFor(e.hash, newCapacity);
e.next = newTable[i];
newTable[i] = e;
e = next;
```

- 执行后的引用关系如下图：

  ![img](https://upload-images.jianshu.io/upload_images/15039238-23ab3f77e98747c1?imageMogr2/auto-orient/strip|imageView2/2/w/201/format/webp)

  image

- 执行后e的指向节点b，因为e不是null，则继续执行循环体，执行后的引用关系：

![img](https://upload-images.jianshu.io/upload_images/15039238-656a4881a5d060d3?imageMogr2/auto-orient/strip|imageView2/2/w/203/format/webp)

image

- 变量e又重新指向了节点a，只能继续执行循环体，这里仔细分析一下：

  - 执行完Entry<K,V>next=e.next；目前节点a没有next，所以next指向null。

  - ==e.next=newTable[i]；其中newTable[i]执行节点b，那就是把a的next指向了节点b，这样a和b互相引用了，形成一个环；==

  - nnewTable[i]=e把节点a放在了数组i的位置；

  - e=next；把变量e赋值为null。因为第一步中变量next就是指向了null，所以最终引用关系是时这样的：

    ![img](https://upload-images.jianshu.io/upload_images/15039238-94614d050ac29160?imageMogr2/auto-orient/strip|imageView2/2/w/201/format/webp)

    image

- ==节点a和b互相引用，形成一个环，当在数组该位置get寻找对应的key时，就会发生死循环。另外如果线程2把newTable设置成内部table，节点c的数据就丢了，所以还存在数据丢失问题。==

# 总结

并发情况下，发生扩容，可能会产生循环链表，在执行get的时候，会触发死循环，引起CPU的100%问题，所以一定要避免并发环境使用HashMap，如果多线程就用ConcurrentHashMap。