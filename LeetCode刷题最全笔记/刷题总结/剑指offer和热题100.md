---

title: LeetCode刷题总结（半年）
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2020-09-24 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/19.jpg
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


### 448 找到消失的数字

给定一个范围在  1 ≤ a[i] ≤ n ( n = 数组大小 ) 的 整型数组，数组中的元素一些出现了两次，另一些只出现一次。找到所有在 [1, n] 范围之间没有出现在数组中的数字。您能在不使用额外空间且时间复杂度为O(n)的情况下完成这个任务吗? 你可以假定返回的数组不算在额外空间内。





## 题目描述

给定一个范围在 1 ≤ a[i] ≤ n ( n = 数组大小 ) 的 整型数组，数组中的元素一些出现了两次，另一些只出现一次。
找到所有在 [1, n] 范围之间没有出现在数组中的数字。
您能在不使用额外空间且时间复杂度为O(n)的情况下完成这个任务吗? 你可以假定返回的数组不算在额外空间内。

示例:

> 输入:
> `[4,3,2,7,8,2,3,1]`
> 输出:
> `[5,6]`

## 题解

注意题目要求不能使用额外空间，这就是题目的难点所在。
这道题的描述部分包含了一个非常重要的信息，`1 ≤ a[i] ≤ n`，即每个数字本身都对应一个`i-1`的数组下标。我们可以利用数组内容本身跟数字下标的关联找出缺失的数字。

扫描两遍数组：

- 第一遍，将所有数字做标记
- 第二遍，根据标记信息找出缺失的数字。



下面来看详细分析
假设有数组`[1,2,3,4,5,6]`
这个数组是有序的，而且也没有缺失数字，范围是`[1,6]`
仔细看，数组中的每个元素，其实和数组下标是有一一对应关系的

![img](https://mmbiz.qpic.cn/mmbiz_jpg/smWnh5qQwsVyjhWxfAnPHBXQZyhqpXNC8GuafQfHqjgB7QRkDxrzFjVxxSyuXnpQpJibBnsQJbJIho3I5hZ9icwA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

这里的对应关系就是：

- 数组值1对应下标0
- 数组值2对应下标1
- 数组值3对应下标2
- 数组值4对应下标3
- 数组值5对应下标4
- 数组值6对应下标5

也就是`数组下标+1`正好**等于** `数组中的值`



如果是一个乱序的数组会怎样呢？
假设数组是`[5,4,6,3,1,2]`，范围是`[1,6]`，也没有缺失数字

![img](https://mmbiz.qpic.cn/mmbiz_jpg/smWnh5qQwsVyjhWxfAnPHBXQZyhqpXNCJxOy0CwnMR7rAFYxrrK2UxrOFic3ibvY355nk3QAmqogicye4FkjehSPw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

这里仍然有一一对应关系：

- 数组值5对应下标4
- 数组值4对应下标3
- 数组值6对应下标5
- 数组值3对应下标2
- 数组值1对应下标0
- 数组值2对应下标1

没有缺失数字的情况下，不管是有序的、还是乱序的，都跟下标有一一对应关系。



现在我们来分析一个缺失数字的例子
假设有数组`[1,2,3,4,6,6]`缺少数字`5`

![img](https://mmbiz.qpic.cn/mmbiz_jpg/smWnh5qQwsVyjhWxfAnPHBXQZyhqpXNC4ZVLxw5IPtvT8w3iaDP1oHVBcqwIr8MtRbUM3KxxGYWRhvwk8tPIuew/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

我们用**值**和**下标**对应的这么一层关系，将数组重写一遍

- 第一个值是1，对应下标是0，将arr[0]设置为-arr[0]，即-1
- 第二个值是2，对应下标是1，将arr[1]设置为-arr[1]，即-2
- 第三个值是3，对应下标是2，将arr[2]设置为-arr[2]，即-3
- 第四个值是4，对应下标是3，将arr[3]设置为-arr[3]，即-4
- 第五个值是6，对应下标是5，`将arr[5]设置为-arr[5]，即-6`
- 第六个值是6，对应下标是5，`将arr[5]设置为-arr[5]，即-6`

第五个、第六个值相同，他们修改的是同一个下标，都将arr[5]改了一次
但是arr[4]这个位置没动过
重写了一遍数组之后，数组就变成了这个样子：

![img](https://mmbiz.qpic.cn/mmbiz_jpg/smWnh5qQwsVyjhWxfAnPHBXQZyhqpXNCOBL30xnfYEwZTT4dBOLT4TLfZib9nw8SBUoBdCbsD1sgM4n6XmujNYg/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

由于`下标4`应该对应数字`5`，现在缺少了这个值，所以没人设置这个位置，于是第一遍处理完后，只有下标`4`这个位置的值是**正数**，其他位置的全部都是负数。
这就好办了，我们遍历一遍数组，找到**大于**0的数，这个数是`6`，对应下标是`4`，所以缺失的数字是`5`



最后再看一个更复杂的例子
数组`[4,3,2,7,8,2,3,1]`，缺少5,6两个数字

![img](https://mmbiz.qpic.cn/mmbiz_jpg/smWnh5qQwsVyjhWxfAnPHBXQZyhqpXNC1IgKYlzRmBAr2y9H5AwQibjtEAhzElRCDp4ibamUBlKR11BZ6aeUveBg/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

我们来看下第一趟的处理过程：

- 第一个数字是4，对应下标3，将arr[3]设置为-7
- 第二个数字是3，对应下标2，将arr[2]设置为-2
- 第三个数字是2，对应下标1，将arr[1]设置为-3
- 第四个数字是7，对应下标6，将arr[6]设置为-3
- 第五个数字是8，对应下标7，将arr[7]设置为-1
- 第六个数字是2，对应下标1，将arr[1]设置为-3
- 第七个数字是3，对应下标2，将arr[2]设置为-2
- 第八个数字是1，对应下标0，将arr[0]设置为-4

第一趟处理完了之后，我们开始第二趟扫描，也就是上图中第二个数组
这个数组中8,2两个元素是大于0的
8对应下标4，所以4+1，即缺少`5`这个数字
2对应下标5，所以5+1，即缺少`6`这个数字

时间复杂度：O(N)
空间复杂度：O(1)



java代码：

```
class Solution {
    public List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> res = new ArrayList<Integer>();
        //第一遍扫描，根据数组的值找到对应的下标，比如3对应下标2
        //将arr[2]设置成负数
        for(int i=0;i<nums.length;++i) {
            int index = Math.abs(nums[i])-1;
            if(nums[index]>0) {
                nums[index] *= -1;
            }
        }
        //第二遍扫描，找到所有非负数，非负数所在的下标+1，即为缺失的数字
        for(int i=1;i<=nums.length;++i) {
            if(nums[i-1]>0) {
                res.add(i);
            }
        }
        return res;
    }
}
```

python代码：

```python
class Solution(object):
    def findDisappearedNumbers(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        res = []
        # 第一遍扫描，根据数组的值找到对应的下标，比如3对应下标2
        # 将arr[2]设置成负数
        for i in nums:
            index = abs(i)-1
            if nums[index]>0:
                nums[index] *= -1
        # 第二遍扫描，找到所有非负数，非负数所在的下标+1，即为缺失的数字
        for i in xrange(len(nums)):
            if nums[i]>0:
                res.append(i+1)
        return res
```

## 自己的思路



#### 思路一(超时间限制)：

每一个数据都看是否在数字内部，复杂度$o(n^2)$

```python 
class Solution:
    def findDisappearedNumbers(self, nums: List[int]) -> List[int]:
        if len(nums)==0:
            return []
        res=[]
        for i in range(1,len(nums)+1):
            if i not in nums:
                res.append(i)
        return res

```



#### 哈希构造

分析

根据题目特点，可以把数组中的元素与索引建立一一对应的关系。因为索引是确定的0到n-1,一个也不缺，而数组的元素不确定，少了哪个也不知道。
既然两者是一一对应的关系，那么我们对数组中的每个元素对应的索引做个标记；
然后再对索引进行一次遍历，那么不存的元素就不会对它对应的索引进行比较，由此可查找出这些不存在的元素。
思路

遍历每个元素，对索引进行标记
将对应索引位置的值变为负数；
遍历下索引，看看哪些索引位置上的数不是负数的。
位置上不是负数的索引，对应的元素就是不存在的。

对数组内部的数据作为下标进行映射，把映射到的数据变成负数，没有变成负数的数据的下标就是消失的数字

（为什么会用这个abs(num),因为数组当中数字可能变成负数，所以需要使用abs)

```python
class Solution:
    def findDisappearedNumbers(self, nums: List[int]) -> List[int]:
        for num in nums:
            nums[abs(num)-1]=-abs(nums[abs(num)-1])
        return[i+1 for i,num in enumerate(nums) if num>0]
```

```java
class Solution {
    public List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> res=new ArrayList<>();
        for(int num:nums){
            nums[Math.abs(num)-1]=-Math.abs(nums[Math.abs(num)-1]);

        }
        for(int j=0;j<nums.length;j++){
            if (nums[j]>0){
                res.add(j+1);
            }
        }
        return res;

    }
}
```



### 160 相交列表

编写一个程序，找到两个单链表相交的起始节点。

如下面的两个链表**：**

[![img](https://assets.leetcode-cn.com/aliyun-lc-upload/uploads/2018/12/14/160_statement.png)](https://assets.leetcode-cn.com/aliyun-lc-upload/uploads/2018/12/14/160_statement.png)

在节点 c1 开始相交。

 

**示例 1：**

[![img](https://assets.leetcode-cn.com/aliyun-lc-upload/uploads/2018/12/14/160_example_1.png)](https://assets.leetcode.com/uploads/2018/12/13/160_example_1.png)



#### 链表进行拼接就可以，重点在于什么时候停下的问题

```python
根据题目意思
如果两个链表相交，那么相交点之后的长度是相同的

我们需要做的事情是，让两个链表从同距离末尾同等距离的位置开始遍历。这个位置只能是较短链表的头结点位置。
为此，我们必须消除两个链表的长度差

指针 pA 指向 A 链表，指针 pB 指向 B 链表，依次往后遍历
如果 pA 到了末尾，则 pA = headB 继续遍历
如果 pB 到了末尾，则 pB = headA 继续遍历
比较长的链表指针指向较短链表head时，长度差就消除了
如此，只需要将最短链表遍历两次即可找到位置

class Solution:
    def getIntersectionNode(self, headA: ListNode, headB: ListNode) -> ListNode:
        slow,fast=headA,headB
        while slow!=fast:
            if slow:
                slow=slow.next
            else:slow=headB
            if fast:
                fast=fast.next
            else:fast=headA
        return slow
```

##### 解析为什么是slow==None才进行转换成headB 而不是slow.next ==None的时候才转换

答案：为了避免死循环，第一种是A和B都多走了一个结尾None，相对于不走结尾None,如果两个链表没有相交的时候，就会死循环，因为走了A+B长度后当前的值不一样，如果是None 走的话，就会强行停止，返回None





### 字符串数字相加



##### 双指针来求解

```python
class Solution:
    def addStrings(self, num1: str, num2: str) -> str:
        res=""
        carry=0
        i,j=len(num1)-1,len(num2)-1
        while i>=0 or j>=0:
            res1=int(num1[i]) if i>=0 else 0
            res2=int(num2[j]) if j>=0 else 0
            temp=(res1+res2+carry)%10 #余数
            res=str(temp)+res #当前位结果
            carry=(res1+res2+carry)//10 #进位
            i=i-1 #都进行具体移动位置
            j=j-1
        return "1"+res if carry==1 else res
```



#### java版本

```java
Stringbuilder 是一个多功能的字符串，可以进行append添加

class Solution {
    public String addStrings(String num1, String num2) {
        StringBuilder res=new StringBuilder();
        int i=num1.length()-1;
        int j=num2.length()-1;
        int carry=0;
        while(i>=0||j>=0){
            int res1=i>=0?num1.charAt(i)-'0':0;
            int res2=j>=0?num2.charAt(j)-'0':0;
            int temp=res1+res2+carry;
            res.append(temp%10);
            carry=temp/10;
            j--;
            i--;
        }
        
        //return carry==1?("1"+res).reverse().toString():res.reverse().toString();
        if(carry==1){
            res=res.append(1);
        }
        return res.reverse().toString();
 




    }
}
```



### 二叉树的直径

> **LeetCode 543 - Diameter of Binary Tree**[1]（Easy）
>
> 给定一棵二叉树，计算它的直径。二叉树的直径是任意两个结点之间的路径长度中的最大值。这条路径有可能不经过根结点。

==核心：遍历每一个节点，以每一个节点为中心点计算最长路径（左子树边长+右子树边长），更新全局变量maxed==

- 直径的定义是边长，所以如果过原点那么就是左边高度加上右边高度即可

- 因为最长路径不一定会经过这个根节点，所以对于每一个节点来讲都需要进行判断，左子树深度+右子树深度与当前最大值maxed的关系

- 最后返回的是maxed

```java
class Solution {
    int maxvalue=0;
    public int diameterOfBinaryTree(TreeNode root) {
        maxzhijing(root);
        return maxvalue;


    }
    public int maxzhijing(TreeNode root){
        if(root==null){
            return 0;
        }
        int l=maxzhijing(root.left);
        int r=maxzhijing(root.right);
        maxvalue=Math.max(maxvalue,l+r);
        return Math.max(l,r)+1;
    }
}
```



```python
class Solution:
    def diameterOfBinaryTree(self, root: TreeNode) -> int:
        if not root:
            return 0
        self.maxed=0
        self.depth(root)
        return self.maxed
    def depth(self,root):
        #求左右深度的同时更新最大值
        if not root:
            return 0
        l=self.depth(root.left)
        r=self.depth(root.right)
        self.maxed=max(self.maxed,l+r)
        return max(l,r)+1
```


#### 另外的一个思路



二叉树的直径这道题是一道非常经典的面试题。我曾经在面试遇到过原题，也听周围参加面试的小伙伴提起过好几次。同时，这道题也是一道非常有代表性的题目，可以用来理解一类带有全局变量的二叉树遍历。本文就来详细讲解这个题目中的道理。

这篇文章将会包含：

- 二叉树直径问题的子问题解法
- 二叉树直径问题的全局变量解法
- 一类全局变量问题的规律
- 相关题目

## 二叉树直径问题的思路

我们在第二讲中讲过了二叉树的子问题划分（[点击这里回顾第二讲内容](https://mp.weixin.qq.com/s?__biz=MzA5ODk3ODA4OQ==&mid=2648167032&idx=1&sn=5734e539c8b037faf649df21dce4578d&scene=21#wechat_redirect)）。二叉树的解题技巧是，首先判断问题能否划分为子问题、应当划分为什么样的子问题。对于二叉树直径（最长路径）问题，需要明确的一点是，二叉树中的最长路径不一定经过根结点：

![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcib94c7slK05n1FxqfNDicY0dYR5qTaUkKerek77XMrWJdWC6hicA4autuW11FaTC98TC5ib3qoVTKm0xQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)二叉树中的最长路径不一定经过根结点

这给我们的子问题划分带来了一点难度。但是稍加思考，还是可以划分出子问题的：

![image-20201113153730625](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20201113153730625.png)

二叉树的最长路径左子树的最长路径右子树的最长路径经过根结点的最长路径

![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcib94c7slK05n1FxqfNDicY0dYzUBicBQ1oiaDVYpeib4nhbFXNmlxXuuZnC6MerM0VSRzv3iaOo9FgM6Iicg/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)子问题之左右子树的最长路径

其中左子树的最长路径和右子树的最长路径是两个可以递归求解的子问题，那么经过根结点的最长路径如何计算呢？是左子树的深度加上右子树的深度。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcib94c7slK05n1FxqfNDicY0dYagtJFl1GhpFicm7gHrLCqwZGJJtQicpW07OjaGCh05PoTPBvib5cC7icRw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)根据左右子树的深度计算出最长路径

代入上面的式子得到：

二叉树的最长路径左子树的最长路径右子树的最长路径左子树的深度右子树的深度

等等。这里好像出现了两个子问题：**子树的最大直径**、**子树的最大深度**。这难道是要让我们把树遍历两遍吗？非也，我们只需要让遍历函数返回两个值即可。

```python
# return (depth, diameter)
def traverse(root):
    if root isNone:
        return (0, 0)

    left_depth, left_diam = traverse(root.left)
    right_depth, right_diam = traverse(root.right)
    # 求二叉树深度的常规方法
    depth = 1 + max(left_depth, right_depth)
    # 套用上面推导出的最长路径公式
    diam = max(left_diam, right_diam, left_depth + right_depth)
    return depth, diam

def diameterOfBinaryTree(root):
    depth, diam = traverse(root)
    return diam
```



### [124. 二叉树中的最大路径和](https://leetcode-cn.com/problems/binary-tree-maximum-path-sum/)

#### 正常的思路

- 首先最大路径和那就是

- 递归根节点左子树最大路径和  递归根节点右子树最大路径和 经过根节点的最大路径和   三者选最大

- 可以用一个框架来描述，遍历每一个节点的时候，求出经过这个节点的最大值，然后保存最大值

- 注意每一个节点最大值就是当前节点值（一定要有）+经过左节点最大路径和（左节点的左子树和右子树选一个）+经过右节点的最大路径和

  

```python
int max = Integer.MIN_VALUE;

public int maxPathSum(TreeNode root) {
    helper(root);
    return max;
} 
int helper(TreeNode root) {
    if (root == null) return 0;

    int left = Math.max(helper(root.left), 0);
    int right = Math.max(helper(root.right), 0);
    
    //求的过程中考虑包含当前根节点的最大路径
    max = Math.max(max, root.val + left + right);
    
    //只返回包含当前根节点和左子树或者右子树的路径
    return root.val + Math.max(left, right);#注意这个说明只能选一端
}


```



```python
class Solution:
    def maxPathSum(self, root: TreeNode) -> int:
        self.maxed=0
        self.dfs(root)
        return self.maxed   
    def dfs(self,root):
        if not root:
            return 
        right=max(0,self.dfs(root.right))
        left=max(0,self.dfs(root.left))
        self.maxed=max(self.maxed,left+right+root.val)
        return root.val+max(left,right)
```



### 对称二叉树

![image-20201116165122954](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20201116165122954.png)

## 递归实现

乍一看无从下手，但用递归其实很好解决。
根据题目的描述，**镜像对称**，就是左右两边相等，也就是左子树和右子树是相当的。
注意这句话，左子树和右子相等，也就是说要递归的比较**左子树**和**右子树**。
我们将根节点的左子树记做left，右子树记做right。比较left是否等于right，不等的话直接返回就可以了。
如果相当，比较left的左节点和right的右节点，再比较left的右节点和right的左节点
比如看下面这两个子树(他们分别是根节点的左子树和右子树)，能观察到这么一个规律：
左子树2的左孩子 == 右子树2的右孩子
左子树2的右孩子 == 右子树2的左孩子

```
    2         2
   / \       / \
  3   4     4   3
 / \ / \   / \ / \
8  7 6  5 5  6 7  8
```

根据上面信息可以总结出递归函数的两个条件：

1. 终止条件：left和right不等，或者left和right都为空
2. 递归的比较left.left和right.right，递归比较left.right和right.left

动态图如下：

![img](https://mmbiz.qpic.cn/mmbiz_gif/smWnh5qQwsUV0bvg78Ys6LgIUy4URiaHsgIjbSFPoydnPD2PMTKD1AicCAV6mwKesZico8p95M6po00rtLIUXNWnQ/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)

算法的时间复杂度是O(n)，因为要遍历n个节点
空间复杂度是O(n)，空间复杂度是递归的深度，也就是跟树高度有关，最坏情况下树变成一个链表结构，高度是n。

```python
class Solution:
    def isSymmetric(self, root: TreeNode) -> bool:
        #能够直接判断的分成三种情况 
        #当前要比较的节点都为空 返回True
        #当前要比较的一个为空 一个不为空 返回False
        #当前要比较的不为空，但是值不同，返回False
        #不为空，但是值相同，需要递归比较子树好不好
        if not root:
            return True 
        return self.dfs(root.left,root.right)
    def dfs(self,left,right):
        if not left and not right:
            return True 
        if (not left and right) or (not right and left):
            return False 
        if left.val!=right.val:
            return False 
        return self.dfs(left.left,right.right) and self.dfs(left.right,right.left)
```

```java
class Solution {
    public boolean isSymmetric(TreeNode root) {
        if (root==null){
            return true;
        }
        return dfs(root.left,root.right);
        
    }
    public boolean dfs(TreeNode left,TreeNode right){
        if(left==null&&right==null){
            return true;
        }
        if((left==null&&right!=null)||(left!=null&&right==null)){
            return false;
        }
        if(left.val!=right.val){
            return false;
        }
        return dfs(left.left,right.right)&&dfs(left.right,right.left);
    }
}
```

## 队列实现

回想下递归的实现：
当两个子树的根节点相等时，就比较:
左子树的left 和 右子树的right，这个比较是用递归实现的。
现在我们改用队列来实现，思路如下：

1. 首先从队列中拿出两个节点(left和right)比较
2. 将left的left节点和right的right节点放入队列
3. 将left的right节点和right的left节点放入队列

时间复杂度是O(n)，空间复杂度是O(n)
动画演示如下：

![img](https://mmbiz.qpic.cn/mmbiz_gif/smWnh5qQwsUV0bvg78Ys6LgIUy4URiaHsLAvg4k6DIldebuw81oibarzxCdVvibjPnnHRStJOxAVFb7ibAsGG1mVfg/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)

```PYTHON
class Solution(object):
	def isSymmetric(self, root):
		"""
		:type root: TreeNode
		:rtype: bool
		"""
		ifnot root ornot (root.left or root.right):
			return True
		# 用队列保存节点	
		queue = [root.left,root.right]
		while queue:
			# 从队列中取出两个节点，再比较这两个节点
			left = queue.pop(0)
			right = queue.pop(0)
			# 如果两个节点都为空就继续循环，两者有一个为空就返回false
			ifnot (left or right):
				continue
			ifnot (left and right):
				returnFalse
			if left.val!=right.val:
				returnFalse
			# 将左节点的左孩子， 右节点的右孩子放入队列
			queue.append(left.left)
			queue.append(right.right)
			# 将左节点的右孩子，右节点的左孩子放入队列
			queue.append(left.right)
			queue.append(right.left)
		return True
```

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
    public boolean isSymmetric(TreeNode root) {
        if (root==null){
            return true;
        }
        LinkedList<TreeNode> res=new LinkedList<>();
        res.add(root.left);
        res.add(root.right);
        while(res.size()>0){
            TreeNode node1=res.removeFirst();
            TreeNode node2=res.removeFirst();
            if(node1==null&&node2==null){
                return true;
            }
            if((node1==null&&node2!=null)||(node1!=null&&node2==null)){
                return false;
            }
            if(node1.val!=node2.val){
                return false;
            }
            res.add(node1.left);
            res.add(node2.right);
            res.add(node1.right);
            res.add(node2.left);
        }
        return true;
    }
    
}
```



### 翻转二叉树

题目地址：https://leetcode-cn.com/problems/invert-binary-tree/

翻转一棵二叉树。
示例：
输入：

```
     4
   /   \
  2     7
 / \   / \
1   3 6   9
```

输出：

```
     4
   /   \
  7     2
 / \   / \
9   6 3   1
```

备注:

> 这个问题是受到 Max Howell 的 原问题 启发的 ：
> 谷歌：我们90％的工程师使用您编写的软件(Homebrew)，但是您却无法在面试时在白板上写出翻转二叉树这道题，这太糟糕了。

## 递归

我们在做二叉树题目时候，第一想到的应该是用**递归**来解决。
仔细看下题目的**输入**和**输出**，输出的左右子树的位置跟输入正好是相反的，于是我们可以递归的交换左右子树来完成这道题。
看一下动画就明白了：



![img](https://mmbiz.qpic.cn/mmbiz_gif/smWnh5qQwsXKibSSmaHCDJx16rib8jNwichXbnSSiarF2A5NVnP4cG9Na9Zbhziaibd0X3ZtzCEIibZZt4AZqoFhxiahdg/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)


其实就是交换一下左右节点，然后再递归的交换左节点，右节点
根据动画图我们可以总结出递归的两个条件如下：

1. 终止条件：当前节点为null时返回

2. 交换当前节点的左右节点，再递归的交换当前节点的左节点，递归的交换当前节点的右节点

   

时间复杂度：每个元素都必须访问一次，所以是O(n)

空间复杂度：最坏的情况下，需要存放O(h)个函数调用(h是树的高度)，所以是O(h)

代码实现如下：
java实现:

```java
class Solution {
    public TreeNode invertTree(TreeNode root) {
        return dfs(root);

    }
    public TreeNode dfs(TreeNode root){
        if(root==null){
            return null;
        }
        TreeNode temp= root.left;
        root.left= root.right;
        root.right= temp;
        dfs(root.left);
        dfs(root.right);
        return root;
    }
}
```



```java
class Solution {
	public TreeNode invertTree(TreeNode root) {
		//递归函数的终止条件，节点为空时返回
		if(root==null) {
			returnnull;
		}
		//下面三句是将当前节点的左右子树交换
		TreeNode tmp = root.right;
		root.right = root.left;
		root.left = tmp;
		//递归交换当前节点的 左子树
		invertTree(root.left);
		//递归交换当前节点的 右子树
		invertTree(root.right);
		//函数返回时就表示当前这个节点，以及它的左右子树
		//都已经交换完了
		return root;
	}
}
```

python实现:

```python
class Solution(object):
	def invertTree(self, root):
		"""
		:type root: TreeNode
		:rtype: TreeNode
		"""
		# 递归函数的终止条件，节点为空时返回
		ifnot root:
			returnNone
		# 将当前节点的左右子树交换
		root.left,root.right = root.right,root.left
		# 递归交换当前节点的 左子树和右子树
		self.invertTree(root.left)
		self.invertTree(root.right)
		# 函数返回时就表示当前这个节点，以及它的左右子树
		# 都已经交换完了		
		return root
```

## 迭代

递归实现也就是深度优先遍历的方式，那么对应的就是广度优先遍历。
广度优先遍历需要额外的数据结构--队列，来存放临时遍历到的元素。
深度优先遍历的特点是一竿子插到底，不行了再退回来继续；而广度优先遍历的特点是层层扫荡。
所以，我们需要先将根节点放入到队列中，然后不断的迭代队列中的元素。  
对当前元素调换其左右子树的位置，然后：

- 判断其左子树是否为空，不为空就放入队列中
- 判断其右子树是否为空，不为空就放入队列中

动态图如下：  

![img](https://mmbiz.qpic.cn/mmbiz_gif/smWnh5qQwsXKibSSmaHCDJx16rib8jNwichCOzC9AN9lNOLfhzXdthTUYZFfcgPibLRuibXX1BAiaQCjmQ6eNAknTiaHQ/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)

深度优先遍历和广度优先遍历，从动画图中看起来很类似，这是因为演示的树层数只有三层。  

时间复杂度：同样每个节点都需要入队列/出队列一次，所以是O(n)

空间复杂度：最坏的情况下会包含所有的叶子节点，完全二叉树叶子节点是n/2个，所以时间复杂度是0(n)

代码实现如下：
java实现:

```java
class Solution {
    public TreeNode invertTree(TreeNode root) {
        if(root ==null){
            return null;
        }
        LinkedList<TreeNode> res=new LinkedList<>();
        res.add(root);
        while(res.size()>0){
            TreeNode node=res.removeFirst();
            TreeNode temp=node.left;
            node.left=node.right;
            node.right=temp;
            if (node.left!=null)
                res.add(node.left);
            if (node.right!=null)
                res.add(node.right);
        }
        return root;

    }

}
```



```
class Solution {
	public TreeNode invertTree(TreeNode root) {
		if(root==null) {
			returnnull;
		}
		/将二叉树中的节点逐层放入队列中，再迭代处理队列中的元素
		LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
		queue.add(root);
		while(!queue.isEmpty()) {
			//每次都从队列中拿一个节点，并交换这个节点的左右子树
			TreeNode tmp = queue.poll();
			TreeNode left = tmp.left;
			tmp.left = tmp.right;
			tmp.right = left;
			//如果当前节点的左子树不为空，则放入队列等待后续处理
			if(tmp.left!=null) {
				queue.add(tmp.left);
			}
			//如果当前节点的右子树不为空，则放入队列等待后续处理
			if(tmp.right!=null) {
				queue.add(tmp.right);
			}
			
		}
		//返回处理完的根节点
		return root;
	}
}
```

python实现:

```
class Solution(object):
	def invertTree(self, root):
		"""
		:type root: TreeNode
		:rtype: TreeNode
		"""
		ifnot root:
			returnNone
		# 将二叉树中的节点逐层放入队列中，再迭代处理队列中的元素
		queue = [root]
		while queue:
			# 每次都从队列中拿一个节点，并交换这个节点的左右子树
			tmp = queue.pop(0)
			tmp.left,tmp.right = tmp.right,tmp.left
			# 如果当前节点的左子树不为空，则放入队列等待后续处理
			if tmp.left:
				queue.append(tmp.left)
			# 如果当前节点的右子树不为空，则放入队列等待后续处理	
			if tmp.right:
				queue.append(tmp.right)
		# 返回处理完的根节点
		return root
```

(全文完)

### 合并二叉树



## 题目描述

给定两个二叉树，想象当你将它们中的一个覆盖到另一个上时，两个二叉树的一些节点便会重叠。
你需要将他们合并为一个新的二叉树。合并的规则是如果两个节点重叠，那么将他们的值相加作为节点合并后的新值，否则不为 NULL   的节点将直接作为新二叉树的节点。
示例 1:
输入:

```
	Tree 1                     Tree 2
          1                         2
         / \                       / \
        3   2                     1   3
       /                           \   \
      5                             4   7
```

输出:
合并后的树:

```
	     3
	    / \
	   4   5
	  / \   \
	 5   4   7
```

注意: 合并必须从两个树的根节点开始。

题目地址：
https://leetcode-cn.com/problems/merge-two-binary-trees/

## 递归实现

如果没有头绪的话，可以将这两颗树想象成是两个数组：

```
1 3 2 5
2 1 3 4 7
```

合并两个数组很直观，将数组2的值合并到数组1中，再返回数组1就可以了。
对于二叉树来说，如果我们像遍历数组那样，挨个遍历两颗二叉树中的每个节点，再把他们相加，那问题就比较容易解决了。

遍历二叉树很简单，用**前序**遍历就可以了，再依次把访问到的节点值相加，因为题目说的是一棵树覆盖另一棵树，所以我们不用再创建新的节点了，直接将树2合并到树1上再返回就可以了。
需要注意：这两颗树并不是长得完全一样，有的树可能有左节点，但有的树没有。对于这种情况，我们统一的都把他们挂到树1 上面就可以了，对于上面例子中的两颗树，合并起来的结果如下：

```
	     3
	    / \
	   4   5
	  / \   \
	 5   4   7
```

相当于树1少了一条腿，而树2有这条腿，那就把树2的拷贝过来。
总结下递归的条件：

1. 终止条件：树1的节点为null，或者树2的节点为null
2. 递归函数内：将两个树的节点相加后，再赋给树1的节点。再递归的执行两个树的左节点，递归执行两个树的右节点

动画演示如下：

![img](https://mmbiz.qpic.cn/mmbiz_gif/smWnh5qQwsVzhqFic3icnJc0fFnjHiaRamLUiaPO2hMuR286G6p2R6CU3taicVKuRUcuK8rzPenwcV3ibkATI3VtScYA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)



时间复杂度：O(N)
空间复杂度：O(h)，h是树的高度
java代码实现：

```
class Solution {
	public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
		return dfs(t1,t2);
	}
	
	TreeNode dfs(TreeNode r1, TreeNode r2) {
		// 如果 r1和r2中，只要有一个是null，函数就直接返回
		if(r1==null || r2==null) {
			return r1==null? r2 : r1;
		}
		//让r1的值 等于  r1和r2的值累加，再递归的计算两颗树的左节点、右节点
		r1.val += r2.val;
		r1.left = dfs(r1.left,r2.left);
		r1.right = dfs(r1.right,r2.right);
		return r1;
	}
}
```

python代码实现：

```
class Solution(object):
	def mergeTrees(self, t1, t2):
		"""
		:type t1: TreeNode
		:type t2: TreeNode
		:rtype: TreeNode
		"""
		def dfs(r1,r2):
			# 如果 r1和r2中，只要有一个是null，函数就直接返回
			ifnot (r1 and r2):
				return r1 if r1 else r2
			# 让r1的值 等于  r1和r2的值累加
			# 再递归的计算两颗树的左节点、右节点
			r1.val += r2.val
			r1.left = dfs(r1.left,r2.left)
			r1.right = dfs(r1.right,r2.right)
			return r1
		return dfs(t1,t2)
```

## 迭代实现

迭代实现用的是广度优先算法，广度优先就需要额外的数据结构来辅助了，我们可以借助栈或者队列来完成。
只要两颗树的左节点都不为null，就把将他们放入队列中；同理只要两棵树的右节点都不为null了，也将他们放入队列中。
然后我们不断的从队列中取出节点，把他们相加。
如果出现  树1的左节点为null，树2的左不为null，直接将树2的左节点赋给树1就可以了；同理如果树1的右节点为null，树2的右节点不为null，将树2的右节点赋给树1。
动画演示如下：

![img](https://mmbiz.qpic.cn/mmbiz_gif/smWnh5qQwsVzhqFic3icnJc0fFnjHiaRamLgicrDMlL8lJ6oJJy9GxibqTv56yzxODhlkFOJrEqEfuSEcyGdFV22LPg/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)



时间复杂度:O(N)
空间复杂度:O(N)，对于满二叉树时，要保存所有的叶子节点，即N/2个节点。

java代码实现：

```
class Solution {
	public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
	        //如果 t1和t2中，只要有一个是null，函数就直接返回
		if(t1==null || t2==null) {
			return t1==null? t2 : t1;
		}
		java.util.LinkedList<TreeNode> queue = new java.util.LinkedList<TreeNode>();
		queue.add(t1);
		queue.add(t2);
		while(queue.size()>0) {
			TreeNode r1 = queue.remove();
			TreeNode r2 = queue.remove();
			r1.val += r2.val;
			//如果r1和r2的左子树都不为空，就放到队列中
			//如果r1的左子树为空，就把r2的左子树挂到r1的左子树上
			if(r1.left!=null && r2.left!=null){
				queue.add(r1.left);
				queue.add(r2.left);
			}
			elseif(r1.left==null) {
				r1.left = r2.left;
			}
			//对于右子树也是一样的
			if(r1.right!=null && r2.right!=null) {
				queue.add(r1.right);
				queue.add(r2.right);
			}
			elseif(r1.right==null) {
				r1.right = r2.right;
			}
		}
		return t1;
	}
}
```

python代码实现：

```
class Solution(object):
	def mergeTrees(self, t1, t2):
		"""
		:type t1: TreeNode
		:type t2: TreeNode
		:rtype: TreeNode
		"""
	        # 如果 t1和t2中，只要有一个是null，函数就直接返回
		ifnot (t1 and t2):
			return t2 ifnot t1 else t1
		queue = [(t1,t2)]
		while queue:
			r1,r2 = queue.pop(0)
			r1.val += r2.val
			# 如果r1和r2的左子树都不为空，就放到队列中
			# 如果r1的左子树为空，就把r2的左子树挂到r1的左子树上
			if r1.left and r2.left:
				queue.append((r1.left,r2.left))
			elifnot r1.left:
				r1.left = r2.left
			# 对于右子树也是一样的
			if r1.right and r2.right:
				queue.append((r1.right,r2.right))
			elifnot r1.right:
				r1.right = r2.right
		return t1
```

(全文完)



### 是否是翻转链表

```java
class Solution {
    public boolean isPalindrome(ListNode head) {
        if((head==null)||(head!=null && head.next==null)){
            return true;
        }
        ArrayList<Integer> res=new ArrayList<>();
        while(head!=null){
            res.add(head.val);
            head=head.next;
        }
        int i=0;
        int j=res.size()-1;//使用的是动态链表
        while(i<j){
            if(res.get(i).compareTo(res.get(j))!=0){//类型是integer 获取用get 比较用compareTo 返回+1 -1 0
                return false;
            }
            i++;
            j--;
        }    
        return true; 
        

    }
}
```









### 使用最基本的方法来翻转链表再来比较

```python
class Solution:
    def isPalindrome(self, head: ListNode) -> bool:
        if not head or (head and not head.next):
            return True
        p=ListNode(-1)
        p.next,fast,slow=head,p,p
        while fast and fast.next:
            slow=slow.next
            fast=fast.next.next
        #这时候分奇数偶数问题
        #偶数的话，slow在前半段最后一个
        #奇数的话，fast在正中间的数据当中
        cur=slow.next #后半段的开始
        slow.next=None #前后端分开
        #下边进行后半段的翻转
        pre=None
        while cur:
            tem=cur.next#保存原来的下一个
            cur.next=pre#下一个数据连起来
            pre=cur#替换上一个数值
            cur=tem#当前值变化
        while pre:
            if pre.val!=p.next.val:
                return False
            pre=pre.next
            p=p.next
        return True
```



### 字符串倒换

#### 直接切片

```java
class Solution {
    public String reverseLeftWords(String s, int n) {
        String res="";
        if(n>s.length()){
            return res;
        }
        return s.substring(n,s.length())+s.substring(0,n);

    }
}
```

#### 使用空的空间来进行排除

```
class Solution {
    public String reverseLeftWords(String s, int n) {
      StringBuilder res=new StringBuilder();
      for(int i=n;i<n+s.length();i++){
          res.append(s.charAt(i%s.length()));
      }
      return res.toString();
    }
}
```



### 翻转链表

##### easy方法这个就不说了

##### 递归比较难

- [ ] 待完成递归的形式

### 删除倒数第k个元素

###### 自己的想法

```python
class Solution:
    def removeNthFromEnd(self, head: ListNode, n: int) -> ListNode:
        fast,slow=head,head
        sumed=0#用来求链表长度
        pre=ListNode(-1)#伪节点
        pre.next=head#指向头结点
        for i in range(n):
            sumed+=1
            fast=fast.next
        while fast:
            sumed+=1
            fast=fast.next
            pre=slow#就是上一个换下一个就可以了
            slow=slow.next
        pre.next=slow.next
        return pre.next if sumed==n else head#如果是删除头结点需要有一个伪节点来进行标志下一个节点
```

```java
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode fast=head;
        ListNode slow=head;
        ListNode pre=new ListNode(-1);
        pre.next=head;
        int sumed=0;
        for(int i=0;i<n;i++){
            fast=fast.next;
            sumed++;
        }
        while(fast!=null){
            fast=fast.next;
            pre=slow;
            slow=slow.next;
            sumed++;
        }
        pre.next=slow.next;
        return head=sumed==n?pre.next:head;

        
    }
}
```



#### 另外一个思路

```python
class Solution:
    def removeNthFromEnd(self, head: ListNode, n: int) -> ListNode:
        #如果用虚拟节点的话，就可以直接锁定待删除节点的前一个节点直接输出就可以
        #再加一个哨兵节点来优化head被删掉的情况就可以
        pre=ListNode(-1)
        pre.next=head
        fast,slow=pre,pre
        for i in range(n):
            fast=fast.next
        while fast.next:
            fast=fast.next
            slow=slow.next
        slow.next=slow.next.next
        return pre.next
```



https://leetcode-cn.com/problems/fan-zhuan-lian-biao-lcof/solution/ru-guo-ni-kan-wan-ping-lun-he-ti-jie-huan-you-wen-/ 

这篇很不错好不好，基本都是模板 代码可以用图片生成



### 打家劫舍问题



#### 使用这个具体的数组来解决：

- 不使用优化

```java
class Solution {
    public int rob(int[] nums) {
        if(nums.length==0){
            return 0;
        }
        int[] res=new int[nums.length+1];
        res[0]=0;
        res[1]=nums[0];
        for(int i=2;i<=nums.length;i++){
            res[i]=Math.max(res[i-1],res[i-2]+nums[i-1]);
        }
        return res[nums.length];

    }
}
```

```python
class Solution:
    def rob(self, nums: List[int]) -> int:
        if len(nums)==0:
            return 0
        if len(nums)==1:
            return nums[0]
        res=[0 for i in range(len(nums)+1)]
        res[0],res[1]=0,nums[0]
        for i in range(2,len(nums)+1):
            res[i]=max(res[i-2]+nums[i-1],res[i-1])#注意对应关系，res[i]代表前i家最多抢多少，对应nums[i-1]
        return res[len(nums)]
```




- 使用空间优化





### 一的个数

java和python的位运算

| 运算符 | 描述                                                         |                             实例                             |
| :----- | :----------------------------------------------------------- | :----------------------------------------------------------: |
| &      | 按位与运算符：参与运算的两个值,如果两个相应位都为1,则该位的结果为1,否则为0 |         (a & b) 输出结果 12 ，二进制解释： 0000 1100         |
| \|     | 按位或运算符：只要对应的二个二进位有一个为1时，结果位就为1。 |        (a \| b) 输出结果 61 ，二进制解释： 0011 1101         |
| ^      | 按位异或运算符：当两对应的二进位相异时，结果为1              |         (a ^ b) 输出结果 49 ，二进制解释： 0011 0001         |
| ~      | 按位取反运算符：对数据的每个二进制位取反,即把1变为0,把0变为1 。**~x** 类似于 **-x-1** | (~a ) 输出结果 -61 ，二进制解释： 1100 0011，在一个有符号二进制数的补码形式。 |
| <<     | 左移动运算符：运算数的各二进位全部左移若干位，由 **<<** 右边的数字指定了移动的位数，高位丢弃，低位补0。 |         a << 2 输出结果 240 ，二进制解释： 1111 0000         |
| >>     | 右移动运算符：把">>"左边的运算数的各二进位全部右移若干位，**>>** 右边的数字指定了移动的位数 |         a >> 2 输出结果 15 ，二进制解释： 0000 1111          |

##### 直接右边移动顺便判断右边是不是1就可以

***注意事项***：==java需要使用>>> 代表无符号数的右移动，左边是补充0，而不是根据原来的符号来判断，跟python不一样==

```java
public class Solution {
    public int hammingWeight(int n) {
        int res = 0;
        while(n != 0) {
            res += n & 1;
            n >>>= 1;
        }
        return res;
    }
}
```

```python
class Solution:
    def hammingWeight(self, n: int) -> int:
        res=0
        while n!=0:
            if n&1==1:
                res+=1
            n=n>>1
        return res
```



#### 一种很好的方法

![image-20201120194811465](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20201120194811465.png)

![Picture10.png](https://pic.leetcode-cn.com/9bc8ab7ba242888d5291770d35ef749ae76ee2f1a51d31d729324755fc4b1b1c-Picture10.png)

![image-20201120194935195](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20201120194935195.png)

每一次n&(n-1)就可以把最右边的1消掉，不断循环直到为0

![img](https://pic.leetcode-cn.com/45b120bce8248a3d573c3d357a99cff589dd511b1c86ce88d2d4b4554ee0f25f-Picture11.png)

![img](https://pic.leetcode-cn.com/5a1ee9ab9e12156294f3324bc8e5a454e22794f0994020e42f4513c6ca331ed9-Picture12.png)

![img](https://pic.leetcode-cn.com/72cb7482dca02d6364a26016a92451442a21adf7b43db1d2702616aff5857405-Picture13.png)

![img](https://pic.leetcode-cn.com/62e1ea0cce964e06ec99da89bd4e312472b7f561ee592ece70240059efd8dca7-Picture14.png)

```java
public class Solution {
    public int hammingWeight(int n) {
        int res = 0;
        while(n != 0) {
            res += 1;
            n &=(n-1);
        }
        return res;
    }
}
```

```python
class Solution:
    def hammingWeight(self, n: int) -> int:
        res=0
        while n!=0:
            res+=1
            n&=(n-1)
        return res
```

### 合并排序的链表

#### 法一 重新拼凑遍历比较就可以

难点在于：伪节点的引入，保证伪节点不变化

```python
class Solution:
    def mergeTwoLists(self, l1: ListNode, l2: ListNode) -> ListNode:
        dummy=ListNode(-1)#伪节点
        cur=dummy#这个是结果链表的指针
        while  l1 and  l2:#当有一个为空的时候就跳出来
            if l1.val<l2.val:
                cur.next=l1
                l1=l1.next
                cur=cur.next
            else:
                cur.next=l2
                l2=l2.next
                cur=cur.next
        cur.next=l1 if l1 else l2
        return dummy.next
```

```java
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy=new ListNode(-1);
        ListNode cur=dummy;
        while(l1!=null&&l2!=null){
            if(l1.val>l2.val){
                cur.next=l2;
                l2=l2.next;
                cur=cur.next;
            }
            else{
                cur.next=l1;
                l1=l1.next;
                cur=cur.next;
            }
        }
        cur.next=l1!=null?l1:l2;
        return dummy.next;

    }
    
}
```

### 递归法

```java
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        //这个函数的意思就是返回l1和l2较小值并且递归拼接
        if(l1==null){
            return l2;
        }
        if(l2==null){
            return l1;
        }
        if(l1.val<l2.val){
            l1.next=mergeTwoLists(l1.next,l2);
            return l1;
        }
        l2.next=mergeTwoLists(l1,l2.next);
        return l2;

    }
    
}
```





### 和为s的连续数据：

#### 左臂右臂格式

```python
class Solution:
    def findContinuousSequence(self, target: int) -> List[List[int]]:
        #感觉还是左闭右闭作为结果比较好
        #因为都是正整数，所以从1开始比较合适好不好
        #假设初始的长度为2，因为至少两个数据吗
        left,right=1,2
        res=[]
        sumed=left+right
        while left<=target/2:
            if sumed<target:#因为已经有了当前的right 所以先加再进行学习就可以
                right+=1
                sumed+=right
            elif sumed>target:
                sumed-=left
                left+=1
            else:
                res.append(list(range(left,right+1)))
                sumed-=left
                left+=1
        return res
```



#### 左闭右开格式

```java
    public int[][] findContinuousSequence(int target) {
        //注意返回的是这个数据二维数组好不好,但是长度不定，所以需要使用动态数组ArrayLidt 里边还是用int[],因为后边整体可以转成二维数组
        int left=1,right=2;
        int sumed=left+right;
        List<int[]> res=new ArrayList<>();//
        while(left<=target/2){
            if(sumed<target){
                right+=1;
                sumed+=right;
            }
            else if(sumed>target){
                sumed-=left;
                left+=1;
            }
            else{
                int[] tem=new int[right-left+1];
                for(int i=left;i<=right;i++){
                    tem[i-left]=i;
                }
                res.add(tem);
               sumed-=left;
               left+=1; 
            }
            
            
        }
        return res.toArray(new int[res.size()][]);//二维数组第一个维度必须确定
    }
}
```

### 众数

####  使用排序加中间数

```python
class Solution:
    def majorityElement(self, nums: List[int]) -> int:
        dic={}
        for num in nums:
            if num not in dic:
                dic[num]=1
            else:
                dic[num]+=1
        maxed=-float('inf')
        res=0
        #本质上就是比较
        for key in dic.keys():
            if maxed<dic[key]:
                maxed=dic[key]
                res=key
        return res
```

#### 使用hashmap

```python
class Solution:
    def majorityElement(self, nums: List[int]) -> int:
        dic={}
        for num in nums:
            if num not in dic:
                dic[num]=1
            else:
                dic[num]+=1
        res=max(dic.keys(),key=lambda x:dic[x])
        return res
```

```java
class Solution {
    public int majorityElement(int[] nums) {
        HashMap<Integer,Integer> dic=new HashMap<>();//这个初始化value上全部都是0
       #因为是引用类型的Integer 默认没有就是null
        for(Integer num:nums){
            dic.put(num,dic.get(num)==null?1:dic.get(num)+1);
        }
        Integer res=0,maxed=-100000;//遍历hashmap
        for(Map.Entry<Integer,Integer> entry:dic.entrySet()){
            if(entry.getValue()>maxed){
                res=entry.getKey();
                maxed=entry.getValue();
            }
        }
        return res;



    }
}
```



#### 投票法

```
class Solution:
    def majorityElement(self, nums: List[int]) -> int:
        maxed=nums[0]
        flag=1
        for i in range(1,len(nums)):
            if flag==0:
                flag=1
                maxed=nums[i]
            if nums[i]!=maxed and flag!=0:
                flag-=1
            if nums[i]==maxed and flag!=0:
                flag+=1
        return maxed
```





### 和为指定值的数据

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        //使用双指针来求解
        int left=0,right=nums.length-1;
        while(left<right){
            if(nums[left]+nums[right]<target){
                left++;
            }
            else if(nums[left]+nums[right]>target){
                right--;
            }
            else{
                return new int[]{nums[left],nums[right]};#//注意要什么直接来进行int数据的新建就可以
            }
        }
        return new int[]{};

    }
}
```

```python
class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        left,right=0,len(nums)-1
        while left<right:
            s=nums[left]+nums[right]
            if s>target:
                right-=1
            elif s<target:
                left+=1
            else:
                return [nums[left],nums[right]]
        return []
```



#### 二分法

```python
class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        #后边使用这个二分查找
        for i in range(len(nums)):
            left=i+1
            right=len(nums)-1
            target1=target-nums[i]
            while left<=right:
                mid=left+(right-left)//2
                if nums[mid]==target1:
                    return [nums[i],nums[mid]]
                elif nums[mid]<target1:
                    left=mid+1
                else:
                    right=mid-1
            return []
```

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        for(int i = 0;i<nums.length;++i){
            int left = i + 1,right = nums.length-1,e =target - nums[i];
            while(left <= right){
                int mid = left + (right - left)/2;
                if(nums[mid] == e){
                    return new int[]{nums[i],nums[mid]};
                }else if(nums[mid] > e){
                    right = mid -1;
                }else if(nums[mid] < e){
                    left = mid + 1;
                }
            }
        }
        return new int[]{};

    }
}
```







#### 前后指针的问题



```python
class Solution:
    def exchange(self, nums: List[int]) -> List[int]:
        #双指针比较easy
        left=0 
        right=len(nums)-1
        while left<right:
            while nums[left]%2==1 and left<right:#找到偶数，同时要保证过程当中不超过边界
                left+=1
            while nums[right]%2==0 and left<right:#找到偶数，同时要保证过程当中不超过边界
                right-=1
            if left>=right:
                return nums
            nums[left],nums[right]=nums[right],nums[left]
            left+=1
            right-=1
        return nums
```

```java
class Solution {
    public int[] exchange(int[] nums) {
        int left=0,right=nums.length-1;
        while(left<right){
            while(nums[left]%2==1&&left<right){
                left++;
            }
            while(nums[right]%2==0&&left<right){
                right--;
            }
            if(left>=right){
                return nums;
            }
            int tem;
            tem=nums[left];
            nums[left]=nums[right];
            nums[right]=tem;
            right--;
            left++;
        }
        return nums;

    }
}
```

#### 快慢指针

```java
class Solution {
    public int[] exchange(int[] nums) {
        int fast=0,slow=0;
        while(fast<nums.length){
            if(nums[fast]%2==1){
                int tem;
                tem=nums[slow];
                nums[slow]=nums[fast];
                nums[fast]=tem;
                slow++;//奇数2位置
            }
            fast++;
        }
        return nums;



    }
}
```

```python
class Solution:
    def exchange(self, nums: List[int]) -> List[int]:
        fast,slow=0,0#fast 往前走 slow来存储奇数位置
        while fast<len(nums):
            if nums[fast]%2==1:
                nums[slow],nums[fast]=nums[fast],nums[slow]
                slow+=1
            fast+=1
        return nums
```



### 排序数据的出现次数

![image-20201123170244165](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20201123170244165.png)

注意这个左边界和右边界定义，这种算法是第一次出现和最后一次出现的位置

```python
class Solution:
    def search(self, nums: List[int], target: int) -> int:
        return self.rightbound(nums,target)-self.rightbound(nums,target)+1
    
    def leftbound(self,nums,target):
        left,right=0,len(nums)-1
        while left<=right:
            mid=(left+right)//2
            if nums[mid]>=target:
                right=mid-1
            elif nums[mid]<target:
                left=mid+1
        return left
    def rightbound(self,nums,target):
        left,right=0,len(nums)-1
        while left<=right:
            mid=(left+right)//2
            if nums[mid]<=target:
                left=mid+1
            elif nums[mid]>target:
                right=mid-1
        return right
```

```java
class Solution {
    public int search(int[] nums, int target) {
        return rightBound(nums, target) - leftBound(nums, target) + 1;
    }

    // 第一次出现的下标或者第一个比target大的下标
    private int leftBound(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] < target) {
                left = mid + 1;
            } else if (nums[mid] >= target) {
                right = mid - 1;
            }
        }
        return left;
    }

    // 最后一次出现的下标或者第一个比target小的下标
    private int rightBound(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] <= target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            }
        }
        return right;
    }
}
```



### 只会出现一次的字符

#### 无脑版

```python
class Solution:
    def firstUniqChar(self, s: str) -> str:
        res={}
        for ss in s:
            res[ss]=res.get(ss,0)+1
        #使用哈希表存储出现的次数
        #遍历哈希表
        for tt in res:
            if res[tt]==1:
                return tt
        return " "
```

```java
class Solution {
    //HashMap不能保证有序好不好,所以使用的是
    public char firstUniqChar(String s) {
        //每一个数据都是Char 类型  所以用Character
        LinkedHashMap<Character,Integer> res=new LinkedHashMap<>();
        for(int i=0;i<s.length();i++){
            Character ss=s.charAt(i);
            res.put(ss,res.get(ss)==null?1:res.get(ss)+1);
        }
            
        
        for(Map.Entry<Character,Integer> entry:res.entrySet()){
            if(entry.getValue()==1){
                return entry.getKey();
            }


        }
        return  ' ';
        

    }
}
```

```java
class Solution {
    //不能保证有序好不好
    public char firstUniqChar(String s) {
        LinkedHashMap<Character,Integer> res=new LinkedHashMap<>();
        char[] carray=s.toCharArray();
        for(char ss:carray){
            //Character ss=s.charAt(i);
            res.put(ss,res.get(ss)==null?1:res.get(ss)+1);
        }
            
        
        for(Map.Entry<Character,Integer> entry:res.entrySet()){
            if(entry.getValue()==1){
                return entry.getKey();
            }


        }
        return  ' ';
        

    }
}
```





### 直接使用数组来强行转就可以

```java
class Solution {
    //不能保证有序好不好
    public char firstUniqChar(String s) {
        int[] res=new int[26];//存储结果，因为只有小写字母,默认是0
        char[] carray=s.toCharArray();
        for(char ss:carray){
            int index=ss-'a';
            res[index]++;
        }
        for(char ss:carray){
            int index=ss-'a';
            if(res[index]==1){
                return ss;
            }
        }
        return ' ';

       
    }
}
```





### 数组排序问题

```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int lened=m+n-1,len1=m-1,len2=n-1;
        while(len1>=0&&len2>=0){
            nums1[lened--]=nums1[len1]>=nums2[len2]?nums1[len1--]:nums2[len2--];
        }
        //下边这个地方，如果是len1>=0的话，直接就放在nums1当中不用动了，如果是len2>=0说明nums1前边位置已经没用了，nums2直接复制就可以
        if(len2>=0){
        System.arraycopy(nums2,0,nums1,0,len2+1);
        }

    }
}
```

```python 
class Solution:
    def merge(self, nums1: List[int], m: int, nums2: List[int], n: int) -> None:
        """
        Do not return anything, modify nums1 in-place instead.
        """
        #答题思路是从后边往前来进行学习
        lened=m+n-1
        len1,len2=m-1,n-1
        #别忘记减去1，因为这个是下标
        while len1>=0 and len2>=0:
            if nums1[len1]>=nums2[len2]:
                nums1[lened]=nums1[len1]
                len1-=1
            else:
                nums1[lened]=nums2[len2]
                len2-=1
            lened-=1
        if len2>=0:
            nums1[0:len2+1]=nums2[0:len2+1]
```



### 两数之和



用字典key 存储值 value 存储下边 边循环边记录

```python
class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        dic={}
        for ind,val in enumerate(nums):
            if target-val in dic:
                return [dic[target-val],ind]
            else:
                dic[val]=ind
```

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        //使用双指针
        HashMap<Integer,Integer> res=new LinkedHashMap<>();
        for(int i=0;i<nums.length;i++){
            if(res.containsKey(target-nums[i])){//记住api
                return new int[]{res.get(target-nums[i]),i};
            }
            else
                res.put(nums[i],i);
        }
       
       throw new IllegalArgumentException("No two sum solution");//抛出异常代替

    }
}
```



### 不使用加减乘除来计算前n项目和

思路：https://leetcode-cn.com/problems/qiu-12n-lcof/solution/mian-shi-ti-64-qiu-1-2-nluo-ji-fu-duan-lu-qing-xi-/

本质上还是使用递归，但是由于第一项还是需要进行判断if 所以改成全局变量和短路进行运算

```java
class Solution {
    int res = 0;
    public int sumNums(int n) {
        boolean x = n > 1 && sumNums(n - 1) > 0;//就是一个过渡的过程好不好，使用短路,该走还是走，大于一的项就递归，第一项就是加一就可以
        res += n;
        return res;
    }
}
```





### 合并有序的链表

```python
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, val=0, next=None):
#         self.val = val
#         self.next = next
class Solution:
    def mergeTwoLists(self, l1: ListNode, l2: ListNode) -> ListNode:
        #其实就是一个不断的迭代的问题，看看下一个会接到哪里去的问题,因此返回的是两个节点当中较小的一个
        if not l1 or not l2:
            return l1 if not l2 else l2
        if l1.val<=l2.val:
            l1.next=self.mergeTwoLists(l1.next,l2)
            return l1
        elif l1.val>l2.val:
            l2.next=self.mergeTwoLists(l1,l2.next)
            return l2
```



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
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1==null||l2==null){
            return l1==null?l2:l1;
        }
        else if(l1.val<=l2.val){
            l1.next=mergeTwoLists(l1.next,l2);
            return l1;
        }
        else{
            l2.next=mergeTwoLists(l1,l2.next);
            return l2;
        }
        
    }
}
```



### 同时进行遍历数据并且存储到一个当中



#### 使用全局变量，把函数写在外边

```python
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

#
# 
# @param root TreeNode类 the root of binary tree
# @return int整型二维数组
#
class Solution:
    def dfs1(self,root):
        if not root:
            return 
        self.res1.append(root.val)
        self.dfs1(root.left)
        self.dfs1(root.right)
    def dfs2(self,root):
        if not root:
            return 
        self.dfs2(root.left)
        self.res1.append(root.val)
        self.dfs2(root.right)
    def dfs3(self,root):
        if not root:
            return 
        self.dfs3(root.left)
        self.dfs3(root.right)
        self.res1.append(root.val)
    def threeOrders(self , root ):
        self.res1=[]
        self.res2=[]
        self.res3=[]
        res=[]
        self.dfs1(root)
        res.append(self.res1)
        self.dfs2(root)
        res.append(self.res2)
        self.dfs3(root)
        res.append(self.res3)
        return res
        #使用这个递归来完成
```

#### 全局变量

```PYTHON 
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

#
# 
# @param root TreeNode类 the root of binary tree
# @return int整型二维数组
#
class Solution:
    def threeOrders(self , root ):
        def preorder(root):
            if not root:
                return
            self.res1.append(root.val)
            preorder(root.left)
            preorder(root.right)
        def inorder(root):
            if not root:
                return 
            inorder(root.left)
            self.res2.append(root.val)
            inorder(root.right)
        def postorder(root):
            if not root:
                return 
            postorder(root.left)
            postorder(root.right)
            self.res3.append(root.val)
        self.res1=[]
        self.res2=[]
        self.res3=[]
        res=[]
        preorder(root)
        res.append(self.res1)
        inorder(root)
        res.append(self.res2)
        postorder(root)
        res.append(self.res3)
        return res
        
        # write code here
```



#### java版本

```java
import java.util.*;

/*
 * public class TreeNode {
 *   int val = 0;
 *   TreeNode left = null;
 *   TreeNode right = null;
 * }
 */

public class Solution {
    /**
     * 
     * @param root TreeNode类 the root of binary tree
     * @return int整型二维数组
     */
    public void preorder(TreeNode root,ArrayList<Integer> res1){
        if(root==null){
            return;
        }
        res1.add(root.val);
        preorder(root.left,res1);
        preorder(root.right,res1);
    }
    public void midorder(TreeNode root,ArrayList<Integer> res2){
        if(root==null){
            return;
        }
        midorder(root.left,res2);
        res2.add(root.val);
        midorder(root.right,res2);
    }
    public void postorder(TreeNode root,ArrayList<Integer> res3){
        if(root==null){
            return;
        }
        postorder(root.left,res3);
        postorder(root.right,res3);
        res3.add(root.val);
    }
    public int[][] threeOrders (TreeNode root) {
        ArrayList<ArrayList<Integer>> res=new ArrayList<>();
        ArrayList<Integer> res1=new ArrayList<>();
        ArrayList<Integer> res2=new ArrayList<>();
        ArrayList<Integer> res3=new ArrayList<>();
        preorder(root,res1);
        midorder(root,res2);
        postorder(root,res3);
        res.add(res1);
        res.add(res2);
        res.add(res3);
        //最后只能无语的化解成循环赋值
        int[][] res4=new int[res.size()][res.get(0).size()];
        for(int i=0;i<3;i++){
            for(int j=0;j<res1.size();j++){
                res4[i][j]=res.get(i).get(j);
            }
        }
        return res4;
        
        
        
    }
}
```



### 第k大的数目

#### easy版的数组排序方法

```java
import java.util.Arrays;
class Solution {
    public int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length-k];

    }
}
```



#### 使用现成的优先队列问题，使用k个大的最小堆数据

```java
class Solution {
    public int findKthLargest(int[] nums, int k) {
        //使用现成的优先队列来实现最大堆与最小堆
        PriorityQueue<Integer> heap=new PriorityQueue<>(k,(a,b)->a-b);//最小堆的结果，维护者前k个最大值
        for(int i=0;i<k;i++){
            heap.add(nums[i]);
        }
        for(int i=k;i<nums.length;i++){
            int topvalue=heap.peek();
            if(topvalue<nums[i]){
                heap.poll();
                heap.add(nums[i]);
            }
        }
        return heap.peek();



    }
}
```

#### 全部的数据都存进去再回来

```java
class Solution {
    public int findKthLargest(int[] nums, int k) {
        //使用现成的优先队列来实现最小堆
        int len=nums.length;
        PriorityQueue<Integer> heap=new PriorityQueue<>(len,(a,b)->a-b);//最小堆的结果，维护者前k个最大值
        for(int i=0;i<len;i++){
            heap.add(nums[i]);
        }
        for(int i=0;i<len-k;i++){

            heap.poll();
        }
        return heap.peek();



    }
}
```

#### 为了更加节省内存，依照k的大小来进行分析

```java
import java.util.PriorityQueue;

public class Solution {

    // 根据 k 的不同，选最大堆和最小堆，目的是让堆中的元素更小
    // 思路 1：k 要是更靠近 0 的话，此时 k 是一个较小的数，用最大堆
    // 例如在一个有 6 个元素的数组里找第 5 大的元素
    // 思路 2：k 要是更靠近 len 的话，用最小堆

    // 所以分界点就是 k = len - k

    public int findKthLargest(int[] nums, int k) {
        int len = nums.length;
        if (k <= len - k) {
            // System.out.println("使用最小堆");
            // 特例：k = 1，用容量为 k 的最小堆
            // 使用一个含有 k 个元素的最小堆
            PriorityQueue<Integer> minHeap = new PriorityQueue<>(k, (a, b) -> a - b);
            for (int i = 0; i < k; i++) {
                minHeap.add(nums[i]);
            }
            for (int i = k; i < len; i++) {
                // 看一眼，不拿出，因为有可能没有必要替换
                Integer topEle = minHeap.peek();
                // 只要当前遍历的元素比堆顶元素大，堆顶弹出，遍历的元素进去
                if (nums[i] > topEle) {
                    minHeap.poll();
                    minHeap.add(nums[i]);
                }
            }
            return minHeap.peek();

        } else {
            // System.out.println("使用最大堆");
            assert k > len - k;
            // 特例：k = 100，用容量为 len - k + 1 的最大堆
            int capacity = len - k + 1;
            PriorityQueue<Integer> maxHeap = new PriorityQueue<>(capacity, (a, b) -> b - a);
            for (int i = 0; i < capacity; i++) {
                maxHeap.add(nums[i]);
            }
            for (int i = capacity; i < len; i++) {
                // 看一眼，不拿出，因为有可能没有必要替换
                Integer topEle = maxHeap.peek();
                // 只要当前遍历的元素比堆顶元素大，堆顶弹出，遍历的元素进去
                if (nums[i] < topEle) {
                    maxHeap.poll();
                    maxHeap.add(nums[i]);
                }
            }
            return maxHeap.peek();
        }
    }
}

```



### LRU 题目的理解

[LeetCode题解](https://leetcode-cn.com/problems/lru-cache/solution/lru-ce-lue-xiang-jie-he-shi-xian-by-labuladong/)

![HashLinkedList](https://pic.leetcode-cn.com/b84cf65debb43b28bd212787ca63d34c9962696ed427f638763be71a3cb8f89d.jpg)

https://www.bilibili.com/video/BV18A411i7ui

LRU 算法实际上是让你设计数据结构：首先要接收一个 capacity 参数作为缓存的最大容量，然后实现两个 API，一个是 put(key, val) 方法存入键值对，另一个是 get(key) 方法获取 key 对应的 val，如果 key 不存在则返回 -1。

注意哦，get 和 put 方法必须都是 O(1) 的时间复杂度，我们举个具体例子来看看 LRU 算法怎么工作。

```java
/* 缓存容量为 2 */
LRUCache cache = new LRUCache(2);
// 你可以把 cache 理解成一个队列
// 假设左边是队头，右边是队尾
// 最近使用的排在队头，久未使用的排在队尾
// 圆括号表示键值对 (key, val)

cache.put(1, 1);
// cache = [(1, 1)]
cache.put(2, 2);
// cache = [(2, 2), (1, 1)]
cache.get(1);       // 返回 1
// cache = [(1, 1), (2, 2)]
// 解释：因为最近访问了键 1，所以提前至队头
// 返回键 1 对应的值 1
cache.put(3, 3);
// cache = [(3, 3), (1, 1)]
// 解释：缓存容量已满，需要删除内容空出位置
// 优先删除久未使用的数据，也就是队尾的数据
// 然后把新的数据插入队头
cache.get(2);       // 返回 -1 (未找到)
// cache = [(3, 3), (1, 1)]
// 解释：cache 中不存在键为 2 的数据
cache.put(1, 4);    
// cache = [(1, 4), (3, 3)]
// 解释：键 1 已存在，把原始值 1 覆盖为 4
// 不要忘了也要将键值对提前到队头


```

- 因为需要进行put get 操作，并且需要复杂度满足要求的所以需要合适的这个数据结构来存储
- 查找快，插入快，删除快，有顺序之分
-  cache 必须有顺序之分，以区分最近使用的和久未使用的数据；而且我们要在 cache 中查找键是否已存在；如果容量满了要删除最后一个数据；每次访问还要把数据插入到队头。
- 那么，什么数据结构同时符合上述条件呢？哈希表查找快，但是数据无固定顺序；链表有顺序之分，插入删除快，但是查找慢。所以结合一下，形成一种新的数据结构：哈希链表。



 双向链表节点类

```java
  class Node {
      public int key, val;
      public Node next, prev;
      public Node(int k, int v) {
          this.key = k;
          this.val = v;
      }
  }
  
```

  需要实现一个双向链表

```java
  class DoubleList {  
      // 在链表头部添加节点 x，时间 O(1)
      public void addFirst(Node x);
  
      // 删除链表中的 x 节点（x 一定存在）
      // 由于是双链表且给的是目标 Node 节点，时间 O(1)
      public void remove(Node x);
      
      // 删除链表中最后一个节点，并返回该节点，时间 O(1)
      public Node removeLast();
      
      // 返回链表长度，时间 O(1)
      public int size();
  }
  
```

  

整体结构伪代码

```java
// key 映射到 Node(key, val)
HashMap<Integer, Node> map;
// Node(k1, v1) <-> Node(k2, v2)...
DoubleList cache;

int get(int key) {
    if (key 不存在) {
        return -1;
    } else {        
        将数据 (key, val) 提到开头；
        return val;
    }
}

void put(int key, int val) {
    Node x = new Node(key, val);
    if (key 已存在) {
        把旧的数据删除；
        将新节点 x 插入到开头；
    } else {
        if (cache 已满) {
            删除链表的最后一个数据腾位置；
            删除 map 中映射到该数据的键；
        } 
        将新节点 x 插入到开头；
        map 中新建 key 对新节点 x 的映射；
    }
}


```

```java
class LRUCache {
    // key -> Node(key, val)
    private HashMap<Integer, Node> map;
    // Node(k1, v1) <-> Node(k2, v2)...
    private DoubleList cache;
    // 最大容量
    private int cap;
    
    public LRUCache(int capacity) {
        this.cap = capacity;
        map = new HashMap<>();
        cache = new DoubleList();
    }
    
    public int get(int key) {
        if (!map.containsKey(key))
            return -1;
        int val = map.get(key).val;
        // 利用 put 方法把该数据提前
        put(key, val);
        return val;
    }
    
    public void put(int key, int val) {
        // 先把新节点 x 做出来
        Node x = new Node(key, val);
        
        if (map.containsKey(key)) {
            // 删除旧的节点，新的插到头部
            cache.remove(map.get(key));
            cache.addFirst(x);
            // 更新 map 中对应的数据
            map.put(key, x);
        } else {
            if (cap == cache.size()) {
                // 删除链表最后一个数据
                Node last = cache.removeLast();
                map.remove(last.key);
            }
            // 直接添加到头部
            cache.addFirst(x);
            map.put(key, x);
        }
    }
}

```





### 使用两个栈来实现先进先出的队列

#### 重点是记住api

```java
class MyQueue {
    //声明成员变量
    private Stack<Integer> Popstack;
    private Stack<Integer> Pushstack;

    /** Initialize your data structure here. */
    public MyQueue() {
        //初始化两个栈，一个用来当做输入一个当做输出
        Popstack=new Stack<>();
        Pushstack=new Stack<>();

    }
    
    /** Push element x to the back of queue. */
    public void push(int x) {
        //无论什么时候都应该直接往Pushstack里边进行填充数据
        Pushstack.push(x);

    }
    
    /** Removes the element from in front of queue and returns that element. */
    public int pop() {
        //Popstack当中有数据的话，直接进行输出第一个
        //当前Popstack当中没有数据之后，需要进行补充，把pushstack当中数据全部倒到popstack当中，再输入第一个，这样才不会弄坏先入先出的顺序在里边
        if(Popstack.isEmpty()){
            while(!Pushstack.isEmpty()){
                Popstack.push(Pushstack.pop());
            }
        }
        return Popstack.pop();



    }
    
    /** Get the front element. */
    public int peek() {
         if(Popstack.isEmpty()){
            while(!Pushstack.isEmpty()){
                Popstack.push(Pushstack.pop());
            }
        }
        return Popstack.peek();

    }
    
    /** Returns whether the queue is empty. */
    public boolean empty() {
        return Popstack.isEmpty()&&Pushstack.isEmpty();

    }
}
```

```java
class MyQueue:

    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.pushstack=[]
        self.popstack=[]


    def push(self, x: int) -> None:
        """
        Push element x to the back of queue.
        """
        self.pushstack.append(x)
        


    def pop(self) -> int:
        """
        Removes the element from in front of queue and returns that element.
        """
        if len(self.popstack)==0:
            while len(self.pushstack)>0:
                self.popstack.append(self.pushstack.pop(0))
            return self.popstack.pop(0)



    def peek(self) -> int:
        """
        Get the front element.
        """ 
        while len(self.pushstack)>0:
            self.popstack.append(self.pushstack.pop(0))
        return self.popstack[0]


    def empty(self) -> bool:
        return len(self.popstack)==0 and len(self.pushstack)==0
        """
        Returns whether the queue is empty.
        """



# Your MyQueue object will be instantiated and called as such:
# obj = MyQueue()
# obj.push(x)
# param_2 = obj.pop()
# param_3 = obj.peek()
# param_4 = obj.empty()
```



### 跳台阶问题

```java
public class Solution {
    public int JumpFloor(int target) {
        int pre=1;
        int cur=1;
        for(int i=0;i<target-1;i++){
            int tem=cur;
            cur=pre+cur;
            pre=tem;
        }
        return cur;
        


    }
```



### 倒数第k个节点

```java
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode pre=new ListNode(-1);
        pre.next=head;
        ListNode fast=pre;
        ListNode slow=pre;
        for(int i=0;i<n;i++){
            fast=fast.next;
        }
        while(fast.next!=null){
            fast=fast.next;
            slow=slow.next;
        }
        slow.next=slow.next.next;
        return pre.next;
      
        
    }
}
```

关键就是把slow放在要删减的前一个，便于slow.next=slow.next.next;就可以

### [剑指 Offer 56 - II. 数组中数字出现的次数 II](https://leetcode-cn.com/problems/shu-zu-zhong-shu-zi-chu-xian-de-ci-shu-ii-lcof/)‘

#### 朴素的哈希表方法

```python
class Solution:
    def singleNumber(self, nums: List[int]) -> int:
        #使用哈希表进行学习
        dic={}
        for num in nums:
            if num not in dic:
                dic[num]=1
            else:
                dic[num]+=1
        for key in dic.keys():
            if dic[key]==1:
                return key
        return -1
```

```java
class Solution {
    public int singleNumber(int[] nums) {
        HashMap<Integer,Integer> res= new HashMap<>();
        for(int num:nums){
            res.put(num,res.get(num)==null?1:res.get(num)+1);
        }
        for(Integer key:res.keySet()){
            if (res.get(key)==1)
                return key;
        } 
        return -1;
    }
}
```



### 位运算求解



[链接的思路2](https://leetcode-cn.com/problems/shu-zu-zhong-shu-zi-chu-xian-de-ci-shu-ii-lcof/solution/mian-shi-ti-56-ii-shu-zu-zhong-shu-zi-chu-xian-d-4/)

```java
class Solution {
    public int singleNumber(int[] nums) {
        int res=0;//最后移动位置求各位的数目
        //第一步求出每一个位置上数据的1的个数
        int[] count=new int[32];
        for(int num:nums){
            for(int j=0;j<32;j++){
                count[j]+=num&1;
                num>>>=1;

            }
        }
        //需要拆出来
        for(int i=0;i<32;i++){
            res<<=1;
            res|=count[31-i]%3;
        }
        return res;
    }
}
```

[思路来源](https://leetcode-cn.com/problems/shu-zu-zhong-shu-zi-chu-xian-de-ci-shu-ii-lcof/solution/javashi-xian-jian-zhi-si-lu-wei-yun-suan-zhu-wei-t/)

上述思路不能解决这里的问题,因为三个相同的数字的异或结果还是该数字。尽管我们这里不能应用异或运算,我们还是可以沿用位运算的思路。
如果一个数字出现三次,那么它的二进制表示的每一位(0或者1)也出现三次。如果把所有出现三次的数字的二进制表示的每一位都分别加起来,那么每一位的和都能被3整除。如果某一位的和能被3整除,那么那个只出现一次的数字二进制表示中对应的那一位是0;否则就是1;



```java
public class Solution {
	
    public int singleNumber(int[] nums) {//本算法同样适用于数组nums中存在负数的情况
        if(nums.length==0) return -1;//输入数组长度不符合要求，返回-1;
        int[] bitSum = new int[32];//java int类型有32位，其中首位为符号位
        int res=0;
        for(int num:nums){
            int bitMask=1;//需要在这里初始化，不能和res一起初始化
            for(int i=31;i>=0;i--){//bitSum[0]为符号位
            	//这里同样可以通过num的无符号右移>>>来实现，否则带符号右移(>>)左侧会补符号位，对于负数会出错。
            	//但是不推荐这样做，最好不要修改原数组nums的数据
                if((num&bitMask)!=0) bitSum[i]++;//这里判断条件也可以写为(num&bitMask)==bitMask,而不是==1
                bitMask=bitMask<<1;//左移没有无符号、带符号的区别，都是在右侧补0
            }
        }
        for(int i=0;i<32;i++){//这种做法使得本算法同样适用于负数的情况
            res=res<<1;
            res+=bitSum[i]%3;//这两步顺序不能变，否则最后一步会多左移一次
        }
        return res;
    }
}
```



### 出现一次的数字

#### 无脑的哈希表

```java
class Solution {
    public int[] singleNumbers(int[] nums) {
        HashMap<Integer,Integer> res =new HashMap<>();
        //List<Integer> finall=new ArrayList<>();
        int[] finall=new int[2];
        for(int num:nums){
            res.put(num,res.get(num)==null?1:res.get(num)+1);

        }
        int index=0;
        for(int num:nums){
            if(res.get(num)==1){
                finall[index]=num;
                index++;
            }
            
        }
        return finall;

    }
}
```



```python 
class Solution:
    def singleNumbers(self, nums: List[int]) -> List[int]:
        #第一种使用这个哈希表
        resed=[]
        dic={}
        for num in nums:
            dic[num]=dic.get(num,0)+1
        for res in set(nums):
            if dic[res]==1:
                resed.append(res)
        return resed
```

#### 使用库函数进行弄起来

```python
class Solution:
    def singleNumbers(self, nums: List[int]) -> List[int]:
        res=[]
        for num in set(nums):
            if nums.count(num)==1:
                res.append(num)
        return res

```

#### 使用位运算来求解

[思路来源--这个链接的评论](https://leetcode-cn.com/problems/shu-zu-zhong-shu-zi-chu-xian-de-ci-shu-lcof/solution/jie-di-qi-jiang-jie-fen-zu-wei-yun-suan-by-eddievi/)

难点主要在于对mask的理解。mask是一个二进制数，且其中只有一位是1，其他位全是0，比如000010，表示我们用倒数第二位作为分组标准，倒数第二位是0的数字分到一组，倒数第二位是1的分到另一组。

那么如何得到这个mask?因为我们分组的目的就是将两个不重复数字分开，这两个不重复数字的二进制表示肯定是不同的，但是我们没必要一位一位地比较，我们可以从右到左，找到第一个不相同的位，将mask当中这一位变成1,就得到了mask。

比如[2,2,3,3,4,6]中，不重复的两个数字是4，6，4和6的异或结果（也是整个数组的异或结果）是010，表示从右到左，第一次出现不同是在倒数第二位，那么可以确定，mask的倒数第二位是1，其他位是0，即010。



```java
class Solution {
    public int[] singleNumbers(int[] nums) {
        int resA=0,resB=0;//因为按照位异或0的话不会影响具体结果，而且顺序也不会影响结果
        int xor=0;//代表所有数的异或结果，第一个不为0的位来制作mask密码子，不相同的两个数在密码子1位上一定结果不一样
        int mask=1;//
        for(int num:nums){
            xor^=num;
        }
        while((xor&mask)==0){
            mask<<=1;
        }
        //得到的mask就是密码，对接到的数据进行分组，这里的分组是和密码子相与，这样相同的数据就会分到一个组，再进行异或就可以
        for(int num:nums){
            if((num&mask)!=0){
                resA^=num;
            }
            else{
                resB^=num;
            }
        }
        return new int[]{resA,resB};
        
        
        
    }
}
```

```python
class Solution:
    def singleNumbers(self, nums: List[int]) -> List[int]:
        resA,resB=0,0
        xor,mask=0,1
        for num in nums:
            xor^=num
        while xor&mask==0:
            mask<<1
        for num in nums:
            if num&mask:
                resA^=num
            else:
                resB^=num
        return [resA,resB]
```



### 位运算进行求解出现次数的问题





### 深拷贝链表问题

#### ==非常巧妙的思路==（还没有掌握）

[思路+评论里边解释](https://leetcode-cn.com/problems/fu-za-lian-biao-de-fu-zhi-lcof/solution/fu-za-lian-biao-de-fu-zhi-jian-dan-yi-dong-de-san-/)

![图解 (2).png](https://pic.leetcode-cn.com/7a70fbac7677a9a1cd31d404a0ad2d9dc3d92ead30b5e741bb2459dea2077523-%E5%9B%BE%E8%A7%A3%20(2).png)

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
        if (head == null) return head;
       
        //在每个Node后面新建一个Node做自己的copy
        copy(head);
        // 先处理random域，因为需要用到next域
        handleRandom(head);

        // 需要暂存copyHead，因为后续会改变next域
        Node res = head.next;
        
        handleNext(head);

        return res;
    }


    //这个函数就是从源节点的头部开始复制新的节点，并且连接在原来节点上
    private void copy(Node head){

        while (head != null) {
            Node tmp = head.next;
            Node copy = new Node(head.val);
            
            head.next = copy;
            copy.next = tmp;
            
            head = tmp;
        }
    }
    
    //这个函数就是把每一个复制后的新节点的random接到原来节点的random节点的新节点上
    private void handleRandom(Node head){
        while (head != null) {
            // 把copy节点的random也指向copy节点
            Node copy = head.next;
            // 注意：random可能指向null，此时不能指向其next结点
            if (head.random != null) {
                copy.random = head.random.next;
            }

            head = copy.next;
        }
    }
    
	//下边这个函数就是分离新的节点和旧的节点
    
    
    
    // 处理next域 , 这里需要把源和copy节点一起处理，因为next结点是相关的。
    // 若单处理一者，循环后，另一个就没办法找到属自己类的结点了。
    // 两者处理相同: curNode.next = curNode.next.next
    // 源 -> copy -> {源 -> copy}+ -> null
    private void handleNext(Node cur){
        // 循环条件无需为: cur!=null
        while (true) {
            // 处理最后一个 源结点，指回null
            // 而在这之前，cur都不可能为空
            if (cur.next != null && cur.next.next == null) {
                cur.next = null;
                break;
            }

            Node tmp = cur.next;
            cur.next = tmp.next;
            cur = tmp;
        }
    }

}
```

#### 使用哈希表来弄

```java
//利用哈希表来求解
class Solution {
    Map<Node,Node> copy=new HashMap<>();//定义一个哈希表存储每本身节点和复制后的节点
    public Node copyRandomList(Node head) {
       Node dummy=new Node(0) ,tail=dummy,p=head;
       //dummy来定位之后的新生成的节点
       //下边这个函数是复制新节点并且把他们通过next链接起来
        while(p!=null){
            Node node=new Node(p.val);
            copy.put(p,node);
            tail.next=node;//这一步是把新生成的node挂在新node后边
            tail=tail.next;//把新生成的node作为新的
            p=p.next;
        }
        p=dummy.next;//记录下第一个生成的复制的node，因为一开始是dummy和tail等价，而tail第一个语句是tail.next=node（第一个生成的node)

        //新的和旧的对齐了之后就重新生成random节点
        while(head!=null){
            p.random=copy.get(head.random);
            p=p.next;
            head=head.next;
        }

        return dummy.next;
    }
}

```





### 利用前序和中序来 还原二叉树

##### 无脑递归版本

[思路](https://leetcode-cn.com/problems/zhong-jian-er-cha-shu-lcof/solution/di-gui-jie-fa-by-ml-zimingmeng-3/)

[思路2很简洁](https://leetcode-cn.com/problems/zhong-jian-er-cha-shu-lcof/solution/pythonti-jie-qing-xi-di-gui-si-lu-by-xiao-xue-66/)

```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution:
    def buildTree(self, preorder: List[int], inorder: List[int]) -> TreeNode:
        if len(preorder)==0 or len(preorder)!=len(inorder):
            return None
        #注意要返回的是这个节点，因此需要递归
        temhead=TreeNode(preorder[0])#取得是当前节点，就是前序表当中的第一个,新建一个节点
        idx=inorder.index(temhead.val)#找出中序当中的数据，便于分离左右子树，此时左子树的长度为inx 为inorder(0:inx)右子树长度为len(preorder)-idx-1 范围是inorder(idx+1,len(predider))
        temhead.left=self.buildTree(preorder[1:1+idx],inorder[0:idx])
        temhead.right=self.buildTree(preorder[1+idx:len(preorder)],inorder[idx+1:len(preorder)])
        return temhead
```

#### 因为java不方便进行切片，所以使用索引来进行

```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution:
    def buildTree(self, preorder: List[int], inorder: List[int]) -> TreeNode:
        if len(preorder)==0 or len(preorder)!=len(inorder):
            return None
        #root是当前数在前序当中的索引
        #left，right是当前数在中序当中的索引
        def recur(root,left,right):
            while left>right:
                return #跳出循环
            temhead=TreeNode(preorder[root])#构造根节点
            idx=dic[preorder[root]]#找出根节点在中序当中的索引
            temhead.left=recur(root+1,left,idx-1)#左右更新，左子树的前序根节点就是root+1 左边是left,右边可以推导出长度
            temhead.right=recur(root+1+idx-left,idx+1,right)#右子树根节点在前序位置可以推出，在中序当中节点可以退出
            return temhead
        dic={}
        for i in range(len(inorder)):
            dic[inorder[i]]=i
        return recur(0,0,len(preorder)-1)
        
```

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
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if(preorder.length==0 || preorder.length!=inorder.length){
            return null;
        }
        HashMap<Integer,Integer> res=new HashMap<>();
    
        for(int i=0;i<inorder.length;i++){
            //TreeNode tem=new TreeNode(inorder[i]);
            res.put(inorder[i],i);
        }
        return recur(0,0,preorder.length-1);
        


    }
    public TreeNode recur(int root,int left,int right){
        if(left>right){
            return null;
        }
        TreeNode temhead= new TreeNode(preorder[root]);
        int idx=dic.get(preorder[root]);
        temhead.left=recur(root+1,left,idx-1);
        temhead.right=recur(root+1+idx-left,idx+1,right);
        return temhead;

    }
}
```



### 打印二叉树

##### 基本的数据结构来求解比较合适

##### 使用python默认的[]当做，使用专门的队列deque

```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution:
    def levelOrder(self, root: TreeNode) -> List[int]:
        #感觉使用栈来比较合适
        if not root:
            return []
        res=[]
        queue=[root]
        while len(queue)!=0:
            tem=queue.pop(0)
            res.append(tem.val)
            if tem.left:
                queue.append(tem.left)
            if tem.right:
                queue.append(tem.right)
        return res
```

```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution:
    def levelOrder(self, root: TreeNode) -> List[int]:
        if not root: return []
        res, queue = [], collections.deque()
        queue.append(root)
        while queue:
            node = queue.popleft()
            res.append(node.val)
            if node.left: queue.append(node.left)
            if node.right: queue.append(node.right)
        return res
```



##### java 的队列api的使用

[廖雪峰的网站](https://www.liaoxuefeng.com/wiki/1252599548343744/1265121791832960)

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
    public int[] levelOrder(TreeNode root) {
        if(root==null){
            return new int[0];
        }
        Queue<TreeNode> nodelist=new LinkedList<>();
        ArrayList<Integer> res=new ArrayList<>();
        nodelist.add(root);
        while(nodelist.size()>0){
            TreeNode node =nodelist.poll();
            res.add(node.val);
            if(node.left!=null){
                nodelist.add(node.left);
            }
            if(node.right!=null){
                nodelist.add(node.right);
            }
        }
        int[] resd=new int[res.size()];
        for(int i=0;i<res.size();i++){
            resd[i]=res.get(i);
        }
        return resd;

    }
}
```

arraylist是get

queue 使用的是Queue来构造的

poll 和 peek offer 和add



### 最大的礼物价值问题（动态规划）

##### 题解见第一个，主要思路就是直接进行换算就可以

#### 原地修改没有问题

```python
class Solution:
    def maxValue(self, grid: List[List[int]]) -> int:
        m,n=len(grid),len(grid[0])
        #首先进行行与列的初始化
        for i in range(1,m):
            grid[i][0]+=grid[i-1][0]
        for j in range(1,n):
            grid[0][j]+=grid[0][j-1]
        for i in range(1,m):
            for j in range(1,n):
                grid[i][j]+=max(grid[i-1][j],grid[i][j-1])
        return grid[-1][-1]
```



### 第n个丑数（动态规划）

![image-20201211171428048](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20201211171428048.png)

```java
class Solution {
    public int nthUglyNumber(int n) {
        int[] dp = new int[n];  // 使用dp数组来存储丑数序列
        dp[0] = 1;  // dp[0]已知为1
        int a = 0, b = 0, c = 0;    // 下个应该通过乘2来获得新丑数的数据是第a个， 同理b, c
        //需要明白的是，这个每一个位置都需要比较谁大谁小，一旦确定后边就进行加一
        for(int i = 1; i < n; i++){
            // 第a丑数个数需要通过乘2来得到下个丑数，第b丑数个数需要通过乘2来得到下个丑数，同理第c个数
            int n2 = dp[a] * 2, n3 = dp[b] * 3, n5 = dp[c] * 5;
            dp[i] = Math.min(Math.min(n2, n3), n5);
            if(dp[i] == n2){
                a++; // 第a个数已经通过乘2得到了一个新的丑数，那下个需要通过乘2得到一个新的丑数的数应该是第(a+1)个数
            }
            if(dp[i] == n3){
                b++; // 第 b个数已经通过乘3得到了一个新的丑数，那下个需要通过乘3得到一个新的丑数的数应该是第(b+1)个数
            }
            if(dp[i] == n5){
                c++; // 第 c个数已经通过乘5得到了一个新的丑数，那下个需要通过乘5得到一个新的丑数的数应该是第(c+1)个数
            }
        }
        return dp[n-1];
    }
}
```



```python
class Solution:
    def nthUglyNumber(self, n: int) -> int:
        res=[0 for i in range(n)]#存储序列
        res[0]=1
        a,b,c=0,0,0#进行不同倍数的增长，初始化为0的原因是这个都是第一个数的倍数
        for i in range(1,n):#遍历之后的丑数排列
            chou_a,chou_b,chou_c=res[a]*2,res[b]*3,res[c]*5
            res[i]=min(min(chou_a,chou_b),chou_c)
            if res[i]==chou_a:
                a+=1 #说明要找第二小的*2的丑数
            elif res[i]==chou_b:
                b+=1
            elif res[i]==chou_c:
                c+=1
        return res[n-1]
```



### 最大利润

[解答](https://leetcode-cn.com/problems/gu-piao-de-zui-da-li-run-lcof/solution/mian-shi-ti-63-gu-piao-de-zui-da-li-run-dong-tai-2/)

#### 优化前

```python
class Solution:
    def maxProfit(self, prices: List[int]) -> int:
        #比较一般的思路，米有优化，定义动态规划的意义，找出关联方程
        n=len(prices)
        if n==0:
            return 0
        dp=[0 for i in range(n)]
        for i in range(1,n):
            dp[i]=max(prices[i]-min(prices[:i]),dp[i-1])
        return dp[n-1]

```





因为这个java，没有这个切片的功能，所以说用一个值来储存过程当中最小值

#### 基本优化

使用一个最小值来存储，可以优化

```python
class Solution:
    def maxProfit(self, prices: List[int]) -> int:
        #比较一般的思路，米有优化，定义动态规划的意义，找出关联方程
        mined=float("inf")#存储前i个数据最小值
        n=len(prices)
        if n==0:
            return 0
        dp=[0 for i in range(n)]
        for i in range(1,n):
            mined=prices[i-1] if prices[i-1]<mined else mined
            dp[i]=max(prices[i]-mined,dp[i-1])

        return dp[n-1]

```

```java
class Solution {
    public int maxProfit(int[] prices) {
        int mined = Integer.MAX_VALUE;
        int n=prices.length;
        if(n==0){
            return 0;
        }
        int[] dp =new int[n];
        for(int i=1;i<n;i++){
            mined=prices[i-1]<mined?prices[i-1]:mined;
            dp[i]=Math.max(dp[i-1],prices[i]-mined);
        }
        return dp[n-1];

    }
}
```

#### 更好地优化

```java
class Solution {
    public int maxProfit(int[] prices) {
        int mined = Integer.MAX_VALUE;
        int n=prices.length;
        if(n==0){
            return 0;
        }
        //int[] dp =new int[n];不需要表格
        int profit=0;
        for(int i=1;i<n;i++){
            mined=prices[i-1]<mined?prices[i-1]:mined;
            profit=Math.max(profit,prices[i]-mined);//profit直接迭代就可以代表前天和今天的最好利润
        }
        return profit;

    }
}
```



### 节点能否相遇的问题



#### 就是最朴素的想法 ，如果不相等就同步下一个，同时判断当前值是不是null,最后一定会跳出循环知道吗，因为即使不相同也会一起到达末尾null 就会相等

```python
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None

class Solution:
    def getIntersectionNode(self, headA: ListNode, headB: ListNode) -> ListNode:
        #知道思路，但是知道怎么具体的写对了吗，需要考虑这个不想交的时候应该返回什么
        dummyA=headA
        dummyB=headB
        #返回的是节点
        while headA!=headB:
            if headA:
                headA=headA.next
            else:
                headA=dummyB
            if headB:
                headB=headB.next
            else:
                headB=dummyA
        return headA
```





### 从上到下之字形打印二叉树

用python的[]来做局限性还是比较忙明显的，因为需要使用pop(0) 时间复杂度为$O(N)$

可以改进使用特定的双端队列，collections.deque ,有方法popleft 和pop从队首和队尾来进行数据弹出的功能，以及appendleft和append的功能问题

```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution:
    def levelOrder(self, root: TreeNode) -> List[List[int]]:
        #用bfs,然后再用一个标志位逆转过来就可以
        res=[]#保存最终结果
        if not root:
            return res
        flag=1#代表
        stack=[root]#来保存每一行的节点
        while stack:
            n=len(stack)
            tem=[]#保存每一行的节点值
            for i in range(n):
                node=stack.pop(0)
                tem.append(node.val)
                if node.left:
                    stack.append(node.left)
                if node.right:
                    stack.append(node.right)
            if flag==1:
                res.append(tem)
            if flag==-1:
                res.append(tem[::-1])
            flag*=-1
        return res
            
                
        
```

#### 改进后的结果分析（其实没有太大的提升）

```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

# 使用这个collections.deque()
# 判断奇数还是偶数就使用这个res存储的数据的大小就可以
# 这样节点入栈的顺序不变，但是中间值的存储顺序可以根据奇数偶数来左边添加还是右边添加

class Solution:
    def levelOrder(self, root: TreeNode) -> List[List[int]]:
        res=[]
        if not root:
            return res
        stack=collections.deque()
        stack.append(root)
        while stack:
            m=len(res)
            n=len(stack)
            tem=collections.deque()
            for i in range(n):
                node=stack.popleft()
                if m%2==0:
                    tem.append(node.val)
                else:
                    tem.appendleft(node.val)
                if node.left:
                    stack.append(node.left)
                if node.right:
                    stack.append(node.right)
            res.append(list(tem))
        return res




```

#### 问题是java怎样模仿呢？如何使用java来 进行双端队列的添加

Java 中将链表 LinkedList 作为双端队列使用

[具体的API](https://www.liaoxuefeng.com/wiki/1252599548343744/1265121791832960)

poll offer size()

```java
Queue<TreeNode> queue = new LinkedList<>();
queue.poll()//具有这个抛出末尾数据的特点
```

##### LinkedList   当做比较好用的列表

addLast，addFirst这个api 也是比较好用

|                                                |                                                              |
| ---------------------------------------------- | :----------------------------------------------------------: |
| public boolean add(E e)                        | 链表末尾添加元素，返回是否成功，成功为 true，失败为 false。  |
| public void add(int index, E element)          |                     向指定位置插入元素。                     |
| public boolean addAll(Collection c)            | 将一个集合的所有元素添加到链表后面，返回是否成功，成功为 true，失败为 false。 |
| public boolean addAll(int index, Collection c) | 将一个集合的所有元素添加到链表的指定位置后面，返回是否成功，成功为 true，失败为 false。 |
| public void addFirst(E e)                      |                       元素添加到头部。                       |
| public void addLast(E e)                       |                       元素添加到尾部。                       |
| public boolean offer(E e)                      | 向链表末尾添加元素，返回是否成功，成功为 true，失败为 false。 |
| public boolean offerFirst(E e)                 |   头部插入元素，返回是否成功，成功为 true，失败为 false。    |
| public boolean offerLast(E e)                  |   尾部插入元素，返回是否成功，成功为 true，失败为 false。    |
| public void clear()                            |                          清空链表。                          |
| public E removeFirst()                         |                    删除并返回第一个元素。                    |
| public E removeLast()                          |                   删除并返回最后一个元素。                   |
| public boolean remove(Object o)                |   删除某一元素，返回是否成功，成功为 true，失败为 false。    |
| public E remove(int index)                     |                     删除指定位置的元素。                     |
| public E poll()                                |                    删除并返回第一个元素。                    |
| public E remove()                              |                    删除并返回第一个元素。                    |
| public boolean contains(Object o)              |                    判断是否含有某一元素。                    |
| public E get(int index)                        |                     返回指定位置的元素。                     |
| public E getFirst()                            |                       返回第一个元素。                       |
| public E getLast()                             |                      返回最后一个元素。                      |
| public int indexOf(Object o)                   |            查找指定元素从前往后第一次出现的索引。            |
| public int lastIndexOf(Object o)               |               查找指定元素最后一次出现的索引。               |
| public E peek()                                |                       返回第一个元素。                       |
| public E element()                             |                       返回第一个元素。                       |
| public E peekFirst()                           |                        返回头部元素。                        |
| public E peekLast()                            |                        返回尾部元素。                        |
| public E set(int index, E element)             |                     设置指定位置的元素。                     |
| public Object clone()                          |                         克隆该列表。                         |
| public Iterator descendingIterator()           |                       返回倒序迭代器。                       |
| public int size()                              |                      返回链表元素个数。                      |
| public ListIterator listIterator(int index)    |              返回从指定位置开始到末尾的迭代器。              |
| public Object[] toArray()                      |                返回一个由链表元素组成的数组。                |
| public T[] toArray(T[] a)                      |            返回一个由链表元素转换类型而成的数组。            |

```JAVA
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
        //首先你看返回的是List的结构，所以来讲需要进行具体的学习和分析
        List<List<Integer>> res = new ArrayList<>();//存储结果
        Queue<TreeNode> stack = new LinkedList<>();//存储节点，这样比较有优势
        if(root!=null){
            stack.add(root);
        }
        while(stack.isEmpty()!=true){
            int n=stack.size(),m=res.size();
            LinkedList<Integer> tem=new LinkedList<>();//保存每一行的数据,注意linkedlist 相当于【】有很多骚操作
            while(n>0){
                TreeNode node=stack.poll();//首部数据
                if(m%2==0){
                    tem.addLast(node.val);
                }
                else{
                    tem.addFirst(node.val);
                }
                if(node.left!=null){
                    stack.offer(node.left);
                }
                if(node.right!=null){
                    stack.offer(node.right);
                }
                n--;
            }
            res.add(tem);
        }
        return res;
        


    }
}
```



### 构建乘积数组

#### 暴力解法超出时间限制

###### python版暴力法

```python
class Solution:
    def constructArr(self, a: List[int]) -> List[int]:
        res=[1 for _ in range(len(a))]
        for i in range(len(res)):
            left,right=1,1
            for j in range(i):
                left*=a[j]
            for j in range(i+1,len(res)):
                right*=a[j]
            res[i]=left*right
        return res
```

###### java暴力

```java
class Solution {
    public int[] constructArr(int[] a) {

        int[] res = new int[a.length];
        for(int i=0;i<a.length;i++){
            int left=1,right=1;
            for(int j=0;j<i;j++){
                left*=a[j];
            }
            for(int j=i+1;j<a.length;j++){
                right*=a[j];
            }
            res[i]=left*right;
        }
        return res;

    }
}
```



#### 使用两个数组来存储每一个数据左右的乘积就可以了

```java
class Solution {
    public int[] constructArr(int[] a) {
       if(a.length==0){
           return a;
       }
       int[] res=new int[a.length];
       int[] left=new int[a.length];
       int[] right=new int[a.length];
       
       left[0]=1;
       right[a.length-1]=1;
       for(int i=1;i<a.length;i++){
           left[i]=left[i-1]*a[i-1];
       }
       for(int i=a.length-2;i>=0;i--){
           right[i]=right[i+1]*a[i+1];
       }
       for(int i=0;i<a.length;i++){
           res[i]=left[i]*right[i];

       }
       return res;

    }
}
```



#### 使用更少的存储空间来存储左边乘积和右边乘积

[题解思路](https://leetcode-cn.com/problems/gou-jian-cheng-ji-shu-zu-lcof/solution/zhong-yu-gao-dong-liao-xie-yi-ge-si-lu-gei-ni-men-/)

```java
class Solution {
    public int[] constructArr(int[] a) {
       if(a.length==0){
           return a;
       }
       int[] res=new int[a.length];//只用一个数组，b数组本身可以代表左侧数组c
        //再用temp变量记录右侧的值，没算出一个值，就和左侧的相乘，也就省去了右侧的数组
        res[0]=1;
        //第一个循环res[i]是代表求解i位置左侧的数的乘积
        for(int i=1;i<a.length;i++){
            res[i]=res[i-1]*a[i-1];
        }
        //下边用一个temp来存储右边的数组，因为是不断地进行累乘法
        int temp=1;
        for(int i=a.length-2;i>=0;i--){
            temp*=a[i+1];
            res[i]*=temp;
        }       
       return res;

    }
}
```





### 求解具体的中位数



![image-20201222161815934](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20201222161815934.png)

[思路](https://leetcode-cn.com/problems/shu-ju-liu-zhong-de-zhong-wei-shu-lcof/solution/mian-shi-ti-41-shu-ju-liu-zhong-de-zhong-wei-shu-y/)



```java
class MedianFinder {
    Queue<Integer> A, B;//首先定义大根堆与小根堆，规定大根堆存的数比小根堆的多一个或者相等，另外相反也可以，待会儿试试
    public MedianFinder() {
        //A当做大根堆 B当做小根堆
        A=new PriorityQueue<>();//默认的是小根堆
        B=new PriorityQueue<>((x,y)->(y-x));//进行lambda表达式就是转化成大根堆,后边的数比前边的数据大
        //初始化
       
    }

    //一句话，要想给那个数加入数据，那么就需要现在另外一个堆当中插入，再弄出头部元素，否则直接插会破坏顺序
    public void addNum(int num) {
        if(A.size()==B.size()){//保证A数据数量大于B数据数量
            B.add(num);//num可能在小的那一个部分，因此需要先放到B中,顶部放在其中
            A.add(B.poll());
        }
        else{
            A.add(num);//保证这个数一定是去了大根堆
            B.add(A.poll());

        }
        
    }
    public double findMedian() {
        return  A.size()!=B.size()?A.peek():(A.peek()+B.peek())/2.0;
       
    }
}





```

因为python 没有办法使用大根堆，所以将数据push进去的方法就是放入数据的复数问题

```python
from heapq import *

class MedianFinder:
    def __init__(self):
        self.A = [] # 小顶堆，保存较大的一半
        self.B = [] # 大顶堆，保存较小的一半

    def addNum(self, num: int) -> None:
        if len(self.A) != len(self.B):
            heappush(self.A, num)
            heappush(self.B, -heappop(self.A))
        else:
            heappush(self.B, -num)
            heappush(self.A, -heappop(self.B))

    def findMedian(self) -> float:
        return self.A[0] if len(self.A) != len(self.B) else (self.A[0] - self.B[0]) / 2.0

```

### 回溯全排列

比较有计算量的数据来了

```python 
/*
 * 回溯法
 *
 * 字符串的排列和数字的排列都属于回溯的经典问题
 *
 * 回溯算法框架：解决一个问题，实际上就是一个决策树的遍历过程：
 * 1. 路径：做出的选择
 * 2. 选择列表：当前可以做的选择
 * 3. 结束条件：到达决策树底层，无法再做选择的条件
 *
 * 伪代码：
 * result = []
 * def backtrack(路径，选择列表):
 *     if 满足结束条件：
 *         result.add(路径)
 *         return
 *     for 选择 in 选择列表:
 *         做选择
 *         backtrack(路径，选择列表)
 *         撤销选择
 *
 * 核心是for循环中的递归，在递归调用之前“做选择”，
 * 在递归调用之后“撤销选择”。
 *
 * 字符串的排列可以抽象为一棵决策树：
 *                       [ ]
 *          [a]          [b]         [c]
 *      [ab]   [ac]  [bc]   [ba]  [ca]  [cb]
 *     [abc]  [acb] [bca]  [bac]  [cab] [cba]
 *
 * 考虑字符重复情况：
 *                       [ ]
 *          [a]          [a]         [c]
 *      [aa]   [ac]  [ac]   [aa]  [ca]  [ca]
 *     [aac]  [aca] [aca]  [aac]  [caa] [caa]
 *
 * 字符串在做排列时，等于从a字符开始，对决策树进行遍历，
 * "a"就是路径，"b""c"是"a"的选择列表，"ab"和"ac"就是做出的选择，
 * “结束条件”是遍历到树的底层，此处为选择列表为空。
 *
 * 本题定义backtrack函数像一个指针，在树上遍历，
 * 同时维护每个点的属性，每当走到树的底层，其“路径”就是一个全排列。
 * 当字符出现重复，且重复位置不一定时，需要先对字符串进行排序，
 * 再对字符串进行“去重”处理，之后按照回溯框架即可。
 * */


```

#### 解法一 切片

```python
class Solution:
    def permutation(self, s: str) -> List[str]:
        self.res = []#存储结果，需要的是一个全局变量
        n = len(s)#其实这一句没有什么用

        def backtrack(s, path):
            if not s:#如果没有需要排列的了，就应该退出添加
                self.res.append(path)
            seen = set()#为了节省重复的，这是在第一层要处理的，每一个数据都需要进行遍历到
            for i in range(len(s)):#遍历重复当前的字符
                if s[i] in seen: continue
                seen.add(s[i])
                backtrack(s[:i]+s[i+1:], path + s[i])#去掉这当前的字母

        backtrack(s, "")
        return self.res
```

[解题思路](https://leetcode-cn.com/problems/zi-fu-chuan-de-pai-lie-lcof/solution/mian-shi-ti-38-zi-fu-chuan-de-pai-lie-hui-su-fa-by/)

```python
class Solution:
    def permutation(self, s: str) -> List[str]:
        n=len(s)
        self.res=[]
        c=list(s)#转成字符数组
        #dfs(x) 当前x位置的值和后面的值进行替换全排列
        def dfs(x):
            if x==n-1:
                self.res.append(''.join(c))
                return 
            seen=set()
            for i in range(x,n):
                if c[i] in seen:
                    continue
                seen.add(c[i])
                c[i],c[x]=c[x],c[i]
                dfs(x+1)
                c[i],c[x]=c[i],c[x]
        dfs(0)
        return self.res
```

```java
class Solution {
    List<String> res = new LinkedList<>();//结果需要的是字符数组,但是为了比较方便，那么就应该使用集合string
    char[] c;//需要把字符串转换成字符数组
    public String[] permutation(String s) {
        c = s.toCharArray();
        dfs(0);
        return res.toArray(new String[res.size()]);//集合转字符数组 toArray
    }
    void dfs(int x) {
        if(x == c.length - 1) {
            res.add(String.valueOf(c)); // 添加排列方案,增加的
            return;
        }
        HashSet<Character> set = new HashSet<>();
        for(int i = x; i < c.length; i++) {
            if(set.contains(c[i])) continue; // 重复，因此剪枝，使用的是contain
            set.add(c[i]);
            swap(i, x); // 交换，将 c[i] 固定在第 x 位 
            dfs(x + 1); // 开启固定第 x + 1 位字符
            swap(i, x); // 恢复交换
        }
    }
    void swap(int a, int b) {
        char tmp = c[a];
        c[a] = c[b];
        c[b] = tmp;
    }
}


```