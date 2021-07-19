---
title:  MySQL原则
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2021-01-16 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/56.jpg
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

#### 回表

比如你创建了name, age索引 ng_index，查询数据时使用了

select * from table where name ='bill' and age = 21;

由于附加索引中只有name 和 age，因此命中索引后，数据库还必须回去聚集索引中查找其他数据，这就是回表，这也是你背的那条：少用select * 的原因。

#### 索引覆盖

结合回表会更好理解，比如上述ng_index索引，有查询

select name, age from table where name ='bill' and age = 21;

此时select的字段name,age在索引ng_index中都能获取到，所以不需要回表，满足索引覆盖，效率较高。

#### 最左匹配

B+树的节点存储索引顺序是从左向右存储，在匹配的时候自然也要满足从左向右匹配；

比如索引ng_index，下列sql都可以命中ng_index；

select name from table where name = 'bill';select name, age from table where name = 'bill' and age = 18；

#### 索引下推

还是索引ng_index，有如下sql

select * from table where name like 'B%' and age > 20

该语句有两种执行可能：

命中ng_index联合索引，查询所有满足name以B开头的数据， 然后回表查询所有满足的行。 命中ng_index联合索引，查询所有满足name以B开头的数据，然后直接筛出age>20的索引，再回表查询全行数据， 显然第2种方式回表查询的行数较少，IO次数也会减少，这就是索引下推。所以不是所有like都不会命中索引。

## 建索引的几大原则

（1） 最左匹配原则

> 对于多列索引，总是从索引的最前面字段开始，接着往后，中间不能跳过。比如创建了多列索引(name,age,sex)，会先匹配name字段，再匹配age字段，再匹配sex字段的，中间不能跳过。mysql会一直向右匹配直到遇到范围查询(>、<、between、like)就停止匹配。
>
> 一般，在创建多列索引时，where子句中使用最频繁的一列放在最左边。
>
> ```
> 举例：
> ```
>
> 表user，建立索引（name, sex）
>
> **不符合最左前缀匹配原则的sql语句：**
>
> ```
> EXPLAIN select * from user where sex = 0;
> 复制代码
> ```
>
> ![img](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/f7dba32beffe4f07830a4b5dee991fb9~tplv-k3u1fbpfcp-watermark.image)
>
> 该查询跳过了第一个索引name，直接根据第二个索引sex查询，不符合最左前缀匹配原则
>
> 未使用索引，是一个低效的全表扫描。
>
> **符合最左前缀匹配原则的sql语句：**
>
> ```
> EXPLAIN select * from user where name = "1" and sex = 0;
> 复制代码
> ```
>
> ![img](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ceff8108452f4c38a511d78f3361d1ef~tplv-k3u1fbpfcp-watermark.image)
>
> 该sql先使用了索引的第一个字段name，再使用索引的第二个字段sex，中间没有跳过，符合最左前缀匹配原则。
>
> 该sql使用了索引，仅扫描了一行。
>
> 对比可知，符合最左前缀匹配原则的sql语句比不符合该原则的sql语句效率有极大提高，从全表扫描上升到了常数扫描。

（2） 尽量选择区分度高的列作为索引

> 比如，我们会选择身份证号做索引，而不会选择年龄来做索引。

（3） =和in可以乱序

> 比如a = 1 and b = 2 and c = 3，建立(a,b,c)索引可以任意顺序，mysql的查询优化器会帮你优化成索引可以识别的形式。

（4） 索引列不能参与计算，保持列“干净”

> 比如：age + 1 > 20。原因很简单，假如索引列参与计算的话，那每次检索时，都会先将索引计算一次，再做比较，显然成本太大。

（5） 尽量的扩展索引，不要新建索引

> 比如表中已经有a的索引，现在要加(a,b)的索引，那么只需要修改原来的索引即可。

## 索引的缺点

虽然索引可以提高查询效率，但索引也有自己的不足之处。

索引的额外开销：

(1) 空间：索引需要占用空间；

(2) 时间：查询索引需要时间；

(3) 维护：索引须要维护（数据变更时）；

不建议使用索引的情况：

(1) 数据量很小的表

(2) 空间紧张

## SQL优化

### 1、有索引但未被用到的情况（不建议）

##### (1) Like的参数以通配符开头时

尽量避免Like的参数以通配符开头，否则数据库引擎会放弃使用索引而进行全表扫描。

以通配符开头的sql语句，例如：select * from  where name like '%1';

![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/44dc5a37df9241ab93eb6f5c41c234bf~tplv-k3u1fbpfcp-watermark.image)

这是全表扫描，没有使用到索引，不建议使用。

不以通配符开头的sql语句，例如：select * from user where name like '1%'

![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/1095059d1a8343738da213bfbcaa3462~tplv-k3u1fbpfcp-watermark.image)

很明显，这使用到了索引，是有范围的查找了，比以通配符开头的sql语句效率提高不少。

##### (2) where条件不符合最左前缀原则时

例子已在最左前缀匹配原则的内容中有举例。

##### (3) 使用！= 或 <> 操作符时

尽量避免使用！= 或 <>操作符，否则数据库引擎会放弃使用索引而进行全表扫描。使用`>`或`<`会比较高效。

```
select * from user where id != '1';
复制代码
```

##### (4) 索引列参与计算

应尽量避免在 where 子句中对字段进行表达式操作，这将导致引擎放弃使用索引而进行全表扫描。

```
select * from user where id +1 > '1';
复制代码
```

##### (5) 对字段进行null值判断

应尽量避免在where子句中对字段进行null值判断，否则将导致引擎放弃使用索引而进行全表扫描，如：

低效：

```
select * from user where sex is null ;
复制代码
```

可以在`sex`上设置默认值0，确保表中`sex`列没有null值，然后这样查询：

高效：

```
select * from user where sex = 0;
复制代码
```

##### (6) 使用or来连接条件

应尽量避免在where子句中使用or来连接条件，否则将导致引擎放弃使用索引而进行全表扫描，如：

低效：

```
select * from user where name = '1' or sex = '0';
复制代码
```

可以用下面这样的查询代替上面的or查询：

高效：

```
select from user where name = '1' union all select from user where name = '2';
复制代码
```

#### 2、避免select *

在解析的过程中，会将`*`依次转换成所有的列名，这个工作是通过查询数据字典完成的，这意味着将耗费更多的时间。

所以，应该养成一个需要什么就取什么的好习惯。

#### 3、order by 语句优化

任何在`order by`语句的非索引项或者有计算表达式都将降低查询速度。

方法：

1. 重写order by语句以使用索引；
2. 为所使用的列建立另外一个索引
3. 绝对避免在order by子句中使用表达式。

#### 4、GROUP BY语句优化

提高`GROUP BY`语句的效率, 可以通过将不需要的记录在`GROUP BY`之前过滤掉

低效:

```
SELECT name , AVG(age)

FROM user

GROUP BY name

HAVING name = '1'

OR name = '2'
复制代码
```

高效:

```
SELECT name , AVG(age)

FROM user

WHERE name = ‘1'

OR name = ‘2'

GROUP by name
复制代码
```

#### 5、用 exists 代替 in

很多时候用`exists`代替`in`是一个好的选择：

```
select num from a where num in(select num from b)
复制代码
```

用下面的语句替换：

```
select num from a where exists(select 1 from b where num=a.num)
复制代码
```

#### 6、使用 varchar/nvarchar 代替 char/nchar

尽可能的使用 `varchar/nvarchar` 代替 `char/nchar` ，因为首先变长字段存储空间小，可以节省存储空间，其次对于查询来说，在一个相对较小的字段内搜索效率显然要高些。

#### 7、能用DISTINCT的就不用GROUP BY

```
SELECT order_id FROM Details WHERE price > 10 GROUP BY order_id
复制代码
```

可改为：

```
SELECT DISTINCT order_id FROM Details WHERE price > 10
复制代码
```

#### 8、能用UNION ALL就不要用UNION

UNION ALL不执行SELECT DISTINCT函数，这样就会减少很多不必要的资源。

#### 9、在Join表的时候使用相当类型的例，并将其索引

如果应用程序有很多JOIN 查询，你应该确认两个表中Join的字段是被建过索引的。这样，MySQL内部会启动为你优化Join的SQL语句的机制。

而且，这些被用来Join的字段，应该是相同的类型的。例如：如果你要把 DECIMAL 字段和一个 INT 字段Join在一起，MySQL就无法使用它们的索引。对于那些STRING类型，还需要有相同的字符集才行。（两个表的字符集有可能不一样）


