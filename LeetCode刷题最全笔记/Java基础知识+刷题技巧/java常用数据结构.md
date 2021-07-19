---
title: java常用数据结构
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2021-01-01 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/41.jpg
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

- - ArrayList是顺序结构，所以定位很快，但插入，删除数据慢。
- - LinkedList 是链表结构，定位慢，但插入，删除数据快。

ArrayList实现了List接口，常见方法有：

- add(); contains(); get(); indexOf():定位对象所处的位置; remove(); size(); toArray(); toString();//转换为字符串

LinkedList也实现了List接口外，可以实现上述ArrayList中的常用方法，此外：

LinkedList还实现了双向链表结构Deque，可以很方便的在头尾插入删除数据。

- LinkedList<class> link = new LinkedList<>();
    常用方法：addFirst(); addLast(); getFirst(); getLast(); removeFirst(); removeLast();

LinkedList除了实现了List和Deque外，还实现了Queue接口(队列),Queue是先进先出队列 FIFO。

- Queue<class> queue = new LinkedList<>();
  常用方法：poll()取出第一个元素; peek()查看第一个元素; offer()在最后添加元素,可用add()替换;

先进后出FILO Stack栈：

- Stack<class> stack = new Stack<>();
  常用方法：push();可用add();代替 pop();输出末尾的元素相当于LinkedList中的removeLast(); peek();查看最后一个元素，相当于getLast();



### 剑指offer 59

```java
public class MaxQueue {

    Queue<Integer> queue;
    LinkedList<Integer> max;
    public MaxQueue() {
        queue = new LinkedList<>();
        max = new LinkedList<>();//LinkedList是双端链表
    }
    
    public int max_value() {
        return max.size()==0?-1:max.getFirst();
    }
    
    public void push_back(int value) {
        queue.add(value);
        while(max.size()!=0&&max.getLast()<value){//注意：这里第二个判断条件不能带等号，即max中对于当前queue中的具有相同值的元素会全部存储，而不是存储最近的那个。
            max.removeLast();
        }
        max.add(value);
    }
    
    public int pop_front() {
        if(max.size()!=0&&queue.peek().equals(max.getFirst()))//Integer类型的值的比较不能直接使用==
            max.removeFirst();
        return queue.size()==0?-1:queue.poll();
    }
    
    /**
     * Your MaxQueue object will be instantiated and called as such:
     * MaxQueue obj = new MaxQueue();
     * int param_1 = obj.max_value();
     * obj.push_back(value);
     * int param_3 = obj.pop_front();
     */
}


```



**Java 中 int 和 Integer 的区别**

**1.** int 是基本数据类型，int 变量存储的是数值。Integer 是引用类型，实际是一个对象，Integer 存储的是引用对象的地址。

**2.**

```
Integer i = new Integer(100);
Integer j = new Integer(100);
System.out.print(i == j); //false
```

因为 new 生成的是两个对象，其内存地址不同。

**3.**

int 和 Integer 所占内存比较：

Integer 对象会占用更多的内存。Integer 是一个对象，需要存储对象的元数据。但是 int 是一个原始类型的数据，所以占用的空间更少。

**4.** 非 new 生成的 Integer 变量与 **new Integer()** 生成的变量比较，结果为 false。

```
/**
 * 比较非new生成的Integer变量与new生成的Integer变量
 */
public class Test {
    public static void main(String[] args) {
        Integer i= new Integer(200);
        Integer j = 200;
        System.out.print(i == j);
        //输出：false
    }
}
```

因为非 new 生成的 Integer 变量指向的是 java 常量池中的对象，而 **new Integer()** 生成的变量指向堆中新建的对象，两者在内存中的地址不同。所以输出为 false。

**5.** 两个非 new 生成的 Integer 对象进行比较，如果两个变量的值在区间 **[-128,127]** 之间，比较结果为 true；否则，结果为 false。

```
/**
 * 比较两个非new生成的Integer变量
 */
public class Test {
    public static void main(String[] args) {
        Integer i1 = 127;
        Integer ji = 127;
        System.out.println(i1 == ji);//输出：true
        Integer i2 = 128;
        Integer j2 = 128;
        System.out.println(i2 == j2);//输出：false
    }
}
```

java 在编译 **Integer i1 = 127** 时，会翻译成 **Integer i1 = Integer.valueOf(127)**。

**6.** Integer 变量(无论是否是 new 生成的)与 int 变量比较，只要两个变量的值是相等的，结果都为 true。

```
/**
 * 比较Integer变量与int变量
 */
public class Test {
    public static void main(String[] args) {
        Integer i1 = 200;
        Integer i2 = new Integer(200);
        int j = 200;
        System.out.println(i1 == j);//输出：true
        System.out.println(i2 == j);//输出：true
    }
}
```

包装类 Integer 变量在与基本数据类型 int 变量比较时，Integer 会自动拆包装为 int，然后进行比较，实际上就是两个 int 变量进行比较，值相等，所以为 true。