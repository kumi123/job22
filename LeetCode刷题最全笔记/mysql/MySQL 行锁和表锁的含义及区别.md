
---
title:  MySQL 行锁和表锁的含义及区别
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2021-01-12 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/52.jpg
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

# [MySQL 行锁和表锁的含义及区别](https://segmentfault.com/a/1190000023662810)



*不可能天天都是好日子，有了不顺心的日子，好日子才会闪闪发亮。*

## 一、前言

*对于行锁和表锁的含义区别，在面试中应该是高频出现的，我们应该对MySQL中的锁有一个系统的认识，更详细的需要自行查阅资料，本篇为概括性的总结回答。*

MySQL常用引擎有MyISAM和InnoDB，而InnoDB是mysql默认的引擎。MyISAM不支持行锁，而InnoDB支持行锁和表锁。

**如何加锁？**

MyISAM在执行查询语句（SELECT）前，会自动给涉及的所有表加读锁，在执行更新操作（UPDATE、DELETE、INSERT等）前，会自动给涉及的表加写锁，这个过程并不需要用户干预，因此用户一般不需要直接用LOCK TABLE命令给MyISAM表显式加锁。

**显式加锁：**

上共享锁（读锁）的写法：`lock in share mode`，例如：

```
select  math from zje where math>60 lock in share mode；
```

上排它锁（写锁）的写法：`for update`，例如：

```
select math from zje where math >60 for update；
```

## 二、表锁

**不会出现死锁，发生锁冲突几率高，并发低。**

### MyISAM引擎

MyISAM在执行查询语句（select）前，会自动给涉及的所有表加读锁，在执行增删改操作前，会自动给涉及的表加写锁。

MySQL的表级锁有两种模式：

- 表共享读锁
- 表独占写锁

**读锁会阻塞写，写锁会阻塞读和写**

- 对MyISAM表的读操作，不会阻塞其它进程对同一表的读请求，但会阻塞对同一表的写请求。只有当读锁释放后，才会执行其它进程的写操作。
- 对MyISAM表的写操作，会阻塞其它进程对同一表的读和写操作，只有当写锁释放后，才会执行其它进程的读写操作。

MyISAM不适合做写为主表的引擎，因为写锁后，其它线程不能做任何操作，大量的更新会使查询很难得到锁，从而造成永远阻塞

## 三、行锁

**会出现死锁，发生锁冲突几率低，并发高。**

在MySQL的InnoDB引擎支持行锁，与Oracle不同==，MySQL的行锁是通过索引加载的，也就是说，行锁是加在索引响应的行上的，要是对应的SQL语句没有走索引，则会全表扫描，行锁则无法实现，取而代之的是表锁==，此时其它事务无法对当前表进行更新或插入操作。

```
CREATE TABLE `user` (
`name` VARCHAR(32) DEFAULT NULL,
`count` INT(11) DEFAULT NULL,
`id` INT(11) NOT NULL AUTO_INCREMENT,
PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8

-- 这里，我们建一个user表，主键为id



-- A通过主键执行插入操作，但事务未提交
update user set count=10 where id=1;
-- B在此时也执行更新操作
update user set count=10 where id=2;
-- 由于是通过主键选中的，为行级锁，A和B操作的不是同一行，B执行的操作是可以执行的



-- A通过name执行插入操作，但事务未提交
update user set count=10 where name='xxx';
-- B在此时也执行更新操作
update user set count=10 where id=2;
-- 由于是通过非主键或索引选中的，升级为为表级锁，
-- B则无法对该表进行更新或插入操作，只有当A提交事务后，B才会成功执行
```

### for update

如果在一条select语句后加上for update，则查询到的数据会被加上一条排它锁，其它事务可以读取，但不能进行更新和插入操作

```
-- A用户对id=1的记录进行加锁
select * from user where id=1 for update;

-- B用户无法对该记录进行操作
update user set count=10 where id=1;

-- A用户commit以后则B用户可以对该记录进行操作
```

**行锁的实现需要注意：**

1. 行锁必须有索引才能实现，否则会自动锁全表，那么就不是行锁了。
2. 两个事务不能锁同一个索引。
3. insert，delete，update在事务中都会自动默认加上排它锁。

**行锁场景：**

A用户消费，service层先查询该用户的账户余额，若余额足够，则进行后续的扣款操作；这种情况查询的时候应该对该记录进行加锁。

否则，B用户在A用户查询后消费前先一步将A用户账号上的钱转走，而此时A用户已经进行了用户余额是否足够的判断，则可能会出现余额已经不足但却扣款成功的情况。

为了避免此情况，需要在A用户操作该记录的时候进行for update加锁

### 扩展：间隙锁

当我们用范围条件而不是相等条件检索数据，并请求共享或排他锁时，InnoDB会给符合条件的已有数据记录的索引项加锁；对于键值在条件范围内并不存在的记录，叫做间隙

InnoDB也会对这个"间隙"加锁，这种锁机制就是所谓的间隙锁

```
-- 用户A
update user set count=8 where id>2 and id<6

-- 用户B
update user set count=10 where id=5;
```

如果用户A在进行了上述操作后，事务还未提交，则B无法对2~6之间的记录进行更新或插入记录，会阻塞，当A将事务提交后，B的更新操作会执行。

### 建议：

- 尽可能让所有数据检索都通过索引来完成，避免无索引行锁升级为表锁
- 合理设计索引，尽量缩小锁的范围
- 尽可能减少索引条件，避免间隙锁
- 尽量控制事务大小，减少锁定资源量和时间长度





### 另外的一些例子



# MySQL -- 行锁

#### 一、行锁概念及特点

1.概念：给单独的一行记录加锁，主要应用于innodb表存储引擎

2.特点：在innodb存储引擎中应用比较多，支持事务、开销大、加锁慢；会出现死锁；锁的粒度小，并发情况下，产生锁等待的概率比较低，所以支持的并发数比较高。

#### 二、数据库事务

1.概念：事务是一系列操作组成的工作单元，该工作单元内的操作是不可分割的，也就是说要么全部都执行，要么全部不执行。

2.特性：ACID

- 原子性：事务是最小的工作单元，不可分割，要么都做，要么都不做
- 一致性：事务执行前和执行后的数据要保证正确性，数据完整性没有被破坏。
- 隔离性：在并发事务执行的时候，一个事务对其他事务不会产生影响。
- 持久性：一个事务一旦提交，它对数据库中的数据的改变就应该是永久性的

#### 三、多个事务并发执行 问题及解决方案

**1.问题**

- 丢失更新：在没有事务隔离的情况下，两个事务同时更新一条数据，后一个事务 会 覆盖前面事务的更新，导致前面的事务丢失更新。
- 脏读：事务A先更新数据，但是没有提交，事务B读到了事务A没有提交的数据。
- 不可重复读：事务A中，先读到一条数据，事务A还没有结束，此时，事务B对该条数据进行了修改操作，事务A又读到了这条数据，事务A两次读到的数据不同。
- 幻读：事务A先读到一批数据，假设读到10条，事务B插入了一条数据，此时，事务A又读这一批数据，发现多了一条，好像幻觉一样。

> 注：不可重复读的重点是修改，同样的条件，你读取过的数据，再次读取出来发现值不一样。
>
> 幻读的重点在于新增或者删除，同样的条件，第 1 次和第 2 次读出来的记录数不一样。

**2.解决方案--数据库隔离机制**

- 未提交读（read uncommitted）：这是数据库最低的隔离级别，允许一个事务读另一个事务未提交的数据。

  `解决了丢失更新，但是会出现脏读、不可重复读、幻读。`

- 提交读（read committed）：一个事务更新的数据 在提交之后 才可以被另一个事务读取，即一个事务不可以读取到另一个事务未提交的数据。

  `解决了丢失更新和脏读，但是会出现不可重复读和幻读。`

- 可重复读（repeatale read）：这是数据库默认的事务隔离级别，保证一个事务在相同条件下前后两次读取的数据是一致的。

  `解决了丢失更新、脏读和不可重复读，但是会出现幻读。`

- 序列化（serializable）：这是数据库最高的隔离级别。事务串行执行，不会交叉执行。

  `解决了所有的问题。`

注：乐观所可以解决幻读。

#### 四、行锁的特性

查看mysql事务隔离级别：show variables like 'tx_iso%';

前提：set autocommit=0; // 设置自动提交事务为手动提交 

```
/* 行锁案例*/
create table lock_two(
 id int,
 col int
)engine=innodb;

insert into lock_two(id,col) values (1,1);
insert into lock_two(id,col) values (2,2);
insert into lock_two(id,col) values (3,3);
复制代码
```

1.在session1中执行update : update lock_two set col=11 where id=1;

（1）分别在session1和session2中查询lock_two，看id为1的记录的col是否修改了。



![img](https://user-gold-cdn.xitu.io/2018/7/24/164c9dbc27490016?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



发现session1 的记录修改了，session2中的记录没有被修改。

（2）在session1中执行commite后，然后再在session2中查询：



![img](https://user-gold-cdn.xitu.io/2018/7/24/164c9dbc298853f4?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



发现session2中的表数据改变了。

2.在session1中执行update：update lock_two set col=11 where id=1，不执行commit;

在session2中执行uodate ：update lock_two set col=11 where id=1，不执行commit;



![img](https://user-gold-cdn.xitu.io/2018/7/24/164c9dbc29764bfd?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

发现session2中的update发生阻塞，并且超过一段时间报错。



3.在session1中执行update：update lock_two set col=22 where id = 2; 不执行commit

在session2中执行另一条update：update lock_two set col=33 where id = 3;



![img](https://user-gold-cdn.xitu.io/2018/7/24/164c9dbc29830a1b?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

  此时，session2中的update发生阻塞，在没发生错误的情况下，session1执行commit，session2中的update会马上执行。



4.在lock_two中创建索引，

```
create index idx_id on lock_two(id);
create index idx_col on lock_two(col);
复制代码
```

然后重复第3步，



![img](https://user-gold-cdn.xitu.io/2018/7/24/164c9dbc29c3e094?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



发现session2可以更新，不会产生阻塞。因为用上了索引，相当于行锁。

结论：如果没有用上索引，行锁变成表锁

#### 五、手动锁一行记录格式

```
begin;
select * from lock_two where id=2 for update;
复制代码
```

1. 在session1中执行上面语句，在ssesion2中可以查看，但是不可以修改 sesion1中的for update 的记录。
2. 当session1中执行commit后，seesion2中的update立刻执行。

#### 六、间隙锁

1.定义：在范围查找的情况下， innodb会给范围条件中的数据加上锁，无论数据是否是否真实存在。

2.例子：

在session1中update：update lock_two set col=666 where id>2 and id<8;

1. 在session2中执行insert：insert into lock_two values(9,99);

插入执行成功！

1. 在session2中执行insert：insert into lock_two values(7,77);

插入阻塞，一段时间后报错！

执行select：select * from lock_two where id=4;

查询成功！

建议：在innodb中，因为有间隙锁的存在，最好在where中少使用这种范围查找。

#### 七、查看行锁的信息

show status like 'innodb_row_lock%';



![img](https://user-gold-cdn.xitu.io/2018/7/24/164c9dbc29b10271?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)



说明：

- Innodb_row_lock_current_waits ：当前正在等待的数量
- Innodb_row_lock_time: 从启动到现在锁定的总时长，单位是ms
- Innodb_row_lock_time_avg :锁等待的平均时长
- Innodb_row_lock_time_max：等待锁时间最长的一个时间
- Innodb_row_lock_waits：总共的等待次数