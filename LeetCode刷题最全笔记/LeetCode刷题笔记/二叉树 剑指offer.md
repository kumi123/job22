

## 二叉树的镜像

#### 1、递归

我们在做二叉树题目时候，第一想到的应该是用递归来解决。
仔细看下题目的输入和输出，输出的左右子树的位置跟输入正好是相反的，于是我们可以递归的交换左右子树来完成这道题。看一下动画就明白了：

![0f91f7cbf5740de86e881eb7427c6c3993f4eca3624ca275d71e21c5e3e2c550-226_2](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/0f91f7cbf5740de86e881eb7427c6c3993f4eca3624ca275d71e21c5e3e2c550-226_2.gif)

其实就是交换一下左右节点，然后再递归的交换左节点，右节点
根据动画图我们可以总结出递归的两个条件如下：

**终止条件：当前节点为null时返回**
**交换当前节点的左右节点，再递归的交换当前节点的左节点，递归的交换当前节点的右节点**
时间复杂度：每个元素都必须访问一次，所以是O(n)
空间复杂度：最坏的情况下，需要存放O(h)个函数调用(h是树的高度)，所以是O(h)
代码实现如下：

```python

# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
#第一种
class Solution(object):
	def mirrorTree(self, root):
		"""
		:type root: TreeNode
		:rtype: TreeNode
		"""
		# 递归函数的终止条件，节点为空时返回
		if not root:
			return None
		# 将当前节点的左右子树交换
		root.left,root.right = root.right,root.left
		# 递归交换当前节点的 左子树和右子树
		self.mirrorTree(root.left) #如果root.left是空，这个mirrirtree直接跳出
		self.mirrorTree(root.right)#同上
		# 函数返回时就表示当前这个节点，以及它的左右子树
		# 都已经交换完了，返回当前节点		
		return root
```





#### 2、迭代

递归实现也就是深度优先遍历的方式，那么对应的就是广度优先遍历。
广度优先遍历需要额外的数据结构--队列，来存放临时遍历到的元素。
深度优先遍历的特点是一竿子插到底，不行了再退回来继续；而广度优先遍历的特点是层层扫荡。
所以，我们需要先将根节点放入到队列中，然后不断的迭代队列中的元素。
对当前元素调换其左右子树的位置，然后：

判断其左子树是否为空，不为空就放入队列中
判断其右子树是否为空，不为空就放入队列中
动态图如下：

![f9e06159617cbf8372b544daee37be70286c3d9b762c016664e225044fc4d479-226_迭代](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/f9e06159617cbf8372b544daee37be70286c3d9b762c016664e225044fc4d479-226_迭代.gif)


深度优先遍历和广度优先遍历，从动画图中看起来很类似，这是因为演示的树层数只有三层。
时间复杂度：同样每个节点都需要入队列/出队列一次，所以是O(n)
空间复杂度：最坏的情况下会包含所有的叶子节点，完全二叉树叶子节点是n/2个，所以时间复杂度是0(n)
代码实现如下：

```python

# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
#第二种
class Solution(object):
	def mirrorTree(self, root):
		"""
		:type root: TreeNode
		:rtype: TreeNode
		"""
		if not root:
			return None
		# 将二叉树中的节点逐层放入队列中，再迭代处理队列中的元素 ，当队列不为空时候就可以循环 不断的交换 然后加入新的元素
		queue = [root]
		while queue:#这相对于什么open表 其实就是一个队列问题
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



## 二叉树镜像2

[前往 LeetCode 做此题](https://leetcode-cn.com/problems/er-cha-shu-de-jing-xiang-lcof/solution/mian-shi-ti-27-er-cha-shu-de-jing-xiang-di-gui-fu-/)

> **二叉树镜像定义：** 对于二叉树中任意节点 **root**，设其左 / 右子节点分别为 left, right；则在二叉树的镜像中的对应 root节点，其左 / 右子节点分别为 right, left
>
> 注意本题思路来自于



#### 方法一：递归法

#### 网址 https://leetcode-cn.com/problems/er-cha-shu-de-jing-xiang-lcof/solution/mian-shi-ti-27-er-cha-shu-de-jing-xiang-di-gui-fu-/

- 根据二叉树镜像的定义，考虑递归先序遍历（dfs）二叉树，交换每个节点的左 / 右子节点，即可生成二叉树的镜像。

##### 递归解析：

1. **终止条件：** 当节点 root为空时（即越过叶节点），则返回 null
2. 递推工作：
   1. 初始化节点 tmp ，用于暂存 root 的左子节点；
   2. 开启递归 **右子节点** mirrorTree(root.right)，并将返回值作为 root的 **左子节点** 。
   3. 开启递归 **左子节点** mirrorTree(tmp) ，并将返回值作为 root的 **右子节点** 。
3. **返回值：** 返回当前节点 root；

> **Q：** 为何需要暂存 root的左子节点？ 
>
> **A**：在递归右子节点 执行完毕后，左子树的值已经发生改变，此时递归左子节点则会出问题。

![sword-for-offer-27-f1.gif](https://krahets.gitee.io/assets/img/sword-for-offer-27-f1.f36847e4.gif)

​															**以上是递归的过程，真的便于理解**

##### 复杂度分析：

- **时间复杂度 O(N) ：** 其中*N* 为二叉树的节点数量，建立二叉树镜像需要遍历树的所有节点，占用 O(N)时间。
- **空间复杂度 O(N)：** 最差情况下（当二叉树退化为链表），递归时系统需使用 O(N)*O*(*N*) 大小的栈空间。

#### 代码：

> Python 利用平行赋值的写法（即 a, b = b, a），可省略暂存操作。其原理是先将等号右侧打包成元组 (b,a)，再序列地分给等号左侧的 a, b 序列。

```java
class Solution {
    public TreeNode mirrorTree(TreeNode root) {
        if(root == null) return null;
        TreeNode tmp = root.left;
        root.left = mirrorTree(root.right);
        root.right = mirrorTree(tmp);
        return root;
    }
}
```



```python
class Solution:
    def mirrorTree(self, root: TreeNode) -> TreeNode:
        if not root: return
        root.left, root.right = self.mirrorTree(root.right), 		self.mirrorTree(root.left)
        return root
```



```python
class Solution:
    def mirrorTree(self, root: TreeNode) -> TreeNode:
        if not root: return
        tmp = root.left
        root.left = self.mirrorTree(root.right)
        root.right = self.mirrorTree(tmp)
        return root
```



#### 方法二：辅助栈（或队列）

- 利用栈（或队列）遍历树的所有节点 node ，并交换每个 node 的左 / 右子节点。

##### 算法流程：

1. **特例处理：** 当 root为空时，直接返回 null

2. **初始化：** 栈（或队列），本文用栈，并加入根节点 root

3. 循环交换：

    当栈stack为空时跳出；

   1. **出栈：** 记为 node；
2. **添加子节点：** 将 node左和右子节点入栈；
   
3. **交换：** 交换 node的左 / 右子节点。
   
4. **返回值：** 返回根节点 root

![sword-for-offer-27-f2.gif](https://krahets.gitee.io/assets/img/sword-for-offer-27-f2.46c69301.gif)

##### 复杂度分析：

- 时间复杂度 O(N) ： 其中 N为二叉树的节点数量，建立二叉树镜像需要遍历树的所有节点，占用 O(N) 时间。
- 空间复杂度 O(N)： 最差情况下（当为满二叉树时），栈 stack最多同时存储 N/2个节点，占用 O(N)额外空间。

##### 代码：

```python
class Solution:
    def mirrorTree(self, root: TreeNode) -> TreeNode:
        if not root: return
        stack = [root]
        while stack:
            node = stack.pop()#弹出最后一个
            if node.left: stack.append(node.left)
            if node.right: stack.append(node.right)
            node.left, node.right = node.right, node.left
        return root
```



```java
class Solution {
    public TreeNode mirrorTree(TreeNode root) {
        if(root == null) return null;
        Stack<TreeNode> stack = new Stack<>() {{ add(root); }};
        while(!stack.isEmpty()) {
            TreeNode node = stack.pop();
            if(node.left != null) stack.add(node.left);
            if(node.right != null) stack.add(node.right);
            TreeNode tmp = node.left;
            node.left = node.right;
            node.right = tmp;
        }
        return root;
    }
}
```



## 面试题55 - I. 二叉树的深度

难度简单20

输入一棵二叉树的根节点，求该树的深度。从根节点到叶节点依次经过的节点（含根、叶节点）形成树的一条路径，最长路径的长度为树的深度。

例如：给定二叉树 `[3,9,20,null,null,15,7]`，

```
    3
   / \
  9  20
    /  \
   15   7
```

返回它的最大深度 3 。

### 解析1

> 树的遍历方式总体分为两类：深度优先搜索（DFS）、广度优先搜索（BFS）；
>
> - **常见的 DFS ：** 先序遍历、中序遍历、后序遍历；
> - **常见的 BFS ：** 层序遍历（即按层遍历）。

- 求树的深度需要遍历树的所有节点，本文将介绍基于 **先序遍历（DFS）** 和 **层序遍历（BFS）** 的两种解法。

**方法一：后序遍历（DFS）**

- 树的先序遍历 / 深度优先搜索往往利用 **递归** 或 **栈** 实现，本文使用递归实现。
- **关键点：** 此树的深度和其左（右）子树的深度之间的关系。显然，**此树的深度** 等于 **左子树的深度** 与 **右子树的深度** 中的 **最大值** +1+1 。

![img](https://krahets.gitee.io/assets/img/sword-for-offer-55-1.beaa34a9.png)

##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-24-sword-for-offer-55-1.html#算法解析：)算法解析：

1. **终止条件：** 当 `root` 为空，说明已越过叶节点，因此返回 深度 00 。

2. 递推工作：

    

   本质上是对树做先序遍历。

   1. 计算节点 `root` 的 **左子树的深度** ，即调用 `maxDepth(root.left)` ；
   2. 计算节点 `root` 的 **右子树的深度** ，即调用 `maxDepth(root.right)` ；

3. **返回值：** 返回 **此树的深度** ，即 `max(maxDepth(root.left), maxDepth(root.right)) + 1` 。

![sword-for-offfffer-55-1-f1.gif](https://krahets.gitee.io/assets/img/sword-for-offer-55-1-f1.23aa00b5.gif)

##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-24-sword-for-offer-55-1.html#复杂度分析：)复杂度分析：

- **时间复杂度 O(N)\*O\*(\*N\*) ：** N*N* 为树的节点数量，计算树的深度需要遍历所有节点。
- **空间复杂度 O(N)\*O\*(\*N\*) ：** 最差情况下（当树退化为链表时），递归深度可达到 N*N* 。

#### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-24-sword-for-offer-55-1.html#代码：)代码：

```python
class Solution:
    def maxDepth(self, root: TreeNode) -> int:
        if not root: return 0
        return max(self.maxDepth(root.left), self.maxDepth(root.right)) + 1
```



```java
class Solution {
    public int maxDepth(TreeNode root) {
        if(root == null) return 0;
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }
}
```

```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
#直接使用递归
class Solution:
    def maxDepth(self, root: TreeNode) -> int:
        if not root:
            return 0
        return max(self.maxDepth(root.left),self.maxDepth(root.right))+1
```

执行用时 :60 ms, 在所有 Python3 提交中击败了32.37%的用户

内存消耗 :15.6 MB, 在所有 Python3 提交中击败了100.00%的用户



### 解析2

**方法二：层序遍历（BFS）**

- 树的层序遍历 / 广度优先搜索往往利用 **队列** 实现。
- **关键点：** 每遍历一层，则计数器 +1+1 ，直到遍历完成，则可得到树的深度。

##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-24-sword-for-offer-55-1.html#算法解析：-2)算法解析：

1. **特例处理：** 当 `root` 为空，直接返回 深度 00 。
2. **初始化：** 队列 `queue` （加入根节点 `root` ），计数器 `res = 0` 。
3. 循环遍历：当queue为空时跳出。
   1. 初始化一个空列表 `tmp` ，用于临时存储下一层节点；
   2. 遍历队列：遍历 `queue` 中的各节点 `node` ，并将其左子节点和右子节点加入 `tmp` ；
   3. 更新队列： 执行 `queue = tmp` ，将下一层节点赋值给 `queue` ；
   4. 统计层数： 执行 `res += 1` ，代表层数加 11 ；
4. **返回值：** 返回 `res` 即可。

![sword-for-offer-55-1-f2.gif](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/sword-for-offer-55-1-f2.955511d1.gif)

##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-24-sword-for-offer-55-1.html#复杂度分析：-2)复杂度分析：

- **时间复杂度 O(N) ：** N为树的节点数量，计算树的深度需要遍历所有节点。
- **空间复杂度 O(N) ：** 最差情况下（当树平衡时），队列 `queue` 同时存储 N/2个节点。

#### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-24-sword-for-offer-55-1.html#代码：-2)代码：

```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution:
    def maxDepth(self, root: TreeNode) -> int:
        if not root:
            return 0
        res,queue=0,[root]#分别存储层数和队列
        temp=[]#用来存储当前层的节点
        while queue:
            for ele in queue:
                if ele.left: temp.append(ele.right)
                if ele.right:temp.append(ele.right)
            #以上是把当前层节点放到temp当中
            queue=temp
            res+=1
        return res
#容易超出时间限制
```



## 树的第k最大值

> 本文解法基于此性质：二叉搜索树的中序遍历为 **递增序列** 。

- 根据以上性质，易得二叉搜索树的 **中序遍历倒序** 为 **递减序列** 。
- 因此，求 “二叉搜索树第 k大的节点” 可转化为求 “此树的中序遍历倒序的第 k个节点” 。

![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/sword-for-offer-54.18cb0636.png)

> **中序遍历** 为 “左、根、右” 顺序，递归法代码如下：

```python
# 打印中序遍历 
def dfs(root):
    if not root: return #直接跳出来
    dfs(root.left)  # 左
    print(root.val) # 根
    dfs(root.right) # 右
```



```java
// 打印中序遍历
void dfs(TreeNode root) {
    if(root == null) return;
    dfs(root.left); // 左
    System.out.println(root.val); // 根
    dfs(root.right); // 右
}
```



> **中序遍历的倒序** 为 “右、根、左” 顺序，递归法代码如下：

```python
# 打印中序遍历倒序
def dfs(root):
    if not root: return
    dfs(root.right) # 右
    print(root.val) # 根
    dfs(root.left)  # 左
```



```java
// 打印中序遍历倒序
void dfs(TreeNode root) {
    if(root == null) return;
    dfs(root.right); // 右
    System.out.println(root.val); // 根
    dfs(root.left); // 左
}
```

```python 
   class Solution {
       int res, k;
       public int kthLargest(TreeNode root, int k) {
           this.k = k;
           dfs(root);
           return res;
       }
       void dfs(TreeNode root) {
           if(root == null) return;
           dfs(root.right);
        if(k == 0) return;
           if(--k == 0) res = root.val;
        dfs(root.left);
       }
   }
   
   
```


- 为求第k个节点，需要实现以下三项工作
  1. 递归遍历时计数，统计当前节点的序号；
  2. 递归到第 k个节点时，应记录结果 res ；
  3. 记录结果后，后续的遍历即失去意义，应提前终止（即返回）。

##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-31-sword-for-offer-54.html#递归解析：)递归解析：

1. **终止条件：** 当节点 root为空（越过叶节点），则直接返回；

2. **递归右子树：** 即 dfs(root.right) ；

3. 三项工作：
   1. 提前返回： 若 k = 0 ，代表已找到目标节点，无需继续遍历，因此直接返回；
   2. 统计序号： 执行 k = k - 1 （即从 k减至 0）；
   3. 记录结果： 若 k = 0，代表当前节点为第 k大的节点，因此记录 res = root.val ；
   
4. **递归左子树：** 即 dfs(root.left)；

   1. **递归左子树：** 即 dfs(root.left)*d**f**s*(*r**o**o**t*.*l**e**f**t*) ；

   ![sword-for-offer-54.gif](https://krahets.gitee.io/assets/img/sword-for-offer-54.d1589774.gif)

   ##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-31-sword-for-offer-54.html#复杂度分析：)复杂度分析：

   - **时间复杂度 O(N) ：** 当树退化为链表时（全部为右子节点），无论 k*k* 的值大小，递归深度都为 N，占用 O(N) 时间。
   - **空间复杂度 O(N) ：**当树退化为链表时（全部为右子节点），系统使用 O(N)*大小的栈空间。

   #### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-31-sword-for-offer-54.html#代码：)代码：

   > 题目指出：1 \leq k \leq （二叉搜索树节点个数）；因此无需考虑 k > N的情况。 若考虑，可以在中序遍历完成后判断 k > 0 是否成立，若成立则说明 k > N。

   
   
   
   
```java
   class Solution {
       int res, k;
       public int kthLargest(TreeNode root, int k) {
           this.k = k;
           dfs(root);
           return res;
       }
       void dfs(TreeNode root) {
           if(root == null) return;
           dfs(root.right);
        if(k == 0) return;
           if(--k == 0) res = root.val;
        dfs(root.left);
       }
}
   
   
```

```python
   

class Solution:
    def kthLargest(self, root: TreeNode, k: int) -> int:
        def dfs(root):
            if not root: return #空节点不再递归
            dfs(root.right)#递归右节点
            if self.k == 0: return #及时判断k==0，避免多余计算
            self.k -= 1#递归完右边 k要减一
            if self.k == 0: self.res = root.val#判断是否为当前节点
            dfs(root.left)#递归左边
        self.k = k
        dfs(root)
        return self.res


```

执行用时 :52 ms, 在所有 Python3 提交中击败了97.90%的用户

内存消耗 :17.8 MB, 在所有 Python3 提交中击败了100.00%的用户

## 面试题32 - I. 从上到下打印二叉树

#### 解题思路：

- 题目要求的二叉树的 **从上至下** 打印（即按层打印），又称为二叉树的 **广度优先搜索**（BFS）。
- BFS 通常借助 **队列** 的先入先出特性来实现。

![img](https://krahets.gitee.io/assets/img/sword-for-offer-32-1.db2eac5a.png)

##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-14-sword-for-offer-32-1.html#算法流程：)算法流程：

1. **特例处理：** 当树的根节点为空，则直接返回空列表 `[]` ；

2. **初始化：** 打印结果列表 `res = []` ，包含根节点的队列 `queue = [root]` ；

3. BFS 循环：

    

   当队列quque为空时跳出；

   1. **出队：** 队首元素出队，记为 `node`；
2. **打印：** 将 `node.val` 添加至列表 `tmp` 尾部；
   
   3. **添加子节点：** 若 `node` 的左（右）子节点不为空，则将左（右）子节点加入队列 `queue` ；
   
4. **返回值：** 返回打印结果列表 `res` 即可。

![sword-for-offer-32-1.gif](https://krahets.gitee.io/assets/img/sword-for-offer-32-1.b9474494.gif)

##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-14-sword-for-offer-32-1.html#复杂度分析：)复杂度分析：

- **时间复杂度 O(N)：** N为二叉树的节点数量，即 BFS 需循环 N次。
- **空间复杂度 O(N)：** 最差情况下，即当树为平衡二叉树时，最多有 N/2 个树节点**同时**在 `queue` 中，使用 O(N) 大小的额外空间。

#### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-14-sword-for-offer-32-1.html#代码：)代码：

> Python 中使用 collections 中的双端队列 `deque()` ，其 `popleft()` 方法可达到 O(1)*O*(1) 时间复杂度；列表 list 的 `pop(0)` 方法时间复杂度为 O(N)*O*(*N*) 



```java
class Solution {
    public int[] levelOrder(TreeNode root) {
        if(root == null) return new int[0];
        Queue<TreeNode> queue = new LinkedList<>(){{ add(root); }};
        ArrayList<Integer> ans = new ArrayList<>();
        while(!queue.isEmpty()) {
            TreeNode node = queue.poll();
            ans.add(node.val);
            if(node.left != null) queue.add(node.left);
            if(node.right != null) queue.add(node.right);
        }
        int[] res = new int[ans.size()];
        for(int i = 0; i < ans.size(); i++)
            res[i] = ans.get(i);
        return res;
    }
}
```

```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None


class Solution:
    def levelOrder(self, root: TreeNode) -> List[List[int]]:
        #定义队列来模拟层次遍历法
        #queue是队列，用来存储每一层的节点，有点像这个open表
        #temp用来存储下一层的节点的值
        #res 最后用temp进行添加
        if not root:return []#边缘情况
        queue,res=collections.deque(),[]
        queue.append(root)
        while queue:
            temp=[]
            for _ in range(len(queue)):#这里的循环是对当前层节点
                node=queue.popleft()#从左边出队
                temp.append(node.val)#保存出队点的值
                if node.left:queue.append(node.left)#补充进队
                if node.right:queue.append(node.right)#补充进队
            res.append(temp)#当前层数据进行赋值保存
        return res

```

执行用时 :40 ms, 在所有 Python3 提交中击败了79.81%的用户

内存消耗 :14 MB, 在所有 Python3 提交中击败了100.00%的用户





## 面试题68 - I. 二叉搜索树的最近公共祖先



### 解题思路：
1. 祖先的定义： 若节点 pp 在节点 root 的左（右）子树中，或 p = root，则称 root 是 p 的祖先。
2. 最近公共祖先的定义： 设节点 root 为节点 p,q 的某公共祖先，若其左子节点 root.left 和右子节点 root.right 都不是 p,q的公共祖先，则称 root 是 “最近的公共祖先” 
3. 根据以上定义，若 root 是 p,q的 最近公共祖先 ，则只可能为以下情况之一：

- p 和 q 在 root 的子树中，且分列 root 的 异侧（即分别在左、右子树中）；
- p = root，且 qq 在 root 的左或右子树中；
- q = root，且 p 在 root的左或右子树中；


本题给定了两个重要条件：① 树为 二叉搜索树 ，② 树的所有节点的值都是 唯一 的。根据以上条件，可方便地判断 p,qp,q 与 rootroot 的子树关系，即：

若 root.val < p.val ，则 p 在 root 右子树 中；
若 root.val > p.val ，则 p 在 root 左子树 中；
若 root.val = p.val ，则 p 和 root指向 同一节点 。
###  方法一：迭代
循环搜索： 当节点 rootroot 为空时跳出；
当 p, q都在 root 的 右子树 中，则遍历至 root.right ；
否则，当 p, q 都在 root 的 左子树 中，则遍历至 root.left ；
否则，说明找到了 最近公共祖先 ，跳出。
返回值： 最近公共祖先 root。
复杂度分析：
时间复杂度 O(N) ： 其中 N 为二叉树节点数；每循环一轮排除一层，二叉搜索树的层数最小为 \log NlogN （满二叉树），最大为 N （退化为链表）。
空间复杂度 O(1) ： 使用常数大小的额外空间。



```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution:
    def lowestCommonAncestor(self, root: 'TreeNode', p: 'TreeNode', q: 'TreeNode') -> 'TreeNode':
        while root:
            if root.val > p.val and root.val>q.val:
                root=root.left
            if root.val < p.val and root.val<q.val:
                root=root.right
        return root
```



### 方法二：递归
递推工作：
当 p, q都在 root 的 右子树 中，则开启递归 root.right并返回；
否则，当 p, q都在 root的 左子树 中，则开启递归 root.left 并返回；
返回值： 最近公共祖先 root 。
复杂度分析：
时间复杂度 O(N) ： 其中 N为二叉树节点数；每循环一轮排除一层，二叉搜索树的层数最小为logN （满二叉树），最大为 N （退化为链表）。
空间复杂度 O(N) ： 最差情况下，即树退化为链表时，递归深度达到树的层数 N。

```python
class Solution:
    def lowestCommonAncestor(self, root: 'TreeNode', p: 'TreeNode', q: 'TreeNode') -> 'TreeNode':
        if root.val < p.val and root.val < q.val:
            return self.lowestCommonAncestor(root.right, p, q)
        if root.val > p.val and root.val > q.val:
            return self.lowestCommonAncestor(root.left, p, q)
        return root

```



## [面试题68 - II. 二叉树的最近公共祖先](https://leetcode-cn.com/problems/er-cha-shu-de-zui-jin-gong-gong-zu-xian-lcof/)

给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。

百度百科中最近公共祖先的定义为：“对于有根树 T 的两个结点 p、q，最近公共祖先表示为一个结点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。”

例如，给定如下二叉树:  root = [3,5,1,6,2,0,8,null,null,7,4]



 ![binarytree](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/binarytree.jpg)

示例 1:

输入: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
输出: 3
解释: 节点 5 和节点 1 的最近公共祖先是节点 3。
示例 2:

输入: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
输出: 5
解释: 节点 5 和节点 4 的最近公共祖先是节点 5。因为根据定义最近公共祖先节点可以为节点本身。


说明:

所有节点的值都是唯一的。
p、q 为不同节点且均存在于给定的二叉树中。

### 解法一:使用数组存储路径

​	给我们的节点没有指向父节点的指针,我们可以使用两个数组保存路径,用先序遍历存储正向路径
如节点4的正向路径为3->5->2->4,节点6的正向路径为3->5->6,从头找到第一个不相同节点的前一个节点即可,这个例子就是指的节点5。

```python
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        """
        :type root: TreeNode
        :type p: TreeNode
        :type q: TreeNode
        :rtype: TreeNode
        """
        stack_1 = []
        stack_2 = []
        def dfs(root, node, stack):
            if not root:
                return False
            stack.append(root)
            if root.val == node.val:
                return True          
            if (dfs(root.left, node, stack) or dfs(root.right, node, stack)):#这一步还是用到了递归
                return True
            stack.pop()
            
        dfs(root, p, stack_1)
        dfs(root, q, stack_2)
        #一下就是使用这个两个栈，找出这两的最后一个公共的点，打印出来就可以
        i = 0
        while i < len(stack_1) and i<len(stack_2) and stack_1[i] == stack_2[i]:
            result = stack_1[i]
            i += 1
        return result


```



### 解法二： 递归

**【思路】**
因为lowestCommonAncestor(root, p, q)的功能是找出以root为根节点的两个节点p和q的最近公共祖先，所以递归体分三种情况讨论：

1. 如果p和q分别是root的左右节点，那么root就是我们要找的最近公共祖先
2. 如果p和q都是root的左节点，那么返回lowestCommonAncestor(root.left,p,q)
3. 如果p和q都是root的右节点，那么返回lowestCommonAncestor(root.right,p,q)
   边界条件讨论：

- 如果root是null，则说明我们已经找到最底了，返回null表示没找到
- 如果root与p相等或者与q相等，则返回root
- 如果左子树没找到，递归函数返回null，证明p和q同在root的右侧，那么最终的公共祖先就是右子树找到的结点
- 如果右子树没找到，递归函数返回null，证明p和q同在root的左侧，那么最终的公共祖先就是左子树找到的结点
**【代码】**

```java
public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if(root==null || root==p || root==q)
        return root;
    
    TreeNode leftNode=lowestCommonAncestor(root.left,p,q);
    TreeNode rightNode=lowestCommonAncestor(root.right,p,q);

    if(leftNode==null)
        return rightNode;
    if(rightNode==null)
        return leftNode;

    return root;
}

```

```python 
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        if root==None or root==p or root==q:
            return root
        leftNode=self.lowestCommonAncestor(root.left,p,q);
        rightNode=self.lowestCommonAncestor(root.right,p,q);
        if not leftNode:
            return rightNode;
        if not rightNode:
            return leftNode;
        return root

```





## 平衡二叉树

#### 方法一：先序遍历 + 剪枝 （从底至顶）

> 此方法为本题的最优解法，但剪枝的方法不易第一时间想到。

思路是对二叉树做先序遍历，从底至顶返回子树深度，若判定某子树不是平衡树则 “剪枝” ，直接向上返回。

##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-25-sword-for-offer-55-2.html#算法流程：)算法流程：

**`recur(root)` 函数：**

- 返回值：
  1. 当节点`root` 左 / 右子树的深度差 \leq 1≤1 ：则返回当前子树的深度，即节点 `root` 的左 / 右子树的深度最大值 +1+1 （ `max(left, right) + 1` ）；
  2. 当节点`root` 左 / 右子树的深度差 > 2>2 ：则返回 -1−1 ，代表 **此子树不是平衡树** 。
- 终止条件：
  1. 当 `root` 为空：说明越过叶节点，因此返回高度 00 ；
  2. 当左（右）子树深度为 -1−1 ：代表此树的 **左（右）子树** 不是平衡树，因此剪枝，直接返回 -1 ；

**`isBalanced(root)` 函数：**

- **返回值：** 若 `recur(root) != -1` ，则说明此树平衡，返回 true； 否则返回 false 。

![sword-for-offer-55-2-f1.gif](https://krahets.gitee.io/assets/img/sword-for-offer-55-2-f1.c52775b4.gif)

##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-25-sword-for-offer-55-2.html#复杂度分析：)复杂度分析：

- **时间复杂度 O(N)：** N*N* 为树的节点数；最差情况下，需要递归遍历树的所有节点。
- **空间复杂度 O(N)：** 最差情况下（树退化为链表时），系统递归需要使用 O(N)*O*(*N*) 的栈空间。

```python
class Solution:
    def isBalanced(self, root: TreeNode) -> bool:
        def recur(root):
            if not root: return 0
            left = recur(root.left)
            if left == -1: return -1
            right = recur(root.right)
            if right == -1: return -1
            return max(left, right) + 1 if abs(left - right) <= 1 else -1

        return recur(root) != -1
```



#### 方法二：先序遍历 + 判断深度 （从顶至底）

5. > 此方法容易想到，但会产生大量重复计算，时间复杂度较高。

   思路是构造一个获取当前子树的深度的函数 `depth(root)` （即 [面试题55 - I. 二叉树的深度](https://leetcode-cn.com/problems/er-cha-shu-de-shen-du-lcof/solution/mian-shi-ti-55-i-er-cha-shu-de-shen-du-xian-xu-bia/) ），通过比较某子树的左右子树的深度差 `abs(depth(root.left) - depth(root.right)) <= 1` 是否成立，来判断某子树是否是二叉平衡树。若所有子树都平衡，则此树平衡。
   
   ##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-25-sword-for-offer-55-2.html#算法流程：-2)算法流程：
   
   **`isBalanced(root)` 函数：** 判断树 `root` 是否平衡
   
   - **特例处理：** 若树根节点 `root` 为空，则直接返回 true；
   - 返回值：所有子树都需要满足平衡树性质，因此以下三者使用与逻辑&\&连接；
     1. `abs(self.depth(root.left) - self.depth(root.right)) <= 1` ：判断 **当前子树** 是否是平衡树；
     2. `self.isBalanced(root.left)` ： 先序遍历递归，判断 **当前子树的左子树** 是否是平衡树；
     3. `self.isBalanced(root.right)` ： 先序遍历递归，判断 **当前子树的右子树** 是否是平衡树；
   
   **`depth(root)` 函数：** 计算树 `root` 的深度
   
   - **终止条件：** 当 `root` 为空，即越过叶子节点，则返回高度 00 ；
   - **返回值：** 返回左 / 右子树的深度的最大值 +1+1 。
   
   ![sword-for-offer-55-2-f2.gif](https://krahets.gitee.io/assets/img/sword-for-offer-55-2-f2.51f0e4b0.gif)
   
   ##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-25-sword-for-offer-55-2.html#复杂度分析：-2)复杂度分析：
   
   - 时间复杂度 O(N log N)：最差情况下（为"满二叉树"时），遍历树所有节点，判断每个节点的深度`depth(root)`需要遍历各子树的所有节点。
   
     - 满二叉树高度的复杂度 O(log N)，将满二叉树按层分为 log (N+1)*l**o**g*(*N*+1) 层；
   
     - 通过调用 `depth(root)` ，判断二叉树各层的节点的对应子树的深度，需遍历节点数量为 
   
       $N \times 1$, $\frac{N-1}{2} \times 2$, $\frac{N-3}{4} \times 4$, $\frac{N-7}{8} \times 8$, ..., $1 \times \frac{N+1}{2}$因此各层执行 `depth(root)` 的时间复杂度为 O(N) （每层开始，最多遍历 N个节点，最少遍历 $\frac{N+1}{2}2*N*+1$ 个节点）。
   
     > 其中，$\frac{N-3}{4} \times 4$代表从此层开始总共需遍历 N-3 个节点，此层共有 4 个节点，因此每个子树需遍历 $\frac{N-3}{4}$个节点。
   
     - 因此，总体时间复杂度 =每层执行复杂度 $\times$ 层数复杂度 = $O(N \times log N)$。
   
   ![img](https://krahets.gitee.io/assets/img/sword-for-offer-55-2-2.475efda6.png)
   
   - **空间复杂度 O(N)：** 最差情况下（树退化为链表时），系统递归需要使用 O(N)的栈空间。
   
   
   

```python
   # Definition for a binary tree node.
   # class TreeNode:
   #     def __init__(self, x):
   #         self.val = x
   #         self.left = None
   #         self.right = None
   
   class Solution:
       def isBalanced(self, root: TreeNode) -> bool:
           if not root:return True
           return self.isBalanced(root.left) and self.isBalanced(root.right) and abs(self.depth(root.left)-self.depth(root.right))<=1
       def depth(self,root):
           if not root:return 0
           return max(self.depth(root.left),self.depth(root.right))+1
```

   

## 面试题28. 对称的二叉树

#### 解题思路：

- 对称二叉树规律：对于树中任意两个对称节点，一定有：

  - $L.val = R.val$：即此两对称节点值相等。
  - $L.left.val = R.right.val$：即 L*L* 的 左子节点 和 R*R* 的 右子节点 对称；
  - $L.right.val = R.left.val$：即 L*L* 的 右子节点 和 R*R* 的 左子节点 对称。

- 根据以上规律，考虑从顶至底递归判断每对节点是否对称，从而判断树是否为对称二叉树。

  

![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/sword-for-offer-28-p1.b2d9e629.png)

- **特殊情况：** 当树无节点（即 `root == null` 时），直接返回 true。
- 递归终止条件：
  - 当 L 和 R 同时越过叶子节点： 说明树从顶至底都满足对称二叉树规律，因此返回 true ；
  - 当 L或 R中只有一个越过叶子节点： 树是不对称的，因此返回 false ；
  - 当节点 L 值 不等于 节点 R 值： 此两节点不对称，因此树肯定是不对称的，因此返回 false ；
- 递推工作：
  - 判断 L.left和 R.right是否对称，即开启下层递归 `recur(L.left, R.right)` ；
  - 判断 L.right和 R.left是否对称，即开启下层递归 `recur(L.right, R.left)` ；
- **返回值：** 两对节点都对称时，才是对称树，因此返回 `recur(L.left, R.right) && recur(L.right, R.left)` 

![sword-for-offer-28.gif](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/sword-for-offer-28.80b4f576.gif)

##### 复杂度分析：

- **时间复杂度 O(N) ：** 其中 N为二叉树的节点数量，每次执行 `recur()` 可以判断一对节点是否对称，因此最多调用 N/2 次 `recur()` 方法。
- **空间复杂度 O(N) ：** 最差情况下（见下图），二叉树退化为链表，系统使用 O(N)大小的栈空间。



```python
class Solution:
    def isSymmetric(self, root: TreeNode) -> bool:
        def recur(L, R):
            if not L and not R: return True #象征着空的子树
            if not L or not R or L.val != R.val: return False#不镜像条件 左子树或者右子树位置没有到叶子 或者节点值不同
            return recur(L.left, R.right) and recur(L.right, R.left)

        return recur(root.left, root.right) if root else True#空树和其他的分开来
```

#### 方法2： 迭代法

利用队列进行迭代。队列中每两个连续的结点应该是相等的，而且它们的子树互为镜像。最初，队列中包含的是 root->left 以及 root->right。该算法的工作原理类似于 BFS，但存在一些关键差异。每次提取两个结点并比较它们的值。然后，将两个结点的左右子结点按相反的顺序插入队列中。当队列为空时，或者检测到树不对称（即从队列中取出两个不相等的连续结点）时，该算法结束。

```python
python
class Solution:
    def isSymmetric(self, root: TreeNode) -> bool:
        if root is None:
            return True
        q = []
        q.append(root.left)
        q.append(root.right)
        while len(q)!=0:
            A = q.pop(0)
            B = q.pop(0)
            if A == None and B == None:
                continue#跳出本次循环while ，你会发现如果空白节点是AB 下边就不会再增添了
            if A == None or B == None:
                return False
            #在这里说明一下子程序结构，第一个if是判断两个节点是否都是叶子节点，如果是，那么说明程序都进行到了叶子节点
            #第二个if 是至少有一个节点为空，但是由于第一个if存在，那么变成一个为空 一个不为空，必然不成立
            #下边分支if可以代表两个节点都不为空了至少
            if A.val != B.val:
                return False 
            q.append(A.left)
            q.append(B.right)
            q.append(A.right)
            q.append(B.left)
        return True 
```

复杂度分析
时间复杂度：O(n)，因为我们遍历整个输入树一次，所以总的运行时间为O(n)，其中nn是树中结点的总数。
空间复杂度：搜索队列需要额外的空间。在最糟糕情况下，我们不得不向队列中插入O(n)个结点。因此，空间复杂度为O(n)

#### 方法三：非递归写法（层序遍历）
这个方法有点像层序遍历，故使用队列，但是根据对称性，队首和队尾其实都需要能够执行入队和出队操作，因此使用双端队列（Deque），本质上只有这个变化了，上一个只是用了一个列表代表普通队列，这次试用双端队列

```python
from collections import deque


class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None


class Solution:
    def isSymmetric(self, root: TreeNode) -> bool:
        if root is None:
            return True

        d = deque()
        d.appendleft(root.left)
        d.append(root.right)

        while d:
            left_node = d.popleft()
            right_node = d.pop()

            if left_node is None and right_node is None:
                continue

            if left_node is None or right_node is None:
                return False
            # 代码走到这里一定有 left_node 和 right_node 非空
            # 因此可以取出 val 进行判断了
            if left_node.val != right_node.val:
                return False
            d.appendleft(left_node.right)
            d.appendleft(left_node.left)
            d.append(right_node.left)
            d.append(right_node.right)
        return True


```



## 利用两个栈模拟成一个队列

#### 解题思路：

- **栈无法实现队列功能：** 栈底元素（对应队首元素）无法直接删除，需要将上方所有元素出栈。
- **双栈可实现列表倒序：** 设有含三个元素的栈 A = [1,2,3] 和空栈 B = []。若循环执行 A*A* 元素出栈并添加入栈 B*B* ，直到栈 A*A* 为空，则 A = [] , B = [3,2,1] ，即 **栈 B 元素实现栈 A 元素倒序** 。
- **利用栈 B删除队首元素：** 倒序后，B执行出栈则相当于删除了 A 的栈底元素，即对应队首元素。

![i78edamg](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/sword-for-offer-09.eb38c00c.png)

##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-02-25-sword-for-offer-09.html#函数设计：)函数设计：

> 题目只要求实现 **加入队尾**`appendTail()` 和 **删除队首**`deleteHead()` 两个函数的正常工作，因此我们可以设计栈 `A` 用于加入队尾操作，栈 `B` 用于将元素倒序，从而实现删除队首元素。

- **加入队尾 `appendTail()`函数：** 将数字 `val` 加入栈 `A` 即可。

- 删除队首`deleteHead()`函数：

   

  有以下三种情况。

  1. **当栈 `B` 不为空：** `B`中仍有已完成倒序的元素，因此直接返回 `B` 的栈顶元素。
  2. **否则，当 `A` 为空：** 即两个栈都为空，无元素，因此返回 -1−1 。
  3. **否则：** 将栈 `A` 元素全部转移至栈 `B` 中，实现元素倒序，并返回栈 `B` 的栈顶元素。

![wssword-edfor-offer-0978.gif](https://krahets.gitee.io/assets/img/sword-for-offer-09.86fbafe4.gif)

##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-02-25-sword-for-offer-09.html#复杂度分析：)复杂度分析：

> 由于问题特殊，以下分析仅满足添加 N个元素并删除 N个元素，即栈初始和结束状态下都为空的情况。

- **时间复杂度：** `appendTail()`函数为 O(1)；`deleteHead()` 函数在 N 次队首元素删除操作中总共需完成 N个元素的倒序。
- **空间复杂度 O(N) ：** 最差情况下，栈 `A` 和 `B` 共保存 N 个元素。

#### [#](https://krahets.gitee.io/views/sword-for-offer/2020-02-25-sword-for-offer-09.html#代码：)代码：

```python
class CQueue:
    def __init__(self):
        self.A, self.B = [], [] #A是数据进入存放的栈 而B是数据流出试用的栈

    def appendTail(self, value: int) -> None:
        self.A.append(value)#来了直接push进去

    def deleteHead(self) -> int:
        if self.B: return self.B.pop() #如果B(出栈)不为空，就直接弹出首个
        if not self.A: return -1#在第一个不成立情况下 就是代表A和B都是空 那么没有可以返回的
        while self.A:
            self.B.append(self.A.pop()) #此时如果入栈A有东西 需要转移到B栈，就是倒叙传入就可以
        return self.B.pop()#默认弹出右边
```

更好的一个代码

1. stack_in只负责进入

2. stack_out只负责取出

3. 只有stack_out为空时才把stack_in的所有元素倾倒进stack_out中，这样顺序就不会乱了

```python
class CQueue:

    def __init__(self):
        self.stack_in = []
        self.stack_out = []

    def appendTail(self, value: int) -> None:
        self.stack_in.append(value)

    def deleteHead(self) -> int:
        if not self.stack_out:
            if not self.stack_in: # 都为空
                return -1
            else: # 把in栈中的东西全部倒入out栈中
                while self.stack_in:
                    self.stack_out.append(self.stack_in.pop())
        
        return self.stack_out.pop()
```

```c++
class CQueue {
    //两个栈，一个出栈，一个入栈
    private Stack<Integer> stack1;
    private Stack<Integer> stack2;
    
    public CQueue() {
        stack1 = new Stack<>();
        stack2 = new Stack<>();
    }
    
    public void appendTail(int value) {
        stack1.push(value);
    }
    
    public int deleteHead() {
        if(!stack2.isEmpty()){
            return stack2.pop();
        }else{
            while(!stack1.isEmpty()){
                stack2.push(stack1.pop());
            }
            return stack2.isEmpty() ? -1 : stack2.pop();
        }
    }
}
```

自己的代码：

```python
class CQueue:

    def __init__(self):
        stack_in,stack_out=[],[]


    def appendTail(self, value: int) -> None:
        self.stack_in.append(value)


    def deleteHead(self) -> int:
        #三种情况
        #case1:
        if self.stack_out:
            return self.stack_out.pop()
        if not self.stack_in:
            return -1
        while self.stack_in:
            self.stack_out.append(self.stack_in.pop())
        return self.stack_out.pop()




# Your CQueue object will be instantiated and called as such:
# obj = CQueue()
# obj.appendTail(value)
# param_2 = obj.deleteHead()
```





####  解题思路：

> 普通栈的 `push()` 和 `pop()` 函数的复杂度为 O(1) ；而获取栈最小值 `min()` 函数需要遍历整个栈，复杂度为 O(N)。

- 本题难点：

  将函数 `pop()` 复杂度降为O(1)，可通过建立辅助栈实现；

  - **数据栈 A：** 栈 A用于存储所有元素，保证入栈 `push()` 函数、出栈 `pop()` 函数、获取栈顶 `top()` 函数的正常逻辑。
  - **辅助栈 B ：** 栈 B 中存储栈 A中所有 **非严格降序** 的元素，则栈 A 中的最小元素始终对应栈 B*B* 的栈顶元素，即 `min()` 函数只需返回栈 B的栈顶元素即可。

- 因此，只需设法维护好 栈 B 的元素，使其保持非严格降序，即可实现 `min()` 函数的 O(1) 复杂度。

![img](https://krahets.gitee.io/assets/img/sword-for-offer-30.c32fe8c4.png)

##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-13-sword-for-offer-30.html#函数设计：)函数设计：

- **`push(x)` 函数：** 重点为保持栈 B*B* 的元素是 **非严格降序** 的。
  1. 将 x压入栈 A （即 `A.add(x)` ）；
  2. 若 ① 栈 B为空 **或** ② x**小于等于** 栈 B 的栈顶元素，则将 x 压入栈 B（即 `B.add(x)` ）。
- **`pop()` 函数：** 重点为保持栈 A, B的 **元素一致性** 。
  1. 执行栈 A 出栈（即 `A.pop()` ），将出栈元素记为 y；
  2. 若 y等于栈 B的栈顶元素，则执行栈 `B` 出栈（即 `B.pop()` ）。
- **`top()` 函数：** 直接返回栈 A 的栈顶元素即可，即返回 `A.peek()` 。
- **`min()` 函数：** 直接返回栈 B 的栈顶元素即可，即返回 `B.peek()` 。

![sword-for-offer-30.gif](https://krahets.gitee.io/assets/img/sword-for-offer-30.d41fb8a0.gif)

##### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-13-sword-for-offer-30.html#复杂度分析：)复杂度分析：

- **时间复杂度 O(1) ：** `push()`, `pop()`, `top()`, `min()` 四个函数的时间复杂度均为常数级别。
- **空间复杂度 O(N) ：** 当共有 N个待入栈元素时，辅助栈 B最差情况下存储 N个元素，使用 O(N)额外空间。

#### [#](https://krahets.gitee.io/views/sword-for-offer/2020-03-13-sword-for-offer-30.html#代码：)代码：



```python
class MinStack:

    def __init__(self):
        """
        initialize your data structure here.
        """
        self.A,self.B=[],[]

#使用辅助栈B来进行完成，使得B栈顶元素一定是最小的
    def push(self, x: int) -> None:
        self.A.append(x)
        if not self.B or self.B[-1]>=x:
            self.B.append(x)


    def pop(self) -> None:
        if self.A.pop()==self.B[-1]:
            self.B.pop()


    def top(self) -> int:
        return self.A[-1]#因为是依次入栈，所以返回栈顶元素


    def min(self) -> int:
        return self.B[-1]#返回B栈顶数据数据



# Your MinStack object will be instantiated and called as such:
# obj = MinStack()
# obj.push(x)
# obj.pop()
# param_3 = obj.top()
# param_4 = obj.min()
```

```python
def pop(self) -> None:
        if self.A.pop() == self.B[-1]:
            self.B.pop()
```

这句话`self.A.pop() == self.B[-1]` 很精妙，如果A出栈的元素正好是最小值元素，那么B也出栈，否则如果A出栈的元素不是最小值的元素（即self.A.pop() != self.B[-1]），那么B不动，因为A栈中还存在最小值的元素。



## [面试题59 - I. 滑动窗口的最大值](https://leetcode-cn.com/problems/hua-dong-chuang-kou-de-zui-da-zhi-lcof/)

难度简单45

给定一个数组 `nums` 和滑动窗口的大小 `k`，请找出所有滑动窗口里的最大值。

**示例:**

```
输入: nums = [1,3,-1,-3,5,3,6,7], 和 k = 3
输出: [3,3,5,5,6,7] 
解释: 

  滑动窗口的位置                最大值
---------------               -----
[1  3  -1] -3  5  3  6  7       3
 1 [3  -1  -3] 5  3  6  7       3
 1  3 [-1  -3  5] 3  6  7       5
 1  3  -1 [-3  5  3] 6  7       5
 1  3  -1  -3 [5  3  6] 7       6
 1  3  -1  -3  5 [3  6  7]      7
```

 

**提示：**

你可以假设 *k* 总是有效的，在输入数组不为空的情况下，1 ≤ k ≤ 输入数组的大小。

### 方法一





- 回忆 面试题30. 包含min函数的栈 ，其使用 单调栈 实现了随意入栈、出栈情况下的 O(1)时间获取 “栈内最小值” 。本题同理，不同点在于 “出栈操作” 删除的是 “列表尾部元素” ，而 “窗口滑动” 删除的是 “列表首部元素” 。

- 窗口对应的数据结构为 双端队列 ，本题使用 单调队列 即可解决以上问题。遍历数组时，每轮保证单调队列 $deque$：

- $deque$ 内 仅包含窗口内的元素 $\Rightarrow$ 每轮窗口滑动移除了元素 $nums[i - 1]$ ，需将$ deque $内的对应元素一起删除。
- $deque$内的元素 非严格递减 $\Rightarrow$每轮窗口滑动添加了元素 
-  $nums[j+1]$ ，需将 $deque$ 内所有 $<nums[j+1] $的元素删除。

算法流程：

1.   初始化： 双端队列 $deque$ ，结果列表 $res$ ，数组长度 n ；
2.   滑动窗口： 左边界范围 $i \in [1 - k, n + 1 - k]$，右边界范围$ j \in [0, n - 1]$；
3.   若$ i > 0 $且 队首元素 $deque[0]$ = 被删除元素 $nums[i−1]$ ：则队首元素出队；
     删除$deque$ 内所有$<nums[j]$ 的元素，以保持 $deque$ 递减；
4.   将 $nums[j]$ 添加至 $deque$ 尾部；
5.   若已形成窗口（即$ i \geq 0$ ）：将窗口最大值（即队首元素 $deque[0] $）添加至列表 res 。
     返回值： 返回结果列表 res。

![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/40bb042c0fc7af0721cde65fc6c53115222002884593b77300c804d62fa2a4c0-Picture2.png)

![img8654](https://pic.leetcode-cn.com/3b864a89c96f40bf9a351adb06324a50bc59cd81e156a037235ba9d7faa51137-Picture3.png)

![img](https://pic.leetcode-cn.com/1394626ba0cf69b26ccb29ebd36413e13c1df00ac41726a0a8db654a5b8da8e3-Picture4.png)

![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/c22e76ff9edb59d9c90719165f8b67825ce16117af7d69d2ef225438fbd36c17-Picture5.png)

![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/49eab62ab7f1977ff5d3b9c21e15da474ebf627a48c96aef70e00e9ab1a2d137-Picture6.png)

![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/022759d28a2068f962f5fbe5333fe93bc1b459401f719837d591b83ba8b9dcf8-Picture7.png)

![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/bc29e74d169335d5dd59314cf5882b2ace6c2f60659927a6d02375164164909e-Picture8.png)

![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/d5c0cf4bb9e4ecb7bfa801d0f1d434b39fa31812f805173c58b3aecfd9ff8d4e-Picture9.png)

![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/0910405bcc9084b894fd3235708c1bc5bf9d197a28155e9cd26713c64a30b4fc-Picture10.png)

![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/74458a079b804b28760666578cd828a68b76331a742d519e7ca003c8c6532eb9-Picture11.png)

```python
class Solution:
    def maxSlidingWindow(self, nums: List[int], k: int) -> List[int]:
        deque = collections.deque()
        res, n = [], len(nums)
        for i, j in zip(range(1 - k, n + 1 - k), range(n)):
            if i > 0 and deque[0] == nums[i - 1]: deque.popleft() # 删除 deque 中对应的 nums[i-1]
            while deque and deque[-1] < nums[j]: deque.pop() # 保持 deque 递减
            deque.append(nums[j])
            if i >= 0: res.append(deque[0]) # 记录窗口最大值
        return res

这就是说，如果deque不为空，并且deque的最后一位元素小于当前入队元素nums[j]的话，直接把最后一位元素弹走，直到deque为空，或deque的最后一位元素大于等于nums[j]。这就保证了，deque的头部总是原始队列的最大值~

```



### 方法二 

暴力解法

```python
class Solution:
    def maxSlidingWindow(self, nums: List[int], k: int) -> List[int]:
        if not nums or k == 0: return []#考虑一下数组为空或者是滑动窗口为空
        res=[]
        for i in range(len(nums)-k+1):
            max2=max(nums[i:i+k])
            res.append(max2)
        return res
```





## 58

### 题目要求

- 首先把空格去掉
- 对字符串倒置
- 思路就是转化成列表来做

#### 思路一

https://leetcode-cn.com/problems/fan-zhuan-dan-ci-shun-xu-lcof/solution/mian-shi-ti-58-i-fan-zhuan-dan-ci-shun-xu-shuang-z/

```python
class Solution:
    def reverseWords(self, s: str) -> str:
        s = s.strip() # 删除首尾空格
        i = j = len(s) - 1
        res = []
        while i >= 0:
            while i >= 0 and s[i] != ' ': i -= 1 # 搜索首个空格
            res.append(s[i + 1: j + 1]) # 添加单词
            while s[i] == ' ': i -= 1 # 跳过单词间空格
            j = i # j 指向下个单词的尾字符
        return ' '.join(res) # 拼接并返回
##本质上就是移动指针来进行从后边来寻找单词并且输出
```

算法解析：
倒序遍历字符串 s ，记录单词左右索引边界 i , j；
每确定一个单词的边界，则将其添加至单词列表 res ；
最终，将单词列表拼接为字符串，并返回即可。
复杂度分析：
时间复杂度 O(N) ： 其中 N 为字符串 s 的长度，线性遍历字符串。
空间复杂度 O(N)： 新建的 list(Python) 或 StringBuilder(Java) 中的字符串总长度 N≤N ，占用 O(N) 大小的额外空间。

#### 思路二

```python
class Solution:
    def reverseWords(self, s: str) -> str:
        s = s.strip() # 删除首尾空格
        res=s.split()#把这字符串按照空格来转成列表
        res.reverse()#字符串倒叙
        return " ".join(res)#转成这个字符串，按照空格隔开

```

## 顺子牌问题

解题思路：
根据题意，此 5 张牌是顺子的 充分条件 如下：

除大小王外，所有牌 无重复 ；
设此 5 张牌中最大的牌为 max ，最小的牌为 min （大小王除外），则需满足：
max - min < 5
max−min<5

因而，可将问题转化为：此 5张牌是否满足以上两个条件？



方法一： 集合 Set + 遍历
遍历五张牌，遇到大小王（即 0 ）直接跳过。
判别重复： 利用 Set 实现遍历判重， Set 的查找方法的时间复杂度为 O(1；
获取最大 / 最小的牌： 借助辅助变量 max1 和 min1 ，遍历统计即可。
复杂度分析：
时间复杂度 O(N) = O(5) = O(1)： 其中 N 为 nums 长度，本题中 N≡5 ；遍历数组使用 O(N)时间。
空间复杂度 O(N) = O(5) = O(1) ： 用于判重的辅助 Set 使用 O(N) 额外空间。



```python
class Solution:
    def isStraight(self, nums: List[int]) -> bool:
        #只要是不重复的数据都是可以的
        res=set()
        max1=0
        min1=14
        for num in nums:
            if num ==0 : continue #如果数字是0那么直接跳出来
            if num in res: return False#如果有重复数字，直接判断
            max1=max(max1,num)#存储最大最小值
            min1=min(min1,num)
            res.add(num)#存着看着是否重复的东西
        return max1-min1<5
```

方法二：排序 + 遍历
先对数组执行排序。
判别重复： 排序数组中的相同元素位置相邻，因此可通过遍历数组，判断]nums[i]=nums[i+1] 是否成立来判重。
获取最大 / 最小的牌： 排序后，数组末位元素 nums[4] 为最大牌；元素 nums[joker]为最小牌，其中 joker 为大小王的数量。
复杂度分析：
时间复杂度 O(NlogN)=O(5log5)=O(1) ： 其中 N 为 nums 长度，本题中 N≡5 ；数组排序使用O(NlogN) 时间。
空间复杂度 O(1) ： 变量 joker 使用 O(1) 大小的额外空间。

```python
class Solution:
    def isStraight(self, nums: List[int]) -> bool:
        nums.sort()#先进行排序
        joker=0
        for i in range(len(nums)):
            if nums[i]==0:joker+=1#探寻有多少的个大小王，最后nums【joker】就是最小的非零值
            elif nums[i]==nums[i-1]:return False#有重复直接出来
        return nums[4]-nums[joker]<5#看看能不能用0来进行补充

```

## 不使用加减法来求解加法

### 本质上使用的是位运算问题

解题思路：
本题考察对位运算的灵活使用，即使用位运算实现加法。
设两数字的二进制形式 a, b，其求和 s = a + b，a(i) 代表 a 的二进制第 i位，则分为以下四种情况：

a(i)	b(i) 无进位和 n(i）	进位 c(i+1)
00	 00	       00	                00
00	 11	       11	                00
11	 00	       11	                00
11	 11	       00	                11
![image-20200628112205132](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200628112205132.png)




![Picture1.png](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/56d56524d8d2b1318f78e209fffe0e266f97631178f6bfd627db85fcd2503205-Picture1.png)

```python
class Solution {
    public int add(int a, int b) {
        while(b != 0) { // 当进位为 0 时跳出
            int c = (a & b) << 1;  // c = 进位
            a ^= b; // a = 非进位和
            b = c; // b = 进位
        }
        return a;
    }
}
```

### 另外的思路

一. 十进制计算
大家都举5+7的例子。那我另想一个例子，计算十进制13+9：

1.计算不进位的和。十位1不变，个位3加9等于2，结果为12；
2.计算进位。十位没进位，个位进位为1，结果为10。

再计算十进制12+10：
1.计算不进位的和。十位1加1等于2，个位2加0等于2，结果为22；
2.计算进位。十位没进位，个位也没进位，结果为0。

因此结果13+9=22。

二. 二进制计算
13二进制为：1101，9二进制为：1001。

十进制是遇到大于等于10就保留余数，然后进位1。
那对应到二进制，就是遇到2就保留余数0，然后进位1。（二进制位之和不可能大于2）

计算二进制1101+1001：
1.计算不进位的和。从左到右，第1位为0，第2位为1，第3位为0，第4位为0，结果为0100；
2.计算进位。从左到右，第1位进位1，第2、3位没有进位，第4位进位1，结果为1001。不对，进位右边要补0，正确结果是10010。

计算二进制0100+10010：
1.计算不进位的和：10110；
2.计算进位：无。

因此结果为10110=22。

三.二进制加法公式

1）分析上面对二进制的计算过程，不难发现：
1.计算不进位的和，相当于对两个数进制异或：1101^1001=0100；
2.计算进位，第1位相当于对两个数求与：1101&1001=1001，然后再对其进行左移1位：1001<<1=10010。
然后再重复以上两个步骤。这里再异或一次就得到结果了，没进位：0100^10010=10110=22。

2）计算a+b，等价于(a^b)+((a&b)<<1)。
由于公式中又出现了+号，因此要再重复2）这个等价的计算过程。
结束条件是：没有进位了。

## 约瑟夫环

### 问题转换
既然约塞夫问题就是用人来举例的，那我们也给每个人一个编号（索引值），每个人用字母代替

下面这个例子是N=8 m=3的例子

我们定义F(n,m)表示最后剩下那个人的索引号，因此我们只关系最后剩下来这个人的索引号的变化情况即可

![约瑟夫环1.png](https://pic.leetcode-cn.com/d7768194055df1c3d3f6b503468704606134231de62b4ea4b9bdeda7c58232f4-约瑟夫环1.png)

从8个人开始，每次杀掉一个人，去掉被杀的人，然后把杀掉那个人之后的第一个人作为开头重新编号

1、第一次C被杀掉，人数变成7，D作为开头，（最终活下来的G的编号从6变成3）

2、第二次F被杀掉，人数变成6，G作为开头，（最终活下来的G的编号从3变成0）
3、第三次A被杀掉，人数变成5，B作为开头，（最终活下来的G的编号从0变成3）

4、以此类推，当只剩一个人时，他的编号必定为0！（重点！）

### 最终活着的人编号的反推
现在我们知道了G的索引号的变化过程，那么我们反推一下
从N = 7 到N = 8 的过程

如何才能将N = 7 的排列变回到N = 8 呢？

我们先把被杀掉的C补充回来，然后右移m个人，发现溢出了，再把溢出的补充在最前面

神奇了 经过这个操作就恢复了N = 8 的排列了！

![约瑟夫环2.png](https://pic.leetcode-cn.com/68509352d82d4a19678ed67a5bde338f86c7d0da730e3a69546f6fa61fb0063c-约瑟夫环2.png)

![image-20200628115434865](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200628115434865.png)

### 自己的理解

本质上就从后往前推导的过程问题，最后活下来的一定是编号为0，由于推导前一个被杀的人和后一个被杀的人满足递推关系，直接循环出来就可以

```java
class Solution {
public:
    int lastRemaining(int n, int m) {
        int pos = 0; // 最终活下来那个人的初始位置
        for(int i = 2; i <= n; i++){
            pos = (pos + m) % i;  // 每次循环右移
        }
        return pos;
    }
};

```

```python
class Solution:
    def lastRemaining(self, n: int, m: int) -> int:
        pos=0
        for i in range(2,n+1):
            pos=(pos+m)%i#不断地进行递推关系
        return pos
```

## 数值乘法

给定一个数组 A[0,1,…,n-1]，请构建一个数组 B[0,1,…,n-1]，其中 B 中的元素 B[i]=A[0]×A[1]×…×A[i-1]×A[i+1]×…×A[n-1]。不能使用除法。

 

示例:

输入: [1,2,3,4,5]
输出: [120,60,40,30,24]


提示：

所有元素乘积之和不会溢出 32 位整数
a.length <= 100000



注意每一个点处都少了一个相乘元素



### 解题思路

解题思路：
本题的难点在于不能使用除法 ，即需要 只用乘法 生成数组 B 。根据题目对 B[i]的定义，可列表格，如下图所示。

![Picture1.png](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/6056c7a5009cb7a4674aab28505e598c502a7f7c60c45b9f19a8a64f31304745-Picture1.png)

根据表格的主对角线（全为 1 ），可将表格分为 上三角 和 下三角 两部分。分别迭代计算下三角和上三角两部分的乘积，即可 不使用除法 就获得结果。

![image-20200628121441998](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200628121441998.png)

![image-20200628121541356](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20200628121541356.png)

![image-20200628121623620](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20200628121623620.png)

```python
class Solution:
    def constructArr(self, a: List[int]) -> List[int]:
        b, tmp = [1] * len(a), 1
        for i in range(1, len(a)):
            b[i] = b[i - 1] * a[i - 1] # 计算每一行的下三角部分
        for i in range(len(a) - 2, -1, -1): 
            tmp *= a[i + 1] # 计算每一行上三角部分
            b[i] *= tmp # 下三角 * 上三角=要求的数值
        return b
    
    class Solution:
    def constructArr(self, a: List[int]) -> List[int]:
        b=[1 for i in range(len(a))]
        tem=1
        for j in range(2,len(a)):
            b[j]=b[j-1]*a[j-1]
        for p in range(len(a)-2,-1,-1):
            tem*=a[p+1]
            b[p]*=tem
        return b

```



![image-20200628155644921](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20200628155644921.png)

![Picture1.png](https://pic.leetcode-cn.com/73153e75d74b1f48ac47244681caacc8ad20ca2ffd2dee2f70a2768dee09d073-Picture1.png)

![image-20200628155732505](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200628155732505.png)

![image-20200628155815609](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200628155815609.png)

```python
class Solution:
    def maxValue(self, grid: List[List[int]]) -> int:
        m,n=len(grid),len(grid[0])
        for i in range(1,m):
            grid[i][0]+=grid[i-1][0]
        for j in range(1,n):
            grid[0][j]+=grid[0][j-1]
        for i in range(1,m):
            for j in range(1,n):
                grid[i][j]+=max(grid[i-1][j],grid[i][j-1])
        return grid[-1][-1]

```



## 买卖股票的时机

![image-20200628161756357](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200628161756357.png)

![Picture1.png](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/4880911383c41712612103c612e390f1ee271e4eb921f22476836dc46aa3a58a-Picture1.png)

![image-20200628161908000](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200628161908000.png)

https://leetcode-cn.com/problems/gu-piao-de-zui-da-li-run-lcof/solution/mian-shi-ti-63-gu-piao-de-zui-da-li-run-dong-tai-2/

```python
class Solution:
    def maxProfit(self, prices: List[int]) -> int:
        cost, profit = float("+inf"), 0 #设置具体的情况，cost为最低价格 profit存储当前阶段的最大利润
        for price in prices:#遍历价格列表
            cost = min(cost, price)
            profit = max(profit, price - cost)
        return profit

```





## 丑数

![image-20200628164739402](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200628164739402.png)

![Picture1.png](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/837411664f096417badf857fa51e77fd30cb1309a5637c37d24d8a4a48a42b03-Picture1.png)

![image-20200628164818499](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20200628164818499.png)

![sword-for-offer-49.gif](https://krahets.gitee.io/assets/img/sword-for-offer-49.b4111f60.gif)

```python
class Solution:
    def nthUglyNumber(self, n: int) -> int:
        dp, a, b, c = [1] * n, 0, 0, 0 #dp存储每一个的数据。abc都弄在上边
        for i in range(1, n):
            n2, n3, n5 = dp[a] * 2, dp[b] * 3, dp[c] * 5
            dp[i] = min(n2, n3, n5)#每一个递推位置都填满，但是要注意一定是前一个ABC倍数最小值
            if dp[i] == n2: a += 1
            if dp[i] == n3: b += 1
            if dp[i] == n5: c += 1
                #取到哪个点都需要及时的更新
        return dp[-1]
```



## 奇数偶数排列

首尾双指针
定义头指针 left ，尾指针 right .
left一直往右移，直到它指向的值为偶数
right 一直往左移， 直到它指向的值为奇数
交换 nums[left] 和 nums[right] .
重复上述操作，直到 left == right.





![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/f25bd8d3c3fd5d30969be2954685a21f67e254a6487c6d9d27edf6589a0fca55.gif)

```python
class Solution:
    def exchange(self, nums: List[int]) -> List[int]:
        front,end=0,len(nums)-1
        while front<end:
            nums[front],nums[end]=nums[end],nums[front]#就应该放在前面，放在后边的话就会重复交换问题
            while  nums[front]%2==1 and front<len(nums)-1:#从左往右，奇数就跳过，偶数退出并且记下该处坐标
                front+=1
            while  nums[end]%2==0 and end >0:#从右往左，偶数就跳过，奇数退出并且记下该处坐标
                end -=1
        return nums
```

```python
#使用快慢指针
class Solution:
    def exchange(self, nums: List[int]) -> List[int]:
        low,fast=0,0
        while fast <len(nums):#当fast遇到奇数时候需要放到low指定地方去
            if nums[fast]%2==1:
                nums[low],nums[fast]=nums[fast],nums[low]
                low+=1
            fast+=1 #一直往前走
        return nums

```

## 异或求解不同数据

来自剑指offer
本题如果是一个数组中除一个数字只出现一次外，其他数字都出现2次。那么如何找出这个只出现一次的数字？
答：由于一个数异或他本身等于0，那么将这个数组中的数依次异或，最终就可以得到那个只出现一次的数。

本例中，这个数组中有两个数字只出现一次，剩下的数字都是出现两次的。如何找到这两个只出现一次的数字。
答：同样将整个数组异或，异或之后，得到一个数字，这个数字从二进制数的角度看，一定有位数为1。
这个1是怎么来的？就是那两个只出现一次的数字，异或得到的，他们对应的位不一样，肯定是一个数对应位数出现的是0，另一个数字对应位置上出现的数字是1.据此，将这个数组分成两个部分。
一部分是，对应位置出现的数是0的数组
一部分是，对应位置出现的数是1的数组
这样，整个数据就分成了两个数组，且这两个数组的特点是，数组中只有1个数只出现了一次。因此就可以求出那两个数字。

代码解释：
xorNumber为对整个数组求异或，用于求出分组条件。
onePosition表示最低位1的位置的数，n&-n是求一个二进制数的最低位的1对应的数。（除其所在最低位为1，其他位为0的一个数）
参考链接：https://blog.csdn.net/o_ohello/article/details/86663613
最后利用onePosition作为划分条件，将数组分成两个数组，最终求得ans1与ans2的值。
因为ans1与ans2的初始值为0，最终结果也应当异或一下0，不过由于0异或任何数都是，其本身，因此有没有都可以。

```java
class Solution {
    public int[] singleNumbers(int[] nums) {
        int xorNumber = nums[0];
        for(int k=1; k<nums.length; k++){
            xorNumber ^=nums[k];
        }#求出两个不同数据异或结果
        int onePosition = xorNumber&(-xorNumber);#求解出异或结果之中最低位为1，其余都为0的数据作为分组标准
        int ans1 = 0, ans2 = 0;
        for(int i=0; i<nums.length; i++){
            if((nums[i]&onePosition)==onePosition){#当数组之中数据和分组标准相与，一定会得到两个结果，分到两组之中，每一组都有一个出现一次的数据，最终分组进行异或就可以
                ans1^=nums[i];
            }else{
                ans2^=nums[i];
            }
        }
        
        return new int[] {ans1^0, ans2^0};
    }
}

```

