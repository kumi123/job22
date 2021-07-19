---
title:  MySQL锁认识
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2021-01-21 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/62.jpg
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
# MySQL锁共享锁、排他锁、悲观锁、乐观锁及其使用场景



## 一、相关名词

> 表级锁（锁定整个表）
> 页级锁（锁定一页）
> 行级锁（锁定一行）
> 共享锁（S锁，MyISAM 叫做读锁）
> 排他锁（X锁，MyISAM 叫做写锁）
> 悲观锁（抽象性，不真实存在这个锁）
> 乐观锁（抽象性，不真实存在这个锁）

## 二、InnoDB与MyISAM

Mysql 在5.5之前默认使用 MyISAM 存储引擎，之后使用 InnoDB 。查看当前存储引擎：



```mysql
show variables like '%storage_engine%';
```

MyISAM 操作数据都是使用的表锁，你更新一条记录就要锁整个表，导致性能较低，并发不高。当然同时它也不会存在死锁问题。

而 InnoDB 与 MyISAM 的最大不同有两点：一是 InnoDB支持**事务**；二是 InnoDB 采用了**行级锁**。也就是你需要修改哪行，就可以只锁定哪行。

在 Mysql 中，行级锁并不是直接锁记录，而是**锁索引**。索引分为主键索引和非主键索引两种，如果一条sql 语句操作了主键索引，Mysql 就会锁定这条主键索引；如果一条语句操作了非主键索引，MySQL会先锁定该非主键索引，再锁定相关的主键索引。

InnoDB 行锁是通过给索引项加锁实现的，如果没有索引，InnoDB 会通过隐藏的聚簇索引来对记录加锁。也就是说：**如果不通过索引条件检索数据，那么InnoDB将对表中所有数据加锁，实际效果跟表锁一样**。因为没有了索引，找到某一条记录就得扫描全表，要扫描全表，就得锁定表。

## 三、共享锁与排他锁

1.首先说明：**数据库的增删改操作默认都会加排他锁，而查询不会加任何锁**。

> 共享锁：对某一资源加共享锁，自身可以读该资源，其他人也可以读该资源（也可以再继续加共享锁，即 共享锁可多个共存），但无法修改。要想修改就必须等所有共享锁都释放完之后。语法为：
> `select * from table lock in share mode`

> 排他锁：对某一资源加排他锁，自身可以进行增删改查，其他人无法进行任何操作。语法为：
> `select * from table for update` -- **增删改自动加了排他锁**

2.下面援引例子说明（援自：http://blog.csdn.net/samjustin1/article/details/52210125）：

这里用T1代表一个数据库执行请求，T2代表另一个请求，也可以理解为T1为一个线程，T2 为另一个线程。

**例1：**

T1: `select * from table lock in share mode`
（假设查询会花很长时间，下面的例子也都这么假设）

T2: `update table set column1='hello'`

过程：



```html
T1运行（并加共享锁)
T2运行
If T1还没执行完
T2等......
else锁被释放
T2执行
endif
```

T2 之所以要等，是因为 T2 在执行 update 前，试图对 table 表加一个排他锁，而数据库规定同一资源上不能同时共存共享锁和排他锁。所以 T2 必须等 T1 执行完，释放了共享锁，才能加上排他锁，然后才能开始执行 update 语句。

**例2：**

T1: `select * from table lock in share mode`

T2: `select * from table lock in share mode`

这里T2不用等待T1执行完，而是可以马上执行。

分析：
T1运行，则 table 被加锁，比如叫lockAT2运行，再对 table 加一个共享锁，比如叫lockB两个锁是可以同时存在于同一资源上的（比如同一个表上）。这被称为共享锁与共享锁兼容。这意味着共享锁不阻止其它人同时读资源，但阻止其它人修改资源。

**例3：**

T1: `select * from table lock in share mode`

T2: `select * from table lock in share mode`

T3: `update table set column1='hello'`

T2 不用等 T1 运行完就能运行，T3 却要等 T1 和 T2 都运行完才能运行。因为 T3 必须等 T1 和 T2 的共享锁全部释放才能进行加排他锁然后执行 update 操作。

**例4：（死锁的发生）**

T1: `begin transelect * from table lock in share modeupdate table set column1='hello'`

T2: `begin transelect * from table lock in share modeupdate table set column1='world'`

假设 T1 和 T2 同时达到 select，T1 对 table 加共享锁，T2 也对 table 加共享锁，当 T1 的 select 执行完，准备执行 update 时，根据锁机制，T1 的共享锁需要升级到排他锁才能执行接下来的 update.在升级排他锁前，必须等 table 上的其它共享锁（T2）释放，同理，T2 也在等 T1 的共享锁释放。于是死锁产生了。

**例5：**

T1: `begin tranupdate table set column1='hello' where id=10`

T2: `begin tranupdate table set column1='world' where id=20`

这种语句虽然最为常见，很多人觉得它有机会产生死锁，但实际上要看情况

> 如果id是主键（默认有主键索引），那么T1会一下子找到该条记录(id=10的记录），然后对该条记录加排他锁，T2，同样，一下子通过索引定位到记录，然后对id=20的记录加排他锁，这样T1和T2各更新各的，互不影响。T2也不需要等。

> 如果id是普通的一列，没有索引。那么当T1对id=10这一行加排他锁后，T2为了找到id=20，需要对全表扫描。但因为T1已经为一条记录加了排他锁，导致T2的全表扫描进行不下去（其实是因为T1加了排他锁，数据库默认会为该表加意向锁，T2要扫描全表，就得等该意向锁释放，也就是T1执行完成），就导致T2等待。

死锁怎么解决呢？一种办法是，如下：

**例6：**

T1: `begin transelect * from table for updateupdate table set column1='hello'`

T2: `begin transelect * from table for updateupdate table set column1='world'`

这样，当 T1 的 select 执行时，直接对表加上了排他锁，T2 在执行 select 时，就需要等 T1 事物完全执行完才能执行。排除了死锁发生。但当第三个 user 过来想执行一个查询语句时，也因为排他锁的存在而不得不等待，第四个、第五个 user 也会因此而等待。在大并发情况下，让大家等待显得性能就太友好了。

所以，有些数据库这里引入了更新锁（如Mssql，注意：Mysql不存在更新锁）。

**例7：**

T1: `begin transelect * from table (加更新锁)update table set column1='hello'`

T2: `begin transelect * from table (加更新锁)update table set column1='world'`

更新锁其实就可以看成排他锁的一种变形，只是它也允许其他人读（并且还允许加共享锁）。但不允许其他操作，除非我释放了更新锁。T1 执行 select，加更新锁。T2 运行，准备加更新锁，但发现已经有一个更新锁在那儿了，只好等。当后来有 user3、user4...需要查询 table 表中的数据时，并不会因为 T1 的 select 在执行就被阻塞，照样能查询，相比起例6，这提高了效率。

后面还有意向锁和计划锁：意向锁即是：某行修改时，自动加上了排他锁，同时会默认给该表加意向锁，表示里面有记录正被锁定，这时，其他人就不可以对该表加表锁了。如果没有意向锁这个类似指示灯的东西存在，其他人加表锁之前就得扫描全表，查看是否有记录正被锁定，效率低下。而计划锁这些，和程序员关系不大，就没去了解了。

## 四、乐观锁与悲观锁

**案例：**

某商品，用户购买后库存数应-1，而某两个或多个用户同时购买，此时三个执行程序均同时读得库存为n，之后进行了一些操作，最后将均执行`update table set`库存数=n-1，那么，很显然这是错误的。

解决：

1.使用悲观锁（其实说白了也就是排他锁）

> 程序A在查询库存数时使用排他锁（`select * from table where id=10 for update`）

> 然后进行后续的操作，包括更新库存数，最后提交事务。

> 程序B在查询库存数时，如果A还未释放排他锁，它将等待。

> 程序C同B……

2.使用乐观锁（靠表设计和代码来实现）

> 一般是在该商品表添加**version版本**字段或者**timestamp时间戳**字段

> 程序A查询后，执行更新变成了：
> `update table set num=num-1 where id=10 and version=23`

这样，保证了修改的数据是和它查询出来的数据是一致的，而其他执行程序未进行修改。当然，如果更新失败，表示在更新操作之前，有其他执行程序已经更新了该库存数，那么就可以尝试重试来保证更新成功。为了尽可能避免更新失败，可以合理调整重试次数（阿里巴巴开发手册规定重试次数不低于三次）。

总结：对于以上，可以看得出来乐观锁和悲观锁的区别。

1.悲观锁使用了排他锁，当程序独占锁时，其他程序就连查询都是不允许的，导致吞吐较低。如果在查询较多的情况下，可使用乐观锁。

2.乐观锁更新有可能会失败，甚至是更新几次都失败，这是有风险的。所以如果写入较频繁，对吞吐要求不高，可使用悲观锁。

也就是一句话：读用乐观锁，写用悲观锁。