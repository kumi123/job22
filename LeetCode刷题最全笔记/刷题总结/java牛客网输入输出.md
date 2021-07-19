---

title: 牛客OJ输入问题
thumbnail: true
author: Kumi
date: 2020-02-23 22:20:51
icons: [fas fa-fire red, fas fa-star green]
cover: true
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN/23.jpg
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

## 牛客网输入输出java

# Java Scanner 类

java.util.Scanner 是 Java5 的新特征，我们可以通过 Scanner 类来获取用户的输入。

下面是创建 Scanner 对象的基本语法：

Scanner s = new Scanner(System.in);

接下来我们演示一个最简单的数据输入，并通过 Scanner 类的 next() 与 nextLine() 方法获取输入的字符串，在读取前我们一般需要 使用 hasNext 与 hasNextLine 判断是否还有输入的数据：



### 使用 next 方法：

## ScannerDemo.java 文件代码：

```java
import java.util.Scanner; 
 
public class ScannerDemo {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // 从键盘接收数据
 
        // next方式接收字符串
        System.out.println("next方式接收：");
        // 判断是否还有输入
        if (scan.hasNext()) {
            String str1 = scan.next();
            System.out.println("输入的数据为：" + str1);
        }
        scan.close();
    }
}
```

执行以上程序输出结果为：

```
$ javac ScannerDemo.java
$ java ScannerDemo
next方式接收：
runoob com
输入的数据为：runoob
```

可以看到 com 字符串并未输出，接下来我们看 nextLine。

### 使用 nextLine 方法：

## ScannerDemo.java 文件代码：

```java
import java.util.Scanner;
 
public class ScannerDemo {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // 从键盘接收数据
 
        // nextLine方式接收字符串
        System.out.println("nextLine方式接收：");
        // 判断是否还有输入
        if (scan.hasNextLine()) {
            String str2 = scan.nextLine();
            System.out.println("输入的数据为：" + str2);
        }
        scan.close();
    }
}
```

执行以上程序输出结果为：

```
$ javac ScannerDemo.java
$ java ScannerDemo
nextLine方式接收：
runoob com
输入的数据为：runoob com
```

可以看到 com 字符串输出。

### next() 与 nextLine() 区别

next():

- 1、一定要读取到有效字符后才可以结束输入。
- 2、对输入有效字符之前遇到的空白，next() 方法会自动将其去掉。
- 3、只有输入有效字符后才将其后面输入的空白作为分隔符或者结束符。
- next() 不能得到带有空格的字符串。

nextLine()：

- 1、以Enter为结束符,也就是说 nextLine()方法返回的是==输入回车之前的所有字符==。
- 2、可以获得空白。

如果要输入 int 或 float 类型的数据，在 Scanner 类中也有支持，但是在输入之前最好先使用 hasNextXxx() 方法进行验证，再使用 nextXxx() 来读取：

## ScannerDemo.java 文件代码：

```java
import java.util.Scanner;
 
public class ScannerDemo {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // 从键盘接收数据
        int i = 0;
        float f = 0.0f;
        System.out.print("输入整数：");
        if (scan.hasNextInt()) {
            // 判断输入的是否是整数
            i = scan.nextInt();
            // 接收整数
            System.out.println("整数数据：" + i);
        } else {
            // 输入错误的信息
            System.out.println("输入的不是整数！");
        }
        System.out.print("输入小数：");
        if (scan.hasNextFloat()) {
            // 判断输入的是否是小数
            f = scan.nextFloat();
            // 接收小数
            System.out.println("小数数据：" + f);
        } else {
            // 输入错误的信息
            System.out.println("输入的不是小数！");
        }
        scan.close();
    }
}
```

执行以上程序输出结果为：

```
$ javac ScannerDemo.java
$ java ScannerDemo
输入整数：12
整数数据：12
输入小数：1.2
小数数据：1.2
```

以下实例我们可以输入多个数字，并求其总和与平均数，每输入一个数字用回车确认，通过输入非数字来结束输入并输出执行结果：

## ScannerDemo.java 文件代码：

```java
import java.util.Scanner;
 
class ScannerDemo {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
 
        double sum = 0;
        int m = 0;
 
        while (scan.hasNextDouble()) {
            double x = scan.nextDouble();
            m = m + 1;
            sum = sum + x;
        }
 
        System.out.println(m + "个数的和为" + sum);
        System.out.println(m + "个数的平均值是" + (sum / m));
        scan.close();
    }
}
```

执行以上程序输出结果为：

```
$ javac ScannerDemo.java
$ java ScannerDemo
12
23
15
21.4
end
4个数的和为71.4
4个数的平均值是17.85
```

更多内容可以参考 API 文档：[http://www.runoob.com/manual/jdk1.6/](https://www.runoob.com/manual/jdk1.6/)。







#### 第二题

![image-20201125163206243](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20201125163206243.png)

```java
import java.util.*;
import java.util.Scanner;
public class Main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while(t>0){//接着弄就可以
            int a = sc.nextInt();
            int b = sc.nextInt();
            System.out.println(a+b);
            t--;
        }
    }
}
```





#### 第二题

![image-20201125164102015](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20201125164102015.png)

```java
import java.util.Scanner;
public class Main{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        while(sc.hasNextInt()){
            int a =sc.nextInt();
            int b=sc.nextInt();
            if(a!=0&&b!=0){
                System.out.println(a+b);
            }
            else{
                break;
            }
        }
        
    }
}
```

##### 相对简洁的方法

```java
import java.io.*;
import java.util.*;
public class Main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextInt()){
            int a = sc.nextInt();
            int b = sc.nextInt();
            if(a==0 && b==0){
                break;
            }
            System.out.println(a+b);
        }
    }
}
```



#### 第四题

![image-20201125172020831](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20201125172020831.png)

```java
import java.util.Scanner;
public class Main{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        while(sc.hasNextInt()){
            int n=sc.nextInt();
            if(n==0){
                break;
            }
            int sum=0;
            while(n>0){
                int a=sc.nextInt();
                sum+=a;
                n--;
            }
            System.out.println(sum);
        }
    }
        
}
```



#### 第五题

```java
import java.util.Scanner;
public class Main{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        while(n>0){
            int nn=sc.nextInt();
            int sum=0;
            while(nn>0){
            sum+=sc.nextInt();
            nn--;
            }
            System.out.println(sum);
            n--;
    }
}
}
```



### 第6题

![image-20201126145646527](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20201126145646527.png)

```java
import java.util.Scanner;
public class Main{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        while(sc.hasNextInt()){
            int n=sc.nextInt();
            int sum=0;
            while(n>0){
                sum+=sc.nextInt();
                n--;
            }
            System.out.println(sum);
        }
    }
}
```

### 第七题（不定大小的数据）



![image-20201126152023039](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20201126152023039.png)

```java
import java.util.Scanner;
public class Main{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        while(sc.hasNextLine()){
            String[] ll=sc.nextLine().split(" ");
            int sum=0;
            for(int i=0;i<ll.length;i++){
                sum+=Integer.parseInt(ll[i]);
                    
            }
            System.out.println(sum);
            
            
        }
    }
}
```

### 第八题

![image-20201126163844837](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20201126163844837.png)

```java
import java.util.Scanner;
import java.util.Arrays;
public class Main{
    public static void main(String[] args ){
        Scanner sc=new Scanner(System.in);
        if(sc.hasNext()){
            sc.nextLine();//ignore,相当移动到下一行
        }
        if(!sc.hasNext()){//如果没有，那就直接进行返回
            return;
        }
        //打印出来的是一个字符串，有空格的而不是直接就是一个字符数组
        StringBuilder res=new StringBuilder();
        String[] ll=sc.nextLine().split(" ");
        Arrays.sort(ll);
        for(int i=0;i<ll.length-1;i++){
            res.append(ll[i]).append(" ");}
            res.append(ll[ll.length-1]);
            System.out.println(res.toString());//builder要转化成toString
    }
}
```

```java
import java.util.Scanner;
import java.util.Arrays;
public class Main{
    public static void main(String[] args ){
        Scanner sc=new Scanner(System.in);
        if(sc.hasNext()){
            sc.nextLine();//ignore
        }
        if(!sc.hasNext()){
            return;
        }
        //StringBuilder res=new StringBuilder();
        String res="";
         String[] ll=sc.nextLine().split(" ");
            Arrays.sort(ll);
            for(int i=0;i<ll.length-1;i++){
                res+=(ll[i]);
                res+=" ";//最鸡肋的方法，打印出一个字母一个空格就可以

            }
            res+=ll[ll.length-1];
            System.out.println(res);

    }
}
```

### 第八题

###### 相对于上边的就一个循环就可以

![image-20201126165544163](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20201126165544163.png)

```java
import java.util.Scanner;
import java.util.Arrays;
public class Main{
    public static void main(String[] args ){
        Scanner sc=new Scanner(System.in);
        while(sc.hasNextLine()){
        //打印出来的是一个字符串，有空格的而不是直接就是一个字符数组
        StringBuilder res=new StringBuilder();
        String[] ll=sc.nextLine().split(" ");
        Arrays.sort(ll);
        for(int i=0;i<ll.length-1;i++){
            res.append(ll[i]).append(" ");
        }
            res.append(ll[ll.length-1]);
            System.out.println(res.toString());//builder要转化成toString
        }
    }
}
```

### 第九题

![image-20201126165803842](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20201126165803842.png)

##### 空格转成这个，就可以

```java
import java.util.Scanner;
import java.util.Arrays;
public class Main{
    public static void main(String[] args ){
        Scanner sc=new Scanner(System.in);
        while(sc.hasNextLine()){
        //打印出来的是一个字符串，有空格的而不是直接就是一个字符数组
        StringBuilder res=new StringBuilder();
        String[] ll=sc.nextLine().split(",");
        Arrays.sort(ll);
        for(int i=0;i<ll.length-1;i++){
            res.append(ll[i]).append(",");
        }
            res.append(ll[ll.length-1]);
            System.out.println(res.toString());//builder要转化成toString
        }
    }
}
```

