
---
title:  Mysql 索引底层数据结构是什么
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2021-01-13 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/53.jpg
tags:
  - 数据库
  - Mysql
categories:
  - Mysql
music:
 server: netease   # netease, tencent, kugou, xiami, baidu
 type: song        # song, playlist, album, search, artist
 id: 16846091      # song id / playlist id / album id / search keyword
 
---

## Mysql 索引底层数据结构是什么

`Mysql` 索引采用的数据结构是 `B+` 树(如下图)，与`B树` 的主要区别在于`B+` 树区分了叶子节点和非叶子节点。只有叶子节点才会存储数据，非叶子节点只存储键值。

叶子节点之间使用双向指针连接，最底层的叶子节点形成了一个双向有序链表。 ![B+树](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a460ce260e9a4594989616351f589bdf~tplv-k3u1fbpfcp-zoom-1.image)

## Mysql 索引数据结构为什么选择 B+ 树，而不选择红黑树或者其他树

这个的话讲一下使用`B+` 树的有点就可以了，然后我又回答了个寂寞(问我当时那一刻有点懵，其实我是知道的，然而我就不告诉他)。

1. `B+` 树是多路平衡二叉树，相较于红黑树、二叉树来说，整个树形结构高度会大幅减少，这也意味着使用索引查找的次数会减少，从而提高查询效率。
2. `B+` 数区分了叶子节点和非叶子节点，只有在叶子节点才会真正的存储数据。由于`Mysql`的`InnoDB`存储引擎一次`IO`会读取的一页（默认一页 16K）的数据量。`B树`中叶子节点和非叶子节点都会存储真实数据，也就是说随着列数的增多，所占空间会变大,树相应就会变高，磁盘 IO 次数就会变大。
3. 使用`B+` 能更好的支持范围查找。这里主要说的是性能上，`B树`也可以支持范围查找，但是需要在树往下或者往上查找。`B+` 在叶子节点就已经维护了一个双向有序链表，所以天然就适合用来做范围查找。

## Mysql 索引文件是怎么加载到内存中的

这里在面试的时候我是没有理解面试官的意思的，当出来坐公交复盘的时候突然就想起来了。面试官应该想知道的是使用索引查找的过程，这个过程包含了加载页索引文件（是我太菜了）。

这里需要重点讲突出的是索引文件是按页加载的，比如说我要做一个唯一`id`等值查找，其大概过程如下:

![等值查找](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/3de942fbdb6044c6b636126a7f61e87d~tplv-k3u1fbpfcp-zoom-1.image)
 假如我们查询值等于 9 的数据。查询路径`磁盘块1`->`磁盘块2`->`磁盘块6`。

第一次磁盘 IO：将`磁盘块1`加载到内存中，在内存中从头遍历比较，`9 < 15`，走左路，到磁盘寻址`磁盘块2`。

第二次磁盘 IO：将`磁盘块2`加载到内存中，在内存中从头遍历比较，`7 < 9 < 12`，到磁盘中寻址定位到`磁盘块6`。

第三次磁盘 IO：将`磁盘块6`加载到内存中，在内存中从头遍历比较，在第三个索引中找到 9，取出`Data`，如果`Data`存储的行记录，取出`Data`，查询结束。如果存储的是磁盘地址，还需要根据磁盘地址到磁盘中取出数据，查询终止。（这里需要区分的是在`InnoDB`中`Data`存储的为行数据，而`MyIsam`中存储的是磁盘地址。）

## TCP 怎么确保传输可靠性

面试官问我这个的时候，我的答案是`我不会`（是真的，我是真的不会）!!!

这篇的话就大概讲一下保证可靠性几点，不做深入的分析。我也不会呀，是时候要好好研究一下网络相关的东西了。

1. `TCP`在建立连接采用三次握手和断开连接四次挥手的连接管理机制（当时我只回答了这一个，然后面试官来了一句就因为这个就能确保传输的安全可靠吗。我是真想回答是的）。
2. 检验和。`TCP`检验和的计算与`UDP`一样，在计算时要加上`12byte`的伪首部，检验范围包括`TCP`首部及数据部分，但是`UDP`的检验和字段为可选的，而`TCP`中是必须有的。计算方法为：在发送方将整个报文段分为多个`16位`的段，然后将所有段进行反码相加，将结果存放在检验和字段中，接收方用相同的方法进行计算，如最终结果为检验字段所有位是全 1 则正确（UDP 中为 0 是正确），否则存在错误。
3. 序列号。`TCP`将每个字节的数据都进行了编号，这就是序列号。作用如下：

- 3.1 保证可靠性（当接收到的数据总少了某个序号的数据时，能马上知道）。
- 3.2 保证数据的按序到达。
- 3.1 提高效率，可实现多次发送，一次确认。
- 3.1 去除重复数据。

1. 确认应答机制（`ACK`）。`TCP`通过确认应答机制实现可靠的数据传输。在`TCP`的首部中有一个标志位——`ACK`，此标志位表示确认号是否有效。接收方对于按序到达的数据会进行确认，当标志位`ACK=1`时确认首部的确认字段有效。进行确认时，确认字段值表示这个值之前的数据都已经按序到达了。而发送方如果收到了已发送的数据的确认报文，则继续传输下一部分数据；而如果等待了一定时间还没有收到确认报文就会启动重传机制。
2. 超时重传机制。当报文发出后在一定的时间内未收到接收方的确认，发送方就会进行重传（通常是在发出报文段后设定一个闹钟，到点了还没有收到应答则进行重传）。
3. 流量控制。接收端处理数据的速度是有限的，如果发送方发送数据的速度过快，导致接收端的缓冲区满，而发送方继续发送，就会造成丢包，继而引起丢包重传等一系列连锁反应。
4. 拥塞控制。流量控制解决了两台主机之间因传送速率而可能引起的丢包问题，在一方面保证了 TCP 数据传送的可靠性。然而如果网络非常拥堵，此时再发送数据就会加重网络负担，那么发送的数据段很可能超过了最大生存时间也没有到达接收方，就会产生丢包问题。为此`TCP`引入启动机制，先发出少量数据，就像探路一样，先摸清当前的网络拥堵状态后，再决定按照多大的速度传送数据。此处引入一个拥塞窗口。

## Http 与 Netty 数据传输的区别

实际上这道题是问`RPC`带出来的。当时问了一个`Dubbo` 和 `Feign`有什么的区别？ 这时安酱就往陷阱里面跳了，由于`Dubbo`使用的是`Netty` 传输，然后通过反射调用本地方法，而`Feign` 是通过`RestTemplate`调用`Http`接口的方式。然后就正中面试官下怀，从而引出了这个一个问题，我又回答了个寂寞。
 ![img](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/2115be8d63bd42f0b701e705babc7a73~tplv-k3u1fbpfcp-watermark.image)

这里的话主要是针对于`Http`和`Scoket`，`Netty`本身也是支持`Http`的。

1. `HTTP`是一个属于应用层的面向对象的`协议`；而`Scoket`是应用程序与`TCP/IP`协议交互提供套接字(`Socket`)的接口。
2. `HTTP`属于短连接（虽然支持长连接，但是连接时间是由服务器决定的）；`Scoket`属于长连接，通常情况下`Socket` 连接就是 `TCP` 连接，因此 `Socket` 连接一旦建立,通讯双方开始互发数据内容，直到双方断开连接。
3. `HTTP`是基于请求/响应模式，一次请求过程中，`Server`端和`client`端的角色是不能互换的；`Scoket`属于全双工通信，`Server`端和`client`端是可以相互通讯的。

比较形象的比喻：`Http`是轿车，提供了封装或者显示数据的具体形式; `Socket`是发动机，提供了网络通信的能力。


