# [面试官：Redis过期key是怎么清理的？](https://segmentfault.com/a/1190000023098431)

redis 的 key 清理，也就是内存回收的时候主要分为：过期删除策略与 内存淘汰策略两部分。

## 过期删除策略

删除到达过期时间的 key 。

### 第一种：==定时检查删除==

对于每一个==设置了过期时间的 key 都会创建一个定时器，一旦达到过期时间都会删除。==这种方式==立即清除过期数据，对内存比较好==。

但是有缺点是：==占用了大量 CPU 的资源去处理过期数据，会影响 redis 的吞吐量 和 响应时间。==

### 第二种：==惰性检查删除==

当==访问一个 key 的时候，才会判断该 key 是否过期，如果过期就删除。该方式能最大限度节省 CPU 的资源。==

但是==对内存不太好，有一种比较极端的情况：出现大量的过期 key 没有被再次访问，因为不会被清除，导致占用了大量的内存。==

### 第三种：==定期检查删除==

每隔一段时间，扫描redis 中设置了过期时间key 的字典，==并清除部分过期的key==。这种方式是前俩种一种折中方法。不同的情况下，调整定时扫描时间间隔，让CPU 与 内存达到最优。

## 内存淘汰策略

redis ==内存淘汰策略是指达到maxmemory极限时，使用某种算法来决定来清理哪些数据，以保证新数据存入==。

### 第一类 ==不处理，等报错==(默认的配置)

- ==noeviction，发现内存不够时，不删除key，执行写入命令时直接返回错误信息。==（Redis默认的配置就是noeviction）

### 第二类 从==所有结果集中的key中挑选，进行淘汰==

- ==allkeys-random 就是从所有的key中随机挑选key，进行淘汰==
- ==allkeys-lru 就是从所有的key中挑选最近使用时间距离现在最远的key，进行淘汰==
- ==allkeys-lfu 就是从所有的key中挑选使用频率最低的key，进行淘汰。==（这是Redis 4.0版本后新增的策略）

### 第三类 ==从设置了过期时间的key中挑选，进行淘汰==

  这种就是从设置了expires过期时间的结果集中选出一部分key淘汰，挑选的算法有：

- ==volatile-random 从设置了过期时间的结果集中随机挑选key删除。==
- ==volatile-lru 从设置了过期时间的结果集中挑选上次使用时间距离现在最久的key开始删除==
- ==volatile-ttl 从设置了过期时间的结果集中挑选可存活时间最短的key开始删除(也就是从哪些快要过期的key中先删除)==
- ==volatile-lfu 从过期时间的结果集中选择使用频率最低的key开始删除（这是Redis 4.0版本后新增的策略）==