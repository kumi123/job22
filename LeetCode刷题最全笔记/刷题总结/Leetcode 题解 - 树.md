---

title: 二叉树
thumbnail: true
author: Kumi
date: 2020-6-28 22:20:51
icons: [fas fa-fire red, fas fa-star green]
cover: true
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN/7.jpg
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
# Leetcode 题解 - 树
<!-- GFM-TOC -->
* [Leetcode 题解 - 树](#leetcode-题解---树)
    * [递归](#递归)
        * [1. 树的高度](#1-树的高度)
        * [2. 平衡树](#2-平衡树)
        * [3. 两节点的最长路径](#3-两节点的最长路径)
        * [4. 翻转树](#4-翻转树)
        * [5. 归并两棵树](#5-归并两棵树)
        * [6. 判断路径和是否等于一个数](#6-判断路径和是否等于一个数)
        * [7. 统计路径和等于一个数的路径数量](#7-统计路径和等于一个数的路径数量)
        * [8. 子树](#8-子树)
        * [9. 树的对称](#9-树的对称)
        * [10. 最小路径](#10-最小路径)
        * [11. 统计左叶子节点的和](#11-统计左叶子节点的和)
        * [12. 相同节点值的最大路径长度](#12-相同节点值的最大路径长度)
        * [13. 间隔遍历](#13-间隔遍历)
        * [14. 找出二叉树中第二小的节点](#14-找出二叉树中第二小的节点)
    * [层次遍历](#层次遍历)
        * [1. 一棵树每层节点的平均数](#1-一棵树每层节点的平均数)
        * [2. 得到左下角的节点](#2-得到左下角的节点)
    * [前中后序遍历](#前中后序遍历)
        * [1. 非递归实现二叉树的前序遍历](#1-非递归实现二叉树的前序遍历)
        * [2. 非递归实现二叉树的后序遍历](#2-非递归实现二叉树的后序遍历)
        * [3. 非递归实现二叉树的中序遍历](#3-非递归实现二叉树的中序遍历)
    * [BST](#bst)
        * [1. 修剪二叉查找树](#1-修剪二叉查找树)
        * [2. 寻找二叉查找树的第 k 个元素](#2-寻找二叉查找树的第-k-个元素)
        * [3. 把二叉查找树每个节点的值都加上比它大的节点的值](#3-把二叉查找树每个节点的值都加上比它大的节点的值)
        * [4. 二叉查找树的最近公共祖先](#4-二叉查找树的最近公共祖先)
        * [5. 二叉树的最近公共祖先](#5-二叉树的最近公共祖先)
        * [6. 从有序数组中构造二叉查找树](#6-从有序数组中构造二叉查找树)
        * [7. 根据有序链表构造平衡的二叉查找树](#7-根据有序链表构造平衡的二叉查找树)
        * [8. 在二叉查找树中寻找两个节点，使它们的和为一个给定值](#8-在二叉查找树中寻找两个节点，使它们的和为一个给定值)
        * [9. 在二叉查找树中查找两个节点之差的最小绝对值](#9-在二叉查找树中查找两个节点之差的最小绝对值)
        * [10. 寻找二叉查找树中出现次数最多的值](#10-寻找二叉查找树中出现次数最多的值)
    * [Trie](#trie)
        * [1. 实现一个 Trie](#1-实现一个-trie)
        * [2. 实现一个 Trie，用来求前缀和](#2-实现一个-trie，用来求前缀和)
<!-- GFM-TOC -->


## 递归

一棵树要么是空树，要么有两个指针，每个指针指向一棵树。树是一种递归结构，很多树的问题可以使用递归来处理。

### 1. 树的高度

104\. Maximum Depth of Binary Tree (Easy)

[Leetcode](https://leetcode.com/problems/maximum-depth-of-binary-tree/description/) / [力扣](https://leetcode-cn.com/problems/maximum-depth-of-binary-tree/description/)

```java
public int maxDepth(TreeNode root) {
    if (root == null) return 0;
    return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
}
```

### 2. 平衡树

110\. Balanced Binary Tree (Easy)

[Leetcode](https://leetcode.com/problems/balanced-binary-tree/description/) / [力扣](https://leetcode-cn.com/problems/balanced-binary-tree/description/)

```html
    3
   / \
  9  20
    /  \
   15   7
```

平衡树左右子树高度差都小于等于 1

#### 自己写的
```java
class Solution {
    public boolean isBalanced(TreeNode root) {
        if(root==null) return true;
        return Math.abs(dfs(root.left)-dfs(root.right))<=1&&isBalanced(root.left)&&isBalanced(root.right);//当前节点为平衡左右子树为平衡那么就是平衡


    }
    public int dfs(TreeNode root){
        if(root==null) return 0;
        return 1+Math.max(dfs(root.left),dfs(root.right));
    }
}
```


```java
private boolean result = true;

public boolean isBalanced(TreeNode root) {
    maxDepth(root);
    return result;
}

public int maxDepth(TreeNode root) {
    if (root == null) return 0;
    int l = maxDepth(root.left);
    int r = maxDepth(root.right);
    if (Math.abs(l - r) > 1) result = false;
    return 1 + Math.max(l, r);
}
```

### 3. 两节点的最长路径

543\. Diameter of Binary Tree (Easy)

[Leetcode](https://leetcode.com/problems/diameter-of-binary-tree/description/) / [力扣](https://leetcode-cn.com/problems/diameter-of-binary-tree/description/)

#### 思路在注解里边

```java
class Solution {
    private int maxed=0;
    public int diameterOfBinaryTree(TreeNode root) {
        //在这里需要注意一个点，就是最长的路径它肯定不一定过root，但是肯定是其中某一个节点的为头结点的结果
        //因此需要进行遍历，深度优先递归就完事儿
        //因此需要一个全局变量来进行比较通过每一个节点的最大直径
        //最大直径就是比较就可以
        dfs(root);
        return maxed;


    }
    public int dfs(TreeNode root){//是求以root为根的最大的高度
        if(root==null) return 0;
        int l=dfs(root.left);
        int r=dfs(root.right);
        maxed=Math.max(maxed,r+l);//1+r+l代表着这个以root为根的最大直径，因为是个数减一所以应该是(l+r+1)-1
        return 1+Math.max(l,r);


    }
}




```





```html
Input:

         1
        / \
       2  3
      / \
     4   5

Return 3, which is the length of the path [4,2,1,3] or [5,2,1,3].
```

```java
private int max = 0;

public int diameterOfBinaryTree(TreeNode root) {
    depth(root);
    return max;
}

private int depth(TreeNode root) {
    if (root == null) return 0;
    int leftDepth = depth(root.left);
    int rightDepth = depth(root.right);
    max = Math.max(max, leftDepth + rightDepth);
    return Math.max(leftDepth, rightDepth) + 1;
}
```

### ==4. 翻转树==

226\. Invert Binary Tree (Easy)

[Leetcode](https://leetcode.com/problems/invert-binary-tree/description/) / [力扣](https://leetcode-cn.com/problems/invert-binary-tree/description/)



### 自己的思路核方法

```java
class Solution {
    public TreeNode invertTree(TreeNode root) {
        //问题就是使用这个函数来进行翻转二叉树，返回值肯定还是本身
        //想想翻转的操作，就是把翻转之后的右子树放到左子树上，把旋转后的左子树放到右子树
        //这时候赋值的话需要有一个过渡值
        if(root==null) return root;
        TreeNode le=root.left; //如果是python那么可以直接两个赋值
        root.left=invertTree(root.right);
        root.right=invertTree(le);
        return root;


    }
}
```



#### python更简单

```python
class Solution:
    def invertTree(self, root: TreeNode) -> TreeNode:
        if root==None:
            return root
        root.left,root.right=self.invertTree(root.right),self.invertTree(root.left)
        return root
```



```java
public TreeNode invertTree(TreeNode root) {
    if (root == null) return null;
    TreeNode left = root.left;  // 后面的操作会改变 left 指针，因此先保存下来
    root.left = invertTree(root.right);
    root.right = invertTree(left);
    return root;
}
```

### ==5. 归并两棵树==

617\. Merge Two Binary Trees (Easy)

[Leetcode](https://leetcode.com/problems/merge-two-binary-trees/description/) / [力扣](https://leetcode-cn.com/problems/merge-two-binary-trees/description/)



### 自己的代码

```java
class Solution {
    public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        //这个题的难点在于同时遍历两棵树？其实前序后续中序都可以
        //在每一个节点遍历的时候，如果两个都有值，那么加起来，如果其他就再弄，按照这个题目规矩弄下来
        //这里可以按照要求重新建一个节点 或者以t1或者t2的节点为骨架网上搭建就可以
        //遍历的话，当前遍历就是改变当前节点
        //往下遍历就是  左子树=递归(树1左子树，树2左子树) 右子树=递归(树1右子树，树2左子树)))
        //之前一直想不通如何同时遍历两棵树，两个树头节点一起进去，每一次都左或者都右就可以

        if(t1==null||t2==null) return t1=t1==null?t2:t1;//每一个都应该返回一个值，和整体函数值应该一致
        t1.val+=t2.val;
        //以上是规律
        t1.left=mergeTrees(t1.left,t2.left);
        t2.right=mergeTrees(t1.right,t2.right);
        return t1;

    }
}
//新建一个节点

class Solution {
    public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        if(t1==null || t2==null){
            return t1=t1!=null?t1:t2;
        }
        TreeNode root=new TreeNode(t1.val+t2.val);
        root.left=mergeTrees(t1.left,t2.left);
        root.right=mergeTrees(t1.right,t2.right);
        return root;

    }
}

```







```html
Input:
       Tree 1                     Tree 2
          1                         2
         / \                       / \
        3   2                     1   3
       /                           \   \
      5                             4   7

Output:
         3
        / \
       4   5
      / \   \
     5   4   7
```

```java
public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
    if (t1 == null && t2 == null) return null;
    if (t1 == null) return t2;
    if (t2 == null) return t1;
    TreeNode root = new TreeNode(t1.val + t2.val);
    root.left = mergeTrees(t1.left, t2.left);
    root.right = mergeTrees(t1.right, t2.right);
    return root;
}
```

### 6. 判断路径和是否等于一个数

Leetcdoe : 112. Path Sum (Easy)

[Leetcode](https://leetcode.com/problems/path-sum/description/) / [力扣](https://leetcode-cn.com/problems/path-sum/description/)

```html
Given the below binary tree and sum = 22,

              5
             / \
            4   8
           /   / \
          11  13  4
         /  \      \
        7    2      1

return true, as there exist a root-to-leaf path 5->4->11->2 which sum is 22.
```

路径和定义为从 root 到 leaf 的所有节点的和。





```java
class Solution {
    //首先序列化这个值
    //private int flag;
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



```java
public boolean hasPathSum(TreeNode root, int sum) {
    if (root == null) return false;
    if (root.left == null && root.right == null && root.val == sum) return true;
    return hasPathSum(root.left, sum - root.val) || hasPathSum(root.right, sum - root.val);
}
```

### 7. 统计路径和等于一个数的路径数量

437\. Path Sum III (Easy)

[Leetcode](https://leetcode.com/problems/path-sum-iii/description/) / [力扣](https://leetcode-cn.com/problems/path-sum-iii/description/)

```html
root = [10,5,-3,3,2,null,11,3,-2,null,1], sum = 8

      10
     /  \
    5   -3
   / \    \
  3   2   11
 / \   \
3  -2   1

Return 3. The paths that sum to 8 are:

1.  5 -> 3
2.  5 -> 2 -> 1
3. -3 -> 11
```

路径不一定以 root 开头，也不一定以 leaf 结尾，但是必须连续。

```java
public int pathSum(TreeNode root, int sum) {
    if (root == null) return 0;
    int ret = pathSumStartWithRoot(root, sum) + pathSum(root.left, sum) + pathSum(root.right, sum);
    return ret;
}

private int pathSumStartWithRoot(TreeNode root, int sum) {
    if (root == null) return 0;
    int ret = 0;
    if (root.val == sum) ret++;
    ret += pathSumStartWithRoot(root.left, sum - root.val) + pathSumStartWithRoot(root.right, sum - root.val);
    return ret;
}
```

### 8. ==子树==

572\. Subtree of Another Tree (Easy)

[Leetcode](https://leetcode.com/problems/subtree-of-another-tree/description/) / [力扣](https://leetcode-cn.com/problems/subtree-of-another-tree/description/)

#### 自己的想法问题



### 注意人家比较的是最后的部分，大树中间有和小树一致的结构不被认定

```java
class Solution {
    public boolean isSubtree(TreeNode s, TreeNode t) {
        //分两种情况，一种是s和t值相同,直接对比，分情况，然后递归。一种是递归看s的左子树和右子树当中是否存在t子树，
        
        //这个是单节点单独分析啊，相当于先看看结构对不对
        if(s==null&&t==null) return true;
        else if(s==null||t==null) return false;
        //这是其他情况分析
        return isSame(s,t)||isSubtree(s.left,t)||isSubtree(s.right,t);

    }
    //这个是看两颗树是否相同，同时遍历就可以，应该比较的是这个相同位置的节点值相同哇，并且节点结构需要一致
    public boolean isSame(TreeNode s,TreeNode t){
        //这是确保节点的结构一致，保证下边比较值的时不为空指针
        if(s==null&&t==null) return true;
        else if(s==null||t==null) return false;
        //节点都不为空,如果当前节点不相同，直接不用往下走了，为false
        else if(s.val!=t.val) return false;
        else{
            return isSame(s.left,t.left)&&isSame(s.right,t.right);
        }

    }
}
```



```html
Given tree s:
     3
    / \
   4   5
  / \
 1   2

Given tree t:
   4
  / \
 1   2

Return true, because t has the same structure and node values with a subtree of s.

Given tree s:

     3
    / \
   4   5
  / \
 1   2
    /
   0

Given tree t:
   4
  / \
 1   2

Return false.
```

```java
public boolean isSubtree(TreeNode s, TreeNode t) {
    if (s == null) return false;
    return isSubtreeWithRoot(s, t) || isSubtree(s.left, t) || isSubtree(s.right, t);
}

private boolean isSubtreeWithRoot(TreeNode s, TreeNode t) {
    if (t == null && s == null) return true;
    if (t == null || s == null) return false;
    if (t.val != s.val) return false;
    return isSubtreeWithRoot(s.left, t.left) && isSubtreeWithRoot(s.right, t.right);
}
```

### 9==. 树的对称==

101\. Symmetric Tree (Easy)

[Leetcode](https://leetcode.com/problems/symmetric-tree/description/) / [力扣](https://leetcode-cn.com/problems/symmetric-tree/description/)



```java
class Solution {
    public boolean isSymmetric(TreeNode root) {
        //其实就是一个简单的递归就可以，但是还是不是很明白，确实有一点难度在里边
        //重点在于：应该是对应节点相同条件下，他的左节点应该和对应节点右节点相同，他的右节点应该和对应节点的左节点相同。这样不断地递归才可以好不好,所以需要有两个参数的节点
        if(root==null) return true;
        return isSymmetricSub(root.left,root.right);
    }
    //为了判定这两个子树是否对称,即他的左节点应该和对应节点右节点相同，他的右节点应该和对应节点的左节点相同，同时保证这两个节点是同等地位就可以
    public boolean isSymmetricSub(TreeNode a,TreeNode b){
        if(a==null&&b==null) return true;
        else if(a==null||b==null) return false;
        else if(a.val!=b.val) return false;
        else{
            return isSymmetricSub(a.left,b.right)&&isSymmetricSub(a.right,b.left);
        }
    }
}
```



```html
    1
   / \
  2   2
 / \ / \
3  4 4  3
```

```java
public boolean isSymmetric(TreeNode root) {
    if (root == null) return true;
    return isSymmetric(root.left, root.right);
}

private boolean isSymmetric(TreeNode t1, TreeNode t2) {
    if (t1 == null && t2 == null) return true;
    if (t1 == null || t2 == null) return false;
    if (t1.val != t2.val) return false;
    return isSymmetric(t1.left, t2.right) && isSymmetric(t1.right, t2.left);
}
```

### 10. 最小路径

111\. Minimum Depth of Binary Tree (Easy)

[Leetcode](https://leetcode.com/problems/minimum-depth-of-binary-tree/description/) / [力扣](https://leetcode-cn.com/problems/minimum-depth-of-binary-tree/description/)

#### 自己的想法(推荐)

```java
class Solution {
    private int mined=Integer.MAX_VALUE;
    public int minDepth(TreeNode root) {
        //目前的思路是遍历到叶子结点的时候，更新最小值，，应该是遍历左右子树，应该有一个中间变量来保存长度
        if(root==null) return 0;//注意要看如果为空的情况
        int count=0;//初始值应该为0
        MinSub(root,count);
        return mined;



    }
    public void  MinSub(TreeNode root,int count){//这个函数是为了要更新最小值mined
        if(root==null) return;//如果为空那么直接跳出
        //如果不为空那么应该加上1
        count++;
        if(root.left==null&&root.right==null){
            mined=Math.min(mined,count);
            return;//到叶子节点就没必要在走了
        }
        MinSub(root.left,count);
        MinSub(root.right,count);


    }
}
```

#### 使用左右子树

```java
class Solution {
    //private int mined=Integer.MAX_VALUE;
    public int minDepth(TreeNode node) {

        //还有一种想法，这个minDepth是求任意一个点到叶子节点的最小值
        //很自然的想到应该和左右子树的高度来对应起来对吧，不应该就是哪一个子树最小就保存就完事儿
        if(node==null) return 0;
        int left=minDepth(node.left);
        int right=minDepth(node.right);
        if (left==0||right==0) return left+right+1;//这是一个关键点,因为如果有一个节点一子树点为空另外一个子树不为空
        //就不应该还是选不存在的那个子树作为最小值，因为到不了叶子节点，所以需要加上上边这一句
        return 1+Math.min(left,right);


    }
}
```







树的根节点到叶子节点的最小路径长度

```java
public int minDepth(TreeNode root) {
    if (root == null) return 0;
    int left = minDepth(root.left);
    int right = minDepth(root.right);
    if (left == 0 || right == 0) return left + right + 1;
    return Math.min(left, right) + 1;
}
```

### 11. 统计左叶子节点的和

404\. Sum of Left Leaves (Easy)

[Leetcode](https://leetcode.com/problems/sum-of-left-leaves/description/) / [力扣](https://leetcode-cn.com/problems/sum-of-left-leaves/description/)

```html
    3
   / \
  9  20
    /  \
   15   7

There are two left leaves in the binary tree, with values 9 and 15 respectively. Return 24.
```

```java
public int sumOfLeftLeaves(TreeNode root) {
    if (root == null) return 0;
    if (isLeaf(root.left)) return root.left.val + sumOfLeftLeaves(root.right);
    return sumOfLeftLeaves(root.left) + sumOfLeftLeaves(root.right);
}

private boolean isLeaf(TreeNode node){
    if (node == null) return false;
    return node.left == null && node.right == null;
}
```

### 12. 相同节点值的最大路径长度

687\. Longest Univalue Path (Easy)

[Leetcode](https://leetcode.com/problems/longest-univalue-path/) / [力扣](https://leetcode-cn.com/problems/longest-univalue-path/)

#### [解题思路](https://leetcode-cn.com/problems/longest-univalue-path/solution/jian-dan-yi-dong-ban-by-a380922457-7/)

![image-20210205141056652](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210205141056652.png)

```java
class Solution {
    private int ans=0;
    public int longestUnivaluePath(TreeNode root) {
        //我们的思路一定是建立一个函数dfs(root)来表示从root节点出发的最大重复值路径，就是一定保证左边最大值和右边最大值只能选一个，这样才会形成递归路径，否则会形成那种虽然值相同但是形状形不成这个
        //该怎么样做呢？因为要连起来一定是当前节点值和子节点的值要相同对吧，这就有三种情况
        //1.当前节点和左节点相同，应该是1+dfs(root.left) 如果不相同 直接连不起来直接为0
        //2.右节点同上
        //3、为了求最大路径，可能是把左右节点都连起来。这样交给全局最大进行比较
        if(root==null) return 0;
        dfs(root);
        return ans;

    }
    public int dfs(TreeNode root){//表示表示从root节点出发的最大重复值路径，注意只能选一边。左右两边情况特殊处理
        if(root==null) return 0;
        int left=dfs(root.left);//左节点最大相同值
        int right=dfs(root.right);

        //核心代码，就是看能不能链接起来
        int left_plus=(root.left!=null&&root.val==root.left.val)?left+1:0;
        int right_plus=(root.right!=null&&root.val==root.right.val)?right+1:0;



        //这段是求每一个节点最大值比较的
        //本来是left_plus 然后right_plus 以及第三种情况比较
        //这里直接求left_plus+right_plus就可以了，因为不是0就是本身最大值
        ans=Math.max(ans,left_plus+right_plus);


        return Math.max(left_plus,right_plus);


    }
}
```



```html
             1
            / \
           4   5
          / \   \
         4   4   5

Output : 2
```

```java
private int path = 0;

public int longestUnivaluePath(TreeNode root) {
    dfs(root);
    return path;
}

private int dfs(TreeNode root){
    if (root == null) return 0;
    int left = dfs(root.left);
    int right = dfs(root.right);
    int leftPath = root.left != null && root.left.val == root.val ? left + 1 : 0;
    int rightPath = root.right != null && root.right.val == root.val ? right + 1 : 0;
    path = Math.max(path, leftPath + rightPath);
    return Math.max(leftPath, rightPath);
}
```

### 13. 间隔遍历

337\. House Robber III (Medium)

[Leetcode](https://leetcode.com/problems/house-robber-iii/description/) / [力扣](https://leetcode-cn.com/problems/house-robber-iii/description/)

#### 思路还是比较清晰的

```java
class Solution {
    public int rob(TreeNode root) {//表示从root开始间隔偷的总数,其实这就是dp那种感觉了
        //明确这个函递归函数的意义就可以
        //其实本质上就是两种偷法，一种是从头结点开始间隔偷 一种是从下一层开始间隔偷
        //选择较大的那一个就可以哇
        //主要是核心函数比较难写，应该具体的怎么样去写,写的时候最好自己画一个简单的图看看写的对不对，走一遍简单的通了，应该整体也没问题
        if(root==null) return 0;
        int option_1=root.val,option_2=0;
        //从头开始偷
        if(root.left!=null)
        option_1+=rob(root.left.left)+rob(root.left.right);
        if(root.right!=null){
        option_1+=rob(root.right.left)+rob(root.right.right);
        }
        //从下一个开始偷
        option_2=rob(root.left)+rob(root.right);//这些就是递归其他的啦
        return Math.max(option_2,option_1);
    }
}
```



```html
     3
    / \
   2   3
    \   \
     3   1
Maximum amount of money the thief can rob = 3 + 3 + 1 = 7.
```

```java
public int rob(TreeNode root) {
    if (root == null) return 0;
    int val1 = root.val;
    if (root.left != null) val1 += rob(root.left.left) + rob(root.left.right);
    if (root.right != null) val1 += rob(root.right.left) + rob(root.right.right);
    int val2 = rob(root.left) + rob(root.right);
    return Math.max(val1, val2);
}
```

### 14. 找出二叉树中第二小的节点

671\. Second Minimum Node In a Binary Tree (Easy)

[Leetcode](https://leetcode.com/problems/second-minimum-node-in-a-binary-tree/description/) / [力扣](https://leetcode-cn.com/problems/second-minimum-node-in-a-binary-tree/description/)

```html
Input:
   2
  / \
 2   5
    / \
    5  7

Output: 5
```

#### 方法一（自己想的）

```java
class Solution {
    //定义一个全体的变量,小根堆来存储
    private PriorityQueue<Integer> res=new PriorityQueue<>((e1,e2)->(e1-e2));
    public int findSecondMinimumValue(TreeNode root) {
        if(root==null) return -1;
        helper(root);
        //往外蹦然后满足条件存储就可以
        int pre=res.poll();
        while(!res.isEmpty()){
            int cur=res.poll();
            if(cur!=pre) return cur;
        }

        return -1;


    }
    public void helper(TreeNode root){
        //递归遍历就完事儿
        if(root==null) return;
        res.offer(root.val);
        helper(root.left);
        helper(root.right);
    }
}
```



#### 保存两个值

```java
   class Solution{
        //直接dfs 用两个数据保存就可以
        //每一次经过一个节点，如果小于first，那么first赋给second 新的最小值赋值给first
        //如果在之间，那么只更新second就可以，其他就算了
    private int first=Integer.MAX_VALUE,second=Integer.MAX_VALUE;
    private int count=0;//表示第二个数被更新了多少次，如果被更新了 那么可以说一定有第二小值
    public int findSecondMinimumValue(TreeNode root) {
        if(root==null) return -1;
        if(root.val<first){
            second=first;
            first=root.val;
            
        }
        else if(root.val<=second&&root.val>first){
            second=root.val;
            count++;
        }
        findSecondMinimumValue(root.left);
        findSecondMinimumValue(root.right);
        return (count>0)?second:-1;

        
 

   }
   }
```



一个节点要么具有 0 个或 2 个子节点，如果有子节点，那么根节点是最小的节点。

```java
public int findSecondMinimumValue(TreeNode root) {
    if (root == null) return -1;
    if (root.left == null && root.right == null) return -1;
    int leftVal = root.left.val;
    int rightVal = root.right.val;
    if (leftVal == root.val) leftVal = findSecondMinimumValue(root.left);
    if (rightVal == root.val) rightVal = findSecondMinimumValue(root.right);
    if (leftVal != -1 && rightVal != -1) return Math.min(leftVal, rightVal);
    if (leftVal != -1) return leftVal;
    return rightVal;
}
```

## 层次遍历

使用 BFS 进行层次遍历。不需要使用两个队列来分别存储当前层的节点和下一层的节点，因为在开始遍历一层的节点时，当前队列中的节点数就是当前层的节点数，只要控制遍历这么多节点数，就能保证这次遍历的都是当前层的节点。

### 1. 一棵树每层节点的平均数

637\. Average of Levels in Binary Tree (Easy)

[Leetcode](https://leetcode.com/problems/average-of-levels-in-binary-tree/description/) / [力扣](https://leetcode-cn.com/problems/average-of-levels-in-binary-tree/description/)



#### 自己使用双端对列

```java
class Solution {
    public List<Double> averageOfLevels(TreeNode root) {
        //一般就是层次遍历，使用这个一个队列，我还是比较习惯双端队列来弄就可以
        //从右边进，从左边出来就可以
        List<Double> res=new ArrayList<>();
        if(root==null) return res;
        Deque<TreeNode> queue=new LinkedList<>();//保存行节点
        queue.addLast(root);
        while(!queue.isEmpty()){
            int n=queue.size();
            double avg=0.0;
            for(int i=0;i<n;i++){
               TreeNode node=queue.pollFirst();
               avg+=node.val;
               
               if(node.left!=null){
                   queue.addLast(node.left);
               }
                if(node.right!=null){
                   queue.addLast(node.right);
               }
               
            }
            res.add(avg/n);

        }
        return res;

    }
}
```

#### 使用单端队列

```java
public List<Double> averageOfLevels(TreeNode root) {
    List<Double> ret = new ArrayList<>();
    if (root == null) return ret;
    Queue<TreeNode> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
        int cnt = queue.size();
        double sum = 0;
        for (int i = 0; i < cnt; i++) {
            TreeNode node = queue.poll();
            sum += node.val;
            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }
        ret.add(sum / cnt);
    }
    return ret;
}
```

### 2. 得到左下角的节点

513\. Find Bottom Left Tree Value (Easy)

[Leetcode](https://leetcode.com/problems/find-bottom-left-tree-value/description/) / [力扣](https://leetcode-cn.com/problems/find-bottom-left-tree-value/description/)



#### 自己中间加了一个东西来保存它

```java
class Solution {
    public int findBottomLeftValue(TreeNode root) {
        //感觉可以这样，层次遍历，然后存下来每一行第一个值就可以，最后就直接输出最后一个值就可以
        List<Integer> res=new ArrayList<>();
        Queue<TreeNode> queue=new LinkedList<>();//保存行节点
        queue.add(root);
        while(!queue.isEmpty()){
            int n=queue.size();
            for(int i=0;i<n;i++){
               TreeNode node=queue.poll();
               if(i==0) res.add(node.val);
               if(node.left!=null){
                   queue.add(node.left);
               }
                if(node.right!=null){
                   queue.add(node.right);
               }
               
            }

        }
        return res.get(res.size()-1);

    }
}



//第二种
class Solution {
    public int findBottomLeftValue(TreeNode root) {
        //也可以后序遍历啊啊啊，最后一个点它一定是最左边的，反过来最右边的那么就应该前序遍历
        Queue<TreeNode> queue=new LinkedList<>();//保存行节点
        queue.add(root);
        TreeNode node=new TreeNode(-1);
        while(!queue.isEmpty()){//这样可以看出来最后边的就是这个最左那
               node=queue.poll();
                if(node.right!=null){
                   queue.add(node.right);
               }  
               if(node.left!=null){
                   queue.add(node.left);
               }
        }
        return node.val;

    }
}
```



```html
Input:

        1
       / \
      2   3
     /   / \
    4   5   6
       /
      7

Output:
7
```

```java
public int findBottomLeftValue(TreeNode root) {
    Queue<TreeNode> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
        root = queue.poll();
        if (root.right != null) queue.add(root.right);
        if (root.left != null) queue.add(root.left);
    }
    return root.val;
}
```

## 前中后序遍历

```html
    1
   / \
  2   3
 / \   \
4   5   6
```

- 层次遍历顺序：[1 2 3 4 5 6]
- 前序遍历顺序：[1 2 4 5 3 6]
- 中序遍历顺序：[4 2 5 1 3 6]
- 后序遍历顺序：[4 5 2 6 3 1]

层次遍历使用 BFS 实现，利用的就是 BFS 一层一层遍历的特性；而前序、中序、后序遍历利用了 DFS 实现。

前序、中序、后序遍只是在对节点访问的顺序有一点不同，其它都相同。

① 前序

```java
void dfs(TreeNode root) {
    visit(root);
    dfs(root.left);
    dfs(root.right);
}
```

② 中序

```java
void dfs(TreeNode root) {
    dfs(root.left);
    visit(root);
    dfs(root.right);
}
```

③ 后序

```java
void dfs(TreeNode root) {
    dfs(root.left);
    dfs(root.right);
    visit(root);
}
```

### 1. 非递归实现二叉树的前序遍历

144\. Binary Tree Preorder Traversal (Medium)

[Leetcode](https://leetcode.com/problems/binary-tree-preorder-traversal/description/) / [力扣](https://leetcode-cn.com/problems/binary-tree-preorder-traversal/description/)

```java
public List<Integer> preorderTraversal(TreeNode root) {
    List<Integer> ret = new ArrayList<>();
    Stack<TreeNode> stack = new Stack<>();
    stack.push(root);
    while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        if (node == null) continue;
        ret.add(node.val);
        stack.push(node.right);  // 先右后左，保证左子树先遍历
        stack.push(node.left);
    }
    return ret;
}
```

### 2. 非递归实现二叉树的后序遍历

145\. Binary Tree Postorder Traversal (Medium)

[Leetcode](https://leetcode.com/problems/binary-tree-postorder-traversal/description/) / [力扣](https://leetcode-cn.com/problems/binary-tree-postorder-traversal/description/)

前序遍历为 root -\> left -\> right，后序遍历为 left -\> right -\> root。可以修改前序遍历成为 root -\> right -\> left，那么这个顺序就和后序遍历正好相反。

```java
public List<Integer> postorderTraversal(TreeNode root) {
    List<Integer> ret = new ArrayList<>();
    Stack<TreeNode> stack = new Stack<>();
    stack.push(root);
    while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        if (node == null) continue;
        ret.add(node.val);
        stack.push(node.left);
        stack.push(node.right);
    }
    Collections.reverse(ret);
    return ret;
}
```

### 3. 非递归实现二叉树的中序遍历

94\. Binary Tree Inorder Traversal (Medium)

[Leetcode](https://leetcode.com/problems/binary-tree-inorder-traversal/description/) / [力扣](https://leetcode-cn.com/problems/binary-tree-inorder-traversal/description/)

```java
public List<Integer> inorderTraversal(TreeNode root) {
    List<Integer> ret = new ArrayList<>();
    if (root == null) return ret;
    Stack<TreeNode> stack = new Stack<>();
    TreeNode cur = root;
    while (cur != null || !stack.isEmpty()) {
        while (cur != null) {
            stack.push(cur);
            cur = cur.left;
        }
        TreeNode node = stack.pop();
        ret.add(node.val);
        cur = node.right;
    }
    return ret;
}
```

## BST

二叉查找树（BST）：根节点大于等于左子树所有节点，小于等于右子树所有节点。

二叉查找树中序遍历有序。

### ==1. 修剪二叉查找树==

669\. Trim a Binary Search Tree (Easy)

[Leetcode](https://leetcode.com/problems/trim-a-binary-search-tree/description/) / [力扣](https://leetcode-cn.com/problems/trim-a-binary-search-tree/description/)

```html
Input:

    3
   / \
  0   4
   \
    2
   /
  1

  L = 1
  R = 3

Output:

      3
     /
   2
  /
 1
```

题目描述：只保留值在 L \~ R 之间的节点

```java
class Solution {
    public TreeNode trimBST(TreeNode root, int low, int high) {
        //思路，明确肯定是使用递归，对于一个根节点（因为是二叉搜索树）
        //如果大于最大值，肯定是要丢弃本节点，从左子树（比本节点小）开始递归，返回递归结果当做输出，
        //如果是小于根节点，那么应该是返回右子树递归的结果
        //如果是其他情况，说明就应该返回当前节点。接下来就递归左子树和右子树赋给左右节点就可以啦
        if(root==null) return null;
        if(root.val>high) return trimBST(root.left,low,high);
        if(root.val<low) return trimBST(root.right,low,high);
        root.left=trimBST(root.left,low,high);
        root.right=trimBST(root.right,low,high);
        return root;
        


    }
}

public TreeNode trimBST(TreeNode root, int L, int R) {
    if (root == null) return null;
    if (root.val > R) return trimBST(root.left, L, R);
    if (root.val < L) return trimBST(root.right, L, R);
    root.left = trimBST(root.left, L, R);
    root.right = trimBST(root.right, L, R);
    return root;
}
```

### 2. 寻找二叉查找树的第 k 个元素

230\. Kth Smallest Element in a BST (Medium)

[Leetcode](https://leetcode.com/problems/kth-smallest-element-in-a-bst/description/) / [力扣](https://leetcode-cn.com/problems/kth-smallest-element-in-a-bst/description/)

中序遍历解法：

注意还是用一个count来弄比较合适

```java
private int cnt = 0;
private int val;

public int kthSmallest(TreeNode root, int k) {
    inOrder(root, k);
    return val;
}

private void inOrder(TreeNode node, int k) {
    if (node == null) return;
    inOrder(node.left, k);
    cnt++;//因为每一个inOrder(node.left, k);都说明这个是最小值找到了
    if (cnt == k) {
        val = node.val;
        return;
    }
    inOrder(node.right, k);
}



// 自己写的

class Solution {
   private int count = 0;
    private int val;

public int kthSmallest(TreeNode root, int k) {
    inOrder(root, k);
    return val;
}

private void inOrder(TreeNode node, int k) {
    if (node == null) return;
    inOrder(node.left, k);
    count++;
    if (count == k) {
        val = node.val;
        return;
    }
    inOrder(node.right, k);
}


}
```

#### 先提取再排序

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
   

public int kthSmallest(TreeNode root, int k) {
    inOrder(root);
    Collections.sort(res);
    return res.get(k-1);

}

private void inOrder(TreeNode node) {
    if (node == null) return;
    inOrder(node.left);
    res.add(node.val);
    inOrder(node.right);
}


}
```





递归解法：

```java
public int kthSmallest(TreeNode root, int k) {
    int leftCnt = count(root.left);
    if (leftCnt == k - 1) return root.val;
    if (leftCnt > k - 1) return kthSmallest(root.left, k);
    return kthSmallest(root.right, k - leftCnt - 1);
}

private int count(TreeNode node) {
    if (node == null) return 0;
    return 1 + count(node.left) + count(node.right);
}
```

### 3. ==把二叉查找树每个节点的值都加上比它大的节点的值==

Convert BST to Greater Tree (Easy)

[Leetcode](https://leetcode.com/problems/convert-bst-to-greater-tree/description/) / [力扣](https://leetcode-cn.com/problems/convert-bst-to-greater-tree/description/)

```html
Input: The root of a Binary Search Tree like this:

              5
            /   \
           2     13

Output: The root of a Greater Tree like this:

             18
            /   \
          20     13
```

先遍历右子树。

```java
class Solution {
    private int count=0;
    public TreeNode convertBST(TreeNode root) {
        //感觉应该是从最右边节点开始递归，思路是对的，但是如何来进行实践呢？
        //应该是有一个数据来不断地取值，然后重新赋值就可以
        //注意所谓的累加树其实就是明确数据遍历就可以啦
        //先右节点 然后当前节点 然后左节点 注意中间值进行更新就可以
        dfs(root);
        return root;

    }
    public void dfs(TreeNode root){
        if(root==null) return;
        convertBST(root.right);
        count+=root.val;
        root.val=count;
        convertBST(root.left);
        //return root;
    }
}

















private int sum = 0;

public TreeNode convertBST(TreeNode root) {
    traver(root);
    return root;
}

private void traver(TreeNode node) {
    if (node == null) return;
    traver(node.right);
    sum += node.val;
    node.val = sum;
    traver(node.left);
}
```

### ==4. 二叉查找树的最近公共祖先==

235\. Lowest Common Ancestor of a Binary Search Tree (Easy)

[Leetcode](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/description/) / [力扣](https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-search-tree/description/)



### 满足条件才跳出

```java
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root==null) return null;
        while((root.val-p.val)*(root.val-q.val)>0){
            root=((root.val-p.val)>0)?root.left:root.right;
        }
        return root;
       

        
    }
}
```



```html
        _______6______
      /                \
  ___2__             ___8__
 /      \           /      \
0        4         7        9
        /  \
       3   5

For example, the lowest common ancestor (LCA) of nodes 2 and 8 is 6. Another example is LCA of nodes 2 and 4 is 2, since a node can be a descendant of itself according to the LCA definition.
```

```java
public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    //思路
    //递归表示从root为节点的树找到满足条件的节点
    if (root.val > p.val && root.val > q.val) return lowestCommonAncestor(root.left, p, q);//不满足，从左子树找
    if (root.val < p.val && root.val < q.val) return lowestCommonAncestor(root.right, p, q);//不满足从右子树找
    return root; //满足条件就直接输出就可
}
```

### ==5. 二叉树的最近公共祖先==

236\. Lowest Common Ancestor of a Binary Tree (Medium) 

[Leetcode](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/description/) / [力扣](https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-tree/description/)



#### 解答方法

![image-20210208131032598](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210208131032598.png)

```java
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        //普通二叉树毕竟不是搜素树，不能通过计算来看公共节点在哪个子树当中
        //但是还是可以有自己的方法
        //对于当前节点来讲，如果左子树当中存在p或者q(只能是一个)，右子树当中存在另外一个，那么直接返当前节点值
        //如果只存在一个子树当中，那么就往这一课子树进行遍历
        //如果都没有(遍历到叶子节点的话) 返回null
        if(root==null)  return null;//说明到头了,返回这个
        if(root==p||root==q) return root;//说明在当前节点为根节点的树中找了至少p和q当中至少一个，另一个黑没有找到，直接返回就完事儿
        TreeNode left=lowestCommonAncestor(root.left,p,q);
        TreeNode right=lowestCommonAncestor(root.right,p,q);
        if(left!=null&&right!=null) return root;
        else{
            return (left==null)?right:left;
        }
        
        
    }
}
```




```html
       _______3______
      /              \
  ___5__           ___1__
 /      \         /      \
6        2       0        8
        /  \
       7    4

For example, the lowest common ancestor (LCA) of nodes 5 and 1 is 3. Another example is LCA of nodes 5 and 4 is 5, since a node can be a descendant of itself according to the LCA definition.
```

```java
public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || root == p || root == q) return root;
    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);
    return left == null ? right : right == null ? left : root;
}
```

### 6. 从有序数组中构造二叉查找树

108\. Convert Sorted Array to Binary Search Tree (Easy)

[Leetcode](https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/description/) / [力扣](https://leetcode-cn.com/problems/convert-sorted-array-to-binary-search-tree/description/)

```java
public TreeNode sortedArrayToBST(int[] nums) {
    return toBST(nums, 0, nums.length - 1);
}

private TreeNode toBST(int[] nums, int sIdx, int eIdx){
    if (sIdx > eIdx) return null;
    int mIdx = (sIdx + eIdx) / 2;
    TreeNode root = new TreeNode(nums[mIdx]);
    root.left =  toBST(nums, sIdx, mIdx - 1);
    root.right = toBST(nums, mIdx + 1, eIdx);
    return root;
}
```

### 7. 根据有序链表构造平衡的二叉查找树

109\. Convert Sorted List to Binary Search Tree (Medium)

[Leetcode](https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/description/) / [力扣](https://leetcode-cn.com/problems/convert-sorted-list-to-binary-search-tree/description/)

```html
Given the sorted linked list: [-10,-3,0,5,9],

One possible answer is: [0,-3,9,-10,null,5], which represents the following height balanced BST:

      0
     / \
   -3   9
   /   /
 -10  5
```

```java
public TreeNode sortedListToBST(ListNode head) {
    if (head == null) return null;
    if (head.next == null) return new TreeNode(head.val);
    ListNode preMid = preMid(head);
    ListNode mid = preMid.next;
    preMid.next = null;  // 断开链表
    TreeNode t = new TreeNode(mid.val);
    t.left = sortedListToBST(head);
    t.right = sortedListToBST(mid.next);
    return t;
}

private ListNode preMid(ListNode head) {
    ListNode slow = head, fast = head.next;
    ListNode pre = head;
    while (fast != null && fast.next != null) {
        pre = slow;
        slow = slow.next;
        fast = fast.next.next;
    }
    return pre;
}




```

#### 方法二 链表转化成数组

```java
class Solution {
    public TreeNode sortedListToBST(ListNode head) {
        //还是转化成这个数组把
        if(head==null) return null;
        List<Integer> res=new ArrayList<>();
        while(head!=null) {
            res.add(head.val);
            head=head.next;
        }
        TreeNode root =dfs(res,0,res.size()-1);
        return root;
    }
    public TreeNode dfs(List<Integer> res,int left,int right){
        if(left>right) return null;
        int mid=left+(right-left+1)/2;
        TreeNode node=new TreeNode(res.get(mid));
        node.left=dfs(res,left,mid-1);
        node.right=dfs(res,mid+1,right);
        return node;
        
}
}
```



#### 直接进行链表的递归

```java
class Solution {
    public TreeNode sortedListToBST(ListNode head) {
        //因为本质上还是只是来找终点吗，所以和数组一样，找到链表的终点
        //然后递归两个子树就可以啦
        //通常使用快慢指针来找重点，但是需要有一个slow中点的前边点来记录，好让左子树和具体的来断开
        if(head==null) return null;
        ListNode slow=head;
        ListNode fast=head;
        ListNode pre=null;
        //寻找中点，其实就是slow
        while(fast!=null&&fast.next!=null){
            fast=fast.next.next;
            pre=slow;//先进行保存，再更新
            slow=slow.next;
        }
        TreeNode node=new  TreeNode(slow.val);
        node.right=sortedListToBST(slow.next);
        //因为左边的链表需要断开嘛，所以
        if(pre!=null){
            pre.next=null;
            node.left=sortedListToBST(head);
        }
        else node.left=null;
        return node;

      
}
}
```





### 8. 在二叉查找树中寻找两个节点，使它们的和为一个给定值

653\. Two Sum IV - Input is a BST (Easy)

[Leetcode](https://leetcode.com/problems/two-sum-iv-input-is-a-bst/description/) / [力扣](https://leetcode-cn.com/problems/two-sum-iv-input-is-a-bst/description/)



#### [思路还是比较清楚地](https://leetcode-cn.com/problems/convert-sorted-array-to-binary-search-tree/solution/tu-jie-er-cha-sou-suo-shu-gou-zao-di-gui-python-go/)

```java
class Solution {
    public TreeNode sortedArrayToBST(int[] nums) {
        //每一次都取中点，这样可以保证为平衡的树
        //然后左边右边链接该节点就可以
        if(nums.length==0||nums==null) return null;
        TreeNode root=dfs(nums,0,nums.length-1);
        return root;
    }
    public TreeNode dfs(int[] nums,int left,int right){
        if(left>right) return null;//这样当两个边界相同的时候也会有输出
        int mid=left+(right-left+1)/2;//选择右边界比较合理
        TreeNode node=new TreeNode(nums[mid]);//这个是还是不错的结果
        node.left=dfs(nums,left,mid-1);
        node.right=dfs(nums,mid+1,right);
        return node;

    }
}
```



```html
Input:

    5
   / \
  3   6
 / \   \
2   4   7

Target = 9

Output: True
```

#### 第一种思路

中序遍历得到顺序的，然后两数之和

```java
class Solution {
    List<Integer> res = new ArrayList<>();
    public boolean findTarget(TreeNode root, int k) {
        //有一种方式就可以中序遍历形成有序数组，然后使用两数之和就可以
        if(root==null) return false;
        dfs(root);
        HashMap<Integer,Integer> map=new HashMap<>();
        //所谓的hashmap 求两数之和 存的是这个值key和坐标作为value
        //为什么要存下标 ，因为防止来求的时候差找的是自己，但是并没有两个值，所以需要用下标来看看是否重复
        for(int i=0;i<res.size();i++){
            map.put(res.get(i),i);
        }
        for(int i=0;i<res.size();i++){
            int tem=k-res.get(i);
            if(map.containsKey(tem)&&map.get(tem)!=i) return true;
        }
        return false;

        
    }
    public void dfs(TreeNode root){
        if(root==null) return;
        dfs(root.left);
        res.add(root.val);
        dfs(root.right);
    }
}
```





使用中序遍历得到有序数组之后，再利用双指针对数组进行查找。

应该注意到，这一题不能用分别在左右子树两部分来处理这种思想，因为两个待求的节点可能分别在左右子树中。

```java
public boolean findTarget(TreeNode root, int k) {
    List<Integer> nums = new ArrayList<>();
    inOrder(root, nums);
    int i = 0, j = nums.size() - 1;
    while (i < j) {
        int sum = nums.get(i) + nums.get(j);
        if (sum == k) return true;
        if (sum < k) i++;
        else j--;
    }
    return false;
}

private void inOrder(TreeNode root, List<Integer> nums) {
    if (root == null) return;
    inOrder(root.left, nums);
    nums.add(root.val);
    inOrder(root.right, nums);
}
```
### 10、利用前序和中序来还原二叉树

```java
import com.google.common.primitives.Ints; 
class Solution {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        //肯定涉及到递归，以及参数应该有下标把
    }
    //因为python有那个直接分割子数组的功能，java不知道有没有
    public TreeNode Bulider(int[] preorder, int[] inorder,int preleft,int preright,int inleft,int inright){
        if(inleft==inright) return new TreeNode(preorder[0]);
        if(inleft>inright)   return null;
        TreeNode node=new TreeNode(preorder[preleft]);//根节点
        int mid=Ints.indexOf(inorder,preorder[preleft]);//找到根节点在这个中序遍历位置
        node.left=Bulider(preorder,inorder,preleft+1,preleft+mid-inleft,inleft,mid-1);
        node.right=Bulider(preorder,inorder,preleft+mid-inleft,preright,mid+1,inright);
        return node;
    }
}
```

#### 方法2

```java
class Solution {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        //肯定涉及到递归，以及参数应该有下标把
        HashMap<Integer,Integer> map=new HashMap<>();//用来标记值在当中的东西
        for(int i=0;i<inorder.length;i++){
            map.put(inorder[i],i);
        }
        return Bulider(preorder,inorder,0,preorder.length-1,0,inorder.length-1,map);
    }
    //因为python有那个直接分割子数组的功能，java不知道有没有
    //但是java可以使用这个下标来进行控制
    public TreeNode Bulider(int[] preorder, int[] inorder,int preleft,int preright,int inleft,int inright,HashMap<Integer,Integer> map){
        if(inleft>inright)   return null;
        TreeNode node=new TreeNode(preorder[preleft]);//根节点
        int mid=map.get(preorder[preleft]);//找到根节点在这个中序遍历位置，一定是根据中序的下标范围来确定前序的下标范围，注意当左右值相同的时候，也可以的，子树的就会跳出
        node.left=Bulider(preorder,inorder,preleft+1,preleft+mid-inleft,inleft,mid-1,map);
        node.right=Bulider(preorder,inorder,preleft+mid-inleft+1,preright,mid+1,inright,map);
        return node;
    }
}
```

### 利用后序和中序求二叉树

```java
class Solution {
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        //肯定涉及到递归，以及参数应该有下标把
        HashMap<Integer,Integer> map=new HashMap<>();//用来标记值在当中的东西
        for(int i=0;i<inorder.length;i++){
            map.put(inorder[i],i);
        }
        return Bulider(inorder,postorder,0,postorder.length-1,0,inorder.length-1,map);
    }
    public TreeNode Bulider(int[] inorder, int[] postorder,int postleft,int postright,int inleft,int inright,HashMap<Integer,Integer> map){
        if(inleft>inright)  return null;
        TreeNode node=new TreeNode(postorder[postright]);//根节点
        int mid=map.get(postorder[postright]);//找到根节点在这个中序遍历位置，一定是根据中序的下标范围来确定前序的下标范围，注意当左右值相同的时候，也可以的，子树的就会跳出
        node.left=Bulider(inorder,postorder,postleft,postleft+mid-inleft-1,inleft,mid-1,map);
        node.right=Bulider(inorder,postorder,postleft+mid-inleft,postright-1,mid+1,inright,map);
        return node;
    }
}
```

### 蛇形遍历



```java
class Solution {
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        //层次遍历就完事儿，加一个变量来进行判定就可以
        List<List<Integer>> res=new ArrayList<>();
        if(root==null) return res;
        Queue<TreeNode> queue=new LinkedList<>();
        queue.add(root);
        int flag=1;
        while(!queue.isEmpty()){
            int n=queue.size();
            List<Integer> arr=new ArrayList<>();
            for(int i=0;i<n;i++){
                TreeNode node=queue.poll();
                arr.add(node.val);
                if(node.left!=null) queue.add(node.left);
                if(node.right!=null) queue.add(node.right);
                }
            if(flag==-1){
                Collections.reverse(arr);
            }
            res.add(arr);
            flag*=-1;
        }
        return res;

    }
}
```

### 搜索树遍历，删除节点

```java
class Solution {
    public TreeNode deleteNode(TreeNode root, int key) {
        //这个树的东西还是比较好想的，先进行寻找这个匹配的节点
        //deletenode意义是删除以root为根节点的树当中值为key的节点
        //因为是搜索树 所以可以比较大小然后递归
        //删除的有几种情况
        //1、删除节点没有子节点 2、只有一个子节点3、有两个子节点（可以通过左旋和右旋）来进行改变
        //返回的是修改后的根节点好不好
        if(root==null) return null;//如果为空，怎么删除都是返回null
        if(root.val>key) root.left=deleteNode(root.left,key);
        if(root.val<key) root.right=deleteNode(root.right,key);
        if(root.val==key){
            if(root.left==null&&root.right==null) return null;
            else if(root.left==null&&root.right!=null) return root.right;
            else if(root.left!=null&&root.right==null) return root.left;
            else{
                //把左子树放到右子树的最左边节点上，然后用root.right来替换root就可以了
                
                //首先需要找到这个最左边的节点
                //注意需要有一个变量来进行代表最左节点，如果有root来找的话就会失去root,无法赋值
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






### ==9. 在二叉查找树中查找两个节点之差的最小绝对值==

530\. Minimum Absolute Difference in BST (Easy)

[Leetcode](https://leetcode.com/problems/minimum-absolute-difference-in-bst/description/) / [力扣](https://leetcode-cn.com/problems/minimum-absolute-difference-in-bst/description/)

```html
Input:

   1
    \
     3
    /
   2

Output:

1
```

利用二叉查找树的中序遍历为有序的性质，计算中序遍历中临近的两个节点之差的绝对值，取最小值。





##### 难点为什么要判定不为pre!=null???? 明明一开始是最小值就可以结果

```java
class Solution {
    int result=Integer.MAX_VALUE;
    //TreeNode pre=new TreeNode(Integer.MIN_VALUE);//这时候选择pre就尤为重要，因为第一个减去第0个不能影响整体最小判断，所以需要确保得到的值最大，所以使得pre初值为最小值，这样一减就变成最大，自然会被过滤到。
    TreeNode pre=null;
    public int getMinimumDifference(TreeNode root) {
        //利用中序遍历来进行比较相邻节点的差，取最小值就可以啦
        //使用递归法求中序遍历
        //因为每一次需要进行比较相邻的节点，所以需要一个变量来存储这个前一个节点用来和当前节点比较
        if(root==null) return 0;
        
        dfs(root);
        return result;


    }
    public void dfs(TreeNode root){
        if(root==null) return;
        dfs(root.left);
        if(pre!=null) result=Math.min(root.val-pre.val,result);//为什么要加上pre!=null ????
        pre=root;//把当前值当成过去值，这个是重点
        dfs(root.right);
    }

}
```



#### 当然也可以中序弄成list 然后直接进行遍历就可以

```java
private int minDiff = Integer.MAX_VALUE;
private TreeNode preNode = null;

public int getMinimumDifference(TreeNode root) {
    inOrder(root);
    return minDiff;
}

private void inOrder(TreeNode node) {
    if (node == null) return;
    inOrder(node.left);
    if (preNode != null) minDiff = Math.min(minDiff, node.val - preNode.val);
    preNode = node;
    inOrder(node.right);
}
```

### 10. 寻找二叉查找树中出现次数最多的值

501\. Find Mode in Binary Search Tree (Easy)

[Leetcode](https://leetcode.com/problems/find-mode-in-binary-search-tree/description/) / [力扣](https://leetcode-cn.com/problems/find-mode-in-binary-search-tree/description/)

```html
   1
    \
     2
    /
   2

return [2].
```

答案可能不止一个，也就是有多个值出现的次数一样多。



#### 自己的想法



```java
class Solution {
    int count=0;
    int prevalue=Integer.MIN_VALUE;
    int maxed=Integer.MIN_VALUE;
    List<Integer> res=new ArrayList<>();
    public int[] findMode(TreeNode root) {
        //思路其实比较简单，但是主要就是需要在边中序遍历的时候边进行操作
        //情况：如果当前值和上一个值不相同，那么计数值置1，如果相同置数+1 并且每一次前边赋值
        //计算完计数之后，和maxed进行比较，小于maxed 不需要管 如果等于maxed 需要加入这个reslut当中
        // 如果大于maxed，maxed 要进行赋值成为新的，那么应该吧前边的值给全部删掉，加入新的值 大概就是这样
        if(root==null) return new int[0];
        dfs(root);
        int[] out=new int[res.size()];
        for(int i=0;i<res.size();i++) {
            out[i]=res.get(i);
        }
        return out;
        

    }
    public void dfs(TreeNode root){
        if(root==null) return;
        dfs(root.left);
        int cur=root.val;
        if(cur==prevalue){
            count++;
        }
        else{
            count=1;
        }
        prevalue=cur;
        if(count==maxed){
            res.add(root.val);

        }
        else if(count>maxed){
            res.clear();
            maxed=count;
            res.add(root.val);
        }
        dfs(root.right);
    }
}
```



```java
private int curCnt = 1;
private int maxCnt = 1;
private TreeNode preNode = null;

public int[] findMode(TreeNode root) {
    List<Integer> maxCntNums = new ArrayList<>();
    inOrder(root, maxCntNums);
    int[] ret = new int[maxCntNums.size()];
    int idx = 0;
    for (int num : maxCntNums) {
        ret[idx++] = num;
    }
    return ret;
}

private void inOrder(TreeNode node, List<Integer> nums) {
    if (node == null) return;
    inOrder(node.left, nums);
    if (preNode != null) {
        if (preNode.val == node.val) curCnt++;
        else curCnt = 1;
    }
    if (curCnt > maxCnt) {
        maxCnt = curCnt;
        nums.clear();
        nums.add(node.val);
    } else if (curCnt == maxCnt) {
        nums.add(node.val);
    }
    preNode = node;
    inOrder(node.right, nums);
}
```

## Trie

<div align="center"> <img src="https://cs-notes-1256109796.cos.ap-guangzhou.myqcloud.com/5c638d59-d4ae-4ba4-ad44-80bdc30f38dd.jpg"/> </div><br>

Trie，又称前缀树或字典树，用于判断字符串是否存在或者是否具有某种字符串前缀。

### 1. 实现一个 Trie

208\. Implement Trie (Prefix Tree) (Medium)

[Leetcode](https://leetcode.com/problems/implement-trie-prefix-tree/description/) / [力扣](https://leetcode-cn.com/problems/implement-trie-prefix-tree/description/)

```java
class Trie {

    private class Node {
        Node[] childs = new Node[26];
        boolean isLeaf;
    }

    private Node root = new Node();

    public Trie() {
    }

    public void insert(String word) {
        insert(word, root);
    }

    private void insert(String word, Node node) {
        if (node == null) return;
        if (word.length() == 0) {
            node.isLeaf = true;
            return;
        }
        int index = indexForChar(word.charAt(0));
        if (node.childs[index] == null) {
            node.childs[index] = new Node();
        }
        insert(word.substring(1), node.childs[index]);
    }

    public boolean search(String word) {
        return search(word, root);
    }

    private boolean search(String word, Node node) {
        if (node == null) return false;
        if (word.length() == 0) return node.isLeaf;
        int index = indexForChar(word.charAt(0));
        return search(word.substring(1), node.childs[index]);
    }

    public boolean startsWith(String prefix) {
        return startWith(prefix, root);
    }

    private boolean startWith(String prefix, Node node) {
        if (node == null) return false;
        if (prefix.length() == 0) return true;
        int index = indexForChar(prefix.charAt(0));
        return startWith(prefix.substring(1), node.childs[index]);
    }

    private int indexForChar(char c) {
        return c - 'a';
    }
}
```

### 2. 实现一个 Trie，用来求前缀和

677\. Map Sum Pairs (Medium)

[Leetcode](https://leetcode.com/problems/map-sum-pairs/description/) / [力扣](https://leetcode-cn.com/problems/map-sum-pairs/description/)

```html
Input: insert("apple", 3), Output: Null
Input: sum("ap"), Output: 3
Input: insert("app", 2), Output: Null
Input: sum("ap"), Output: 5
```

```java
class MapSum {

    private class Node {
        Node[] child = new Node[26];
        int value;
    }

    private Node root = new Node();

    public MapSum() {

    }

    public void insert(String key, int val) {
        insert(key, root, val);
    }

    private void insert(String key, Node node, int val) {
        if (node == null) return;
        if (key.length() == 0) {
            node.value = val;
            return;
        }
        int index = indexForChar(key.charAt(0));
        if (node.child[index] == null) {
            node.child[index] = new Node();
        }
        insert(key.substring(1), node.child[index], val);
    }

    public int sum(String prefix) {
        return sum(prefix, root);
    }

    private int sum(String prefix, Node node) {
        if (node == null) return 0;
        if (prefix.length() != 0) {
            int index = indexForChar(prefix.charAt(0));
            return sum(prefix.substring(1), node.child[index]);
        }
        int sum = node.value;
        for (Node child : node.child) {
            sum += sum(prefix, child);
        }
        return sum;
    }

    private int indexForChar(char c) {
        return c - 'a';
    }
}
```

