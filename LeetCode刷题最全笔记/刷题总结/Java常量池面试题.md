# Java常量池面试题

## 试题一、String s = new String("abc")创建了几个对象？

String s = new String("abc")实际上是"abc"本身就是字符串池中的一个对象，在运行 new String()时，把字符串池的字符串"abc"复制到堆中，并把这个对象的应用交给s，所以创建了两个String对象，一个在字符串池中，一个在堆中。(注：我们假设的是字符串池中默认是没有abc字符串的，如果之前已存在的话，则该题的答案就是一个对象了)

下面看这个程序，创建了几个对象。

```
package cn.bjca.february.first;

public class stringPool {

	public static void main(String[] args) {
		String s1 = new String("abc");
		String s2 = new String("abc");
		if (s1 == s2) {
			System.out.println("在堆中只创建了一个对象");
		} else {
			System.out.println("在堆中创建了二个对象");
		}
	}
}
```

输出：

```
在堆中创建了二个对象
```

可知在堆中创建了两个对象，但是在字符串池中还有一个对象，所以共创建了三个对象。

## 试题二、String name=new String("java"+"hello");创建了几个对象？

对于该题常见的解释有如下两种，

其一：

1."java"创建了一个对象，存于String常量池
2."hello"创建了一个对象，存于String常量池
3."java"+"hello",创建了一个对象，存于常量池（基于字符串的+操作，如带有引用的，将在堆中创建对象，否则值会存于字符常量池）
4.new将会创建一个对象，将字符常量池中的"javahello"复制到堆中
此例将创建4个对象

其二：

像"java"+"hello"，java在编译期间会自己先优化的，会合并成一个对象"javahello"的，然后在字符串池中保留，然后new的时候再在堆中创建新的对象。因此共创建了两个对象。

```
[root@hina stringtest]# more TestString.java 
public class TestString{
public static void main(String[] args) {
String name = new String("java" + "hello");
}
}
[root@hina stringtest]# javac TestString.java
[root@hina stringtest]# javap -c TestString
 
Compiled from "TestString.java"
public class TestString extends java.lang.Object{
public TestString();
  Code:
   0:	aload_0
   1:	invokespecial	#1; //Method java/lang/Object."<init>":()V
   4:	return
 
public static void main(java.lang.String[]);
  Code:
   0:	new	#2; //class java/lang/String
   3:	dup
   4:	ldc	#3; //String javahello
   6:	invokespecial	#4; //Method java/lang/String."<init>":(Ljava/lang/String;)V
   9:	astore_1
   10:	return
}
```

看清楚了，编译的字节码里没有java也没有hello，而是javahello。这个javahello不是在执行的时候被创建，而是jvm启动的时候初始化好的。

实际上只有一个new，也就是说只创建了一个对象:
0 new 创建string对象
3 dup 复制顶栈内容，这是所有new对象都会做的事情，因为jvm要做<init>
4 ldc 把常量池中javahello的引用压入栈中
6 invokespecial 对象初始化
9 astore_1 把栈中的引用赋给第一个变量

所以，上面的第二种解释是正确的，即：创建了两个对象，一个javahello的对象在字符串池中，一个new出的对象在堆上。

## 试题三

见下面的代码

```
String str1="abc";
String str2="def";
String str3=str1+str2;
System.out.println(str3=="abcdef");
//结果是false
```

因为str3指向堆中的"abcdef"对象并未是字符串池中的对象，而"abcdef"是字符串池中的对象，所以结果为false。JVM对String str="abc"对象放在常量池中是在编译时做的，而String str3=str1+str2是在运行时刻才能知道的。new对象也是在运行时才做的。而这段代码总共创建了5个对象，字符串池中两个、堆中三个。+运算符会在堆中建立来两个String对象，也就是说从字符串池中复制这两个值，然后在堆中创建两个对象，然后再建立对象str3,然后将"abcdef"的堆地址赋给str3。

而如果，如下的代码：

```
String str="abc"+"def";   //直接将"abcdef"放入字符串池中
System.out.println(str=="abcdef");
//结果为true

String str1="abc";     
String str2=str1+"def";    //str1是在堆中创建的对象
System.out.println(str2=="abcdef");
//结果为false
```


本例子说明，str2也并未放到字符串池中。从上面的结果中我们不难看出，==只有使用引号包含文本的方式创建的String对象之间使用“+”连接产生的新对象才会被加入字符串池中。对于所有包含new方式新建对象（包括null）的“+”连接表达式，它所产生的新对象都不会被加入字符串池中。==

由于字符串对象的大量使用(它是一个对象，一般而言对象总是在堆中分配内存)，Java中为了节省内存空间和运行时间(如比较字符串时，==比equals()快)，在编译阶段就把所有的字符串文字放到一个字符串池中，而运行时字符串池成为常量池的一部分。字符串池的好处，就是该池中所有相同的字符串常量被合并，只占用一个空间。
我们知道，对两个引用变量，使用==判断它们的值(引用)是否相等，即指向同一个对象：

```
String s1 = "abc" ;
String s2 = "abc" ;
if( s1 == s2 ) 
    System.out.println("s1,s2 refer to the same object");
else 
    System.out.println("trouble");
```

这里的输出显示，两个字符串文字保存为一个对象。就是说，上面的代码只在字符串中创建了一个String对象。

现在看String s = new String("abc");语句，这里"abc"本身就是pool中的一个对象，而在运行时执行new String()时，将字符串池中的对象复制一份放到堆中，并且把堆中的这个对象的引用交给s持有。ok，这条语句就创建了2个String对象。

String s1 = new String("abc") ;
String s2 = new String("abc") ;
if( s1 == s2 ){ //不会执行的语句}

这时用==判断就可知，虽然两个对象的"内容"相同(equals()判断)，但两个引用变量所持有的引用不同，上面的代码创建了几个String 对象? (3个，字符串池中1个，堆中2个。)

## 试题四：字符串常量池创建字符串有几种方式?

创建字符串有两种方式：两种内存区域（字符串池，堆）

==1." " 引号创建的字符串在字符串池中==
==2.new，new创建字符串时首先查看池中是否有相同值的字符串，如果有，则拷贝一份到堆中，然后返回堆中的地址；如果池中没有，则在堆中创建一份，然后返回堆中的地址（注意，此时不需要从堆中复制到池中，否则导致浪费池的空间）==
==3.另外，对字符串进行赋值时，如果右操作数含有一个或一个以上的字符串引用时，则在堆中再建立一个字符串对象，返回引用；如String str2=str1+ "abc";== 
==比较两个已经存在于字符串池中字符串对象可以用"=="进行，拥有比equals操作符更快的速度。==