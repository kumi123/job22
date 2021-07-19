---

title: 题解贪心
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2020-04-24 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/22.jpg
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
# Leetcode 题解 - 贪心思想
<!-- GFM-TOC -->
* [Leetcode 题解 - 贪心思想](#leetcode-题解---贪心思想)
    * [1. 分配饼干](#1-分配饼干)
    * [2. 不重叠的区间个数](#2-不重叠的区间个数)
    * [3. 投飞镖刺破气球](#3-投飞镖刺破气球)
    * [4. 根据身高和序号重组队列](#4-根据身高和序号重组队列)
    * [5. 买卖股票最大的收益](#5-买卖股票最大的收益)
    * [6. 买卖股票的最大收益 II](#6-买卖股票的最大收益-ii)
    * [7. 种植花朵](#7-种植花朵)
    * [8. 判断是否为子序列](#8-判断是否为子序列)
    * [9. 修改一个数成为非递减数组](#9-修改一个数成为非递减数组)
    * [10. 子数组最大的和](#10-子数组最大的和)
    * [11. 分隔字符串使同种字符出现在一起](#11-分隔字符串使同种字符出现在一起)
<!-- GFM-TOC -->


保证每次操作都是局部最优的，并且最后得到的结果是全局最优的。

## 1. 分配饼干

455\. Assign Cookies (Easy)

[Leetcode](https://leetcode.com/problems/assign-cookies/description/) / [力扣](https://leetcode-cn.com/problems/assign-cookies/description/)

```html
Input: grid[1,3], size[1,2,4]
Output: 2
```

题目描述：每个孩子都有一个满足度 grid，每个饼干都有一个大小 size，只有饼干的大小大于等于一个孩子的满足度，该孩子才会获得满足。求解最多可以获得满足的孩子数量。

1. 给一个孩子的饼干应当尽量小并且又能满足该孩子，这样大饼干才能拿来给满足度比较大的孩子。
2. 因为满足度最小的孩子最容易得到满足，所以先满足满足度最小的孩子。

在以上的解法中，我们只在每次分配时饼干时选择一种看起来是当前最优的分配方法，但无法保证这种局部最优的分配方法最后能得到全局最优解。我们假设能得到全局最优解，并使用反证法进行证明，即假设存在一种比我们使用的贪心策略更优的最优策略。如果不存在这种最优策略，表示贪心策略就是最优策略，得到的解也就是全局最优解。

证明：假设在某次选择中，贪心策略选择给当前满足度最小的孩子分配第 m 个饼干，第 m 个饼干为可以满足该孩子的最小饼干。假设存在一种最优策略，可以给该孩子分配第 n 个饼干，并且 m \< n。我们可以发现，经过这一轮分配，贪心策略分配后剩下的饼干一定有一个比最优策略来得大。因此在后续的分配中，贪心策略一定能满足更多的孩子。也就是说不存在比贪心策略更优的策略，即贪心策略就是最优策略。

####  从小到大

```java
class Solution {
    public int findContentChildren(int[] g, int[] s) {
        if(g.length==0||s.length==0){
            return 0;
        }
        //首先要排序，首先要尽量用小的糖果来满足需求比较小的孩子
        Arrays.sort(g);//胃口
        Arrays.sort(s);//糖果大小
        //使用for循环来进行控制，注意一定是要循环的是糖果，比较一下孩子不要超过限制就可以
        int j=0;//糖果下标
        int count=0;//孩子计数
        for(j=0;j<s.length&&count<g.length;j++){
            if(g[count]<=s[j]){
                count++;
            }
        }
        return count;
```

#### 从大到小

```java
class Solution {
    public int findContentChildren(int[] g, int[] s) {
        if(g.length==0||s.length==0){
            return 0;
        }
        //首先要排序，首先要尽量用大的糖果来满足需求比较大的孩子
        Arrays.sort(g);
        Arrays.sort(s);
        int i;
        int j=s.length-1;
        int count=0;
        //需要遍历孩子，看看消耗了多少糖果
        for(i=g.length-1;i>=0&&j>=0;i--){
            if(g[i]<=s[j]){
                j--;
                count++;
            }

        }
        return count;
```



<div align="center"> <img src="https://cs-notes-1256109796.cos.ap-guangzhou.myqcloud.com/e69537d2-a016-4676-b169-9ea17eeb9037.gif" width="430px"> </div><br>

```java
public int findContentChildren(int[] grid, int[] size) {
    if (grid == null || size == null) return 0;
    Arrays.sort(grid);
    Arrays.sort(size);
    int gi = 0, si = 0;
    while (gi < grid.length && si < size.length) {
        if (grid[gi] <= size[si]) {
            gi++;
        }
        si++;
    }
    return gi;
}
```

## 2. 不重叠的区间个数

435\. Non-overlapping Intervals (Medium)

[Leetcode](https://leetcode.com/problems/non-overlapping-intervals/description/) / [力扣](https://leetcode-cn.com/problems/non-overlapping-intervals/description/)

```html
Input: [ [1,2], [1,2], [1,2] ]

Output: 2

Explanation: You need to remove two [1,2] to make the rest of intervals non-overlapping.
```

```html
Input: [ [1,2], [2,3] ]

Output: 0

Explanation: You don't need to remove any of the intervals since they're already non-overlapping.
```

题目描述：计算让一组区间不重叠所需要移除的区间个数。

先计算最多能组成的不重叠区间个数，然后用区间总个数减去不重叠区间的个数。

在每次选择中，区间的结尾最为重要，选择的区间结尾越小，留给后面的区间的空间越大，那么后面能够选择的区间个数也就越大。

按区间的结尾进行排序，每次选择结尾最小，并且和前一个区间不重叠的区间。





#### 前边关于sort 表达式排序





```java
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
 
public class ComparatorTest {
 
    @Test
    public void test1(){
        /**
         * Collections.sort()使用
         */
        //被排序的集合
        List<User> userList = Lists.newArrayList(new User("Jack",11),new User("Jack",10));
 
        //1. Java8之前，使用匿名内部类的基本排序
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return user1.getAge().compareTo(user2.getAge());
            }
        });
 
        //2. Java8,使用Lambda表达式的基本排序
        Collections.sort(userList,
                 (User user1, User user2) ->user1.getAge().compareTo(user2.getAge()));
 
        //userList.sort((User user1, User user2) -> user1.getAge().compareTo(user2.getAge()));
 
        //3. Java8,Lambda表达式可以简化，省略定义类型User
        userList.sort((user1, user2) -> user1.getAge().compareTo(user2.getAge()));
 
        //4. Java8,Lambda表达式，多条件排序
        userList.sort((user1, user2) -> {
            if (user1.getName().equals(user2.getName())) {
            return user1.getAge() - user2.getAge();
            } else {
            return user1.getName().compareTo(user2.getName());
            }
        });
 
        //5. Java8,多条件组合排序
        userList.sort(Comparator.comparing(User::getName).thenComparing(User::getAge));
 
        //6. Java8,提取Comparator进行排序
        Collections.sort(userList, Comparator.comparing(User::getName));
 
        //7. Java8,自定义静态的比较方法来排序(静态方法必须写在被比较的类(这里是User类)中)
        userList.sort(User::compareByAgeThenName);
 
        //8. Java8,反转排序
        Comparator<User> comparator = (user1, user2) -> user1.getName().compareTo(user2.getName());
        userList.sort(comparator);//先按name排序
        userList.sort(comparator.reversed());//反转排序
        Assert.assertEquals(userList.get(0),new User("Jack",10));
 
        /**
         * Arrays.sort()使用
         */
        //被排序的字符串数组
        String[] months = {"January","February","March","April","May","June","July","August","September","October","December"};
        //按字符串长度排序
        //1.
        Arrays.sort(months, (a, b) -> Integer.signum(a.length() - b.length()));
        //2.
        Arrays.sort(months, Comparator.comparingInt(String::length));
        //3.
        Arrays.sort(months, (a, b) -> a.length() - b.length());
        //4.
        Arrays.sort(months,
                (String a, String b) -> { return Integer.signum(a.length() - b.length()); }
        );
        
        System.out.println(Arrays.toString(months));
    }
}
```

#### 具体代码



```java
class Solution {
    public int eraseOverlapIntervals(int[][] intervals) {
        if(intervals==null||intervals.length==0) return 0;
        //按照结尾数组来进行排序就可以，每一次都进行排序就可以
        Arrays.sort(intervals,(o1,o2)->(o1[1]-o2[1]));
        int count=0,end=intervals[0][1];
        for(int i=1;i<intervals.length;i++){
            if(intervals[i][0]<end){
                continue;

            }
            count++;
            end=intervals[i][1];
        }
        return intervals.length-count-1;//减一是因为count只是不包含第一个区间的，所以是多个好吧

    }
}
```

```java
class Solution:
    def eraseOverlapIntervals(self, intervals: List[List[int]]) -> int:
        if len(intervals)==0:
            return 0
        sorted(intervals, key=lambda x:x[1])
        end=intervals[0][1]
        count=1
        for i in range(1,len(intervals)):
            if intervals[i][0]<end:
                continue
            end=intervals[i][1]
            count+=1
        return len(intervals)-count
```



```java
public int eraseOverlapIntervals(int[][] intervals) {
    if (intervals.length == 0) {
        return 0;
    }
    Arrays.sort(intervals, Comparator.comparingInt(o -> o[1]));
    int cnt = 1;
    int end = intervals[0][1];
    for (int i = 1; i < intervals.length; i++) {
        if (intervals[i][0] < end) {
            continue;
        }
        end = intervals[i][1];
        cnt++;
    }
    return intervals.length - cnt;
}
```

使用 lambda 表示式创建 Comparator 会导致算法运行时间过长，如果注重运行时间，可以修改为普通创建 Comparator 语句：

```java
Arrays.sort(intervals, new Comparator<int[]>() {
     @Override
     public int compare(int[] o1, int[] o2) {
         return (o1[1] < o2[1]) ? -1 : ((o1[1] == o2[1]) ? 0 : 1);
     }
});
```

实现 compare() 函数时避免使用 `return o1[1] - o2[1];` 这种减法操作，防止溢出。

## 3. 投飞镖刺破气球

452\. Minimum Number of Arrows to Burst Balloons (Medium)

[Leetcode](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/description/) / [力扣](https://leetcode-cn.com/problems/minimum-number-of-arrows-to-burst-balloons/description/)



[这篇题解绘图太棒了](https://leetcode-cn.com/problems/minimum-number-of-arrows-to-burst-balloons/solution/yong-zui-shao-shu-liang-de-jian-yin-bao-qi-qiu-tu-/)

[说明了为什么用右边升序而不是左边升序](https://leetcode-cn.com/problems/minimum-number-of-arrows-to-burst-balloons/solution/tu-jie-tan-tao-wei-shi-yao-yao-an-qu-jian-de-you-d/)



![image-20210112152618472](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210112152618472.png)

只要选择不相交的节点就可以，相交的只要选择最小的数据右边节点就可以全部射爆

![image.png](https://pic.leetcode-cn.com/1606095622-AbeBhX-image.png)

![image.png](https://pic.leetcode-cn.com/1606095631-EXJdOo-image.png)

```
Input:
[[10,16], [2,8], [1,6], [7,12]]

Output:
2
```

题目描述：气球在一个水平数轴上摆放，可以重叠，飞镖垂直投向坐标轴，使得路径上的气球都被刺破。求解最小的投飞镖次数使所有气球都被刺破。

也是计算不重叠的区间个数，不过和 Non-overlapping Intervals 的区别在于，[1, 2] 和 [2, 3] 在本题中算是重叠区间。



```python
class Solution:
    def findMinArrowShots(self, points: List[List[int]]) -> int:
        if len(points)==0 or points==None:
            return 0
        points.sort(key=lambda x:x[1])
        end=points[0][1]
        count=1
        for i in range(1,len(points)):
            if end>=points[i][0]:#这说明是有重合和上一个，所以可以一起射爆
                continue#停止本次循环
            end=points[i][1]
            count+=1
        return count

class Solution:
    def findMinArrowShots(self, points: List[List[int]]) -> int:
        if len(points)==0 or points==None:
            return 0
        points.sort(key=lambda x:x[1])
        end=points[0][1]
        count=1
        for i in range(1,len(points)):
            if end<points[i][0]:#这说明是没有重合，所以可以一起射爆
                end=points[i][1]
                count+=1
        return count    
```



```java
public int findMinArrowShots(int[][] points) {
    if (points.length == 0) {
        return 0;
    }
    Arrays.sort(points, Comparator.comparingInt(o -> o[1]));
    int cnt = 1, end = points[0][1];
    for (int i = 1; i < points.length; i++) {
        if (points[i][0] <= end) {
            continue;
        }
        cnt++;
        end = points[i][1];
    }
    return cnt;
}


class Solution {
    public int findMinArrowShots(int[][] points) {
        if(points.length==0||points==null){
            return 0;
        }
        Arrays.sort(points,(e1,e2)->(e1[1]-e2[1]));
        int end=points[0][1];
        int count=1;
        for(int i=1;i<points.length;i++){
            if(end<points[i][0]){
                count++;
                end=points[i][1];
            }
        }
        return count;

    }
}
```

## 4. 根据身高和序号重组队列

406\. Queue Reconstruction by Height(Medium)

[Leetcode](https://leetcode.com/problems/queue-reconstruction-by-height/description/) / [力扣](https://leetcode-cn.com/problems/queue-reconstruction-by-height/description/)

```html
Input:
[[7,0], [4,4], [7,1], [5,0], [6,1], [5,2]]

Output:
[[5,0], [7,0], [5,2], [6,1], [4,4], [7,1]]
```

题目描述：一个学生用两个分量 (h, k) 描述，h 表示身高，k 表示排在前面的有 k 个学生的身高比他高或者和他一样高。

为了使插入操作不影响后续的操作，身高较高的学生应该先做插入操作，否则身高较小的学生原先正确插入的第 k 个位置可能会变成第 k+1 个位置。

身高 h 降序、个数 k 值升序，然后将某个学生插入队列的第 k 个位置中。

```java
    /**
     * 解题思路：先排序再插入
     * 1.排序规则：按照先H高度降序，K个数升序排序
     * 2.遍历排序后的数组，根据K插入到K的位置上
     *
     * 核心思想：高个子先站好位，矮个子插入到K位置上，前面肯定有K个高个子，矮个子再插到前面也满足K的要求
     *
     * @param people
     * @return
     */
    public int[][] reconstructQueue(int[][] people) {
        // [7,0], [7,1], [6,1], [5,0], [5,2], [4,4]
        // 再一个一个插入。
        // [7,0]
        // [7,0], [7,1]
        // [7,0], [6,1], [7,1]
        // [5,0], [7,0], [6,1], [7,1]
        // [5,0], [7,0], [5,2], [6,1], [7,1]
        // [5,0], [7,0], [5,2], [6,1], [4,4], [7,1]
        Arrays.sort(people, (o1, o2) -> o1[0] == o2[0] ? o1[1] - o2[1] : o2[0] - o1[0]);

        LinkedList<int[]> list = new LinkedList<>();
        for (int[] i : people) {
            list.add(i[1], i);
        }

        return list.toArray(new int[list.size()][2]);
    }

//自己的一个结果

class Solution {
    public int[][] reconstructQueue(int[][] people) {
        //先进行排序,按照第一个数据从高到低排序，如果第一个身高相等的话就按照这个从低到高进行排序
        Arrays.sort(people,(e1,e2)->e1[0]==e2[0]?e1[1]-e2[1]:e2[0]-e1[0]);//意义就是先按照身高从大到小排列，如果身高相同就按照位置从小到大排列
        List<int[]> res=new ArrayList<>();//存储并且排序
        for(int[] person:people){
            int index=person[1];
            res.add(index,person);//先插大的数据到合适位置，这样小的数据即使插到前边也不会影响大的数据的排列结果
        }
        return res.toArray(new int[res.size()][2]);
        

    }
}




```

#### python版本

```python
class Solution:
    def reconstructQueue(self, people: List[List[int]]) -> List[List[int]]:
        #首先进行排序
        res=[]
        people.sort(key=lambda x:(-x[0],x[1]))
        for person in people:
            res.insert(person[1],person)
        return res
            
```







```java
public int[][] reconstructQueue(int[][] people) {
    if (people == null || people.length == 0 || people[0].length == 0) {
        return new int[0][0];
    }
    Arrays.sort(people, (a, b) -> (a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]));
    List<int[]> queue = new ArrayList<>();
    for (int[] p : people) {
        queue.add(p[1], p);
    }
    return queue.toArray(new int[queue.size()][]);
}
```

## 5. 买卖股票最大的收益

121\. Best Time to Buy and Sell Stock (Easy)

[Leetcode](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/description/) / [力扣](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock/description/)

题目描述：一次股票交易包含买入和卖出，只进行一次交易，求最大收益。

只要记录前面的最小价格，将这个最小价格作为买入价格，然后将当前的价格作为售出价格，查看当前收益是不是最大收益。





#### 动态规划比较合理

```python
class Solution:
    def maxProfit(self, prices: List[int]) -> int:
        if len(prices)==0:
            return 0
        #想想用dp怎么进行求解，dp[i]表示前i天可以达到的最大利润=当天的值 -之前最小值或者是前一天的值,初始值需要有 然后从哪儿开始也需要有
        dp=[0 for i in range(len(prices))]
        mined=prices[0]
        dp[0]=0
        for i in range(1,len(prices)):
            mined=min(prices[i],mined)
            dp[i]=max(dp[i-1],prices[i]-mined)
        return dp[-1]
```





```java
public int maxProfit(int[] prices) {
    int n = prices.length;
    if (n == 0) return 0;
    int soFarMin = prices[0];
    int max = 0;
    for (int i = 1; i < n; i++) {
        if (soFarMin > prices[i]) soFarMin = prices[i];
        else max = Math.max(max, prices[i] - soFarMin);
    }
    return max;
}
```


## 6. 买卖股票的最大收益 II

122\. Best Time to Buy and Sell Stock II (Easy)

[Leetcode](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/description/) / [力扣](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/description/)

题目描述：可以进行多次交易，多次交易之间不能交叉进行，可以进行多次交易。

对于 [a, b, c, d]，如果有 a \<= b \<= c \<= d ，那么最大收益为 d - a。而 d - a = (d - c) + (c - b) + (b - a) ，因此当访问到一个 prices[i] 且 prices[i] - prices[i-1] \> 0，那么就把 prices[i] - prices[i-1] 添加到收益中。

```python
class Solution:
    def maxProfit(self, prices: List[int]) -> int:
        if len(prices)==0:
            return 0
        res=0
        #每一个上升期的股票都进行买卖，下降期都不会进行分析
        for i in range(1,len(prices)):
            tem=prices[i]-prices[i-1]
            if tem>0:
                res+=tem
        return res
```



```java
public int maxProfit(int[] prices) {
    int profit = 0;
    for (int i = 1; i < prices.length; i++) {
        if (prices[i] > prices[i - 1]) {
            profit += (prices[i] - prices[i - 1]);
        }
    }
    return profit;
}
```


## 7. 种植花朵

605\. Can Place Flowers (Easy)

[Leetcode](https://leetcode.com/problems/can-place-flowers/description/) / [力扣](https://leetcode-cn.com/problems/can-place-flowers/description/)

```html
Input: flowerbed = [1,0,0,0,1], n = 1
Output: True
```

题目描述：flowerbed 数组中 1 表示已经种下了花朵。花朵之间至少需要一个单位的间隔，求解是否能种下 n 朵花。

```java
public boolean canPlaceFlowers(int[] flowerbed, int n) {
    int len = flowerbed.length;
    int cnt = 0;
    for (int i = 0; i < len && cnt < n; i++) {
        if (flowerbed[i] == 1) {
            continue;
        }
        int pre = i == 0 ? 0 : flowerbed[i - 1];
        int next = i == len - 1 ? 0 : flowerbed[i + 1];
        if (pre == 0 && next == 0) {
            cnt++;
            flowerbed[i] = 1;
        }
    }
    return cnt >= n;
}
```

## 8. 判断是否为子序列

392\. Is Subsequence (Medium)

[Leetcode](https://leetcode.com/problems/is-subsequence/description/) / [力扣](https://leetcode-cn.com/problems/is-subsequence/description/)





```java
class Solution {
    public boolean isSubsequence(String s, String t) {
        //首先边界条件
        if(s.length()>t.length()){
            return false;
        }
        if(s.length()==0){
            return true;
        }
        int i=0;//子串的下标索引，循环大串来弄小串
        int j;//原始的下标索引
        for(j=0;j<t.length();j++){
            if(t.charAt(j)==s.charAt(i)&&i<s.length()){
                i++;
                if(i==s.length()) break;
            }
        }
        return i==s.length();

    }
}
```

#### 就是一个遍历的问题



```java
class Solution {
    public boolean isSubsequence(String s, String t) {
        //首先边界条件
        if(s.length()>t.length()){
            return false;
        }
        if(s.length()==0){
            return true;
        }
        //使用A。indexOf(string,index)从A串的index位置找string,如果可以找到那么就返回下标
        int inx=-1;//初始下标，后边需要更新,这里需要注意，下一次一定是在inx+1位置进行找
        //如果是inx 那么可能连着两个一样的字母被一个字母重复找出，如果是inx+(>1) 可能会漏掉
        for(char c:s.toCharArray()){
            inx=t.indexOf(c,inx+1);
            if(inx==-1) {
                return false;
            }
            
        }
        return true;
    }
}
```



```java
class Solution {
    public boolean isSubsequence(String s, String t) {
        //首先边界条件
        if(s.length()>t.length()){
            return false;
        }
        if(s.length()==0){
            return true;
        }
        int j=0;//代表长字符下标函数
        //思路就是看长的字符串能不能找到小的字符串,就是循环全部的短字符之后就看长字符索引是否超过索引就可以
        for(char ss:s.toCharArray()){
            while(j<t.length()&&t.charAt(j)!=ss) j++;
            if(j++>=t.length()) break;//如果超过界限直接停止就可以
        }
        return j<=t.length();
        
    }
}
```

```html
s = "abc", t = "ahbgdc"
Return true.
```

```java
public boolean isSubsequence(String s, String t) {
    int index = -1;
    for (char c : s.toCharArray()) {
        index = t.indexOf(c, index + 1);
        if (index == -1) {
            return false;
        }
    }
    return true;
}
```

## 9. 修改一个数成为非递减数组

665\. Non-decreasing Array (Easy)

[Leetcode](https://leetcode.com/problems/non-decreasing-array/description/) / [力扣](https://leetcode-cn.com/problems/non-decreasing-array/description/)

```html
Input: [4,2,3]
Output: True
Explanation: You could modify the first 4 to 1 to get a non-decreasing array.
```

题目描述：判断一个数组是否能只修改一个数就成为非递减数组。

在出现 nums[i] \< nums[i - 1] 时，需要考虑的是应该修改数组的哪个数，使得本次修改能使 i 之前的数组成为非递减数组，并且   **不影响后续的操作**  。优先考虑令 nums[i - 1] = nums[i]，因为如果修改 nums[i] = nums[i - 1] 的话，那么 nums[i] 这个数会变大，就有可能比 nums[i + 1] 大，从而影响了后续操作。还有一个比较特别的情况就是 nums[i] \< nums[i - 2]，修改 nums[i - 1] = nums[i] 不能使数组成为非递减数组，只能修改 nums[i] = nums[i - 1]。

```java
public boolean checkPossibility(int[] nums) {
    int cnt = 0;
    for (int i = 1; i < nums.length && cnt < 2; i++) {
        if (nums[i] >= nums[i - 1]) {
            continue;
        }
        cnt++;
        if (i - 2 >= 0 && nums[i - 2] > nums[i]) {
            nums[i] = nums[i - 1];
        } else {
            nums[i - 1] = nums[i];
        }
    }
    return cnt <= 1;
}
```



## 10. 子数组最大的和

53\. Maximum Subarray (Easy)

[Leetcode](https://leetcode.com/problems/maximum-subarray/description/) / [力扣](https://leetcode-cn.com/problems/maximum-subarray/description/)





#### 一个更好的思路

```java

class Solution {
    public int maxSubArray(int[] nums) {
        int maxed=Integer.MIN_VALUE;
        int[] dp=new int[nums.length];
        dp[0]=nums[0];//这个dp[i]就是真真切切的以第i个数据结尾的最大数列和
        for(int i=1;i<nums.length;i++){
            dp[i]=Math.max(0,dp[i-1])+nums[i];//看这个之前的dp[i]是否为正，如果为正数，直接就进行相加相连就可以
            maxed=Math.max(maxed,dp[i]);//比较最大值
        }
        return Math.max(maxed,dp[0]);//还要看和第一个比较

    }
}


//状态压缩的结果，减少空间复杂度

class Solution {
    public int maxSubArray(int[] nums) {
        if(nums.length==0||nums==null) return 0;
        //把dp[i-1] 当成presum
        int presum=nums[0];
        int cursum;
        int maxed=Integer.MIN_VALUE;
        for(int i=1;i<nums.length;i++){
            cursum=Math.max(0,presum)+nums[i];
            maxed=Math.max(cursum,maxed);
            presum=cursum;
            
            
        }
        return Math.max(maxed,nums[0]);

    }
}

```



```html
For example, given the array [-2,1,-3,4,-1,2,1,-5,4],
the contiguous subarray [4,-1,2,1] has the largest sum = 6.
```

```java
//这个思路也是这个样子的，就是直接进行看之前的和是正数还是负数，然后比较就可以
public int maxSubArray(int[] nums) {
    if (nums == null || nums.length == 0) {
        return 0;
    }
    int preSum = nums[0];
    int maxSum = preSum;
    for (int i = 1; i < nums.length; i++) {
        preSum = preSum > 0 ? preSum + nums[i] : nums[i];
        maxSum = Math.max(maxSum, preSum);
    }
    return maxSum;
}
```

## 11. 分隔字符串使同种字符出现在一起

763\. Partition Labels (Medium)

[Leetcode](https://leetcode.com/problems/partition-labels/description/) / [力扣](https://leetcode-cn.com/problems/partition-labels/description/)

```html
Input: S = "ababcbacadefegdehijhklij"
Output: [9,7,8]
Explanation:
The partition is "ababcbaca", "defegde", "hijhklij".
This is a partition so that each letter appears in at most one part.
A partition like "ababcbacadefegde", "hijhklij" is incorrect, because it splits S into less parts.
```

```java
public List<Integer> partitionLabels(String S) {
    int[] lastIndexsOfChar = new int[26];
    for (int i = 0; i < S.length(); i++) {
        lastIndexsOfChar[char2Index(S.charAt(i))] = i;
    }
    List<Integer> partitions = new ArrayList<>();
    int firstIndex = 0;
    while (firstIndex < S.length()) {
        int lastIndex = firstIndex;
        for (int i = firstIndex; i < S.length() && i <= lastIndex; i++) {
            int index = lastIndexsOfChar[char2Index(S.charAt(i))];
            if (index > lastIndex) {
                lastIndex = index;
            }
        }
        partitions.add(lastIndex - firstIndex + 1);
        firstIndex = lastIndex + 1;
    }
    return partitions;
}

private int char2Index(char c) {
    return c - 'a';
}
```

#### [一个题解](https://leetcode-cn.com/problems/partition-labels/solution/shou-hua-tu-jie-hua-fen-zi-mu-qu-jian-ji-lu-zui-yu/)

###### 思路

- [ ] 需要一个数组来表示每一个字母出现的最远的索引（下标是字母，value代表着该字母出现的最后位置）不断更新就可以
- [ ] 然后切割的做法就是，从左到右来进行分析，保存已经遍历过的字母当中的最远距离curfar，当迭代的下标正好等于curfar,那么就直接切割就可以，下一个指针从下标下一个开始就可以

```java
public List<Integer> partitionLabels(String S) {
    int[] map = new int[26];
    Arrays.fill(map, -1);
    for (int i = 0; i < S.length(); i++) {
      map[S.charAt(i) - 'a'] = i;
    }
    List<Integer> res = new ArrayList<>();
    int idx = 0;
    while (idx < S.length()) {
      int curRight = map[S.charAt(idx) - 'a'];
      int i = idx;
      for (; i <= curRight; i++) {
        curRight = Math.max(curRight, map[S.charAt(i) - 'a']);
      }
      res.add(i - idx);
      idx = i;
    }
    return res;
  }


///自己写的
class Solution {
    public List<Integer> partitionLabels(String S) {
        List<Integer> res= new ArrayList<>();
        int[] map=new int[26];
        for(int i=0;i<S.length();i++){
            map[S.charAt(i)-'a']=i;
        }
        int start=0;//代表每一个开始段的指针
        int curfar=-1;//表示当前已经遍历过的字符的最远距离
        for(int i=0;i<S.length();i++){
            curfar=Math.max(curfar,map[S.charAt(i)-'a']);
            if(i==curfar){
                res.add(i-start+1);
                start=i+1;
            }
        }
        return res;

    }
}
```

```python
class Solution:
    def partitionLabels(self, S: str) -> List[int]:
        res=[]#存储结果
        maped=[-1 for i in range(26) ]#存储最远距离
        start=0#起点每一段
        curfar=-1#遍历过的最远位置
        for i in range(len(S)):
            maped[ord(S[i])-ord('a')]=i
        for i in range(len(S)):
            curfar=max(curfar,maped[ord(S[i])-ord('a')])
            if curfar==i:
                res.append(i-start+1)
                start=i+1
        return res
```

