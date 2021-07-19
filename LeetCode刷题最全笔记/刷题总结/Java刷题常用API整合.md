# [Java刷题常用API整合](https://my.oschina.net/u/4332589/blog/4678259)

#### 零：输入、输出

远程在线面试的手撕代码环节，通常需要白板上写代码。
如果需要在控制台输入、输出，需要借助Scanner类。
示例如下：



```java
import java.util.Scanner; 
public class Solution {
   
   
    public static void main(String[] args) {
   
   
		Scanner scanner = new Scanner(System.in);
		//方法1：一般用来接收数组。以空格分界
		while(scanner.hasNext()){
   
   
			int num = scanner.nextInt();
			/*或者*/String next = scanner.next(); 
		}
		//方法2：一般用来接受字符串。以换行符分界
		while (scanner.hasNextLine()) {
   
   
            String str = scanner.nextLine();
        }
    }
```



#### 一：Integer 类

1.String -> int：`int value = Integer.parseInt(str);`
2.将str转换成二进制的int： `int binary = Integer.parseInt(str,2);`
3.十进制转二进制：`String binaryN=Integer.toBinaryString(N);`





#### 二：String 类

1.Object -> String：`String str = String.valueOf(o);`
 char[ ] -> String：`String str = String.valueOf(charArray);`
  int -> String：`String str = String.valueOf(N);`





#### 三：对StringObject的方法



##### 1.String转int、long等

```
public void test() {
    String sInt = "123";
    int s = Integer.parseInt(sInt);
    long ls = Long.parseLong(sInt);
    System.out.println(s + " " + ls);
}123456
```



##### 2.StringBuffer

> StringBuffer类和String一样，也用来代表字符串，只是由于StringBuffer的内部实现方式和String不同，所以StringBuffer在进行字符串处理时，不生成新的对象，在内存使用上要优于String类。 所以在实际使用时，如果经常需要对一个字符串进行修改，例如插入、删除等操作，使用StringBuffer要更加适合一些。 在StringBuffer类中存在很多和String类一样的方法，这些方法在功能上和String类中的功能是完全一样的。 **但是有一个最显著的区别在于，对于StringBuffer对象的每次修改都会改变对象自身，这点是和String类最大的区别**。 另外由于**StringBuffer是线程安全的**，所以在多线程程序中也可以很方便的进行使用，但是程序的执行效率相对来说就要稍微慢一些。

 2.1 String 转 StringBuffer

```
StringBuffer s = new StringBuffer("abc");1
```

 2.2 append() 方法，将内容追加到StringBuffer末尾

```
StringBuffer s = new StringBuffer("abc");
s.append("efg");
System.out.println(s.toString());123
```

 2.3 deleteCharAt(int index) 方法，作用是删除指定位置的字符，然后将剩余的内容形成新的字符串，第一位为0。
​ s.delete(int start,int end) 该方法的作用是删除指定区间以内的所有字符，包含start，不包含end索引值的区间。

```
StringBuffer s = new StringBuffer("abc");
s.append("efg");
s.deleteCharAt(2);
s.delete(0,2);1234
```

 2.4 insert(int offset, String str) 方法，作用是在StringBuffer对象中插入内容，然后形成新的字符串。例如：

```
StringBuffer sb = new StringBuffer("TestString");
sb.insert(4,"hello");
System.out.println(sb.toString());123
```

 2.5 reverse() 方法，作用是将StringBuffer对象中的内容反转，然后形成新的字符串。例如：

```
StringBuffer sb = new StringBuffer("TestString");
sb.reverse();
System.out.println(sb.toString());  // ---> gnirtStseT123
```

2.6 setCharAt(int index, char ch) 方法，作用是修改对象中索引值为index位置的字符为新的字符ch。例如：

```
StringBuffer sb = new StringBuffer("bc");
sb.setCharAt(1,'D');  // ---> sb = ”aDc”12
```



#### 四：对MapObject的方法

1.map中是否包含key：`boolean isContain = map.containsKey(key);`
2.map的get()：`Object value = map.get(key);`
 map的getOrDefault()： `map.getOrDefault(key,default);//没找到就return default`
3.map的put()：`map.put(key,value);`
4.map的遍历：`for(Object key : map.keySet()){ //... }`







#### 五：求长度的总结

1.数组：用`arr.length` length 是数组的一个属性。
2.字符串：用`str.length()` length() 是 StringObject 的一个方法。
3.集合：用`list.size()` size()是list的一个方法。





#### 六：边界处理

1.数组判空：`if(arr == null|| arr.length == 0) ...`
 二维数组判空：`if(arr == null || arr.length == 0 || arr[0].length == 0) ...`
2.字符串判空：`if(str == null || str.equals("")) ...`





#### 七：各种数值类型最大值和最小值

```
fmax = Float.MAX_VALUE;
fmin = Float.MIN_VALUE;
dmax = Double.MAX_VALUE;
dmin = Double.MIN_VALUE;
bmax = Byte.MAX_VALUE;
bmin = Byte.MIN_VALUE;
cmax = Character.MAX_VALUE;
cmin = Character.MIN_VALUE;
shmax = Short.MAX_VALUE;
shmin = Short.MIN_VALUE;
imax = Integer.MAX_VALUE;
imin = Integer.MIN_VALUE;
lmax = Long.MAX_VALUE;
lmin = Long.MIN_VALUE;1234567891011121314
```



#### 八：数组



##### 1.排序

Array.Sort() 快排

```
ublic void test() {
    int[] arrayToSort = new int[] { 48, 5, 89, 80, 81, 23, 45, 16, 2 };
    System.out.println("排序前");
    for (int i = 0; i < arrayToSort.length; i++)
        System.out.println(arrayToSort[i]);
    // 调用数组的静态排序方法sort
    Arrays.sort(arrayToSort);
    System.out.println("排序后");
    for (int i = 0; i < arrayToSort.length; i++)
        System.out.println(arrayToSort[i]);
}1234567891011
```



##### 2.数组与list转换

 2.1 数组转list，调用Arrays.asList()方法：

```
public static <T> List<T> asList(T... a) {
     return new ArrayList<>(a);
}
String[] strings = str.split(" ");
List<String> list = Arrays.asList(strings);12345
```

 2.2 list转数组

```
List list = new ArrayList();
list.add("1");
list.add("2");
final int size =  list.size();
String[] arr = (String[])list.toArray(new String[size]);12345
```



#### 九：数字



##### 1.比大小

> Math.max(int a, int b); Math.max(float a, float b); Math.min(int a, int b);

```
public void testMain() throws Exception {
        int a = 100;
        int b = 200;

        System.out.println(Math.max(a,b));
        System.out.println(Math.min(a,b));
}1234567
```



##### 2.Hash

HashMap put返回值

> leetcode 中 205. Isomorphic Strings这道题，在 discuss 中，有用到 map.put(key, value)的返回值，查看源码可以发现其返回值为之前存进去key对应的value。
> jdk源码注释 @return the previous value associated with key, ornull if there was no mapping forkey. (A null return can also indicate that the map previously associated null with key,if the implementation supports null values.)

```
public void test() {
    Map<Character,Integer> m = new HashMap<>();
    System.out.println(m.put('a',2));
    System.out.println(m.put('b',3));
    System.out.println(m.put('a',1));
    System.out.println(m.put('a',5));
}

// ---> null null 2 1123456789
```



#### 十：常用的现成数据结构类



##### 1.线性表（List接口，有序的 collection（也称为序列）。此接口的用户可以对列表中每个元素的插入位置进行精确地控制。用户可以根据元素的整数索引（在列表中的位置）访问元素，并搜索列表中的元素。与 set 不同，列表通常允许重复的元素。）

> - Stack，堆栈
> - Vector, 动态数组
> - ArrayList, 实现了List接口的动态数组
> - LinkedList, List 接口的链接列表实现，包含队列、双端队列的API，同时实现Queue接口

Collections的reverse方法()、sort()方法

```
// reverse(list) 反转
ArrayList<Integer> list = new ArrayList<Integer>();
list.add(3);
list.add(1);
list.add(2);
Collections.reverse(list);   // 使用Collections的reverse方法，直接将list反转
// ---> list = {2, 1, 3}

// sort() 升序排序
Collections.sort(list);   // ---> list = {1, 2, 3}

// 升序在反转就相当于降序123456789101112
```



##### 2.映射(Map接口，将键映射到值的对象。一个映射不能包含重复的键；每个键最多只能映射到一个值)

> - Hashtable，此类实现一个哈希表，该哈希表将键映射到相应的值。任何非 null 对象都可以用作键或值。
> - HashMap,基于哈希表的 Map 接口的实现。此实现提供所有可选的映射操作，并允许使用 null 值和 null 键。（除了非同步和允许使用 null 之外，HashMap 类与 Hashtable 大致相同。）
> - LinkedHashMap，Map 接口的哈希表和链接列表实现，具有可预知的迭代顺序，为插入顺序。
> - TreeMap，基于红黑树，该映射根据其键的自然顺序进行排序，或者根据创建映射时提供的 Comparator 进行排序，具体取决于使用的构造方法。



##### 3.集合(Set接口是一个不包含重复元素的集合)

> - HashSet，此类实现 Set 接口，由哈希表支持，元素存储迭代没有固定顺序
> - LinkedHashSet，具有可预知迭代顺序的 Set 接口的哈希表和链接列表实现，顺序为插入顺序
> - TreeSet，元素是内部排序的，使用元素的自然顺序对元素进行排序，或者根据创建 set 时提供的 Comparator 进行排序，具体取决于使用的构造方法。



##### 4.优先队列

> - PriorityQueue,一个基于优先级堆的无界优先级队列。优先级队列的元素按照其自然顺序进行排序，或者根据构造队列时提供的 Comparator 进行排序。元素小的优先级高，输出时先输出。实现Queue接口



##### 5.其他常用的类，直接使用类中的静态方法

> - 类Arrays，此类包含用来操作数组（比如排序和搜索）的各种方法。
> - Math类，Math 类包含用于执行基本数学运算的方法，如初等指数、对数、平方根和三角函数。
> - Boolean,Byte,Short,Interger,Long,Double,Floate,Charcter等八种基本类型对应的包装类。



#### 十一：集合的Stack、Queue、Map的遍历



##### 1.Map的遍历

```
public class TestMap { 
        public static void main(String[] args) { 
                Map<String, String> map = new HashMap<String, String>(); 
                // put值
                map.put("1", "a"); 
                map.put("2", "b"); 
                map.put("3", "c"); 

                //最简洁、最通用的遍历方式 
                for (Map.Entry<String, String> entry : map.entrySet()) { 
                        System.out.println(entry.getKey() + " = " + entry.getValue()); 
                } 
                //Java5之前的比较简洁的便利方式1 
                System.out.println("----1----"); 
                for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext();) { 
                        Map.Entry<String, String> entry = it.next(); 
                        System.out.println(entry.getKey() + " = " + entry.getValue()); 
                } 
                //Java5之前的比较简洁的便利方式2 
                System.out.println("----2----"); 
                for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) { 
                        String key = it.next(); 
                        System.out.println(key + " = " + map.get(key)); 
                } 
        } 
}1234567891011121314151617181920212223242526
```



##### 2.Queue队列的遍历

```
public class TestQueue { 
        public static void main(String[] args) { 
                Queue<Integer> q = new LinkedBlockingQueue<Integer>(); 
                //初始化队列 
                for (int i = 0; i < 5; i++) { 
                        q.offer(i); 
                } 
                System.out.println("-------1-----"); 
                //集合方式遍历，元素不会被移除 
                for (Integer x : q) { 
                        System.out.println(x); 
                } 
                System.out.println("-------2-----"); 
                //队列方式遍历，元素逐个被移除 
                while (q.peek() != null) { 
                        System.out.println(q.poll()); 
                } 
        } 
}12345678910111213141516171819
```



##### 3.Stack栈的遍历

```
public class TestStack { 
        public static void main(String[] args) { 
                Stack<Integer> s = new Stack<Integer>(); 
                for (int i = 0; i < 10; i++) { 
                        s.push(i); 
                } 
                //集合遍历方式 
                for (Integer x : s) { 
                        System.out.println(x); 
                } 
                System.out.println("------1-----"); 
                // 栈弹出遍历方式 
                // while (s.peek()!=null) {     //不健壮的判断方式，容易抛异常，正确写法是下面的 
                while (!s.empty()) { 
                        System.out.println(s.pop()); 
                } 
                System.out.println("------2-----"); 
                // 错误的遍历方式 
                // for (Integer x : s) { 
                //        System.out.println(s.pop()); 
                // } 
        } 
}1234567891011121314151617181920212223
```

在遍历集合时候，优先考虑使用foreach语句来做，这样代码更简洁些。



#### 十二：length,length(),size() 的使用与区别

> - length属性是针对数组说的，比如说你声明了一个数组，想知道这个数组的长度则用到了length这个属性。
> - length()方法是针对字符串String说的，如果想看这个字符串的长度则用到length()这个方法。
> - size()方法是针对泛型集合泛型集合(Collection)如Set、List、Map说的，如果想看这个泛型有多少个元素，就调用此方法来查看。数组没有size()方法。

```
public static void main(String[] args) {
        String[] list = {"hello", "baidu"};
        String a = "hellobaidu";
        List<Object> array = new ArrayList();
        array.add(a);

        System.out.println(list.length);
        System.out.println(a.length());
        System.out.println(array.size());
}

// ---> 2  10  1
```