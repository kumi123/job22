## 题目地址(322. 零钱兑换)

https://leetcode-cn.com/problems/coin-change/

## 题目描述

```
给定不同面额的硬币 coins 和一个总金额 amount。编写一个函数来计算可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1。

你可以认为每种硬币的数量是无限的。

 

示例 1：

输入：coins = [1, 2, 5], amount = 11
输出：3 
解释：11 = 5 + 5 + 1

示例 2：

输入：coins = [2], amount = 3
输出：-1

示例 3：

输入：coins = [1], amount = 0
输出：0


示例 4：

输入：coins = [1], amount = 1
输出：1


示例 5：

输入：coins = [1], amount = 2
输出：2


 

提示：

1 <= coins.length <= 12
1 <= coins[i] <= 231 - 1
0 <= amount <= 104
```

## 前置知识

- 就是最基本的动态规划的知识

## 公司

## 思路

## 关键点

-  如何遍历，顺序遍历金额，每一个都遍历一下硬币数组
-  注意变动的条件

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int coinChange(int[] coins, int amount) {
        //思路相对还是比较明确
        //使用动态规划，最基本的哪一种存储方式就可以
        //dp[i]代表金额为amount的时候的最小硬币个数，每一个初值可以是一个非常大的值，代表一开始无法满足要求
        int[] dp=new int[amount+1];//因为要有一个0
        Arrays.fill(dp,amount+1);
        dp[0]=0;
        //int[] coins=new int[]{1,2,5};
        for(int i=1;i<=amount;i++){
            for(int coin:coins){
                //每一个硬币面额都去试一下，只有大于硬币面额才可以进行递推，否则还是在原来
                if(i-coin>=0){
                    dp[i]=Math.min(dp[i],dp[i-coin]+1);
                }
            }
        }
        return dp[amount]==amount+1?-1:dp[amount];
        


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n^2)$
- 空间复杂度：$O(n)$



## 题目地址(36. 二叉搜索树与双向链表)

https://leetcode-cn.com/problems/er-cha-sou-suo-shu-yu-shuang-xiang-lian-biao-lcof/

## 题目描述

```
输入一棵二叉搜索树，将该二叉搜索树转换成一个排序的循环双向链表。要求不能创建任何新的节点，只能调整树中节点指针的指向。

 

为了让您更好地理解问题，以下面的二叉搜索树为例：

 

 

我们希望将这个二叉搜索树转化为双向循环链表。链表中的每个节点都有一个前驱和后继指针。对于双向循环链表，第一个节点的前驱是最后一个节点，最后一个节点的后继是第一个节点。

下图展示了上面的二叉搜索树转化成的链表。“head” 表示指向链表中有最小元素的节点。

 

 

特别地，我们希望可以就地完成转换操作。当转化完成以后，树中节点的左指针需要指向前驱，树中节点的右指针需要指向后继。还需要返回链表中的第一个节点的指针。

 

注意：本题与主站 426 题相同：https://leetcode-cn.com/problems/convert-binary-search-tree-to-sorted-doubly-linked-list/

注意：此题对比原题有改动。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  判断两个边界的时候
-  注意每一次要执行的是cur.left=pre 因为cur一定是不为空，所以把pre.right=cur 放在if判断当中

## 代码

- 语言支持：Java

Java Code:

```java

/*
// Definition for a Node.
class Node {
    public int val;
    public Node left;
    public Node right;

    public Node() {}

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val,Node _left,Node _right) {
        val = _val;
        left = _left;
        right = _right;
    }
};
*/
class Solution {
    private Node pre=null,head=null;//这个cur没有必要定义，因为root在递归过程中就是这个cur
    public Node treeToDoublyList(Node root) {
        //思路也算比较清楚，就是一个中序遍历保证顺序，然后中间加一些操作就可以啦
        //这个操作接下来就是 需要更改当前节点cur与前一个节点pre的引用关系
        //使得pre.right=cur  cur.left=pre
        //注意边界条件 就是需要把head和tail找出来 最后相连就可以
        //head好找，第一个的时候 pre为空的时候 cur就是head 
        //tail好找，一直递归，最后一个pre就是tail
        if(root==null) return root;
        dfs(root);
        head.left=pre;
        pre.right=head;
        return head;
        
    }
    public void dfs(Node root){
        if(root==null) return;
        dfs(root.left);
        if(pre!=null) pre.right=root;//如果pre为空，说明是头头，所以是赋值给head
        else head=root;
        root.left=pre;//这一步都会有，不出现双指针异常
        pre=root;
        dfs(root.right);
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(62. 不同路径)

https://leetcode-cn.com/problems/unique-paths/

## 题目描述

```
一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为 “Start” ）。

机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为 “Finish” ）。

问总共有多少条不同的路径？

 

示例 1：

输入：m = 3, n = 7
输出：28

示例 2：

输入：m = 3, n = 2
输出：3
解释：
从左上角开始，总共有 3 条路径可以到达右下角。
1. 向右 -> 向下 -> 向下
2. 向下 -> 向下 -> 向右
3. 向下 -> 向右 -> 向下


示例 3：

输入：m = 7, n = 3
输出：28


示例 4：

输入：m = 3, n = 3
输出：6

 

提示：

1 <= m, n <= 100
题目数据保证答案小于等于 2 * 109
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  递推关系
-  初始值

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int uniquePaths(int m, int n) {
        //还是有这种递推关系 每一个都是从左边或者下边来
        //dp[i][j]=d[i-1][j]+d[i][j-1]
        //补充上初始条件就可以
        if(m==0||n==0) return 0;
        int[][] dp=new int[m][n];
        for(int i=0;i<m;i++) dp[i][0]=1;
        for(int j=1;j<n;j++) dp[0][j]=1;
        for(int i=1;i<m;i++){
            for(int j=1;j<n;j++){
                dp[i][j]=dp[i-1][j]+dp[i][j-1];
            }
        }
        return dp[m-1][n-1];

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(179. 最大数)

https://leetcode-cn.com/problems/largest-number/

## 题目描述

```
给定一组非负整数 nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。

注意：输出结果可能非常大，所以你需要返回一个字符串而不是整数。

 

示例 1：

输入：nums = [10,2]
输出："210"

示例 2：

输入：nums = [3,30,34,5,9]
输出："9534330"


示例 3：

输入：nums = [1]
输出："1"


示例 4：

输入：nums = [10]
输出："10"


 

提示：

1 <= nums.length <= 100
0 <= nums[i] <= 109
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  排序方式
-  substring的api不清楚

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public String largestNumber(int[] nums) {
        //不清楚这个具体的排序是如何做到的
        //但是先写下来吧
        //先进行转成字符串数组
        String[] ss=new String[nums.length];
        for(int i=0;i<nums.length;i++){
            ss[i]=""+nums[i];//这是一种整数转字符串的方法
        }
        Arrays.sort(ss,(a,b)->{
            return (b+a).compareTo(a+b);//按照组合后大小降序排列 不是很懂 看排名第一的题解
        });
        StringBuilder st=new StringBuilder();//为了取子串
        for(String s:ss) st.append(s);
        int index=0;
        int len=st.length();
        while(index<len-1&&st.charAt(index)=='0') index++;//排除前边的零 但是注意要最极端的保留一位，然后因为可能为0
        return st.substring(index);//从index开始往后取
    }
}

```

**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$





![image-20210604152721575](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210604152721575.png)

## 题目地址(468. 验证IP地址)

https://leetcode-cn.com/problems/validate-ip-address/

## 题目描述

```
编写一个函数来验证输入的字符串是否是有效的 IPv4 或 IPv6 地址。

如果是有效的 IPv4 地址，返回 "IPv4" ；
如果是有效的 IPv6 地址，返回 "IPv6" ；
如果不是上述类型的 IP 地址，返回 "Neither" 。

IPv4 地址由十进制数和点来表示，每个地址包含 4 个十进制数，其范围为 0 - 255， 用(".")分割。比如，172.16.254.1；

同时，IPv4 地址内的数不会以 0 开头。比如，地址 172.16.254.01 是不合法的。

IPv6 地址由 8 组 16 进制的数字来表示，每组表示 16 比特。这些组数字通过 (":")分割。比如,  2001:0db8:85a3:0000:0000:8a2e:0370:7334 是一个有效的地址。而且，我们可以加入一些以 0 开头的数字，字母可以使用大写，也可以是小写。所以， 2001:db8:85a3:0:0:8A2E:0370:7334 也是一个有效的 IPv6 address地址 (即，忽略 0 开头，忽略大小写)。

然而，我们不能因为某个组的值为 0，而使用一个空的组，以至于出现 (::) 的情况。 比如， 2001:0db8:85a3::8A2E:0370:7334 是无效的 IPv6 地址。

同时，在 IPv6 地址中，多余的 0 也是不被允许的。比如， 02001:0db8:85a3:0000:0000:8a2e:0370:7334 是无效的。

 

示例 1：

输入：IP = "172.16.254.1"
输出："IPv4"
解释：有效的 IPv4 地址，返回 "IPv4"


示例 2：

输入：IP = "2001:0db8:85a3:0:0:8A2E:0370:7334"
输出："IPv6"
解释：有效的 IPv6 地址，返回 "IPv6"


示例 3：

输入：IP = "256.256.256.256"
输出："Neither"
解释：既不是 IPv4 地址，又不是 IPv6 地址


示例 4：

输入：IP = "2001:0db8:85a3:0:0:8A2E:0370:7334:"
输出："Neither"


示例 5：

输入：IP = "1e1.4.5.6"
输出："Neither"


 

提示：

IP 仅由英文字母，数字，字符 '.' 和 ':' 组成。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

[记录遇到的坑（必看） - 验证IP地址 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/validate-ip-address/solution/ji-lu-yu-dao-de-keng-bi-kan-by-study11/)

## 关键点

- 两个判断条件，
- 拿IPV4说：必须有三个.和4个段;每个段内的字符串长度必须1<=len<=4;每个字符串的字符必须 '0'<=c&&c<='9';不能出现首位字符是'0'且长度大于2的字符串;
- 拿IPV8说：必须有7个：和8个段;每个段内的字符串长度必须1<=len<=4;每个字符串的字符必须 '0'<=c&&c<='9'||'a'<=c&&c<='f';


## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public String validIPAddress(String IP) {
        //思路还是比较清楚的，就是疯狂的进行判断好不好

        //为了方便进行判断  设置两个函数 第一个是判断字符串是否只含有数字  第二个是判断是否是hex数组，这里不需要判断长度
        //注意这个字符串的意义是什么啊，contains()
        //endsWith()以什么结束
        //startWith()以什么开头
        //split()是进行划分的


        //两个判断条件，
        //拿IPV4说：必须有三个.和4个段;每个段内的字符串长度必须1<=len<=4;每个字符串的字符必须 '0'<=c&&c<='9';不能出现首位字符是'0'且长度大于2的字符串;
        //拿IPV8说：必须有7个：和8个段;每个段内的字符串长度必须1<=len<=4;每个字符串的字符必须 '0'<=c&&c<='9'||'a'<=c&&c<='f';


        if(IP.contains(".")){
            if(IP.endsWith(".")){
                return "Neither";
            }
            String[] strs = IP.split("\\.");
            if(strs.length!=4){
                return "Neither";
            }
            for(String s : strs){
                if(s.length()<1 || s.length()>3){
                    return "Neither";
                }
                if(s.startsWith("0") && s.length()!=1){
                    return "Neither";
                }else if(!validNum(s)){
                    return "Neither";
                }
            }
            return "IPv4";
        }else if(IP.contains(":")){
            if(IP.endsWith(":")){
                return "Neither";
            }
            String[] strs = IP.split(":");
            if(strs.length!=8){
                return "Neither";
            }
            for(String s : strs){
                if(s.length()<1 || s.length()>4){
                    return "Neither";
                }
                if(!validHex(s)){
                    return "Neither";
                }
            }
            return "IPv6";
        }
        return "Neither";
    }

    private boolean validHex(String s){
        for(int i=0; i<s.length(); i++){
            char ch = s.charAt(i);
            if(!(ch>='0'&&ch<='9' || ch>='a'&&ch<='f' || ch>='A'&&ch<='F')){
                return false;
            }
        }
        return true;
    }

    private boolean validNum(String s){
        int res = 0;
        for(int i=0; i<s.length(); i++){
            char ch = s.charAt(i);
            if(ch<'0' || ch>'9'){
                return false;
            }
            res = res*10+ch-'0';
        }
        return res>=0 && res<=255;
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$



### 这个斐波那契数列

```java
class Solution {
    public int fib(int n) {
        //最简单的是那种普通的有存储的递归
        if(n<2) return n;
        int[] dp=new int[n+1];
        dp[0]=0;
        dp[1]=1;
        for(int i=2;i<n+1;i++){
            dp[i]=dp[i-1]+dp[i-2];
            dp[i]=dp[i]%1000000007;
        }
        return dp[n];


    }
}
```

### 没有数组的这个

```java
class Solution {
    public int fib(int n) {
        //最简单的是那种普通的有存储的递归
        if(n<2) return n;
        int a=0,b=1;//相当于存储前两项
        int sum=0;//存储当前值
        for(int i=2;i<n+1;i++){
            sum=(a+b)%1000000007;
            a=b;
            b=sum;
        }
        return sum;


    }
}
```

## 题目地址(21. 调整数组顺序使奇数位于偶数前面)

https://leetcode-cn.com/problems/diao-zheng-shu-zu-shun-xu-shi-qi-shu-wei-yu-ou-shu-qian-mian-lcof/

## 题目描述

```
输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有奇数位于数组的前半部分，所有偶数位于数组的后半部分。

 

示例：

输入：nums = [1,2,3,4]
输出：[1,3,2,4] 
注：[3,1,2,4] 也是正确的答案之一。

 

提示：

0 <= nums.length <= 50000
1 <= nums[i] <= 10000
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  双指针的意义

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int[] exchange(int[] nums) {
        //思路是双指针，其实就是两个坐标
        //快指针是找奇数，慢指针是指示奇数应该放置的位置
        int fast=0,slow=0;
        while(fast<nums.length){
            if(nums[fast]%2!=0){
                int tem=nums[slow];
                nums[slow]=nums[fast];
                nums[fast]=tem;
                slow++;
            }
            fast++;
                             
                    }
            return nums;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(283. 移动零)

https://leetcode-cn.com/problems/move-zeroes/

## 题目描述

```
给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。

示例:

输入: [0,1,0,3,12]
输出: [1,3,12,0,0]

说明:

必须在原数组上操作，不能拷贝额外的数组。
尽量减少操作次数。
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
    public void moveZeroes(int[] nums) {
        //直接开始写比较easy
        //还是双指针，然后快的找不为0的位置 而慢的就是看不为0的数应该放在哪个位置
        int slow=0,fast=0;
        while(fast<nums.length){
            if(nums[fast]!=0){
                nums[slow]=nums[fast];
                slow++;
            }
            fast++;
        }
        for(int i=slow;i<nums.length;i++){
            nums[i]=0;
        }
        

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(224. 基本计算器)

https://leetcode-cn.com/problems/basic-calculator/

## 题目描述

```
给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。

 

示例 1：

输入：s = "1 + 1"
输出：2


示例 2：

输入：s = " 2-1 + 2 "
输出：3


示例 3：

输入：s = "(1+(4+5+2)-3)+(6+8)"
输出：23


 

提示：

1 <= s.length <= 3 * 105
s 由数字、'+'、'-'、'('、')'、和 ' ' 组成
s 表示一个有效的表达式
```

## 前置知识

- 

## 公司

- 暂无

## 思路

[如何想到用「栈」？思路来自于递归 - 基本计算器 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/basic-calculator/solution/ru-he-xiang-dao-yong-zhan-si-lu-lai-zi-y-gpca/)

## 关键点

- 具体思路其实就是入栈出栈应该是什么的问题

  

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
public  int calculate(String s) {
//        计算结果，部分计算结果，括号中计算结果
        int res = 0;
//        当前的数字，例如：1+23中的1或者23
        int num = 0;
//        符号，加号(+1)或者减号(-1)
        int sign = 1;
//        当右括号时，用于存储计算结果
        Stack<Integer> stack = new Stack<>();

        char[] chars = s.toCharArray();
        int len = chars.length;

        for (int i = 0; i < len; i++) {
            char c = chars[i];
//            如果当前字符为' '，则直接continue
            if (c == ' ') {
                continue;
            }
//            如果当前字符是一个数字，则用num进行记录
//            当前有可能是一个>9的数字，所以需要num = num * 10 + c - '0'
            if (c >= '0' && c <= '9') {
                num = num * 10 + c - '0';
//                判断当前数字是否已经取完
//                例如：123+4，只有当取到+时，才能确定123为当前的num
                if (i < len-1 && '0' <= chars[i+1] && chars[i+1] <= '9') {
                    continue;
                }
//            如果当前字符为'+'或者'-'
            } else if (c == '+' || c == '-') {
//                将num置为0，用来存放当前符号(+/-)之后的数字
                num = 0;
//                如果当前符号为+，则sign为+1
//                如果当前符号为-，则sign为-1
                sign = c=='+' ? 1 : -1;
//            如果当前符号为'('
            } else if (c == '(') {
//                例如当前表达式为：'123+(...)'
//                则将res:123，入栈
                stack.push(res);
//                则将sign:+，入栈
                stack.push(sign);
//                同时res置为0，用来保存()中的计算结果
                res = 0;
//                sign置为初始状态，为1
                sign = 1;
//            如果当前符号为')'
            } else if (c == ')') {
//                '('前边的符号出栈
                sign = stack.pop();
//                将num替换为括号中的计算结果
                num = res;
//                将res替换为括号前边的计算结果
                res = stack.pop();
            }
//            每遍历一次，得到一个res
            res += sign * num;
        }
        return res;
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(227. 基本计算器 II)

https://leetcode-cn.com/problems/basic-calculator-ii/

## 题目描述

```
给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。

整数除法仅保留整数部分。

 

示例 1：

输入：s = "3+2*2"
输出：7


示例 2：

输入：s = " 3/2 "
输出：1


示例 3：

输入：s = " 3+5 / 2 "
输出：5


 

提示：

1 <= s.length <= 3 * 105
s 由整数和算符 ('+', '-', '*', '/') 组成，中间由一些空格隔开
s 表示一个 有效表达式
表达式中的所有整数都是非负整数，且在范围 [0, 231 - 1] 内
题目数据保证答案是一个 32-bit 整数
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  看相应题解就可以

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int calculate(String s) {
        //思路比较清楚 
        //没有括号简单的多，用栈保存可以直接计算的结果
        //num 保存当前计算出来的结果，比如说123+4*3+4当中的123或者4*3
        //presign 表示计算出num之后要进行入栈，需要判断num的符号啊 这个就是num之前的符号
        //如果pre的话
        char presign='+';//默认+
        int num=0;
        Stack<Integer> stack=new Stack<>();

        char[] res=s.toCharArray();
        for(int i=0;i<res.length;i++){
            //是数字就求和
            if(Character.isDigit(res[i])){
                num=num*10+res[i]-'0';
            }
            // 因为这里的表达式不包含括号, 所以最后一位必然是数字, 又因为当数字时, 其实只是num被赋值, 并不入栈也不计算, 所以在最后一位要和遇见+-*/时的操作效果一样才行
            if(!Character.isDigit(res[i])&&res[i] != ' '||i==res.length-1){
                if(presign=='+'){
                    stack.push(num);
                }
                if(presign=='-'){
                    stack.push(-1*num);                  
                }
                if(presign=='*'){
                    stack.push(num*stack.pop());
                }
                if(presign=='/'){
                    stack.push(stack.pop()/num);
                    
                }
                num=0;
                presign=res[i];
            }
        }
        int sumed=0;
        while(!stack.isEmpty()){
            sumed+=stack.pop();
        }
        return sumed;
       
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## ==题目地址(224. 基本计算器)==

https://leetcode-cn.com/problems/basic-calculator/

## 题目描述

```
给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。

 

示例 1：

输入：s = "1 + 1"
输出：2


示例 2：

输入：s = " 2-1 + 2 "
输出：3


示例 3：

输入：s = "(1+(4+5+2)-3)+(6+8)"
输出：23


 

提示：

1 <= s.length <= 3 * 105
s 由数字、'+'、'-'、'('、')'、和 ' ' 组成
s 表示一个有效的表达式
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  弄明白具体的意义

## 代码

- 语言支持：Java

Java Code:

```java
class Solution {
public  int calculate(String s) {
//        计算结果，部分计算结果，括号中计算结果
        int res = 0;
//        当前的数字，例如：1+23中的1或者23
        int num = 0;
//        符号，加号(+1)或者减号(-1)
        int sign = 1;
//        当右括号时，用于存储计算结果
        Stack<Integer> stack = new Stack<>();

        char[] chars = s.toCharArray();
        int len = chars.length;

        for (int i = 0; i < len; i++) {
            char c = chars[i];
//            如果当前字符为' '，则直接continue
            if (c == ' ') {
                continue;
            }
//            如果当前字符是一个数字，则用num进行记录
//            当前有可能是一个>9的数字，所以需要num = num * 10 + c - '0'
            if (c >= '0' && c <= '9') {
                num = num * 10 + c - '0';
//                判断当前数字是否已经取完
//                例如：123+4，只有当取到+时，才能确定123为当前的num,只有最后那一位不满足条件，然后不能加起来
                if (i < len-1 && '0' <= chars[i+1] && chars[i+1] <= '9') {
                    continue;
                }
//            如果当前字符为'+'或者'-'
            } else if (c == '+' || c == '-') {
//                将num置为0，用来存放当前符号(+/-)之后的数字
                num = 0;
//                如果当前符号为+，则sign为+1
//                如果当前符号为-，则sign为-1
                sign = c=='+' ? 1 : -1;
//            如果当前符号为'('
            } else if (c == '(') {
//                例如当前表达式为：'123+(...)'
//                则将res:123，入栈
                stack.push(res);
//                则将sign:+，入栈
                stack.push(sign);
//                同时res置为0，用来保存()中的计算结果
                res = 0;
//                sign置为初始状态，为1
                sign = 1;
//            如果当前符号为')'
            } else if (c == ')') {
//                '('前边的符号出栈
                sign = stack.pop();
//                将num替换为括号中的计算结果
                num = res;
//                将res替换为括号前边的计算结果
                res = stack.pop();
            }
//            每遍历一次，得到一个res
            res += sign * num;
        }
        return res;
    }
}









class Solution {
public  int calculate(String s) {
     int num=0;//当前值
     int res=0;//存储表达式左边的值
     int sign=1;
     Stack<Integer> stack=new Stack<>();
     char[] res1=s.toCharArray();
     for(int i=0;i<res1.length;i++){
         char ss=res1[i];
         if(ss==' ') continue;
         if('0'<=ss&&ss<='9'){
             num=num*10+res1[i]-'0';
             if(i<res1.length-1&&res1[i+1]>='0'&&res1[i+1]<='9'){
                 continue;
             }

         }
         else if(ss=='+'||ss=='-'){
             num=0;//清零
             sign=ss=='+'?1:-1;
         }
         else if(ss=='('){
             //入栈
             stack.push(res);
             stack.push(sign);
//         重新规整
             res=0;
             sign=1;
         }
         else if(ss==')'){
             sign=stack.pop();//上一个的符号
             num=res;//自身的值就是num
             res=stack.pop();//左边的值 就是res
             
         }



         res+=num*sign;
     }
return res;

    }
}
```

**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(162. 寻找峰值)

https://leetcode-cn.com/problems/find-peak-element/

## 题目描述

```
峰值元素是指其值大于左右相邻值的元素。

给你一个输入数组 nums，找到峰值元素并返回其索引。数组可能包含多个峰值，在这种情况下，返回 任何一个峰值 所在位置即可。

你可以假设 nums[-1] = nums[n] = -∞ 。

 

示例 1：

输入：nums = [1,2,3,1]
输出：2
解释：3 是峰值元素，你的函数应该返回其索引 2。

示例 2：

输入：nums = [1,2,1,3,5,6,4]
输出：1 或 5 
解释：你的函数可以返回索引 1，其峰值元素为 2；
     或者返回索引 5， 其峰值元素为 6。


 

提示：

1 <= nums.length <= 1000
-231 <= nums[i] <= 231 - 1
对于所有有效的 i 都有 nums[i] != nums[i + 1]

 

进阶：你可以实现时间复杂度为 O(logN) 的解决方案吗？
```

## 前置知识

- 

## 公司

- 暂无

## 思路

[详细通俗的思路分析，多解法 - 寻找峰值 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/find-peak-element/solution/xiang-xi-tong-su-de-si-lu-fen-xi-duo-jie-fa-by-39/)

太强了这个链接

## 关键点

-  方法一
-  ![image-20210606183816287](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210606183816287.png)
-  方法二
-  二分的意义在哪儿？
-  ![image-20210606183854156](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210606183854156.png)
-  

## 代码

- 语言支持：Java

Java Code:

方法一  线性扫描

```java

class Solution {
    public int findPeakElement(int[] nums) {
        //第一种思路 线性扫描
        //因为第一个是负无穷 所以从第一项开始是是上升趋势
        //所以线性扫描  第一次出现下降就返回当前值就可
        //一旦不满足下降就会一直上升趋势 也是要判断第一次下降

        //如果没有下降 所以说就是一直上升 那么返回最后的位置 肯定是
        for(int i=0;i<nums.length-1;i++){
            if(nums[i]>nums[i+1]) return i;
        }
        return nums.length-1;


    }
}

```

方法二  二分法

```java
class Solution {
    public int findPeakElement(int[] nums) {
        //二分法，就是每一次减少一一半判断就可以啦
        //比较mid与mid+1
        //谁大就说明在那半边至少存在一个峰值  r=mid 或者l=mid+1
        //为什么呢？使用反证法
        //比如说mid >mid+1 （mid+1 到最后没有峰值）  说明mid+1左边是上升趋势 而右边（mid+2到最后）假设没有峰值以及mid+1自身不是峰值不会同时成立
        //因为 末尾是负无穷 右边没有峰值的唯一可能是 从mid+2开始都是递减的 而mid+1到mid+2 无论是什么趋势 
        // mid+1 与mid+2 都会有一个是峰值  不满足反证  所以成立


        //反过来mid >mid+1 左边一定至少一个峰值

        int left=0,right=nums.length-1;
        while(left<right){
            int mid=left+(right-left)/2;
            if(nums[mid]>nums[mid+1]) right=mid;
            else left=mid+1;
        }
        return left;
        

    }
}
```

**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(24. 两两交换链表中的节点)

https://leetcode-cn.com/problems/swap-nodes-in-pairs/

## 题目描述

```
给定一个链表，两两交换其中相邻的节点，并返回交换后的链表。

你不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。

 

示例 1：

输入：head = [1,2,3,4]
输出：[2,1,4,3]


示例 2：

输入：head = []
输出：[]


示例 3：

输入：head = [1]
输出：[1]


 

提示：

链表中节点的数目在范围 [0, 100] 内
0 <= Node.val <= 100

 

进阶：你能在不修改链表节点值的情况下解决这个问题吗?（也就是说，仅修改节点本身。）
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
    public ListNode swapPairs(ListNode head) {
        //思路确实比比较清楚,就是两个一组一组 连接顺序就可以了

        //先使用一个烧饼节点
        ListNode dummy=new ListNode(-1);
        dummy.next=head;
        ListNode pre=dummy;

        //明确两个一组  l1 l2  需要弄得是 有一个pre 当作上一组节点的末尾 以及一个next 作为下一组节点的开始
        //做到 l2.next=l1 l1.next=next pre.next=l2 
        //需要更新pre  pre=pre.next.next 
        //l1 l2 pre 也需要更新 l1=pre.next l2=pre.next.next next=l2.next;
        while(pre!=null&&pre.next!=null&&pre.next.next!=null){
            ListNode l1=pre.next;
            ListNode l2=pre.next.next;
            ListNode next=l2.next;
            l2.next=l1;
            l1.next=next;
            pre.next=l2;
            pre=l1;
        }
        return dummy.next;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(14. 最长公共前缀)

https://leetcode-cn.com/problems/longest-common-prefix/

## 题目描述

```
编写一个函数来查找字符串数组中的最长公共前缀。

如果不存在公共前缀，返回空字符串 ""。

 

示例 1：

输入：strs = ["flower","flow","flight"]
输出："fl"


示例 2：

输入：strs = ["dog","racecar","car"]
输出：""
解释：输入不存在公共前缀。

 

提示：

0 <= strs.length <= 200
0 <= strs[i].length <= 200
strs[i] 仅由小写英文字母组成
```

## 前置知识

- 

## 公司

- 暂无

## 思路 

见代码

## 关键点

-  

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public String longestCommonPrefix(String[] strs) {
        //这个貌似是剑指offer的原题
        //思路就是令第一个字符串为具体的结果
        //然后后续字符串跟来比较
        //使用一个指针来表征具体到哪一个位置是相同的
        //一旦出现不同 就跳出来 然后0到j下标的子串就是当前结果
        //一旦出现了空字符串的话，那么就可以进行直接返回了
        if(strs.length<1) return "";
        String ans=strs[0];
        for(int i=1;i<strs.length;i++){
            int j=0;
            while(j<ans.length()&&j<strs[i].length()){
                if(ans.charAt(j)!=strs[i].charAt(j)) break;//说明到头了
                j++;
            }
            ans=strs[i].substring(0,j);//左闭右开
            if(ans.equals("")) return "";
        }
        return ans;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$





[简明易懂的Java解答 - 最长连续序列 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/longest-consecutive-sequence/solution/jian-ming-yi-dong-de-javajie-da-by-lan-s-pf26/)



## 题目地址(128. 最长连续序列)

https://leetcode-cn.com/problems/longest-consecutive-sequence/

## 题目描述

```
给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。

 

进阶：你可以设计并实现时间复杂度为 O(n) 的解决方案吗？

 

示例 1：

输入：nums = [100,4,200,1,3,2]
输出：4
解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。

示例 2：

输入：nums = [0,3,7,2,5,8,4,6,0,1]
输出：9


 

提示：

0 <= nums.length <= 104
-109 <= nums[i] <= 109
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

![image-20210607152521720](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210607152521720.png)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int longestConsecutive(int[] nums) {
        //思路很有意思 因为连续的可能分成挺多段的
        //所以需要找到每一段的左边界 然后计算每一段的长度 最后选择最大的
        //怎么样找分段的呢？需要用hashset来进行存储，然后如果对于每一个值
        //hashset当中没有当前值减1 说明可以当做左边界
        //然后只有一个的时候


      
        HashSet<Integer> res=new HashSet<>();
        int count=0;
        for(int num:nums) res.add(num);
        for(int val:res){
            if(res.contains(val-1)) continue;//说明不能当做左边边界
            else{
                //说明可以当做左边界
                //注意一个点 如果某一个段只有一个的话,还是从val开始吧 这样比较经典一些
                int len=0;
                while(res.contains(val)){
                    len++;
                    val++;
                    count=Math.max(len,count);
                }
            }
        }
        return count;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## ==题目地址(32. 最长有效括号)==

https://leetcode-cn.com/problems/longest-valid-parentheses/

## 题目描述

```
给你一个只包含 '(' 和 ')' 的字符串，找出最长有效（格式正确且连续）括号子串的长度。

 

示例 1：

输入：s = "(()"
输出：2
解释：最长有效括号子串是 "()"


示例 2：

输入：s = ")()())"
输出：4
解释：最长有效括号子串是 "()()"


示例 3：

输入：s = ""
输出：0


 

提示：

0 <= s.length <= 3 * 104
s[i] 为 '(' 或 ')'
```

## 前置知识

- 

## 公司

- 暂无

## 思路

[「手画图解」剖析两种解法：利用栈、动态规划 | 32. 最长有效括号 - 最长有效括号 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/longest-valid-parentheses/solution/shou-hua-tu-jie-zhan-de-xiang-xi-si-lu-by-hyj8/)

## 关键点

![image-20210607160802858](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210607160802858.png)

![image-20210607160841273](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210607160841273.png)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int longestValidParentheses(String s) {
        //利用入栈出栈来进行求解
        
        //思路其实因为就是()()()这种算是6
        //让(入栈 然后遇到）就出栈
        //这样会当栈为空的时候 就当前)的索引减去 栈顶索引就可以
        
        //为了防止为空，写入-1 以及 当栈为空的时候就会写入右边括号的索引
        
        //具体的思路见上边
        int maxed=0;
        Stack<Integer> stack=new Stack<>();
        stack.push(-1);
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)=='(') stack.push(i);
            else{
                stack.pop();//已经加了判别是否为空的情况，一定会有值
                if(!stack.isEmpty()){
                    int tem=i-stack.peek();
                    maxed=Math.max(maxed,tem);
                }
                else stack.push(i);
            }
        }
        return maxed;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$



动态规划方法

![image-20210607165318896](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210607165318896.png)

```java
class Solution {
    public int longestValidParentheses(String s) {
        //使用动态规划，其实比较难搞
        //分情况讨论
        //dp[i]表示以s[i]结尾的最长有效括号长度
        int[] dp=new int[s.length()];
        Arrays.fill(dp,0);
        int res=0;
        for(int i=1;i<s.length();i++){
            if(s.charAt(i)==')'){
                if(s.charAt(i-1)=='('){
                    dp[i]=2+((i-2>=0)?dp[i-2]:0);
                }
                else if(s.charAt(i-1)==')'&&i-dp[i-1]-1>= 0&&s.charAt(i-1-dp[i-1])=='(')
                {
                    if(i-2-dp[i-1]>=0) dp[i]=2+dp[i-1]+dp[i-2-dp[i-1]];
                    else dp[i]=2+dp[i-1];
                }
            }
            res=Math.max(dp[i],res);
        }
        return res;

    }
}
```

## 题目地址(7. 整数反转)

https://leetcode-cn.com/problems/reverse-integer/

## 题目描述

```
给你一个 32 位的有符号整数 x ，返回将 x 中的数字部分反转后的结果。

如果反转后整数超过 32 位的有符号整数的范围 [−231,  231 − 1] ，就返回 0。

假设环境不允许存储 64 位整数（有符号或无符号）。

 

示例 1：

输入：x = 123
输出：321


示例 2：

输入：x = -123
输出：-321


示例 3：

输入：x = 120
输出：21


示例 4：

输入：x = 0
输出：0


 

提示：

-231 <= x <= 231 - 1
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

[图解 7. 整数反转 - 整数反转 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/reverse-integer/solution/tu-jie-7-zheng-shu-fan-zhuan-by-wang_ni_ma/) 具体的思路问题

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int reverse(int x) {
        //具体的思路来讲分析，就是每一步取得最后数字就可以
        //然后乘10加当前值
        //但是要注意溢出的情况 ，提前判断
        int res=0;
        while(x!=0){
            int val=x%10;//最后位的数字
            //正数情况
            if(res>214748364||(res==214748364&&val>7)) return 0;
            if(res<-214748364||(res==-214748364&&val<-8)) return 0;

            res=res*10+val;
            x/=10;
        }
        return res;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$



## 题目地址(43. 字符串相乘)

https://leetcode-cn.com/problems/multiply-strings/

## 题目描述

```
给定两个以字符串形式表示的非负整数 num1 和 num2，返回 num1 和 num2 的乘积，它们的乘积也表示为字符串形式。

示例 1:

输入: num1 = "2", num2 = "3"
输出: "6"

示例 2:

输入: num1 = "123", num2 = "456"
输出: "56088"

说明：

num1 和 num2 的长度小于110。
num1 和 num2 只包含数字 0-9。
num1 和 num2 均不以零开头，除非是数字 0 本身。
不能使用任何标准库的大数类型（比如 BigInteger）或直接将输入转换为整数来处理。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

- 利用竖式乘法来进行计算

  [优化版竖式(打败99.4%) - 字符串相乘 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/multiply-strings/solution/you-hua-ban-shu-shi-da-bai-994-by-breezean/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    /**
    * 计算形式
    *    num1
    *  x num2
    *  ------
    *  result
    */
    public String multiply(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        // 保存计算结果
        String res = "0";
        
        // num2 逐位与 num1 相乘
        for (int i = num2.length() - 1; i >= 0; i--) {
            int carry = 0;
            // 保存 num2 第i位数字与 num1 相乘的结果
            StringBuilder temp = new StringBuilder();
            // 补 0 
            for (int j = 0; j < num2.length() - 1 - i; j++) {
                temp.append(0);
            }
            int n2 = num2.charAt(i) - '0';
            
            // num2 的第 i 位数字 n2 与 num1 相乘
            for (int j = num1.length() - 1; j >= 0 || carry != 0; j--) {
                int n1 = j < 0 ? 0 : num1.charAt(j) - '0';
                int product = (n1 * n2 + carry) % 10;
                temp.append(product);
                carry = (n1 * n2 + carry) / 10;
            }
            // 将当前结果与新计算的结果求和作为新的结果
            res = addStrings(res, temp.reverse().toString());
        }
        return res;
    }

    /**
     * 对两个字符串数字进行相加，返回字符串形式的和
     */
    public String addStrings(String num1, String num2) {
        StringBuilder builder = new StringBuilder();
        int carry = 0;
        for (int i = num1.length() - 1, j = num2.length() - 1;
             i >= 0 || j >= 0 || carry != 0;
             i--, j--) {
            int x = i < 0 ? 0 : num1.charAt(i) - '0';
            int y = j < 0 ? 0 : num2.charAt(j) - '0';
            int sum = (x + y + carry) % 10;
            builder.append(sum);
            carry = (x + y + carry) / 10;
        }
        return builder.reverse().toString();
    }
}


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$



## 多思考进行起飞操作

![image-20210607173012620](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210607173012620.png)

```java
class Solution {
public String multiply(String num1, String num2) {
    //如果不对输入有"0"做处理则返回"000...0"而不是"0"
    if("0".equals(num1)||"0".equals(num2)) return "0";//需要单独对是否为0进行相应的处理
    int l1=num1.length(),l2=num2.length();//具体的尺寸
    int[] result = new int[l1+l2-1];//具体的结果尺寸

    //计算乘积并存储到数组中
    for (int i = 0; i < l1; i++) {
        for (int j = 0; j < l2; j++) {
            result[i+j]+=(num1.charAt(i)-'0')*(num2.charAt(j)-'0');//这个从前从后都可以
        }
    }

    //整理数组中结果到最终的字符串中，这个是从后到前，然后进行,记住要有进位就可以
    StringBuilder sb = new StringBuilder();
    int carry=0;
    for (int i = l1 + l2 - 2; i >= 0; i--) {
        sb.append((result[i]+carry)%10);
        carry=(result[i]+carry)/10;
    }
    while (carry != 0) {
        sb.append(carry%10);
        carry/=10;
    }
    return sb.reverse().toString();
}
}



//自己的想法

class Solution {
public String multiply(String num1, String num2) {
   //首先特殊情况，有一个一旦为"0"就应该直接返回0
   if(num1.equals("0")||num2.equals("0")) return "0";
   //建立一个数组来保存每一个位置的值
   int[] res=new int[num1.length()+num2.length()-1];//因为最大的数为num1.length()-1+num2.length()-1
   for(int i=0;i<num1.length();i++){
       for(int j=0;j<num2.length();j++){
           res[i+j]+=(num1.charAt(i)-'0')*(num2.charAt(j)-'0');
       }
   }
   StringBuilder ans=new StringBuilder();
   int carry=0;//进位
   for(int t=num1.length()+num2.length()-2;t>=0;t--){
       int val=(res[t]+carry)%10;
       ans.append(val);
       carry=(res[t]+carry)/10;

   }
   while(carry!=0){//因为可能是很大的数，carry可能是两位数
    int tend=carry%10;
    ans.append(tend);
    carry/=10;

   }
   return ans.reverse().toString();

}
}
```





## 题目地址(912. 排序数组)

https://leetcode-cn.com/problems/sort-an-array/

## 题目描述

```
给你一个整数数组 nums，请你将该数组升序排列。

 

示例 1：

输入：nums = [5,2,3,1]
输出：[1,2,3,5]


示例 2：

输入：nums = [5,1,1,2,0,0]
输出：[0,0,1,1,2,5]


 

提示：

1 <= nums.length <= 50000
-50000 <= nums[i] <= 50000
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  快速排序

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int[] sortArray(int[] nums) {
        //手写一个快速排序把
        //思路就是分别移动，然后就直接原地更改，找一个值，小的都移动到左边，大的都移动到右边
        quick_sort(nums,0,nums.length-1);
        return nums;


    }
    public void quick_sort(int[] nums,int left,int right){
        //left right 就是要排序的两个边界，在两个边界当中进行学习
        if(left>right) return;//第一个点 要注意跳出边界条件
        int i=left,j=right;//双指针
        int tem=nums[left];//因为这个要进行交换；
        while(i<j){
            //遇到什么情况会continue 遇到什么情况会进行交换
            while(i<j&&nums[j]>=tem) j--;//第二个就是i<j还是要继续弄 第三个点严格大于或者小于才要交换
            while(i<j&&nums[i]<=tem) i++;
            int tt=nums[j];
            nums[j]=nums[i];
            nums[i]=tt;
        }
        nums[left]=nums[j];
        nums[j]=tem;
        quick_sort(nums,left,j-1);//注意范围
        quick_sort(nums,j+1,right);

    }

}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(59. 螺旋矩阵 II)

https://leetcode-cn.com/problems/spiral-matrix-ii/

## 题目描述

```
给你一个正整数 n ，生成一个包含 1 到 n2 所有元素，且元素按顺时针顺序螺旋排列的 n x n 正方形矩阵 matrix 。

 

示例 1：

输入：n = 3
输出：[[1,2,3],[8,9,4],[7,6,5]]


示例 2：

输入：n = 1
输出：[[1]]


 

提示：

1 <= n <= 20
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
    public int[][] generateMatrix(int n) {
        //其实就是不断进行边界的缩放问题
        int l = 0, r = n - 1, t = 0, b = n - 1;
        int[][] mat = new int[n][n];
        int num = 1, tar = n * n;
        while(num <= tar){
            for(int i = l; i <= r; i++) mat[t][i] = num++; // left to right.
            t++;
            for(int i = t; i <= b; i++) mat[i][r] = num++; // top to bottom.
            r--;
            for(int i = r; i >= l; i--) mat[b][i] = num++; // right to left.
            b--;
            for(int i = b; i >= t; i--) mat[i][l] = num++; // bottom to top.
            l++;
        }
        return mat;
    }
}



```



## 方法2 和螺旋矩阵1的方式一样进行学习就可以

```java

class Solution {
    public int[][] generateMatrix(int n) {
        //其实就是不断进行边界的缩放问题
        int l = 0, r = n - 1, t = 0, b = n - 1;
        int[][] mat = new int[n][n];
        int num = 1;
        while(true){
            for(int i = l; i <= r; i++) mat[t][i] = num++; // left to right.
            if(++t>b) break;
            for(int i = t; i <= b; i++) mat[i][r] = num++; // top to bottom.
            if(--r<l) break;
            for(int i = r; i >= l; i--) mat[b][i] = num++; // right to left.
            if(--b<t) break;
            for(int i = b; i >= t; i--) mat[i][l] = num++; // bottom to top.
            if(++l>r) break;
        }
        return mat;
    }
}



```



**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$





### 螺旋矩阵1的方式

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





## 题目地址(208. 实现 Trie (前缀树))

https://leetcode-cn.com/problems/implement-trie-prefix-tree/

## 题目描述

```
Trie（发音类似 "try"）或者说 前缀树 是一种树形数据结构，用于高效地存储和检索字符串数据集中的键。这一数据结构有相当多的应用情景，例如自动补完和拼写检查。

请你实现 Trie 类：

Trie() 初始化前缀树对象。
void insert(String word) 向前缀树中插入字符串 word 。
boolean search(String word) 如果字符串 word 在前缀树中，返回 true（即，在检索之前已经插入）；否则，返回 false 。
boolean startsWith(String prefix) 如果之前已经插入的字符串 word 的前缀之一为 prefix ，返回 true ；否则，返回 false 。

 

示例：

输入
["Trie", "insert", "search", "search", "startsWith", "insert", "search"]
[[], ["apple"], ["apple"], ["app"], ["app"], ["app"], ["app"]]
输出
[null, null, true, false, true, null, true]

解释
Trie trie = new Trie();
trie.insert("apple");
trie.search("apple");   // 返回 True
trie.search("app");     // 返回 False
trie.startsWith("app"); // 返回 True
trie.insert("app");
trie.search("app");     // 返回 True


 

提示：

1 <= word.length, prefix.length <= 2000
word 和 prefix 仅由小写英文字母组成
insert、search 和 startsWith 调用次数 总计 不超过 3 * 104 次
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

class Trie {
    //初始化定义一些东西
    private Trie[] kids;//后代数组
    private boolean isEnd;//是否是结局


    /** Initialize your data structure here. */
    public Trie() {
        isEnd=false;//默认不是结局
        kids=new Trie[26];//26长度的kida


    }
    
    /** Inserts a word into the trie. */
    public void insert(String word) {
        //定义一个头啊
        Trie cur=this;//定义正在进行操作的节点
        if(word==null||word.length()==0) return;//不需要进行操作
        char[] ss=word.toCharArray();
        for(int i=0;i<ss.length;i++){
            int inx=ss[i]-'a';
            if(cur.kids[inx]==null) cur.kids[inx]=new Trie();//如果没有这个前缀 需要进行新建
            cur=cur.kids[inx];//继承下去
        }
        cur.isEnd=true;

    }
    
    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        //应该看匹配是否是结尾节点
        Trie node=fuhe(word);
        if(node==null) return false;
        return node!=null&&node.isEnd==true;

    }
    
    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        Trie node=fuhe(prefix);
        return node!=null;        

    }
//建立一个辅助函数，看看当前的这个前缀是否匹配，返回的①如果匹配，返回最后的节点 ②如果不匹配 那么返回null
    public Trie fuhe(String prefix){
        Trie cur=this;
        for(int i=0;i<prefix.length();i++){
            int inx=prefix.charAt(i)-'a';
            if(cur.kids[inx]==null) return null;
            cur=cur.kids[inx];
        }
        return cur;

    }
}

/**
 * Your Trie object will be instantiated and called as such:
 * Trie obj = new Trie();
 * obj.insert(word);
 * boolean param_2 = obj.search(word);
 * boolean param_3 = obj.startsWith(prefix);
 */

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(912. 排序数组)

https://leetcode-cn.com/problems/sort-an-array/

## 题目描述

```
给你一个整数数组 nums，请你将该数组升序排列。

 

示例 1：

输入：nums = [5,2,3,1]
输出：[1,2,3,5]


示例 2：

输入：nums = [5,1,1,2,0,0]
输出：[0,0,1,1,2,5]


 

提示：

1 <= nums.length <= 50000
-50000 <= nums[i] <= 50000
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
    public int[] sortArray(int[] nums) {
        mergeSort(nums,0,nums.length-1);
        return nums;
    }
    public void mergeSort(int[] arr, int left, int right) {
        //有两个重要的点 第一个是就是二分选择mid
        //第二个是根据下标来进行分开与分离，就是left到mid以及mid+1与right两段
        //第三个就是对两段进行合并merge函数


        //merge函数来讲的 需要提供left right以及mid 两个值  其实就是两个有序数组的合并

        //第一个数组 从nums的left到mid 第二个就是mid+1到right
        //就是第一组与第二组合并到一个新的临时数组当中 tem 大小为 right-left+1

        //然后把有序的tem 复制到num的 left下标到right下标当中
        //System.arraycopy(当前数组,当前数组的下标,目标数组,目标数组开始位置,要拷贝的长度); 

        if (left < right) {
            int mid = left + ((right - left) >> 1);
            mergeSort(arr,left,mid);
            mergeSort(arr,mid+1,right);
            merge(arr,left,mid,right);
        }
    } 
    //归并
    public void merge(int[] arr,int left, int mid, int right) {
        //第一步，定义一个新的临时数组
        int[] temparr = new int[right -left + 1];
        int temp1 = left, temp2 = mid + 1;
        int index = 0;
        //对应第二步，比较每个指针指向的值，小的存入大集合
        while (temp1 <= mid && temp2 <= right) {
            if (arr[temp1] <= arr[temp2]) {
                temparr[index++] = arr[temp1++];
            } else {
                temparr[index++] = arr[temp2++];
            }
        }
        //对应第三步，将某一小集合的剩余元素存到大集合中
        if (temp1 <= mid) System.arraycopy(arr, temp1, temparr, index, mid - temp1 + 1);
        if (temp2 <= right) System.arraycopy(arr, temp2, temparr, index, right -temp2 + 1);     
        //将大集合的元素复制回原数组
        System.arraycopy(temparr,0,arr,0+left,right-left+1); 
    }
}


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(98. 验证二叉搜索树)

https://leetcode-cn.com/problems/validate-binary-search-tree/

## 题目描述

```
给定一个二叉树，判断其是否是一个有效的二叉搜索树。

假设一个二叉搜索树具有如下特征：

节点的左子树只包含小于当前节点的数。
节点的右子树只包含大于当前节点的数。
所有左子树和右子树自身必须也是二叉搜索树。

示例 1:

输入:
    2
   / \
  1   3
输出: true


示例 2:

输入:
    5
   / \
  1   4
     / \
    3   6
输出: false
解释: 输入为: [5,1,4,null,null,3,6]。
     根节点的值为 5 ，但是其右子节点值为 4 。

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
    private List<Integer> res=new ArrayList<>();
    public boolean isValidBST(TreeNode root) {
        if(root==null) return true;
        //思路是不是来进行便利 保存当前值与前一个值
        //中序遍历之后当前值大于前一个值就是正常 否则返回false
        int pre=Integer.MIN_VALUE;
        dfs(root);
        for(int i=1;i<res.size();i++){
            if(res.get(i-1)>=res.get(i)) return false;
        }
        return true;

        

    }
    public void dfs(TreeNode root){
        if(root==null) return;
        dfs(root.left);
        res.add(root.val);
        dfs(root.right);

    }
}

```

#### 中序遍历递归

```java
class Solution {
    private List<Integer> res=new ArrayList<>();
    private long pre=Long.MIN_VALUE;//这里因为节点的值可能大于int上限，那么就可以直接使用long
    public boolean isValidBST(TreeNode root) {
        //递归中序遍历
        if(root==null) return true;
        boolean left=isValidBST(root.left);//左子树是否为搜索树
        if(!left) return false;
        
        if(pre>=root.val) return false; //看看当前节点是否大于上一个节点 不满足直接走
        pre=root.val;//无论如何都要赋值
        return isValidBST(root.right);//看右边是否满足就可以

       
    }
}


class Solution {
    private List<Integer> res=new ArrayList<>();
    private TreeNode pre=null;//因为是节点的替换 所以用pre代表节点
    public boolean isValidBST(TreeNode root) {
        //递归中序遍历
        if(root==null) return true;
        boolean left=isValidBST(root.left);//左子树是否为搜索树
        if(!left) return false;
        
        if(pre!=null&&pre.val>=root.val) return false; //看看当前节点是否大于上一个节点 不满足直接走,前提是不为空
        pre=root;//无论如何都要赋值
        return isValidBST(root.right);//看右边是否满足就可以

       
    }
}
```



**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$





## 题目地址(498. 对角线遍历)

https://leetcode-cn.com/problems/diagonal-traverse/

## 题目描述

```
给定一个含有 M x N 个元素的矩阵（M 行，N 列），请以对角线遍历的顺序返回这个矩阵中的所有元素，对角线遍历如下图所示。

 

示例:

输入:
[
 [ 1, 2, 3 ],
 [ 4, 5, 6 ],
 [ 7, 8, 9 ]
]

输出:  [1,2,4,7,5,3,6,8,9]

解释:



 

说明:

给定矩阵中的元素总数不会超过 100000 。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

[【对角线遍历】小白看过来，最直白易理解版本！！手把手解释代码！！！ - 对角线遍历 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/diagonal-traverse/solution/xiao-bai-kan-guo-lai-zui-zhi-bai-yi-li-jie-ban-ben/)

## 关键点

-  

## 代码

- 语言支持：Java

Java Code:

```java

class Solution{
public static int[] findDiagonalOrder(int[][] matrix) {
    //思路比较简单，就是分情况讨论
    //结果是一共走m+n-1趟，对2取余可以确定往右上还是左下
    //往右上走得时候 在范围之内时候 行-- 列++ 在范围之外了 要进行转弯时候
    //这时候分成两种情况 列在范围之内还是范围之外 再讨论

    //第一种  列在范围之内 m++就可以回来 第二种 列在范围之外m+=2 n--才可以

    //往左下走得时候 在范围之内时候 列-- 行++ 在范围之外了 要进行转弯时候
    //这时候分成两种情况 列在范围之内还是范围之外 再讨论

     //第一种  列在范围之内 n++就可以回来 第二种 列在范围之外n+=2 m--才可以

     if(matrix==null) return null;
     int m=matrix.length;
     int n=matrix[0].length;
     int[] res=new int[m*n];//结果集为m*n
     int x=0,y=0;//遍历元素的下标
     int inx=0;//结果数据集的下标
     for(int i=0;i<m+n-1;i++){//按对角线填充
         if(i%2==0){
             //正常情况 x y都在范围之内 x-- y++ 这样一直走
             while(x>=0&&y<n){
                 res[inx++]=matrix[x--][y++];
             }
             //一旦不满足条件，就跳出来 说明x或者y越界了

             //case1
             if(y<n){
                 
                 x++;
             }
             //case2
             else{
                 x+=2;
                 y--;
             }
         }
         else{
            //x与y对调就可以 n与m对调
             while(y>=0&&x<m){
                 res[inx++]=matrix[x++][y--];
             }
             //一旦不满足条件，就跳出来 说明x或者y越界了

             //case1
             if(x<m){
                 
                 y++;
             }
             //case2
             else{
                 y+=2;
                 x--;
             }

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

## 题目地址(83. 删除排序链表中的重复元素)

https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list/

## 题目描述

```
存在一个按升序排列的链表，给你这个链表的头节点 head ，请你删除所有重复的元素，使每个元素 只出现一次 。

返回同样按升序排列的结果链表。

 

示例 1：

输入：head = [1,1,2]
输出：[1,2]


示例 2：

输入：head = [1,1,2,3,3]
输出：[1,2,3]


 

提示：

链表中节点数目在范围 [0, 300] 内
-100 <= Node.val <= 100
题目数据保证链表已经按升序排列
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
    public ListNode deleteDuplicates(ListNode head) {
        //思路比较清晰 双指针
        //如果是相同的值 那么当前slow指针不变守住当前值  此时slow.next=fast.next fast一直往后走 直到不满足值相同

        //如果不相同 那么一起往前走

        //边界条件 链表为空或者单值 那么结果自身
        //跳出条件 你想想只有两个的时候 跳出的结果是slow.next==null为跳出条件
        if(head==null||head.next==null) return head;
        //返回值是head 因为无论如何这个值一定不会被删除
        ListNode slow=head;
        ListNode fast=head.next;
        while(slow.next!=null){
            if(slow.val==fast.val){
                //fast要往后走
                if(fast!=null){
                slow.next=fast.next;//要保证fast不为空
                fast=fast.next;
                }


            }
            else{
                slow=slow.next;
                fast=fast.next;
            }

        }
return head;



    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(112. 路径总和)

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
    public boolean hasPathSum(TreeNode root, int targetSum) {
        //思路 其实就是一个先左子树 后右子树的递归调用问题
        if(root==null) return false;//为空当然是不能成立啊
        boolean left=false,right=false;//用来保存左右子树结果
        targetSum-=root.val;
        if(root.left==null&&root.right==null&&targetSum==0) return true;//叶子节点的情况
        if(root.left!=null)  left=hasPathSum(root.left,targetSum);//这个期望值需要相应减少
        if(root.right!=null) right=hasPathSum(root.right,targetSum);
        return left||right;//只要有一个就可以啦
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(129. 求根节点到叶节点数字之和)

https://leetcode-cn.com/problems/sum-root-to-leaf-numbers/

## 题目描述

```
给你一个二叉树的根节点 root ，树中每个节点都存放有一个 0 到 9 之间的数字。

每条从根节点到叶节点的路径都代表一个数字：

例如，从根节点到叶节点的路径 1 -> 2 -> 3 表示数字 123 。

计算从根节点到叶节点生成的 所有数字之和 。

叶节点 是指没有子节点的节点。

 

示例 1：

输入：root = [1,2,3]
输出：25
解释：
从根到叶子节点路径 1->2 代表数字 12
从根到叶子节点路径 1->3 代表数字 13
因此，数字总和 = 12 + 13 = 25

示例 2：

输入：root = [4,9,0,5,1]
输出：1026
解释：
从根到叶子节点路径 4->9->5 代表数字 495
从根到叶子节点路径 4->9->1 代表数字 491
从根到叶子节点路径 4->0 代表数字 40
因此，数字总和 = 495 + 491 + 40 = 1026


 

提示：

树中节点的数目在范围 [1, 1000] 内
0 <= Node.val <= 9
树的深度不超过 10
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [0 ms 教科书级解答 - 求根节点到叶节点数字之和 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/sum-root-to-leaf-numbers/solution/0-ms-jiao-ke-shu-ji-jie-da-by-liuzhaoce/)

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
    public int sumNumbers(TreeNode root) {
        //直接进行递归就完事儿
        //每一个节点都会计算一个值 roog.val+sumed*10;
        return dfs(root,0);



    }
    public int dfs(TreeNode root,int sumed){
        if(root==null) return 0;
        sumed=sumed*10+root.val;
        if(root.left==null&&root.right==null) return sumed;//是末尾，说明可以进行返回
        return dfs(root.left,sumed)+dfs(root.right,sumed);
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(41. 缺失的第一个正数)

https://leetcode-cn.com/problems/first-missing-positive/

## 题目描述

```
给你一个未排序的整数数组 nums ，请你找出其中没有出现的最小的正整数。

请你实现时间复杂度为 O(n) 并且只使用常数级别额外空间的解决方案。

 

示例 1：

输入：nums = [1,2,0]
输出：3


示例 2：

输入：nums = [3,4,-1,1]
输出：2


示例 3：

输入：nums = [7,8,9,11,12]
输出：1


 

提示：

1 <= nums.length <= 5 * 105
-231 <= nums[i] <= 231 - 1
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
/*
思路③、使用座位交换法
      根据思路② 可知，缺失的第一个整数是 [1, len + 1] 之间，
      那么我们可以遍历数组，然后将对应的数据填充到对应的位置上去，比如 1 就填充到 nums[0] 的位置， 2 就填充到 nums[1]
      如果填充过程中， nums[i] < 1 && nums[i] > len，那么直接舍弃
      填充完成，我们再遍历一次数组，如果对应的 nums[i] != i + 1，那么这个 i + 1 就是缺失的第一个正数

      比如 nums = [7, 8, 9, 10, 11], len = 5
      我们发现数组中的元素都无法进行填充，直接舍弃跳过，
      那么最终遍历数组的时候，我们发现 nums[0] != 0 + 1，即第一个缺失的是 1 

      比如 nums = [3, 1, 2], len = 3
      填充过后，我们发现最终数组变成了 [1, 2, 3]，每个元素都对应了自己的位置，那么第一个缺失的就是 len + 1 == 4
*/
class Solution {
    public int firstMissingPositive(int[] nums) {
    //整体思路就是原地哈希，让每一个nums[i]的值放到这个nums[nums[i]-1]当中 比如 3放到下标为2的位置
    //怎么放呢？就是外层一个循环i 对于每一个i下标的值都要找到正确的位置 使用while不达目的不罢休
    //分析可以知道 因为找第一个正整数 一定是在1到len+1当中
    //len+1是所有都正确归位后自然返回的值  只要分析哪一个位置不匹配返回下标加一就可以

    //1 3 4  匹配后为 1 4 3 显然4不匹配 返回此时下标加一 为2



    for(int i=0;i<nums.length;i++){
        while(nums[i]>=1&&nums[i]<=nums.length&&nums[nums[i]-1]!=nums[i]){//满足条件开始交换
        //条件一 只有nums[i] 在1 到 len之间才能交换
        //条件二 当还没有归位的时候 
        //为什么用while？因为要保证i下标位置一定要完成结果，可能第一次交换不能成功
        swap(nums,nums[i]-1,i);

        }

    }
    for(int i=0;i<nums.length;i++){
            if(nums[i]!=i+1){
                return i+1;
            }
        }
        return nums.length+1;//说明都已经回归了
    //交换下标

    }
        public void swap(int[] nums,int left,int right){
        int tem=nums[left];
        nums[left]=nums[right];
        nums[right]=tem;
    }
}
```



```java

/*
思路③、使用座位交换法
      根据思路② 可知，缺失的第一个整数是 [1, len + 1] 之间，
      那么我们可以遍历数组，然后将对应的数据填充到对应的位置上去，比如 1 就填充到 nums[0] 的位置， 2 就填充到 nums[1]
      如果填充过程中， nums[i] < 1 && nums[i] > len，那么直接舍弃
      填充完成，我们再遍历一次数组，如果对应的 nums[i] != i + 1，那么这个 i + 1 就是缺失的第一个正数

      比如 nums = [7, 8, 9, 10, 11], len = 5
      我们发现数组中的元素都无法进行填充，直接舍弃跳过，
      那么最终遍历数组的时候，我们发现 nums[0] != 0 + 1，即第一个缺失的是 1 

      比如 nums = [3, 1, 2], len = 3
      填充过后，我们发现最终数组变成了 [1, 2, 3]，每个元素都对应了自己的位置，那么第一个缺失的就是 len + 1 == 4
*/
class Solution {
    public int firstMissingPositive(int[] nums) {

        int len = nums.length;
        for(int i = 0; i < len; i++){
       /*
       只有在 nums[i] 是 [1, len] 之间的数，并且不在自己应该呆的位置， nums[i] != i + 1 ，
        并且 它应该呆的位置没有被同伴占有（即存在重复值占有）	nums[nums[i] - 1] != nums[i] 的时候才进行交换
        	
        为什么使用 while ？ 因为交换后，原本 i 位置的 nums[i] 已经交换到了别的地方，
        交换后到这里的新值不一定是适合这个位置的，因此需要重新进行判断交换
        如果使用 if，那么进行一次交换后，i 就会 +1 进入下一个循环，那么交换过来的新值就没有去找到它该有的位置
         比如 nums = [3, 4, -1, 1] 当 3 进行交换后， nums 变成 [-1，4，3，1]，
         此时 i == 0，如果使用 if ，那么会进入下一个循环， 这个 -1 就没有进行处理
        */
            while(nums[i] > 0 && nums[i] <= len && nums[i] != i + 1 && nums[nums[i] - 1] != nums[i]){
                swap(nums, nums[i] - 1, i);
            }
        }
        for(int i = 0; i < len; i++){
            if(nums[i] != i + 1){
                return i + 1;
            }
        }
        return len + 1;
    }

    private void swap(int[] nums, int i, int j){
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(72. 编辑距离)

https://leetcode-cn.com/problems/edit-distance/

## 题目描述

```
给你两个单词 word1 和 word2，请你计算出将 word1 转换成 word2 所使用的最少操作数 。

你可以对一个单词进行如下三种操作：

插入一个字符
删除一个字符
替换一个字符

 

示例 1：

输入：word1 = "horse", word2 = "ros"
输出：3
解释：
horse -> rorse (将 'h' 替换为 'r')
rorse -> rose (删除 'r')
rose -> ros (删除 'e')


示例 2：

输入：word1 = "intention", word2 = "execution"
输出：5
解释：
intention -> inention (删除 't')
inention -> enention (将 'i' 替换为 'e')
enention -> exention (将 'n' 替换为 'x')
exention -> exection (将 'n' 替换为 'c')
exection -> execution (插入 'u')


 

提示：

0 <= word1.length, word2.length <= 500
word1 和 word2 由小写英文字母组成
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

```prolog
讲一下我自己对状态转移方程的理解,麻烦大家看看我说得对不对有没有道理:
(一)、当word1[i]==word2[j]时,由于遍历到了i和j,说明word1的0~i-1和word2的0~j-1的匹配结果已经生成,
由于当前两个字符相同,因此无需做任何操作,dp[i][j]=dp[i-1][j-1]
(二)、当word1[i]!=word2[j]时,可以进行的操作有3个:
      ① 替换操作:可能word1的0~i-1位置与word2的0~j-1位置的字符都相同,
           只是当前位置的字符不匹配,进行替换操作后两者变得相同,
           所以此时dp[i][j]=dp[i-1][j-1]+1(这个加1代表执行替换操作)
      ②删除操作:若此时word1的0~i-1位置与word2的0~j位置已经匹配了,
         此时多出了word1的i位置字符,应把它删除掉,才能使此时word1的0~i(这个i是执行了删除操作后新的i)
         和word2的0~j位置匹配,因此此时dp[i][j]=dp[i-1][j]+1(这个加1代表执行删除操作)
      ③插入操作:若此时word1的0~i位置只是和word2的0~j-1位置匹配,
          此时只需要在原来的i位置后面插入一个和word2的j位置相同的字符使得
          此时的word1的0~i(这个i是执行了插入操作后新的i)和word2的0~j匹配得上,
          所以此时dp[i][j]=dp[i][j-1]+1(这个加1代表执行插入操作)
      ④由于题目所要求的是要最少的操作数:所以当word1[i] != word2[j] 时,
          需要在这三个操作中选取一个最小的值赋格当前的dp[i][j]
(三)总结:状态方程为:
if(word1[i] == word2[j]):
      dp[i][j] = dp[i-1][j-1]
else:
       min(dp[i-1][j-1],dp[i-1][j],dp[i][j-1])+1
```

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int minDistance(String word1, String word2) {
        //思路确实比较难想，但是本质上还是状态转移
        //dp[i][j]的意义 在于从word1串 前i个字符串转变成word2的前j个字符串最少要进行操作的次数
        //因为我们想让i j代表真实的第几个串 因此需要dp[m+1][n+1];
        int m=word1.length();
        int n=word2.length();
        int[][] dp=new int[m+1][n+1];

        //需要进行初始化，第一行与第一列 （就是下标为0的一行一列）
        //分析一下 dp[i][0]表示 从前i个word1字符串转成word2的第一个字母的最少次数 i=0时候为0
        //i=1 替换操作就可以 1次  i=2  删除第二位 再替换 2次     i次
        //同理 第一行 也是 j次
        for(int i=1;i<=m;i++) dp[i][0]=i;
        
        for(int j=1;j<=n;j++) dp[0][j]=j;

        for(int i=1;i<=m;i++){
            for(int j=1;j<=n;j++){
                if(word1.charAt(i-1)==word2.charAt(j-1)) dp[i][j]=dp[i-1][j-1];//说明word1[i-1]与word2【j-1]一致不需要进行操作了 直接等于前边的
                else{
                    dp[i][j]=Math.min(Math.min(dp[i-1][j],dp[i-1][j-1]),dp[i][j-1])+1;

                    //dp[i-1][j]+1 表示删除，此时word1的0~i-1位置与word2的0~j位置已经匹配了,此时多出了word1的i位置字符,应把它删除掉,才能使此时word1的0~i(这个i是执行了删除操作后新的i)
                    //dp[i-1][j-1]+1表示替换，可能word1的0~i-1位置与word2的0~j-1位置的字符都相同,只是当前位置的字符不匹配,进行替换操作后两者变得相同,所以此时dp[i][j]=dp[i-1][j-1]+1(这个加1代表执行替换操作)
                    //dp[i][j-1]+1 表示插入 此时word1的0~i位置只是和word2的0~j-1位置匹配,此时只需要在原来的i位置后面插入一个和word2的j位置相同的字符使得此时的word1的0~i(这个i是执行了插入操作后新的i)和word2的0~j匹配得上,所以此时dp[i][j]=dp[i][j-1]+1(这个加1代表执行插入操作)
                }
            }
        }
        return dp[m][n];
        


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(46. 全排列)

https://leetcode-cn.com/problems/permutations/

## 题目描述

```
给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。

 

示例 1：

输入：nums = [1,2,3]
输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]


示例 2：

输入：nums = [0,1]
输出：[[0,1],[1,0]]


示例 3：

输入：nums = [1]
输出：[[1]]


 

提示：

1 <= nums.length <= 6
-10 <= nums[i] <= 10
nums 中的所有整数 互不相同
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
    public List<List<Integer>> permute(int[] nums) {
        //全排列直接来做就完事儿了，回溯算法
        List<List<Integer>> res=new ArrayList<>();//存储最终结果
        List<Integer> ans=new ArrayList<>();//存储每一个单独的排列结果
        Pai(nums,res,ans);
        return res;


//重点还是在于

    }
    public void Pai(int[] nums,List<List<Integer>> res, List<Integer> ans){
        if(ans.size()==nums.length) res.add(new ArrayList<>(ans));//注意是引用传递 如果不新增ans内容而是ans 最后的结果都是ans最后的结果 就是[]
        for(int i=0;i<nums.length;i++){
            if(ans.contains(nums[i])) continue;
            ans.add(nums[i]);
            Pai(nums,res,ans);
            ans.remove(ans.size()-1);
        }
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(78. 子集)

https://leetcode-cn.com/problems/subsets/

## 题目描述

```
给你一个整数数组 nums ，数组中的元素 互不相同 。返回该数组所有可能的子集（幂集）。

解集 不能 包含重复的子集。你可以按 任意顺序 返回解集。

 

示例 1：

输入：nums = [1,2,3]
输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]


示例 2：

输入：nums = [0]
输出：[[],[0]]


 

提示：

1 <= nums.length <= 10
-10 <= nums[i] <= 10
nums 中的所有元素 互不相同
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
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();//这个和全排列完全一样的思路好不好 存储的结果
        List<Integer> tem=new ArrayList<Integer>();//存储每一个点
        backtrack(0, nums, res, tem);
        return res;

    }

    private void backtrack(int i, int[] nums, List<List<Integer>> res, List<Integer> tmp) {
        res.add(new ArrayList<>(tmp));//这里不一样，因为是子集，所以每一个无论什么都要进行弄进去
        for (int j = i; j < nums.length; j++) {
            tmp.add(nums[j]);//往后走 这样不会重复 先是1开头 [] [1] [1,2] [1,2,3] 再是2开头 [2] [2,3] 再是3开头 [3]
            backtrack(j + 1, nums, res, tmp);
            tmp.remove(tmp.size() - 1);
        }
    }
}


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(165. 比较版本号)

https://leetcode-cn.com/problems/compare-version-numbers/

## 题目描述

```
给你两个版本号 version1 和 version2 ，请你比较它们。

版本号由一个或多个修订号组成，各修订号由一个 '.' 连接。每个修订号由 多位数字 组成，可能包含 前导零 。每个版本号至少包含一个字符。修订号从左到右编号，下标从 0 开始，最左边的修订号下标为 0 ，下一个修订号下标为 1 ，以此类推。例如，2.5.33 和 0.1 都是有效的版本号。

比较版本号时，请按从左到右的顺序依次比较它们的修订号。比较修订号时，只需比较 忽略任何前导零后的整数值 。也就是说，修订号 1 和修订号 001 相等 。如果版本号没有指定某个下标处的修订号，则该修订号视为 0 。例如，版本 1.0 小于版本 1.1 ，因为它们下标为 0 的修订号相同，而下标为 1 的修订号分别为 0 和 1 ，0 < 1 。

返回规则如下：

如果 version1 > version2 返回 1，
如果 version1 < version2 返回 -1，
除此之外返回 0。

 

示例 1：

输入：version1 = "1.01", version2 = "1.001"
输出：0
解释：忽略前导零，"01" 和 "001" 都表示相同的整数 "1"


示例 2：

输入：version1 = "1.0", version2 = "1.0.0"
输出：0
解释：version1 没有指定下标为 2 的修订号，即视为 "0"


示例 3：

输入：version1 = "0.1", version2 = "1.1"
输出：-1
解释：version1 中下标为 0 的修订号是 "0"，version2 中下标为 0 的修订号是 "1" 。0 < 1，所以 version1 < version2


示例 4：

输入：version1 = "1.0.1", version2 = "1"
输出：1


示例 5：

输入：version1 = "7.5.2.4", version2 = "7.5.3"
输出：-1


 

提示：

1 <= version1.length, version2.length <= 500
version1 和 version2 仅包含数字和 '.'
version1 和 version2 都是 有效版本号
version1 和 version2 的所有修订号都可以存储在 32 位整数 中
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
    public int compareVersion(String version1, String version2) {
        //第一个就是函数的具体问题，就是计算两个.之间的数字，保证位置相同
        int m=version1.length();
        int n=version2.length();
        int len=Math.max(m,n);//要一直比较到最后才可以 
        int inx1=0,inx2=0;//两个指针
        while(inx1<len||inx2<len){
            int val1=0;
            int val2=0;//存储.之间的值
            while(inx1<m&&version1.charAt(inx1)!='.'){
                val1=val1*10+version1.charAt(inx1)-'0';
                inx1++;
            }//val1是一个值
            while(inx2<n&&version2.charAt(inx2)!='.'){
                val2=val2*10+version2.charAt(inx2)-'0';
                inx2++;
            }//val2是一个值
            if(val1>val2) return 1;
            if(val1<val2) return -1;

            //为了越过"."
            inx1++;
            inx2++;



        }
        //一直相同
        return 0;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(82. 删除排序链表中的重复元素 II)

https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list-ii/

## 题目描述

```
存在一个按升序排列的链表，给你这个链表的头节点 head ，请你删除链表中所有存在数字重复情况的节点，只保留原始链表中 没有重复出现 的数字。

返回同样按升序排列的结果链表。

 

示例 1：

输入：head = [1,2,3,3,4,4,5]
输出：[1,2,5]


示例 2：

输入：head = [1,1,1,2,3]
输出：[2,3]


 

提示：

链表中节点数目在范围 [0, 300] 内
-100 <= Node.val <= 100
题目数据保证链表已经按升序排列
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
    public ListNode deleteDuplicates(ListNode head) {
        //这个就是度小满的题目啊啊啊，让自己铩羽而归的题目 必须拿下
        //注意这个题是删除全部重复的链表
        //思路就是双指针pre与cur 如果 cur.val==cur.next.val 那么就继续往下走 cur=cur.next
        //直到cur.next.val !=cur.val 直接不要cur了（ 到时候pre.next=cur.next接上新的就可以）;

        //以上显然使用的是while循环
        
        //以上的可能会导致 pre.next不在等于cur 
        //case1: cur和pre不再相邻 说明cur这个节点重复了 那么pre.next=cur.next cur=cur.next;
        //case2:cur 与pre相邻 说明cur节点没有重复 直接pre=pre.next cur=cur.next 往后移动
        ListNode dummy=new ListNode(-1);
        dummy.next=head;//注意哨兵节点的话 head可能被删除
        ListNode cur=head;
        ListNode pre=dummy;
        while(cur!=null){
            while(cur.next!=null&&cur!=null&&cur.val==cur.next.val){
                //注意cur不等于nuLL还是要放在前边判断是合理的
                cur=cur.next;
            }
            if(pre.next==cur){
                pre=pre.next;
                cur=cur.next;
            }
            else{
                pre.next=cur.next;
                cur=cur.next;
            }
        }
        
        return dummy.next;
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(39. 组合总和)

https://leetcode-cn.com/problems/combination-sum/

## 题目描述

```
给定一个无重复元素的数组 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。

candidates 中的数字可以无限制重复被选取。

说明：

所有数字（包括 target）都是正整数。
解集不能包含重复的组合。 

示例 1：

输入：candidates = [2,3,6,7], target = 7,
所求解集为：
[
  [7],
  [2,2,3]
]


示例 2：

输入：candidates = [2,3,5], target = 8,
所求解集为：
[
  [2,2,2,2],
  [2,3,3],
  [3,5]
]

 

提示：

1 <= candidates.length <= 30
1 <= candidates[i] <= 200
candidate 中的每个元素都是独一无二的。
1 <= target <= 500
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  // 注意：由于每一个元素可以重复使用，下一轮搜索的起点依然是 i，这里非常容易弄错

## 代码

- 语言支持：Java

Java Code:

```java

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

//其实完全就是回溯的思路
//和全排列那个题一模一样 为了防止重复添加

public class Solution {

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        int len = candidates.length;
        List<List<Integer>> res = new ArrayList<>();
        if (len == 0) {
            return res;
        }

        Deque<Integer> path = new ArrayDeque<>();
        dfs(candidates, 0, len, target, path, res);
        return res;
    }

    /**
     * @param candidates 候选数组
     * @param begin      搜索起点
     * @param len        冗余变量，是 candidates 里的属性，可以不传
     * @param target     每减去一个元素，目标值变小
     * @param path       从根结点到叶子结点的路径，是一个栈
     * @param res        结果集列表
     */
    private void dfs(int[] candidates, int begin, int len, int target, Deque<Integer> path, List<List<Integer>> res) {
        // target 为负数和 0 的时候不再产生新的孩子结点
        if (target < 0) {
            return;
        }
        if (target == 0) {
            res.add(new ArrayList<>(path));
            return;
        }

        // 重点理解这里从 begin 开始搜索的语意
        for (int i = begin; i < len; i++) {
            path.addLast(candidates[i]);

            // 注意：由于每一个元素可以重复使用，下一轮搜索的起点依然是 i，这里非常容易弄错
            dfs(candidates, i, len, target - candidates[i], path, res);

            // 状态重置
            path.removeLast();
        }
    }
}





//其实完全就是回溯的思路
//和全排列那个题一模一样 为了防止重复添加

public class Solution {

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res=new ArrayList<>();//结果
        List<Integer> path=new ArrayList<>();//路径结果
        if(candidates.length==0) return res;
        dfs(candidates,target,res,path,0);
        return res;
       
    }
    public void dfs(int[] candidates,int target,List<List<Integer>> res,List<Integer> path,int begin){
        if(target==0){
            res.add(new ArrayList<>(path));
            return;
        }
        if(target<0){
            return;
        }

        //这里需要明确 第一：需要有一个下标来说明下一个数只能从自身以及以后来取
        //第二 迭代说明从i开始取
        for(int i=begin;i<candidates.length;i++){
            path.add(candidates[i]);
            dfs(candidates,target-candidates[i],res,path,i);// 注意：由于每一个元素可以重复使用，下一轮搜索的起点依然是 i，这里非常容易弄错
            path.remove(path.size()-1);

        }
    }
}




```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(64. 最小路径和)

https://leetcode-cn.com/problems/minimum-path-sum/

## 题目描述

```
给定一个包含非负整数的 m x n 网格 grid ，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。

说明：每次只能向下或者向右移动一步。

 

示例 1：

输入：grid = [[1,3,1],[1,5,1],[4,2,1]]
输出：7
解释：因为路径 1→3→1→1→1 的总和最小。


示例 2：

输入：grid = [[1,2,3],[4,5,6]]
输出：12


 

提示：

m == grid.length
n == grid[i].length
1 <= m, n <= 200
0 <= grid[i][j] <= 100
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  要在原始上进行操作，不要一上来就那样无脑dp 看清楚再使用

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int minPathSum(int[][] grid) {
        //路径规划应该没啥问题
        //输出的是最终的结果
        int m=grid.length;
        int n=grid[0].length;
        //int[][] dp=new int[m][n];

        //实际上只要在原数祖上进行操作就可以啦

        //知道错在那儿了吧啊啊啊 要用grid来进行求取啊啊啊
        //初始化第一行第一列
        for(int i=1;i<m;i++){
            grid[i][0]+=grid[i-1][0];
        }
        for(int j=1;j<n;j++){
            grid[0][j]+=grid[0][j-1];

        }
        for(int i=1;i<m;i++){
            for(int j=1;j<n;j++){
                grid[i][j]=Math.min(grid[i-1][j],grid[i][j-1])+grid[i][j];
            }
        }
        return grid[m-1][n-1];

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## ==题目地址(76. 最小覆盖子串)==

https://leetcode-cn.com/problems/minimum-window-substring/

## 题目描述

```
给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。

注意：如果 s 中存在这样的子串，我们保证它是唯一的答案。

 

示例 1：

输入：s = "ADOBECODEBANC", t = "ABC"
输出："BANC"


示例 2：

输入：s = "a", t = "a"
输出："a"


 

提示：

1 <= s.length, t.length <= 105
s 和 t 由英文字母组成

 

进阶：你能设计一个在 o(n) 时间内解决此问题的算法吗？
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
    public String minWindow(String s, String t) {
        if (s == null || s.length() == 0 || t == null || t.length() == 0){
            return "";
        }
        int[] need = new int[128];//注意什么都数都存 只要是在串口当中的
        //记录需要的字符的个数
        for (int i = 0; i < t.length(); i++) {//一开始进行遍历就可以。只有需要的字符为正数
            need[t.charAt(i)]++;
        }
        //l是当前左边界，r是当前右边界，size记录窗口大小，count是需求的字符个数，start是最小覆盖串开始的index
        int l = 0, r = 0, size = Integer.MAX_VALUE, count = t.length(), start = 0;
        //遍历所有字符
        while (r < s.length()) {
            char c = s.charAt(r);//窗口右端的字符

            //case1 如果查看need数组，如果需要，需求数组减一
            if (need[c] > 0) {//需要字符c
                count--;
            }
            need[c]--;//无论什么，需求数组都要减一 

            //如果都满足了，就要取舍选出最少的字符组合
            if (count == 0) {//窗口中已经包含所有字符

            //减少左边界
                while (l < r && need[s.charAt(l)] < 0) {
                    need[s.charAt(l)]++;//如果左边窗口是有重合的话，直接need数组减一 并且左指针右移动
                    l++;//指针右移
                }
                //跳出来之后l位置就是一个必须的字符所在位置 

            //减少左边界之后 重新更改长度 与左边位置
                if (r - l + 1 < size) {//不能右移时候挑战最小窗口大小，更新最小窗口开始的start
                    size = r - l + 1;
                    start = l;//记录下最小值时候的开始位置，最后返回覆盖串时候会用到
                }
                //l向右移动后窗口肯定不能满足了 重新开始循环

                //此时把l往右移动 此时这个窗口不能再满足了 需要往右走
                need[s.charAt(l)]++;
                l++;
                count++;
            }
            r++;
        }
        return size == Integer.MAX_VALUE ? "" : s.substring(start, start + size);
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(198. 打家劫舍)

https://leetcode-cn.com/problems/house-robber/

## 题目描述

```
你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。

给定一个代表每个房屋存放金额的非负整数数组，计算你 不触动警报装置的情况下 ，一夜之内能够偷窃到的最高金额。

 

示例 1：

输入：[1,2,3,1]
输出：4
解释：偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。
     偷窃到的最高金额 = 1 + 3 = 4 。

示例 2：

输入：[2,7,9,3,1]
输出：12
解释：偷窃 1 号房屋 (金额 = 2), 偷窃 3 号房屋 (金额 = 9)，接着偷窃 5 号房屋 (金额 = 1)。
     偷窃到的最高金额 = 2 + 9 + 1 = 12 。


 

提示：

1 <= nums.length <= 100
0 <= nums[i] <= 400
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  初始值的确定啊

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int rob(int[] nums) {
        //dp[i]代表偷到第 i间房能偷得最高的金额
        int n=nums.length;
        //注意难点就是函数的取值问题好不好，第一个和第二个值究竟如何
        int[] dp=new int[n];
        if(n==1) return nums[0];
        if(n==2) return Math.max(nums[0],nums[1]);
        dp[0]=nums[0];
        dp[1]=Math.max(nums[0],nums[1]);
        //转移方程 dp[i]=max(dp[i-2]+nums[i],dp[i-1])
        for(int i=2;i<n;i++){
            dp[i]=Math.max(dp[i-1],dp[i-2]+nums[i]);
        }
        return dp[n-1];


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(145. 二叉树的后序遍历)

https://leetcode-cn.com/problems/binary-tree-postorder-traversal/

## 题目描述

```
给定一个二叉树，返回它的 后序 遍历。

示例:

输入: [1,null,2,3]  
   1
    \
     2
    /
   3 

输出: [3,2,1]

进阶: 递归算法很简单，你可以通过迭代算法完成吗？
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
    private List<Integer> res=new ArrayList<>();
    public List<Integer> postorderTraversal(TreeNode root) {
        if(root==null) return res;
        postor(root);
        return res;

        


    }
    public void postor(TreeNode root) {
        if(root==null) return;
        postor(root.left);
        postor(root.right);
        res.add(root.val);
        


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(22. 括号生成)

https://leetcode-cn.com/problems/generate-parentheses/

## 题目描述

```
数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。

 

示例 1：

输入：n = 3
输出：["((()))","(()())","(())()","()(())","()()()"]


示例 2：

输入：n = 1
输出：["()"]


 

提示：

1 <= n <= 8
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
import java.util.ArrayList;
import java.util.List;
//思路相对easy

public class Solution {

    // 做加法
    //那个图还是需要看人家的题解好不好

    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();//一个存储的结果集
        // 特判
        if (n == 0) {//
            return res;
        }

        dfs("", 0, 0, n, res);
        return res;
    }

    /**
     * @param curStr 当前递归得到的结果，貌似不用和arraylist一样重新新生成new  因为string直接就是每一个都是新的
     * @param left   左括号已经用了几个
     * @param right  右括号已经用了几个
     * @param n      左右括号的对数已经使用的 
     * @param res    结果集
     */
    private void dfs(String curStr, int left, int right, int n, List<String> res) {
        if (left == n && right == n) {
            res.add(curStr);//不需要new了，只有满足条件才能加入
            return;
        }

        // 剪枝
        if (left < right) {//因为这个括号产生是有顺序的 只要当前左括号个数少于右括号，显然一定是不可能再生成结果了
            return;
        }

        if (left < n) {//只要小于左边就可以继续进行
            dfs(curStr + "(", left + 1, right, n, res);
        }
        if (right < n) {//只要少于n就可以
            dfs(curStr + ")", left, right + 1, n, res);
        }
    }
}


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(394. 字符串解码)

https://leetcode-cn.com/problems/decode-string/

## 题目描述

```
给定一个经过编码的字符串，返回它解码后的字符串。

编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。

你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。

此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。

 

示例 1：

输入：s = "3[a]2[bc]"
输出："aaabcbc"


示例 2：

输入：s = "3[a2[c]]"
输出："accaccacc"


示例 3：

输入：s = "2[abc]3[cd]ef"
输出："abcabccdcdcdef"


示例 4：

输入：s = "abc3[cd]xyz"
输出："abccdcdcdxyz"

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
    public String decodeString(String s) {
        StringBuilder res = new StringBuilder();//使用stringbuilder来存储这个结果是最好的，比较灵活
        int multi = 0;//存储当前的倍数
        LinkedList<Integer> stack_multi = new LinkedList<>();//栈顶元素存储上一个[到当前[之间的数字倍数last_mul
        LinkedList<String> stack_res = new LinkedList<>();//栈顶元素存储上一个[到当前[之间的字母组合内容last_tem
        
        //其实公式就是 res=last_tem+last_mul*tem
        
        //
        for(Character c : s.toCharArray()) {
            //碰到[就进栈，保存上一个倍数与小段字母组合
            if(c == '[') {
                stack_multi.addLast(multi);
                stack_res.addLast(res.toString());
                //然后倍数与字母组合置0置空
                multi = 0;
                res = new StringBuilder();
            }
            //碰到] 就套用公式就可以  当前的字母组合+数字倍数得到的结果res有了
            //新的res=last_mul*res+last_tem
            else if(c == ']') {
                StringBuilder tmp = new StringBuilder();
                int cur_multi = stack_multi.removeLast();
                for(int i = 0; i < cur_multi; i++) tmp.append(res);
                res = new StringBuilder(stack_res.removeLast() + tmp);
            }
            //如果是数字 存起来
            else if(c >= '0' && c <= '9') multi = multi * 10 + Integer.parseInt(c + "");
            //如果是字母 加入res就可以啦
            else res.append(c);
        }
        return res.toString();
    }
}




class Solution {
    public String decodeString(String s) {
        //存储每一段字母组合结果的东西
        StringBuilder res=new StringBuilder();
        //存储每一段数字的东西
        int mul=0;
        Stack<String> last_res=new Stack<>();//存储上一个字母组合
        Stack<Integer> last_mul=new Stack<>();//存储上一个倍数
        char[] ss=s.toCharArray();
        for(int i=0;i<ss.length;i++){
            if(ss[i]>='0'&&ss[i]<='9') mul=mul*10+ss[i]-'0';
            else if(ss[i]=='['){
                last_res.push(res.toString());
                last_mul.push(mul);
                mul=0;
                res=new StringBuilder();
            }
            else if(ss[i]==']'){
                int last_bei=last_mul.pop();//上一个倍数
                String last_string=last_res.pop();
                StringBuilder tem=new StringBuilder();
                for(int j=0;j<last_bei;j++) tem.append(res);//乘以当前的值
                res=new StringBuilder(last_string+tem.toString());
            }
            else res.append(ss[i]);
        }
        return res.toString();

        
    }
}



```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(518. 零钱兑换 II)

https://leetcode-cn.com/problems/coin-change-2/

## 题目描述

```
给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。

请你计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0 。

假设每一种面额的硬币有无限个。 

题目数据保证结果符合 32 位带符号整数。

 

示例 1：

输入：amount = 5, coins = [1, 2, 5]
输出：4
解释：有四种方式可以凑成总金额：
5=5
5=2+2+1
5=2+1+1+1
5=1+1+1+1+1


示例 2：

输入：amount = 3, coins = [2]
输出：0
解释：只用面额 2 的硬币不能凑成总金额 3 。


示例 3：

输入：amount = 10, coins = [10] 
输出：1


 

提示：

1 <= coins.length <= 300
1 <= coins[i] <= 5000
coins 中的所有值 互不相同
0 <= amount <= 5000
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [【breakman解书】超基础全方位入门本题 - 零钱兑换 II - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/coin-change-2/solution/breakmanjie-shu-chao-ji-chu-quan-fang-we-eyja/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int change(int amount, int[] coins) {
        //思路使用二维的动态规划来进行完成
        //dp[i][j]代表使用前i种硬币组成j面额的组合数目
        //注意 这里的i代表是真的 i正常从1开始
        int m=coins.length;
        int[][] dp=new int[m+1][amount+1];
        //初始化
        
        //当j=0时候 可以选择不使用硬币这一种组合方式 所以为1
        for(int i=0;i<=m;i++){
            dp[i][0]=1;
        }
        //当i=0时候 不使用硬币 所以组合数目一定为0
        for(int j=1;j<=amount;j++) dp[0][j]=0;

        //转移方程
        //dp[i][j]分成两部分 使用第i种硬币情况+不使用第i种硬币
        //不使用第i种硬币情况 相当于前i-1种组成j dp[i-1][j]
        //使用第i种硬币情况，因为使用的个数可能有多种类 等价于dp[i-1][j-1*val]+dp[i-1][j-2*val]+*****
        //直到j-n*val<才可以停止 

        //转移方程为dp[i][j]=dp[i-1][j-n*val] j-n*val>=0&&n>=0;
        for(int i=1;i<=m;i++){
            for(int j=1;j<=amount;j++){
                int val=coins[i-1];//第i个硬币对应coins[i-1]
                for(int n=0;j-n*val>=0;n++){
                    dp[i][j]+=dp[i-1][j-n*val];
                }


            }
        }
        return dp[m][amount];


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(138. 复制带随机指针的链表)

https://leetcode-cn.com/problems/copy-list-with-random-pointer/

## 题目描述

```
给你一个长度为 n 的链表，每个节点包含一个额外增加的随机指针 random ，该指针可以指向链表中的任何节点或空节点。

构造这个链表的 深拷贝。 深拷贝应该正好由 n 个 全新 节点组成，其中每个新节点的值都设为其对应的原节点的值。新节点的 next 指针和 random 指针也都应指向复制链表中的新节点，并使原链表和复制链表中的这些指针能够表示相同的链表状态。复制链表中的指针都不应指向原链表中的节点 。

例如，如果原链表中有 X 和 Y 两个节点，其中 X.random --> Y 。那么在复制链表中对应的两个节点 x 和 y ，同样有 x.random --> y 。

返回复制链表的头节点。

用一个由 n 个节点组成的链表来表示输入/输出中的链表。每个节点用一个 [val, random_index] 表示：

val：一个表示 Node.val 的整数。
random_index：随机指针指向的节点索引（范围从 0 到 n-1）；如果不指向任何节点，则为  null 。

你的代码 只 接受原链表的头节点 head 作为传入参数。

 

示例 1：

输入：head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
输出：[[7,null],[13,0],[11,4],[10,2],[1,0]]


示例 2：

输入：head = [[1,1],[2,1]]
输出：[[1,1],[2,1]]


示例 3：

输入：head = [[3,null],[3,0],[3,null]]
输出：[[3,null],[3,0],[3,null]]


示例 4：

输入：head = []
输出：[]
解释：给定的链表为空（空指针），因此返回 null。


 

提示：

0 <= n <= 1000
-10000 <= Node.val <= 10000
Node.random 为空（null）或指向链表中的节点。
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

/*
// Definition for a Node.
class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}
*/

class Solution {
    public Node copyRandomList(Node head) {
        //直接使用hashmap 存储就可以HashMap<Node,Node>
        //注意新建时候使用构造函数就可以保证val一致
        //后续需要进行指针的赋值就完事儿
        HashMap<Node,Node> res=new HashMap<>();
        Node dummy=new Node(-1);
        dummy.next=head;
        Node poi=dummy.next;
        Node poi2=dummy.next;
        if(poi==null) return poi;
        while(poi!=null){
            res.put(poi,new Node(poi.val));
            poi=poi.next;
        }
        while(poi2!=null){
            res.get(poi2).next=res.get(poi2.next);
            res.get(poi2).random=res.get(poi2.random);
            poi2=poi2.next;
        }
        return res.get(head);
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(209. 长度最小的子数组)

https://leetcode-cn.com/problems/minimum-size-subarray-sum/

## 题目描述

```
给定一个含有 n 个正整数的数组和一个正整数 target 。

找出该数组中满足其和 ≥ target 的长度最小的 连续子数组 [numsl, numsl+1, ..., numsr-1, numsr] ，并返回其长度。如果不存在符合条件的子数组，返回 0 。

 

示例 1：

输入：target = 7, nums = [2,3,1,2,4,3]
输出：2
解释：子数组 [4,3] 是该条件下的长度最小的子数组。


示例 2：

输入：target = 4, nums = [1,4,4]
输出：1


示例 3：

输入：target = 11, nums = [1,1,1,1,1,1,1,1]
输出：0


 

提示：

1 <= target <= 109
1 <= nums.length <= 105
1 <= nums[i] <= 105

 

进阶：

如果你已经实现 O(n) 时间复杂度的解法, 请尝试设计一个 O(n log(n)) 时间复杂度的解法。
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

class Solution{
public int minSubArrayLen(int s, int[] nums) {
    
    int n = nums.length;
    if (n == 0) {
        return 0;
    }
    int left = 0;
    int right = 0;
    int sum = 0;
    int min = Integer.MAX_VALUE;
    //模拟出队入队的情况
    while (right < n) {
        sum += nums[right];
        
        while (sum >= s) {
            min = Math.min(min, right - left+1);//满足条件就求长度
            sum -= nums[left];//尝试减少左边界，如果还满足就求长度比较
            left++;
        }
        right++;
    }
    return min == Integer.MAX_VALUE ? 0 : min;//看最后结果就可
}
}


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(102. 二叉树的层序遍历)

https://leetcode-cn.com/problems/binary-tree-level-order-traversal/

## 题目描述

```
给你一个二叉树，请你返回其按 层序遍历 得到的节点值。 （即逐层地，从左到右访问所有节点）。

 

示例：
二叉树：[3,9,20,null,null,15,7],

    3
   / \
  9  20
    /  \
   15   7


返回其层序遍历结果：

[
  [3],
  [9,20],
  [15,7]
]

```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  注意三点
-  结果list<list<Integer>> res =new list<>();
-  队列使用linkedlist来当成生成 Queue<TreeNode> res1=new LinkedList<>();存储节点好不好
-  注意queue用法 add/remove  offer/poll 后边进 前边出

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
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */


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
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        //if(root==null){
        //    return new List<Integer>();
        //}
        
        List<List<Integer>> res=new ArrayList<>();//最终结果
        if(root==null) return res;
        Queue<TreeNode> res1=new LinkedList<>();//中间结果
        res1.add(root);
        while(!res1.isEmpty()){
            int n=res1.size();
            List<Integer> temp=new ArrayList();
            for(int i=0;i<n;i++){
                TreeNode node=res1.remove();
                temp.add(node.val);
                if(node.left!=null){
                    res1.add(node.left);
                }
                if(node.right!=null){
                    res1.add(node.right);
                }
            }
            res.add(temp);
        }
        return res;


    }
}












class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        //if(root==null){
        //    return new List<Integer>();
        //}
        
        List<List<Integer>> res=new ArrayList<>();//最终结果
        if(root==null) return res;
        Queue<TreeNode> res1=new ArrayDeque<>();//中间结果
        res1.offer(root);
        while(!res1.isEmpty()){
            int n=res1.size();
            List<Integer> temp=new ArrayList();
            for(int i=0;i<n;i++){
                TreeNode node=res1.poll();
                temp.add(node.val);
                if(node.left!=null){
                    res1.offer(node.left);
                }
                if(node.right!=null){
                    res1.offer(node.right);
                }
            }
            res.add(temp);
        }
        return res;


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(221. 最大正方形)

https://leetcode-cn.com/problems/maximal-square/

## 题目描述

```
在一个由 '0' 和 '1' 组成的二维矩阵内，找到只包含 '1' 的最大正方形，并返回其面积。

 

示例 1：

输入：matrix = [["1","0","1","0","0"],["1","0","1","1","1"],["1","1","1","1","1"],["1","0","0","1","0"]]
输出：4


示例 2：

输入：matrix = [["0","1"],["1","0"]]
输出：1


示例 3：

输入：matrix = [["0"]]
输出：0


 

提示：

m == matrix.length
n == matrix[i].length
1 <= m, n <= 300
matrix[i][j] 为 '0' 或 '1'
```

## 前置知识

- 

## 公司

- 暂无

## 思路

[理解 三者取最小+1 - 最大正方形 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/maximal-square/solution/li-jie-san-zhe-qu-zui-xiao-1-by-lzhlyle/)

## 关键点

![image-20210616153521411](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210616153521411.png)



## 代码

- 语言支持：Java

Java Code:

```java

class Solution{
public int maximalSquare(char[][] matrix) {
    // 明确那个具体的算法过程，状态转移方程问题
    //dp[i][j]表示以matrix[i-1][j-1]为右下角的最大正方形边长 记住i ,j是从1开始 i=0 j=0需要进行初始化
    //如果当前matrix[i][j]==1 ，那么dp[i+1][j+1]也是合理的
    if (matrix == null || matrix.length < 1 || matrix[0].length < 1) return 0;

    int height = matrix.length;//高度
    int width = matrix[0].length;//宽度
    int maxSize = 0;//存储最大边长
    
    int[][] dp=new int[height+1][width+1];
    //if(matrix[i][j]==1) dp[i+1][j+1]=Math.max(Math.max(dp[i][j],dp[i+1][j]),dp[i][j+1])+1;
    for(int i=0;i<height;i++){
        for(int j=0;j<width;j++){
            if(matrix[i][j]=='1'){
                dp[i+1][j+1]=Math.max(Math.max(dp[i][j],dp[i+1][j]),dp[i][j+1])+1;
                maxSize=Math.max(maxSize,dp[i+1][j+1]);
            }
        }
    }
    return maxSize*maxSize;




    
}
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(189. 旋转数组)

https://leetcode-cn.com/problems/rotate-array/

## 题目描述

```
给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。

 

进阶：

尽可能想出更多的解决方案，至少有三种不同的方法可以解决这个问题。
你可以使用空间复杂度为 O(1) 的 原地 算法解决这个问题吗？

 

示例 1:

输入: nums = [1,2,3,4,5,6,7], k = 3
输出: [5,6,7,1,2,3,4]
解释:
向右旋转 1 步: [7,1,2,3,4,5,6]
向右旋转 2 步: [6,7,1,2,3,4,5]
向右旋转 3 步: [5,6,7,1,2,3,4]


示例 2:

输入：nums = [-1,-100,3,99], k = 2
输出：[3,99,-1,-100]
解释: 
向右旋转 1 步: [99,-1,-100,3]
向右旋转 2 步: [3,99,-1,-100]

 

提示：

1 <= nums.length <= 2 * 104
-231 <= nums[i] <= 231 - 1
0 <= k <= 105
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  思路很重要
-  [【数组翻转】旋转数组 - 旋转数组 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/rotate-array/solution/shu-zu-fan-zhuan-xuan-zhuan-shu-zu-by-de-5937/)这个很不错

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public void rotate(int[] nums, int k) {
        //思路最简单的就是 第一步整体翻转 第二步分部分间隔翻转就可以
        k=k%nums.length;//别忘记取余数
        rever(nums,0,nums.length-1);//整体翻转
        rever(nums,0,k-1);//前边部分翻转
        rever(nums,k,nums.length-1);//后边部分翻转


    }
    public void rever(int[] nums,int left,int right){
        //在下标left到right之间进行翻转
        if(left>=right) return;
        int l=left;
        int r=right;
        //很简单的感觉
        while(l<r){
            int tem=nums[l];
            nums[l]=nums[r];
            nums[r]=tem; 
            l++;
            r--;   
        }
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(40. 最小的k个数)

https://leetcode-cn.com/problems/zui-xiao-de-kge-shu-lcof/

## 题目描述

```
输入整数数组 arr ，找出其中最小的 k 个数。例如，输入4、5、1、6、2、7、3、8这8个数字，则最小的4个数字是1、2、3、4。

 

示例 1：

输入：arr = [3,2,1], k = 2
输出：[1,2] 或者 [2,1]


示例 2：

输入：arr = [0,1,2,1], k = 1
输出：[0]

 

限制：

0 <= k <= arr.length <= 10000
0 <= arr[i] <= 10000
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  边界条件问题，当为空的时候 返回的是new int[0];
-  对于queue来讲，就是两种入队出队的方式 add offer  remove poll

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int[] getLeastNumbers(int[] arr, int k) {
        //思路就是使用大根堆 维护长度为k
        //后期循环比较 如果当前值比根顶元素要小，当前元素进入，最顶端的数据出来就可以
        if(arr==null||k==0) return new int[0];//返回个数为0的值
        PriorityQueue<Integer> queue=new PriorityQueue<>((e1,e2)->e2-e1);//大根堆
        for(int num:arr){
            if(queue.size()<k) queue.add(num);//这样先维护一个长度为k的大根堆
            else{
                if(num<queue.peek()){
                    queue.add(num);
                    queue.remove();//或者使用remove 
                }
            }
        }
        int[] res=new int[k];
        for(int i=0;i<k;i++){
            res[i]=queue.remove();
        }
        return res;
        



        




    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$





## 题目地址(125. 验证回文串)

https://leetcode-cn.com/problems/valid-palindrome/

## 题目描述

```
给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写。

说明：本题中，我们将空字符串定义为有效的回文串。

示例 1:

输入: "A man, a plan, a canal: Panama"
输出: true


示例 2:

输入: "race a car"
输出: false

```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [java的4种解题方式 - 验证回文串 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/valid-palindrome/solution/javade-4chong-jie-ti-fang-shi-by-sdwwld/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public boolean isPalindrome(String s) {
        //难点就是需要判断不是字母以及数字字符的数据
        //以及要忽略字母的大小写
        //库函数要熟练的进行使用 Character.isLetterOrDigit()是否为数字或者是字母 Character.toLowerCase() 转换成小写
        if(s==null||s.length()==0) return true;
        int left=0,right=s.length()-1;
        while(left<right){
            while(left<right&&!Character.isLetterOrDigit(s.charAt(left))) left++;//跳到是数字或者字母的东西
            while(left<right&&!Character.isLetterOrDigit(s.charAt(right))) right--;
            if(Character.toLowerCase(s.charAt(left))!=Character.toLowerCase(s.charAt(right))) return false;
            left++;
            right--;

        }
        return true;
        

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(384. 打乱数组)

https://leetcode-cn.com/problems/shuffle-an-array/

## 题目描述

```
给你一个整数数组 nums ，设计算法来打乱一个没有重复元素的数组。

实现 Solution class:

Solution(int[] nums) 使用整数数组 nums 初始化对象
int[] reset() 重设数组到它的初始状态并返回
int[] shuffle() 返回数组随机打乱后的结果

 

示例：

输入
["Solution", "shuffle", "reset", "shuffle"]
[[[1, 2, 3]], [], [], []]
输出
[null, [3, 1, 2], [1, 2, 3], [1, 3, 2]]

解释
Solution solution = new Solution([1, 2, 3]);
solution.shuffle();    // 打乱数组 [1,2,3] 并返回结果。任何 [1,2,3]的排列返回的概率应该相同。例如，返回 [3, 1, 2]
solution.reset();      // 重设数组到它的初始状态 [1, 2, 3] 。返回 [1, 2, 3]
solution.shuffle();    // 随机返回数组 [1, 2, 3] 打乱后的结果。例如，返回 [1, 3, 2]


 

提示：

1 <= nums.length <= 200
-106 <= nums[i] <= 106
nums 中的所有元素都是 唯一的
最多可以调用 5 * 104 次 reset 和 shuffle
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [扑克牌随机排序，会打牌就会做题 - 打乱数组 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/shuffle-an-array/solution/bu-ke-pai-sui-ji-pai-xu-hui-da-pai-jiu-h-8yzh/)
-  int num = random.nextInt(max)%(max-min+1)+min;此时num取值在min到max之间

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    //思路就是定义两个数组，reset数组与shuffle数组
    //reset数组直接就是nums赋值给他
    //shuffle数组就是一个副本 在副本上进行变换然后直接返回副本就可以
    int[] reset;
    int[] shuffle;

    public Solution(int[] nums) {
        //初始化
        reset=nums;//把nums的结果送给你
        shuffle=nums.clone();//下一步的shuffle在初始化基础上操作

    }
    
    /** Resets the array to its original configuration and return it. */
    public int[] reset() {
        shuffle=reset.clone();//下一步shuffle在reset基础上操作
        return reset;

    }
    
    /** Returns a random shuffling of the array. */
    public int[] shuffle() {
        //目的就是从0到shuffle长度下标 生成随机数也是0到shuffle之间 然后两者进行交换就可以
        //random.nextInt(max)%random.nextInt(max-min+1)+min 生成min到max之间的随机数
        //这里min=0 max=shuffle.length-1;
        Random random=new Random();
        for(int i=0;i<shuffle.length;i++){
            int inx=random.nextInt(shuffle.length)%(shuffle.length-0+1)+0;
            int tem=shuffle[i];
            shuffle[i]=shuffle[inx];
            shuffle[inx]=tem;
            
        }
        return shuffle;

    }
}

/**
 * Your Solution object will be instantiated and called as such:
 * Solution obj = new Solution(nums);
 * int[] param_1 = obj.reset();
 * int[] param_2 = obj.shuffle();
 */

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(662. 二叉树最大宽度)

https://leetcode-cn.com/problems/maximum-width-of-binary-tree/

## 题目描述

```
给定一个二叉树，编写一个函数来获取这个树的最大宽度。树的宽度是所有层中的最大宽度。这个二叉树与满二叉树（full binary tree）结构相同，但一些节点为空。

每一层的宽度被定义为两个端点（该层最左和最右的非空节点，两端点间的null节点也计入长度）之间的长度。

示例 1:

输入: 

           1
         /   \
        3     2
       / \     \  
      5   3     9 

输出: 4
解释: 最大值出现在树的第 3 层，宽度为 4 (5,3,null,9)。


示例 2:

输入: 

          1
         /  
        3    
       / \       
      5   3     

输出: 2
解释: 最大值出现在树的第 3 层，宽度为 2 (5,3)。


示例 3:

输入: 

          1
         / \
        3   2 
       /        
      5      

输出: 2
解释: 最大值出现在树的第 2 层，宽度为 2 (3,2)。


示例 4:

输入: 

          1
         / \
        3   2
       /     \  
      5       9 
     /         \
    6           7
输出: 8
解释: 最大值出现在树的第 4 层，宽度为 8 (6,null,null,null,null,null,null,7)。


注意: 答案在32位有符号整数的表示范围内。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  看思路就可以

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
    public int widthOfBinaryTree(TreeNode root) {
        //思路比较简单 层次遍历 然后有一个数组存储每一行的下标 遍历结束之后就进行求职最大就可以
        if(root==null) return 0;
        Queue<TreeNode> queue=new LinkedList<>();//存储节点，poll与offer
        List<Integer> res=new ArrayList<>();//存储每一层的节点的坐标 注意每一层都要更新与清空
        queue.add(root);
        int maxed=1;//初始化为1比较合适
        res.add(1);//根节点从1开始 在投入左孩子时候 下标可以计算 就是 2倍 右孩子就是2倍加1
        while(!queue.isEmpty()){
            
            //res=new new ArrayList<>();
            int n=queue.size();
            for(int i=0;i<n;i++){
                TreeNode node=queue.poll();
                int inx=res.get(0);//得到下标
                res.remove(0);//剔除当前值
                if(node.left!=null){
                    queue.offer(node.left);
                    res.add(2*inx);
                }
                if(node.right!=null){
                    queue.offer(node.right);
                    res.add(2*inx+1);
                }
            }
            
            if(res.size()>1){//一行长度小于2没有必要更新
            maxed=Math.max(res.get(res.size()-1)-res.get(0)+1,maxed);
            }
            
            

        }
        return maxed;




    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(27. 二叉树的镜像)

https://leetcode-cn.com/problems/er-cha-shu-de-jing-xiang-lcof/

## 题目描述

```
请完成一个函数，输入一个二叉树，该函数输出它的镜像。

例如输入：

     4
   /   \
  2     7
 / \   / \
1   3 6   9
镜像输出：

     4
   /   \
  7     2
 / \   / \
9   6 3   1

 

示例 1：

输入：root = [4,2,7,1,3,6,9]
输出：[4,7,2,9,6,3,1]


 

限制：

0 <= 节点个数 <= 1000

注意：本题与主站 226 题相同：https://leetcode-cn.com/problems/invert-binary-tree/
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  就是递归方法或者是进展出栈的问题好不好

## 代码

- 语言支持：Java

迭代:

```java

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
class Solution {
    public TreeNode mirrorTree(TreeNode root) {
        //感觉使用递归来做 参数是两个节点 
        //使用栈进行具体的模拟,每一个层级都可以得到当前节点以及对当前的节点进行交换
        if(root==null) return root;
        Queue<TreeNode> queue=new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            TreeNode node=queue.poll();
            //把子树归入队列当中
            if(node.left!=null) queue.add(node.left);
            if(node.right!=null) queue.add(node.right);
            //进行左右子树的调换
            TreeNode tem=node.left;
            node.left=node.right;
            node.right=tem;

        }
        return root;


    }

}

```



递归法：

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
class Solution {
    public TreeNode mirrorTree(TreeNode root) {
        //感觉使用递归来做 参数是两个节点 
        //递归的话就是有意义吗 mirrorTree 返回的是已经修改好的树的节点
        if(root==null) return null;
        TreeNode new_left=mirrorTree(root.right);//新的结果
        TreeNode new_right=mirrorTree(root.left);
        root.left=new_left;
        root.right=new_right;
        return root;//返回的是当前值

    }

}
```



**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## ==题目地址(79. 单词搜索)==

https://leetcode-cn.com/problems/word-search/

## 题目描述

```
给定一个 m x n 二维字符网格 board 和一个字符串单词 word 。如果 word 存在于网格中，返回 true ；否则，返回 false 。

单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母不允许被重复使用。

 

示例 1：

输入：board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
输出：true


示例 2：

输入：board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "SEE"
输出：true


示例 3：

输入：board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCB"
输出：false


 

提示：

m == board.length
n = board[i].length
1 <= m, n <= 6
1 <= word.length <= 15
board 和 word 仅由大小写英文字母组成

 

进阶：你可以使用搜索剪枝的技术来优化解决方案，使其在 board 更大的情况下可以更快解决问题？
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
    private boolean[][] visited;//当前位置是否被访问过
    private int[][] dics=new int[][]{{-1,0},{0,1},{1,0},{0,-1}};
    public boolean exist(char[][] board, String word) {
        //大体的思路比较简单，就是dfs向四个方向找 不满足情况就回来 同时需要把当前的visit变成true 
        if(board==null||board.length==0||word==null||word.length()==0) return false;
        int m=board.length;
        int n=board[0].length;
        visited=new boolean[m][n];
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(board[i][j]==word.charAt(0)){
                    if(dfs(i,j,board,0,word)) return true;//因为第一个字母已经匹配 要从第二个(1)开始看看能否匹配
                }
            }
        }
        return false;
    }

    public boolean dfs(int i,int j,char[][] board,int begin,String word){
        
        //if(board[i][j]==word.charAt(begin)&&begin==word.length()-1) return true;//说明直接到头了
        if(begin==word.length()-1) return true;
        visited[i][j]=true;//说明当前遍历过
        for(int[] dic:dics){
        int new_x=i+dic[0];
        int new_y=j+dic[1];
        if(isArea(new_x,new_y,board)&&board[new_x][new_y]==word.charAt(begin+1)&&!visited[new_x][new_y]){
            if(dfs(new_x,new_y,board,begin+1,word)) return true;
        }
    }
        visited[i][j]=false;//回溯
        return false;//因为进不去上边的循环所以肯定不匹配
    }
    public boolean isArea(int x,int y,char[][] board){
        int m=board.length;
        int n=board[0].length;
        return x>=0&&x<m&&y>=0&&y<n;

        
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(350. 两个数组的交集 II)

https://leetcode-cn.com/problems/intersection-of-two-arrays-ii/

## 题目描述

```
给定两个数组，编写一个函数来计算它们的交集。

 

示例 1：

输入：nums1 = [1,2,2,1], nums2 = [2,2]
输出：[2,2]


示例 2:

输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4]
输出：[4,9]

 

说明：

输出结果中每个元素出现的次数，应与元素在两个数组中出现次数的最小值一致。
我们可以不考虑输出结果的顺序。

进阶：

如果给定的数组已经排好序呢？你将如何优化你的算法？
如果 nums1 的大小比 nums2 小很多，哪种方法更优？
如果 nums2 的元素存储在磁盘上，内存是有限的，并且你不能一次加载所有的元素到内存中，你该怎么办？
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
    public int[] intersect(int[] nums1, int[] nums2) {
        //这个难点就是因为可能出现 多次出现的情形好不好
        //比如前边是1 1 后边是1 只能是一个1  
        //前边是1 1 后边是 1 1 1  结果是1  1
        //因此使用hashmap 存储个数 只有contains并且 出现次数大于0才可以算数
        HashMap<Integer,Integer> res=new HashMap<>();
        List<Integer> ans=new ArrayList<>();
        for(Integer num:nums1){
            res.put(num,res.getOrDefault(num,0)+1);
        }
        for(int num:nums2){
            if(res.containsKey(num)&&res.get(num)>0){
                ans.add(num);
                res.put(num,res.get(num)-1);
            }
        }
        int[] res1=new int[ans.size()];
        int inx=0;
        for(int num:ans){
            res1[inx++]=num;
        }
        return res1;
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(230. 二叉搜索树中第K小的元素)

https://leetcode-cn.com/problems/kth-smallest-element-in-a-bst/

## 题目描述

```
给定一个二叉搜索树的根节点 root ，和一个整数 k ，请你设计一个算法查找其中第 k 个最小元素（从 1 开始计数）。

 

示例 1：

输入：root = [3,1,4,null,2], k = 1
输出：1


示例 2：

输入：root = [5,3,6,2,4,null,null,1], k = 3
输出：3


 

 

提示：

树中的节点数为 n 。
1 <= k <= n <= 104
0 <= Node.val <= 104

 

进阶：如果二叉搜索树经常被修改（插入/删除操作）并且你需要频繁地查找第 k 小的值，你将如何优化算法？
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
    private int count=0;
    private int ans=-1;
    public int kthSmallest(TreeNode root, int k) {
        //中序，但是中间加一个变量来记录进行了多少次的过程
        //需要是全局的
        dfs(root,k);
        return ans;

    }
    public void dfs(TreeNode root,int k){
        if(root==null) return;
        dfs(root.left,k);
        count++;
        if(count==k) ans=root.val;
        dfs(root.right,k);

    }
    
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(9. 回文数)

https://leetcode-cn.com/problems/palindrome-number/

## 题目描述

```
给你一个整数 x ，如果 x 是一个回文整数，返回 true ；否则，返回 false 。

回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。例如，121 是回文，而 123 不是。

 

示例 1：

输入：x = 121
输出：true


示例 2：

输入：x = -121
输出：false
解释：从左向右读, 为 -121 。 从右向左读, 为 121- 。因此它不是一个回文数。


示例 3：

输入：x = 10
输出：false
解释：从右向左读, 为 01 。因此它不是一个回文数。


示例 4：

输入：x = -101
输出：false


 

提示：

-231 <= x <= 231 - 1

 

进阶：你能不将整数转为字符串来解决这个问题吗？
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
    public boolean isPalindrome(int x) {
        //思路就是 右边出来的也进行计算 就完事儿了 
        //奇数个的话 或者偶数个 
                //如果是0 那么也算
        if(x==0) return true;
        //先进行特例判断 负数 或者是最后一位是0的直接不可能
        if(x<0||x%10==0) return false;

        int right=0;//意义是这个右边数的倒置
        while(right<x){
            right=right*10+x%10;
            x/=10;    
        }
        return x==right||right/10==x;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(297. 二叉树的序列化与反序列化)

https://leetcode-cn.com/problems/serialize-and-deserialize-binary-tree/

## 题目描述

```
序列化是将一个数据结构或者对象转换为连续的比特位的操作，进而可以将转换后的数据存储在一个文件或者内存中，同时也可以通过网络传输到另一个计算机环境，采取相反方式重构得到原数据。

请设计一个算法来实现二叉树的序列化与反序列化。这里不限定你的序列 / 反序列化算法执行逻辑，你只需要保证一个二叉树可以被序列化为一个字符串并且将这个字符串反序列化为原始的树结构。

提示: 输入输出格式与 LeetCode 目前使用的方式一致，详情请参阅 LeetCode 序列化二叉树的格式。你并非必须采取这种方式，你也可以采用其他的方法解决这个问题。

 

示例 1：

输入：root = [1,2,3,null,null,4,5]
输出：[1,2,3,null,null,4,5]


示例 2：

输入：root = []
输出：[]


示例 3：

输入：root = [1]
输出：[1]


示例 4：

输入：root = [1,2]
输出：[1,2]


 

提示：

树中结点数在范围 [0, 104] 内
-1000 <= Node.val <= 1000
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

public class Codec {

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            if (root == null) return "X,";
            String leftSerialize = serialize(root.left); //左子树的序列化字符串
            String rightSerialize = serialize(root.right); //右子树的序列化字符串
            return root.val + "," + leftSerialize + rightSerialize;
            //这个确实是会生成带有,的结果好不好
            //比如说1 2 3 这个树
            //序列化会生成"1,2,X,X,3,X,X"
        }

        // Decodes your encoded data to tree.
        /**
         * 构建树的函数 buildTree 接收的 “状态” 是 list 数组，由序列化字符串转成
         * 按照前序遍历的顺序：先构建根节点，再构建左子树、右子树
         * 将 list 数组的首项弹出，它是当前子树的 root 节点
         * 如果它为 'X' ，返回 null
         * 如果它不为 'X'，则为它创建节点，并递归调用 buildTree 构建左右子树，当前子树构建完毕，向上返回
         */
        public TreeNode deserialize(String data) {
            String[] temp = data.split(",");
            Deque<String> dp = new LinkedList<>(Arrays.asList(temp));//一个双端队列 很稳这种直接由list进行初始化
            return buildTree(dp);
        }
        private TreeNode buildTree(Deque<String> dq){//就是不断的往回走 遇到X就null 遇到其他的就生成就可以
            String s = dq.poll(); //返回当前结点
            if (s.equals("X")) return null;
            TreeNode node = new TreeNode(Integer.parseInt(s));
            node.left = buildTree(dq); //构建左子树 遇到X直接返回空也是合理的 
            node.right = buildTree(dq); //构建右子树
            return node;
        }
    }

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(62. 圆圈中最后剩下的数字)

https://leetcode-cn.com/problems/yuan-quan-zhong-zui-hou-sheng-xia-de-shu-zi-lcof/

## 题目描述

```
0,1,···,n-1这n个数字排成一个圆圈，从数字0开始，每次从这个圆圈里删除第m个数字（删除后从下一个数字开始计数）。求出这个圆圈里剩下的最后一个数字。

例如，0、1、2、3、4这5个数字组成一个圆圈，从数字0开始每次删除第3个数字，则删除的前4个数字依次是2、0、4、1，因此最后剩下的数字是3。

 

示例 1：

输入: n = 5, m = 3
输出: 3


示例 2：

输入: n = 10, m = 17
输出: 2


 

限制：

1 <= n <= 10^5
1 <= m <= 10^6
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

![Screenshot from 2021-05-28 21-08-29.png](https://pic.leetcode-cn.com/1622207345-bKYeRl-Screenshot%20from%202021-05-28%2021-08-29.png)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int lastRemaining(int n, int m) {
        //就是一个逆推的过程
        //最终的结果为下标为0
        //递推公式为 f(n)=(f(n-1)+m)%当前长度;
        //相当于知道了f(1)=0
        int ans=0;
        for(int i=2;i<=n;i++){
            ans=(ans+m)%i;//i表示现在当前长度
                    }
        return ans;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(26. 删除有序数组中的重复项)

https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array/

## 题目描述

```
给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使每个元素 只出现一次 ，返回删除后数组的新长度。

不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。

 

说明:

为什么返回数值是整数，但输出的答案是数组呢?

请注意，输入数组是以「引用」方式传递的，这意味着在函数里修改输入数组对于调用者是可见的。

你可以想象内部操作如下:

// nums 是以“引用”方式传递的。也就是说，不对实参做任何拷贝
int len = removeDuplicates(nums);

// 在函数里修改输入数组对于调用者是可见的。
// 根据你的函数返回的长度, 它会打印出数组中 该长度范围内 的所有元素。
for (int i = 0; i < len; i++) {
    print(nums[i]);
}

 

示例 1：

输入：nums = [1,1,2]
输出：2, nums = [1,2]
解释：函数应该返回新的长度 2 ，并且原数组 nums 的前两个元素被修改为 1, 2 。不需要考虑数组中超出新长度后面的元素。


示例 2：

输入：nums = [0,0,1,1,1,2,2,3,3,4]
输出：5, nums = [0,1,2,3,4]
解释：函数应该返回新的长度 5 ， 并且原数组 nums 的前五个元素被修改为 0, 1, 2, 3, 4 。不需要考虑数组中超出新长度后面的元素。


 

提示：

0 <= nums.length <= 3 * 104
-104 <= nums[i] <= 104
nums 已按升序排列

 
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
    public int removeDuplicates(int[] nums) {
        //思路就是双指针
        //前边后边啷个
        //如果前边后边一致的话 那么fast++
        //知道不相同那么nums[++slow]=nums[fast]
        int slow=0,fast=1;
        while(fast<nums.length){
            while(fast<nums.length&&nums[fast] == nums[slow]){
                    fast++;
                    }
            //这样得到的此时 nums[fast]!=nums[slow]

            //if(fast==nums.length-1) return slow+1;
            if(fast<nums.length) nums[++slow] = nums[fast];//防止上边fast超出长度限制 需要加一个判断
            fast++;
            
        }
        return slow+1;
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(287. 寻找重复数)

https://leetcode-cn.com/problems/find-the-duplicate-number/

## 题目描述

```
给定一个包含 n + 1 个整数的数组 nums ，其数字都在 1 到 n 之间（包括 1 和 n），可知至少存在一个重复的整数。

假设 nums 只有 一个重复的整数 ，找出 这个重复的数 。

你设计的解决方案必须不修改数组 nums 且只用常量级 O(1) 的额外空间。

 

示例 1：

输入：nums = [1,3,4,2,2]
输出：2


示例 2：

输入：nums = [3,1,3,4,2]
输出：3


示例 3：

输入：nums = [1,1]
输出：1


示例 4：

输入：nums = [1,1,2]
输出：1


 

提示：

1 <= n <= 105
nums.length == n + 1
1 <= nums[i] <= n
nums 中 只有一个整数 出现 两次或多次 ，其余整数均只出现 一次

 

进阶：

如何证明 nums 中至少存在一个重复的数字?
你可以设计一个线性级时间复杂度 O(n) 的解决方案吗？
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
    public int findDuplicate(int[] nums) {
        //这个难点就是在于怎么把数组当成一个链表的映射
        //只要有相同的数值  那么两个数字之间的映射就是环
        //可以使用找环入口的方式进行求解
        //快慢指针 快的走两步 慢的走一步 fast.next.next====>fast=nums[nums[fast]];
        //slow.next====> slow=nums[slow]
        int slow=0,fast=0;
        while(true){
            fast=nums[nums[fast]];//相当于走两步
            slow=nums[slow];//走单步
            if(slow==fast) break;
        }
        fast=0;
        while(fast!=slow){
            slow=nums[slow];
            fast=nums[fast];
        }
        return fast;


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$





![image-20210618155647620](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210618155647620.png)

## 题目地址(114. 二叉树展开为链表)

https://leetcode-cn.com/problems/flatten-binary-tree-to-linked-list/

## 题目描述

```
给你二叉树的根结点 root ，请你将它展开为一个单链表：

展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。
展开后的单链表应该与二叉树 先序遍历 顺序相同。

 

示例 1：

输入：root = [1,2,5,3,4,null,6]
输出：[1,null,2,null,3,null,4,null,5,null,6]


示例 2：

输入：root = []
输出：[]


示例 3：

输入：root = [0]
输出：[0]


 

提示：

树中结点数在范围 [0, 2000] 内
-100 <= Node.val <= 100

 

进阶：你可以使用原地算法（O(1) 额外空间）展开这棵树吗？
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
    public void flatten(TreeNode root) {
        //递归直接，思路太清楚了
        //flatten意思就是完成一个树转成链表的操作
        //先转换左子树 再转换右子树
        //然后把左子树放到当前root的右子树，当前root右子树转成null 但是要保存右子树
        //然后移动到root现在最右边子节点，把转成链表的结果赋值给他有节点就可以啦
        
        if(root==null) return;
        flatten(root.left);//先转换左子树 现在root.left开始的就是一个链表形式了
        flatten(root.right);//再转换右子树 现在root.right开始的就是一个链表形式了
        //修改当前root
        TreeNode tem=root.right;//保存下右子树
        root.right=root.left;
        root.left=null;
        while(root.right!=null) root=root.right;
        root.right=tem;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(22. 链表中倒数第k个节点)

https://leetcode-cn.com/problems/lian-biao-zhong-dao-shu-di-kge-jie-dian-lcof/

## 题目描述

```
输入一个链表，输出该链表中倒数第k个节点。为了符合大多数人的习惯，本题从1开始计数，即链表的尾节点是倒数第1个节点。

例如，一个链表有 6 个节点，从头节点开始，它们的值依次是 1、2、3、4、5、6。这个链表的倒数第 3 个节点是值为 4 的节点。

 

示例：

给定一个链表: 1->2->3->4->5, 和 k = 2.

返回链表 4->5.
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
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode getKthFromEnd(ListNode head, int k) {
        //不需要进行求解k和长度的关系
        //首先把fast指针弄到和slow指针差k个位置就可以
        ListNode fast=head,slow=head;
        for(int i=0;i<k;i++){
            fast=fast.next;
                    }
       while(fast!=null){
           fast=fast.next;
           slow=slow.next;
       }
       return slow;



    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(61. 旋转链表)

https://leetcode-cn.com/problems/rotate-list/

## 题目描述

```
给你一个链表的头节点 head ，旋转链表，将链表每个节点向右移动 k 个位置。

 

示例 1：

输入：head = [1,2,3,4,5], k = 2
输出：[4,5,1,2,3]


示例 2：

输入：head = [0,1,2], k = 4
输出：[2,0,1]


 

提示：

链表中节点的数目在范围 [0, 500] 内
-100 <= Node.val <= 100
0 <= k <= 2 * 109
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
    public ListNode rotateRight(ListNode head, int k) {
        //思路就是找到倒数第k+1个节点slow
        //slow.next后边段开头 fast为结尾的结
        //slow.next是新的开头，然后原来的结果接到原来的头上就可以

        //特殊
        if(head==null) return head; 

        ListNode pos=head;
        int len=0;
        while(pos!=null){
            len++;
            pos=pos.next;
        }
        k=k%len;//可能大于，同时可能是0
        if(k==0) return head;
        //需要找到第k+1个
        ListNode fast=head,slow=head;
        for(int i=0;i<k;i++){
            fast=fast.next;
        }
        //我们这里让slow和fast隔着k个 那么当fast为最后一个时候 slow就是倒数k+1个
        while(fast!=null&&fast.next!=null){
            fast=fast.next;
            slow=slow.next;
        }
        //现在slow是倒数第k+1个 fast是最后一个
        ListNode res=slow.next;//保存下后边的端开头
        fast.next=head;//后边段接到开头
        slow.next=null;//前边段成为最后了
        return res;


    }
 }


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(11. 盛最多水的容器)

https://leetcode-cn.com/problems/container-with-most-water/

## 题目描述

```
给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0) 。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。

说明：你不能倾斜容器。

 

示例 1：

输入：[1,8,6,2,5,4,8,3,7]
输出：49 
解释：图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。

示例 2：

输入：height = [1,1]
输出：1


示例 3：

输入：height = [4,3,2,1,4]
输出：16


示例 4：

输入：height = [1,2,1]
输出：2


 

提示：

n = height.length
2 <= n <= 3 * 104
0 <= height[i] <= 3 * 104
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [O(n) 双指针解法：理解正确性、图解原理（C++/Java） - 盛最多水的容器 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/container-with-most-water/solution/on-shuang-zhi-zhen-jie-fa-li-jie-zheng-que-xing-tu/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int maxArea(int[] height) {
        int left=0,right=height.length-1;
        //思路挺有意思的
        int maxed=0;
        while(left<right){
            maxed=Math.max(maxed,Math.min(height[left],height[right])*(right-left));
            if(height[left]<height[right]) left++;//当左边小的时候 只能通过移动左边来观察有没有更大的
            else right--;//同理右边小的时候 移动右边
        }
        return maxed;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(560. 和为K的子数组)

https://leetcode-cn.com/problems/subarray-sum-equals-k/

## 题目描述

```
给定一个整数数组和一个整数 k，你需要找到该数组中和为 k 的连续的子数组的个数。

示例 1 :

输入:nums = [1,1,1], k = 2
输出: 2 , [1,1] 与 [1,1] 为两种不同的情况。


说明 :

数组的长度为 [1, 20,000]。
数组中元素的范围是 [-1000, 1000] ，且整数 k 的范围是 [-1e7, 1e7]。
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
    public int subarraySum(int[] nums, int k) {
        //使用map 前缀和 出现次数
        //后边前缀和减去前边的前缀和如果为k
        //count++ 
        //先后顺序
        HashMap<Integer,Integer> res=new HashMap<>();//存储前缀和出现的次数
        int presum=0;//代表前缀和
        int count=0;
        //因为前缀和可能从第一个数开始 需要有一个初始值 presum=0要提前放到hashmap当中
        res.put(presum,1);
        for(int i=0;i<nums.length;i++){
            presum+=nums[i];
            if(res.containsKey(presum-k)){
                //for(int j=0;j<res.get(presum-k);j++) count++;
                //可以直接加
                count+=res.get(presum-k);
                //如果之前有过presum-k的记录 说明两者之间是有和为k的连续子序列
            }
            res.put(presum,res.getOrDefault(presum,0)+1);

        }
        return count;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(443. 压缩字符串)

https://leetcode-cn.com/problems/string-compression/

## 题目描述

```
给定一组字符，使用原地算法将其压缩。

压缩后的长度必须始终小于或等于原数组长度。

数组的每个元素应该是长度为1 的字符（不是 int 整数类型）。

在完成原地修改输入数组后，返回数组的新长度。

 

进阶：
你能否仅使用O(1) 空间解决问题？

 

示例 1：

输入：
["a","a","b","b","c","c","c"]

输出：
返回 6 ，输入数组的前 6 个字符应该是：["a","2","b","2","c","3"]

说明：
"aa" 被 "a2" 替代。"bb" 被 "b2" 替代。"ccc" 被 "c3" 替代。


示例 2：

输入：
["a"]

输出：
返回 1 ，输入数组的前 1 个字符应该是：["a"]

解释：
没有任何字符串被替代。


示例 3：

输入：
["a","b","b","b","b","b","b","b","b","b","b","b","b"]

输出：
返回 4 ，输入数组的前4个字符应该是：["a","b","1","2"]。

解释：
由于字符 "a" 不重复，所以不会被压缩。"bbbbbbbbbbbb" 被 “b12” 替代。
注意每个数字在数组中都有它自己的位置。


 

提示：

所有字符都有一个ASCII值在[35, 126]区间内。
1 <= len(chars) <= 1000。
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
    public int compress(char[] chars) {
        //思路就是左右指针，滑动窗口
        //fast和slow
        //fast来进行遍历，slow来指示当前重复的字符
        //利用fast与slow的差来计算具体的字符长度，如果大于1 要求解 如果小于1 不用求解
        if(chars.length<=1) return chars.length;
        int slow=0;
        int size=0;
        for(int fast=0;fast<=chars.length;fast++){
            //当fast为最后时候
            if(fast==chars.length){
                chars[size++]=chars[slow];//size 代表当前要填写的位置 slow是当前重复的字母  fast是为了求解长度
                if(fast-slow>1){//如果不大于1 就是单个数字
                    for(char c:String.valueOf(fast-slow).toCharArray()){
                        chars[size++]=c;//写上数字
                    }
            }
            }
            //当是普通时候
            else if(chars[fast]!=chars[slow]){
                chars[size++]=chars[slow];//size 代表当前要填写的位置 slow是当前重复的字母  fast是为了求解长度
                if(fast-slow>1){//如果不大于1 就是单个数字
                    for(char c:String.valueOf(fast-slow).toCharArray()){
                        chars[size++]=c;//写上数字
                    }
                }
                slow=fast;//进入下一个循环
            }
            
        }
        return size;
    }
    }
        
    

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(06. 字符串压缩)

https://leetcode-cn.com/problems/compress-string-lcci/

## 题目描述

```
字符串压缩。利用字符重复出现的次数，编写一种方法，实现基本的字符串压缩功能。比如，字符串aabcccccaaa会变为a2b1c5a3。若“压缩”后的字符串没有变短，则返回原先的字符串。你可以假设字符串中只包含大小写英文字母（a至z）。

示例1:

 输入："aabcccccaaa"
 输出："a2b1c5a3"


示例2:

 输入："abbccd"
 输出："abbccd"
 解释："abbccd"压缩后为"a1b2c2d1"，比原字符串长度更长。


提示：

字符串长度在[0, 50000]范围内。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [图解双指针法取连续字符（C++/Java/Python） - 字符串压缩 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/compress-string-lcci/solution/shuang-zhi-zhen-fa-qu-lian-xu-zi-fu-cpython-by-net/)

## 代码

- 语言支持：Java

Java Code:

```java
class Solution{
public String compressString(String S) {
    int N = S.length();
    int slow = 0;
    StringBuilder sb = new StringBuilder();
    while (slow < N) {
        int fast = slow;//这里是相当于一个新的循环开启，fast进行递增
        while (fast < N && S.charAt(fast) == S.charAt(slow)) {
            fast++;//fast不相同或者到了最后的点进而会跳出循环
        }
        sb.append(S.charAt(slow));//重复的字母
        sb.append(fast - slow);//长度
        slow = fast;
    }

    String res = sb.toString();
    if (res.length() < S.length()) {
        return res;
    } else {
        return S;
    }
}
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(242. 有效的字母异位词)

https://leetcode-cn.com/problems/valid-anagram/

## 题目描述

```
给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。

示例 1:

输入: s = "anagram", t = "nagaram"
输出: true


示例 2:

输入: s = "rat", t = "car"
输出: false

说明:
你可以假设字符串只包含小写字母。

进阶:
如果输入字符串包含 unicode 字符怎么办？你能否调整你的解法来应对这种情况？
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
    public boolean isAnagram(String s, String t) {
        //使用字母统计数组
        if(s.length()!=t.length()) return false;
        int[] res=new int[26];
        for(int i=0;i<s.length();i++){
            res[s.charAt(i)-'a']++;
            res[t.charAt(i)-'a']--;

            
        }
        for(int i=0;i<26;i++){
            if(res[i]!=0) return false;
        }
        return true;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(191. 位1的个数)

https://leetcode-cn.com/problems/number-of-1-bits/

## 题目描述

```
编写一个函数，输入是一个无符号整数（以二进制串的形式），返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。

 

提示：

请注意，在某些语言（如 Java）中，没有无符号整数类型。在这种情况下，输入和输出都将被指定为有符号整数类型，并且不应影响您的实现，因为无论整数是有符号的还是无符号的，其内部的二进制表示形式都是相同的。
在 Java 中，编译器使用二进制补码记法来表示有符号整数。因此，在上面的 示例 3 中，输入表示有符号整数 -3。

 

示例 1：

输入：00000000000000000000000000001011
输出：3
解释：输入的二进制串 00000000000000000000000000001011 中，共有三位为 '1'。


示例 2：

输入：00000000000000000000000010000000
输出：1
解释：输入的二进制串 00000000000000000000000010000000 中，共有一位为 '1'。


示例 3：

输入：11111111111111111111111111111101
输出：31
解释：输入的二进制串 11111111111111111111111111111101 中，共有 31 位为 '1'。

 

提示：

输入必须是长度为 32 的 二进制串 。

 

进阶：

如果多次调用这个函数，你将如何优化你的算法？
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

public class Solution {
    // you need to treat n as an unsigned value
    public int hammingWeight(int n) {
        //无符号右边移动
        int count=0;
        while(n!=0){
            count+=n&1;
            n=n>>>1;//一定是无符号移动，否则负数的话就是一直补充1，永远不为0

        }
        return count;
        
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(86. 分隔链表)

https://leetcode-cn.com/problems/partition-list/

## 题目描述

```
给你一个链表的头节点 head 和一个特定值 x ，请你对链表进行分隔，使得所有 小于 x 的节点都出现在 大于或等于 x 的节点之前。

你应当 保留 两个分区中每个节点的初始相对位置。

 

示例 1：

输入：head = [1,4,3,2,5,2], x = 3
输出：[1,2,2,4,3,5]


示例 2：

输入：head = [2,1], x = 2
输出：[1,2]


 

提示：

链表中节点的数目在范围 [0, 200] 内
-100 <= Node.val <= 100
-200 <= x <= 200
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
    public ListNode partition(ListNode head, int x) {
        //起飞了好不好，两个链表然后就拼接

        ListNode dummy1=new ListNode(-1);
        ListNode dummy2=new ListNode(-1);
        ListNode p1=dummy1; //注意需要有一个代替跑路遍历的指针
        ListNode p2=dummy2;//代替跑路的指针
        while(head!=null){
            if(head.val<x){
                p1.next=head;
                p1=p1.next;
            }
            else{
                p2.next=head;
                p2=p2.next;
            }
            head=head.next;
                
        }
        //该拆开就拆开 
        p1.next=dummy2.next;
        p2.next=null;
        return dummy1.next;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(47. 全排列 II)

https://leetcode-cn.com/problems/permutations-ii/

## 题目描述

```
给定一个可包含重复数字的序列 nums ，按任意顺序 返回所有不重复的全排列。

 

示例 1：

输入：nums = [1,1,2]
输出：
[[1,1,2],
 [1,2,1],
 [2,1,1]]


示例 2：

输入：nums = [1,2,3]
输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]


 

提示：

1 <= nums.length <= 8
-10 <= nums[i] <= 10
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [回溯搜索 + 剪枝（Java、Python） - 全排列 II - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/permutations-ii/solution/hui-su-suan-fa-python-dai-ma-java-dai-ma-by-liwe-2/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public List<List<Integer>> permuteUnique(int[] nums) {
        //这个题标准的回溯方法
        //但是可能会有重复的数字
        //因此需要进行剪枝，如何才能进行剪枝呢？就是当当前值等于上一个值 并且上一个值被释放的时候 下边就会产生重复
        List<List<Integer>> res=new ArrayList<>();
        List<Integer> path=new ArrayList<>();
        Arrays.sort(nums);//排序才能相邻的在一起
        boolean[] used=new boolean[nums.length];//是否用过

        dfs(nums,res,path,used);
        return res;


    }
    public void dfs(int[] nums,List<List<Integer>> res,List<Integer> path,boolean[] used){
        if(path.size()==nums.length){
            res.add(new ArrayList<>(path));
        }
        for(int i=0;i<nums.length;i++){
            if(used[i]==true) continue;//当已经用过之后就直接跳过
            if(i>0&&nums[i]==nums[i-1]&&used[i-1]==false) continue; //因此需要进行剪枝，如何才能进行剪枝呢？就是当当前值等于上一个值 并且上一个一样的值被释放的时候 下边就会产生重复
            path.add(nums[i]);//进入
            used[i]=true;//用过
            dfs(nums,res,path,used);
            used[i]=false;//撤销
            path.remove(path.size()-1);//撤销
        }
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(74. 搜索二维矩阵)

https://leetcode-cn.com/problems/search-a-2d-matrix/

## 题目描述

```
编写一个高效的算法来判断 m x n 矩阵中，是否存在一个目标值。该矩阵具有如下特性：

每行中的整数从左到右按升序排列。
每行的第一个整数大于前一行的最后一个整数。

 

示例 1：

输入：matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
输出：true


示例 2：

输入：matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13
输出：false


 

提示：

m == matrix.length
n == matrix[i].length
1 <= m, n <= 100
-104 <= matrix[i][j], target <= 104
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
    public boolean searchMatrix(int[][] matrix, int target) {
        //直接移动就可以啦，选择右下角就可以啦
        if(matrix==null||matrix.length==0) return false;
        int m=matrix.length,n=matrix[0].length;
        int i=0,j=n-1;
        //基本的是用while啊
        while(j>=0&&i<m){
          if(matrix[i][j]>target) j--;
          else if(matrix[i][j]==target) return true;
          else i++;
        }

        return false;
        
        


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(52. 两个链表的第一个公共节点)

https://leetcode-cn.com/problems/liang-ge-lian-biao-de-di-yi-ge-gong-gong-jie-dian-lcof/

## 题目描述

```
输入两个链表，找出它们的第一个公共节点。

如下面的两个链表：

在节点 c1 开始相交。

 

示例 1：

输入：intersectVal = 8, listA = [4,1,8,4,5], listB = [5,0,1,8,4,5], skipA = 2, skipB = 3
输出：Reference of the node with value = 8
输入解释：相交节点的值为 8 （注意，如果两个列表相交则不能为 0）。从各自的表头开始算起，链表 A 为 [4,1,8,4,5]，链表 B 为 [5,0,1,8,4,5]。在 A 中，相交节点前有 2 个节点；在 B 中，相交节点前有 3 个节点。


 

示例 2：

输入：intersectVal = 2, listA = [0,9,1,2,4], listB = [3,2,4], skipA = 3, skipB = 1
输出：Reference of the node with value = 2
输入解释：相交节点的值为 2 （注意，如果两个列表相交则不能为 0）。从各自的表头开始算起，链表 A 为 [0,9,1,2,4]，链表 B 为 [3,2,4]。在 A 中，相交节点前有 3 个节点；在 B 中，相交节点前有 1 个节点。


 

示例 3：

输入：intersectVal = 0, listA = [2,6,4], listB = [1,5], skipA = 3, skipB = 2
输出：null
输入解释：从各自的表头开始算起，链表 A 为 [2,6,4]，链表 B 为 [1,5]。由于这两个链表不相交，所以 intersectVal 必须为 0，而 skipA 和 skipB 可以是任意值。
解释：这两个链表不相交，因此返回 null。


 

注意：

如果两个链表没有交点，返回 null.
在返回结果后，两个链表仍须保持原有的结构。
可假定整个链表结构中没有循环。
程序尽量满足 O(n) 时间复杂度，且仅用 O(1) 内存。
本题与主站 160 题相同：https://leetcode-cn.com/problems/intersection-of-two-linked-lists/
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
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        //之前的就直接做就可以

        if(headA==null||headB==null) return null;
        ListNode p1=headA;
        ListNode p2=headB;
        while(p1!=p2){
            //一定要注意好吧，是一个非此即彼的关系 真的是差劲怎么老是忘
            if(p1==null) p1=headB;
            else p1=p1.next;
            if(p2==null) p2=headA;
            else p2=p2.next;

        }
        return p1;
        
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(448. 找到所有数组中消失的数字)

https://leetcode-cn.com/problems/find-all-numbers-disappeared-in-an-array/

## 题目描述

```
给你一个含 n 个整数的数组 nums ，其中 nums[i] 在区间 [1, n] 内。请你找出所有在 [1, n] 范围内但没有出现在 nums 中的数字，并以数组的形式返回结果。

 

示例 1：

输入：nums = [4,3,2,7,8,2,3,1]
输出：[5,6]


示例 2：

输入：nums = [1,1]
输出：[2]


 

提示：

n == nums.length
1 <= n <= 105
1 <= nums[i] <= n

进阶：你能在不使用额外空间且时间复杂度为 O(n) 的情况下解决这个问题吗? 你可以假定返回的数组不算在额外空间内。
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
    public List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> res=new ArrayList<>();
        if(nums.length==0) return res;
        for(int i=0;i<nums.length;i++){
            while(nums[i]!=nums[nums[i]-1]){//只是交换过来了，不一定一次成功
                swap(nums,i,nums[i]-1);
            }
        }
        for(int i=0;i<nums.length;i++){
            if(nums[i]!=i+1) res.add(i+1);
        }
        return res;

    }
    
    public void swap(int[] nums,int i,int j){
        int tem=nums[i];
        nums[i]=nums[j];
        nums[j]=tem;
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(442. 数组中重复的数据)

https://leetcode-cn.com/problems/find-all-duplicates-in-an-array/

## 题目描述

```
给定一个整数数组 a，其中1 ≤ a[i] ≤ n （n为数组长度）, 其中有些元素出现两次而其他元素出现一次。

找到所有出现两次的元素。

你可以不用到任何额外空间并在O(n)时间复杂度内解决这个问题吗？

示例：

输入:
[4,3,2,7,8,2,3,1]

输出:
[2,3]

```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [“抽屉原理” + 基于“异或运算”交换两个变量的值 - 数组中重复的数据 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/find-all-duplicates-in-an-array/solution/chou-ti-yuan-li-ji-yu-yi-huo-yun-suan-jiao-huan-li/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public List<Integer> findDuplicates(int[] nums) {

     List<Integer> res=new ArrayList<>();
        if(nums.length==0) return res;
        for(int i=0;i<nums.length;i++){
            while(nums[i]!=nums[nums[i]-1]){//只是交换过来了，不一定一次成功
                swap(nums,i,nums[i]-1);
            }
        }
        //这里和消失的数字的差别就是返回的是不配对位置上的数值而不是不配对的位置
        for(int i=0;i<nums.length;i++){
            if(nums[i]!=i+1) res.add(nums[i]);
        }
        return res;

    }
    
    public void swap(int[] nums,int i,int j){
        int tem=nums[i];
        nums[i]=nums[j];
        nums[j]=tem;
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(11. 旋转数组的最小数字)

https://leetcode-cn.com/problems/xuan-zhuan-shu-zu-de-zui-xiao-shu-zi-lcof/

## 题目描述

```
把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。输入一个递增排序的数组的一个旋转，输出旋转数组的最小元素。例如，数组 [3,4,5,1,2] 为 [1,2,3,4,5] 的一个旋转，该数组的最小值为1。  

示例 1：

输入：[3,4,5,1,2]
输出：1


示例 2：

输入：[2,2,2,0,1]
输出：0


注意：本题与主站 154 题相同：https://leetcode-cn.com/problems/find-minimum-in-rotated-sorted-array-ii/
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
    public int minArray(int[] numbers) {
        //直接二分，和最右边的数据进行比较
        int left=0;
        int right=numbers.length-1;
        while(left<right){
            int mid=left+(right-left)/2;
            if(numbers[mid]>numbers[right]) left=mid+1;
            else if(numbers[mid]<numbers[right]) right=mid;
            else right--;//相等的时候无法判断 往后弄
        }
        return numbers[right];

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$







![image-20210621201846019](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210621201846019.png)

![image-20210621201903071](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210621201903071.png)

## 题目地址(152. 乘积最大子数组)

https://leetcode-cn.com/problems/maximum-product-subarray/

## 题目描述

```
给你一个整数数组 nums ，请你找出数组中乘积最大的连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积。

 

示例 1:

输入: [2,3,-2,4]
输出: 6
解释: 子数组 [2,3] 有最大乘积 6。


示例 2:

输入: [-2,0,-1]
输出: 0
解释: 结果不能为 2, 因为 [-2,-1] 不是子数组。
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
    public int maxProduct(int[] nums) {
        //思路相对比较easy，就是直接进行分析就可以啦
        //思路https://leetcode-cn.com/problems/maximum-product-subarray/solution/xiang-xi-tong-su-de-si-lu-fen-xi-duo-jie-fa-by--36/ 思路一 动态规划
        //dpmax[i]代表以nums[i]结尾的最大值 dpmin[i]代表以nums[i]结尾的最小值
        int maxed=nums[0];//因为从第二个开始比较
        int[] dpmax=new int[nums.length];
        int[] dpmin=new int[nums.length];
        dpmax[0]=nums[0];
        dpmin[0]=nums[0];
        for(int i=1;i<nums.length;i++){
            dpmin[i]=Math.min(nums[i]*dpmax[i-1],Math.min(nums[i]*dpmin[i-1],nums[i]));
            dpmax[i]=Math.max(nums[i]*dpmax[i-1],Math.max(nums[i]*dpmin[i-1],nums[i]));
            maxed=Math.max(maxed,dpmax[i]);
        }
        return maxed;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(213. 打家劫舍 II)

https://leetcode-cn.com/problems/house-robber-ii/

## 题目描述

```
你是一个专业的小偷，计划偷窃沿街的房屋，每间房内都藏有一定的现金。这个地方所有的房屋都 围成一圈 ，这意味着第一个房屋和最后一个房屋是紧挨着的。同时，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警 。

给定一个代表每个房屋存放金额的非负整数数组，计算你 在不触动警报装置的情况下 ，今晚能够偷窃到的最高金额。

 

示例 1：

输入：nums = [2,3,2]
输出：3
解释：你不能先偷窃 1 号房屋（金额 = 2），然后偷窃 3 号房屋（金额 = 2）, 因为他们是相邻的。


示例 2：

输入：nums = [1,2,3,1]
输出：4
解释：你可以先偷窃 1 号房屋（金额 = 1），然后偷窃 3 号房屋（金额 = 3）。
     偷窃到的最高金额 = 1 + 3 = 4 。

示例 3：

输入：nums = [0]
输出：0


 

提示：

1 <= nums.length <= 100
0 <= nums[i] <= 1000
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
    public int rob(int[] nums) {
        //难点是在于环形的结构，像是两个的和的形式
        //那么可不可以分成两个部分，比较并且取最大值就可以
        //分成两个 第一个不包含第一个数据  第二个不包含第二个数据
        if(nums.length==0) return 0;
        if(nums.length==1) return nums[0];
        int[] res1=new int[nums.length-1];//第一个不包含第一个数据
        int[] res2=new int[nums.length-1];//第二个不包含最后一个数据
        for(int i=1;i<nums.length;i++) res1[i-1]=nums[i];
        for(int i=0;i<nums.length-1;i++) res2[i]=nums[i];
        return Math.max(helper(res1),helper(res2));

    }
    public int helper(int[] nums){
        //这个就是偷窃问题
        int m=nums.length;
        //其实都是特殊情况
        
        if(m==0) return 0;
        if(m==1) return nums[0];
        int[] dp=new int[m+1];
        //dp[i] 代表前i个数据最大值
        dp[0]=0;
        dp[1]=nums[0];
        for(int i=2;i<=m;i++ ){
            dp[i]=Math.max(dp[i-1],dp[i-2]+nums[i-1]);
        }
        return dp[m];
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(337. 打家劫舍 III)

https://leetcode-cn.com/problems/house-robber-iii/

## 题目描述

```
在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。这个地区只有一个入口，我们称之为“根”。 除了“根”之外，每栋房子有且只有一个“父“房子与之相连。一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。 如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。

计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。

示例 1:

输入: [3,2,3,null,3,null,1]

     3
    / \
   2   3
    \   \ 
     3   1

输出: 7 
解释: 小偷一晚能够盗取的最高金额 = 3 + 3 + 1 = 7.

示例 2:

输入: [3,4,5,1,3,null,1]

     3
    / \
   4   5
  / \   \ 
 1   3   1

输出: 9
解释: 小偷一晚能够盗取的最高金额 = 4 + 5 = 9.

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
    public int rob(TreeNode root) {
        //直接递归就完事儿,两种偷法 从头节点开始偷 偷孙子  第二种 从孩子节点开始偷 曾孙子
        //rob的意义在于返回的是当前节点开始偷的最优方案

        if(root==null) return 0;
        int first=0;//第一种结果
        int sec=root.val;//第二种结果
        
        //第一种从孩子开始偷
        first+=rob(root.left);
        first+=rob(root.right);
        //第二种 当前节点以及孙子节点
        if(root.left!=null){
            sec+=rob(root.left.left);
            sec+=rob(root.left.right);
        }
        if(root.right!=null){
            sec+=rob(root.right.left);
            sec+=rob(root.right.right);
        }
        return Math.max(sec,first);
   
        

 


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(876. 链表的中间结点)

https://leetcode-cn.com/problems/middle-of-the-linked-list/

## 题目描述

```
给定一个头结点为 head 的非空单链表，返回链表的中间结点。

如果有两个中间结点，则返回第二个中间结点。

 

示例 1：

输入：[1,2,3,4,5]
输出：此列表中的结点 3 (序列化形式：[3,4,5])
返回的结点值为 3 。 (测评系统对该结点序列化表述是 [3,4,5])。
注意，我们返回了一个 ListNode 类型的对象 ans，这样：
ans.val = 3, ans.next.val = 4, ans.next.next.val = 5, 以及 ans.next.next.next = NULL.


示例 2：

输入：[1,2,3,4,5,6]
输出：此列表中的结点 4 (序列化形式：[4,5,6])
由于该列表有两个中间结点，值分别为 3 和 4，我们返回第二个结点。


 

提示：

给定链表的结点数介于 1 和 100 之间。
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
    public ListNode middleNode(ListNode head) {
        if(head==null||head.next==null) return head;//一个或者为空的时候
        ListNode dummy=new ListNode(-1);
        dummy.next=head;
        ListNode fast=dummy,slow=dummy;
        while(fast!=null&&fast.next!=null){
            fast=fast.next.next;
            slow=slow.next;
        } 
        //还是要有一个dummy节点  分析一下 看最后fast的状态 如果为空 说明是奇数个 此时slow就是 否则就是偶数个 结果是slow的下一个
        if(fast==null) return slow;
        else return slow.next;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(120. 三角形最小路径和)

https://leetcode-cn.com/problems/triangle/

## 题目描述

```
给定一个三角形 triangle ，找出自顶向下的最小路径和。

每一步只能移动到下一行中相邻的结点上。相邻的结点 在这里指的是 下标 与 上一层结点下标 相同或者等于 上一层结点下标 + 1 的两个结点。也就是说，如果正位于当前行的下标 i ，那么下一步可以移动到下一行的下标 i 或 i + 1 。

 

示例 1：

输入：triangle = [[2],[3,4],[6,5,7],[4,1,8,3]]
输出：11
解释：如下面简图所示：
   2
  3 4
 6 5 7
4 1 8 3
自顶向下的最小路径和为 11（即，2 + 3 + 5 + 1 = 11）。


示例 2：

输入：triangle = [[-10]]
输出：-10


 

提示：

1 <= triangle.length <= 200
triangle[0].length == 1
triangle[i].length == triangle[i - 1].length + 1
-104 <= triangle[i][j] <= 104

 

进阶：

你可以只使用 O(n) 的额外空间（n 为三角形的总行数）来解决这个问题吗？
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

//普通递归会超时

```java

class Solution {
    public int minimumTotal(List<List<Integer>> triangle) {
        //会超时
        //使用递归的方法，就是定义一个dfs(triangle,i,j) 表示从i,j到最后一层的值
        return dfs(triangle,0,0);

    }
    public int dfs(List<List<Integer>> triangle,int i,int j){
        if(i==triangle.size()) return 0;//说明到了最后一层 需要直接有一个返回值
        return Math.min(dfs(triangle,i+1,j),dfs(triangle,i+1,j+1))+triangle.get(i).get(j);
    }
}

```

/

```java
class Solution {
    public int minimumTotal(List<List<Integer>> triangle) {
        //会超时
        //使用递归的方法，就是定义一个dfs(triangle,i,j) 表示从i,j到最后一层的值
        Integer[][] memo=new Integer[triangle.size()][triangle.size()];//存储中间的值 
        return dfs(triangle,0,0,memo);


    }
    public int dfs(List<List<Integer>> triangle,int i,int j,Integer[][] memo){
        if(i==triangle.size()) return 0;//说明到了最后一层 需要直接有一个返回值,因为实际上这样每一次求一个中间值 都会往下递归 不合理
        if(memo[i][j]!=null) return memo[i][j];
        memo[i][j]=Math.min(dfs(triangle,i+1,j,memo),dfs(triangle,i+1,j+1,memo))+triangle.get(i).get(j);
        return memo[i][j];
    }
}
```



**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(509. 斐波那契数)

https://leetcode-cn.com/problems/fibonacci-number/

## 题目描述

```
斐波那契数，通常用 F(n) 表示，形成的序列称为 斐波那契数列 。该数列由 0 和 1 开始，后面的每一项数字都是前面两项数字的和。也就是：

F(0) = 0，F(1) = 1
F(n) = F(n - 1) + F(n - 2)，其中 n > 1


给你 n ，请计算 F(n) 。

 

示例 1：

输入：2
输出：1
解释：F(2) = F(1) + F(0) = 1 + 0 = 1


示例 2：

输入：3
输出：2
解释：F(3) = F(2) + F(1) = 1 + 1 = 2


示例 3：

输入：4
输出：3
解释：F(4) = F(3) + F(2) = 2 + 1 = 3


 

提示：

0 <= n <= 30
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
    public int fib(int n) {
        if(n<2) return n;
        int pre=0;
        int cur=1;
        //本质上还是进行学习，进行空间换时间
        for(int i=2;i<=n;i++){
            int tem=cur;
            cur+=pre;
            pre=tem;
        }
        return cur;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(208. 实现 Trie (前缀树))

https://leetcode-cn.com/problems/implement-trie-prefix-tree/

## 题目描述

```
Trie（发音类似 "try"）或者说 前缀树 是一种树形数据结构，用于高效地存储和检索字符串数据集中的键。这一数据结构有相当多的应用情景，例如自动补完和拼写检查。

请你实现 Trie 类：

Trie() 初始化前缀树对象。
void insert(String word) 向前缀树中插入字符串 word 。
boolean search(String word) 如果字符串 word 在前缀树中，返回 true（即，在检索之前已经插入）；否则，返回 false 。
boolean startsWith(String prefix) 如果之前已经插入的字符串 word 的前缀之一为 prefix ，返回 true ；否则，返回 false 。

 

示例：

输入
["Trie", "insert", "search", "search", "startsWith", "insert", "search"]
[[], ["apple"], ["apple"], ["app"], ["app"], ["app"], ["app"]]
输出
[null, null, true, false, true, null, true]

解释
Trie trie = new Trie();
trie.insert("apple");
trie.search("apple");   // 返回 True
trie.search("app");     // 返回 False
trie.startsWith("app"); // 返回 True
trie.insert("app");
trie.search("app");     // 返回 True


 

提示：

1 <= word.length, prefix.length <= 2000
word 和 prefix 仅由小写英文字母组成
insert、search 和 startsWith 调用次数 总计 不超过 3 * 104 次
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

class Trie {
     private Trie[] child;
     private boolean  isEnd; 
     

    /** Initialize your data structure here. */
    public Trie() {
        this.child=new Trie[26];
        this.isEnd=false;

    }
    
    /** Inserts a word into the trie. */
    public void insert(String word) {
        Trie cur=this;
        char[] res=word.toCharArray();
        for(int i=0;i<res.length;i++){
            int inx=res[i]-'a';
            if(cur.child[inx]==null) cur.child[inx]=new Trie();
            cur=cur.child[inx];
        }
        cur.isEnd=true;





    }
    
    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        Trie result=fuhe(word);
        if(result==null) return false;
        if(result.isEnd==false) return false;
        return true;


    }
    
    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        Trie result=fuhe(prefix);
        return result!=null;

    }

    public Trie fuhe(String word){
        //返回相合的最后一个Tire
        Trie cur=this;
        char[] res=word.toCharArray();
        for(int i=0;i<res.length;i++){
            int inx=res[i]-'a';
            if(cur.child[inx]==null) return null;
            cur=cur.child[inx];
        }
        return cur;//最后相合的位置
    }
}

/**
 * Your Trie object will be instantiated and called as such:
 * Trie obj = new Trie();
 * obj.insert(word);
 * boolean param_2 = obj.search(word);
 * boolean param_3 = obj.startsWith(prefix);
 */

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(75. 颜色分类)

https://leetcode-cn.com/problems/sort-colors/

## 题目描述

```
给定一个包含红色、白色和蓝色，一共 n 个元素的数组，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。

此题中，我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。

 

示例 1：

输入：nums = [2,0,2,1,1,0]
输出：[0,0,1,1,2,2]


示例 2：

输入：nums = [2,0,1]
输出：[0,1,2]


示例 3：

输入：nums = [0]
输出：[0]


示例 4：

输入：nums = [1]
输出：[1]


 

提示：

n == nums.length
1 <= n <= 300
nums[i] 为 0、1 或 2

 

进阶：

你可以不使用代码库中的排序函数来解决这道题吗？
你能想出一个仅使用常数空间的一趟扫描算法吗？
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
    public void sortColors(int[] nums) {
        //思路就是一共三个颜色 ，把2和0移动到两边就可以 中间1不变
        //双指针 p0 p2表示0和2当前应该放的位置
        int p0=0,p2=nums.length-1;
        //p0=0 p2=nums.length-1;
        //接着遍历cur 就可以
        int cur=0;

        while(cur<=p2){
            if(nums[cur]==0){
                swap(nums,cur,p0);//交换,可以想一下只要为0就放在前边 此时cur也要往前 这样一定可以保证全部0都能在前边
                p0++;
                cur++;
            }

            else if(nums[cur]==2){
                swap(nums,cur,p2);//交换,但是这个是吧当前的值放在后边，如果和2交换的是后边的0 cur再跳一步的话 ，就会错过这个换过来的0 所以cur不能+
                p2--;
            }  

            else cur++;//如果等于1要继续往前走啊啊啊什么也不用做，不然死循环
                     
        }



    }
    public void swap(int[] nums,int i,int j){
        int tem=nums[i];
        nums[i]=nums[j];
        nums[j]=tem;
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(45. 把数组排成最小的数)

https://leetcode-cn.com/problems/ba-shu-zu-pai-cheng-zui-xiao-de-shu-lcof/

## 题目描述

```
输入一个非负整数数组，把数组里所有数字拼接起来排成一个数，打印能拼接出的所有数字中最小的一个。

 

示例 1:

输入: [10,2]
输出: "102"

示例 2:

输入: [3,30,34,5,9]
输出: "3033459"

 

提示:

0 < nums.length <= 100

说明:

输出结果可能非常大，所以你需要返回一个字符串而不是整数
拼接起来的数字可能会有前导 0，最后结果不需要去掉前导 0
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
    public String minNumber(int[] nums) {
        //转成字符串的拼接 两个数字字符串a b
        //如果a+b>b+a 字符串序有这个关系 那么a就应该在b前边
        if(nums.length==0) return String.valueOf(nums[0]);
        //先转换成字符数组
        String[] s=new String[nums.length];
        StringBuilder res=new StringBuilder();
        for(int i=0;i<s.length;i++){
            s[i]=String.valueOf(nums[i]);
        }
        Arrays.sort(s,(a,b)->((a+b).compareTo(b+a)));//自定义这个排序方式
        for(int i=0;i<s.length;i++){
            res.append(s[i]);//放入当中
        }
        return res.toString();




    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(100. 相同的树)

https://leetcode-cn.com/problems/same-tree/

## 题目描述

```
给你两棵二叉树的根节点 p 和 q ，编写一个函数来检验这两棵树是否相同。

如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。

 

示例 1：

输入：p = [1,2,3], q = [1,2,3]
输出：true


示例 2：

输入：p = [1,2], q = [1,null,2]
输出：false


示例 3：

输入：p = [1,2,1], q = [1,1,2]
输出：false


 

提示：

两棵树上的节点数目都在范围 [0, 100] 内
-104 <= Node.val <= 104
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
    public boolean isSameTree(TreeNode p, TreeNode q) {
        //这个意义是当前值为跟节点的数是否相同
        //肯定要分情况讨论 并且需要进行递归
        //怎么样证明两棵树相同 
        if(p==null&&q==null) return true;//空为相同
        else if(p==null||q==null) return false;//一个为空一个不为空不相同
        //剩下就是两者都不为空
        else if(p.val!=q.val) return false;
        //注意四个是互斥的哦 所以用else if
        else return isSameTree(p.left,q.left)&&isSameTree(p.right,q.right);//才能去验证


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(459. 重复的子字符串)

https://leetcode-cn.com/problems/repeated-substring-pattern/

## 题目描述

```
给定一个非空的字符串，判断它是否可以由它的一个子串重复多次构成。给定的字符串只含有小写英文字母，并且长度不超过10000。

示例 1:

输入: "abab"

输出: True

解释: 可由子字符串 "ab" 重复两次构成。


示例 2:

输入: "aba"

输出: False


示例 3:

输入: "abcabcabcabc"

输出: True

解释: 可由子字符串 "abc" 重复四次构成。 (或者子字符串 "abcabc" 重复两次构成。)

```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

![image-20210623113200593](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210623113200593.png)

![image-20210623113220056](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210623113220056.png)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public boolean repeatedSubstringPattern(String s) {
        String double_string=s+s;
        //只有重复字符串粘贴之后才会至少有两个本身的一样的字节
        //不重复的的只有两个 第一个就是开头 第二个就是拼接的开头
        //把开头一个字符和 末尾字符都去掉 那么不重复的就不会含有原来的字符 重复的还有
        return double_string.substring(1,double_string.length()-1).contains(s);


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$



## 题目地址(24. 反转链表)

https://leetcode-cn.com/problems/fan-zhuan-lian-biao-lcof/

## 题目描述

```
定义一个函数，输入一个链表的头节点，反转该链表并输出反转后链表的头节点。

 

示例:

输入: 1->2->3->4->5->NULL
输出: 5->4->3->2->1->NULL

 

限制：

0 <= 节点个数 <= 5000

 

注意：本题与主站 206 题相同：https://leetcode-cn.com/problems/reverse-linked-list/
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
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode reverseList(ListNode head) {
        if(head==null||head.next==null) return head;
        ListNode pre=null;
        ListNode cur=head;
        ListNode next;
        while(cur!=null){
            next=cur.next;
            cur.next=pre;
            pre=cur;
            cur=next;
        }
        return pre;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(268. 丢失的数字)

https://leetcode-cn.com/problems/missing-number/

## 题目描述

```
给定一个包含 [0, n] 中 n 个数的数组 nums ，找出 [0, n] 这个范围内没有出现在数组中的那个数。

 

进阶：

你能否实现线性时间复杂度、仅使用额外常数空间的算法解决此问题?

 

示例 1：

输入：nums = [3,0,1]
输出：2
解释：n = 3，因为有 3 个数字，所以所有的数字都在范围 [0,3] 内。2 是丢失的数字，因为它没有出现在 nums 中。

示例 2：

输入：nums = [0,1]
输出：2
解释：n = 2，因为有 2 个数字，所以所有的数字都在范围 [0,2] 内。2 是丢失的数字，因为它没有出现在 nums 中。

示例 3：

输入：nums = [9,6,4,2,3,5,7,0,1]
输出：8
解释：n = 9，因为有 9 个数字，所以所有的数字都在范围 [0,9] 内。8 是丢失的数字，因为它没有出现在 nums 中。

示例 4：

输入：nums = [0]
输出：1
解释：n = 1，因为有 1 个数字，所以所有的数字都在范围 [0,1] 内。1 是丢失的数字，因为它没有出现在 nums 中。

 

提示：

n == nums.length
1 <= n <= 104
0 <= nums[i] <= n
nums 中的所有数字都 独一无二
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
    public int missingNumber(int[] nums) {
        //使用那个求和方法就可以
        //sum=1+2+3*****n  减去所有nums值 剩下的就是缺的
        int sum=0;
        for(int i=0;i<nums.length;i++){
            sum+=((i+1)-nums[i]);
        }
        return sum;

        

    }

}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(344. 反转字符串)

https://leetcode-cn.com/problems/reverse-string/

## 题目描述

```
编写一个函数，其作用是将输入的字符串反转过来。输入字符串以字符数组 char[] 的形式给出。

不要给另外的数组分配额外的空间，你必须原地修改输入数组、使用 O(1) 的额外空间解决这一问题。

你可以假设数组中的所有字符都是 ASCII 码表中的可打印字符。

 

示例 1：

输入：["h","e","l","l","o"]
输出：["o","l","l","e","h"]


示例 2：

输入：["H","a","n","n","a","h"]
输出：["h","a","n","n","a","H"]
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
    public void reverseString(char[] s) {
        //双指针不就完事儿了吗
        if(s==null||s.length==1) return;
        int left=0,right=s.length-1;
        while(left<right){
            swap(s,left++,right--);
        }



    }
    public void swap(char[] s,int i,int j){
        char tem=s[i];
        s[i]=s[j];
        s[j]=tem;
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(556. 下一个更大元素 III)

https://leetcode-cn.com/problems/next-greater-element-iii/

## 题目描述

```
给你一个正整数 n ，请你找出符合条件的最小整数，其由重新排列 n 中存在的每位数字组成，并且其值大于 n 。如果不存在这样的正整数，则返回 -1 。

注意 ，返回的整数应当是一个 32 位整数 ，如果存在满足题意的答案，但不是 32 位整数 ，同样返回 -1 。

 

示例 1：

输入：n = 12
输出：21


示例 2：

输入：n = 21
输出：-1


 

提示：

1 <= n <= 231 - 1
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [通俗易懂的线性解法！🐱 - 下一个更大元素 III - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/next-greater-element-iii/solution/tong-su-yi-dong-de-xian-xing-jie-fa-by-c-2v2x/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    //思路其实没有什么思路
    //采用的是别人的思路问题
    
    
    public int nextGreaterElement(int n) {

        String num = String.valueOf(n);//转成字符数组
        char[] chs = num.toCharArray();
        int len = chs.length;    
        
        int index = -1;
        // 从后向前查找,找到第一个下降的元素所在位置 用i-1=j来表示
        for (int i = len - 1; i >= 0 && index == -1; i--) {
            int j = i - 1;
            // 找到一对符合条件的num[i], num[i - 1]
            if (j >= 0 && chs[i] > chs[j]) {
                // 从[i, len - 1]区间中找到刚好大于num[i - 1]的数
                // 因为[i, len - 1]中元素是单调递减的, 所以从后往前遍历

                //从后往前找第一个比nums[j]大的元素k 两者交换
                for (int k = len - 1; k >= i; k--) {
                    if (chs[k] > chs[j]) {
                        char temp = chs[j];
                        chs[j] = chs[k];
                        chs[k] = temp;
                        index = i;
                        break;//找到这个就进行停下来  记录下此时的i 就是从i开始 到len-1这些数据 需要重新排序
                    }
                }
            }
        }

        if (index == -1)
            return -1;

        // 将index 之后的元素从小到大排序
        Arrays.sort(chs, index, len);//[index,len)排序
        
        // 构建结果
        //这里因为可能超 所以用long来加 看最后是否大于max 如果没有就直接转成int就可以
        long res = 0;
        for (int i = 0 ; i < len; i++) 
            res = res * 10 + (chs[i] - '0');

        return res > Integer.MAX_VALUE ? -1 : (int)(res);
				
    }
}


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(496. 下一个更大元素 I)

https://leetcode-cn.com/problems/next-greater-element-i/

## 题目描述

```
给你两个 没有重复元素 的数组 nums1 和 nums2 ，其中nums1 是 nums2 的子集。

请你找出 nums1 中每个元素在 nums2 中的下一个比其大的值。

nums1 中数字 x 的下一个更大元素是指 x 在 nums2 中对应位置的右边的第一个比 x 大的元素。如果不存在，对应位置输出 -1 。

 

示例 1:

输入: nums1 = [4,1,2], nums2 = [1,3,4,2].
输出: [-1,3,-1]
解释:
    对于 num1 中的数字 4 ，你无法在第二个数组中找到下一个更大的数字，因此输出 -1 。
    对于 num1 中的数字 1 ，第二个数组中数字1右边的下一个较大数字是 3 。
    对于 num1 中的数字 2 ，第二个数组中没有下一个更大的数字，因此输出 -1 。

示例 2:

输入: nums1 = [2,4], nums2 = [1,2,3,4].
输出: [3,-1]
解释:
    对于 num1 中的数字 2 ，第二个数组中的下一个较大数字是 3 。
    对于 num1 中的数字 4 ，第二个数组中没有下一个更大的数字，因此输出 -1 。


 

提示：

1 <= nums1.length <= nums2.length <= 1000
0 <= nums1[i], nums2[i] <= 104
nums1和nums2中所有整数 互不相同
nums1 中的所有整数同样出现在 nums2 中

 

进阶：你可以设计一个时间复杂度为 O(nums1.length + nums2.length) 的解决方案吗？
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
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        //由于nums1是nums2的子集
        //所以可以求解nums当中每一个数字的下一个最大值
        //当小于栈顶元素或者为空 加入 
        //大于栈顶元素 不断弹出 当前元素入栈
        //最后就可以 留下来的就是没有的
        //利用hashmap把这个关系给弄掉就可以
        Stack<Integer> stack=new Stack<>();
        HashMap<Integer,Integer> res=new HashMap<>();
        for(int i=0;i<nums2.length;i++){
            while(!stack.isEmpty()&&nums2[i]>stack.peek()){
                res.put(stack.pop(),nums2[i]);

            }
            stack.push(nums2[i]);
        }
        while(!stack.isEmpty()){
            res.put(stack.pop(),-1);

        }
        int[] ans=new int[nums1.length];
        for(int i=0;i<ans.length;i++){
            ans[i]=res.get(nums1[i]);

        }
        return ans;



    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(105. 从前序与中序遍历序列构造二叉树)

https://leetcode-cn.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/

## 题目描述

```
根据一棵树的前序遍历与中序遍历构造二叉树。

注意:
你可以假设树中没有重复的元素。

例如，给出

前序遍历 preorder = [3,9,20,15,7]
中序遍历 inorder = [9,3,15,20,7]

返回如下的二叉树：

    3
   / \
  9  20
    /  \
   15   7
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
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        //思路就是分治
        //利用前序的第一个元素，找到中序当中的位置 然后利用位置分成左右子树
        //需要用hashmap存储中序的下标 方便求解
        //注意新建的时候 需要返回的是一个节点  所以生成节点（值可以求） 然后节点左右子树就是递归的结果
        //注意当左边大于右边就进行返回就可以
        //难点就是下标的计算 我们的下标都是从0开始的真实下标
        HashMap<Integer,Integer> res=new HashMap<>();//存储当前节点的下标位置
        for(int i=0;i<inorder.length;i++){
            res.put(inorder[i],i);
        }

        return Builder(res,preorder,0,preorder.length-1,inorder,0,inorder.length-1);


    }
        /*
        res  存储中序序列值下标，方便划分左右子树
        preorder 前序序列
        preleft 左子树的左边界
        preright 左子树右边界
        inorder 中序序列
        inleft 右子树左边界
        inright 右子树的右边界

        
        */

    public TreeNode Builder(HashMap<Integer,Integer> res,int[] preorder,int preleft,int preright,int[] inorder,int inleft,int inright){
     if(preleft>preright) return null;//无法构成子树
     TreeNode node=new TreeNode(preorder[preleft]);//找到根节点并且构造
     int mid=res.get(preorder[preleft]);//找到对中序的位置
     node.left=Builder(res,preorder,preleft+1,preleft+mid-inleft,inorder,inleft,mid-1);
     node.right=Builder(res,preorder,preleft+mid-inleft+1,preright,inorder,mid+1,inright);
     return node;


     
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(106. 从中序与后序遍历序列构造二叉树)

https://leetcode-cn.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/

## 题目描述

```
根据一棵树的中序遍历与后序遍历构造二叉树。

注意:
你可以假设树中没有重复的元素。

例如，给出

中序遍历 inorder = [9,3,15,20,7]
后序遍历 postorder = [9,15,7,20,3]

返回如下的二叉树：

    3
   / \
  9  20
    /  \
   15   7

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
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        //思路就是分治
        //利用后续序的最后一个元素，找到中序当中的位置 然后利用位置分成左右子树
        //需要用hashmap存储中序的下标 方便求解
        //注意新建的时候 需要返回的是一个节点  所以生成节点（值可以求） 然后节点左右子树就是递归的结果
        //注意当左边大于右边就进行返回就可以
        //难点就是下标的计算 我们的下标都是从0开始的真实下标
        HashMap<Integer,Integer> res=new HashMap<>();//存储当前节点的下标位置
        for(int i=0;i<inorder.length;i++){
            res.put(inorder[i],i);
        }

        return Builder(res,postorder,0,postorder.length-1,inorder,0,inorder.length-1);


    }
        /*
        res  存储中序序列值下标，方便划分左右子树
        postorder 后序序列
        preleft 左子树的左边界
        preright 左子树右边界
        inorder 中序序列
        inleft 右子树左边界
        inright 右子树的右边界

        
        */

    public TreeNode Builder(HashMap<Integer,Integer> res,int[] postorder,int postleft,int postright,int[] inorder,int inleft,int inright){
     if(postleft>postright) return null;//无法构成子树
     TreeNode node=new TreeNode(postorder[postright]);//找到根节点并且构造,难点就是下标的意义，注意找下标别忘记应该是左右边界而不是一个固定的
     int mid=res.get(postorder[postright]);//找到对中序的位置
     node.left=Builder(res,postorder,postleft,postleft+mid-inleft-1,inorder,inleft,mid-1);
     node.right=Builder(res,postorder,postleft+mid-inleft,postright-1,inorder,mid+1,inright);
     return node;


     
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(48. 最长不含重复字符的子字符串)

https://leetcode-cn.com/problems/zui-chang-bu-han-zhong-fu-zi-fu-de-zi-zi-fu-chuan-lcof/

## 题目描述

```
请从字符串中找出一个最长的不包含重复字符的子字符串，计算该最长子字符串的长度。

 

示例 1:

输入: "abcabcbb"
输出: 3 
解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。


示例 2:

输入: "bbbbb"
输出: 1
解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。


示例 3:

输入: "pwwkew"
输出: 3
解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
     请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。


 

提示：

s.length <= 40000

注意：本题与主站 3 题相同：https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/
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
    public int lengthOfLongestSubstring(String s) {
        //快慢指针
        if(s.length()<2) return s.length();
        int fast=0;
        int slow=0;
        HashMap<Character,Integer> res=new HashMap<>();
        int maxed=0;
        for(fast=0;fast<s.length();fast++){
            if(res.containsKey(s.charAt(fast))){
                //一旦发现有重复 需要从下一个位置进行指定slow
                //res.put(s.charAt(fast),res.get(s.charAt(fast))+1);
                slow=Math.max(slow,res.get(s.charAt(fast))+1);//需要看看还是哪一个大，防止出问题
                //不应该直接slow=res.get(s.charAt(fast))+1 因为可能当前的值在很久之前出现过

            }

            maxed=Math.max(maxed,fast-slow+1);//每一次都算长度

            res.put(s.charAt(fast),fast);//记录下上次出现的位置


        }
        return maxed;
        

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(1095. 山脉数组中查找目标值)

https://leetcode-cn.com/problems/find-in-mountain-array/

## 题目描述

```
（这是一个 交互式问题 ）

给你一个 山脉数组 mountainArr，请你返回能够使得 mountainArr.get(index) 等于 target 最小 的下标 index 值。

如果不存在这样的下标 index，就请返回 -1。

 

何为山脉数组？如果数组 A 是一个山脉数组的话，那它满足如下条件：

首先，A.length >= 3

其次，在 0 < i < A.length - 1 条件下，存在 i 使得：

A[0] < A[1] < ... A[i-1] < A[i]
A[i] > A[i+1] > ... > A[A.length - 1]

 

你将 不能直接访问该山脉数组，必须通过 MountainArray 接口来获取数据：

MountainArray.get(k) - 会返回数组中索引为k 的元素（下标从 0 开始）
MountainArray.length() - 会返回该数组的长度

 

注意：

对 MountainArray.get 发起超过 100 次调用的提交将被视为错误答案。此外，任何试图规避判题系统的解决方案都将会导致比赛资格被取消。

为了帮助大家更好地理解交互式问题，我们准备了一个样例 “答案”：https://leetcode-cn.com/playground/RKhe3ave，请注意这 不是一个正确答案。

 

示例 1：

输入：array = [1,2,3,4,5,3,1], target = 3
输出：2
解释：3 在数组中出现了两次，下标分别为 2 和 5，我们返回最小的下标 2。

示例 2：

输入：array = [0,1,2,4,2,1], target = 3
输出：-1
解释：3 在数组中没有出现，返回 -1。


 

提示：

3 <= mountain_arr.length() <= 10000
0 <= target <= 10^9
0 <= mountain_arr.get(index) <= 10^9
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  其实就是两段
-  第一段就是找峰值 二分法num[i]与nums[i+1] 比较 判断在哪一段
-  第二段就是两段找下界 模板 然后看跳出的值是否相同再比较就可以啦

## 代码

- 语言支持：Java

Java Code:

```java

/**
 * // This is MountainArray's API interface.
 * // You should not implement it, or speculate about its implementation
 * interface MountainArray {
 *     public int get(int index) {}
 *     public int length() {}
 * }
 */
 
/**
 * // This is MountainArray's API interface.
 * // You should not implement it, or speculate about its implementation
 */

class Solution {
    public int findInMountainArray(int target, MountainArray mountainArr) {
      //主函数进行查找匹配问题 findpeak findleft findright
      int size=mountainArr.length();
      int n= findpeak(0,size-1,mountainArr);
      int left=findleft(target,mountainArr,0,n);
      if (left!=-1){
          return left;   
      }
      //如果左边有结果那么就进行返回
      //否则直接进行返回
      int right=findright(target,mountainArr,n+1,size-1);
      return right;
      
    }
    public int findpeak(int l,int r,MountainArray mountainArr){
        //int l=0,r=mountainArr.length()-1;
        while(l<r){
            int mid=l+(r-l)/2;
            if(mountainArr.get(mid)<mountainArr.get(mid+1)){
                l=mid+1;
            }
            else r=mid;
        }
        return r;
    }
    public int findleft(int target,MountainArray mountainArr,int left,int right){
        //一个找下界的函数
        while(left<right){
            int mid=left+(right-left)/2;
            if(mountainArr.get(mid)<target){
                left=mid+1;
            }
            else right=mid;
        }
        //就看退出来是否等于目标值，如果是一定是直接返回
        if(mountainArr.get(left)!=target){
            return -1;
        }
        return left;
        }
    public int findright(int target,MountainArray mountainArr,int left,int right){
        
        while(left<right){
            int mid=left+(right-left)/2;
            if(mountainArr.get(mid)>target){
                left=mid+1;
            }
            else right=mid;
        }
        if(mountainArr.get(left)!=target){
            return -1;
        }
        return left;
        }


    }

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(557. 反转字符串中的单词 III)

https://leetcode-cn.com/problems/reverse-words-in-a-string-iii/

## 题目描述

```
给定一个字符串，你需要反转字符串中每个单词的字符顺序，同时仍保留空格和单词的初始顺序。

 

示例：

输入："Let's take LeetCode contest"
输出："s'teL ekat edoCteeL tsetnoc"


 

提示：

在字符串中，每个单词由单个空格分隔，并且字符串中不会有任何额外的空格。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  就是翻转

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public String reverseWords(String s) {
        //这个直接就和原来的思路一样 双指针找到前边和后边
        //放到一个临时的地方StringBuilder进行翻转加入就可以
        s.trim();//去除两边的空格
        int left=0,right=0;
        StringBuilder res=new StringBuilder();
        //从前往后
        while(right<=s.length()-1){
            StringBuilder tem=new StringBuilder();
            while(right<=s.length()-1&&s.charAt(right)!=' ') right++;//找到中间空格
            tem.append(s.substring(left,right));//加一个半分
            res.append(tem.reverse().toString()+" ");//加一个中间
            while(right<=s.length()-1&&s.charAt(right)==' ') right++;
            left=right;
            
        }
        return res.toString().trim();//别忘记去掉末尾空格


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(167. 两数之和 II - 输入有序数组)

https://leetcode-cn.com/problems/two-sum-ii-input-array-is-sorted/

## 题目描述

```
给定一个已按照 升序排列  的整数数组 numbers ，请你从数组中找出两个数满足相加之和等于目标数 target 。

函数应该以长度为 2 的整数数组的形式返回这两个数的下标值。numbers 的下标 从 1 开始计数 ，所以答案数组应当满足 1 <= answer[0] < answer[1] <= numbers.length 。

你可以假设每个输入只对应唯一的答案，而且你不可以重复使用相同的元素。

 

示例 1：

输入：numbers = [2,7,11,15], target = 9
输出：[1,2]
解释：2 与 7 之和等于目标数 9 。因此 index1 = 1, index2 = 2 。


示例 2：

输入：numbers = [2,3,4], target = 6
输出：[1,3]


示例 3：

输入：numbers = [-1,0], target = -1
输出：[1,2]


 

提示：

2 <= numbers.length <= 3 * 104
-1000 <= numbers[i] <= 1000
numbers 按 递增顺序 排列
-1000 <= target <= 1000
仅存在一个有效答案
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
    public int[] twoSum(int[] numbers, int target) {
        //双指针起飞
        int left=0,right=numbers.length-1;
        while(left<right){
            int num=numbers[left]+numbers[right];
            if(num==target) break;
            else if(num>target) right--;
            else left++;

        }
        return new int[]{left+1,right+1};


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(39. 数组中出现次数超过一半的数字)

https://leetcode-cn.com/problems/shu-zu-zhong-chu-xian-ci-shu-chao-guo-yi-ban-de-shu-zi-lcof/

## 题目描述

```
数组中有一个数字出现的次数超过数组长度的一半，请找出这个数字。

 

你可以假设数组是非空的，并且给定的数组总是存在多数元素。

 

示例 1:

输入: [1, 2, 3, 2, 2, 2, 5, 4, 2]
输出: 2

 

限制：

1 <= 数组长度 <= 50000

 

注意：本题与主站 169 题相同：https://leetcode-cn.com/problems/majority-element/

 
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
    public int majorityElement(int[] nums) {
        //直接排序是可以的
        //Arrays.sort(nums);
        //return nums[nums.length/2];
        //也可以使用就是先加后减这种方式
        int ans=nums[0];
        int count=1;
        for(int i=1;i<nums.length;i++){
            if(ans!=nums[i]){
                count--;
                if(count==0) {
                    ans=nums[i];
                    count=1;//重新赋值为1

                }
            }
            else count++;
        }
        return ans;


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(55. 跳跃游戏)

https://leetcode-cn.com/problems/jump-game/

## 题目描述

```
给定一个非负整数数组 nums ，你最初位于数组的 第一个下标 。

数组中的每个元素代表你在该位置可以跳跃的最大长度。

判断你是否能够到达最后一个下标。

 

示例 1：

输入：nums = [2,3,1,1,4]
输出：true
解释：可以先跳 1 步，从下标 0 到达下标 1, 然后再从下标 1 跳 3 步到达最后一个下标。


示例 2：

输入：nums = [3,2,1,0,4]
输出：false
解释：无论怎样，总会到达下标为 3 的位置。但该下标的最大跳跃长度是 0 ， 所以永远不可能到达最后一个下标。


 

提示：

1 <= nums.length <= 3 * 104
0 <= nums[i] <= 105
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

class Solution{
public static boolean canJump(int[] nums) {
        if (nums == null) {
            return false;
        }
        //前n-1个元素能够跳到的最远距离，只要可以到达最后的下标就可以了
        int k = 0;//代表最远的距离
        for (int i = 0; i <= k; i++) {
            //相当于最远的距离是一个变量，最远距离之前的我们都可以进行访问 并且更新最远距离
            //第i个元素能够跳到的最远距离
            int temp = i + nums[i];//每一个元素能跳到的最远距离，但是可能没有前边的远，要进行比较之后
            //更新最远距离
            k = Math.max(k, temp);//
            //如果最远距离已经大于或等于最后一个元素的下标,则说明能跳过去,退出. 减少循环
            if (k >= nums.length - 1) {
                return true;
            }
        }
        //最远距离k不再改变,且没有到末尾元素
        return false;
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(207. 课程表)

https://leetcode-cn.com/problems/course-schedule/

## 题目描述

```
你这个学期必须选修 numCourses 门课程，记为 0 到 numCourses - 1 。

在选修某些课程之前需要一些先修课程。 先修课程按数组 prerequisites 给出，其中 prerequisites[i] = [ai, bi] ，表示如果要学习课程 ai 则 必须 先学习课程  bi 。

例如，先修课程对 [0, 1] 表示：想要学习课程 0 ，你需要先完成课程 1 。

请你判断是否可能完成所有课程的学习？如果可以，返回 true ；否则，返回 false 。

 

示例 1：

输入：numCourses = 2, prerequisites = [[1,0]]
输出：true
解释：总共有 2 门课程。学习课程 1 之前，你需要完成课程 0 。这是可能的。

示例 2：

输入：numCourses = 2, prerequisites = [[1,0],[0,1]]
输出：false
解释：总共有 2 门课程。学习课程 1 之前，你需要先完成课程 0 ；并且学习课程 0 之前，你还应先完成课程 1 。这是不可能的。

 

提示：

1 <= numCourses <= 105
0 <= prerequisites.length <= 5000
prerequisites[i].length == 2
0 <= ai, bi < numCourses
prerequisites[i] 中的所有课程对 互不相同
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
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        int[] indegrees = new int[numCourses];//表示相应下标课程有多少前置课程 就是入度个数
        List<List<Integer>> adjacency = new ArrayList<>();//一个二维集合 表示前置课程相应对应的后续课程
        Queue<Integer> queue = new LinkedList<>();//一个双向队列来表示

        //初始化二维集合 很重要
        for(int i = 0; i < numCourses; i++)
            adjacency.add(new ArrayList<>());
        // Get the indegree and adjacency of every course.
        //利用已知的表 来填充结果
        for(int[] cp : prerequisites) {
            indegrees[cp[0]]++;
            adjacency.get(cp[1]).add(cp[0]);
        }
        // Get all the courses with the indegree of 0.
        //入度为0的结果放到队列
        
        for(int i = 0; i < numCourses; i++)
            if(indegrees[i] == 0) queue.add(i);
        // BFS TopSort.
        while(!queue.isEmpty()) {
            int pre = queue.poll();
            numCourses--;//课程个数，出队就说明可以上完
            //出队顺便把这个对应的
            for(int cur : adjacency.get(pre)){//与当前值有关联的后置结果，入度需要减一
                indegrees[cur]--;
                if(indegrees[cur] == 0) queue.add(cur);//如果变成无状态 那么就直接入堆
            }
        }
        return numCourses == 0;
    }
}



```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(61. 扑克牌中的顺子)

https://leetcode-cn.com/problems/bu-ke-pai-zhong-de-shun-zi-lcof/

## 题目描述

```
从扑克牌中随机抽5张牌，判断是不是一个顺子，即这5张牌是不是连续的。2～10为数字本身，A为1，J为11，Q为12，K为13，而大、小王为 0 ，可以看成任意数字。A 不能视为 14。

 

示例 1:

输入: [1,2,3,4,5]
输出: True

 

示例 2:

输入: [0,0,1,2,5]
输出: True

 

限制：

数组长度为 5 

数组的数取值为 [0, 13] .
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
    public boolean isStraight(int[] nums) {
        HashSet<Integer> set=new HashSet<>();
        int maxed=0;
        int mined=Integer.MAX_VALUE;
        for(int i=0;i<nums.length;i++){
            if(set.contains(nums[i])) return false;
            if(nums[i]==0) continue;//统计非0的
            maxed=Math.max(nums[i],maxed);
            mined=Math.min(nums[i],mined);
            set.add(nums[i]);//加入nums[i]看看是否重复


        }
        return maxed-mined<5;


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(547. 省份数量)

https://leetcode-cn.com/problems/number-of-provinces/

## 题目描述

```
有 n 个城市，其中一些彼此相连，另一些没有相连。如果城市 a 与城市 b 直接相连，且城市 b 与城市 c 直接相连，那么城市 a 与城市 c 间接相连。

省份 是一组直接或间接相连的城市，组内不含其他没有相连的城市。

给你一个 n x n 的矩阵 isConnected ，其中 isConnected[i][j] = 1 表示第 i 个城市和第 j 个城市直接相连，而 isConnected[i][j] = 0 表示二者不直接相连。

返回矩阵中 省份 的数量。

 

示例 1：

输入：isConnected = [[1,1,0],[1,1,0],[0,0,1]]
输出：2


示例 2：

输入：isConnected = [[1,0,0],[0,1,0],[0,0,1]]
输出：3


 

提示：

1 <= n <= 200
n == isConnected.length
n == isConnected[i].length
isConnected[i][j] 为 1 或 0
isConnected[i][i] == 1
isConnected[i][j] == isConnected[j][i]
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  并查集

## 代码

- 语言支持：Java

Java Code:

```java

class UnionFind {
    // 记录父节点
    private Map<Integer,Integer> father;
    // 记录集合的数量
    private int numOfSets = 0;
    
    public UnionFind() {
        father = new HashMap<Integer,Integer>();
        numOfSets = 0;
    }
    
    public void add(int x) {
        if (!father.containsKey(x)) {
            father.put(x, null);
            numOfSets++;//一开始假设都不联通
        }
    }
    
    public void merge(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX != rootY){
            father.put(rootX,rootY);
            numOfSets--;//联通之后 少了一个不连通的
        }
    }
    
    public int find(int x) {
        int root = x;
        
        while(father.get(root) != null){
            root = father.get(root);
        }//不断迭代找头上的
        
        while(x != root){
            int original_father = father.get(x);
            father.put(x,root);
            x = original_father;
        }
        //上边这部就是把联通弄成两层的 方便后续找
        
        return root;
    }
    
    public boolean isConnected(int x, int y) {
        return find(x) == find(y);
    }
    
    public int getNumOfSets() {
        return numOfSets;
    }
}

class Solution {
    public int findCircleNum(int[][] isConnected) {
        UnionFind uf = new UnionFind();
        for(int i = 0;i < isConnected.length;i++){
            uf.add(i);
            for(int j = 0;j < i;j++){
                if(isConnected[i][j] == 1){
                    uf.merge(i,j);
                }
            }
        }
        
        return uf.getNumOfSets();
    }
}



```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(135. 分发糖果)

https://leetcode-cn.com/problems/candy/

## 题目描述

```
老师想给孩子们分发糖果，有 N 个孩子站成了一条直线，老师会根据每个孩子的表现，预先给他们评分。

你需要按照以下要求，帮助老师给这些孩子分发糖果：

每个孩子至少分配到 1 个糖果。
评分更高的孩子必须比他两侧的邻位孩子获得更多的糖果。

那么这样下来，老师至少需要准备多少颗糖果呢？

 

示例 1：

输入：[1,0,2]
输出：5
解释：你可以分别给这三个孩子分发 2、1、2 颗糖果。


示例 2：

输入：[1,2,2]
输出：4
解释：你可以分别给这三个孩子分发 1、2、1 颗糖果。
     第三个孩子只得到 1 颗糖果，这已满足上述两个条件。
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
    public int candy(int[] ratings) {
        //基础就是一开始都分成一个
        //左边规则 右边数比左边数大 右边数会增加一个
        //右边规则 左边数比右边数大 左边数加一
        int[] left=new int[ratings.length];
        int[] right=new int[ratings.length];
        Arrays.fill(left,1);
        Arrays.fill(right,1);
        int count=0;
        for(int i=0;i<ratings.length-1;i++){
            if(ratings[i]<ratings[i+1]) left[i+1]=left[i]+1;
        }
        for(int i=ratings.length-1;i>0;i--){
            if(ratings[i-1]>ratings[i]) right[i-1]=right[i]+1;
            
        }
        
        for(int i=0;i<ratings.length;i++){
            count+=Math.max(left[i],right[i]);
        }
        return count;
        

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(450. 删除二叉搜索树中的节点)

https://leetcode-cn.com/problems/delete-node-in-a-bst/

## 题目描述

```
给定一个二叉搜索树的根节点 root 和一个值 key，删除二叉搜索树中的 key 对应的节点，并保证二叉搜索树的性质不变。返回二叉搜索树（有可能被更新）的根节点的引用。

一般来说，删除节点可分为两个步骤：

首先找到需要删除的节点；
如果找到了，删除它。

说明： 要求算法时间复杂度为 O(h)，h 为树的高度。

示例:

root = [5,3,6,2,4,null,7]
key = 3

    5
   / \
  3   6
 / \   \
2   4   7

给定需要删除的节点值是 3，所以我们首先找到 3 这个节点，然后删除它。

一个正确的答案是 [5,4,6,2,null,null,7], 如下图所示。

    5
   / \
  4   6
 /     \
2       7

另一个正确答案是 [5,2,6,null,4,null,7]。

    5
   / \
  2   6
   \   \
    4   7

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
    public TreeNode deleteNode(TreeNode root, int key) {
        //分情况删除 
        if(root==null) return null;
        else if(root.val<key)  root.right=deleteNode(root.right,key);
        else if(root.val>key) root.left= deleteNode(root.left,key);
        else if(root.val==key){
            //返回root.right
            if(root.left==null&&root.right==null) return null;
            else if(root.left==null&&root.right!=null) return root.right;
            else if(root.left!=null&&root.right==null) return root.left;
            else{
                TreeNode node=root.right;
                while(node.left!=null) node=node.left;
                node.left=root.left;
                root=root.right;
               
            }
        }
        return root;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(402. 移掉 K 位数字)

https://leetcode-cn.com/problems/remove-k-digits/

## 题目描述

```
给你一个以字符串表示的非负整数 num 和一个整数 k ，移除这个数中的 k 位数字，使得剩下的数字最小。请你以字符串形式返回这个最小的数字。

 

示例 1 ：

输入：num = "1432219", k = 3
输出："1219"
解释：移除掉三个数字 4, 3, 和 2 形成一个新的最小的数字 1219 。


示例 2 ：

输入：num = "10200", k = 1
输出："200"
解释：移掉首位的 1 剩下的数字为 200. 注意输出不能有任何前导零。


示例 3 ：

输入：num = "10", k = 2
输出："0"
解释：从原数字移除所有的数字，剩余为空就是 0 。


 

提示：

1 <= k <= num.length <= 105
num 仅由若干位数字（0 - 9）组成
除了 0 本身之外，num 不含任何前导零
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
    public String removeKdigits(String num, int k) {
        Deque<Character> stack = new ArrayDeque<>(num.length());
        for(char c : num.toCharArray()){
            while(k > 0 && !stack.isEmpty() && c < stack.peek()){
                stack.pop();
                k--;
            }
            if( c != '0' || !stack.isEmpty()){
            stack.push(c);
            
            }
        }

        while( k > 0 && !stack.isEmpty()){
            stack.pop();
            k--;
        }

        StringBuffer buffer = new StringBuffer();
        while(!stack.isEmpty()){
            buffer.append(stack.pollLast());
        }
        
        return buffer.length() == 0 ? "0" : buffer.toString();
    }
}
```



```java

class Solution {
    public String removeKdigits(String num, int k) {
        //这种题 tm的完全就没有思路 移动数据
        // 思路就是从高位开始 如果下一位比当前位小 当前位应该拜拜
        //用一个栈来存储 注意如果第一个为0的时候 这样永远会被压在底部 出现0123不行 0不能在栈空的时候进入
        //如果删除的值不满足k个 那就从后边删除相应少的为

        //操还能超了
        if(k==num.length()) return "0";
        Stack<Character> stack=new Stack<>();
        for(int i=0;i<num.length();i++){
            if(k>0&&!stack.isEmpty()&&stack.peek()>num.charAt(i)){
                k--;
                stack.pop();            
            }
            //如果是递增的 4 5 6 这种不需要丢弃 因为456 丢掉5变成46 是更大的
            if(!(stack.isEmpty()&&num.charAt(i)=='0')) stack.push(num.charAt(i));
        }
        while(k>0&&!stack.isEmpty()){
            k--;
            stack.pop();
        }
        StringBuilder res=new StringBuilder();
        while(!stack.isEmpty()){
            res.append(stack.pop());
        }
        return res.length()==0?"0":res.reverse().toString();

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(347. 前 K 个高频元素)

https://leetcode-cn.com/problems/top-k-frequent-elements/

## 题目描述

```
给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按 任意顺序 返回答案。

 

示例 1:

输入: nums = [1,1,1,2,2,3], k = 2
输出: [1,2]


示例 2:

输入: nums = [1], k = 1
输出: [1]

 

提示：

1 <= nums.length <= 105
k 的取值范围是 [1, 数组中不相同的元素的个数]
题目数据保证答案唯一，换句话说，数组中前 k 个高频元素的集合是唯一的

 

进阶：你所设计算法的时间复杂度 必须 优于 O(n log n) ，其中 n 是数组大小。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  使用list<>[数组]，右边是new ArrayList[个数]，本质上还是数组 所以寻址用[] 并且需要初始化
-  从keyset当中找东西

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        //使用一个数据桶，代表每一次的频次，代表下标开始
        List<Integer>[] bukit=new ArrayList[nums.length+1];//从下标1开始存储，这是一个list<>数组 长度定了但是每一个元素（就是list是不定的）
        HashMap<Integer,Integer> fre=new HashMap<>();
        for(int num:nums){
            fre.put(num,fre.getOrDefault(num,0)+1);
        }
        //根据频率划分到当中就可以
        for(int num:fre.keySet()){//注意从hashmap当中求解
            int freque=fre.get(num);
            if(bukit[freque]==null) bukit[freque]=new ArrayList<Integer>();
            bukit[freque].add(num);
        }

        List<Integer> res=new ArrayList<>();//存储结果
        //从bukit当中添加到rea
        for(int i=nums.length;i>0;i--){
            ////if(k-res.size()<=0) break;
            if(bukit[i]==null) continue;
            if(k-res.size()>=bukit[i].size()){
                //还有余地放进整个出现次数为当前的
                res.addAll(bukit[i]);
            }
            if(k-res.size()<bukit[i].size()){
                res.addAll(bukit[i].subList(0,k-res.size()));
                
            }
        }
        int[] ans=new int[k];
        for(int i=0;i<k;i++){
            ans[i]=res.get(i);
        }
        return ans;



        //使用来实现 最大出现次数肯定就是nums.length;
        

        


    }
}

```

//栈结果

```java
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        HashMap<Integer,Integer> fre=new HashMap<>();
        for(int num:nums){
            fre.put(num,fre.getOrDefault(num,0)+1);
        }

        //最小堆 
        PriorityQueue<Integer> res=new PriorityQueue<>((a,b)->(fre.get(a)-fre.get(b)));

        //维护一个长度为k的最小堆
        for(int num:fre.keySet()){
         if(res.size()<k){
            res.add(num);
         }
         else{
             if(fre.get(num)>fre.get(res.peek())){//比较频率
                 res.add(num);
                 res.poll();
             }
         }

        }


        
        int[] ans=new int[k];
        for(int i=0;i<k;i++){
            ans[i]=res.poll();
        }
        return ans;



        //使用来实现 最大出现次数肯定就是nums.length;
        

        


    }
}
```





**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(154. 寻找旋转排序数组中的最小值 II)

https://leetcode-cn.com/problems/find-minimum-in-rotated-sorted-array-ii/

## 题目描述

```
已知一个长度为 n 的数组，预先按照升序排列，经由 1 到 n 次 旋转 后，得到输入数组。例如，原数组 nums = [0,1,4,4,5,6,7] 在变化后可能得到：
若旋转 4 次，则可以得到 [4,5,6,7,0,1,4]
若旋转 7 次，则可以得到 [0,1,4,4,5,6,7]

注意，数组 [a[0], a[1], a[2], ..., a[n-1]] 旋转一次 的结果为数组 [a[n-1], a[0], a[1], a[2], ..., a[n-2]] 。

给你一个可能存在 重复 元素值的数组 nums ，它原来是一个升序排列的数组，并按上述情形进行了多次旋转。请你找出并返回数组中的 最小元素 。

 

示例 1：

输入：nums = [1,3,5]
输出：1


示例 2：

输入：nums = [2,2,2,0,1]
输出：0


 

提示：

n == nums.length
1 <= n <= 5000
-5000 <= nums[i] <= 5000
nums 原来是一个升序排序的数组，并进行了 1 至 n 次旋转

 

进阶：

这道题是 寻找旋转排序数组中的最小值 的延伸题目。
允许重复会影响算法的时间复杂度吗？会如何影响，为什么？
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
    public int findMin(int[] nums) {
        int left=0;
        int right=nums.length-1;
        while(left<right){
            int mid=left+(right-left)/2;
            if(nums[mid]>nums[right]){
                left=mid+1;
            }
            else if(nums[mid]<nums[right]){
                right=mid;
            }
            else right--;//因为可能会有重复
        }
        return nums[right];


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(525. 连续数组)

https://leetcode-cn.com/problems/contiguous-array/

## 题目描述

```
给定一个二进制数组 nums , 找到含有相同数量的 0 和 1 的最长连续子数组，并返回该子数组的长度。

 

示例 1:

输入: nums = [0,1]
输出: 2
说明: [0, 1] 是具有相同数量 0 和 1 的最长连续子数组。

示例 2:

输入: nums = [0,1,0]
输出: 2
说明: [0, 1] (或 [1, 0]) 是具有相同数量0和1的最长连续子数组。

 

提示：

1 <= nums.length <= 105
nums[i] 不是 0 就是 1
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
    public int findMaxLength(int[] nums) {
        //使用前缀和presum遍历得到每一个位置上的前缀和
        //hashmap存储达到这个前缀和的最开始下标
        //一旦当前前缀和再次出现 那么可以求长度和最长比较可以
        HashMap<Integer,Integer> res=new HashMap<>();
        res.put(0,-1);
        int maxed=0;
        int presum=0;
        for(int i=0;i<nums.length;i++){
            if(nums[i]==0) presum+=-1;
            else presum+=1;
            
            if(res.containsKey(presum)){
                maxed=Math.max(maxed,i-res.get(presum));
            }
            else{
                res.put(presum,i);
            }

        }
        return  maxed;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(494. 目标和)

https://leetcode-cn.com/problems/target-sum/

## 题目描述

```
给你一个整数数组 nums 和一个整数 target 。

向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式 ：

例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，然后串联起来得到表达式 "+2-1" 。

返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目。

 

示例 1：

输入：nums = [1,1,1,1,1], target = 3
输出：5
解释：一共有 5 种方法让最终目标和为 3 。
-1 + 1 + 1 + 1 + 1 = 3
+1 - 1 + 1 + 1 + 1 = 3
+1 + 1 - 1 + 1 + 1 = 3
+1 + 1 + 1 - 1 + 1 = 3
+1 + 1 + 1 + 1 - 1 = 3


示例 2：

输入：nums = [1], target = 1
输出：1


 

提示：

1 <= nums.length <= 20
0 <= nums[i] <= 1000
0 <= sum(nums[i]) <= 1000
-1000 <= target <= 1000
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
    public int findTargetSumWays(int[] nums, int S) {
        //思路，确实比较难想，为什么呢因为，需要进行变形计算
        //01背包问题是选或者不选，但本题是必须选，是选+还是选-。先将本问题转换为01背包问题。
        /*①假设所有符号为+的元素和为x，符号为-的元素和的绝对值是y。
        我们想要的 S = 正数和 - 负数和 = x - y
        而已知x与y的和是数组总和：x + y = sum
        可以求出 x = (S + sum) / 2 = target
        也就是我们要从nums数组里选出几个数，令其和为target
        于是就转化成了求容量为target的01背包问题 =>要装满容量为target的背包，有几种方案*/
        /*②特例判断
        如果S大于sum，不可能实现，返回0
        如果x不是整数，也就是S + sum不是偶数，不可能实现，返回0
        dp[j]代表的意义：填满容量为j的背包，有dp[j]种方法。因为填满容量为0的背包有且只有一种方法，所以dp[0] = 1
        状态转移：dp[j] = dp[j] + dp[j - num]，
        当前填满容量为j的包的方法数 = 之前填满容量为j的包的方法数 + 之前填满容量为j - num的包的方法数
        也就是当前数num的加入，可以把之前和为j - num的方法数加入进来。
        返回dp[-1]，也就是dp[target]*/

        int sumed=0;
        for(int num:nums) sumed+=num;
        if(S>sumed||(sumed+S)%2==1) return 0;//不满足匹配条件

        int target=(sumed+S)/2;
        int[] dp=new int[target+1];
        //dp[i]代表得到和为i的方案
        dp[0]=1;//初始化
        for(int num:nums){
            //因为每一个j都可能从nums当中得到，所以遍历num
            //而且从后往前遍历 防止重复
            for(int j=target;j>=num;j--){
                //递推表达式
                dp[j]=dp[j]+dp[j-num];

            }
        }
        return dp[target];




    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(416. 分割等和子集)

https://leetcode-cn.com/problems/partition-equal-subset-sum/

## 题目描述

```
给你一个 只包含正整数 的 非空 数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。

 

示例 1：

输入：nums = [1,5,11,5]
输出：true
解释：数组可以分割成 [1, 5, 5] 和 [11] 。

示例 2：

输入：nums = [1,2,3,5]
输出：false
解释：数组不能分割成两个元素和相等的子集。


 

提示：

1 <= nums.length <= 200
1 <= nums[i] <= 100
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [一套框架解决「背包问题」 - 目标和 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/target-sum/solution/yi-tao-kuang-jia-jie-jue-bei-bao-wen-ti-58wvk/) 直接解决
-  [494. 目标和，动态规划之01背包问题 - 目标和 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/target-sum/solution/494-mu-biao-he-dong-tai-gui-hua-zhi-01be-78ll/) 此人的动态规划解法基本无敌

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public boolean canPartition(int[] nums) {
        //1、如果数组的和为基数，则肯定不能二分
        int sum = IntStream.of(nums).sum();
        if (sum % 2 != 0){
            return false;
        }
        //2、0-1背包问题
        //从后往前 因为这个也是选择哪几个数据是否可以得到具体的和，那么可以和目标和一样
        //看看最后的结果是否可以凑到，如果可以凑到那么一定可以分成相应的和
        sum = sum/2;
        //首先创建dp数组
        int[] dp = new int[sum + 1];
        dp[0] = 1;
        for (int number : nums){
            for (int i = sum; i >= number ; i--) {
                dp[i] = dp[i] + dp[i - number];
            }
        }
        if (dp[sum] != 0){
            return true;
        }else{
            return false;
        }
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(862. 和至少为 K 的最短子数组)

https://leetcode-cn.com/problems/shortest-subarray-with-sum-at-least-k/

## 题目描述

```
返回 A 的最短的非空连续子数组的长度，该子数组的和至少为 K 。

如果没有和至少为 K 的非空子数组，返回 -1 。

 

示例 1：

输入：A = [1], K = 1
输出：1


示例 2：

输入：A = [1,2], K = 4
输出：-1


示例 3：

输入：A = [2,-1,2], K = 3
输出：3


 

提示：

1 <= A.length <= 50000
-10 ^ 5 <= A[i] <= 10 ^ 5
1 <= K <= 10 ^ 9
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [JAVA 前缀和与滑动窗 - 和至少为 K 的最短子数组 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/shortest-subarray-with-sum-at-least-k/solution/java-qian-zhui-he-yu-shuang-duan-by-ppppjqute/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int shortestSubarray(int[] A, int K) {
        long [] arr = new long [A.length+1];
        //求前缀和提前求出来，注意arr[i]代表前i个数据的和
        for(int i=0;i<A.length;i++){
            arr[i+1] = arr[i]+A[i];
            if(A[i]>=K) return 1;//如果某一个数据单独大于1 那么可以直接返回这就是最小的
        }//得到前缀和数组
        int res = Integer.MAX_VALUE;
        // for(int i=0;i<=A.length-1;i++){  //暴力破解 N^2 超时
        //     for(int j = i+1;j<=A.length;j++){
        //         if(arr[j]-arr[i]>=K){
        //             res = Math.min(j-i,res);
        //         }
        //     }
        // }
        Deque<Integer> queue = new ArrayDeque<>();//一个双端队列
        for(int i=0;i<arr.length;i++){
            while(!queue.isEmpty()&&arr[i]<=arr[queue.getLast()])   queue.removeLast();//如果前i个前缀和大于 前j个前缀和 并且i<j 那么肯定选j作为窗口开始 因为后边和j一起减一定更容易大于k 并且长度也更小  
            while(!queue.isEmpty()&&arr[i]-arr[queue.peek()]>=K)    res = Math.min(res,i-queue.poll());//一旦满足就比较大小
            queue.add(i);//存储下标
        }
        return res==Integer.MAX_VALUE?-1:res;
    }
}


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(405. 数字转换为十六进制数)

https://leetcode-cn.com/problems/convert-a-number-to-hexadecimal/

## 题目描述

```
给定一个整数，编写一个算法将这个数转换为十六进制数。对于负整数，我们通常使用 补码运算 方法。

注意:

十六进制中所有字母(a-f)都必须是小写。
十六进制字符串中不能包含多余的前导零。如果要转化的数为0，那么以单个字符'0'来表示；对于其他情况，十六进制字符串中的第一个字符将不会是0字符。 
给定的数确保在32位有符号整数范围内。
不能使用任何由库提供的将数字直接转换或格式化为十六进制的方法。

示例 1：

输入:
26

输出:
"1a"


示例 2：

输入:
-1

输出:
"ffffffff"

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
    public String toHex(int num) {
        //其实是很简单，就是不断的与四个1进行与
        //这样可以得到当前数据后四位的值 然后映射就可以
        //直接映射一个字符数组 是多少就是多少就可以啦
        if (num == 0) { return "0"; }   // 0特殊处理
        char[] hex = "0123456789abcdef".toCharArray();  // 相当于映射关系
        StringBuilder ans = new StringBuilder();
        while (num != 0) {
            int temp = num & 0xf;   // 取低4位的十进制值,其实用15也可以，用0xf也可以
            ans.append(hex[temp]);  // 映射对应字符
            num >>>= 4;             // 逻辑右移4位,注意一定是无符号右移，因为其他的右移只是补充符号位，这种全部补充0
        }
        // while的循环条件保证了不会出现前导0
        // 但是从低位开始转换多了一步reverse反转
        return ans.reverse().toString();
    }
}



```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(295. 数据流的中位数)

https://leetcode-cn.com/problems/find-median-from-data-stream/

## 题目描述

```
中位数是有序列表中间的数。如果列表长度是偶数，中位数则是中间两个数的平均值。

例如，

[2,3,4] 的中位数是 3

[2,3] 的中位数是 (2 + 3) / 2 = 2.5

设计一个支持以下两种操作的数据结构：

void addNum(int num) - 从数据流中添加一个整数到数据结构中。
double findMedian() - 返回目前所有元素的中位数。

示例：

addNum(1)
addNum(2)
findMedian() -> 1.5
addNum(3) 
findMedian() -> 2

进阶:

如果数据流中所有整数都在 0 到 100 范围内，你将如何优化你的算法？
如果数据流中 99% 的整数都在 0 到 100 范围内，你将如何优化你的算法？
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

import java.util.PriorityQueue;
/**
前置知识：
需要找到堆排序的知识。
比如，大顶堆：这堆中（一般是二叉树结构），根节点的元素时最大的，对左右节点的大小没有限制。严格的定义是所有的父节点元素比子节点元素要大。小顶堆同理。找到这个前置知识应该就能解决这道题目了。另外，在java中，这种堆结构使用PriorityQueue优先队列的结构。
解题思路：
一个数据结构没有办法实现求一个流的中位数，所以需要两个优先级队列。
需要维护这另个队列的元素数量差小于等于1，这样求中位数直接拿堆顶元素即可。
注意：大顶堆存储流的小元素，取值取堆中的大元素，也就是中间的位置。小顶堆存储大元素，取值取堆顶，大中的小元素，也是中间段。同时，需要确保小顶堆存储的大元素都比大顶堆存储的小元素都要大。这样才能确保去中位数的时候是正确的。
总结：
把流的数据分成两堆，一堆大的，一堆小的，大堆的数据都比小堆的大。同时两堆的数量差控制在1以内。求中位数的时候，直接拿大堆的最小元素，和小堆的最大元素，就可以求出中位数。这也就是大的堆需要用小顶堆求最小的元素的原因。小堆同理。
代码实现细节：
① getMedian：如果两个堆的元素一样，那么去两个堆顶元素的平均值，否则取数量多的堆的堆顶元素即可。
② add：根据两个堆的数量，决定要把元素添加到哪个堆里面
	 比如：大的堆数量多，则需要把元素添加到小的堆里面。
	注意：这个时候不能直接添加到小的堆里面，因为这样没有办法确保小的堆的所有元素都比大堆的元素要小，那么获取中位数就会出现问题。所以需要一个技巧，先加入到大顶堆，然后取栈顶元素，大顶堆的使用的是小顶堆的数据结构，所以堆顶的元素是大元素中最小的元素，把它加入到小的元素，就不会出现上面的问题。具体细节看代码实现。十分巧妙。
*/

class MedianFinder {
    private PriorityQueue<Integer> large;
    private PriorityQueue<Integer> small;

    public MedianFinder() {
        // 小顶堆
        large = new PriorityQueue<>();
        // 大顶堆
        small = new PriorityQueue<>((a, b) -> {
            return b - a;
        });
    }

    public double findMedian() {
        // 如果元素不一样多，多的那个堆的堆顶元素就是中位数
        if (large.size() < small.size()) {
            return small.peek();
        } else if (large.size() > small.size()) {
            return large.peek();
        }
        // 如果元素一样多，两个堆堆顶元素的平均数是中位数
        return (large.peek() + small.peek()) / 2.0;
    }

    public void addNum(int num) {
    if (small.size() >= large.size()) {
        small.offer(num);
        large.offer(small.poll());
    } else {
        large.offer(num);
        small.offer(large.poll());
    }
    }
}

/**
 * Your MedianFinder object will be instantiated and called as such:
 * MedianFinder obj = new MedianFinder();
 * obj.addNum(num);
 * double param_2 = obj.findMedian();
 */






import java.util.PriorityQueue;
/**
前置知识：
需要找到堆排序的知识。
比如，大顶堆：这堆中（一般是二叉树结构），根节点的元素时最大的，对左右节点的大小没有限制。严格的定义是所有的父节点元素比子节点元素要大。小顶堆同理。找到这个前置知识应该就能解决这道题目了。另外，在java中，这种堆结构使用PriorityQueue优先队列的结构。
解题思路：
一个数据结构没有办法实现求一个流的中位数，所以需要两个优先级队列。
需要维护这另个队列的元素数量差小于等于1，这样求中位数直接拿堆顶元素即可。
注意：大顶堆存储流的小元素，取值取堆中的大元素，也就是中间的位置。小顶堆存储大元素，取值取堆顶，大中的小元素，也是中间段。同时，需要确保小顶堆存储的大元素都比大顶堆存储的小元素都要大。这样才能确保去中位数的时候是正确的。
总结：
把流的数据分成两堆，一堆大的，一堆小的，大堆的数据都比小堆的大。同时两堆的数量差控制在1以内。求中位数的时候，直接拿大堆的最小元素，和小堆的最大元素，就可以求出中位数。这也就是大的堆需要用小顶堆求最小的元素的原因。小堆同理。
代码实现细节：
① getMedian：如果两个堆的元素一样，那么去两个堆顶元素的平均值，否则取数量多的堆的堆顶元素即可。
② add：根据两个堆的数量，决定要把元素添加到哪个堆里面
​	 比如：大的堆数量多，则需要把元素添加到小的堆里面。
​	注意：这个时候不能直接添加到小的堆里面，因为这样没有办法确保小的堆的所有元素都比大堆的元素要小，那么获取中位数就会出现问题。所以需要一个技巧，先加入到大顶堆，然后取栈顶元素，大顶堆的使用的是小顶堆的数据结构，所以堆顶的元素是大元素中最小的元素，把它加入到小的元素，就不会出现上面的问题。具体细节看代码实现。十分巧妙。
*/

class MedianFinder {
    private PriorityQueue<Integer> large;//存储后半部分数据，是一个小根堆
    private PriorityQueue<Integer> small;//存储前半部分数据，是一个大根堆

    public MedianFinder() {
        large=new PriorityQueue<>();
        small=new PriorityQueue<>((a,b)->b-a);


       
    }

    public double findMedian() {
        if(large.size()>small.size()) return (double) large.peek();
        else if(large.size()<small.size()) return (double) small.peek();
        else return (large.peek()+small.peek())/2.0;
        
    }

    public void addNum(int num) {
        if(small.size()>=large.size()){
            small.add(num);//保证前后部分大小关系
            large.add(small.poll());
        }
        else{
            large.add(num);//保证前后部分大小关系
            small.add(large.poll());

        }

    }
}

/**
 * Your MedianFinder object will be instantiated and called as such:
 * MedianFinder obj = new MedianFinder();
 * obj.addNum(num);
 * double param_2 = obj.findMedian();
 */

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(400. 第 N 位数字)

https://leetcode-cn.com/problems/nth-digit/

## 题目描述

```
在无限的整数序列 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ...中找到第 n 位数字。

 

注意：n 是正数且在 32 位整数范围内（n < 231）。

 

示例 1：

输入：3
输出：3


示例 2：

输入：11
输出：0
解释：第 11 位数字在序列 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ... 里是 0 ，它是 10 的一部分。

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
    static final int[] level = new int[10];

    static {
        level[0] = 0;
        level[1] = level[0] + 9;
        level[2] = level[1] + 90 * 2;
        level[3] = level[2] + 900 * 3;
        level[4] = level[3] + 9000 * 4;
        level[5] = level[4] + 90000 * 5;
        level[6] = level[5] + 900000 * 6;
        level[7] = level[6] + 9000000 * 7;
        level[8] = level[7] + 90000000 * 8;
        level[9] = Integer.MAX_VALUE;
    }

    public int findNthDigit(int n) {
        int len = 1, base = 1;
        while (level[len] < n) {
            ++len;
            base *= 10;
        }
        n -= level[len - 1];
        if (n % len == 0) return getPos(base + n / len - 1, len);
        return getPos(base + n / len, n % len);
    }

    // 获取数字num的第i位
    int getPos(int num, int i) {
        return Integer.toString(num).charAt(i - 1) - '0';
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(139. 单词拆分)

https://leetcode-cn.com/problems/word-break/

## 题目描述

```
给定一个非空字符串 s 和一个包含非空单词的列表 wordDict，判定 s 是否可以被空格拆分为一个或多个在字典中出现的单词。

说明：

拆分时可以重复使用字典中的单词。
你可以假设字典中没有重复的单词。

示例 1：

输入: s = "leetcode", wordDict = ["leet", "code"]
输出: true
解释: 返回 true 因为 "leetcode" 可以被拆分成 "leet code"。


示例 2：

输入: s = "applepenapple", wordDict = ["apple", "pen"]
输出: true
解释: 返回 true 因为 "applepenapple" 可以被拆分成 "apple pen apple"。
     注意你可以重复使用字典中的单词。


示例 3：

输入: s = "catsandog", wordDict = ["cats", "dog", "sand", "and", "cat"]
输出: false

```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [【单词拆分】拒绝装x，从简单的想法出发，轻松识破动态规划小套路 - 单词拆分 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/word-break/solution/dan-ci-chai-fen-ju-jue-zhuang-xcong-jian-dan-de-xi/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    /*
        动态规划算法，dp[i]表示s前i个字符能否拆分
        转移方程：dp[j] = dp[i] && check(s[i+1, j]);
        check(s[i+1, j])就是判断i+1到j这一段字符是否能够拆分
        其实，调整遍历顺序，这等价于s[i+1, j]是否是wordDict中的元素
        这个举个例子就很容易理解。
        假如wordDict=["apple", "pen", "code"],s = "applepencode";
        dp[8] = dp[5] + check("pen")
        翻译一下：前八位能否拆分取决于前五位能否拆分，加上五到八位是否属于字典
        （注意：i的顺序是从j-1 -> 0哦~
    */

    public HashMap<String, Boolean> hash = new HashMap<>();
    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length()+1];
        
        //方便check，构建一个哈希表,方便看子串是否方便在已知结果当中
        for(String word : wordDict){
            hash.put(word, true);
        }

        //初始化
        dp[0] = true;//初始条件很有必要

        //遍历
        for(int j = 1; j <= s.length(); j++){
            for(int i = j-1; i >= 0; i--){
                dp[j] = dp[i] && check(s.substring(i, j));
                if(dp[j])   break;//只要有一种可能能达到就可以跳出结果了
            }
        }

        return dp[s.length()];
    }

    public boolean check(String s){
        return hash.getOrDefault(s, false);
    }
}


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(134. 加油站)

https://leetcode-cn.com/problems/gas-station/

## 题目描述

```
在一条环路上有 N 个加油站，其中第 i 个加油站有汽油 gas[i] 升。

你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。你从其中的一个加油站出发，开始时油箱为空。

如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。

说明: 

如果题目有解，该答案即为唯一答案。
输入数组均为非空数组，且长度相同。
输入数组中的元素均为非负数。

示例 1:

输入: 
gas  = [1,2,3,4,5]
cost = [3,4,5,1,2]

输出: 3

解释:
从 3 号加油站(索引为 3 处)出发，可获得 4 升汽油。此时油箱有 = 0 + 4 = 4 升汽油
开往 4 号加油站，此时油箱有 4 - 1 + 5 = 8 升汽油
开往 0 号加油站，此时油箱有 8 - 2 + 1 = 7 升汽油
开往 1 号加油站，此时油箱有 7 - 3 + 2 = 6 升汽油
开往 2 号加油站，此时油箱有 6 - 4 + 3 = 5 升汽油
开往 3 号加油站，你需要消耗 5 升汽油，正好足够你返回到 3 号加油站。
因此，3 可为起始索引。

示例 2:

输入: 
gas  = [2,3,4]
cost = [3,4,3]

输出: -1

解释:
你不能从 0 号或 1 号加油站出发，因为没有足够的汽油可以让你行驶到下一个加油站。
我们从 2 号加油站出发，可以获得 4 升汽油。 此时油箱有 = 0 + 4 = 4 升汽油
开往 0 号加油站，此时油箱有 4 - 3 + 2 = 3 升汽油
开往 1 号加油站，此时油箱有 3 - 3 + 3 = 3 升汽油
你无法返回 2 号加油站，因为返程需要消耗 4 升汽油，但是你的油箱只有 3 升汽油。
因此，无论怎样，你都不可能绕环路行驶一周。
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

class Solution{
    //暴力解法
public int canCompleteCircuit(int[] gas, int[] cost) {
    int n = gas.length;
    //考虑从每一个点出发
    for (int i = 0; i < n; i++) {
        int j = i;//这是相当于从每一个点出发的遍历数组
        int remain = gas[i]- cost[j];//初始化看是否能够到达下一个节点，预先判断当前剩下的油是否可以到达下一个点
        //当前剩余的油能否到达下一个点
        while (remain >= 0) {
            //减去花费的加上新的点的补给
             j = (j + 1) % n;//说明可以到下一个点了
             //j 回到了 i，这时候判断
            if (j == i) {
                return i;
            }
            //这个就是求当前点的预估油量
            remain = remain+ gas[j % n]-cost[j% n];
           
            
        }
    }
    //任何点都不可以
    return -1;
}
}


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(134. 加油站)

https://leetcode-cn.com/problems/gas-station/

## 题目描述

```
在一条环路上有 N 个加油站，其中第 i 个加油站有汽油 gas[i] 升。

你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。你从其中的一个加油站出发，开始时油箱为空。

如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。

说明: 

如果题目有解，该答案即为唯一答案。
输入数组均为非空数组，且长度相同。
输入数组中的元素均为非负数。

示例 1:

输入: 
gas  = [1,2,3,4,5]
cost = [3,4,5,1,2]

输出: 3

解释:
从 3 号加油站(索引为 3 处)出发，可获得 4 升汽油。此时油箱有 = 0 + 4 = 4 升汽油
开往 4 号加油站，此时油箱有 4 - 1 + 5 = 8 升汽油
开往 0 号加油站，此时油箱有 8 - 2 + 1 = 7 升汽油
开往 1 号加油站，此时油箱有 7 - 3 + 2 = 6 升汽油
开往 2 号加油站，此时油箱有 6 - 4 + 3 = 5 升汽油
开往 3 号加油站，你需要消耗 5 升汽油，正好足够你返回到 3 号加油站。
因此，3 可为起始索引。

示例 2:

输入: 
gas  = [2,3,4]
cost = [3,4,3]

输出: -1

解释:
你不能从 0 号或 1 号加油站出发，因为没有足够的汽油可以让你行驶到下一个加油站。
我们从 2 号加油站出发，可以获得 4 升汽油。 此时油箱有 = 0 + 4 = 4 升汽油
开往 0 号加油站，此时油箱有 4 - 3 + 2 = 3 升汽油
开往 1 号加油站，此时油箱有 3 - 3 + 3 = 3 升汽油
你无法返回 2 号加油站，因为返程需要消耗 4 升汽油，但是你的油箱只有 3 升汽油。
因此，无论怎样，你都不可能绕环路行驶一周。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

[详细通俗的思路分析，多解法 - 加油站 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/gas-station/solution/xiang-xi-tong-su-de-si-lu-fen-xi-duo-jie-fa-by--30/)

## 关键点

![image-20210628193846643](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210628193846643.png)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution{
    //暴力解法
public int canCompleteCircuit(int[] gas, int[] cost) {
    int n = gas.length;
    //考虑从每一个点出发
    for (int i = 0; i < n; i++) {
        int j = i;//这是相当于从每一个点出发的遍历数组
        int remain = gas[i]- cost[j];//初始化看是否能够到达下一个节点，预先判断当前剩下的油是否可以到达下一个点
        //当前剩余的油能否到达下一个点
        while (remain >= 0) {
            //减去花费的加上新的点的补给
             j = (j + 1) % n;//说明可以到下一个点了
             //j 回到了 i，这时候判断
            if (j == i) {
                return i;
            }
            //这个就是求当前点的预估油量
            remain = remain+ gas[j % n]-cost[j% n];
            

           
            

        }
        //这里有一个优化
        //根据递推 因为j是每一个i到达的最远位置 可以推出来 从i+1到j-1之间都无法绕一圈 所以直接下一个从j开始就可以
        if(j<i) return -1;//更有甚者如果j越过末尾从头开始 那么 说明到尾部都无法围成一圈了，直接范围
        i=j;
    }
    //任何点都不可以
    return -1;
}
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(400. 第 N 位数字)

https://leetcode-cn.com/problems/nth-digit/

## 题目描述

```
在无限的整数序列 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ...中找到第 n 位数字。

 

注意：n 是正数且在 32 位整数范围内（n < 231）。

 

示例 1：

输入：3
输出：3


示例 2：

输入：11
输出：0
解释：第 11 位数字在序列 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ... 里是 0 ，它是 10 的一部分。

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
    static final int[] level = new int[10];

    static {
        //代表的是几位数的末尾数字的下标
        level[0] = 0;
        level[1] = level[0] + 9;
        level[2] = level[1] + 90 * 2;
        level[3] = level[2] + 900 * 3;
        level[4] = level[3] + 9000 * 4;
        level[5] = level[4] + 90000 * 5;
        level[6] = level[5] + 900000 * 6;
        level[7] = level[6] + 9000000 * 7;
        level[8] = level[7] + 90000000 * 8;
        level[9] = Integer.MAX_VALUE;//超过最大值
    }

    public int findNthDigit(int n) {
        int len = 1, base = 1;
        while (level[len] < n) {
            ++len;//求出这是该几位数
            base *= 10;//因为每一位数都是从10 100 1000开始的
        }
        n -= level[len - 1];//求出他是该位的第一个数据

        //下面就应该是找寻当这个数据所在的数字 num=base+n/len 
        //位置应该是n%len


        //但是有一个特殊情况 就是 n%len我们想着是从0 1 2 3 
        //但是实际上是 1 2 3 。。。。0这种
        //所以当n%len==0时候 说明这个是当前数据的末尾的数字 比如说 第一个数字 为10 的第2个数字
        //应该是 数字 base+n/len-1 并且是第len个数字
        if (n % len == 0) return getPos(base + n / len - 1, len);

    ///   //下面就应该是找寻当这个数据所在的数字 num=base+n/len 
        //位置应该是n%len

        return getPos(base + n / len, n % len);
    }

    // 获取数字num的第i位
    int getPos(int num, int i) {
        return String.valueOf(num).charAt(i - 1) - '0';//找相应位置 
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## ==题目地址(188. 买卖股票的最佳时机 IV)==

https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iv/

## 题目描述

```
给定一个整数数组 prices ，它的第 i 个元素 prices[i] 是一支给定的股票在第 i 天的价格。

设计一个算法来计算你所能获取的最大利润。你最多可以完成 k 笔交易。

注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。

 

示例 1：

输入：k = 2, prices = [2,4,1]
输出：2
解释：在第 1 天 (股票价格 = 2) 的时候买入，在第 2 天 (股票价格 = 4) 的时候卖出，这笔交易所能获得利润 = 4-2 = 2 。

示例 2：

输入：k = 2, prices = [3,2,6,5,0,3]
输出：7
解释：在第 2 天 (股票价格 = 2) 的时候买入，在第 3 天 (股票价格 = 6) 的时候卖出, 这笔交易所能获得利润 = 6-2 = 4 。
     随后，在第 5 天 (股票价格 = 0) 的时候买入，在第 6 天 (股票价格 = 3) 的时候卖出, 这笔交易所能获得利润 = 3-0 = 3 。

 

提示：

0 <= k <= 100
0 <= prices.length <= 1000
0 <= prices[i] <= 1000
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [股票交易系列：贪心思想和动态规划 - 买卖股票的最佳时机 IV - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iv/solution/gu-piao-jiao-yi-xi-lie-cong-tan-xin-dao-dong-tai-g/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int maxProfit(int K, int[] prices) {//这里悄咪咪把小k换成了大K，便于后续索引赋值
        int n=prices.length;
        if(n<=1)    return 0;
        //因为一次交易至少涉及两天，所以如果k大于总天数的一半，就直接取天数一半即可，多余的交易次数是无意义的
        K=Math.min(K,n/2);

        /*dp定义：dp[i][j][k]代表 第i天交易了k次时的最大利润，其中j代表当天是否持有股票，0不持有，1持有*/
        int[][][] dp=new int[n][2][K+1];
        for(int k=0;k<=K;k++){
            dp[0][0][k]=0;
            dp[0][1][k]=-prices[0];
        }

        /*状态方程：
        dp[i][0][k]，当天不持有股票时，看前一天的股票持有情况
        dp[i][1][k]，当天持有股票时，看前一天的股票持有情况*/
        for(int i=1;i<n;i++){
            for(int k=1;k<=K;k++){
                dp[i][0][k]=Math.max(dp[i-1][0][k],dp[i-1][1][k]+prices[i]);
                dp[i][1][k]=Math.max(dp[i-1][1][k],dp[i-1][0][k-1]-prices[i]);
            }
        }
        return dp[n-1][0][K];
    }
}



```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(572. 另一个树的子树)

https://leetcode-cn.com/problems/subtree-of-another-tree/

## 题目描述

```
给定两个非空二叉树 s 和 t，检验 s 中是否包含和 t 具有相同结构和节点值的子树。s 的一个子树包括 s 的一个节点和这个节点的所有子孙。s 也可以看做它自身的一棵子树。

示例 1:
给定的树 s:

     3
    / \
   4   5
  / \
 1   2


给定的树 t：

   4 
  / \
 1   2


返回 true，因为 t 与 s 的一个子树拥有相同的结构和节点值。

示例 2:
给定的树 s：

     3
    / \
   4   5
  / \
 1   2
    /
   0


给定的树 t：

   4
  / \
 1   2


返回 false。
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
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        //感觉像是递归问题
        //子问题就是当前值
        //分情况来进行讨论,别忘记这个返回值是boolean类型的
        if(root==null&&subRoot==null) return true;
        else if(root==null||subRoot==null) return false;
        else return isSubtree(root.left,subRoot)||isSubtree(root.right,subRoot)||isSame(root,subRoot);
        //说明现在root和subRoot都不为空 那么就是两种可能 一种是root和subroot开始相同
        //或者从左子树以及右子树相同也可以


    }

    public boolean isSame(TreeNode root,TreeNode subRoot){
        if(root==null&&subRoot==null) return true;
        else if(root==null||subRoot==null) return false;
        else if(root.val!=subRoot.val) return false;
        else return isSame(root.left,subRoot.left)&&isSame(root.right,subRoot.right);
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(03. 数组中重复的数字)

https://leetcode-cn.com/problems/shu-zu-zhong-zhong-fu-de-shu-zi-lcof/

## 题目描述

```
找出数组中重复的数字。


在一个长度为 n 的数组 nums 里的所有数字都在 0～n-1 的范围内。数组中某些数字是重复的，但不知道有几个数字重复了，也不知道每个数字重复了几次。请找出数组中任意一个重复的数字。

示例 1：

输入：
[2, 3, 1, 0, 2, 5, 3]
输出：2 或 3 


 

限制：

2 <= n <= 100000
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  还是具体的思路问题

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int findRepeatNumber(int[] nums) {
        //使用那个抽屉法
        //不符合条件对应直接输出
        for(int i=0;i<nums.length;i++){
            while(nums[nums[i]]!=nums[i]){
                swap(nums,i,nums[i]);
            }
        }
        int ans=-1;
        for(int i=0;i<nums.length;i++){
            if(nums[i]!=i){
                ans=nums[i];
                break;
            }
        }
        return ans;


    }
    public void swap(int[] nums,int i,int j){
        int tem=nums[i];
        nums[i]=nums[j];
        nums[j]=tem;
        }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(17. 电话号码的字母组合)

https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number/

## 题目描述

```
给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按 任意顺序 返回。

给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。

 

示例 1：

输入：digits = "23"
输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]


示例 2：

输入：digits = ""
输出：[]


示例 3：

输入：digits = "2"
输出：["a","b","c"]


 

提示：

0 <= digits.length <= 4
digits[i] 是范围 ['2', '9'] 的一个数字。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  非常标准的回溯行为
-  每一个部分都是直接进行遍历

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
	//一个映射表，第二个位置是"abc“,第三个位置是"def"。。。
	//这里也可以用map，用数组可以更节省点内存
	String[] letter_map = {" ","*","abc","def","ghi","jkl","mno","pqrs","tuv","wxyz"};
	public List<String> letterCombinations(String digits) {
		//注意边界条件
		if(digits==null || digits.length()==0) {
			return new ArrayList<>();
		}
        List<String> res = new ArrayList<>();
        StringBuilder builder=new StringBuilder();
		iterStr(digits, builder, 0,res);
		return res;
	}
	//最终输出结果的list
	
	
	//递归函数，这里使用了递归深度这个来进行求解，还是可以知道哪里是最后要输出的结果分析，其实letter这种也是比较可以的
    
	void iterStr(String str, StringBuilder letter, int index,List<String> res) {
		//递归的终止条件，注意这里的终止条件看上去跟动态演示图有些不同，主要是做了点优化
		//动态图中是每次截取字符串的一部分，"234"，变成"23"，再变成"3"，最后变成""，这样性能不佳
		//而用index记录每次遍历到字符串的位置，这样性能更好
		if(index == str.length()) {
			res.add(new String(letter.toString()));//一定是要是新的
			return;
		}
		//获取index位置的字符，假设输入的字符是"234"
		//第一次递归时index为0所以c=2，第二次index为1所以c=3，第三次c=4
		//subString每次都会生成新的字符串，而index则是取当前的一个字符，所以效率更高一点
		char c = str.charAt(index);
		//map_string的下表是从0开始一直到9， c-'0'就可以取到相对的数组下标位置
		//比如c=2时候，2-'0'，获取下标为2,letter_map[2]就是"abc"
		int pos = c - '0';
		String map_string = letter_map[pos];
		//遍历字符串，比如第一次得到的是2，页就是遍历"abc"
		for(int i=0;i<map_string.length();i++) {
			//调用下一层递归，用文字很难描述，请配合动态图理解
            letter.append(map_string.charAt(i));
            //如果是String类型做拼接效率会比较低
			//iterStr(str, letter+map_string.charAt(i), index+1);
            iterStr(str, letter, index+1,res);
            letter.deleteCharAt(letter.length()-1);
		}
	}
}



```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(04. 二维数组中的查找)

https://leetcode-cn.com/problems/er-wei-shu-zu-zhong-de-cha-zhao-lcof/

## 题目描述

```
在一个 n * m 的二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个高效的函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。

 

示例:

现有矩阵 matrix 如下：

[
  [1,   4,  7, 11, 15],
  [2,   5,  8, 12, 19],
  [3,   6,  9, 16, 22],
  [10, 13, 14, 17, 24],
  [18, 21, 23, 26, 30]
]


给定 target = 5，返回 true。

给定 target = 20，返回 false。

 

限制：

0 <= n <= 1000

0 <= m <= 1000

 

注意：本题与主站 240 题相同：https://leetcode-cn.com/problems/search-a-2d-matrix-ii/
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  直接使用放缩 看看在哪里有大小的固定方向

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        if(matrix==null||matrix.length==0) return false;
        int m=matrix.length;
        int n=matrix[0].length;
        int i=0,j=n-1;
        while(i<m&&j>=0){
            if(matrix[i][j]==target) return true;
            else if(matrix[i][j]>target) j--;
            else i++;
        }
        return false;


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(42. 连续子数组的最大和)

https://leetcode-cn.com/problems/lian-xu-zi-shu-zu-de-zui-da-he-lcof/

## 题目描述

```
输入一个整型数组，数组中的一个或连续多个整数组成一个子数组。求所有子数组的和的最大值。

要求时间复杂度为O(n)。

 

示例1:

输入: nums = [-2,1,-3,4,-1,2,1,-5,4]
输出: 6
解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。

 

提示：

1 <= arr.length <= 10^5
-100 <= arr[i] <= 100

注意：本题与主站 53 题相同：https://leetcode-cn.com/problems/maximum-subarray/

 
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
    public int maxSubArray(int[] nums) {
        //dp[i] 以nums[i]为结尾的最大值
        if(nums.length==0||nums==null) return 0;
        int[] dp=new int[nums.length];
        dp[0]=nums[0];
        int maxed=Integer.MIN_VALUE;
        for(int i=1;i<nums.length;i++){
            dp[i]=nums[i]+Math.max(0,dp[i-1]);
            maxed=Math.max(dp[i],maxed);

        }
        return Math.max(dp[0],maxed);

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(410. 分割数组的最大值)

https://leetcode-cn.com/problems/split-array-largest-sum/

## 题目描述

```
给定一个非负整数数组 nums 和一个整数 m ，你需要将这个数组分成 m 个非空的连续子数组。

设计一个算法使得这 m 个子数组各自和的最大值最小。

 

示例 1：

输入：nums = [7,2,5,10,8], m = 2
输出：18
解释：
一共有四种方法将 nums 分割为 2 个子数组。 其中最好的方式是将其分为 [7,2,5] 和 [10,8] 。
因为此时这两个子数组各自的和的最大值为18，在所有情况中最小。

示例 2：

输入：nums = [1,2,3,4,5], m = 2
输出：9


示例 3：

输入：nums = [1,4,4], m = 3
输出：4


 

提示：

1 <= nums.length <= 1000
0 <= nums[i] <= 106
1 <= m <= min(50, nums.length)
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
    public int splitArray(int[] nums, int m) {
        //因为组合和最小值 范围一定是 max(nums[i]) 与sum(nums[i]) 之间
        //在这之间进行二分查找就可以
        //每一个mid都尽量平均进行分配
        //思路 从头统计整数和 每一个尽量接近mid，因为一定是平分最合理这样最大值会最小
        //具体的分发就是每一个和都不大于这个mid
        //看看一共能分成几个段，如果分的分数过小 就说明mid太小 left=mid+1
        //如果分的分数过大，说明mid太大 right=mid

        int left=0;//代表最大值，同时也是范围的左边界
        int right=0;//代表整体的组合和，同时也是范围的右边界
        for(int i=0;i<nums.length;i++){
            left=Math.max(left,nums[i]);
            right+=nums[i];
        }
        while(left<right){
            int mid=left+(right-left)/2;
            //对于每一个mid需要一个循环
            int count=0;//计算平均分割的个数
            int sumed=0;//每一部分的累加和
            for(int i=0;i<nums.length;i++){
                sumed+=nums[i];
                if(sumed>mid) 
                {
                    //这个判断就是防止恰好最后平均分成 这样下一个的count++就加错了
                    count++;
                    sumed=nums[i];//每一次满了之后就进行赋值重新计算下一段的和，前边一段和不包含nums[i] 因为加上就大于mid了
                }
            }
            count++;//最后一段不管有没有满都要加上
            if(count>m) left=mid+1;//分的段数越少 代表mid越大 分的段数越多 代表mid月小
            else right=mid;//就离谱
            
        }
        return left;



    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(16. 最接近的三数之和)

https://leetcode-cn.com/problems/3sum-closest/

## 题目描述

```
给定一个包括 n 个整数的数组 nums 和 一个目标值 target。找出 nums 中的三个整数，使得它们的和与 target 最接近。返回这三个数的和。假定每组输入只存在唯一答案。

 

示例：

输入：nums = [-1,2,1,-4], target = 1
输出：2
解释：与 target 最接近的和是 2 (-1 + 2 + 1 = 2) 。


 

提示：

3 <= nums.length <= 10^3
-10^3 <= nums[i] <= 10^3
-10^4 <= target <= 10^4
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
    public int threeSumClosest(int[] nums, int target) {
        //不重复，直接比较就可以
        if(nums.length==0||nums==null) return 0;
        Arrays.sort(nums);
        int sumed=0;
        int mined=nums[0]+nums[1]+nums[2];//因为这个target不知道是多少，所以不能盲目选 选一个开始的来进行比较
        for(int i=0;i<nums.length;i++){
            int left=i+1;
            int right=nums.length-1;
            while(left<right){
                sumed=nums[i]+nums[left]+nums[right];
                
                if(sumed>target) right--;
                if(sumed<target) left++;
                if(sumed==target) return sumed;
                //但是需要求出更接近的
                if(Math.abs(sumed-target)<Math.abs(mined-target)) mined=sumed;
                
            }

        }
        return mined;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(530. 二叉搜索树的最小绝对差)

https://leetcode-cn.com/problems/minimum-absolute-difference-in-bst/

## 题目描述

```
给你一棵所有节点为非负值的二叉搜索树，请你计算树中任意两节点的差的绝对值的最小值。

 

示例：

输入：

   1
    \
     3
    /
   2

输出：
1

解释：
最小绝对差为 1，其中 2 和 1 的差的绝对值为 1（或者 2 和 3）。


 

提示：

树中至少有 2 个节点。
本题与 783 https://leetcode-cn.com/problems/minimum-distance-between-bst-nodes/ 相同
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
    private int mined=Integer.MAX_VALUE;
    private TreeNode pre=null;//存储前一个值
    public int getMinimumDifference(TreeNode root) {
        //中序遍历 存储下每一个最小值和
        if(root==null) return 0;
        dfs(root);
        return mined;


    }
    public void dfs(TreeNode root){
        if(root==null) return;
        dfs(root.left);
        if(pre==null) pre=root;//第一次要赋值
        else {
        mined=Math.min(mined,Math.abs(pre.val-root.val));
        pre=root;
        }
        dfs(root.right);
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(171. Excel表列序号)

https://leetcode-cn.com/problems/excel-sheet-column-number/

## 题目描述

```
给定一个Excel表格中的列名称，返回其相应的列序号。

例如，

    A -> 1
    B -> 2
    C -> 3
    ...
    Z -> 26
    AA -> 27
    AB -> 28 
    ...


示例 1:

输入: "A"
输出: 1


示例 2:

输入: "AB"
输出: 28


示例 3:

输入: "ZY"
输出: 701

致谢：
特别感谢 @ts 添加此问题并创建所有测试用例。
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
    public int titleToNumber(String columnTitle) {
        //直接26进制
        if(columnTitle==null||columnTitle.length()==0) return 0;
        //需要有一个最终结果
        int ans=0;
        for(int i=0;i<columnTitle.length();i++){
            int mid=columnTitle.charAt(i)-'A'+1;
            ans=ans*26+mid;


        }
        return ans;


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(172. 阶乘后的零)

https://leetcode-cn.com/problems/factorial-trailing-zeroes/

## 题目描述

```
给定一个整数 n，返回 n! 结果尾数中零的数量。

示例 1:

输入: 3
输出: 0
解释: 3! = 6, 尾数中没有零。

示例 2:

输入: 5
输出: 1
解释: 5! = 120, 尾数中有 1 个零.

说明: 你算法的时间复杂度应为 O(log n) 。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [详细通俗的思路分析 - 阶乘后的零 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/factorial-trailing-zeroes/solution/xiang-xi-tong-su-de-si-lu-fen-xi-by-windliang-3/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
public int trailingZeroes(int n) {
    int count = 0;
    while (n > 0) {
        count += n / 5;
        n = n / 5;
    }
    return count;
}

}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(51. 数组中的逆序对)

https://leetcode-cn.com/problems/shu-zu-zhong-de-ni-xu-dui-lcof/

## 题目描述

```
在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。输入一个数组，求出这个数组中的逆序对的总数。

 

示例 1:

输入: [7,5,6,4]
输出: 5

 

限制：

0 <= 数组长度 <= 50000
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
    public int reversePairs(int[] nums) {
        //本质上就是
        if(nums==null&&nums.length==0) return 0;
        return Nixu(nums,0,nums.length-1);

    }
    
    public int Nixu(int[] nums,int left,int right){
        //注意返回的是逆序数，同时把left到right已经弄好序了
        //递归左边+递归右边+左边排好序之后的逆序数
        if(left>=right) return 0;
        int mid=left+(right-left)/2;
        int count=Nixu(nums,left,mid)+Nixu(nums,mid+1,right);
        int i=left,j=mid+1;//两个指针因为要排序，遍历结果
        int inx=0;
        int[] tem=new int[right-left+1];
        while(i<=mid&&j<=right){
            if(nums[i]>nums[j]) count+=(mid-i+1);
            tem[inx++]=(nums[i]<=nums[j])?nums[i++]:nums[j++];
        }
        while(i<=mid) tem[inx++]=nums[i++];
        while(j<=right) tem[inx++]=nums[j++];
        System.arraycopy(tem,0,nums,left,right-left+1);
        return count;
    }



}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(38. 字符串的排列)

https://leetcode-cn.com/problems/zi-fu-chuan-de-pai-lie-lcof/

## 题目描述

```
输入一个字符串，打印出该字符串中字符的所有排列。

 

你可以以任意顺序返回这个字符串数组，但里面不能有重复元素。

 

示例:

输入：s = "abc"
输出：["abc","acb","bac","bca","cab","cba"]


 

限制：

1 <= s 的长度 <= 8
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  可能会出现相同的 所以使用visted 
-  但是仅仅使用visted不行  因为aab 这种还会重复  所以需要使用set来存储每一次生成的结果去重

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public String[] permutation(String s) {
        //这就是全排列啊
        //使用StringBuilder进行分析替换

        Set<String> res = new HashSet<>();
        StringBuilder path=new StringBuilder();
        boolean[] visited=new boolean[s.length()];
        
        dfs(s,res,path,visited);
        String[] ans=res.toArray(new String[res.size()]);
        return ans;


    }
    public void dfs(String s,Set<String> res,StringBuilder path,boolean[] visited){
        if(path.length()==s.length()) {
            res.add(new String(path.toString()));
            return;
        }
        for(int i=0;i<s.length();i++){
            if(visited[i]==true) continue;
            path.append(s.charAt(i));
            visited[i]=true;//当前锁住了
            dfs(s,res,path,visited);
            visited[i]=false;
            path.deleteCharAt(path.length()-1);
        }
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(6. Z 字形变换)

https://leetcode-cn.com/problems/zigzag-conversion/

## 题目描述

```
将一个给定字符串 s 根据给定的行数 numRows ，以从上往下、从左到右进行 Z 字形排列。

比如输入字符串为 "PAYPALISHIRING" 行数为 3 时，排列如下：

P   A   H   N
A P L S I I G
Y   I   R

之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："PAHNAPLSIIGYIR"。

请你实现这个将字符串进行指定行数变换的函数：

string convert(string s, int numRows);

 

示例 1：

输入：s = "PAYPALISHIRING", numRows = 3
输出："PAHNAPLSIIGYIR"

示例 2：
输入：s = "PAYPALISHIRING", numRows = 4
输出："PINALSIGYAHRPI"
解释：
P     I    N
A   L S  I G
Y A   H R
P     I


示例 3：

输入：s = "A", numRows = 1
输出："A"


 

提示：

1 <= s.length <= 1000
s 由英文字母（小写和大写）、',' 和 '.' 组成
1 <= numRows <= 1000
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
    public String convert(String s, int numRows) {
        //直接做
        if(numRows<=1) return s;//需要进行有效地分析
        List<StringBuilder> res=new ArrayList<>();//注意这种只能是进行插入好不好，而不是其他的东西
        for(int i=0;i<numRows;i++){
            res.add(new StringBuilder());
        }
        int start=0;
        int flag=-1;
        for(char ss:s.toCharArray()){
            //注意是toCharArray好不好
            if(start==0||start==numRows-1){
                flag*=-1;
            }
            res.get(start).append(ss);
            start+=flag;

        }
        StringBuilder ans=res.get(0);
        for(int i=1;i<numRows;i++){
            ans.append(res.get(i));
        }

        return ans.toString();
        

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(692. 前K个高频单词)

https://leetcode-cn.com/problems/top-k-frequent-words/

## 题目描述

```
给一非空的单词列表，返回前 k 个出现次数最多的单词。

返回的答案应该按单词出现频率由高到低排序。如果不同的单词有相同出现频率，按字母顺序排序。

示例 1：

输入: ["i", "love", "leetcode", "i", "love", "coding"], k = 2
输出: ["i", "love"]
解析: "i" 和 "love" 为出现次数最多的两个单词，均为2次。
    注意，按字母顺序 "i" 在 "love" 之前。


 

示例 2：

输入: ["the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"], k = 4
输出: ["the", "is", "sunny", "day"]
解析: "the", "is", "sunny" 和 "day" 是出现次数最多的四个单词，
    出现次数依次为 4, 3, 2 和 1 次。


 

注意：

假定 k 总为有效值， 1 ≤ k ≤ 集合元素数。
输入的单词均由小写字母组成。

 

扩展练习：

尝试以 O(n log k) 时间复杂度和 O(n) 空间复杂度解决。
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
    public List<String> topKFrequent(String[] words, int k) {
        Map<String,Integer> map = new HashMap<>();
        for(String str: words){
            map.put(str, map.getOrDefault(str, 0) + 1);
        }
        PriorityQueue<String> heap = new PriorityQueue<>((o1, o2) -> map.get(o1) == map.get(o2) ? o2.compareTo(o1) : map.get(o1) - map.get(o2));
        //这个基本无敌，就是先看词频，按照小根堆来弄  词频相同按照字典序使用大根堆来弄
        for(String str: map.keySet()){
            heap.offer(str);
            if(heap.size() > k){
                heap.poll();
            }
        }
        List<String> res = new ArrayList<>();
        while(heap.size() > 0){
            res.add(heap.poll());
        }
        Collections.reverse(res);
        return res;
    }
}



class Solution {
    public List<String> topKFrequent(String[] words, int k) {
       HashMap<String,Integer> res=new HashMap<>();
       for(String s:words){
           res.put(s,res.getOrDefault(s,0)+1);
       }
       PriorityQueue<String> queue=new PriorityQueue<>(
           (e1,e2)->{
               if(res.get(e1)!=res.get(e2)) return res.get(e1)-res.get(e2);//小根堆
               else{
                   return e2.compareTo(e1);//大根堆
               }

           }
       );
       for(String s:res.keySet()){
           //注意是keyset好不好
           if(queue.size()<k) queue.add(s);
           else{
               if(res.get(s)>=res.get(queue.peek())){
                   queue.add(s);
                   queue.poll();
               }

           }
       }
       List<String> ans=new ArrayList<>();
       while(!queue.isEmpty()){
           ans.add(queue.poll());
       }
        Collections.reverse(ans);
        return ans;

    }
}
```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

```
class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        List<List<Integer>> res = new ArrayList<>();
        if(root != null) queue.add(root);
        while(!queue.isEmpty()) {
            List<Integer> tmp = new ArrayList<>();
            for(int i = queue.size(); i > 0; i--) {
                TreeNode node = queue.poll();
                tmp.add(node.val);
                if(node.left != null) queue.add(node.left);
                if(node.right != null) queue.add(node.right);
            }
            if(res.size() % 2 == 1) Collections.reverse(tmp);
            res.add(tmp);
        }
        return res;
    }
}

```



## 题目地址(678. 有效的括号字符串)

https://leetcode-cn.com/problems/valid-parenthesis-string/

## 题目描述

```
给定一个只包含三种字符的字符串：（ ，） 和 *，写一个函数来检验这个字符串是否为有效字符串。有效字符串具有如下规则：

任何左括号 ( 必须有相应的右括号 )。
任何右括号 ) 必须有相应的左括号 ( 。
左括号 ( 必须在对应的右括号之前 )。
* 可以被视为单个右括号 ) ，或单个左括号 ( ，或一个空字符串。
一个空字符串也被视为有效字符串。

示例 1:

输入: "()"
输出: True


示例 2:

输入: "(*)"
输出: True


示例 3:

输入: "(*))"
输出: True


注意:

字符串大小将在 [1，100] 范围内。
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
public boolean checkValidString(String s) {
    Stack<Integer> left = new Stack<>(), star = new Stack<>(); // 存储这个左括号与星号的位置索引
    for (int i = 0; i < s.length(); i++) { 
        //入栈的时候 分别分情况讨论
        
        char c = s.charAt(i);

        if (c == '(') left.push(i);
        else if (c == '*') star.push(i);
        //右边的括号进行分析，优先消减左括号（贪心思想）
        
        else {
            if (!left.isEmpty()) left.pop();
            else if (!star.isEmpty()) star.pop();
            else return false;//如果没有抵消的，那么直接进行返回就可以啦
        }
    }
    
    //说明只剩下左括号与星号*
    //需要进行分别出栈，这时候星号只能当做右括号
    //因为右括号需要满足 下标大于跳出来的左括号 一旦不满足就返回错
    while (!left.isEmpty() && !star.isEmpty()) {
        if (left.pop() > star.pop()) return false;
    }
    //最后就是两者有一个消耗完 主要看左括号是否为空
    return left.isEmpty();
}


}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(29. 顺时针打印矩阵)

https://leetcode-cn.com/problems/shun-shi-zhen-da-yin-ju-zhen-lcof/

## 题目描述

```
输入一个矩阵，按照从外向里以顺时针的顺序依次打印出每一个数字。

 

示例 1：

输入：matrix = [[1,2,3],[4,5,6],[7,8,9]]
输出：[1,2,3,6,9,8,7,4,5]


示例 2：

输入：matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
输出：[1,2,3,4,8,12,11,10,9,5,6,7]


 

限制：

0 <= matrix.length <= 100
0 <= matrix[i].length <= 100

注意：本题与主站 54 题相同：https://leetcode-cn.com/problems/spiral-matrix/
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
    public int[] spiralOrder(int[][] matrix) {
        //这个感觉直接打印就可以啦
        
        if(matrix==null||matrix.length<1) return new int[0];
        int m=matrix.length;
        int n=matrix[0].length;
        int left=0,right=n-1;
        int top=0,below=m-1;
        int[] res=new int[m*n];
        int inx=0;
        while(true){

            for(int i=left;i<=right;i++){
                res[inx++]=matrix[top][i];
                }
            if(++top>below) break;
            for(int j=top;j<=below;j++){
                res[inx++]=matrix[j][right];
                }
            if(--right<left) break;
            for(int t=right;t>=left;t--){
                res[inx++]=matrix[below][t];
                }
            if(--below<top) break;
            for(int s=below;s>=top;s--){
                res[inx++]=matrix[s][left];
                }
            if(++left>right) break;            

        }
        return res;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(46. 把数字翻译成字符串)

https://leetcode-cn.com/problems/ba-shu-zi-fan-yi-cheng-zi-fu-chuan-lcof/

## 题目描述

```
给定一个数字，我们按照如下规则把它翻译为字符串：0 翻译成 “a” ，1 翻译成 “b”，……，11 翻译成 “l”，……，25 翻译成 “z”。一个数字可能有多个翻译。请编程实现一个函数，用来计算一个数字有多少种不同的翻译方法。

 

示例 1:

输入: 12258
输出: 5
解释: 12258有5种不同的翻译，分别是"bccfi", "bwfi", "bczi", "mcfi"和"mzi"

 

提示：

0 <= num < 231
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
    public int translateNum(int num) {
        //递归方程没有什么问题
        //见热门题解的具体问题好不好
        String res=String.valueOf(num);
        int dp[]=new int[res.length()+1];
        dp[0]=1;
        dp[1]=1;
        for(int i=2;i<=res.length();i++){
            String tem=res.substring(i-2,i);//左闭右开 比如25 看25这个字符串 0 到2
            if(tem.compareTo("10")>=0&&tem.compareTo("25")<=0){
                dp[i]=dp[i-2]+dp[i-1];
            }
            else dp[i]=dp[i-1];
        }
        return dp[res.length()];

        

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(65. 不用加减乘除做加法)

https://leetcode-cn.com/problems/bu-yong-jia-jian-cheng-chu-zuo-jia-fa-lcof/

## 题目描述

```
写一个函数，求两个整数之和，要求在函数体内不得使用 “+”、“-”、“*”、“/” 四则运算符号。

 

示例:

输入: a = 1, b = 1
输出: 2

 

提示：

a, b 均可能是负数或 0
结果不会溢出 32 位整数
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [禁止套娃，如何用位运算完成加法？ - 不用加减乘除做加法 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/bu-yong-jia-jian-cheng-chu-zuo-jia-fa-lcof/solution/jin-zhi-tao-wa-ru-he-yong-wei-yun-suan-wan-cheng-j/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution{
    public int add(int a,int b){
        int presum;
        int carry;
        while(b!=0){
            //当进位真的没有了之后
            presum=a^b;//单纯和
            carry=(a&b)<<1;//进位
            a=presum;
            b=carry;
        }
        return a;

}
}


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(38. 字符串的排列)

https://leetcode-cn.com/problems/zi-fu-chuan-de-pai-lie-lcof/

## 题目描述

```
输入一个字符串，打印出该字符串中字符的所有排列。

 

你可以以任意顺序返回这个字符串数组，但里面不能有重复元素。

 

示例:

输入：s = "abc"
输出：["abc","acb","bac","bca","cab","cba"]


 

限制：

1 <= s 的长度 <= 8
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
    public String[] permutation(String s) {
        //这就是全排列啊
        //使用StringBuilder进行分析替换

        Set<String> res = new HashSet<>();
        StringBuilder path=new StringBuilder();
        boolean[] visited=new boolean[s.length()];
        
        dfs(s,res,path,visited);
        String[] ans=res.toArray(new String[res.size()]);
        return ans;


    }
    public void dfs(String s,Set<String> res,StringBuilder path,boolean[] visited){
        if(path.length()==s.length()) {
            res.add(new String(path.toString()));
            return;
        }
        for(int i=0;i<s.length();i++){
            if(visited[i]==true) continue;
            path.append(s.charAt(i));
            visited[i]=true;//当前锁住了
            dfs(s,res,path,visited);
            visited[i]=false;
            path.deleteCharAt(path.length()-1);
        }
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(701. 二叉搜索树中的插入操作)

https://leetcode-cn.com/problems/insert-into-a-binary-search-tree/

## 题目描述

```
给定二叉搜索树（BST）的根节点和要插入树中的值，将值插入二叉搜索树。 返回插入后二叉搜索树的根节点。 输入数据 保证 ，新值和原始二叉搜索树中的任意节点值都不同。

注意，可能存在多种有效的插入方式，只要树在插入后仍保持为二叉搜索树即可。 你可以返回 任意有效的结果 。

 

示例 1：

输入：root = [4,2,7,1,3], val = 5
输出：[4,2,7,1,3,5]
解释：另一个满足题目要求可以通过的树是：



示例 2：

输入：root = [40,20,60,10,30,50,70], val = 25
输出：[40,20,60,10,30,50,70,null,null,25]


示例 3：

输入：root = [4,2,7,1,3,null,null,null,null,null,null], val = 5
输出：[4,2,7,1,3,5]


 

 

提示：

给定的树上的节点数介于 0 和 10^4 之间
每个节点都有一个唯一整数值，取值范围从 0 到 10^8
-10^8 <= val <= 10^8
新值和原始二叉搜索树中的任意节点值都不同
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
    public TreeNode insertIntoBST(TreeNode root, int val) {
        //递归的写法
        //单个节点的话 要新建一个结果
        if(root==null) return new TreeNode(val);

        //这个意义就是右子树就是重新生成的节点
        if(root.val<val){
            root.right=insertIntoBST(root.right,val);
        }
        //这个意义就是右子树节点
        if(root.val>val){
            root.left=insertIntoBST(root.left,val);
        }
        return root;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(701. 二叉搜索树中的插入操作)

https://leetcode-cn.com/problems/insert-into-a-binary-search-tree/

## 题目描述

```
给定二叉搜索树（BST）的根节点和要插入树中的值，将值插入二叉搜索树。 返回插入后二叉搜索树的根节点。 输入数据 保证 ，新值和原始二叉搜索树中的任意节点值都不同。

注意，可能存在多种有效的插入方式，只要树在插入后仍保持为二叉搜索树即可。 你可以返回 任意有效的结果 。

 

示例 1：

输入：root = [4,2,7,1,3], val = 5
输出：[4,2,7,1,3,5]
解释：另一个满足题目要求可以通过的树是：



示例 2：

输入：root = [40,20,60,10,30,50,70], val = 25
输出：[40,20,60,10,30,50,70,null,null,25]


示例 3：

输入：root = [4,2,7,1,3,null,null,null,null,null,null], val = 5
输出：[4,2,7,1,3,5]


 

 

提示：

给定的树上的节点数介于 0 和 10^4 之间
每个节点都有一个唯一整数值，取值范围从 0 到 10^8
-10^8 <= val <= 10^8
新值和原始二叉搜索树中的任意节点值都不同
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
    public TreeNode insertIntoBST(TreeNode root, int val) {
       //使用迭代的方法试试看
       if(root==null) return new TreeNode(val);
       TreeNode node=new TreeNode(val);//新建一个节点，等着要连在当中
       //需要有一个点来进行遍历 往下走
       TreeNode cur=root;
       while(cur!=null){
           if(cur.val>val){
               if(cur.left==null){
                   cur.left=node;
                   break;
               }
               cur=cur.left;
           }
           else if(cur.val<val){
               if(cur.right==null){
                   cur.right=node;
                   break;
               }
               cur=cur.right;
           }
       }
       return root;


    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(67. 二进制求和)

https://leetcode-cn.com/problems/add-binary/

## 题目描述

```
给你两个二进制字符串，返回它们的和（用二进制表示）。

输入为 非空 字符串且只包含数字 1 和 0。

 

示例 1:

输入: a = "11", b = "1"
输出: "100"

示例 2:

输入: a = "1010", b = "1011"
输出: "10101"

 

提示：

每个字符串仅由字符 '0' 或 '1' 组成。
1 <= a.length, b.length <= 10^4
字符串如果不是 "0" ，就都不含前导零。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [画解算法：67. 二进制求和 - 二进制求和 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/add-binary/solution/hua-jie-suan-fa-67-er-jin-zhi-qiu-he-by-guanpengch/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public String addBinary(String a, String b) {
        //思路就是进位并且拼接字符串
        //使用stringbuilder
        //从后往前
        //进位就是对2取模 当前和就是对2取余
        int i=a.length()-1,j=b.length()-1;
        //需要以前弄一个进位变量carry
        //需要有一个部分和的变量
        int carry=0;
        int sumed=0;
        StringBuilder res=new StringBuilder();
        while(i>=0||j>=0){
            sumed+=(i>=0)?(a.charAt(i)-'0'):0;
            i--;
            sumed+=(j>=0)?(b.charAt(j)-'0'):0;
            j--;
            carry=sumed/2;//进位
            sumed=sumed%2;//当前和
            res.append(sumed);
            sumed=carry;//下一位的和
            
        }
        if(sumed!=0) res.append(sumed);
        return res.reverse().toString();

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(386. 字典序排数)

https://leetcode-cn.com/problems/lexicographical-numbers/

## 题目描述

```
给定一个整数 n, 返回从 1 到 n 的字典顺序。

例如，

给定 n =1 3，返回 [1,10,11,12,13,2,3,4,5,6,7,8,9] 。

请尽可能的优化算法的时间复杂度和空间复杂度。 输入的数据 n 小于等于 5,000,000。
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
    
    public List<Integer> lexicalOrder(int n) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i < 10; i++){
             dfs(n, i, list);
        }
        return list;
    }
    private void dfs(int n,int i,List<Integer>list){
        if(i>n){
            return ;
        }
        list.add(i);
        for(int j=0;j<=9;j++){
            dfs(n,i*10+j,list);
        }
    }

}


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(386. 字典序排数)

https://leetcode-cn.com/problems/lexicographical-numbers/

## 题目描述

```
给定一个整数 n, 返回从 1 到 n 的字典顺序。

例如，

给定 n =1 3，返回 [1,10,11,12,13,2,3,4,5,6,7,8,9] 。

请尽可能的优化算法的时间复杂度和空间复杂度。 输入的数据 n 小于等于 5,000,000。
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
    
    public List<Integer> lexicalOrder(int n) {
        List<Integer> list = new ArrayList<>();
        //其实本质上就是分配的过程嘛 先序遍历 从1开始

        for (int i = 1; i < 10; i++){
             dfs(n, i, list);//找寻从i开头的字典序排列好 然后就跳出来就好啦
        }
        return list;
    }
    private void dfs(int n,int i,List<Integer>list){
        if(i>n){
            return ;//提前判断
        }
        list.add(i);//加入其中
        //单独判断，其实这种应该加一个剪枝 不然都会往下走没有必要,提前判断 如果有之间全部拜拜
        for(int j=0;j<=9;j++){
            int tem=i*10+j;
            if(tem>n) break;
            dfs(n,i*10+j,list);
        }
    }

}


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(73. 矩阵置零)

https://leetcode-cn.com/problems/set-matrix-zeroes/

## 题目描述

```
给定一个 m x n 的矩阵，如果一个元素为 0 ，则将其所在行和列的所有元素都设为 0 。请使用 原地 算法。

进阶：

一个直观的解决方案是使用  O(mn) 的额外空间，但这并不是一个好的解决方案。
一个简单的改进方案是使用 O(m + n) 的额外空间，但这仍然不是最好的解决方案。
你能想出一个仅使用常量空间的解决方案吗？

 

示例 1：

输入：matrix = [[1,1,1],[1,0,1],[1,1,1]]
输出：[[1,0,1],[0,0,0],[1,0,1]]


示例 2：

输入：matrix = [[0,1,2,0],[3,4,5,2],[1,3,1,5]]
输出：[[0,0,0,0],[0,4,5,0],[0,3,1,0]]


 

提示：

m == matrix.length
n == matrix[0].length
1 <= m, n <= 200
-231 <= matrix[i][j] <= 231 - 1
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
    public void setZeroes(int[][] matrix) {
        Set<Integer> row_zero = new HashSet<>();
        Set<Integer> col_zero = new HashSet<>();
        int row = matrix.length;
        int col = matrix[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] == 0) {
                    row_zero.add(i);
                    col_zero.add(j);
                }
            }
        }
        //再遍历 只要有一个沾边那么都应该置零
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (row_zero.contains(i) || col_zero.contains(j)) matrix[i][j] = 0;
            }
        }  
    }
}


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(80. 删除有序数组中的重复项 II)

https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array-ii/

## 题目描述

```
给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使每个元素 最多出现两次 ，返回删除后数组的新长度。

不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。

 

说明：

为什么返回数值是整数，但输出的答案是数组呢？

请注意，输入数组是以「引用」方式传递的，这意味着在函数里修改输入数组对于调用者是可见的。

你可以想象内部操作如下:

// nums 是以“引用”方式传递的。也就是说，不对实参做任何拷贝
int len = removeDuplicates(nums);

// 在函数里修改输入数组对于调用者是可见的。
// 根据你的函数返回的长度, 它会打印出数组中 该长度范围内 的所有元素。
for (int i = 0; i < len; i++) {
    print(nums[i]);
}


 

示例 1：

输入：nums = [1,1,1,2,2,3]
输出：5, nums = [1,1,2,2,3]
解释：函数应返回新长度 length = 5, 并且原数组的前五个元素被修改为 1, 1, 2, 2, 3 。 不需要考虑数组中超出新长度后面的元素。


示例 2：

输入：nums = [0,0,1,1,1,1,2,3,3]
输出：7, nums = [0,0,1,1,2,3,3]
解释：函数应返回新长度 length = 7, 并且原数组的前五个元素被修改为 0, 0, 1, 1, 2, 3, 3 。 不需要考虑数组中超出新长度后面的元素。


 

提示：

1 <= nums.length <= 3 * 104
-104 <= nums[i] <= 104
nums 已按升序排列
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [【负雪明烛】动画题解，帮助理清思路 - 删除有序数组中的重复项 II - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array-ii/solution/fu-xue-ming-zhu-dong-hua-ti-jie-bang-zhu-yrx5/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int removeDuplicates(int[] nums) {
        //其实就是这样，快慢指针问题
        //slow 存储的是当前要存的的位置
        //fast就负责遍历 
        //如果nums[fast]!=nums[slow-2] 那么 
        //前两个数据直接进行遍历就可以
        int slow=0;
        for(int fast=0;fast<nums.length;fast++){
            if(slow<2||nums[fast]!=nums[slow-2]){
                nums[slow]=nums[fast]; 
                slow+=1;
            }
        }
        return slow;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(97. 交错字符串)

https://leetcode-cn.com/problems/interleaving-string/

## 题目描述

```
给定三个字符串 s1、s2、s3，请你帮忙验证 s3 是否是由 s1 和 s2 交错 组成的。

两个字符串 s 和 t 交错 的定义与过程如下，其中每个字符串都会被分割成若干 非空 子字符串：

s = s1 + s2 + ... + sn
t = t1 + t2 + ... + tm
|n - m| <= 1
交错 是 s1 + t1 + s2 + t2 + s3 + t3 + ... 或者 t1 + s1 + t2 + s2 + t3 + s3 + ...

提示：a + b 意味着字符串 a 和 b 连接。

 

示例 1：

输入：s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
输出：true


示例 2：

输入：s1 = "aabcc", s2 = "dbbca", s3 = "aadbbbaccc"
输出：false


示例 3：

输入：s1 = "", s2 = "", s3 = ""
输出：true


 

提示：

0 <= s1.length, s2.length <= 100
0 <= s3.length <= 200
s1、s2、和 s3 都由小写英文字母组成
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [类似路径问题，找准状态方程快速求解 - 交错字符串 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/interleaving-string/solution/lei-si-lu-jing-wen-ti-zhao-zhun-zhuang-tai-fang-ch/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public boolean isInterleave(String s1, String s2, String s3) {
        int m = s1.length(), n = s2.length();
        if (s3.length() != m + n) return false;
        // 动态规划，dp[i,j]表示s1前i字符能与s2前j字符组成s3前i+j个字符；
        boolean[][] dp = new boolean[m+1][n+1];
        dp[0][0] = true;
        for (int i = 1; i <= m && s1.charAt(i-1) == s3.charAt(i-1); i++) dp[i][0] = true; // 不相符直接终止
        for (int j = 1; j <= n && s2.charAt(j-1) == s3.charAt(j-1); j++) dp[0][j] = true; // 不相符直接终止
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = (dp[i - 1][j] && s3.charAt(i + j - 1) == s1.charAt(i - 1))
                    || (dp[i][j - 1] && s3.charAt(i + j - 1) == s2.charAt(j - 1));
            }
        }
        return dp[m][n];
    }
}



```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(97. 交错字符串)

https://leetcode-cn.com/problems/interleaving-string/

## 题目描述

```
给定三个字符串 s1、s2、s3，请你帮忙验证 s3 是否是由 s1 和 s2 交错 组成的。

两个字符串 s 和 t 交错 的定义与过程如下，其中每个字符串都会被分割成若干 非空 子字符串：

s = s1 + s2 + ... + sn
t = t1 + t2 + ... + tm
|n - m| <= 1
交错 是 s1 + t1 + s2 + t2 + s3 + t3 + ... 或者 t1 + s1 + t2 + s2 + t3 + s3 + ...

提示：a + b 意味着字符串 a 和 b 连接。

 

示例 1：

输入：s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
输出：true


示例 2：

输入：s1 = "aabcc", s2 = "dbbca", s3 = "aadbbbaccc"
输出：false


示例 3：

输入：s1 = "", s2 = "", s3 = ""
输出：true


 

提示：

0 <= s1.length, s2.length <= 100
0 <= s3.length <= 200
s1、s2、和 s3 都由小写英文字母组成
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
    public boolean isInterleave(String s1, String s2, String s3) {
        //dp[i][j] 代表s1的前i个字母和s2的前j个字母可以组成s3的前i+j个字母
        int m=s1.length();
        int n=s2.length();
        if(m+n!=s3.length()) return false;
        boolean[][] dp=new boolean[m+1][n+1];
        dp[0][0]=true;
        //初始化第一行

        //注意横着的是这个s2 
        for(int j=1;j<=n;j++){
            if(dp[0][j-1]==true&&s2.charAt(j-1)==s3.charAt(j-1)){
                dp[0][j]=true;
            }
            else dp[0][j]=false;
        }

        //初始化第一列

        //竖着的是s3
        for(int i=1;i<=m;i++){
            if(dp[i-1][0]==true&&s1.charAt(i-1)==s3.charAt(i-1)){
                dp[i][0]=true;
            }
            else dp[i][0]=false;
        }
        for(int i=1;i<=m;i++){
            for(int j=1;j<=n;j++){

                dp[i][j]=(dp[i-1][j]==true&&s1.charAt(i-1)==s3.charAt(i+j-1))||(dp[i][j-1]==true&&s2.charAt(j-1)==s3.charAt(i+j-1));
                
            }
        }
        return dp[m][n];

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(222. 完全二叉树的节点个数)

https://leetcode-cn.com/problems/count-complete-tree-nodes/

## 题目描述

```
给你一棵 完全二叉树 的根节点 root ，求出该树的节点个数。

完全二叉树 的定义如下：在完全二叉树中，除了最底层节点可能没填满外，其余每层节点数都达到最大值，并且最下面一层的节点都集中在该层最左边的若干位置。若最底层为第 h 层，则该层包含 1~ 2h 个节点。

 

示例 1：

输入：root = [1,2,3,4,5,6]
输出：6


示例 2：

输入：root = []
输出：0


示例 3：

输入：root = [1]
输出：1


 

提示：

树中节点的数目范围是[0, 5 * 104]
0 <= Node.val <= 5 * 104
题目数据保证输入的树是 完全二叉树

 

进阶：遍历树来统计节点是一种时间复杂度为 O(n) 的简单解决方案。你可以设计一个更快的算法吗？
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
    private int count=0;
    public int countNodes(TreeNode root) {
        //层次遍历最小就可以
        if(root==null) return 0;
        Queue<TreeNode> queue=new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            for(int i=0;i<queue.size();i++){
                TreeNode node=queue.poll();
                count++;
                if(node.left!=null) queue.add(node.left);
                if(node.right!=null) queue.add(node.right);
            }
        }
        return count;

    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(503. 下一个更大元素 II)

https://leetcode-cn.com/problems/next-greater-element-ii/

## 题目描述

```
给定一个循环数组（最后一个元素的下一个元素是数组的第一个元素），输出每个元素的下一个更大元素。数字 x 的下一个更大的元素是按数组遍历顺序，这个数字之后的第一个比它更大的数，这意味着你应该循环地搜索它的下一个更大的数。如果不存在，则输出 -1。

示例 1:

输入: [1,2,1]
输出: [2,-1,2]
解释: 第一个 1 的下一个更大的数是 2；
数字 2 找不到下一个更大的数； 
第二个 1 的下一个最大的数需要循环搜索，结果也是 2。


注意: 输入数组的长度不会超过 10000。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  https://leetcode-cn.com/problems/count-complete-tree-nodes/solution/javadai-ma-tu-wen-xiang-jie-ji-bai-liao-100de-yong/

## 代码

- 语言支持：Java

Java Code:

```java

 class Solution{
    public int countNodes(TreeNode root) {
        //计算树的高度，
        int height = treeHeight(root);
        //如果树是空的，或者高度是1，直接返回
        if (height == 0 || height == 1)
            return height;
        //如果右子树的高度是树的高度减1，说明左子树是满二叉树，
        //左子树可以通过公式计算，只需要递归右子树就行了
        if (treeHeight(root.right) == height - 1) {
            //注意这里的计算，左子树的数量是实际上是(1 << (height - 1))-1，
            //不要把根节点给忘了，在加上1就是(1 << (height - 1))
            return (1 << (height - 1)) + countNodes(root.right);
        } else {
            //如果右子树的高度不是树的高度减1，说明右子树是满二叉树，可以通过
            //公式计算右子树，只需要递归左子树就行了
            return (1 << (height - 2)) + countNodes(root.left);
        }
    }

    //计算树的高度
    private int treeHeight(TreeNode root) {
        return root == null ? 0 : 1 + treeHeight(root.left);
    }
 }


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(260. 只出现一次的数字 III)

https://leetcode-cn.com/problems/single-number-iii/

## 题目描述

```
给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。 找出只出现一次的那两个元素。你可以按 任意顺序 返回答案。

 

进阶：你的算法应该具有线性时间复杂度。你能否仅使用常数空间复杂度来实现？

 

示例 1：

输入：nums = [1,2,1,3,2,5]
输出：[3,5]
解释：[5, 3] 也是有效的答案。


示例 2：

输入：nums = [-1,0]
输出：[-1,0]


示例 3：

输入：nums = [0,1]
输出：[1,0]


提示：

2 <= nums.length <= 3 * 104
-231 <= nums[i] <= 231 - 1
除两个只出现一次的整数外，nums 中的其他数字都出现两次
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  https://leetcode-cn.com/problems/single-number-iii/solution/cai-yong-fen-zhi-de-si-xiang-jiang-wen-ti-jiang-we/

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int[] singleNumber(int[] nums) {
        int ans=nums[0];
        for(int i=1;i<nums.length;i++){
            ans^=nums[i];
        }
        //求得ans就是这个两个数的异或值
        //需要分出来  找到两个数的不同的位  该位的异或值为0
        //选择ans&(-ans)就是最低的不同值 只有这一位为1
        //分组就可以

        int mask=ans&(-ans);
        int[] res=new int[2];
        for(int num:nums){
            if((num&mask)==0){
                res[0]^=num;

            }
            else{
                res[1]^=num;
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

## 题目地址(264. 丑数 II)

https://leetcode-cn.com/problems/ugly-number-ii/

## 题目描述

```
给你一个整数 n ，请你找出并返回第 n 个 丑数 。

丑数 就是只包含质因数 2、3 和/或 5 的正整数。

 

示例 1：

输入：n = 10
输出：12
解释：[1, 2, 3, 4, 5, 6, 8, 9, 10, 12] 是由前 10 个丑数组成的序列。


示例 2：

输入：n = 1
输出：1
解释：1 通常被视为丑数。


 

提示：

1 <= n <= 1690
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
    int[] nums = new int[]{2,3,5};
    public int nthUglyNumber(int n) {
        Set<Long> set = new HashSet<>();
        Queue<Long> pq = new PriorityQueue<>();
        set.add(1L);
        pq.add(1L);
        for (int i = 1; i <= n; i++) {
            long x = pq.poll();//一定要用小的下标 因为集合里边采用的是包装类
            if (i == n) return (int)x;
            for (int num : nums) {
                long t = num * x;
                if (!set.contains(t)) {
                    set.add(t);
                    pq.add(t);
                }
            }
        }
        return -1;
    }
}



```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(429. N 叉树的层序遍历)

https://leetcode-cn.com/problems/n-ary-tree-level-order-traversal/

## 题目描述

```
给定一个 N 叉树，返回其节点值的层序遍历。（即从左到右，逐层遍历）。

树的序列化输入是用层序遍历，每组子节点都由 null 值分隔（参见示例）。

 

示例 1：

输入：root = [1,null,3,2,4,null,5,6]
输出：[[1],[3,2,4],[5,6]]


示例 2：

输入：root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
输出：[[1],[2,3,4,5],[6,7,8,9,10],[11,12,13],[14]]


 

提示：

树的高度不会超过 1000
树的节点总数在 [0, 10^4] 之间
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

/*
// Definition for a Node.
class Node {
    public int val;
    public List<Node> children;

    public Node() {}

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, List<Node> _children) {
        val = _val;
        children = _children;
    }
};
*/
class Solution{
public List<List<Integer>> levelOrder(Node root) {
    List<List<Integer>> res = new ArrayList<>();
    if (root == null) return res;
    Queue<Node> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
        int count = queue.size();
        //外层循环为一层
        List<Integer> list = new ArrayList<>();
        while (count-- > 0) {
            //将当前元素的非空子节点压入栈
            Node cur = queue.poll();
            list.add(cur.val);
            //这个遍历就完事儿了，比较简单
            for (Node node : cur.children) {
                if (node != null) {
                    queue.add(node);
                }
            }
        }
        res.add(list);
    }
    return res;
}

}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(767. 重构字符串)

https://leetcode-cn.com/problems/reorganize-string/

## 题目描述

```
给定一个字符串S，检查是否能重新排布其中的字母，使得两相邻的字符不同。

若可行，输出任意可行的结果。若不可行，返回空字符串。

示例 1:

输入: S = "aab"
输出: "aba"


示例 2:

输入: S = "aaab"
输出: ""


注意:

S 只包含小写字母并且长度在[1, 500]区间内。
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [java代码，击败了100%的用户 - 重构字符串 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/reorganize-string/solution/javadai-ma-ji-bai-liao-100de-yong-hu-by-sdwwld/)

## 代码

- 语言支持：Java

Java Code:

```java

    class Solution{
    public String reorganizeString(String S) {
        //把字符串S转化为字符数组
        char[] alphabetArr = S.toCharArray();
        //记录每个字符出现的次数
        int[] alphabetCount = new int[26];
        //字符串的长度
        int length = S.length();
        //统计每个字符出现的次数
        for (int i = 0; i < length; i++) {
            alphabetCount[alphabetArr[i] - 'a']++;
        }
        int max = 0, alphabet = 0, threshold = (length + 1) >> 1;
        //找出出现次数最多的那个字符
        for (int i = 0; i < alphabetCount.length; i++) {
            if (alphabetCount[i] > max) {
                max = alphabetCount[i];
                alphabet = i;
                //如果出现次数最多的那个字符的数量大于阈值，说明他不能使得
                // 两相邻的字符不同，直接返回空字符串即可
                if (max > threshold)
                    return "";
            }
        }
        //到这一步说明他可以使得两相邻的字符不同，我们随便返回一个结果，res就是返回
        //结果的数组形式，最后会再转化为字符串的
        char[] res = new char[length];
        int index = 0;
        //先把出现次数最多的字符存储在数组下标为偶数的位置上
        while (alphabetCount[alphabet]-- > 0) {
            res[index] = (char) (alphabet + 'a');
            index += 2;
        }
        //然后再把剩下的字符存储在其他位置上
        for (int i = 0; i < alphabetCount.length; i++) {
            while (alphabetCount[i]-- > 0) {
                if (index >= res.length) {
                    index = 1;
                }
                res[index] = (char) (i + 'a');
                index += 2;
            }
        }
        return new String(res);
    }
    }

    //放置 S 中出现次数 最多的字符 c_max 新建一个字符串 res_str 放置对 S 重构后的结果, string res_str = S ; res_str的大小和 S 相等。将S 中 所有的字符 c_max 从 0 开始排在字符串res_str 的偶数位上, c_max 可能占不满 res_str 的所有偶数位。

//放置 S 中除c_max 的其他字符 其他字符继续放在 c_max 后面的偶数位上(c_max 没有占满 res_str 的所有偶数位), 偶数位用完之后， 在从 1 开始的奇数位上继续放其他字符。


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(445. 两数相加 II)

https://leetcode-cn.com/problems/add-two-numbers-ii/

## 题目描述

```
给你两个 非空 链表来代表两个非负整数。数字最高位位于链表开始位置。它们的每个节点只存储一位数字。将这两数相加会返回一个新的链表。

你可以假设除了数字 0 之外，这两个数字都不会以零开头。

 

进阶：

如果输入链表不能修改该如何处理？换句话说，你不能对列表中的节点进行翻转。

 

示例：

输入：(7 -> 2 -> 4 -> 3) + (5 -> 6 -> 4)
输出：7 -> 8 -> 0 -> 7

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
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
 class Solution{
public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    //目的是制造一个链表，因为加法是从低位来进行，并且进位再进行求和什么的
    //但是原始链表是高位在前边，所以需要进行放进一个栈里边，这样取出来的时候就是低位
    //这样才是从后往前来做
    Stack<Integer> stack1=new Stack<>();
    Stack<Integer> stack2=new Stack<>();    
    //将节点值放进去
    while(l1!=null){
        stack1.push(l1.val);
        l1=l1.next;
    }
    while(l2!=null){
        stack2.push(l2.val);
        l2=l2.next;
    }
    //下边就是进行加法运算，每一次都会得到一个x,y以及下一个的进位add以及产生另外的一个进位
    //求出当前位的和，那么就需要进行生成一个节点，插到当前节点的前边
    //产生节点的条件是  x或者y不为0 以及或者进位不为0
    
    ListNode head=null;//作为被代替的旧的节点
    int add=0;//进位
    //这个就是一个头插法 用新生成的节点来代替新的头节点，因为是先算低位然后再算高位
    while(!stack1.isEmpty()||!stack2.isEmpty()){
        int x=(stack1.isEmpty()==false)?stack1.pop():0;
        int y=(stack2.isEmpty()==false)?stack2.pop():0;
        int sum=x+y+add;
        add=sum/10;
        ListNode newnode=new ListNode(sum%10);
        newnode.next=head;
        head=newnode;

    }
    if(add!=0){
        ListNode newnode=new ListNode(add);
        newnode.next=head;
        head=newnode;

    }
    return head;
}
 }


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(480. 滑动窗口中位数)

https://leetcode-cn.com/problems/sliding-window-median/

## 题目描述

```
中位数是有序序列最中间的那个数。如果序列的长度是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。

例如：

[2,3,4]，中位数是 3
[2,3]，中位数是 (2 + 3) / 2 = 2.5

给你一个数组 nums，有一个长度为 k 的窗口从最左端滑动到最右端。窗口中有 k 个数，每次窗口向右移动 1 位。你的任务是找出每次窗口移动后得到的新窗口中元素的中位数，并输出由它们组成的数组。

 

示例：

给出 nums = [1,3,-1,-3,5,3,6,7]，以及 k = 3。

窗口位置                      中位数
---------------               -----
[1  3  -1] -3  5  3  6  7       1
 1 [3  -1  -3] 5  3  6  7      -1
 1  3 [-1  -3  5] 3  6  7      -1
 1  3  -1 [-3  5  3] 6  7       3
 1  3  -1  -3 [5  3  6] 7       5
 1  3  -1  -3  5 [3  6  7]      6


 因此，返回该滑动窗口的中位数数组 [1,-1,-1,3,5,6]。

 

提示：

你可以假设 k 始终有效，即：k 始终小于等于输入的非空数组的元素个数。
与真实值误差在 10 ^ -5 以内的答案将被视作正确答案。
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
    public double[] medianSlidingWindow(int[] nums, int k) {
        double[] res = new double[nums.length - k + 1];//最终结果的个数 注意一定是double类型的哦
        int[] window = new int[k];//具体的窗口 注意是int[]类型的
        //添加初始值


        //初始化窗口的值
        for (int i = 0; i < k; i++) {
            window[i] = nums[i];
        }
        //初始的快排，懒得写直接调用，就是一个排序
        Arrays.sort(window);
        res[0] = getMid(window);//找寻中值
        //窗口滑动
        for (int i = 0; i < nums.length - k; i++) {
            //需要删除的数
            //旧的数nums[i] 出去
            int index = search(window, nums[i]);//二分查找这个
            //替换为需要插入的数 进行替换
            window[index] = nums[i + k];//新的数nums[i+k]出去
            //向后冒泡

            //因为刚插入所以 只有一个不满足条件 所以应该冒泡排序
            while (index < window.length - 1 && window[index] > window[index + 1]) {
                swap(window, index, index + 1);
                index++;
            }
            //向前冒泡 两种情况之一
            while (index > 0 && window[index] < window[index - 1]) {
                swap(window, index, index - 1);
                index--;
            }
            res[i + 1] = getMid(window);
        }
        return res;
    }

    //交换
    private void swap(int[] window, int i, int j) {
        int temp = window[i];
        window[i] = window[j];
        window[j] = temp;
    }

    //求数组的中位数
    //注意都是double类型的结果
    //所以需要分情况讨论
    private double getMid(int[] window) {
        int len = window.length;
        if (window.length % 2 == 0) {
            //避免溢出，每一个数都先除以2.0 这样就转化成double了
            return window[len / 2] / 2.0 + window[len / 2 - 1] / 2.0;
        } else {
             //这样算强转了吧 因为是double类型的
            return window[len / 2];
        }
    }

    //最简单的二分查找
    //返回的是下标
    private int search(int[] window, int target) {
        int start = 0;
        int end = window.length - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (window[mid] > target) {
                end = mid - 1;
            } else if (window[mid] < target) {
                start = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
}


```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(18. 四数之和)

https://leetcode-cn.com/problems/4sum/

## 题目描述

```
给定一个包含 n 个整数的数组 nums 和一个目标值 target，判断 nums 中是否存在四个元素 a，b，c 和 d ，使得 a + b + c + d 的值与 target 相等？找出所有满足条件且不重复的四元组。

注意：答案中不可以包含重复的四元组。

 

示例 1：

输入：nums = [1,0,-1,0,-2,2], target = 0
输出：[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]


示例 2：

输入：nums = [], target = 0
输出：[]


 

提示：

0 <= nums.length <= 200
-109 <= nums[i] <= 109
-109 <= target <= 109
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
    public List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        int len = nums.length;
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        int c;//后边两个数双指针
        int d;//后边两个数双指针
        for(int i = 0; i <= len - 4; i++){ 

            //前边是线性从i=0 后边流出3个数
            if(i > 0 && nums[i] == nums[i-1]){
                continue;
            }
            //第二个就是从i+1开始 后边流出2个数
            for(int j = i + 1; j <= len - 3; j++){
                if(j > i + 1 && nums[j] == nums[j-1]){
                    continue;
                }
                //双指针比较
                c = j + 1;
                d = len - 1;
                while(c < d){
                    if(c > j + 1 && nums[c] == nums[c-1]){
                        c++;
                        continue;
                    }
                    if(nums[i]+nums[j]+nums[c]+nums[d] > target){
                        d--;
                    }else if(nums[i]+nums[j]+nums[c]+nums[d] < target){
                        c++;
                    }else{
                        res.add(new ArrayList<Integer>(Arrays.asList(nums[i],nums[j],nums[c],nums[d])));
                        c++;
                        d--;
                    }
                }
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

## 题目地址(516. 最长回文子序列)

https://leetcode-cn.com/problems/longest-palindromic-subsequence/

## 题目描述

```
给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。

子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。

 

示例 1：

输入：s = "bbbab"
输出：4
解释：一个可能的最长回文子序列为 "bbbb" 。


示例 2：

输入：s = "cbbd"
输出：2
解释：一个可能的最长回文子序列为 "bb" 。


 

提示：

1 <= s.length <= 1000
s 仅由小写英文字母组成
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [动态规划，四要素 - 最长回文子序列 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/longest-palindromic-subsequence/solution/dong-tai-gui-hua-si-yao-su-by-a380922457-3/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int longestPalindromeSubseq(String s) {
        /*
            bb aa bb
            bb a  bb
            dp[i][j] 表示 第 i 个字符到 第 j 个字符之间最长的回文子序列长度
            1、当 s[i] == s[j] 时，考虑 i 和 j 中间序列的奇偶个数， dp[i][j] = dp[i+1][j-1] + 2
            对上述 dp[i][j] =  dp[i+1][j-1] + 2 的解释：
            当序列为 b aa b 时， i = 0, j = 3，则 dp[0][3] = dp[1][2] + 2 = 4
            当序列为 b a b 时，i = 0, j = 2，则 dp[0][2] = dp[1][1] + 2 = 3 
            当序列为 b b 时， i = 0, j = 1，则 dp[0][1] = dp[1][0] = 0 + 2 = 2 (dp[1][0] 默认值为 0，因为还没有赋值)
            该式子同时考虑到了奇偶
            2、当 s[i] != s[j] ，那么 dp[i][j] = Math.max(dp[i+1][j],dp[i][j-1])
            对上述 dp[i][j] 式子的解释：
            假如序列为 d c b c c（index：0-4），s[0] != s[4] ，则 dp[0][4] = Math.max(dp[0][3],dp[1,4]) = Math.max(2,3) = 3

            注意：上述按我习惯分析是将 i 放在了 j 的前面，而按我写法习惯这里是将 i 放在了 j 的后面，即上面的 dp[i][j] 在这里应该是 dp[j][i]
            两层 for 循环，令 i 从 0 遍历到 len-1，而 j 为 i-1 递减到 0
            假如 i = 5，那么 j 的顺序为 4 3 2 1 0，在得到 dp[0][5] 过程中，dp[1][5]等值 就已经提前准备好了，有预先值
            一个字符单独作为一个回文子序列，即 dp[i][i] = 1
        */



        int len = s.length();
        int[][] dp = new int[len][len];
        //dp[i][j] 是下标为i到下标为j之间的最长子序列
       
        for(int i = 0; i < len; i++){
            dp[i][i] = 1;
            //首先声明这一点
            //注意情况就是因为递推都是  dp[j][i] = dp[j+1][i-1] + 2; i是从小递推到大所以应该顺着循环
            //j 是从大递推到小 所以应该是逆着循环 就是从 len-1到i+1
            for(int j = i - 1; j >= 0; j--){
                if(s.charAt(i) == s.charAt(j)){
                    dp[j][i] = dp[j+1][i-1] + 2;
                }else{
                    dp[j][i] = Math.max(dp[j+1][i],dp[j][i-1]);
                }
            }
        }
        return dp[0][len-1];
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(117. 填充每个节点的下一个右侧节点指针 II)

https://leetcode-cn.com/problems/populating-next-right-pointers-in-each-node-ii/

## 题目描述

```
给定一个二叉树

struct Node {
  int val;
  Node *left;
  Node *right;
  Node *next;
}

填充它的每个 next 指针，让这个指针指向其下一个右侧节点。如果找不到下一个右侧节点，则将 next 指针设置为 NULL。

初始状态下，所有 next 指针都被设置为 NULL。

 

进阶：

你只能使用常量级额外空间。
使用递归解题也符合要求，本题中递归程序占用的栈空间不算做额外的空间复杂度。

 

示例：

输入：root = [1,2,3,4,5,null,7]
输出：[1,#,2,3,#,4,5,7,#]
解释：给定二叉树如图 A 所示，你的函数应该填充它的每个 next 指针，以指向其下一个右侧节点，如图 B 所示。序列化输出按层序遍历顺序（由 next 指针连接），'#' 表示每层的末尾。

 

提示：

树中的节点数小于 6000
-100 <= node.val <= 100

 
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [BFS解决（最好的击败了100%的用户） - 填充每个节点的下一个右侧节点指针 II - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/populating-next-right-pointers-in-each-node-ii/solution/bfsjie-jue-zui-hao-de-ji-bai-liao-100de-yong-hu-by/)

## 代码

- 语言支持：Java

Java Code:

```java

/*
// Definition for a Node.
class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {}
    
    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
};
*/

class Solution {
    public Node connect(Node root) {
        //本质上还是bfs好不好，无非是每一行遍历的时候 增加一个pre然后遍历每一行连起来就可以
        if(root==null) return root;
        Queue<Node> queue=new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            Node pre=null;
            //一定注意 这里queue在变化的 所以需要弄一个定的值
            int n=queue.size();
            for(int i=0;i<n;i++){
                Node node=queue.poll();
                if(pre!=null) pre.next=node;
                pre=node;
                if(node.left!=null) queue.add(node.left);
                if(node.right!=null) queue.add(node.right);

            }
        }
        return root;

        
    }
}

```


**复杂度分析**

令 n 为数组长度。

- 时间复杂度：$O(n)$
- 空间复杂度：$O(n)$

## 题目地址(977. 有序数组的平方)

https://leetcode-cn.com/problems/squares-of-a-sorted-array/

## 题目描述

```
给你一个按 非递减顺序 排序的整数数组 nums，返回 每个数字的平方 组成的新数组，要求也按 非递减顺序 排序。

 

示例 1：

输入：nums = [-4,-1,0,3,10]
输出：[0,1,9,16,100]
解释：平方后，数组变为 [16,1,0,9,100]
排序后，数组变为 [0,1,9,16,100]

示例 2：

输入：nums = [-7,-3,2,3,11]
输出：[4,9,9,49,121]


 

提示：

1 <= nums.length <= 104
-104 <= nums[i] <= 104
nums 已按 非递减顺序 排序

 

进阶：

请你设计时间复杂度为 O(n) 的算法解决本问题
```

## 前置知识

- 

## 公司

- 暂无

## 思路

## 关键点

-  [「代码随想录」977. 有序数组的平方:【排序】【双指针】详解 - 有序数组的平方 - 力扣（LeetCode） (leetcode-cn.com)](https://leetcode-cn.com/problems/squares-of-a-sorted-array/solution/977-you-xu-shu-zu-de-ping-fang-pai-xu-shuang-zhi-z/)

## 代码

- 语言支持：Java

Java Code:

```java

class Solution {
    public int[] sortedSquares(int[] nums) {
        //双指针 最大值一定是在两边 然后用双指针代替就可以
        int left=0,right=nums.length-1;//比较当前平方该选谁
        int k=nums.length-1;//结果从后往前来走
        int[] res=new int[nums.length];
        while(left<=right){
            //注意这里要使用等号，因为每一个数都需要计算好不好
            if(nums[left]*nums[left]>=nums[right]*nums[right]){
                res[k--]=nums[left]*nums[left];
                left++;
            }
            else{
                res[k--]=nums[right]*nums[right];
                right--;

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
