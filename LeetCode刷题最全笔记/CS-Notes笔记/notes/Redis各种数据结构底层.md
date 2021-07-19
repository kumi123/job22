# Redis各种数据结构底层

## String 字符串

set hello word 为例，因为==Redis 是KV 的数据库==，它是==通过hashtable 实现的==（我们把这个叫做外层的哈希）。所以==每个键值对都会有一个dictEntry==（源码位置：dict.h），==里面指向了key 和value 的指针。next 指向下一个dictEntry==。

![img](https://upload-images.jianshu.io/upload_images/16701032-6495a5280fe02d04.png?imageMogr2/auto-orient/strip|imageView2/2/w/628/format/webp)



==key 是字符串==，但是Redis 没有直接使用C 的字符数组，而是==存储在自定义的SDS==中。

==value== 既不是直接作为字符串存储，也不是直接存储在SDS 中，而是==存储在redisObject 中。实际上五种常用的数据类型的任何一种，都是通过redisObject 来存储的。==

==字符串类型的内部编码有三种==：
1、==int，存储8 个字节的长整型==（long，2^63-1）。
2、==embstr==, 代表embstr 格式的SDS（Simple Dynamic String 简单动态字符串），==存储小于44 个字节的字符串==。
3、==raw==，==存储大于44 个字节的字符串==（3.2 版本之前是39 字节）。

SDS 的特点：
1、==不用担心内存溢出问题，如果需要会对SDS 进行扩容。==
2、==获取字符串长度时间复杂度为O(1)，因为定义了len 属性。==
3、==通过“空间预分配”（ sdsMakeRoomFor）和“惰性空间释放”，防止多次重分配内存。==
4、==判断是否结束的标志是len 属性（它同样以'\0'结尾是因为这样就可以使用C语言中函数库操作字符串的函数了），可以包含'\0'。==

## Hash 哈希

包含键==值对的无序散列表。value 只能是字符串==，不能嵌套其他类型。

同样是存储字符串，Hash 与String 的主要区别？
1、==把所有相关的值聚集到一个key 中，节省内存空间==
2、==只使用一个key，减少key 冲突==
3、当需要批量获取值的时候，只需要使用一个命令，减少内存/IO/CPU 的消耗





==Redis 的Hash 本身也是一个KV 的结构，类似于Java 中的HashMap。==
==外层的哈希（Redis KV 的实现）只用到了hashtable==。当存储hash 数据类型时，叫做内层的哈希。内层的哈希底层可以使用两种数据结构实现：

==ziplist：OBJ_ENCODING_ZIPLIST（压缩列表）==
==hashtable：OBJ_ENCODING_HT（哈希表）==

## List 列表

3.2 版本之后，==统一用quicklist 来存储。quicklist 存储了一个双向链表，每个节点都是一个ziplist。==

![img](https://upload-images.jianshu.io/upload_images/16701032-eaa26c5d0ba0b3f5.png?imageMogr2/auto-orient/strip|imageView2/2/w/1109/format/webp)

## Set集合

==String 类型的无序集合，最大存储数量2^32-1（40 亿左右）。==

==Redis 用intset 或hashtable 存储set==。==如果元素都是整数类型，就用inset 存储。==
==如果不是整数类型，就用hashtable（数组+链表的存来储结构）。==
==问题：KV 怎么存储set 的元素？key 就是元素的值，value 为null。==
==如果元素个数超过512 个，也会用hashtable 存储。==



## ZSet 有序集合

sorted set，有序的set，每个元素有个score。
==score 相同时，按照key 的ASCII 码排序。==



同时满足以下条件时使用==ziplist 编码==：

- 元素数量小于128 个
- 所有member 的长度都小于64 字节

在ziplist 的内部，按照score 排序递增来存储。插入的时候要移动之后的数据。



==超过阈值之后，使用skiplist+dict 存储==。

由于新增加的指针，我们不再需要与链表中每个节点逐个进行比较了。需要比较的节点数大概只有原来的一半。这就是跳跃表