---
title:  MySQL与Redis区别
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2021-01-17 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/57.jpg
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

**1. MySQL和Redis的数据库类型**

MySQL是关系型数据库,主要用于存放持久化数据,将数据存储在硬盘中,读取速度较慢.

Redis是NOSQL,即非关系型数据库,也是缓存数据库,即将数据存储在缓存中,缓存的读取速度快,能够大大的提高运行效率,但是保存时间有限.

**2. MySQL的运行机制**

MySQL作为持久化存储的关系型数据库,相对薄弱的地方在于每次请求访问数据库时，**都存在着I/O操作**，如果反复频繁的访问数据库.第一:会在反复链接数据库上花费大量时间，从而导致运行**效率过慢;**第二:反复的访问数据库也会导致数据库的负载过高，那么此时缓存的概念就衍生了出来.

**3. 缓存**

缓存就是数据交换的缓冲区(cache)当浏览器执行请求时,首先会对在缓存中进行查找,如果存在就获取;否则就访问数据库.

缓存的好处就是读取速度快.

**4. Redis数据库**

Redis数据库就是一款缓存数据库,用于存储使用频繁的数据,这样减少访问数据库的次数,提高运行效率.

**5. Redis和MySQL的区别总结**

5.1 类型上

从类型上来说,MySQL是关系型数据库,Redis是缓存数据库.

5.2 作用上

MySQL用于持久化的存储数据到硬盘,功能强大,但是速度较慢

Redis用于存储使用较为频繁的数据到缓存中,读取速度快.

5.3 需求上

MySQL和Redis因为需求的不同,一般都是配合使用.

5.4 场景选型上

Redis和MySQL要根据具体业务场景去选型.

5.5 存放位置

数据存放位置MySQL:数据放在磁盘

Redis:数据放在内存

5.6 适合存放数据类型

Redis适合放一些频繁使用,比较热的数据,因为是放在内存中,读写速度都非常快,一般会应用在下面一些场景:排行榜、计数器、消息队列推送、好友关注、粉丝.

**6. 数据可不可以直接全部用Redis存储呢？**

6.1 首先要知道MySQL存储在磁盘里,Redis存储在内存里,Redis既可以用来做持久存储,也可以做缓存,而目前大多数公司的存储都是MySQL + Redis,MySQL作为主存储,Redis作为辅助存储被用作缓存,加快访问读取的速度,提高性能.

6.2 Redis存储在内存中,如果存储在内存中,存储容量肯定要比磁盘少很多,那么要存储大量数据,只能花更多的钱去购买内存,造成在一些不需要高性能的地方是相对比较浪费的,所以目前基本都是MySQL**(主) + Redis(辅),**在需要性能的地方使用Redis,在不需要高性能的地方使用MySQL,好钢用在刀刃上.

6.3 MySQL支持sql查询,可以实现一些关联的查询以及统计.

6.4 Redis对内存要求比较高,在有限的条件下不能把所有数据都放在Redis.

6.5 MySQL偏向于存数据,Redis偏向于快速取数据,但Redis查询复杂的表关系时不如MySQL,所以可以把热门的数据放Redis,MySQL存基本数据.