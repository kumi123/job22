
---
title: 解决缓存穿透、缓存击穿、缓存雪崩
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2020-03-03 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/54.jpg
tags:
  - Redis
categories:
  - Redis
music:
 server: netease   # netease, tencent, kugou, xiami, baidu
 type: song        # song, playlist, album, search, artist
 id: 16846091      # song id / playlist id / album id / search keyword


---

# 原来大厂都这么解决Redis缓存雪崩、缓存穿透、缓存击穿

# 1 缓存雪崩

## 1.1 什么是缓存雪崩?

由于

- 设置缓存时，key都采用了相同expire
- 更新策略
- 数据热点
- 缓存服务宕机

等原因，可能导致缓存数据同一时刻大规模不可用，或者都更新。

## 1.2 解决方案

- 更新策略在时间上做到比较均匀
- 使用的热数据尽量分散到不同的机器上
- 多台机器做主从复制或者多副本，实现高可用
- 实现熔断限流机制，对系统进行负载能力控制

在原有失效时间基础上增加一个随机值，比如1~5分钟的随机，这样每个缓存的过期时间重复率就会降低，集体失效概率也会大大降低。

# 2 缓存穿透

## 2.1 什么是缓存穿透？

大量并发查询不存在的KEY，导致都直接将压力透传到数据库。

为什么会多次透传呢？不存在一直为空。 需要注意让缓存能够区分KEY不存在和查询到一个空值。

例如：访问**id=-1**的数据。可能出现绕过Redis依然频繁访问数据库，称为缓存穿透，多出现在查询为null的情况不被缓存时。

## 2.2 解决方案

- 缓存空值的KEY，这样第一次不存在也会被加载会记录，下次拿到有这个KEY
- Bloom过滤或RoaringBitmap 判断KEY是否存在

最常见的布隆过滤器，将所有可能存在的数据哈希到一个足够大的bitmap中，一个一定不存在的数据会被这个bitmap拦截掉，从而避免了对底层存储系统的查询压力。

- 完全以缓存为准，使用 延迟异步加载 的策略2，这样就不会触发更新。

更为简单粗暴的方法，如果一个查询返回的数据为空（不管是数据不存在，还是系统故障），仍然把这个空结果进行缓存，但它的过期时间会很短，最长不超过5min。

```java
if(list==null){
    // key value 有效时间 时间单位
    redisTemplate.opsForValue().set(navKey,null,10, TimeUnit.MINUTES);
}else{
    redisTemplate.opsForValue().set(navKey,result,7,TimeUnit.DAYS);
}
复制代码
```

# 3 缓存击穿

击穿是针对某一key缓存，而雪崩是很多key。

某KEY失效时，正好有大量并发请求访问该KEY。

通常使用缓存 + 过期时间的策略来帮助我们加速接口的访问速度，减少了后端负载，同时保证功能的更新，一般情况下这种模式已经基本满足要求了。

但如下问题若同时出现，可能对系统致命：

- 为热点key，访问量非常大
- 缓存的构建是需要时间（可能是个复杂过程，例如复杂SQL、多次I/O、多个接口依赖）

于是就会导致： 在缓存失效瞬间，有大量线程构建缓存，导致后端负载加剧，甚至可能让系统崩溃。

![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/b360c5a1efc14dfbb4e6a475def1d825~tplv-k3u1fbpfcp-zoom-1.image)

## 3.2 解决方案

所以问题就在于限制处理线程的数量，即KEY的更新操作添加全局互斥锁。

### 互斥锁

在缓存失效时（判断拿出来的值为空），不是立即去load db，而是

- 先使用缓存工具的某些带成功操作返回值的操作（Redis的SETNX）去set一个mutex key
- 当操作返回成功时，再load db的操作并回设缓存；否则，就重试整个get缓存的方法。

```java
public String get(key) {
      String value = redis.get(key);
      if (value == null) { // 缓存已过期
          // 设置超时，防止del失败时，下次缓存过期一直不能load db
		  if (redis.setnx(key_mutex, 1, 3 * 60) == 1) { // 设置成功
               value = db.get(key);
                      redis.set(key, value, expire_secs);
                      redis.del(key_mutex);
          } else {
            		// 其他线程已load db并回设缓存，重试获取缓存即可
                    sleep(50);
                    get(key);  //重试
          }
        } else { // 缓存未过期
            return value;      
        }
 }
复制代码
```

### 提前"使用互斥锁(mutex key)：

在value内部设置1个超时值(timeout1)， timeout1比实际的memcache timeout(timeout2)小。当从cache读取到timeout1发现它已经过期时候，马上延长timeout1并重新设置到cache。然后再从数据库加载数据并设置到cache中。伪代码如下：

```java
v = memcache.get(key);  
if (v == null) {  
    if (memcache.add(key_mutex, 3 * 60 * 1000) == true) {  
        value = db.get(key);  
        memcache.set(key, value);  
        memcache.delete(key_mutex);  
    } else {  
        sleep(50);  
        retry();  
    }  
} else {  
    if (v.timeout <= now()) {  
        if (memcache.add(key_mutex, 3 * 60 * 1000) == true) {  
            // extend the timeout for other threads  
            v.timeout += 3 * 60 * 1000;  
            memcache.set(key, v, KEY_TIMEOUT * 2);  
  
            // load the latest value from db  
            v = db.get(key);  
            v.timeout = KEY_TIMEOUT;  
            memcache.set(key, value, KEY_TIMEOUT * 2);  
            memcache.delete(key_mutex);  
        } else {  
            sleep(50);  
            retry();  
        }  
    }  
} 
复制代码
```

### 缓存为准

使用异步线程负责维护缓存的数据，定期或根据条件触发更新，这样就不会触发更新。

### 限流

如使用 hystrix 或者 sentinel。