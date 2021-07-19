---
title: java数组转List
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2021-01-02 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/42.jpg
tags:
  - Java
  - Java基础
categories:
  - 语言基础
music:
 server: netease   # netease, tencent, kugou, xiami, baidu
 type: song        # song, playlist, album, search, artist
 id: 16846091      # song id / playlist id / album id / search keyword


---

# Java数组转List的三种方式及对比

![image-20210301210705628](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210301210705628.png)

##### 一：最常见方式（未必最佳）

通过Arrays.asList(strArray)方式，将列表转换为列表后，不能对列表增删，只能查改，否则抛异常。
 关键代码：List list = Arrays.asList(strArray);



```cpp
private void testArrayCastToListError() {
  String[] strArray = new String[2];
  List list = Arrays.asList(strArray);
  //对转换后的list插入一条数据
  list.add("1");
  System.out.println(list);
 }
```

原因解析：
 Arrays.asList(strArray)返回值是java.util.Arrays类中一个私有静态内部类java.util.Arrays.ArrayList，它并非java.util.ArrayList类。java.util.Arrays.ArrayList类具有set（），get（），contains（）等方法，但是不具有添加add()或删除remove()方法，所以调用add()方法会报错。

使用场景：Arrays.asList(strArray)方式仅能用在将转换转换为列表后，不需要增加删除其中的值，仅作为数据源读取使用。

##### 二：副本转为List后，支持增删改查的方式

通过ArrayList的构造器，将Arrays.asList(strArray)的返回值由java.util.Arrays.ArrayList转为java.util.ArrayList。
 关键代码：ArrayList<String> list = new ArrayList<String>(Arrays.asList(strArray)) ;



```cpp
private void testArrayCastToListRight() {
  String[] strArray = new String[2];
  ArrayList<String> list = new ArrayList<String>(Arrays.asList(strArray)) ;
  list.add("1");
  System.out.println(list);
 }
```

使用场景：需要在将转换为列表后，对列表进行增删改查操作，在列表的数据量不大的情况下，可以使用。

##### 三：通过集合工具类Collections.addAll（）方法（最高效）

通过Collections.addAll(arrayList, strArray)方式转换，根据副本的长度创建一个长度相同的列表，然后通过Collections.addAll()方法，将数组中的元素转换为二进制，然后添加到列表中，这是最高效的方法。
 关键代码：
 ArrayList< String> arrayList = new ArrayList<String>(strArray.length);
 Collections.addAll(arrayList, strArray);



```tsx
private void testArrayCastToListEfficient(){
  String[] strArray = new String[2];
  ArrayList< String> arrayList = new ArrayList<String>(strArray.length);
  Collections.addAll(arrayList, strArray);
  arrayList.add("1");
  System.out.println(arrayList);
 }
```

使用场景：需要在将转换为列表后，对列表进行增删改查操作，在列表的数据量巨大的情况下，优先使用，可以提高操作速度。



