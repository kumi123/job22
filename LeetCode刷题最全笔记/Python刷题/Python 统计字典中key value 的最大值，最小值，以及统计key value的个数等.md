
---
title: Python 统计字典中key value 的最大值
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2020-03-08 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/57.jpg
tags:
  - JVM
categories:
  - Java虚拟机
music:
 server: netease   # netease, tencent, kugou, xiami, baidu
 type: song        # song, playlist, album, search, artist
 id: 16846091      # song id / playlist id / album id / search keyword

---
# Python 统计字典中key value 的最大值，最小值，以及统计key value的个数等



#### dic.get来统计一个数组当中出现的次数

```python
class Solution:
    def singleNumbers(self, nums: List[int]) -> List[int]:
        #第一种使用这个哈希表
        resed=[]
        dic={}
        for num in nums:
            dic[num]=dic.get(num,0)+1
        for res in set(nums):
            if dic[res]==1:
                resed.append(res)
        return resed
```



```java
class Solution:
    def singleNumbers(self, nums: List[int]) -> List[int]:
        resA,resB=0,0
        xor,mask=0,1
        for num in nums:
            xor^=num
        while xor&mask==0:
            mask=mask<<1
        for num in nums:
            if num&mask:
                resA^=num
            else:
                resB^=num
        return [resA,resB]

        
        
```



#### 获取最大值

键一般是唯一的，如果重复最后的一个键值对会替换前面的，值不需要唯一。

```python
dict = {'a': 1, 'b': 2, 'b': '3'}
print(dict['b'])
print(dict)
```

![img](https://img-blog.csdnimg.cn/2019082415054840.png)

后面的会替换前面的：如上结果：'b': 2,被 'b': '3'替换，因为key一样

 

2.字典里统计key value 的最大值，最小值

统计value的最大值：（最小值同理）

```python
dict = {1: 1, 2: 8, 3: 3,4:7}
#value的最大值
m=max(dict.keys(),key=(lambda x:dict[x]))
#输出最大value对应的key
print(m)
#输出最大value
print(dict[m])
```

![img](https://img-blog.csdnimg.cn/20190824151510127.png)

统计key的最大值：（最小值同理）

```python
dict = {1: 1, 2: 8, 3: 3,4:7}
#key的最大值
m=max(dict.keys(),key=(lambda x:x))
#输出最大key
print(m)
#输出最大key对应的value
print(dict[m])
```

![img](https://img-blog.csdnimg.cn/20190824151644722.png)

3.sorted max min 等和 lambad函数连用 :(max min)上面已经说了：

主要说一下sorted ：

```python
dict = {1: 1, 2: 8, 3: 3,4:7}
m=sorted(dict.keys(),key=(lambda x:x))
print("按key升序：",m)

m=sorted(dict.values(),key=(lambda x:x))
print("按value升序：",m)

降序：reverse=True
m=sorted(dict.keys(),key=(lambda x:x),reverse=True)
print("按key降序：",m)
```

![img](https://img-blog.csdnimg.cn/2019082415270260.png)

4.

```python
获取key:dic.keys()   获取value:dic.values()
dict = {'剧情': 4, '犯罪': 3, '动作': 2, '爱情': 3, '喜剧': 2}
keys = list(dict.keys())
values = list(dict.values())
print(keys)
print(values)
```

![img](https://img-blog.csdnimg.cn/20190824153505517.png)