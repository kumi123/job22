---
title: deque用法
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2020-02-23 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/40.jpg
tags:
  - LeetCode刷题
  - 算法
categories:
  - Leetcode
music:
 server: netease   # netease, tencent, kugou, xiami, baidu
 type: song        # song, playlist, album, search, artist
 id: 16846091      # song id / playlist id / album id / search keyword


---

```python
from collections import deque

dlist=deque([1,'a'])
dlist.append('b') # 在末尾加数据
dlist.appendleft(0) # 在最前端插入数据
print(dlist)
# 输出 :  deque([0, 1, 'a', 'b'])

dlist.pop() # 删除末尾的数据
dlist.popleft() # 删除最前端的数据
print(dlist)
# 输出 :  deque([1, 'a'])

dlist.extend(['b','c']) # 在末尾追加list 数据
dlist.extendleft([-1,0])# 在前端插入list 数据
print(dlist)
# 输出 : deque([0, -1, 1, 'a', 'b', 'c'])

print(dlist.index('a')) # 找出 a 的索引位置
# 输出 :  3

dlist.insert(2, 555) # 在索引2 的位置插入555
print(dlist)
# 输出 :  deque([0, -1, 555, 1, 'a', 'b', 'c'])

print(dlist.count('a')) # 查找 ‘a’ 的数量

dlist.remove(1) # 删除第一个匹配值
dlist.reverse()  # 反向
print(dlist)
# 输出 :  deque(['c', 'b', 'a', 555, -1, 0])


dlist.rotate(-2) # 将左端的元素移动到右端
print(dlist)
# 输出 :  deque(['a', 555, -1, 0, 'c', 'b'])

dlist.rotate(2) # 将右端的元素移动到左端
print(dlist)
# 输出 :  deque(['c', 'b', 'a', 555, -1, 0])

dl1=dlist # 赋值 dlist 值变化，dl1的值也会修改
dl2=dlist.copy() # 拷贝 dlist, 拷贝后对dl修改不影响dlist的值
dlist.pop() # 删除最后一个数据, dl1的值也被修改
print(dl1) # 输出： deque(['c', 'b', 'a', 555, -1])
print(dl2) # 输出： deque(['c', 'b', 'a', 555, -1, 0])
```

#### 因为是双端队列