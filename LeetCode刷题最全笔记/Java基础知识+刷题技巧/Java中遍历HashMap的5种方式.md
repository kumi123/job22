---
title: Java遍历Hashmap
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2021-01-07 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/46.jpg
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
# Java中遍历HashMap的5种方式

### 1、 通过ForEach循环进行遍历

```java
mport java.io.IOException;

import java.util.HashMap;

import java.util.Map;

public class Test {

	public static void main(String[] args) throws IOException {

		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		map.put(1, 10);

		map.put(2, 20);


		// Iterating entries using a For Each loop

		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
	System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

		}

	}

}
```

### 2、 ForEach迭代键值对方式

如果你只想使用键或者值，推荐使用如下方式

```java
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class Test {
	public static void main(String[] args) throws IOException {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(1, 10);
		map.put(2, 20);

		// 迭代键

		for (Integer key : map.keySet()) {
			System.out.println("Key = " + key);

		}

		// 迭代值

		for (Integer value : map.values()) {
		System.out.println("Value = " + value);
		}

	}

}
```

### 3、使用带泛型的迭代器进行遍历

```java
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Map;
public class Test {
	public static void main(String[] args) throws IOException {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(1, 10);

		map.put(2, 20);
		Iterator<Map.Entry<Integer, Integer>> entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<Integer, Integer> entry = entries.next();
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}

}
```

### 4、使用不带泛型的迭代器进行遍历

```java
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class Test {
	public static void main(String[] args) throws IOException {
		Map map = new HashMap();
		map.put(1, 10);
    	map.put(2, 20);

		Iterator<Map.Entry> entries = map.entrySet().iterator();

		while (entries.hasNext()) {
	Map.Entry entry = (Map.Entry) entries.next();

			Integer key = (Integer) entry.getKey();
		Integer value = (Integer) entry.getValue();

			System.out.println("Key = " + key + ", Value = " + value);

		}

	}

}
```

### 5、通过Java8 Lambda表达式遍历

```java
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class Test {
	public static void main(String[] args) throws IOException {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(1, 10);
		map.put(2, 20);

		map.forEach((k, v) -> System.out.println("key: " + k + " value:" + v));

	}

}
```

 

输出

 

```groovy
key: 1 value:10
key: 2 value:20
```