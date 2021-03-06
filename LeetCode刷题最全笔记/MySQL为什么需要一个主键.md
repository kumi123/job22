# MySQL为什么需要一个主键



#### 主键

表中==每一行都应该有可以唯一标识自己的一列（==或一组列）。

一个顾客可以使用顾客编号列，而订单可以使用订单ID，雇员可以使用雇员ID 或 雇员社会保险号。

**主键（primary key） 一列（或一组列）**，其值能够唯一区分表中的每个行。
唯一标识表中每行的这个列（或这组列）称为主键。***==没有主键，更新或删除表中特定行很困难，因为没有安全的方法保证只设计相关的行。==\***

虽然并不总是都需要主键，但大多数数据库设计人员都应保证他们创建的每个表有一个主键，以便于以后数据操纵和管理

表中的任何列都可以作为主键，只要它满足一下条件：







```cpp
 1、任何两行都不具有相同的主键值
 2、每个行都必须具有一个主键值（主键列不允许NULL值）
```

主键值规范：这里列出的规则是MySQL本身强制实施的。

主键的最好习惯：
除MySQL强制实施的规则外，应该坚持的几个普遍认为的最好习惯为：



```undefined
 1、不更新主键列的值
 2、不重用主键列的值
 3、不在主键列中使用可能会更改的值（例如，如果使用一个名字作为主键以标识某个供应商，应该供应商合并和更改其名字时，必须更改这个主键）
```

总之：不应该使用一个具有意义的column（id 本身并不保存表 有意义信息） 作为主键，并且一个表必须要有一个主键，为方便扩展、松耦合，高可用的系统做铺垫。

------

==主键的作用，在于索引。==



### 为什么只有一个索引

在MySQL等数据库中，主键也是聚簇索引。这提供了一个更直接的原因。数据根据聚簇索引在页面上排序。一张表只能有一个排序顺序。





**无特殊需求下Innodb建议使用与业务无关的==自增ID作为主键==**

==InnoDB引擎使用聚集索引，数据记录本身被存于主索引（一颗B+Tree）的叶子节点上。这就要求同一个叶子节点内（大小为一个内存页或磁盘页）的各条数据记录按主键顺序存放，因此每当有一条新的记录插入时，MySQL会根据其主键将其插入适当的节点和位置，如果页面达到装载因子（InnoDB默认为15/16），**则开辟一个新的页（节点）**==

1、==如果表使用自增主键，那么每次插入新的记录，记录就会顺序添加到当前索引节点的后续位置，当一页写满，就会自动开辟一个新的页。==

==这样就会形成一个紧凑的索引结构，近似顺序填满。**由于每次插入时也不需要移动已有数据，因此效率很高，也不会增加很多开销在维护索引上。**==

2、 如果使用非自增主键（如果身份证号或学号等），由于==每次插入主键的值近似于随机，因此每次新纪录都要被插到现有索引页得中间某个位置==

**==此时MySQL不得不为了将新记录插到合适位置而移动数据==，甚至目标页面可能已经被回写到磁盘上而从缓存中清掉，此时又要从磁盘上读回来，这增加了很多开销，==同时频繁的移动、分页操作造成了大量的碎片==**，得到了不够紧凑的索引结构，后续不得不通过OPTIMIZE TABLE来重建表并优化填充页面。

在使用InnoDB存储引擎时，如果没有特别的需要，请永远使用一个与业务无关的自增字段作为主键。

**mysql 在频繁的更新、删除操作，会产生碎片。而含碎片比较大的表，查询效率会降低。此时需对表进行优化，这样才会使查询变得更有效率。**

