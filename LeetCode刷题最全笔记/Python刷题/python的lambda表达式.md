---
title: Python lambda表达式
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2020-03-09 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/58.jpg
tags:
  - Python
categories:
  - Python刷题
music:
 server: netease   # netease, tencent, kugou, xiami, baidu
 type: song        # song, playlist, album, search, artist
 id: 16846091      # song id / playlist id / album id / search keyword

---

**filter函数** 此时lambda函数用于指定过滤列表元素的条件。例如filter(lambda x: x % 3 == 0, [1, 2, 3])指定将列表[1,2,3]中能够被3整除的元素过滤出来，其结果是[3]。

```python
def is_odd(n):
    return n%2==1
 
newlist = filter(is_odd,[1,2,3,4,5,6,7,8,9,10])
print(newlist)
# Python3.6结果：<filter object at 0x00000184ED881358>
# Python2.x结果：[1, 3, 5, 7, 9]
 
# Python3.6返回的是迭代器对象，可以转换成list
print(list(newlist))
# [1, 3, 5, 7, 9]
 
# -------------------------------------------
 
# 以上函数可以用lambda表达式书写
newlist = list(filter(lambda n:n%2==1,[1,2,3,4,5,6,7,8,9,10]))
print(newlist)
# [1, 3, 5, 7, 9]
 
# -------------------------------------------
 
# 在对象遍历处理方面，其实Python的for..in..if语法已经很强大，并且在易读上胜过了lambda。
# 以上函数还可以写成如下：
newlist = list(x for x in [1,2,3,4,5,6,7,8,9,10] if x%2==1)
print(newlist)
# [1, 3, 5, 7, 9]
 
```

**sorted函数** 此时lambda函数用于指定对列表中所有元素进行排序的准则。例如sorted([1, 2, 3, 4, 5, 6, 7, 8, 9], key=lambda x: abs(5-x))将列表[1, 2, 3, 4, 5, 6, 7, 8, 9]按照元素与5距离从小到大进行排序，其结果是[5, 4, 6, 3, 7, 2, 8, 1, 9]。

```python
# lambda 表达式只有一行代码，并返回该行代码的结果
a = [
    {'name': 'Bill', 'age': '40'},
    {'name': 'Mike', 'age': '18'},
    {'name': 'Johb', 'age': '28'}
]

print(a)

print(sorted(a, key=lambda x: x['age']))

a.sort(key=lambda x: x['age'], reverse=True)
print(a)

print(sorted(a, key=lambda x: x['name']))

# 换成sorted也是可以的


```

**map函数** 此时lambda函数用于指定对列表中每一个元素的共同操作。例如map(lambda x: x+1, [1, 2,3])将列表[1, 2, 3]中的元素分别加1，其结果[2, 3, 4]。

```python
>>>def square(x) :            # 计算平方数
...     return x ** 2
... 
>>> map(square, [1,2,3,4,5])   # 计算列表各个元素的平方
[1, 4, 9, 16, 25]
>>> map(lambda x: x ** 2, [1, 2, 3, 4, 5])  # 使用 lambda 匿名函数
[1, 4, 9, 16, 25]
 
# 提供了两个列表，对相同位置的列表数据进行相加
>>> map(lambda x, y: x + y, [1, 3, 5, 7, 9], [2, 4, 6, 8, 10])
[3, 7, 11, 15, 19]

# 同理也可以使用三个列表
>>>s = [1,2,3]
>>>list(map(lambda x,y,z:x*y*z ,s,s,s))
>>>[1,8,27]






def multi(x):
    return x * 2

def multi2(x, y):
    return x * y

#参数只有1个序列时
list1 = map(multi,[1,2,3,4,5])
print(list(list1)) #输出：[2, 4, 6, 8, 10]

#用lambda改写上面语句
list1_1 = map(lambda x : x*2, [1,2,3,4,5])
print(list(list1_1)) #输出：[2, 4, 6, 8, 10]

#参数有2个序列时，
list2 = map(multi2,[1,2,3,4,5],[6,7,8,9,10])
print(list(list2)) #对2个列表数据的相同位置元素相乘，输出：[6, 14, 24, 36, 50]

#用lambda改写上面语句
list2_1 = map(lambda x,y : x*y, [1,2,3,4,5],[6,7,8,9,10])
print(list(list2_1)) #输出：[6, 14, 24, 36, 50]

#当2个序列长度不一致时，结果以2个序列中的最短长度为准
list2_2 = map(lambda x,y : x*y, [1,2,3],[6,7,8,9,10])
print(list(list2_2)) #输出：[6, 14, 24]
list2_3 = map(lambda x,y : x*y, [1,2,3,4,5],[6,7,8])
print(list(list2_3)) #输出：[6, 14, 24]
```

**reduce函数** 此时lambda函数用于指定列表中两两相邻元素的结合条件。例如reduce(lambda a, b: '{}, {}'.format(a, b), [1, 2, 3, 4, 5, 6, 7, 8, 9])将列表 [1, 2, 3, 4, 5, 6, 7, 8, 9]中的元素从左往右两两以逗号分隔的字符的形式依次结合起来，其结果是'1, 2, 3, 4, 5, 6, 7, 8, 9'。

reduce()函数对一个数据集合的所有数据进行操作：用传给 reduce 中的函数 function（必须有两个参数）先对集合中的第 1、2 个元素进行操作，得到的结果再与第三个数据用 function 函数运算，最后得到一个结果。
在Python2中reduce()是内置函数，Pytho3移到functools 模块。

**语法：**

```
reduce(function, iterable[, initializer])
```

function -- 函数，有两个参数
iterable -- 可迭代对象
initializer -- 可选，初始参数

**使用例子：**

```python
from functools import reduce

def add(x, y):
    return x + y
def mulit(x, y):
    return x * y

print(reduce(add, [1, 2, 3, 4, 5])) #输出：15
print(reduce(add, [1, 2, 3, 4, 5], 10)) #输出：25

print(reduce(mulit, [1, 2, 3, 4, 5])) #输出：120
print(reduce(mulit, [1, 2, 3, 4, 5], 10)) #输出：1200

print(reduce(lambda x,y:x+y,[1, 2, 3, 4, 5]))#输出：15
print(reduce(lambda x,y:x+y,[1, 2, 3, 4, 5], 10))#输出：25
```

