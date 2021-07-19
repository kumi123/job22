---
title:  MySQL 覆盖索引详解
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2021-01-11 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/51.jpg
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
# MySQL 覆盖索引详解

这是一篇学习后的心得分享，在正文之前，我们需要对一些索引的基本概念进行说明讲解。

## 1. 什么是索引?

索引（在 MySQL 中也叫“键key”）是存储引擎快速找到记录的一种数据结构，通俗来说类似书本的目录，这个比方虽然被用的最多但是也是最恰如其当的，在查询书本中的某个知识点不借助目录的情况下，往往都找的够呛，那么索引相较于数据库的重要性也可见一斑。

## 2. 索引的有哪些种类？

索引的种类这里只罗列出InnoDB支持的索引：主键索引(PRIMARY)，普通索引(INDEX)，唯一索引(UNIQUE)，组合索引，总体划分为两类，主键索引也被称为聚簇索引（clustered index），其余都称呼为非主键索引也被称为二级索引（secondary index）。

## 3. InnoDB的不同的索引组织结构是怎样的呢？

众所周知在InnoDB引用的是B+树索引模型，这里对B+树结构暂时不做过多阐述，很多文章都有描述，在第二问中我们对索引的种类划分为两大类主键索引和非主键索引，那么问题就在于比较两种索引的区别了，我们这里建立一张学生表，其中包含字段id设置主键索引、name设置普通索引、age(无处理)，并向数据库中插入4条数据：（"小赵", 10）（"小王", 11）（"小李", 12）（"小陈", 13）

```
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '名称',
  `age` int(3) unsigned NOT NULL DEFAULT '1' COMMENT '年龄',
  PRIMARY KEY (`id`),
  KEY `I_name` (`name`)
) ENGINE=InnoDB;

INSERT INTO student (name, age) VALUES("小赵", 10),("小王", 11),("小李", 12),("小陈", 13);
复制代码
```

这里我们设置了主键为自增，那么此时数据库里数据为



![img](https://user-gold-cdn.xitu.io/2019/10/15/16dcff55c1ff558f?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

每一个索引在 InnoDB 里面对应一棵B+树，那么此时就存着两棵B+树。





![img](https://user-gold-cdn.xitu.io/2019/10/15/16dd012fccb51ee0?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

可以发现区别在与叶子节点中，主键索引存储了整行数据，而非主键索引中存储的值为主键id, 在我们执行如下sql后



```
SELECT age FROM student WHERE name = '小李'；
复制代码
```

流程为：

1. 在name索引树上找到名称为小李的节点 id为03
2. 从id索引树上找到id为03的节点 获取所有数据
3. 从数据中获取字段命为age的值返回 12

**在流程中从非主键索引树搜索回到主键索引树搜索的过程称为：回表**，在本次查询中因为查询结果只存在主键索引树中，我们必须回表才能查询到结果，那么如何优化这个过程呢？引入正文覆盖索引

## 4. 什么是覆盖索引？

覆盖索引（covering index ，或称为索引覆盖）即从非主键索引中就能查到的记录，而不需要查询主键索引中的记录，避免了回表的产生减少了树的搜索次数，显著提升性能。

## 5. 如何使用是覆盖索引？

之前我们已经建立了表student，那么现在出现的业务需求中要求根据名称获取学生的年龄，并且该搜索场景非常频繁，那么先在我们删除掉之前以字段name建立的普通索引，以name和age两个字段建立联合索引，sql命令与建立后的索引树结构如下

```
ALTER TABLE student DROP INDEX I_name;
ALTER TABLE student ADD INDEX I_name_age(name, age);
复制代码
```



![img](https://user-gold-cdn.xitu.io/2019/10/16/16dd033b2a2c7c24?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

那在我们再次执行如下sql后



```
SELECT age FROM student WHERE name = '小李'；
复制代码
```

流程为：

1. 在name,age联合索引树上找到名称为小李的节点
2. 此时节点索引里包含信息age 直接返回 12

## 6. 如何确定数据库成功使用了覆盖索引呢？

当发起一个索引覆盖查询时，在explain的extra列可以看到using index的信息

![img](https://user-gold-cdn.xitu.io/2019/10/16/16dd03e788c92e7e?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

这里我们很清楚的看到Extra中Using index表明我们成功使用了覆盖索引



> 总结：覆盖索引避免了回表现象的产生，从而减少树的搜索次数，显著提升查询性能，所以使用覆盖索引是性能优化的一种手段，文章有不当之处，欢迎指正~