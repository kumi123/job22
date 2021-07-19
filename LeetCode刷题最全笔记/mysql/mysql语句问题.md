---
title:  MySQL语句问题
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2021-01-24 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/65.jpg
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

## [使用select for share，for update的场景及死锁陷阱](https://www.cnblogs.com/qilong853/p/9427145.html)

SELECT ... FOR SHARE 和 SELECT ... FOR UPDATE语句是innodb事务中的常用语句
for share会给表增加一个is锁，给记录行增加一个s锁，for update会给表增加一个ix锁，给记录行增加一个x锁。

## SELECT ... FOR SHARE使用场景

他们的意思就如语法表示的一样，SELECT ... FOR SHARE，我选择一些记录，这些记录可以share，其他事务也可以读，但是如果你要修改，不好意思，我加了一个s锁，你是不可以修改的。这个语句的应用场景之一是用来读取到最新的数据。
例如，因为innodb中mvcc机制的存在，在可重复读隔离级别下，A事务修改某一行的数据，B事务在A事务提交前是看不到A事务对该行的修改的，但是利用SELECT ... FOR SHARE，B事务会等待A事务释放该行的锁才能查看到该行数据。
创建一个测试表：

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
-- ----------------------------
-- Table structure for test_tab
-- ----------------------------
DROP TABLE IF EXISTS `test_tab`;
CREATE TABLE `test_tab` (
`f1` int(11) NOT NULL AUTO_INCREMENT,
`f2` varchar(11) NOT NULL DEFAULT '1',
PRIMARY KEY (`f1`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of test_tab
-- ----------------------------
INSERT INTO `test_tab` VALUES ('1', '1');
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

![img](https://images2018.cnblogs.com/blog/654151/201808/654151-20180805205058915-918894246.png)

 

## SELECT ... FOR UPDATE使用场景

下面再说SELECT ... FOR UPDATE，我选择一些记录，这些select的记录是我下一步要update的，你要读或者修改这些记录，不好意思，我加的是x锁，你读不了也改不了。只有我当前事务提交了，这些记录你才可以读到或者修改。这个语句的应用场景之一是为了防止更新丢失。
例如，A事务和B事务同时读取银行账户余额，是2元钱，A事务看到2元，消费了1元，将余额更新为1元，B事务看到2元，消费了1元，也将余额更新为1元，那么账户变为1元，但是实际应该扣费2元。使用SELECT ... FOR UPDATE读取记录，可以避免这种丢失更新的现象
丢失更新现象：
![img](https://images2018.cnblogs.com/blog/654151/201808/654151-20180805205118274-997962092.png)

 

防止丢失更新
![img](https://images2018.cnblogs.com/blog/654151/201808/654151-20180805205137078-1500531976.png)

 

可能有人看到这里会有疑问：为什么innodb采用MVCC这种多版本并发控制，每次看到的不是最新的数据，而是以前的一个快照呢？
这是因为一个事务的操作有可能成功，也有可能失败rollback，在一个事务commit之前，被其他事务读到还没提交的变更记录，会产生数据不一样的现象（脏读），这种情况就是innodb最低的隔离级别READ UNCOMMITTED，可以读到没有commit的数据。
那么如果想要不产生脏读，容易想到的是采用锁的方式，当一个事务更改某行记录，就加上锁，其他事务等待该事务执行完毕才能读取到该行记录，但是这样做的话会产生大量的锁占用与等待，效率是非常低下的，因此innoDB采用了MVCC的方式。简单的说，A事务变更某行记录，innodb会产生对应的redo log，如果接下来A事务进行回滚，innodb可以根据redo log将记录回滚到事务开始之前的状态。在A事务没有结束时，如果B事务来查询该行记录，B事务会根据A事务变更后的记录值（在内存中）加上redo log“计算”出A事务开始前的该行记录值，从而读取到该行记录的一个快照，其中并不会产生锁与等待。
如果是可重复读REPEATABLE READ的隔离级别（默认隔离级别），B事务进行过程中看到的始终会是B事务开始前的记录行快照信息，不管B事务进行过程中A事务有没有完成；如果是提交读READ COMMITTED级别，B事务进行过程中，可以看到A事务提交对记录行修改值（即如果A事务没有完成，B查询到的是A事务开始前的记录值，如果A事务完成了，B事务查询到的是A事务完成后的记录值），在这种情况下会产生不可重复读的现象，即同一次事务中多次查询看到的结果会不一样。



## 使用select for share，for update的陷阱

再说使用select for share，for update的陷阱，for share会给记录行增加一个s锁，for update会给记录行增加一个x锁。如果此时有另一个事务B也想给这些记录行加s锁或者x锁，此时就会产生等待，即事务B等待事务A，此时，如果事务A对这些记录行想加上另一个类型的锁，就会产生死锁，用等待图来表示就是，事务B在等待事务A释放资源，接下来，事务A又必须等待事务B释放资源，如此形成了一个有向的环。让我们举例说明，为了方便观察，我们将锁等待超时时间设置长一点，首先，来看一个互相占用资源的例子:

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
-- ----------------------------
-- Table structure for test_tab
-- ----------------------------
DROP TABLE IF EXISTS `test_tab`;
CREATE TABLE `test_tab` (
`f1` int(11) NOT NULL AUTO_INCREMENT,
`f2` varchar(11) NOT NULL DEFAULT '1',
PRIMARY KEY (`f1`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of test_tab
-- ----------------------------
INSERT INTO `test_tab` VALUES ('1', '1');
INSERT INTO `test_tab` VALUES ('2', '1');
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

### 死锁示例1：

![img](https://images2018.cnblogs.com/blog/654151/201808/654151-20180805205215191-899158725.png)

 

上面的示例中，A等B，只要B释放资源，A就可以进行下去，但是B接下来的操作是去等待A，形成了一个环，产生死锁。
这种互相占有不同资源的例子等待对方释放应该是最常见的死锁场景了，下面，我们来看一下不常见的

 

### 死锁示例2：

 

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
-- ----------------------------
-- Table structure for test_tab
-- ----------------------------
DROP TABLE IF EXISTS `test_tab`;
CREATE TABLE `test_tab` (
`f1` int(11) NOT NULL AUTO_INCREMENT,
`f2` varchar(11) NOT NULL DEFAULT '1',
PRIMARY KEY (`f1`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of test_tab
-- ----------------------------
INSERT INTO `test_tab` VALUES ('1', '1');
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

![img](https://images2018.cnblogs.com/blog/654151/201808/654151-20180805205233332-1046488758.png)

上述两个事务并没有互相占有不同资源，B事务甚至没有实际占有资源，但是也产生了死锁，原因是在第二步中B事务等待A事务释放资源，并且B事务要求分配一个x锁，接下来A事务需要一个f1=1的x锁，但是此时B事务已经在等待x锁，A事务只有一个s锁，并不能升级成x锁，因此A事务需要等待B。最终形成B等A，A又等B的环状图，产生死锁。
如果第一步中A事务使用的是for update呢？那么这种死锁情况就不会发生，因为for update语句已经申请到一个x锁，A事务此时持有x锁就可以直接在第3步执行删除操作，并不需要等待B事务的任何资源。

 

### 死锁示例3：

下面是一个因插入导致产生的死锁，数据库创建及数据同上
![img](https://images2018.cnblogs.com/blog/654151/201808/654151-20180805205256632-1257475693.png)

 

上面这个例子可以看做innoDB中“幻行”的解决方案，使用for share或者for update语句将锁定记录及记录之间的空白区间，阻止任何其他事务在该区间中插入数据（如果其他事务允许插入，这将导致同一个事务中多次读取到不一样的数据，如A事务select，B事务insert提交，A事务select for share，可以读取到B事务刚刚提交的记录）

此外，根据测试，在mysql8.0中如果next key锁区间重合，那么只能第一个事务拥有该区间的锁，其他事务不是等待该区间的锁，而是等待该区间第一个数据的锁，这方面的原因不明。如果再配合max等函数的话，又会出现一些神奇的死锁现象，例如插入意向锁的冲突。这些方面估计只有查看innodb的源码才能知道原因了，这里不深入探究了。

总之，明白死锁的原因是由于事务之间互相等待对方占有的资源，在等待图中形成了环即可，分析死锁有以下方式：
查看当前事务
SELECT * FROM information_schema.INNODB_TRX;
查看当前锁
SELECT * FROM `performance_schema`.data_locks;
查看当前锁等待
SELECT * FROM `performance_schema`.data_lock_waits;
分析死锁日志：
show ENGINE INNODB STATUS;
在日志中搜索“LATEST DETECTED DEADLOCK”

我们看到，使用for update或者for share时有可能发生死锁情况，虽然死锁并不可怕，mysql拥有死锁检测的机制打破死锁并且我们可以重新选择执行该事物，当时当死锁频繁出现时，还是应当注意并加以排查的。最好的情况是不出现死锁，因此如果快照数据满足要求时，少用for share或者for update语句，虽然有时你看起来只是在一行记录上加锁，但是由于间隙锁和下一个键锁的存在，锁住的可能不止是一行记录。