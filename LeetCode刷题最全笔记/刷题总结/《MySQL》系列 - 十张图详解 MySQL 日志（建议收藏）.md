# 《MySQL》系列 - 十张图详解 MySQL 日志（建议收藏）

# 01 前言

事情是这样的，我负责我司的报表系统，小胖是我小弟。某天他手贱误删了一条生产的数据。被用户在群里疯狂投诉质问，火急火燎的跑来问我怎么办。我特么冷汗都出来了，训斥了他一顿：蠢，蠢得都可以进博物馆了，生产的数据能随便动？

小胖看我平常笑嘻嘻的，没想到发这么大的火。心一急，居然给我跪下了：远哥，我上有老，下有小，中有女朋友，不要开除我呀。我一听火更大了：合着就你有女朋友？？？这个时候我们 DBA 老林来打圆场：**别慌，年轻人管不住下本身，难免做错事。我可以把数据恢复到一个月内任意时刻的状态**。听到这，小胖忙抱着老林大腿哭爹喊娘地感谢。

听到这你是不是很奇怪？能恢复到半个月前的数据？DBA 老林到底是如何做到的？我跟他细聊了一番。老林点燃了手中 82 年的华子，深深吸了一口说到：**事情还得从 update 语句是如何执行的说起**。

## 1.1 从更新语句说起

假设我现在有建表语句，如下：

```sql
CREATE TABLE `student`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `age` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
复制代码
```

表数据如下：

![表数据](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/e39f8b439c5549ef9088d67b9cb29093~tplv-k3u1fbpfcp-zoom-1.image)

今天恰好张三生日，我要把它的 age 加一岁。于是执行以下的 sql 语句：

```sql
update student set age = age + 1 where id = 2;
复制代码
```

前面聊过查询语句是如何执行的？错过的同学看这篇[《工作三年：小胖连 select 语句是如何执行的都不知道，真的菜！》](https://juejin.cn/post/6944940454177669128/)，里面的查询语句流程，更新语句也会走一遍，如下流程图：

![Mysql架构图](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ca8ffbfe557a4b069bfd1cbb5b40fbe3~tplv-k3u1fbpfcp-zoom-1.image)

update 语句发起：首先连接器会连接数据库。接着分析器通过词法、语法分析知道这是更新语句。所以查询缓存失效。

之前的文章提到：**如果表有更新。那么它的查询缓存会失败。这也是为啥，我不建议你使用查询缓存的原因**。

优化器则决定使用 ID 索引去更新，最后执行器负责找到这行数据，执行更新。

重点来了：与查询流程不一样，更新还涉及两个重要的日志模块。一是重做日志： redo log，二是归档日志：binlog。要解答文章开头的问题，必须要明白这两日志的原理才能整明白 DBA 是怎么做到的。

# 02 事务日志：redo log

什么是 redo log？为了方便理解，先举个来自极客时间的例子：

> 还记得《孔乙己》这篇文章，饭店掌柜**有一个粉板，专门用来记录客人的赊账记录**。如果赊账的人不多，那么他可以把顾客名和账目写在板上。但如果赊账的人多了，粉板总会有记不下的时候，这个时候掌柜一定还有一个**专门记录赊账的账本**。
>
> 如果有人要赊账或者还账的话，掌柜一般有两种做法：
>
> - 一种做法是直接把账本翻出来，把这次赊的账加上去或者扣除掉；
> - 另一种做法是先在粉板上记下这次的账，等打烊以后再把账本翻出来核算。
>
> 在生意红火柜台很忙时，掌柜一定会选择后者，因为前者操作实在是太麻烦了。首先，你得找到这个人的赊账总额那条记录。你想想，密密麻麻几十页，掌柜要找到那个名字，可能还得带上老花镜慢慢找，找到之后再拿出算盘计算，最后再将结果写回到账本上。
>
> 这整个过程想想都麻烦。相比之下，还是先在粉板上记一下方便。你想想，如果掌柜没有粉板的帮助，每次记账都得翻账本，效率是不是低得让人难以忍受？

## 2.1 为什么需要 redo log？

同样，在 MySQL 中，如果每一次的更新要写进磁盘，这么做会带来严重的性能问题：

- 因为 Innodb 是以页为单位进行磁盘交互的，而一个事务很可能只修改一个数据页里面的几个字节，这时将完整的数据页刷到磁盘的话，太浪费资源了！
- 一个事务可能涉及修改多个数据页，并且这些数据页在物理上并不连续，使用**随机 IO 写入性能太差**！

为了解决这个问题，MySQL 的设计者就用了类似掌柜粉板的思路来提升更新效率。这种思路在 MySQL 中叫 WAL（Write-Ahead Logging），意思就是：**先写 redo log 日志，后写磁盘**。日志和磁盘就对应上面的粉板和账本。

==具体到 MySQL 是这样的：有记录需要更新，InnDB 把记录写到 redo log 中，**并更新内存中的数据页**，此时更新就算完成。同时，后台线程会把操作记录更新异步到磁盘中的数据页。==

PS：**当需要更新的数据页在内存中时，就会直接更新内存中的数据页；不在内存中时，在可以使用 change buffer（篇幅有限，这个后面写文章再聊） 的情况下，就会将更新操作记录到 change buffer 中，并将这些操作记录到 redo log 中；如果此时有查询操作，则触发 merge 操作，返回更改后的记录值**。

==有些人说 InnoDB 引擎把日志记录写到 redo log 中，redo log 在哪，不也是在磁盘上么？==

==**对，这也是一个写磁盘的过程，但是与更新过程不一样的是，更新过程是在磁盘上随机 IO，费时。 而写 redo log 是在磁盘上顺序 IO，效率要高**。==

PPS：==**redo log 的存在就是把全局的随机写，变换为局部的顺序写，从而提高效率**。==

## 2.2 redo log 的写入过程

**redo log 记录了事务对数据页做了哪些修改**。它包括两部分：分别是内存中的日志缓冲（redo log buffer）和磁盘上的日志文件（redo logfile）。

mysql 每执行一条 DML 语句，先将记录写入 redo log buffer，后续某个时间点再一次性将多个操作记录写到 redo log file。也就是我们上面提到的 WAL 技术。

计算机操作系统告诉我们：用户空间下的缓冲区数据是无法直接写入磁盘的。因为中间必须经过操作系统的内核空间缓冲区（OS Buffer）。

所以，**redo log buffer 写入 redo logfile 实际上是先写入 OS Buffer，然后操作系统调用 fsync() 函数将日志刷到磁盘**。过程如下：

![redo log 的写入过程](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/e8eda31174184145a0597d45e0155318~tplv-k3u1fbpfcp-zoom-1.image)

mysql 支持三种将 redo log buffer 写入 redo log file 的时机，可以通过 innodb_flush_log_at_trx_commit 参数配置，各参数值含义如下：**建议设置成1，这样可以保证MySQL 异常重启之后数据不丢失**。

| 参数值                    | 含义                                                         |
| ------------------------- | ------------------------------------------------------------ |
| 0（延迟写）               | 事务提交时不会将 redo log buffer 中日志写到 os buffer，而是每秒写入os buffer 并调用 fsync() 写入到 redo logfile 中。也就是说设置为 0 时是（大约）每秒刷新写入到磁盘中的，当系统崩溃，会丢失1秒钟的数据。 |
| 1（实时写、实时刷新）     | ==事务每次提交都会将 redo log buffer 中的日志写入 os buffer 并调用 fsync() 刷到 redo logfile 中。这种方式即使系统崩溃也不会丢失任何数据，但是因为每次提交都写入磁盘，IO的性能差。== |
| 2（实时写、延迟刷新刷新） | 每次提交都仅写入到 os buffer，然后是每秒调用 fsync() 将 os buffer 中的日志写入到 redo log file。 |

写的过程如下：

![redo log buffer 写入 redo log file 的时机](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/2f7b8493ebfc4e5fb205b5a09181cd63~tplv-k3u1fbpfcp-zoom-1.image)

## 2.3 redo log file 的结构

InnoDB 的 redo log 是固定大小的。比如可以配置为一组 4 个文件，每个文件的大小是 1GB，那么 redo log file 可以记录 4GB 的操作。从头开始写。写到末尾又回到开头循环写。如下图：

![redo log file 的结构](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/81157239ce3845f1bfab370f49f18386~tplv-k3u1fbpfcp-zoom-1.image)

上图中，write pos 表示 redo log 当前记录的 LSN (逻辑序列号) 位置，一边写一遍后移，写到第 3 号文件末尾后就回到 0 号文件开头； check point 表示数据页更改记录刷盘后对应 redo log 所处的 LSN(逻辑序列号) 位置，也是往后推移并且循环的。

**PS：check point 是当前要擦除的位置，它与数据页中的 LSN 应当是一致的**。

write pos 到 check point 之间的部分是 redo log 的未写区域，可用于记录新的记录；==check point 到 write pos 之间是 redo log 已写区域，是待刷盘的数据页更改记录。==

当 write pos 追上 check point 时，表示 redo log file 写满了，这时候有就不能执行新的更新。得停下来先擦除一些记录（擦除前要先把记录刷盘），再推动 check point 向前移动，腾出位置再记录新的日志。

## 2.4 什么是 crash-save ？

有了 redo log ，**即在 InnoDB 存储引擎中，事务提交过程中任何阶段，MySQL 突然奔溃，重启后都能保证事务的完整性，已提交的数据不会丢失，未提交完整的数据会自动进行回滚**。这个能力称为 crash-safe，依赖的就是 redo log 和 undo log 两个日志。

比如：重启 innodb 时，首先会检查磁盘中数据页的 LSN ，如果数据页的 LSN 小于日志中 check point 的 LSN ，则会从 checkpoint 开始恢复。

## 2.5 回滚日志 undo log

==**undo log，主要提供回滚的作用，但还有另一个作用，就是多个行版本控制 (MVCC)，保证事务的原子性。**==在数据修改的流程中，==会记录一条与当前操作相反的逻辑日志到 undo log 中==（可以认为当 delete 一条记录时，undo log 中会记录一条对应的 insert 记录，反之亦然，当 update 一条记录时，它记录一条对应相反的 update 记录），如果因为某些原因导致事务异常失败了，可以借助该 undo log 进行回滚，保证事务的完整性，所以 undo log 也必不可少。

# 03 归档日志：binlog

上一篇聊查询语句的执行过程时，聊到 ==MySQL 的架构包含 server 层和引擎层==。而 ==redo log 是 InnoDB 引擎特有的日志==，而 ==server 层也有自己的日志，那就是 binlog==。

> 最开始 MySQL 里并没有 InnoDB 引擎。MySQ L自带的引擎是 MyISAM，但是 MyISAM 没有 crash-safe 的能力，**binlog 日志只能用于归档**。而 InnoDB 是另一个公司以插件形式引入MySQL 的，只依靠 binlog 是没有 crash-safe 能力的，所以 InnoDB 使用另外一套日志系统——也就是 redo log 来实现 crash-safe 能力。

## 3.1 binlog 日志格式？

binlog 有三种格式，分别为 STATMENT 、 ROW 和 MIXED。

> ==在 MySQL 5.7.7 之前，默认的格式是 STATEMENT ， MySQL 5.7.7 之后，默认值是 ROW。日志格式通过 binlog-format 指定。==

- ==STATMENT：每一条会修改数据的 sql 语句会记录到 binlog 中 。==
- ==ROW：不记录 sql 的上下文信息，仅需记录哪条数据被修改。记两条，更新前和更新后都有。==
- MIXED：前两种模式的混合，一般的复制使用 STATEMENT 模式保存 binlog ，对于 STATEMENT 模式无法复制的操作使用 ROW 模式保存 binlog

## 3.2 binlog 可以做 crash-save 吗？

只用一个 binlog 是否可以实现 cash_safe 能力呢？答案是可以的，只不过 binlog 中也要加入 checkpoint，数据库故障重启后，binlog checkpoint 之后的 sql 都重放一遍。但是这样做让 binlog 耦合的功能太多。

有人说，也可以直接直接对比匹配全量 binlog 和磁盘数据库文件，但这样做的话，效率低不说。因为 binlog 是 server 层的记录并不是引擎层的，有可能导致数据不一致的情况：

> 假如 binlog 记录了 3 条数据，正常情况引擎层也写了 3 条数据，但是此时节点宕机重启，binlog 发现有 3 条记录需要回放，所以回放 3 条记录，但是引擎层可能已经写入了 2 条数据到磁盘，只需要回放一条 1 数据。那 binlog 回放的前两条数据会不会重复呢，比如会报错 duplicate key。

另外，==binlog 是追加写，crash 时不能判定 binlog 中哪些内容是已经写入到磁盘，哪些还没被写入。而 redolog 是循环写，从 check point 到 write pos 间的内容都是未写入到磁盘的。==

所以，binlog 并不适合做 crash-save。

## 3.3 两种日志的区别

redo log 和 binlog 主要有三种不同：

- ==redo log 是 InnoDB 引擎特有的；binlog 是 MySQL 的 Server 层实现的，所有引擎都可以使用。==
- ==redo log 是物理日志，记录的是**在某个数据页上做了什么修改**；binlog 是逻辑日志，记录的是这个语句的原始逻辑，比如**"给 ID=2 这一行的 age 字段加1"**。==
- ==redo log 是循环写的，空间固定会用完；binlog是可以追加写入的。**追加写**是指 binlog文件写到一定大小后会切换到下一个，并不会覆盖以前的日志。==

## 3.4 update 语句的执行流程

了解了两种日志的概念，再来看看执行器和 InnoDB 引擎在执行 update 语句时的流程：

- 执行器取 id = 2 的行数据。ID 是主键，引擎用树搜索找到这一行。如果这一行所在的数据页本来就在内存中，就直接返回给执行器；否则，需要先从磁盘读入内存，再返回。
- 执行器拿到引擎给的行数据，把这个值加上 1，比如原来是 N，现在就是 N+1，得到新的一行数据，再调用引擎接口写入这行新数据。
- 引擎将这行新数据更新到内存中，同时将这个更新操作记录到 redo log 里面，此时 redo log 处于 prepare 状态。然后告知执行器执行完成了，随时可以提交事务。
- 执行器生成这个操作的 binlog，并把 binlog 写入磁盘。
- 执行器调用引擎的提交事务接口，引擎把刚刚写入的 redo log 改成提交（commit）状态，redo log 会写入 binlog 的文件名和位置信息来保证 binlog 和 redo log 的一致性，更新完成。

整个过程如下图所示，其中橙色框表示是在 InnoDB 内部执行的，绿色框表示是在执行器中执行的：

![update 语句的执行过程](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="800" height="600"></svg>)

## 3.5 两阶段提交

由于 redo log 和 binlog 是两个独立的逻辑，如果不用两阶段提交，要么就是先写完 redo log 再写 binlog，或者采用反过来的顺序。我们看看这两种方式会有什么问题。

仍然用前面的 update 语句来做例子。假设当前 id=2 的行，字段 age 的值是 22，再假设执行update 语句过程中在写完第一个日志后，第二个日志还没有写完期间发生了 crash，会出现什么情况呢？

1. **先写redo log 后写binlog**。假设在redo log写完，binlog 还没有写完的时候，MySQL进程异常重启。由于我们前面说过的，redo log 写完之后，系统即使崩溃，仍然能够把数据恢复回来，所以恢复后这一行 age 的值是 22。

但是 binlog 没写完就 crash 了，这时 binlog 里面并没有记录这个语句。因此，之后备份日志的时候，存起来的 binlog 里面就没有这条语句。

等到需要用这个binlog 来恢复临时库的话，由于这个语句的 binlog 丢失，这个临时库就会少了这一次更新，恢复出来的这一行 age 值就是 22，与原库的值不同。

1. 先写 binlog 后写 redo log。如果在 binlog 写完之后 crash，由于 redo log 还没写，崩溃恢复以后这个事务无效，所以 age 的值是 22。但是 binlog 里面已经记录了"把从 22 改成 23" 这个日志。所以，在之后用 binlog 来恢复的时候就多了一个事务出来，恢复出来的这一行 age 的值就是 23，与原库的值不同。

所以，如果不使用"两阶段提交"，数据库的状态就有可能和用 binlog 恢复出来的不一致。

另外：==**sync_binlog 这个参数建议设置成 1，表示每次事务的binlog都持久化到磁盘，这样可以保证 MySQL 异常重启之后 binlog 不丢失**。==

## 3.6 binlog 的应用场景

- 主从复制 ：在 Master 端开启 binlog ，然后将 binlog 发送到各个 Slave 端， Slave 端重放 binlog 从而达到主从数据一致。
- 数据恢复 ：通过使用 mysqlbinlog 工具来恢复数据。

# 04 数据恢复的过程

前面说过，==binlog 会记录所有的逻辑操作，并且是采用"追加写"的形式。如果你的DBA承诺说一个月内可以恢复，那么备份系统中一定会保存最近一个月的所有 binlog，同时系统会定期做整库备份。这里的"定期"取决于系统的重要性，可以是一天一备，也可以是一周一备。==

当需要恢复到指定的某一秒时，比如某天下午两点发现中午十二点有一次误删表，需要找回数据，那你可以这么做：

- 首先，找到最近的一次全量备份，如果你运气好，可能就是昨天晚上的一个备份，从这个备份恢复到临时库；
- 然后，从备份的时间点开始，将备份的binlog依次取出来，重放到中午误删表之前的那个时刻。

这样你的临时库就跟误删之前的线上库一样了，然后你可以把表数据从临时库取出来，按需要恢复到线上库。

看到这里，小胖露出了目视父亲的笑容。

**巨人的肩膀**

- 《高性能MySQL》
- zhihu.com/question/411272546/answer/1375199755
- zhihu.com/question/425750274/answer/1525436152
- time.geekbang.org/column/article/68633
- my.oschina.net/vivotech/blog/4289724
- hiddenpps.blog.csdn.net/article/details/108505371

# 05 总结

本文讲解了事务日志（redo og）的几个方面：为什么需要 redo log？它的写入过程、结构、存的啥以及什么是 crash-save等等；此外还聊了 binlog 的定义、日志格式、与 redo log 的区别、update 语句的执行流程、两阶段提交、以及 binlog 的应用场景。