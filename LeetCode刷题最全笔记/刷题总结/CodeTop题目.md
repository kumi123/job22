---

title: codetop题目
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2021-02-25 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/12.jpg
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

### ==[最长不重复子串](https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/solution/hua-jie-suan-fa-3-wu-zhong-fu-zi-fu-de-zui-chang-z/)==
```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        int n = s.length(), ans = 0;//初始值
        Map<Character, Integer> map = new HashMap<>();
        for (int end = 0, start = 0; end < n; end++) {
            char alpha = s.charAt(end);
            if (map.containsKey(alpha)) {//根据是否出现这个当前值，来看是否更新左边界，相当于从划窗当中去掉左边，加上右边
                start = Math.max(map.get(alpha), start);//这里要明确为什么要有一个max，比如说abcdca
                //一开始 abcd start=0
                //遇到d 窗口变成dc start=3
                //遇到a 窗口变成 dca start=3 不会变 ，如果没有max 那么就会start=1 这样显然不对，又会重复
            }
            ans = Math.max(ans, end - start + 1);
            map.put(s.charAt(end), end + 1);//这个意思是为了指明如果出现了重复的值，我就要从当前值下一个值进行开始统计
        }
        return ans;
    }
}

```



```JAVA
//会超过整数界限
class Solution {
    public String addStrings(String num1, String num2) {
        //这样还是需要就是做一个加数运算啊
        // 但是会超过最大
        int a=StringtoInt(num1);
        int b=StringtoInt(num2);

        return String.valueOf(a+b);


    }
    public int StringtoInt(String s){
        if(s==null) return 0;
        int n=s.length();
        int tem=0;
        for(int i=0;i<s.length();i++){
            Character ss=s.charAt(i);
            tem=10*tem+ss-'0';
        }
        return tem;
    }
}
```



### 最大股票效率机制

```java
class Solution {
    public int maxProfit(int[] prices) {
        //这里是完全米有更新
        //本质上是一个动态规划的问题
        //如果当前天的价格-前边的最小值>前一天的最大利润，那么就进行更新 否则还是前一天最大价格
        //然后返回最大值就可以
        int mined=prices[0];
        int maxed=Integer.MIN_VALUE;
        int[] res=new int[prices.length];//表示第几天的最大价格
        for(int i=1;i<prices.length;i++){
            res[i]=Math.max(prices[i]-mined,res[i-1]);
            mined=Math.min(mined,prices[i]);
        }
        for(int i=0;i<res.length;i++){
            maxed=Math.max(maxed,res[i]);
        }
        return maxed;


    }
}
```



优化的思路问题好不好

- 不需要最后再遍历一遍找最大值，只是需要中间有东西记录就可以，最后输出出来

- dp数组其实也没有必要要了这样



#### 优化后版本

```java
class Solution {
    public int maxProfit(int[] prices) {
        //本质上是一个动态规划的问题
        //如果当前天的价格-前边的最小值>前一天的最大利润，那么就进行更新 否则还是前一天最大价格
        //然后返回最大值就可以
        int mined=prices[0];
        int maxed=0;
        //int[] res=new int[prices.length];//表示第几天的最大价格
        for(int i=1;i<prices.length;i++){
            maxed=Math.max(prices[i]-mined,maxed);
            mined=Math.min(mined,prices[i]);
        }
        return maxed;

    }
}
```



### 有效地括号(思路比较easy)

```java
class Solution {
    public boolean isValid(String s) {
        //这个题确实 忘记了
        //但是音乐的想着使用单调栈来使用
        //如果在左边那么就进去，遇到这个相同的就进行弄出来
        //最后应该是空的栈
        //但是注意,还要进行特殊字符的辨别
        HashMap<Character,Character> res=new HashMap<>();
        Stack<Character> stack=new Stack<>();
        res.put(')','(');
        res.put(']','[');
        res.put('}','{');
        for(Character ss:s.toCharArray()){
            if(!stack.isEmpty()&&res.get(ss)==stack.peek()){
                stack.pop();
            }
            else stack.push(ss);
        }
        return stack.size()==0;
   
    }
}
```

### ==最长子串（不要忘记和第一个进行比较==）

```java
class Solution {
    public int maxSubArray(int[] nums) {
        //动态规划
        //需要看前边的值是否是负数，如果是那么就直接甩掉就可以
        if(nums.length==1)return nums[0];
        int[] dp=new int[nums.length];
        dp[0]=nums[0];
        int maxed=Integer.MIN_VALUE;
        for(int i=1;i<nums.length;i++){
            dp[i]=(dp[i-1]<=0)?nums[i]:nums[i]+dp[i-1];
            maxed=Math.max(dp[i],maxed);
        }
        return Math.max(maxed,dp[0]);
    }
}
```

### 字符串转整数

#### [解法一](https://leetcode-cn.com/problems/string-to-integer-atoi/solution/java-yi-dong-yi-jie-xiao-lu-gao-by-spirit-9-27/)



##### 不好在于，可能会出现越界行为，但是又不能用long来保存

![image-20210308170825690](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210308170825690.png)

```java
//用long来存储

class Solution {
public int myAtoi(String str) {
        str = str.trim();
        if (str == null || str.length() == 0)
            return 0;

        char firstChar = str.charAt(0);
        int sign = 1;
        int start = 0;
        long res = 0;
        if (firstChar == '+') {
            sign = 1;
            start++;
        } else if (firstChar == '-') {
            sign = -1;
            start++;
        }

        for (int i = start; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return (int) res * sign;
            }//不是数字，进行截断
            res = res * 10 + str.charAt(i) - '0';//正常的
            //数据超过界限
            if (sign == 1 && res > Integer.MAX_VALUE)
                return Integer.MAX_VALUE;
            //超过界限
            if (sign == -1 && res > Integer.MAX_VALUE)
                return Integer.MIN_VALUE;
        }
        return (int) res * sign;
    }


}
```



#### 改进方法

![image-20210308170949850](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210308170949850.png)

```java

//用long来存储

class Solution {
public int myAtoi(String str) {
        str = str.trim();
        if (str == null || str.length() == 0)
            return 0;

        char firstChar = str.charAt(0);
        int sign = 1;
        int start = 0;
        int res = 0;
        if (firstChar == '+') {
            sign = 1;
            start++;
        } else if (firstChar == '-') {
            sign = -1;
            start++;
        }

        for (int i = start; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return (int) res * sign;
            }//不是数字，进行截断
            if(res>(Integer.MAX_VALUE-(str.charAt(i)-'0'))/10){
            if (sign == 1 )
                return Integer.MAX_VALUE;
            //超过界限
            if (sign == -1 )
                return Integer.MIN_VALUE;

            }
            res = res * 10 + str.charAt(i) - '0';//正常的
            //数据超过界限

        }
        return res * sign;
    }


}
```



### 最长公共子串（数字版）

```java
class Solution{
public int findLength(int[] A, int[] B) {
    if(A.length==0||B.length==0||B==null||A==null) return 0;
    int[][] res=new int[A.length+1][B.length+1];
    int maxed=-1;
    for(int i=1;i<=A.length;i++){
        for(int j=1;j<=B.length;j++){
            if(A[i-1]==B[j-1]) res[i][j]=res[i-1][j-1]+1;
            else res[i][j]=0;
            maxed=Math.max(maxed,res[i][j]);
        }
    }
    return maxed;
}
}
```

#### 参考（[最长公共子串](https://mp.weixin.qq.com/s?__biz=MzU0ODMyNDk0Mw==&mid=2247486892&idx=1&sn=4d4c122bf5139ba711b53e9ffd208408&chksm=fb419e8ccc36179a7518796a1339d348ef7786b89c8cc62ec26e9f5bc1c3ec5eb6e68a44e84d&scene=21#wechat_redirect)）





### [旋转字符串](https://leetcode-cn.com/problems/spiral-matrix/solution/cxiang-xi-ti-jie-by-youlookdeliciousc-3/)

```java
class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        //每一次遍历都进行确认看是不是结束了就可以啦
        if(matrix==null) return null;
        List<Integer> res=new ArrayList<>();
        int top=0,down=matrix.length-1,left=0,right=matrix[0].length-1;
        while(true){
            //从左到右
            for(int i=left;i<=right;i++) res.add(matrix[top][i]);
            if(++top>down) break;
            for(int j=top;j<=down;j++) res.add(matrix[j][right]);
            if(--right<left) break;
            for(int k=right;k>=left;k--) res.add(matrix[down][k]);
            if(--down<top) break;
            for(int t=down;t>=top;t--) res.add(matrix[t][left]);
            if(++left>right) break;
        }
        return res;


    }
}
```





### ==[翻转链表(头插法)](https://leetcode-cn.com/problems/reverse-linked-list-ii/solution/java-shuang-zhi-zhen-tou-cha-fa-by-mu-yi-cheng-zho/)==

思路：1、我们定义两个指针，分别称之为g(guard 守卫)和p(point)。
我们首先根据方法的参数m确定g和p的位置。将g移动到第一个要反转的节点的前面，将p移动到第一个要反转的节点的位置上。我们以m=2，n=4为例。

![1.png](https://pic.leetcode-cn.com/5389db651086bd4bcd42dd5c4552f180b553a9b204cfc1013523dfe09beac382-1.png)

2、将p后面的元素删除，然后添加到g的后面。也即头插法。

![2.png](https://pic.leetcode-cn.com/db22bdb60035e45f8c354b3f45f2a844260d6cafcf81528d2c4f1b51e484fb45-2.png)


3、根据m和n重复步骤（2）
4、返回dummyHead.next

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode reverseBetween(ListNode head, int m, int n) {
        //新建一个虚拟节点
        ListNode dummy=new ListNode(0);
        dummy.next=head;
        ListNode pre=dummy;//之前
        ListNode cur=dummy.next;//当前
        int pp=m;
        while(pp-1>0){
            cur=cur.next;
            pre=pre.next;
            pp--;
        }
        //此时cur位置是第一个要移动的位置
        //pre是其前边的位置

        ListNode removed=null;//存储准备头插的节点

        //使用头插法，把从第二个开始的节点往前不断移动
        for(int i=0;i<n-m;i++){//移动n-m次
            removed=cur.next;//准备头插的节点,注意p节点相当于不动
            cur.next=cur.next.next;//移走一个，p节点的下一个节点应该是下下个了
            //下边对头插的节点来明确其头结点和尾巴节点
            removed.next=pre.next;//明确尾巴节点，插在pre之后
            pre.next=removed;//明确头结点就是pre节点



        }
        return dummy.next;


    }
}

```



### ==岛屿数量问题==

```java
class Solution {
    public int numIslands(char[][] grid) {
        //岛屿数量，四个角DFS
        //一旦碰见==1 的 全部置为1，这样遍历之后一个岛屿只能分开为

        int count=0;
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[0].length;j++){
                if(grid[i][j]=='1'){
                    count++;
                    dfs(grid,i,j);//意义就是1、置1 2、进行遍历
                }
                else continue;//么有事儿的话直接拜拜就可以
            }
        }
        return count;


    }


    public boolean inArea(char[][] grid,int i,int j){
        return i>=0&&i<grid.length&&j>=0&&j<grid[0].length;
    }

    public void dfs(char[][] grid,int i,int j){
        if(!inArea(grid,i,j)||grid[i][j]=='0') return;//超出范围或者不是这个岛屿范围，就可以跳出来了
        grid[i][j]='0';
        dfs(grid,i+1,j);
        dfs(grid,i,j-1);
        dfs(grid,i,j+1);
        dfs(grid,i-1,j);

    }
}
```

### 岛屿周长问题

```java
class Solution {
    public int islandPerimeter(int[][] grid) {
        //目的就是应该是遍历
        //遇到海水（应该加一） 遇到边界 （应该加一）
        //因为只有一个岛屿
        //直接返回递归结果就可以
        //return dfs(1)+dfs(2)+dfs(3)+dfs(4)
        int count=0;
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[0].length;j++){
                if(grid[i][j]==1){
                    count=dfs(grid,i,j);//意义就是1、置1 2、进行遍历
                }
                else continue;//么有事儿的话直接拜拜就可以
            }
        }
        return count;


    }


    public boolean inArea(int[][] grid,int i,int j){
        return i>=0&&i<grid.length&&j>=0&&j<grid[0].length;
    }

    public int dfs(int[][] grid,int i,int j){
        if(!inArea(grid,i,j)) return 1;
        if(grid[i][j]==0) return 1;

        //难点来了，注意当grid==2 的时候也需要直接跳出
        if(grid[i][j]==2) return 0;

        grid[i][j]=2;//别忘记，标记遍历过
        return dfs(grid,i+1,j)+
        dfs(grid,i,j-1)+
        dfs(grid,i,j+1)+
        dfs(grid,i-1,j);

    }

    }
```

### 最大岛屿

```java
class Solution {
    public int maxAreaOfIsland(int[][] grid) {

 //目的就是应该是遍历
 //DFS就是计算当前所在位置的岛屿的面积
 //有一个中间量来进行存储最大值
        if(grid==null||grid.length==0||grid[0].length==0) return 0;
        int maxed=0;
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[0].length;j++){
                if(grid[i][j]==1){
                    maxed=Math.max(dfs(grid,i,j),maxed);//意义就是1、置1 2、进行遍历
                }
                else continue;//么有事儿的话直接拜拜就可以
            }
        }
        return maxed;


    }


    public boolean inArea(int[][] grid,int i,int j){
        return i>=0&&i<grid.length&&j>=0&&j<grid[0].length;
    }

    public int dfs(int[][] grid,int i,int j){
        if(!inArea(grid,i,j)) return 0;
        if(grid[i][j]==0) return 0;

        //难点来了，注意当grid==2 的时候也需要直接跳出
        if(grid[i][j]==2) return 0;

        grid[i][j]=2;//别忘记，标记遍历过
        return 1+dfs(grid,i+1,j)+
        dfs(grid,i,j-1)+
        dfs(grid,i,j+1)+
        dfs(grid,i-1,j);

    }

    }

```



### ==搜索旋转排序数组（一看到就想到二分）==

```java
class Solution {
    public int search(int[] nums, int target) {
        //查明是左边有序还是右边有序就可以
        int l=0,r=nums.length-1;
        while(l<r){
            int mid=l+(r-l)/2;
            //直接得到结果
            if(nums[mid]==target){
                return mid;
            }
            //说明这个真实值一定是在mid左边或者右边，二分的思想就是可以分清楚左边还是右边
            if(nums[mid]>nums[l]){
                //说明是左边是有序的，可以使用二分，根据有序，左边是大的有序，右边是另外情况
                if(nums[l]<=target && target<nums[mid]){
                    r=mid;
                }
                else l=mid+1;
            }
            //说明右边有序,看看是不是在右边，否则在右边
            else{
                if(nums[mid+1]<=target && target<=nums[r]){
                    l=mid+1;
                }
                else r=mid;
            }
            
            
        }
        int res=nums[l]==target?l:-1;
        return res;


    }
}
```



### ==最长回文子串（[DP](https://leetcode-cn.com/problems/longest-palindromic-substring/solution/zhong-xin-kuo-san-dong-tai-gui-hua-by-liweiwei1419/)）==

注意，动态规划一定是需要递推，因此需要进行有先有后的进行遍历

![image-20210311212859465](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210311212859465.png)

![image-20210311212939866](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210311212939866.png)

#### 必须先知道左下才能，所以不能无脑那种递推，因此可以使用下边的第三种递推
![image-20210311212956854](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210311212956854.png)

```java
class Solution {
    public String longestPalindrome(String s) {
        //动态规划比较合理
        //dp[i][j]判断是否是回文字符串，包括i到j
        //dp[i][j]递推关系，就是s[i]==s[j] dp[i][j]=dp[i+1][j-1]
        //一些初始条件 
        //1、i=j时候为true
        //2、s[i]==s[j]并且j-i<3时候 一定为true
        //3、dp[i][j]递推关系，就是s[i]==s[j]时候（j-i>3时候） dp[i][j]=dp[i+1][j-1]
        //4、s[i]!=s[j] 直接为false
        int left=0;
        int maxlen=-1;

        boolean[][] res=new boolean[s.length()][s.length()];//默认是这个false
        char[] ss=s.toCharArray();
        //for(int t=0;t<s.length();t++) res[t][t]=true;

         for (int j = 0; j < s.length(); j++) {
            for (int  i= 0; i <= j; i++){
                if(ss[i]==ss[j]){
                    if(j-i<3){
                        res[i][j]=true;                        
                    }
                    else{
                        res[i][j]=res[i+1][j-1];
                    }
                }
                else res[i][j]=false;
                if(res[i][j]==true){
                    //这里是要保存下来最长字符串的长度的同时需要保存下最左边下标
                    if(j-i+1>maxlen){
                        maxlen=j-i+1;//最大长度
                        left=i;//zui
                    }

                }
            }
        }
        return s.substring(left,left+maxlen);      

    }
}
```





## ==题目地址(46. 全排列)==

https://leetcode-cn.com/problems/permutations/

## 题目描述

```
给定一个 没有重复 数字的序列，返回其所有可能的全排列。

示例:

输入: [1,2,3]
输出:
[
  [1,2,3],
  [1,3,2],
  [2,1,3],
  [2,3,1],
  [3,1,2],
  [3,2,1]
]
```

## 前置知识

- 回溯 dfs

## 公司

- 美团

## 思路

```java
    //思路很清楚，就是递归+撤销=回溯
    //重点是画图，这样的话比较好理解
    //每一个节点都是对所有的nums里边的数据进行辨别然后不满足条件就出来
    //别忘记撤销当前选择
    //跳出的条件是什么，path的长度到了就跳出就可以
```
## ==关键点==

-  ![image-20210318160544225](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/image-20210318160544225.png)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    List<Integer> path =new ArrayList<>();//记录路径
    //List<List<Integer>> res =new ArrayList<List<Integer>>();//这里使用的是泛型
    List<List<Integer>> res =new ArrayList<>();//无论多么复杂，一定是只有一个<>
    public List<List<Integer>> permute(int[] nums) {
        //思路很清楚，就是递归+撤销=回溯
        //重点是画图，这样的话比较好理解
        //每一个节点都是对所有的nums里边的数据进行辨别然后不满足条件就出来
        //别忘记撤销当前选择
        //跳出的条件是什么，path的长度到了就跳出就可以
        dfs(path,nums);
        return res;
        
        
        //HashMap<Integer,Boolean> map=new HashMap<>();
       

    }
    public void dfs(List<Integer> path,int[] nums){
        if(path.size()==nums.length){
            res.add(new ArrayList<>(path));
            return;
        } //长度到了直接跳出去
        for(Integer num:nums){
            if(path.contains(num)) continue;//有了就下一个循环,也可以用hashmap
            path.add(num);
            dfs(path,nums);
            path.remove(num);

        }
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(nlogn)$
- 空间复杂度：$O(nlogn)$





## 题目地址(113. 路径总和 II)

https://leetcode-cn.com/problems/path-sum-ii/

## 题目描述

```
给你二叉树的根节点 root 和一个整数目标和 targetSum ，找出所有 从根节点到叶子节点 路径总和等于给定目标和的路径。

叶子节点 是指没有子节点的节点。

 

示例 1：

输入：root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
输出：[[5,4,11,2],[5,8,4,5]]


示例 2：

输入：root = [1,2,3], targetSum = 5
输出：[]


示例 3：

输入：root = [1,2], targetSum = 0
输出：[]


 

提示：

树中节点总数在范围 [0, 5000] 内
-1000 <= Node.val <= 1000
-1000 <= targetSum <= 1000
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  回溯
-  java 是引用，不是拷贝副本,如果按照Python那种来弄的话，形参和实参没有什么关系

## 代码


- 语言支持：Java

Java Code:
### 自己的思路（修正）

```java
class Solution {
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        //思路比较easy dfs
        //跳出就是叶子节点并且path的和为target,并且返回
        //一定注意 java是引用传递
        //用path的话，res加的应该是一个new的
        //不然全都是一个path
       
        int sum= targetSum;
        List<List<Integer>> res =new ArrayList<>();
         if(root==null) return res;
        List<Integer> path=new ArrayList<>();
        dfs(root,sum,path,res);
        return res;

    }
    public void dfs(TreeNode root,int sum,List<Integer> path,List<List<Integer>> res){
        if(root==null) return;
        sum-=root.val;
        path.add(root.val);
        if(root.left==null&&root.right==null&&sum==0){
        res.add(new ArrayList<>(path));
        return ; 
        }
 
        //targetSum-=root.val;
        if(root.left!=null){
        dfs(root.left,sum,path,res);
        path.remove(path.size()-1);}
        if(root.right!=null){
        dfs(root.right,sum,path,res);
        path.remove(path.size()-1);}
    }
}
```

### 正确版本
``` java

class Solution {
    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> result=new ArrayList();
        if (root==null) return result;
        List<Integer> path=new ArrayList();
        return findPath(root,sum,result,path);
    }
    private List<List<Integer>> findPath(TreeNode root,int sum,List<List<Integer>> result,List<Integer> path){
        sum-=root.val;
        path.add(root.val);
        if (sum==0 && root.left==null && root.right==null){
            result.add(new ArrayList(path));//注意一定是要新建一个，否则path都是一个地址，最后path都一致了。
            //回溯的意义也是这个，如果是不变的话，左遍历完了之后，会影响后边右边
            return result;
        }
        if (root.left!=null){
            result=findPath(root.left,sum,result,path);//递归访问左子树
            path.remove(path.size()-1);//此时添加了当前节点的左子树到路径中,要往右子树寻找时,要先删除这个左节点
        }
        if (root.right!=null){
            result=findPath(root.right,sum,result,path);
            path.remove(path.size()-1);
        }
        return result;
    }
}

```



**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$



## ==题目地址(112. 路径总和)==

https://leetcode-cn.com/problems/path-sum/

## 题目描述

```
给你二叉树的根节点 root 和一个表示目标和的整数 targetSum ，判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum 。

叶子节点 是指没有子节点的节点。

 

示例 1：

输入：root = [5,4,8,11,null,13,4,7,2,null,null,null,1], targetSum = 22
输出：true


示例 2：

输入：root = [1,2,3], targetSum = 5
输出：false


示例 3：

输入：root = [1,2], targetSum = 0
输出：false


 

提示：

树中节点的数目在范围 [0, 5000] 内
-1000 <= Node.val <= 1000
-1000 <= targetSum <= 1000
```

## 前置知识

- 

## 公司

- 暂无

## 思路
 定义一个全局关键点，如果满足就值为true，否则为false不变

## 关键点
 要放在全局当中，用private来修饰

-  

## 代码

- 语言支持：Java

Java Code:

```java

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    boolean flag=false;
    public boolean hasPathSum(TreeNode root, int targetSum) {
        //定义一个sum，每一次都减去节点值，遇到合适的就返回就可以
        if(root==null) return false;
        int sum=targetSum;
        dfs(root,sum);
        return flag;
    }
    public void dfs(TreeNode root,int sum){
        if(root==null) return ;//不会有走到最后的情况，因为到了叶子节点就返回了
        sum-=root.val;
        if(sum==0&&root.left==null&&root.right==null) 
        {
            flag=true;

        }
        dfs(root.left,sum);
        dfs(root.right,sum);
    }
}

```

## 方法2

``` java
class Solution {
    public boolean hasPathSum(TreeNode root, int targetSum) {
        //想来就是每一次遍历的时候就有一个值来做标志就可以
        //到了叶子节点的时候，判断一下这个值是否为0
        //flag=targetSum;
        return dfs(root,targetSum);

    }
    public boolean dfs(TreeNode root,int value){
        //慢慢分析一定会出来
        if(root==null) return false;//因为一定要进行判断是否为叶子节点或者是为空
        value-=root.val;
        if(root.left==null&&root.right==null) return value==0;
        return dfs(root.left,value)||dfs(root.right,value);

    }
}
```

**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$



## 题目地址(56. 合并区间)

https://leetcode-cn.com/problems/merge-intervals/

## 题目描述

```
以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 。请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。

 

示例 1：

输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
输出：[[1,6],[8,10],[15,18]]
解释：区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].


示例 2：

输入：intervals = [[1,4],[4,5]]
输出：[[1,5]]
解释：区间 [1,4] 和 [4,5] 可被视为重叠区间。

 

提示：

1 <= intervals.length <= 104
intervals[i].length == 2
0 <= starti <= endi <= 104
```

## 前置知识

- 

## 公司

- 暂无

## 思路
- 其实还是比较easy，按照端点排序，然后和前边比较，是否有了重叠
- 是否要进入Arraylist，或者是更新右端点

## 关键点
- 泛型内部 List<int[]>,因为里边可以有多重数据类型


-  ==toArray 参数 要穿进去一个`new int[维度要已知][]`==

## 代码

- 语言支持：Java

Java Code:

### 按照最左边端点排序

```java

class Solution {
    public int[][] merge(int[][] intervals) {
        //关于这个区间的一看就是先试用排序，然后进行分析就可以
        //要记住常用的api
        //比如说是toArray，这种Api等等
        //如果小于一个，直接排序返回
        if(intervals.length<2) return intervals;
        //int maxed=0;
        List<int[]> res=new ArrayList<>();//关键点,list集合里边应该是int元素啊
        Arrays.sort(intervals,(o1,o2)->o1[0]-o2[0]);//按照第一个元素升序排列
        for(int i=0;i<intervals.length;i++){
            if(i==0||intervals[i][0]>res.get(res.size()-1)[1]){
                res.add(intervals[i]);
            }
            else{
                res.get(res.size()-1)[1]=Math.max(res.get(res.size()-1)[1],intervals[i][1]);
                
                
            }
        }
        return res.toArray(new int[res.size()][]);
        
        

    }
}

```



**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$



## 题目地址(155. 最小栈)

https://leetcode-cn.com/problems/min-stack/

## 题目描述

```
设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。

push(x) —— 将元素 x 推入栈中。
pop() —— 删除栈顶的元素。
top() —— 获取栈顶元素。
getMin() —— 检索栈中的最小元素。

 

示例:

输入：
["MinStack","push","push","push","getMin","pop","top","getMin"]
[[],[-2],[0],[-3],[],[],[],[]]

输出：
[null,null,null,null,-3,null,0,-2]

解释：
MinStack minStack = new MinStack();
minStack.push(-2);
minStack.push(0);
minStack.push(-3);
minStack.getMin();   --> 返回 -3.
minStack.pop();
minStack.top();      --> 返回 0.
minStack.getMin();   --> 返回 -2.


 

提示：

pop、top 和 getMin 操作总是在 非空栈 上调用。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

 需要注意的是，每一个pop的时候需要看一看是不是栈顶元素，如果是的话要一起弹出，否则下一次就会得到错误最小值

## 关键点

- 整数数值比较要用equals比较合理，因为equals比较的是字面值，而1256这个数据的话，如果用==比较就会是false，得不到想要的结果数值大小不是new出来的噢， ==是判断地址没错，但是当你数的大小在-128到127这个区间内，会指向同一个地址，返回true。


-  每一个pop的时候需要看一看是不是栈顶元素，如果是的话要一起弹出，否则下一次就会得到错误最小值
-  最小栈更新的条件有两个，一个是为空，另外一个是当前值大于栈顶元素

## 代码

- 语言支持：Java

Java Code:

```java

class MinStack {

    /** initialize your data structure here. */
    //本质上就是两个栈，一个保存数据
    //一个保存的最小值
    //需要注意的是，每一个pop的时候需要看一看是不是栈顶元素，如果是的话要一起弹出，否则下一次就会得到错误最小值
    private Stack<Integer> data;
    private Stack<Integer> min_stack;
    public MinStack() {
    data=new Stack<>();
    min_stack=new Stack<>();

    }
    
    public void push(int val) {
        data.push(val);
        if(min_stack.isEmpty()||val<=min_stack.peek()) min_stack.push(val);

    }
    
    public void pop() {
        if(data.pop()==min_stack.peek()) min_stack.pop();
 

    }
    
    public int top() {
         return data.peek();


    }
    
    public int getMin() {
         return min_stack.peek();


    }
}

/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(val);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.getMin();
 */

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$



## 题目地址(300. 最长递增子序列)

https://leetcode-cn.com/problems/longest-increasing-subsequence/

## 题目描述

```java
给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。

子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。例如，[3,6,2,7] 是数组 [0,3,1,6,2,2,7] 的子序列。

 

示例 1：

输入：nums = [10,9,2,5,3,7,101,18]
输出：4
解释：最长递增子序列是 [2,3,7,101]，因此长度为 4 。


示例 2：

输入：nums = [0,1,0,3,2,3]
输出：4


示例 3：

输入：nums = [7,7,7,7,7,7,7]
输出：1


 

提示：

1 <= nums.length <= 2500
-104 <= nums[i] <= 104

 

进阶：

你可以设计时间复杂度为 O(n2) 的解决方案吗？
你能将算法的时间复杂度降低到 O(n log(n)) 吗?
```

## 前置知识

- 

## 公司

- 美团

## 思路

        //明显使用这个动态规划
        //注意是序列，不是一直的那种连续的过程问题
        //dp[i]是到前i个数目当中的最大子序列，但是这样无法来找递推关系，因此需要来找变换意义
        //dp[i]是以nums[i]为结尾的最长递增子序列
        //因为i之前会有很多不同的子序列，所以应该是更新这个maxed最大值
        //for j in range(0,i) dp[i]=max(dp[j]+1,dp[i]) if(nums[i]>nums[j])
## 关键点

![image-20210324134959782](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/image-20210324134959782.png)

![image-20210324135048973](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/image-20210324135048973.png)

       //如何想到这个递推关系呢？还是需要多练

-  

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int lengthOfLIS(int[] nums) {
        //明显使用这个动态规划
        //注意是序列，不是一直的那种连续的过程问题
        //dp[i]是到前i个数目当中的最大子序列，但是这样无法来找递推关系，因此需要来找变换意义
        //dp[i]是以nums[i]为结尾的最长递增子序列
        //因为i之前会有很多不同的子序列，所以应该是更新这个maxed最大值
        //for j in range(0,i) dp[i]=max(dp[j]+1,dp[i]) if(nums[i]>nums[j])
        int maxed=1;
        int[] dp=new int[nums.length];
        Arrays.fill(dp,1);
        for(int i=0;i<nums.length;i++){
            for(int j=0;j<i;j++){
                if(nums[i]>nums[j]){
                    dp[i]=Math.max(dp[i],dp[j]+1);
                }//就应该比dp[j]要大，变成dp[j]+1 要找出最大的dp[j]+1
                maxed=Math.max(dp[i],maxed);
            }
        }
        return maxed;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(2^n)$
- 空间复杂度：$O(2^n)$

## 题目地址(42. 接雨水)

https://leetcode-cn.com/problems/trapping-rain-water/

## 题目描述

```
给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。

 

示例 1：

输入：height = [0,1,0,2,1,0,1,3,2,1,2,1]
输出：6
解释：上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。 


示例 2：

输入：height = [4,2,0,3,2,5]
输出：9


 

提示：

n == height.length
0 <= n <= 3 * 104
0 <= height[i] <= 105
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  

## 代码

- 语言支持：Java

Java Code:


### 比价好想的方法
```java

class Solution{
public int trap(int[] height) {
    int sum = 0;
    //最两端的列不用考虑，因为一定不会有水。所以下标从 1 到 length - 2
    for (int i = 1; i < height.length - 1; i++) {
    这里有一个点，每一次需要重新的进行新建变量好不好，防治污染
        int max_left = 0;
        //找出左边最高
        for (int j = i - 1; j >= 0; j--) {
            if (height[j] > max_left) {
                max_left = height[j];
            }
        }
        int max_right = 0;
        //找出右边最高
        for (int j = i + 1; j < height.length; j++) {
            if (height[j] > max_right) {
                max_right = height[j];
            }
        }
        //找出两端较小的
        int min = Math.min(max_left, max_right);
        //只有较小的一段大于当前列的高度才会有水，其他情况不会有水
        if (min > height[i]) {
            sum = sum + (min - height[i]);
        }
    }
    return sum;
}
}

```

### 进阶方法(使用动态规划，提前求出每一个点左边的最大值和右边的最大值储存起来好不好）
```java
class Solution{
public int trap(int[] height) {
    //思路比较清晰，但是复杂度比较高，所以说学一个这个，在学一个效率高一点就可以
    int sum=0;
    int[] max_left=new int [height.length];//从第二个到倒数第二个就乐意，第一个和倒数第一个存不住水
    int[] max_right=new int [height.length];
    //这个要明确地意义就是,转移方程如何写，max_left[i]表示第i个元素左边的最大值
    //max_left[i]=max(max_left[i-1],height[i-1]) 因为max_left[i-1]不包含height[i-1]
    //同理右边
    
    for(int i=1;i<height.length-1;i++){
        max_left[i]=Math.max(max_left[i-1],height[i-1]);

    }
    for(int j=height.length-2;j>=1;j--){
        max_right[j]=Math.max(max_right[j+1],height[j+1]);
    }

    for(int i=1;i<height.length-1;i++){
        if(Math.min(max_right[i],max_left[i])>height[i]){
            sum+=(Math.min(max_right[i],max_left[i])-height[i]);
        }
    }
    return sum;

}
}
```

### 进阶方法(使用更优化的动态规划，双指针）
```java
class Solution{
public int trap(int[] height) {
    //思路比较清晰，但是需要提前算出来这个左边和右边的最大值，不是很优雅好不好
    //可不可以一遍遍历直接进行比较
    //因为我们需要的就只是知道我的左边和右边谁更大就可以
    //转移方程如何写，max_left[i]表示第i个元素左边的最大值
    //max_left[i]=max(max_left[i-1],height[i-1]) 因为max_left[i-1]不包含height[i-1]

    //双指针做法 left 和 right  代表当前遍历到那个列

    //现在当前目光在left列和right列上

    //看着这个转移方程可以看到，左边最大值，是由height[left-1]不断比较出来的，如果这个时候right列右边最高的列比左指针left左边的最高列要，那么一定是左边的更高的墙更低

    //这样就直接判断就可以啦
    int sum=0;//结果
    int left=1;//保存的是左边当前最高点的坐标下标，左指针
    int right=height.length-2;//保存的是右边当前最高点的坐标下标，右指针
    int max_left=0;//左边最高点
    int max_right=0;//右边最高点

    for(int i=1;i<height.length-1;i++){
        if(height[left-1]<height[right+1]){//如果这个时候右边的指针所保存的值>左指针求出来的值，那么一定是左边的更高的墙更低
            max_left=Math.max(max_left,height[left-1]);//但是需要进行求出这个值是多少，就是不断地比较就是啦
            if(max_left>height[left]) sum+=(max_left-height[left]);
            left++;//为什么要往右边走，因为只要左边的值比右边的小，就要往右移动，可能下一个就会局势反转

        }
        else{//如果这个时候右边的指针所保存的值<左指针求出来的值，那么一定是右边的更高的墙更低
            max_right=Math.max(max_right,height[right+1]);//但是需要进行求出这个值是多少，就是不断地比较就是啦
            if(max_right>height[right]) sum+=(max_right-height[right]);
            right--;//为什么要往右边走，因为只要左边的值比右边的小，就要往右移动，可能下一个就会局势反转

        }
        
  
    }
    return sum;
}
}

```

**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$



## 题目地址(234. 回文链表)

https://leetcode-cn.com/problems/palindrome-linked-list/

## 题目描述

```
请判断一个链表是否为回文链表。

示例 1:

输入: 1->2
输出: false

示例 2:

输入: 1->2->2->1
输出: true


进阶：
你能否用 O(n) 时间复杂度和 O(1) 空间复杂度解决此题？
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  加入伪节点
-  注意断开之后的操作

## 代码

- 语言支持：Java

Java Code:

```java

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public boolean isPalindrome(ListNode head) {
        //先断开，一定要使用伪节点
        //翻转无敌
        if(head!=null&&head.next==null) return true;
        ListNode dummy=new ListNode(-1);
        dummy.next=head;
        ListNode slow=dummy;
        ListNode fast=dummy;
        while(fast!=null&&fast.next!=null){
            fast=fast.next.next;
            slow=slow.next;
        }
        ListNode cur=slow.next;
        slow.next=null;

        ListNode myfront=dummy.next;
        
        ListNode pre=null;
        ListNode next=null;

        while(cur!=null){
            next=cur.next;
            cur.next=pre;
            pre=cur;
            cur=next;
        }
        while(pre!=null){
            if(pre.val!=myfront.val) return false;
            pre=pre.next;
            myfront=myfront.next;
        }
        return true;
        

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(151. 翻转字符串里的单词)

https://leetcode-cn.com/problems/reverse-words-in-a-string/

## 题目描述

```
给定一个字符串，逐个翻转字符串中的每个单词。

说明：

无空格字符构成一个 单词 。
输入字符串可以在前面或者后面包含多余的空格，但是反转后的字符不能包括。
如果两个单词间有多余的空格，将反转后单词间的空格减少到只含一个。

 

示例 1：

输入："the sky is blue"
输出："blue is sky the"


示例 2：

输入："  hello world!  "
输出："world! hello"
解释：输入字符串可以在前面或者后面包含多余的空格，但是反转后的字符不能包括。


示例 3：

输入："a good   example"
输出："example good a"
解释：如果两个单词间有多余的空格，将反转后单词间的空格减少到只含一个。


示例 4：

输入：s = "  Bob    Loves  Alice   "
输出："Alice Loves Bob"


示例 5：

输入：s = "Alice does not even like bob"
输出："bob like even not does Alice"


 

提示：

1 <= s.length <= 104
s 包含英文大小写字母、数字和空格 ' '
s 中 至少存在一个 单词

 

进阶：

请尝试使用 O(1) 额外空间复杂度的原地解法。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  

## 代码

- 语言支持：Java

Java Code:

```java
class Solution {
    public String reverseWords(String s) {
        //先去空格
        //然后按照" "来进行分割
        //然后进行从后往前遍历
        //注意这个API的具体使用好不好
        String[] res=s.trim().split(" ");
        StringBuilder ss=new StringBuilder();
        for(int i=res.length-1;i>=0;i--){
            if(res[i].equals("")) continue;
            ss.append(res[i]+" ");
            
        }
        return ss.toString().trim();



    }
}
```

### 方法2

```java
class Solution {
    public String reverseWords(String s) {
        //直接用双指针
        //双指针可以进行框定这个单词的位置
        //可以把数据放进来就可以
        s.trim();
        int left=s.length()-1;//代表单词左边
        int right=s.length()-1;//代表单词右边
        StringBuilder res=new StringBuilder();
        while(left>=0){
            //框住元素
            while(left>=0&&s.charAt(left)!=' ') left--;
            res.append(s.substring(left+1,right+1)+" ");
            //跳过空元素
            while(left>=0&&s.charAt(left)==' ') left--;
            right=left;
        }
        return res.toString().trim();
        
        



    }
}
```

**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(124. 二叉树中的最大路径和)

https://leetcode-cn.com/problems/binary-tree-maximum-path-sum/

## 题目描述

```
路径 被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中 至多出现一次 。该路径 至少包含一个 节点，且不一定经过根节点。

路径和 是路径中各节点值的总和。

给你一个二叉树的根节点 root ，返回其 最大路径和 。

 

示例 1：

输入：root = [1,2,3]
输出：6
解释：最优路径是 2 -> 1 -> 3 ，路径和为 2 + 1 + 3 = 6

示例 2：

输入：root = [-10,9,20,null,null,15,7]
输出：42
解释：最优路径是 15 -> 20 -> 7 ，路径和为 15 + 20 + 7 = 42


 

提示：

树中节点数目范围是 [1, 3 * 104]
-1000 <= Node.val <= 1000
```

## 前置知识

- 

## 公司

- 暂无

## 思路

- //这个函数的意义就是求出根节点为当前节点root的路径和，这样中间一比较就可以啦

## 关键点

-  //一般来讲使用递归求解，难在每一个点都是一个路径，如何来进行界定
           //这个递归函数肯定是中间求得最大值   单独列一个
           //然后 dfs一定是完成一个递归的功能
           //要形成路径 只能选一个左子树或者右子树
           //但是如果左子树或者右子树已经是负数的话，就不用加了，是0就可以

## 代码

- 语言支持：Java

Java Code:

```java

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    private int maxed=Integer.MIN_VALUE;
    public int maxPathSum(TreeNode root) {
        //一般来讲使用递归求解，难在每一个点都是一个路径，如何来进行界定
        //这个递归函数肯定是中间求得最大值   单独列一个
        //然后 dfs一定是完成一个递归的功能
        //要形成路径 只能选一个左子树或者右子树
        //但是如果左子树或者右子树已经是负数的话，就不用加了，是0就可以
        int set=dfs(root);
        return maxed;


    }
    public int dfs(TreeNode root){//这个函数的意义就是求出根节点为当前节点root的路径和，这样中间一比较就可以啦
        if(root==null) return 0;
        int left=Math.max(dfs(root.left),0);
        int right=Math.max(dfs(root.right),0);

        maxed=Math.max(left+root.val+right,maxed);

        return root.val+Math.max(left,right);//只能是一面
    }
    
    }

```

**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(4. 寻找两个正序数组的中位数)

https://leetcode-cn.com/problems/median-of-two-sorted-arrays/

## 题目描述

![image-20210325164325355](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/image-20210325164325355.png)

```
给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的 中位数 。

 

示例 1：

输入：nums1 = [1,3], nums2 = [2]
输出：2.00000
解释：合并数组 = [1,2,3] ，中位数 2


示例 2：

输入：nums1 = [1,2], nums2 = [3,4]
输出：2.50000
解释：合并数组 = [1,2,3,4] ，中位数 (2 + 3) / 2 = 2.5


示例 3：

输入：nums1 = [0,0], nums2 = [0,0]
输出：0.00000


示例 4：

输入：nums1 = [], nums2 = [1]
输出：1.00000


示例 5：

输入：nums1 = [2], nums2 = []
输出：2.00000


 

提示：

nums1.length == m
nums2.length == n
0 <= m <= 1000
0 <= n <= 1000
1 <= m + n <= 2000
-106 <= nums1[i], nums2[i] <= 106

 

进阶：你能设计一个时间复杂度为 O(log (m+n)) 的算法解决此问题吗？
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {

public double findMedianSortedArrays(int[] nums1, int[] nums2) {
    //这种思路比较easy，就是进行比较并且进行归并
    //归并排序是存在递归版本的
    //而这个一共就是两个，所以来讲就是比较大小并且进行赋值就可以啦
    //注意一点 n/2是中位数
    int m=nums1.length,n=nums2.length;
    int[] res=new int[m+n];//从存储排好序的结果

    //case1:nums1是空的
    if(m==0){
        if(n%2==0) return  (nums2[n/2-1]+nums2[n/2])/2.0;
        else return (double)nums2[n/2];
    }

    //case2：nums2是空的
    if(n==0){
        if(m%2==0) return  (nums1[m/2-1]+nums1[m/2])/2.0;
        else return (double)nums1[m/2];
    }

    //case3：比较并且把排好序的东西放到res当中，然后返回中间值
    int count=0;//作为排序好的数据的指针
    int i=0,j=0;
    while(count!=m+n){
        //case1:如果i==m说明nums1已经完全到里边了
        if(i==m){
            while(j!=n) res[count++]=nums2[j++];//需要不断地进行，所以使用while
            break;//跳出外侧循环
        }

        //case2:如果j==n说明nums2已经完全到里边了
        if(j==n){
            while(i!=m) res[count++]=nums1[i++];//需要不断地进行，所以使用while
            break;
        }

        //case3：平时的感觉比较大小并且赋值
        if(nums1[i]>nums2[j]) res[count++]=nums2[j++];
        else if(nums1[i]<=nums2[j]) res[count++]=nums1[i++];



    }
    if((m+n)%2==0) return (res[(m+n)/2-1]+res[(m+n)/2])/2.0;
    else return (double)res[(m+n)/2];



}
}



```
## 解法二

![image-20210325164300895](https://cdn.jsdelivr.net/gh/kumi123/CDN//img/image-20210325164300895.png)

```java
class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        //找到第中间数就可以
        //没有必要把排序完的数据全部弄完再计算中间数
 
        //需要两个哨兵下标 指示两个数组当前值位置 start_1 start_2
        //需要两个数 来存储中位数 因为可能是两个数平均值 top low

        int size=nums1.length+nums2.length;
        int low=-1,front=-1;
        int start_1=0,start_2=0;
        for(int i=0;i<=size/2;i++){//无论size是奇数还是偶数，经历过size/2次循环一定可以把中间数找出来
            low=front;//前边的值赋给后边
            /*如果 aStart 还没有到最后并且此时 A 位置的数字小于 B 位置的数组，那么就可以后移了。也就是aStart＜m&&A[aStart]< B[bStart]。但如果 B 数组此刻已经没有数字了，就是nums已经到达了这个末尾，值变成最大下标加一了，继续取数字 B[ bStart ]，则会越界，所以判断下 bStart 是否大于数组长度了，这样 || 后边的就不会执行了，也就不会导致错误了，所以增加为 aStart＜m&&(bStart) >= n||A[aStart]<B[bStart]) 。*/

            if(start_1<nums1.length&&(start_2>=nums2.length||nums1[start_1]<nums2[start_2])) front=nums1[start_1++];
            else front=nums2[start_2++];
            

        }
        if(size%2==0) return (front+low)/2.0;
        else return  (double)front;



    }
}
```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(148. 排序链表)

https://leetcode-cn.com/problems/sort-list/

## 题目描述

```
给你链表的头结点 head ，请将其按 升序 排列并返回 排序后的链表 。

进阶：

你可以在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序吗？

 

示例 1：

输入：head = [4,2,1,3]
输出：[1,2,3,4]


示例 2：

输入：head = [-1,5,3,4,0]
输出：[-1,0,3,4,5]


示例 3：

输入：head = []
输出：[]


 

提示：

链表中节点的数目在范围 [0, 5 * 104] 内
-105 <= Node.val <= 105
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  

## 代码

- 语言支持：Java

Java Code:

```java

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
 public ListNode sortList(ListNode head) {
        // 1、递归结束条件
        if (head == null || head.next == null) {
            return head;
        }

        // 2、找到链表中间节点并断开链表 & 递归下探
        ListNode midNode = middleNode(head);
        ListNode rightHead = midNode.next;
        midNode.next = null;

        ListNode left = sortList(head);
        ListNode right = sortList(rightHead);

        // 3、当前层业务操作（合并有序链表）
        return mergeTwoLists(left, right);
    }
    
    //  找到链表中间节点（876. 链表的中间结点）
    private ListNode middleNode(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode slow = head;
        ListNode fast = head.next.next;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    // 合并两个有序链表（21. 合并两个有序链表）
    private ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode sentry = new ListNode(-1);
        ListNode curr = sentry;

        while(l1 != null && l2 != null) {
            if(l1.val < l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }

            curr = curr.next;
        }

        curr.next = l1 != null ? l1 : l2;
        return sentry.next;
    }
}

```

### 自己的算法，还是比较简单
```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
 public ListNode sortList(ListNode head) {
     //主要思路就是找到中点 切开 然后分别对两个进行排序 最后归并在一起就可以了
     if(head==null||head.next==null) return head;
     ListNode mid=findmid(head);//找到中点
     ListNode newhead=mid.next;//找出后边端点的开始
     mid.next=null;//断开前后两部分

     //递归对两部分进行排序
     ListNode left=sortList(head);
     ListNode right=sortList(newhead);
     return merge(left,right);

     //子函数 找中点


     //子函数 合并两个有序的链表
 
}
     public ListNode findmid(ListNode head){
         if(head==null) return head;
         ListNode dummy=new ListNode(-1);
         dummy.next=head;
         ListNode fast=dummy,slow=dummy;
         while(fast!=null&&fast.next!=null){
             slow=slow.next;
             fast=fast.next.next;
         }
         return slow;
     }

     public ListNode merge(ListNode left,ListNode right){
         if(left==null||right==null) return (left==null)?right:left;
         
         if(left.val<right.val){
             left.next=merge(left.next,right);
             return left;
         }
         else right.next=merge(left,right.next);
         return right;
         
     }
}
```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$


## 题目地址(409. 最长回文串)

https://leetcode-cn.com/problems/longest-palindrome/

## 题目描述

```
给定一个包含大写字母和小写字母的字符串，找到通过这些字母构造成的最长的回文串。

在构造过程中，请注意区分大小写。比如 "Aa" 不能当做一个回文字符串。

注意:
假设字符串的长度不会超过 1010。

示例 1:

输入:
"abccccdd"

输出:
7

解释:
我们可以构造的最长的回文串是"dccaccd", 它的长度是 7。

```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-   //这个意思是是否能够构成回文子串
           //所以只要是偶数个的字符就应该加上
           //如果是奇数的哈，就应该是加上奇数个数减一，最后的回文长度只要出现了奇数那么就应该加上1
           //如果全部都是偶数个的话，就不用了加了，所以判断条件就是看最后长度是否是等于s的长度

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int longestPalindrome(String s) {
        //这个意思是是否能够构成回文子串
        //所以只要是偶数个的字符就应该加上
        //如果是奇数的哈，就应该是加上奇数个数减一，最后的回文长度只要出现了奇数那么就应该加上1
        //如果全部都是偶数个的话，就不用了加了，所以判断条件就是看最后长度是否是等于s的长度
        int[] table=new int[58];//需要存储大写字母和小写字母的ASCII码，大写字母65-90 小写字母 97-122 所以是122-65+1=58个
        for(Character ss:s.toCharArray()){
            table[ss-'A']++;
        }
        int count=0;
        for(Integer num:table){
            count+=(num-(num&1));
        }
        return (count<s.length())?count+1:count;
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$





## 题目地址(239. 滑动窗口最大值)

https://leetcode-cn.com/problems/sliding-window-maximum/

## 题目描述

```
给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。

返回滑动窗口中的最大值。

 

示例 1：

输入：nums = [1,3,-1,-3,5,3,6,7], k = 3
输出：[3,3,5,5,6,7]
解释：
滑动窗口的位置                最大值
---------------               -----
[1  3  -1] -3  5  3  6  7       3
 1 [3  -1  -3] 5  3  6  7       3
 1  3 [-1  -3  5] 3  6  7       5
 1  3  -1 [-3  5  3] 6  7       5
 1  3  -1  -3 [5  3  6] 7       6
 1  3  -1  -3  5 [3  6  7]      7


示例 2：

输入：nums = [1], k = 1
输出：[1]


示例 3：

输入：nums = [1,-1], k = 1
输出：[1,-1]


示例 4：

输入：nums = [9,11], k = 2
输出：[11]


示例 5：

输入：nums = [4,-2], k = 2
输出：[4]

 

提示：

1 <= nums.length <= 105
-104 <= nums[i] <= 104
1 <= k <= nums.length
```

## 前置知识

- 

## 公司

- 暂无

## 思路

https://leetcode-cn.com/problems/sliding-window-maximum/solution/shuang-xiang-dui-lie-jie-jue-hua-dong-chuang-kou-2/

## 关键点

-  

## 代码

- 语言支持：Java

Java Code:

```java


class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        if(nums == null || nums.length < 2) return nums;
        // 双向队列 保存当前窗口最大值的数组位置 保证队列中数组位置的数值按从大到小排序
        LinkedList<Integer> queue = new LinkedList();
        // 结果数组
        int[] result = new int[nums.length-k+1];
        // 遍历nums数组
        for(int i = 0;i < nums.length;i++){
            // 保证从大到小 如果前面数小则需要依次弹出，直至满足要求
            while(!queue.isEmpty() && nums[queue.peekLast()] <= nums[i]){
                queue.pollLast();
            }
            // 添加当前值对应的数组下标
            queue.addLast(i);
            // 判断当前队列中队首的值是否有效
            if(queue.peek() <= i-k){
                queue.poll();   
            } 
            // 当窗口长度为k时 保存当前窗口中最大值
            if(i+1 >= k){
                result[i+1-k] = nums[queue.peek()];
            }
        }
        return result;
    }
}

```

### 自己描述版本（easy)

```java
class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        //思路弄明白比较简单，就是维护一个滑动窗口，使用双端队列来存储
        //这个队列很像一个单调栈，就是把当前滑动窗口的数据最大值（下标）放到队头
        //这样每一次返回的就是队头元素就完事儿了
        //但是要注意，每一次需要判断队头元素是否还应该在欢动窗口当中，如果不再在了，那么就应该及时的踢出去


        //边界条件分析
        if(nums==null||nums.length<2) return nums;
        //新建一个双端队列
        LinkedList<Integer> queue=new LinkedList<>();//存储的是相关元素下标

        //新建一个结果集
        int[] res=new int[nums.length-k+1];
        //一共需要循环k次
        for(int i=0;i<nums.length;i++){

            //需要看当前的值和队尾进行比价,剔除小于当前值的元素
            while(!queue.isEmpty()&&nums[i]>=nums[queue.peekLast()]){
                queue.pollLast();
            }
            //进入这个队列？
            queue.addLast(i);//从后边进来
            //看看当前最大值是否过期，过期就不能要了
            if(i-queue.peek()>=k){
                queue.poll();
            }
            if(i+1>=k){//当前第i次循环只要i+1>=k就可以直接输出头部元素啦,比如说k=3那么从i=2就可以进行输出了
                res[i+1-k]=nums[queue.peek()];

            }
        }
        return res;

    }
}
```

**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(958. 二叉树的完全性检验)

https://leetcode-cn.com/problems/check-completeness-of-a-binary-tree/

## 题目描述

```
给定一个二叉树，确定它是否是一个完全二叉树。

百度百科中对完全二叉树的定义如下：

若设二叉树的深度为 h，除第 h 层外，其它各层 (1～h-1) 的结点数都达到最大个数，第 h 层所有的结点都连续集中在最左边，这就是完全二叉树。（注：第 h 层可能包含 1~ 2h 个节点。）

 

示例 1：

输入：[1,2,3,4,5,6]
输出：true
解释：最后一层前的每一层都是满的（即，结点值为 {1} 和 {2,3} 的两层），且最后一层中的所有结点（{4,5,6}）都尽可能地向左。


示例 2：

输入：[1,2,3,4,5,null,7]
输出：false
解释：值为 7 的结点没有尽可能靠向左侧。


 

提示：

树中将会有 1 到 100 个结点。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  

## 代码

- 语言支持：Java

Java Code:

```java

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public boolean isCompleteTree(TreeNode root) {
        //思路还是比较清楚，层次遍历
        //出现什么情况就不是完全二叉树了呢，就是保存两个节点，pre和cur
        //如果pre=null而cur!=null就会说明一定不是完全二叉树  
        //这个不管在哪一层都会说明不是完全二叉
        LinkedList<TreeNode> queue = new LinkedList<>();//双端队列存储树节点
        TreeNode pre=root;
        queue.addLast(root);
        while(!queue.isEmpty()){
            TreeNode node=queue.poll();
            if(node!=null&&pre==null) return false;
            if(node!=null){
                queue.addLast(node.left);
                queue.addLast(node.right);
            }
            //用完node赋值给pre
            pre=node;
        }
        return true;

       
    }
}



```

**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(101. 对称二叉树)

https://leetcode-cn.com/problems/symmetric-tree/

## 题目描述

```
给定一个二叉树，检查它是否是镜像对称的。

 

例如，二叉树 [1,2,2,3,4,4,3] 是对称的。

    1
   / \
  2   2
 / \ / \
3  4 4  3


 

但是下面这个 [1,2,2,null,3,null,3] 则不是镜像对称的:

    1
   / \
  2   2
   \   \
   3    3


 

进阶：

你可以运用递归和迭代两种方法解决这个问题吗？
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  

## 代码

- 语言支持：Java

Java Code:

```java

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public boolean isSymmetric(TreeNode root) {
        //递归分析就可以啦
        //怎么样算是对称
        if(root==null) return true;
        return isSame(root.left,root.right);

        


    }
    //主要的是比较左子树和右子树是否是对称的
    //注意，递归比较的是两个节点相对应节点是否相同才可以，不然白搭好不好
    public boolean isSame(TreeNode a,TreeNode b){
        if(a==null&&b==null) return true;
        else if(a==null||b==null) return false;
        else if(a.val!=b.val) return false;
        else return isSame(a.left,b.right)&&isSame(a.right,b.left);

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(143. 重排链表)

https://leetcode-cn.com/problems/reorder-list/

## 题目描述

```
给定一个单链表 L：L0→L1→…→Ln-1→Ln ，
将其重新排列后变为： L0→Ln→L1→Ln-1→L2→Ln-2→…

你不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。

示例 1:

给定链表 1->2->3->4, 重新排列为 1->4->2->3.

示例 2:

给定链表 1->2->3->4->5, 重新排列为 1->5->2->4->3.
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  

## 代码

- 语言支持：Java

Java Code:

```java

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
public void reorderList(ListNode head) {
    //思路和想法非常的不错好不好
    //先存到arraylist当中，然后进行双指针就可以啦
    if (head == null) {
        return;
    }
    //存到 list 中去
    List<ListNode> list = new ArrayList<>();
    while (head != null) {
        list.add(head);
        head = head.next;
    }
    //头尾指针依次取元素，双指针，分成奇数和偶数两种情况好不好
    int i = 0, j = list.size() - 1;
    while (i < j) {
        list.get(i).next = list.get(j);
        i++;
        //偶数个节点的情况，会提前相遇
        if (i == j) {
            break;
        }
        list.get(j).next = list.get(i);
        j--;
    }
    list.get(i).next = null;//别忘记最后跳出来的时候i一定是指向最后的那个链表节点，最后就可以啦进行学习分析
}

}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$