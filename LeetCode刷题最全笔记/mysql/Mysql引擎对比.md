---
title:  MySQL引擎对比
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2021-01-23 22:20:51
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

存储引擎是数据库的核心，对于mysql来说，存储引擎是以插件的形式运行的。虽然mysql支持种类繁多的存储引擎，但是常用的就那么几种。这篇文章主要是对其进行一个总结和对比。

**MySQL 中查看引擎**

1、show engines;  // 查看mysql所支持的存储引擎，以及从中得到mysql默认的存储引擎。

2、show variables like '% storage_engine';  //  查看mysql 默认的存储引擎

![img](https://img-blog.csdn.net/20150106223151155?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvUUhfSkFWQQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

3、show create  table tablename ;  //  查看具体某一个表所使用的存储引擎，这个默认存储引擎被修改了！

![img](https://img-blog.csdn.net/20150106222921332?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvUUhfSkFWQQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)



4、show table  status from database where name="tablename"

//准确查看某个数据库中的某一表所使用的存储引擎

![img](https://img-blog.csdn.net/20150106224114456?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvUUhfSkFWQQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

## 一、引言

在mysql5之后，支持的存储引擎有十几个，但是常用的就那么几种，而且默认支持的也是InnoDB，既然要进行一个对比，我们就要从不同的维度来看一下。

我们可以使用命令来看看当前数据库可以支持的存储引擎有哪些。



![img](https://pic4.zhimg.com/v2-389b9ab03103f272058860fcafaff03b_r.jpg)



在这里我们发现默认支持了9种。还是比较多的，下面我们进行一个对比。

不同的存储引擎都有各自的特点，以适应不同的需求，如表所示。为了做出选择，首先要考虑每一个存储引擎提供了哪些不同的功能。



![img](https://pic4.zhimg.com/v2-dc3fe4ad61cb8a1f812bc1621b3e5fe7_r.jpg)



在这里我们列举了一些特点并作出了比较。下面我们来具体分析对比一下。



# [重要的区别](https://blog.csdn.net/zgrgfr/article/details/74455547)



## MyISAM存储引擎


MyISAM基于ISAM存储引擎，并对其进行扩展。它是在Web、数据仓储和其他应用环境下最常使用的存储引擎之一。MyISAM拥有较高的插入、查询速度，但**不支持事务**。

MyISAM主要特性有：
1、大文件（达到63位文件长度）在支持大文件的文件系统和操作系统上被支持。
2、当把删除和更新及插入操作混合使用的时候，动态尺寸的行产生更少碎片。这要通过合并相邻被删除的块，以及若下一个块被删除，就扩展到下一块自动完成。
3、每个MyISAM表最大索引数是64，这可以通过重新编译来改变。每个索引最大的列数是16
4、NULL被允许在索引的列中，这个值占每个键的0~1个字节
5、可以把数据文件和索引文件放在不同目录（InnoDB是放在一个目录里面的）

MyISAM引擎使用**B+Tree**作为索引结构，**叶节点的data域存放的是数据记录的地址**。下图是MyISAM索引的原理图：
![MyISAM索引的原理图](https://img-blog.csdn.net/20170705170330879?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemdyZ2Zy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

这里设表一共有三列，假设我们以Col1为主键，则上图是一个MyISAM表的主索引（Primary key）示意。可以看出**MyISAM的索引文件仅仅保存数据记录的地址**。在MyISAM中，主索引和辅助索引（Secondary key）在结构上没有任何区别，只是主索引要求key是唯一的，而辅助索引的key可以重复。如果我们在Col2上建立一个辅助索引，则此索引的结构如下图所示：
![辅助索引的原理图](https://img-blog.csdn.net/20170705170516932?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemdyZ2Zy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

同样也是一颗**B+Tree**，data域保存数据记录的地址。因此，**MyISAM中索引检索的算法为首先按照B+Tree搜索算法搜索索引，如果指定的Key存在，则取出其data域的值，然后以data域的值为地址，读取相应数据记录。**
MyISAM的索引方式也叫做“非聚集”的，之所以这么称呼是为了与InnoDB的聚集索引区分。

## **InnoDB存储引擎**

InnoDB是事务型数据库的首选引擎，支持事务安全表（ACID），支持**行锁定**和**外键**，上图也看到了，**InnoDB是默认的MySQL引擎**。

InnoDB主要特性有：

1、InnoDB给MySQL提供了具有提交、回滚和崩溃恢复能力的事物安全（ACID兼容）存储引擎。InnoDB锁定在行级并且也在SELECT语句中提供一个类似Oracle的非锁定读。这些功能增加了多用户部署和性能。在SQL查询中，可以自由地将InnoDB类型的表和其他MySQL的表类型混合起来，甚至在同一个查询中也可以混合

2、InnoDB是为**处理巨大数据量**的最大性能设计。它的CPU效率可能是任何其他基于磁盘的关系型数据库引擎锁不能匹敌的

3、InnoDB存储引擎完全与MySQL服务器整合，InnoDB存储引擎为在主内存中缓存数据和索引而维持它自己的缓冲池。**InnoDB将它的表和索引在一个逻辑表空间中**，表空间可以包含数个文件（或原始磁盘文件）。这与MyISAM表不同，比如在**MyISAM表中每个表被存放在分离的文件中**。InnoDB表可以是任何尺寸，即使在文件尺寸被限制为2GB的操作系统上

4、**InnoDB支持外键完整性约束**，存储表中的数据时，每张表的存储都按主键顺序存放，如果没有显示在表定义时指定主键，InnoDB会为每一行生成一个6字节的ROWID，并以此作为主键。

虽然InnoDB也使用B+Tree作为索引结构，但具体实现方式却与MyISAM截然不同。
**第一个重大区别是InnoDB的数据文件本身就是索引文件**。从 上文知道，**MyISAM索引文件和数据文件是分离的**，索引文件仅保存数据记录的地址。而在InnoDB中，表数据文件本身就是按B+Tree组织的一个索 引结构，**这棵树的叶节点data域保存了完整的数据记录**。这个索引的key是数据表的主键，因此InnoDB表数据文件本身就是主索引。
![InnoDB主索引](https://img-blog.csdn.net/20170705170833096?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemdyZ2Zy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
上图是InnoDB主索引（同时也是数据文件）的示意图，可以看到叶节点包含了完整的数据记录。这种索引叫做**聚集索引**。因为InnoDB的数据文件本身 要按主键聚集，所以**InnoDB要求表必须有主键**（MyISAM可以没有），如果没有显式指定，则MySQL系统会自动选择一个可以唯一标识数据记录的列 作为主键，如果不存在这种列，则MySQL自动为InnoDB表生成一个隐含字段作为主键，这个字段长度为6个字节，类型为长整形。

**第二个与MyISAM索引的不同是InnoDB的辅助索引data域存储相应记录主键的值而不是地址。**换句话说，InnoDB的所有辅助索引都引用主键作为data域。例如，下图为定义在Col3上的一个辅助索引：
![辅助索引](https://img-blog.csdn.net/20170705171044159?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemdyZ2Zy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
**
这里以英文字符的ASCII码作为比较准则。聚集索引这种实现方式使得按主键的搜索十分高效，但是辅助索引搜索需要检索两遍索引：**首先检索辅助索引获得主键，然后用主键到主索引中检索获得记录**。
**了 解不同存储引擎的索引实现方式对于正确使用和优化索引都非常有帮助**，例如知道了InnoDB的索引实现后，就很容易明白为什么不建议使用过长的字段作为 主键，**因为所有辅助索引都引用主索引，过长的主索引会令辅助索引变得过大。**再例如，用非单调的字段作为主键在InnoDB中不是个好主意，因为 InnoDB数据文件本身是一颗B+Tree，非单调的主键会造成在插入新记录时数据文件为了维持B+Tree的特性而频繁的分裂调整，十分低效，而**使用 自增字段作为主键则是一个很好的选择。**

## **MEMORY存储引擎**

**
**MEMORY存储引擎将表中的数据存储到内存中，未查询和引用其他表数据提供快速访问。**

MEMORY主要特性有：
1、MEMORY表的每个表可以有多达32个索引，每个索引16列，以及500字节的最大键长度
2、MEMORY存储引擎执行HASH和BTREE缩影
3、可以在一个MEMORY表中有非唯一键值
4、MEMORY表使用一个固定的记录长度格式
5、MEMORY不支持BLOB或TEXT列
6、MEMORY支持AUTO_INCREMENT列和对可包含NULL值的列的索引
7、MEMORY表在所由客户端之间共享（就像其他任何非TEMPORARY表）
8、MEMORY表内存被存储在内存中，内存是MEMORY表和服务器在查询处理时的空闲中，创建的内部表共享
9、当不再需要MEMORY表的内容时，要释放被MEMORY表使用的内存，应该执行DELETE FROM或TRUNCATE TABLE，或者删除整个表（使用DROP TABLE）

## **Archive存储引擎**

## **存储引擎的选择**

不同的存储引擎都有各自的特点，以适应不同的需求，如下表所示：
![这里写图片描述](https://img-blog.csdn.net/20170705172036010?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemdyZ2Zy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

**InnoDB** ：如果要提供提交、回滚、崩溃恢复能力的事务安全（ACID兼容）能力，并要求实现并发控制，InnoDB是一个好的选择

**InnoDB 和 MyISAM之间的区别：**
1>.InnoDB支持事物，而MyISAM不支持事物

2>.InnoDB支持行级锁，而MyISAM支持表级锁

3>.InnoDB支持MVCC, 而MyISAM不支持

4>.InnoDB支持外键，而MyISAM不支持

5>.InnoDB不支持全文索引，而MyISAM支持。（X)

**MyISAM**：如果数据表主要用来插入和查询记录，则MyISAM（但是不支持事务）引擎能提供较高的处理效率

**Memory**：如果只是临时存放数据，数据量不大，并且不需要较高的数据安全性，可以选择将数据保存在内存中的Memory引擎，MySQL中使用该引擎作为临时表，存放查询的中间结果。数据的处理速度很快但是安全性不高。

**Archive**：如果只有INSERT和SELECT操作，可以选择Archive，Archive支持高并发的插入操作，但是本身不是事务安全的。Archive非常适合存储归档数据，如记录日志信息可以使用Archive

使用哪一种引擎需要灵活选择，一个数据库中多个表可以使用不同引擎以满足各种性能和实际需求，使用合适的存储引擎，将会提高整个数据库的性能



































## 二、存储引擎

**1、MyISAM**

使用这个存储引擎，每个MyISAM在磁盘上存储成三个文件。

（1）frm文件：存储表的定义数据

（2）MYD文件：存放表具体记录的数据

（3）MYI文件：存储索引

frm和MYI可以存放在不同的目录下。MYI文件用来存储索引，但仅保存记录所在页的指针，索引的结构是B+树结构。下面这张图就是MYI文件保存的机制：



![img](https://pic1.zhimg.com/v2-da3450b2f34b2d8f77af3df998b5ea2c_r.jpg)



从这张图可以发现，这个存储引擎通过MYI的B+树结构来查找记录页，再根据记录页查找记录。并且支持全文索引、B树索引和数据压缩。

支持数据的类型也有三种：

（1）静态固定长度表

这种方式的优点在于存储速度非常快，容易发生缓存，而且表发生损坏后也容易修复。缺点是占空间。这也是默认的存储格式。

（2）动态可变长表

优点是节省空间，但是一旦出错恢复起来比较麻烦。

（3）压缩表

上面说到支持数据压缩，说明肯定也支持这个格式。在数据文件发生错误时候，可以使用check table工具来检查，而且还可以使用repair table工具来恢复。

有一个重要的特点那就是不支持事务，但是这也意味着他的存储速度更快，如果你的读写操作允许有错误数据的话，只是追求速度，可以选择这个存储引擎。

**2、InnoDB**

InnoDB是默认的数据库存储引擎，他的主要特点有：

（1）可以通过自动增长列，方法是auto_increment。

（2）支持事务。默认的事务隔离级别为可重复度，通过MVCC（并发版本控制）来实现的。

（3）使用的锁粒度为行级锁，可以支持更高的并发；

（4）支持外键约束；外键约束其实降低了表的查询速度，但是增加了表之间的耦合度。

（5）配合一些热备工具可以支持在线热备份；

（6）在InnoDB中存在着缓冲管理，通过缓冲池，将索引和数据全部缓存起来，加快查询的速度；

（7）对于InnoDB类型的表，其数据的物理组织形式是聚簇表。所有的数据按照主键来组织。数据和索引放在一块，都位于B+数的叶子节点上；

当然InnoDB的存储表和索引也有下面两种形式：

（1）使用共享表空间存储：所有的表和索引存放在同一个表空间中。

（2）使用多表空间存储：表结构放在frm文件，数据和索引放在IBD文件中。分区表的话，每个分区对应单独的IBD文件，分区表的定义可以查看我的其他文章。使用分区表的好处在于提升查询效率。

对于InnoDB来说，最大的特点在于支持事务。但是这是以损失效率来换取的。

**3、Memory**

将数据存在内存，为了提高数据的访问速度，每一个表实际上和一个磁盘文件关联。文件是frm。

（1）支持的数据类型有限制，比如：不支持TEXT和BLOB类型，对于字符串类型的数据，只支持固定长度的行，VARCHAR会被自动存储为CHAR类型；

（2）支持的锁粒度为表级锁。所以，在访问量比较大时，表级锁会成为MEMORY存储引擎的瓶颈；

（3）由于数据是存放在内存中，一旦服务器出现故障，数据都会丢失；

（4）查询的时候，如果有用到临时表，而且临时表中有BLOB，TEXT类型的字段，那么这个临时表就会转化为MyISAM类型的表，性能会急剧降低；

（5）默认使用hash索引。

（6）如果一个内部表很大，会转化为磁盘表。

在这里只是给出3个常见的存储引擎。使用哪一种引擎需要灵活选择，一个数据库中多个表可以使用不同引擎以满足各种性能和实际需求，使用合适的存储引擎，将会提高整个数据库的性能