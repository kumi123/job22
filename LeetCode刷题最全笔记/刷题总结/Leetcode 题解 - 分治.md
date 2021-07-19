---

title: 题解分治
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2020-03-24 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/3.jpg
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
# Leetcode 题解 - 分治
<!-- GFM-TOC -->
* [Leetcode 题解 - 分治](#leetcode-题解---分治)
    * [1. 给表达式加括号](#1-给表达式加括号)
    * [2. 不同的二叉搜索树](#2-不同的二叉搜索树)
<!-- GFM-TOC -->


## 1. 给表达式加括号

241\. Different Ways to Add Parentheses (Medium)

[Leetcode](https://leetcode.com/problems/different-ways-to-add-parentheses/description/) / [力扣](https://leetcode-cn.com/problems/different-ways-to-add-parentheses/description/)



### 本质上是一个递归求解这个两端的问题

[题解一](https://leetcode-cn.com/problems/different-ways-to-add-parentheses/solution/pythongolang-fen-zhi-suan-fa-by-jalan/)

```python
class Solution:
    def diffWaysToCompute(self, input: str) -> List[int]:
        # 如果只有数字，直接返回
        if input.isdigit():
            return [int(input)]

        res = []
        for i, char in enumerate(input):
            if char in ['+', '-', '*']:
                # 1.分解：遇到运算符，计算左右两侧的结果集
                # 2.解决：diffWaysToCompute 递归函数求出子问题的解
                left = self.diffWaysToCompute(input[:i])
                right = self.diffWaysToCompute(input[i+1:])
                # 3.合并：根据运算符合并子问题的解
                for l in left:
                    for r in right:
                        if char == '+':
                            res.append(l + r)
                        elif char == '-':
                            res.append(l - r)
                        else:
                            res.append(l * r)

        return res
```

[题解二](https://leetcode-cn.com/problems/different-ways-to-add-parentheses/solution/java-fen-zhi-fa-zi-fu-chuan-chu-li-liang-chong-fan/)

```java
//自己的解法直接递归就可以
class Solution {
    
    public List<Integer> diffWaysToCompute(String input) {
        List<Integer> res =new ArrayList<>();
        if(!input.contains("+")&&!input.contains("-")&&!input.contains("*")) {
            res.add(Integer.valueOf(input));
            return res;

        }
        for(int i=0;i<input.length();i++){
            if(input.charAt(i)=='+'||input.charAt(i)=='-'||input.charAt(i)=='*'){
                /*List<Integer> left =new ArrayList<>();
                List<Integer> right =new ArrayList<>();
                left=diffWaysToCompute(input.substring(0,i));
                right=diffWaysToCompute(input.substring(i+1,input.length()));*/
                List<Integer> left=diffWaysToCompute(input.substring(0,i));
                List<Integer> right=diffWaysToCompute(input.substring(i+1,input.length()));
                for(Integer l:left){
                    for(Integer r:right){
                        if(input.charAt(i)=='+') res.add(l+r);
                        if(input.charAt(i)=='-') res.add(l-r);
                        if(input.charAt(i)=='*') res.add(l*r);
                    }
                }

                
            }
        }
        return res;
    }
    

}

//下边这个switch结构很不错，中间变量引入来弄好看的  还有直接定义 List<Integer> left = diffWaysToCompute(input.substring(0, i)); 比new更加有效
```



```java
class Solution {
    
    public List<Integer> diffWaysToCompute(String input) {
        return partition(input);
    }
    
    public List<Integer> partition(String input) {
        List<Integer> result = new ArrayList<>();
        if (!input.contains("+") && !input.contains("-") && !input.contains("*")) {
            result.add(Integer.valueOf(input));
            return result;
        }
        for(int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '+' || input.charAt(i) == '-' || input.charAt(i) == '*') {
                for(Integer left : partition(input.substring(0, i))) {
                    for (Integer right : partition(input.substring(i + 1))) {
                        if (input.charAt(i) == '+') {
                            result.add(left + right);
                        } else if (input.charAt(i) == '-') {
                            result.add(left - right);
                        } else if (input.charAt(i) == '*') {
                            result.add(left * right);
                        }
                    }
                }
            }
        }
        return result;
    }
}


```



```html
Input: "2-1-1".

((2-1)-1) = 0
(2-(1-1)) = 2

Output : [0, 2]
```

```java
public List<Integer> diffWaysToCompute(String input) {
    List<Integer> ways = new ArrayList<>();
    for (int i = 0; i < input.length(); i++) {
        char c = input.charAt(i);
        if (c == '+' || c == '-' || c == '*') {
            List<Integer> left = diffWaysToCompute(input.substring(0, i));
            List<Integer> right = diffWaysToCompute(input.substring(i + 1));
            for (int l : left) {
                for (int r : right) {
                    switch (c) {
                        case '+':
                            ways.add(l + r);
                            break;
                        case '-':
                            ways.add(l - r);
                            break;
                        case '*':
                            ways.add(l * r);
                            break;
                    }
                }
            }
        }
    }
    if (ways.size() == 0) {
        ways.add(Integer.valueOf(input));
    }
    return ways;
}
```

## 2. 不同的二叉搜索树

95\. Unique Binary Search Trees II (Medium)

[Leetcode](https://leetcode.com/problems/unique-binary-search-trees-ii/description/) / [力扣](https://leetcode-cn.com/problems/unique-binary-search-trees-ii/description/)

给定一个数字 n，要求生成所有值为 1...n 的二叉搜索树。

```html
Input: 3
Output:
[
  [1,null,3,2],
  [3,2,null,1],
  [3,1,null,null,2],
  [2,1,3],
  [1,null,2,null,3]
]
Explanation:
The above output corresponds to the 5 unique BST's shown below:

   1         3     3      2      1
    \       /     /      / \      \
     3     2     1      1   3      2
    /     /       \                 \
   2     1         2                 3
```

```java
public List<TreeNode> generateTrees(int n) {
    if (n < 1) {
        return new LinkedList<TreeNode>();
    }
    return generateSubtrees(1, n);
}

private List<TreeNode> generateSubtrees(int s, int e) {
    List<TreeNode> res = new LinkedList<TreeNode>();
    if (s > e) {
        res.add(null);
        return res;
    }
    for (int i = s; i <= e; ++i) {
        List<TreeNode> leftSubtrees = generateSubtrees(s, i - 1);
        List<TreeNode> rightSubtrees = generateSubtrees(i + 1, e);
        for (TreeNode left : leftSubtrees) {
            for (TreeNode right : rightSubtrees) {
                TreeNode root = new TreeNode(i);
                root.left = left;
                root.right = right;
                res.add(root);
            }
        }
    }
    return res;
}
```

题解

```java
class Solution {
    public List<TreeNode> generateTrees(int n) {
        if(n < 1)
            return new ArrayList<>();
        return helper(1, n);
    }

    public List<TreeNode> helper(int start, int end){
        List<TreeNode> list = new ArrayList<>();

        if(start > end){
            // 如果当前子树为空，不加null行吗？
            list.add(null);
            return list;
        }

        for(int i = start; i <= end; i++){
            // 想想为什么这行不能放在这里，而放在下面？
            // TreeNode root = new TreeNode(i);
            List<TreeNode> left = helper(start, i-1);  
            List<TreeNode> right = helper(i+1, end); 

            // 固定左孩子，遍历右孩子
            for(TreeNode l : left){
                for(TreeNode r : right){
                    TreeNode root = new TreeNode(i);
                    root.left = l;
                    root.right = r;
                    list.add(root);
                }
            }
        }
        return list;
    }
}

关于TreeNode root = new TreeNode(i)的放置的位置问题
如果这行代码放置在注释的地方，会造成一个问题，就是以当前为root根结点的树个数就
num = left.size() * right.size() > 1时，num棵子树会共用这个root结点，在下面两层for循环中，root的左右子树一直在更新，如果每次不新建一个root，就会导致num个root为根节点的树都相同。

关于如果当前子树为空，不加null行不行的问题
显然，如果一颗树的左子树为空，右子树不为空，要正确构建所有树，依赖于对左右子树列表的遍历，也就是上述代码两层for循环的地方，如果其中一个列表为空，那么循环都将无法进行。

```

#### [解法](https://leetcode-cn.com/problems/unique-binary-search-trees-ii/solution/cong-gou-jian-dan-ke-shu-dao-gou-jian-suo-you-shu-/)

#### 自己实现python

```python
class Solution:
    def generateTrees(self, n: int) -> List[TreeNode]:
        if n<1:
            return []
        return self.helper(1,n)
    def helper(self,s,e):
        res=[]#存储结果节点
        if s>e:
            res.append(None)
            return res
        for i in range(s,e+1):
            left=self.helper(s,i-1)
            right=self.helper(i+1,e)
            for l in left:
                for r in right:
                    node=TreeNode(i)
                    node.left=l
                    node.right=r
                    res.append(node)
        return res
```



#### 自己实现java

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
    public List<TreeNode> generateTrees(int n) {
        if(n < 1)
            return new ArrayList<>();
        return helper(1, n);
    }
    public List<TreeNode> helper(int s,int e){
        List<TreeNode> res = new ArrayList<>();
        if(s>e){
            res.add(null);
            return res;
        }
        for(int i=s;i<=e;i++){
            List<TreeNode> left=helper(s,i-1);
            List<TreeNode> right=helper(i+1,e);
            for(TreeNode l:left){
                for(TreeNode r:right){
                    TreeNode node=new TreeNode(i);
                    node.left=l;
                    node.right=r;
                    res.add(node);
                }
            }        
        }
      return res;  
    }

    }
```

