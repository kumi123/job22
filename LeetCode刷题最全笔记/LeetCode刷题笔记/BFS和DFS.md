# BFS和DFS

本期例题为 LeetCode「岛屿问题」系列：

- **LeetCode 463. Island Perimeter** 岛屿的周长（Easy）
- **LeetCode 695. Max Area of Island** 岛屿的最大面积（Medium）
- **LeetCode 827. Making A Large Island** 填海造陆（Hard）

我们所熟悉的 DFS（深度优先搜索）问题通常是在树或者图结构上进行的。而我们今天要讨论的 DFS 问题，是在一种「网格」结构中进行的。岛屿问题是这类网格 DFS 问题的典型代表。网格结构遍历起来要比二叉树复杂一些，如果没有掌握一定的方法，DFS 代码容易写得冗长繁杂。

本文将以岛屿问题为例，展示网格类问题 DFS 通用思路，以及如何让代码变得简洁。主要内容包括：

- 网格类问题的基本性质
- 在网格中进行 DFS 遍历的方法与技巧
- 三个岛屿问题的解法
- 相关题目

## 网格类问题的 DFS 遍历方法

### 网格问题的基本概念

我们首先明确一下岛屿问题中的网格结构是如何定义的，以方便我们后面的讨论。

网格问题是由 个小方格组成一个网格，每个小方格与其上下左右四个方格认为是相邻的，要在这样的网格上进行某种搜索。

岛屿问题是一类典型的网格问题。每个格子中的数字可能是 0 或者 1。我们把数字为 0 的格子看成海洋格子，数字为 1 的格子看成陆地格子，这样相邻的陆地格子就连接成一个岛屿。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcib8CSJjOnbGUamCj2B7OiclOvfCa2mN4vYZpiaUicV2YeKibVCXmQW0TTQ8U4EkHCRyibHLogpl5malQLPw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)岛屿问题示例

在这样一个设定下，就出现了各种岛屿问题的变种，包括岛屿的数量、面积、周长等。不过这些问题，基本都可以用 DFS 遍历来解决。

### DFS 的基本结构

网格结构要比二叉树结构稍微复杂一些，它其实是一种简化版的**图**结构。要写好网格上的 DFS 遍历，我们首先要理解二叉树上的 DFS 遍历方法，再类比写出网格结构上的 DFS 遍历。我们写的二叉树 DFS 遍历一般是这样的：

```java
void traverse(TreeNode root) {
    // 判断 base case
    if (root == null) {
        return;
    }
    // 访问两个相邻结点：左子结点、右子结点
    traverse(root.left);
    traverse(root.right);
}
```

可以看到，二叉树的 DFS 有两个要素：「**访问相邻结点**」和「**判断 base case**」。

第一个要素是**访问相邻结点**。二叉树的相邻结点非常简单，只有左子结点和右子结点两个。二叉树本身就是一个递归定义的结构：一棵二叉树，它的左子树和右子树也是一棵二叉树。那么我们的 DFS 遍历只需要递归调用左子树和右子树即可。

第二个要素是 **判断 base case**。一般来说，二叉树遍历的 base case 是 `root == null`。这样一个条件判断其实有两个含义：一方面，这表示 `root` 指向的子树为空，不需要再往下遍历了。另一方面，在 `root == null` 的时候及时返回，可以让后面的 `root.left` 和 `root.right` 操作不会出现空指针异常。

对于网格上的 DFS，我们完全可以参考二叉树的 DFS，写出网格 DFS 的两个要素：

首先，网格结构中的格子有多少相邻结点？答案是上下左右四个。对于格子 `(r, c)` 来说（r 和 c 分别代表行坐标和列坐标），四个相邻的格子分别是 `(r-1, c)`、`(r+1, c)`、`(r, c-1)`、`(r, c+1)`。换句话说，网格结构是「四叉」的。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcib8CSJjOnbGUamCj2B7OiclOv9IsSuia3DE42FJWfpThMFlfng0OKu0nsPmwDv2J5BHUkSibGy0KbA4Fg/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)网格结构中四个相邻的格子

其次，网格 DFS 中的 base case 是什么？从二叉树的 base case 对应过来，应该是网格中不需要继续遍历、`grid[r][c]` 会出现数组下标越界异常的格子，也就是那些超出网格范围的格子。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcib8CSJjOnbGUamCj2B7OiclOvddOM2NyLmWmXcwicNe3aUI1U6eYFpq3WibINqFgZAkmicpxjbsRP0SrSw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)网格 DFS 的 base case

这一点稍微有些反直觉，坐标竟然可以临时超出网格的范围？这种方法我称为「先污染后治理」—— 甭管当前是在哪个格子，先往四个方向走一步再说，如果发现走出了网格范围再赶紧返回。这跟二叉树的遍历方法是一样的，先递归调用，发现 `root == null` 再返回。

这样，我们得到了网格 DFS 遍历的框架代码：

```c++
void dfs(int[][] grid, int r, int c) {
    // 判断 base case
    // 如果坐标 (r, c) 超出了网格范围，直接返回
    if (!inArea(grid, r, c)) {
        return;
    }
    // 访问上、下、左、右四个相邻结点
    dfs(grid, r - 1, c);
    dfs(grid, r + 1, c);
    dfs(grid, r, c - 1);
    dfs(grid, r, c + 1);
}

// 判断坐标 (r, c) 是否在网格中
boolean inArea(int[][] grid, int r, int c) {
    return 0 <= r && r < grid.length 
         && 0 <= c && c < grid[0].length;
}
```

### 如何避免重复遍历

网格结构的 DFS 与二叉树的 DFS 最大的不同之处在于，遍历中可能遇到遍历过的结点。这是因为，网格结构本质上是一个「图」，我们可以把每个格子看成图中的结点，每个结点有向上下左右的四条边。在图中遍历时，自然可能遇到重复遍历结点。

这时候，DFS 可能会不停地「兜圈子」，永远停不下来，如下图所示：

![img](https://mmbiz.qpic.cn/mmbiz_gif/TKAD4axFcib8CSJjOnbGUamCj2B7OiclOvhNwXiaoJdXnDNUGpmtqqlHIbPnpejyAVqnQqODmYMIxovmlLcn0xEicA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)DFS 遍历可能会兜圈子（动图）

如何避免这样的重复遍历呢？答案是标记已经遍历过的格子。以岛屿问题为例，我们需要在所有值为 1 的陆地格子上做 DFS 遍历。每走过一个陆地格子，就把格子的值改为 2，这样当我们遇到 2 的时候，就知道这是遍历过的格子了。也就是说，每个格子可能取三个值：

- 0 —— 海洋格子
- 1 —— 陆地格子（未遍历过）
- 2 —— 陆地格子（已遍历过）

我们在框架代码中加入避免重复遍历的语句：

```java
void dfs(int[][] grid, int r, int c) {
    // 判断 base case
    if (!inArea(grid, r, c)) {
        return;
    }
    // 如果这个格子不是岛屿，直接返回
    if (grid[r][c] != 1) {
        return;
    }
    grid[r][c] = 2; // 将格子标记为「已遍历过」
    
    // 访问上、下、左、右四个相邻结点
    dfs(grid, r - 1, c);
    dfs(grid, r + 1, c);
    dfs(grid, r, c - 1);
    dfs(grid, r, c + 1);
}

// 判断坐标 (r, c) 是否在网格中
boolean inArea(int[][] grid, int r, int c) {
    return 0 <= r && r < grid.length 
         && 0 <= c && c < grid[0].length;
}
```

![img](https://mmbiz.qpic.cn/mmbiz_gif/TKAD4axFcib8CSJjOnbGUamCj2B7OiclOvd7rGNeVFazWpx83b9hwSBAKvRthwn43QC5cbnZGMnPHUgkUML4aIkA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)标记已遍历的格子

这样，我们就得到了一个岛屿问题、乃至各种网格问题的通用 DFS 遍历方法。以下所讲的几个例题，其实都只需要在 DFS 遍历框架上稍加修改而已。

> **小贴士：**
>
> 在一些题解中，可能会把「已遍历过的陆地格子」标记为和海洋格子一样的 0，美其名曰「陆地沉没方法」，即遍历完一个陆地格子就让陆地「沉没」为海洋。这种方法看似很巧妙，但实际上有很大隐患，因为这样我们就无法区分「海洋格子」和「已遍历过的陆地格子」了。如果题目更复杂一点，这很容易出 bug。

## 岛屿问题的解法

理解了网格结构的 DFS 遍历方法以后，岛屿问题就不难解决了。下面我们分别看看三个题目该如何用 DFS 遍历来求解。

### 例题 1：岛屿的最大面积

**LeetCode 695. Max Area of Island** （Medium）

> 给定一个包含了一些 0 和 1 的非空二维数组 `grid`，一个岛屿是一组相邻的 1（代表陆地），这里的「相邻」要求两个 1 必须在水平或者竖直方向上相邻。你可以假设 `grid` 的四个边缘都被 0（代表海洋）包围着。
>
> 找到给定的二维数组中最大的岛屿面积。如果没有岛屿，则返回面积为 0 。

这道题目只需要对每个岛屿做 DFS 遍历，求出每个岛屿的面积就可以了。求岛屿面积的方法也很简单，代码如下，每遍历到一个格子，就把面积加一。

```
int area(int[][] grid, int r, int c) {  
    return 1 
        + area(grid, r - 1, c)
        + area(grid, r + 1, c)
        + area(grid, r, c - 1)
        + area(grid, r, c + 1);
}
```

最终我们得到的完整题解代码如下：

```c++
public int maxAreaOfIsland(int[][] grid) {
    int res = 0;
    for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[0].length; c++) {
            if (grid[r][c] == 1) {
                int a = area(grid, r, c);
                res = Math.max(res, a);
            }
        }
    }
    return res;
}

int area(int[][] grid, int r, int c) {
    if (!inArea(grid, r, c)) {
        return 0;
    }
    if (grid[r][c] != 1) {
        return 0;
    }
    grid[r][c] = 2;
    
    return 1 
        + area(grid, r - 1, c)
        + area(grid, r + 1, c)
        + area(grid, r, c - 1)
        + area(grid, r, c + 1);
}

boolean inArea(int[][] grid, int r, int c) {
    return 0 <= r && r < grid.length 
         && 0 <= c && c < grid[0].length;
}
```

### 例题 2：填海造陆问题

**LeetCode 827. Making A Large Island** （Hard）

> 在二维地图上， 0 代表海洋，1代表陆地，我们最多只能将一格 0 （海洋）变成 1 （陆地）。进行填海之后，地图上最大的岛屿面积是多少？

这道题是岛屿最大面积问题的升级版。现在我们有填海造陆的能力，可以把一个海洋格子变成陆地格子，进而让两块岛屿连成一块。那么填海造陆之后，最大可能构造出多大的岛屿呢？

大致的思路我们不难想到，我们先计算出所有岛屿的面积，在所有的格子上标记出岛屿的面积。然后搜索哪个海洋格子相邻的两个岛屿面积最大。例如下图中红色方框内的海洋格子，上边、左边都与岛屿相邻，我们可以计算出它变成陆地之后可以连接成的岛屿面积为 。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcib8CSJjOnbGUamCj2B7OiclOv1hBJr1PNvOaHctNRUJalic7NibSotbQEMJnbibwiaeZ05FiaKrodTqZuZ7Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)一个海洋格子连接起两个岛屿

然而，这种做法可能遇到一个问题。如下图中红色方框内的海洋格子，它的上边、左边都与岛屿相邻，这时候连接成的岛屿面积难道是 ？显然不是。这两个 7 来自同一个岛屿，所以填海造陆之后得到的岛屿面积应该只有 。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcib8CSJjOnbGUamCj2B7OiclOvribX6kr8bFBNQxYyqbFXRXYY8ickDBmEibFTuZs4d1KgmgMCUVQjTsiarQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)一个海洋格子与同一个岛屿有两个边相邻

可以看到，要让算法正确，我们得能区分一个海洋格子相邻的两个 7 是不是来自同一个岛屿。那么，我们不能在方格中标记岛屿的面积，而应该标记岛屿的索引（下标），另外用一个数组记录每个岛屿的面积，如下图所示。这样我们就可以发现红色方框内的海洋格子，它的「两个」相邻的岛屿实际上是同一个。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcib8CSJjOnbGUamCj2B7OiclOvmhcyNNzBnr99s7fTPmoyHqrc31AGMANhQN1lt0gOXIibHbuc6ViadgeQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)标记每个岛屿的索引（下标）

可以看到，这道题实际上是对网格做了两遍 DFS：第一遍 DFS 遍历陆地格子，计算每个岛屿的面积并标记岛屿；第二遍 DFS 遍历海洋格子，观察每个海洋格子相邻的陆地格子。

这道题的基本思路就是这样，具体的代码还有一些需要注意的细节，但和本文的主题已经联系不大。各位可以自己思考一下如何把上述思路转化为代码。

### 例题 3：岛屿的周长

**LeetCode 463. Island Perimeter** （Easy）

> 给定一个包含 0 和 1 的二维网格地图，其中 1 表示陆地，0 表示海洋。网格中的格子水平和垂直方向相连（对角线方向不相连）。整个网格被水完全包围，但其中恰好有一个岛屿（一个或多个表示陆地的格子相连组成岛屿）。
>
> 岛屿中没有“湖”（“湖” 指水域在岛屿内部且不和岛屿周围的水相连）。格子是边长为 1 的正方形。计算这个岛屿的周长。
>
> ![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcib8CSJjOnbGUamCj2B7OiclOv8Uokdav68hF5tXP7QDIL4uqPluhfevgHUqQpwzRqiacBPoxdrURbcWQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)题目示例

实话说，这道题用 DFS 来解并不是最优的方法。对于岛屿，直接用数学的方法求周长会更容易。不过这道题是一个很好的理解 DFS 遍历过程的例题，不信你跟着我往下看。

我们再回顾一下 网格 DFS 遍历的基本框架：

```C++
void dfs(int[][] grid, int r, int c) {
    // 判断 base case
    if (!inArea(grid, r, c)) {
        return;
    }
    // 如果这个格子不是岛屿，直接返回
    if (grid[r][c] != 1) {
        return;
    }
    grid[r][c] = 2; // 将格子标记为「已遍历过」
    
    // 访问上、下、左、右四个相邻结点
    dfs(grid, r - 1, c);
    dfs(grid, r + 1, c);
    dfs(grid, r, c - 1);
    dfs(grid, r, c + 1);
}

// 判断坐标 (r, c) 是否在网格中
boolean inArea(int[][] grid, int r, int c) {
    return 0 <= r && r < grid.length 
         && 0 <= c && c < grid[0].length;
}
```

可以看到，`dfs` 函数直接返回有这几种情况：

- `!inArea(grid, r, c)`，即坐标 `(r, c)` 超出了网格的范围，也就是我所说的「先污染后治理」的情况

- `grid[r][c] != 1`，即当前格子不是岛屿格子，这又分为两种情况：

- - `grid[r][c] == 0`，当前格子是海洋格子
  - `grid[r][c] == 2`，当前格子是已遍历的陆地格子

那么这些和我们岛屿的周长有什么关系呢？实际上，岛屿的周长是计算岛屿全部的「边缘」，而这些边缘就是我们在 DFS 遍历中，`dfs` 函数返回的位置。观察题目示例，我们可以将岛屿的周长中的边分为两类，如下图所示。黄色的边是与网格边界相邻的周长，而蓝色的边是与海洋格子相邻的周长。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcib8CSJjOnbGUamCj2B7OiclOv2stseeeMwBkibibYJD0tNLX59nBQUW0MV8fNTr0ibsAdNg8O1YeO3WeKQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)将岛屿周长中的边分为两类

当我们的 `dfs` 函数因为「坐标 `(r, c)` 超出网格范围」返回的时候，实际上就经过了一条黄色的边；而当函数因为「当前格子是海洋格子」返回的时候，实际上就经过了一条蓝色的边。这样，我们就把岛屿的周长跟 DFS 遍历联系起来了，我们的题解代码也呼之欲出：

```c++
public int islandPerimeter(int[][] grid) {
    for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[0].length; c++) {
            if (grid[r][c] == 1) {
                // 题目限制只有一个岛屿，计算一个即可
                return dfs(grid, r, c);
            }
        }
    }
    return 0;
}

int dfs(int[][] grid, int r, int c) {
    
    //
    
    下边明显代表着具体的清苦
    // 函数因为「坐标 (r, c) 超出网格范围」返回，对应一条黄色的边
    if (!inArea(grid, r, c)) {
        return 1;
    }
    // 函数因为「当前格子是海洋格子」返回，对应一条蓝色的边
    if (grid[r][c] == 0) {
        return 1;
    }
    // 函数因为「当前格子是已遍历的陆地格子」返回，和周长没关系
    if (grid[r][c] != 1) {
        return 0;
    }
    grid[r][c] = 2;
    return dfs(grid, r - 1, c)
        + dfs(grid, r + 1, c)
        + dfs(grid, r, c - 1)
        + dfs(grid, r, c + 1);
}

// 判断坐标 (r, c) 是否在网格中
boolean inArea(int[][] grid, int r, int c) {
    return 0 <= r && r < grid.length 
         && 0 <= c && c < grid[0].length;
}
```

## 总结

对比完三个例题的题解代码，你会发现网格问题的代码真的都非常相似。其实这一类问题属于「会了不难」类型。了解树、图的基本遍历方法，再学会一点小技巧，掌握网格 DFS 遍历就一点也不难了。

岛屿类问题是网格类问题中的典型代表，做了几道岛屿类问题，再看其他的问题其实本质上和岛屿问题是一样的，例如 **LeetCode 130. Surrounded Regions** 这道题，将岛屿的 1 和 0 转换为字母 O 和 X，但本质上是完全一样的。

## 写法3

![img](https://pic.leetcode-cn.com/7e3a5726330949604314953a5cd5a08a8aec627d3c26e74051e848847d7d641f-200-dfs-1.png)

```python
from typing import List


class Solution:
    #        x-1,y
    # x,y-1    x,y      x,y+1
    #        x+1,y
    # 方向数组，它表示了相对于当前位置的 4 个方向的横、纵坐标的偏移量，这是一个常见的技巧
    directions = [(-1, 0), (0, -1), (1, 0), (0, 1)]

    def numIslands(self, grid: List[List[str]]) -> int:
        m = len(grid)
        # 特判
        if m == 0:
            return 0
        n = len(grid[0])
        marked = [[False for _ in range(n)] for _ in range(m)]
        count = 0
        # 从第 1 行、第 1 格开始，对每一格尝试进行一次 DFS 操作
        for i in range(m):
            for j in range(n):
                # 只要是陆地，且没有被访问过的，就可以使用 DFS 发现与之相连的陆地，并进行标记
                if not marked[i][j] and grid[i][j] == '1':
                    # count 可以理解为连通分量，你可以在深度优先遍历完成以后，再计数，
                    # 即这行代码放在【位置 1】也是可以的
                    count += 1
                    self.__dfs(grid, i, j, m, n, marked)
                    # 【位置 1】
        return count

    def __dfs(self, grid, i, j, m, n, marked):
        marked[i][j] = True
        for direction in self.directions:
            new_i = i + direction[0]
            new_j = j + direction[1]
            if 0 <= new_i < m and 0 <= new_j < n and not marked[new_i][new_j] and grid[new_i][new_j] == '1':
                self.__dfs(grid, new_i, new_j, m, n, marked)


                
if __name__ == '__main__':
    grid = [['1', '1', '1', '1', '0'],
            ['1', '1', '0', '1', '0'],
            ['1', '1', '0', '0', '0'],
            ['0', '0', '0', '0', '0']]
    solution = Solution()
    result = solution.numIslands(grid)
    print(result)

```

![img](https://pic.leetcode-cn.com/b34d1c49bea642363609e6d82d5fc43c7cc06de39e45d9f7273929e183d5d4ce-200-bfs-3.png)

```python
from typing import List
from collections import deque


class Solution:
    #        x-1,y
    # x,y-1    x,y      x,y+1
    #        x+1,y
    # 方向数组，它表示了相对于当前位置的 4 个方向的横、纵坐标的偏移量，这是一个常见的技巧
    directions = [(-1, 0), (0, -1), (1, 0), (0, 1)]

    def numIslands(self, grid: List[List[str]]) -> int:
        m = len(grid)
        # 特判
        if m == 0:
            return 0
        n = len(grid[0])
        marked = [[False for _ in range(n)] for _ in range(m)]
        count = 0
        # 从第 1 行、第 1 格开始，对每一格尝试进行一次 DFS 操作
        for i in range(m):
            for j in range(n):
                # 只要是陆地，且没有被访问过的，就可以使用 BFS 发现与之相连的陆地，并进行标记
                if not marked[i][j] and grid[i][j] == '1':
                    # count 可以理解为连通分量，你可以在广度优先遍历完成以后，再计数，
                    # 即这行代码放在【位置 1】也是可以的
                    count += 1
                    queue = deque()
                    queue.append((i, j))
                    # 注意：这里要标记上已经访问过
                    marked[i][j] = True
                    while queue:
                        cur_x, cur_y = queue.popleft()
                        # 得到 4 个方向的坐标
                        for direction in self.directions:
                            new_i = cur_x + direction[0]
                            new_j = cur_y + direction[1]
                            # 如果不越界、没有被访问过、并且还要是陆地，我就继续放入队列，放入队列的同时，要记得标记已经访问过
                            if 0 <= new_i < m and 0 <= new_j < n and not marked[new_i][new_j] and grid[new_i][new_j] == '1':
                                queue.append((new_i, new_j))
                                #【特别注意】在放入队列以后，要马上标记成已经访问过，语义也是十分清楚的：反正只要进入了队列，你迟早都会遍历到它
                                # 而不是在出队列的时候再标记
                                #【特别注意】如果是出队列的时候再标记，会造成很多重复的结点进入队列，造成重复的操作，这句话如果你没有写对地方，代码会严重超时的
                                marked[new_i][new_j] = True
                    #【位置 1】
        return count


if __name__ == '__main__':
    grid = [['1', '1', '1', '1', '0'],
            ['1', '1', '0', '1', '0'],
            ['1', '1', '0', '0', '0'],
            ['0', '0', '0', '0', '0']]
    # grid = [["1", "1", "1", "1", "1", "0", "1", "1", "1", "1", "1", "1", "1", "1", "1", "0", "1", "0", "1", "1"],
    #         ["0", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "0", "1", "1", "1", "1", "1", "0"],
    #         ["1", "0", "1", "1", "1", "0", "0", "1", "1", "0", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
    #         ["1", "1", "1", "1", "0", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
    #         ["1", "0", "0", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
    #         ["1", "0", "1", "1", "1", "1", "1", "1", "0", "1", "1", "1", "0", "1", "1", "1", "0", "1", "1", "1"],
    #         ["0", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "0", "1", "1", "0", "1", "1", "1", "1"],
    #         ["1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "0", "1", "1", "1", "1", "0", "1", "1"],
    #         ["1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "0", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
    #         ["1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
    #         ["0", "1", "1", "1", "1", "1", "1", "1", "0", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
    #         ["1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
    #         ["1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
    #         ["1", "1", "1", "1", "1", "0", "1", "1", "1", "1", "1", "1", "1", "0", "1", "1", "1", "1", "1", "1"],
    #         ["1", "0", "1", "1", "1", "1", "1", "0", "1", "1", "1", "0", "1", "1", "1", "1", "0", "1", "1", "1"],
    #         ["1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "0", "1", "1", "1", "1", "1", "1", "0"],
    #         ["1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "0", "1", "1", "1", "1", "0", "0"],
    #         ["1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
    #         ["1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
    #         ["1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"]]
    solution = Solution()
    result = solution.numIslands(grid)
    print(result)


```

## BFS

DFS（深度优先搜索）和 BFS（广度优先搜索）就像孪生兄弟，提到一个总是想起另一个。然而在实际使用中，我们用 DFS 的时候远远多于 BFS。那么，是不是 BFS 就没有什么用呢？

如果我们使用 DFS/BFS 只是为了遍历一棵树、一张图上的所有结点的话，那么 DFS 和 BFS 的能力没什么差别，我们当然更倾向于更方便写、空间复杂度更低的 DFS 遍历。不过，某些使用场景是 DFS 做不到的，只能使用 BFS 遍历。这就是本文要介绍的两个场景：「层序遍历」、「最短路径」。

本文包括以下内容：

- DFS 与 BFS 的特点比较
- BFS 的适用场景
- 如何用 BFS 进行层序遍历
- 如何用 BFS 求解最短路径问题

## DFS 与 BFS

让我们先看看在二叉树上进行 DFS 遍历和 BFS 遍历的代码比较。

DFS 遍历使用**递归**：

```C++
void dfs(TreeNode root) {
    if (root == null) {
        return;
    }
    dfs(root.left);
    dfs(root.right);
}
```

BFS 遍历使用**队列**数据结构：

```C++
void bfs(TreeNode root) {
    Queue<TreeNode> queue = new ArrayDeque<>();
    queue.add(root);
    while (!queue.isEmpty()) {
        TreeNode node = queue.poll(); // Java 的 pop 写作 poll()
        if (node.left != null) {
            queue.add(node.left);
        }
        if (node.right != null) {
            queue.add(node.right);
        }
    }
}
```

只是比较两段代码的话，最直观的感受就是：DFS 遍历的代码比 BFS 简洁太多了！这是因为递归的方式隐含地使用了系统的**栈**，我们不需要自己维护一个数据结构。如果只是简单地将二叉树遍历一遍，那么 DFS 显然是更方便的选择。

虽然 DFS 与 BFS 都是将二叉树的所有结点遍历了一遍，但它们遍历结点的顺序不同。

![img](https://mmbiz.qpic.cn/mmbiz_gif/TKAD4axFcibicpic2kCo7q8saB8PYemyCbY8MxP7X9fcX22IkBcvXAciaiatzwhhtFoBzhnZcbTSVWBzsU7mficxA3EQ/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)DFS 与 BFS 对比

这个遍历顺序也是 BFS 能够用来解「层序遍历」、「最短路径」问题的根本原因。下面，我们结合几道例题来讲讲 BFS 是如何求解层序遍历和最短路径问题的。

## BFS 的应用一：层序遍历

**LeetCode 102. Binary Tree Level Order Traversal** 二叉树的层序遍历（Medium）

> 给定一个二叉树，返回其按层序遍历得到的节点值。层序遍历即逐层地、从左到右访问所有结点。

什么是层序遍历呢？简单来说，层序遍历就是把二叉树分层，然后每一层从左到右遍历：

![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcibicpic2kCo7q8saB8PYemyCbYXoJIHsfzFwlbdwhWJKungO9wbaCp4nj2ic7vOKH2zDKVmrYZic6xAebA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)二叉树的层序遍历

乍一看来，这个遍历顺序和 BFS 是一样的，我们可以直接用 BFS 得出层序遍历结果。然而，层序遍历要求的输入结果和 BFS 是不同的。层序遍历要求我们区分每一层，也就是返回一个二维数组。而 BFS 的遍历结果是一个一维数组，无法区分每一层。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcibicpic2kCo7q8saB8PYemyCbYTfBWibXUx4gKKgp46OQkUJuiaVxnFicLYe2xT3BeU2vXHAYRlkwYPPRmw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)BFS 遍历与层序遍历的输出结果不同

那么，怎么给 BFS 遍历的结果分层呢？我们首先来观察一下 BFS 遍历的过程中，结点进队列和出队列的过程：

![img](https://mmbiz.qpic.cn/mmbiz_gif/TKAD4axFcibicpic2kCo7q8saB8PYemyCbY7qVMwDU55wz0VibMyfXpEbOFEcKxeFjlbXd2NESWwwYgsH4wsM7hYCQ/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)BFS 遍历的过程（动图）

截取 BFS 遍历过程中的某个时刻：

![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcibicpic2kCo7q8saB8PYemyCbYdhm4jw1jQ3dHnEzBk0kC8LgUfDpCvF881SGKPUmiaOfdwlB8XS7xHWA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)BFS 遍历中某个时刻队列的状态

可以看到，此时队列中的结点是 3、4、5，分别来自第 1 层和第 2 层。这个时候，第 1 层的结点还没出完，第 2 层的结点就进来了，而且两层的结点在队列中紧挨在一起，我们**无法区分队列中的结点来自哪一层**。

因此，我们需要稍微修改一下代码，在每一层遍历开始前，先记录队列中的结点数量 （也就是这一层的结点数量），然后一口气处理完这一层的 个结点。

```C++
// 二叉树的层序遍历
void bfs(TreeNode root) {
    Queue<TreeNode> queue = new ArrayDeque<>();
    queue.add(root);
    while (!queue.isEmpty()) {
        int n = queue.size();
        for (int i = 0; i < n; i++) { 
            // 变量 i 无实际意义，只是为了循环 n 次
            TreeNode node = queue.poll();
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
    }
}
```

这样，我们就将 BFS 遍历改造成了层序遍历。在遍历的过程中，结点进队列和出队列的过程为：

![img](https://mmbiz.qpic.cn/mmbiz_gif/TKAD4axFcibicpic2kCo7q8saB8PYemyCbY3NNEDB3C7YrWBibN3IfvpukEniag6KCrkREzicS1zYs8kjjkHPFCficGWg/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)BFS 层序遍历的过程（动图）

可以看到，在 while 循环的每一轮中，都是将当前层的所有结点出队列，再将下一层的所有结点入队列，这样就实现了层序遍历。

最终我们得到的题解代码为：

```C++
public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> res = new ArrayList<>();

    Queue<TreeNode> queue = new ArrayDeque<>();
    if (root != null) {
        queue.add(root);
    }
    while (!queue.isEmpty()) {
        int n = queue.size();
        List<Integer> level = new ArrayList<>();
        for (int i = 0; i < n; i++) { 
            TreeNode node = queue.poll();
            level.add(node.val);
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
        res.add(level);
    }

    return res;
}
```

## BFS 的应用二：最短路径

在一棵树中，一个结点到另一个结点的路径是唯一的，但在图中，结点之间可能有多条路径，其中哪条路最近呢？这一类问题称为**最短路径问题**。最短路径问题也是 BFS 的典型应用，而且其方法与层序遍历关系密切。

在二叉树中，BFS 可以实现一层一层的遍历。在图中同样如此。从源点出发，BFS 首先遍历到第一层结点，到源点的距离为 1，然后遍历到第二层结点，到源点的距离为 2…… 可以看到，用 BFS 的话，距离源点更近的点会先被遍历到，这样就能找到到某个点的最短路径了。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/TKAD4axFcibicpic2kCo7q8saB8PYemyCbYmr4TFR7EN209BK7JIib6Khs21GZVtMkA6aunUqrAvlibZfay064Day7A/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)层序遍历与最短路径

> **小贴士：**
>
> 很多同学一看到「最短路径」，就条件反射地想到「Dijkstra 算法」。为什么 BFS 遍历也能找到最短路径呢？
>
> 这是因为，Dijkstra 算法解决的是**带权最短路径问题**，而我们这里关注的是**无权最短路径问题**。也可以看成每条边的权重都是 1。这样的最短路径问题，用 BFS 求解就行了。
>
> 在面试中，你可能更希望写 BFS 而不是 Dijkstra。毕竟，敢保证自己能写对 Dijkstra 算法的人不多。

最短路径问题属于图算法。由于图的表示和描述比较复杂，本文用比较简单的网格结构代替。网格结构是一种特殊的图，它的表示和遍历都比较简单，适合作为练习题。在 LeetCode 中，最短路径问题也以网格结构为主。

### 最短路径例题讲解

**LeetCode 1162. As Far from Land as Possible** 离开陆地的最远距离（Medium）

> 你现在手里有一份大小为 的地图网格 `grid`，上面的每个单元格都标记为 0 或者 1，其中 0 代表海洋，1 代表陆地，请你找出一个海洋区域，这个海洋区域到离它最近的陆地区域的距离是最大的。
>
> 我们这里说的距离是「曼哈顿距离」。 和 这两个区域之间的距离是 。
>
> 如果我们的地图上只有陆地或者海洋，请返回 -1。

这道题就是一个在网格结构中求最短路径的问题。同时，它也是一个「岛屿问题」，即用网格中的 1 和 0 表示陆地和海洋，模拟出若干个岛屿。

在上一篇文章中，我们介绍了网格结构的基本概念，以及网格结构中的 DFS 遍历。其中一些概念和技巧也可以用在 BFS 遍历中：

- 格子 `(r, c)` 的相邻四个格子为：`(r-1, c)`、`(r+1, c)`、`(r, c-1)` 和 `(r, c+1)`；
- 使用函数 `inArea` 判断当前格子的坐标是否在网格范围内；
- 将遍历过的格子标记为 2，避免重复遍历。

对于网格结构的性质、网格结构的 DFS 遍历技巧不是很了解的同学，可以复习一下上一篇文章：[LeetCode 例题精讲 | 12 岛屿问题：网格结构中的 DFS](https://mp.weixin.qq.com/s?__biz=MzA5ODk3ODA4OQ==&mid=2648167208&idx=1&sn=d8118c7c0e0f57ea2bdd8aa4d6ac7ab7&chksm=88aa236ebfddaa78a6183cf6dcf88f82c5ff5efb7f5c55d6844d9104b307862869eb9032bd1f&token=1064083695&lang=zh_CN&scene=21#wechat_redirect)。

上一篇文章讲过了网格结构 DFS 遍历，这篇文章正好讲解一下网格结构的 BFS 遍历。要解最短路径问题，我们首先要写出层序遍历的代码，仿照上面的二叉树层序遍历代码，类似地可以写出网格层序遍历：

```C++
// 网格结构的层序遍历
// 从格子 (i, j) 开始遍历
void bfs(int[][] grid, int i, int j) {
    Queue<int[]> queue = new ArrayDeque<>();
    queue.add(new int[]{r, c});
    while (!queue.isEmpty()) {
        int n = queue.size();
        for (int i = 0; i < n; i++) { 
            int[] node = queue.poll();
            int r = node[0];
            int c = node[1];
            if (r-1 >= 0 && grid[r-1][c] == 0) {
                grid[r-1][c] = 2;
                queue.add(new int[]{r-1, c});
            }
            if (r+1 < N && grid[r+1][c] == 0) {
                grid[r+1][c] = 2;
                queue.add(new int[]{r+1, c});
            }
            if (c-1 >= 0 && grid[r][c-1] == 0) {
                grid[r][c-1] = 2;
                queue.add(new int[]{r, c-1});
            }
            if (c+1 < N && grid[r][c+1] == 0) {
                grid[r][c+1] = 2;
                queue.add(new int[]{r, c+1});
            }
        }
    }
}
```

以上的层序遍历代码有几个注意点：

- 队列中的元素类型是 `int[]` 数组，每个数组的长度为 2，包含格子的行坐标和列坐标。
- 为了避免重复遍历，这里使用到了和 DFS 遍历一样的技巧：把已遍历的格子标记为 2。注意：我们在将格子放入队列之前就将其标记为 2。想一想，这是为什么？
- 在将格子放入队列之前就检查其坐标是否在网格范围内，避免将「不存在」的格子放入队列。

这段网格遍历代码还有一些可以优化的地方。由于一个格子有四个相邻的格子，代码中判断了四遍格子坐标的合法性，代码稍微有点啰嗦。我们可以用一个 `moves` 数组存储相邻格子的四个方向：

```C++
int[][] moves = {
    {-1, 0}, {1, 0}, {0, -1}, {0, 1},
};
```

然后把四个 if 判断变成一个循环：

```C++
for (int[][] move : moves) {
    int r2 = r + move[0];
    int c2 = c + move[1];
    if (inArea(grid, r2, c2) && grid[r2][c2] == 0) {
        grid[r2][c2] = 2;
        queue.add(new int[]{r2, c2});
    }
}
```

写好了层序遍历的代码，接下来我们看看如何来解决本题中的最短路径问题。

这道题要找的是距离陆地最远的海洋格子。假设网格中只有一个陆地格子，我们可以从这个陆地格子出发做层序遍历，直到所有格子都遍历完。最终遍历了几层，海洋格子的最远距离就是几。

![img](https://mmbiz.qpic.cn/mmbiz_gif/TKAD4axFcibicpic2kCo7q8saB8PYemyCbYkxaREnz930D068wIfEbWPhUuMoTHlxdCFgkUKVfUu84OTfra66mMaQ/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)

从单个陆地格子出发的距离（动图）

那么有多个陆地格子的时候怎么办呢？一种方法是将每个陆地格子都作为起点做一次层序遍历，但是这样的时间开销太大。

**BFS 完全可以以多个格子同时作为起点**。我们可以把所有的陆地格子同时放入初始队列，然后开始层序遍历，这样遍历的效果如下图所示：

![img](https://mmbiz.qpic.cn/mmbiz_gif/TKAD4axFcibicpic2kCo7q8saB8PYemyCbY2ySmWLvjgz8cTJKYHKgQphPo46Bzusic8Zt96Ah2M2FuumDEiaj8d8uQ/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)从多个陆地格子出发的距离

这种遍历方法实际上叫做「**多源 BFS**」。多源 BFS 的定义不是今天讨论的重点，你只需要记住多源 BFS 很方便，只需要把多个源点同时放入初始队列即可。

需要注意的是，虽然上面的图示用 1、2、3、4 表示层序遍历的层数，但是在代码中，我们不需要给每个遍历到的格子标记层数，只需要用一个 `distance` 变量记录当前的遍历的层数（也就是到陆地格子的距离）即可。

最终，我们得到的题解代码为：

```c++
public int maxDistance(int[][] grid) {
    int N = grid.length;

    Queue<int[]> queue = new ArrayDeque<>();
    // 将所有的陆地格子加入队列
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            if (grid[i][j] == 1) {
                queue.add(new int[]{i, j});
            }
        }
    }

    // 如果地图上只有陆地或者海洋，返回 -1
    if (queue.isEmpty() || queue.size() == N * N) {
        return -1;
    }

    int[][] moves = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1},
    };

    int distance = -1; // 记录当前遍历的层数（距离）
    while (!queue.isEmpty()) {
        distance++;
        int n = queue.size();
        for (int i = 0; i < n; i++) { 
            int[] node = queue.poll();
            int r = node[0];
            int c = node[1];
            for (int[] move : moves) {
                int r2 = r + move[0];
                int c2 = c + move[1];
                if (inArea(grid, r2, c2) && grid[r2][c2] == 0) {
                    grid[r2][c2] = 2;
                    queue.add(new int[]{r2, c2});
                }
            }
        }
    }

    return distance;
}

// 判断坐标 (r, c) 是否在网格中
boolean inArea(int[][] grid, int r, int c) {
    return 0 <= r && r < grid.length 
        && 0 <= c && c < grid[0].length;
}
```

## 总结

可以看到，「BFS 遍历」、「层序遍历」、「最短路径」实际上是递进的关系。在 BFS 遍历的基础上区分遍历的每一层，就得到了层序遍历。在层序遍历的基础上记录层数，就得到了最短路径。

BFS 遍历是一类很值得反复体会和练习的题目。一方面，BFS 遍历是一个经典的基础算法，需要重点掌握。另一方面，我们需要能根据题意分析出题目是要求最短路径，知道是要做 BFS 遍历。

本文讲解的只是两道非常典型的例题。LeetCode 中还有许多层序遍历和最短路径的题目

层序遍历的一些变种题目：

- **LeetCode 103. Binary Tree Zigzag Level Order Traversal** 之字形层序遍历
- **LeetCode 199. Binary Tree Right Side View** 找每一层的最右结点
- **LeetCode 515. Find Largest Value in Each Tree Row** 计算每一层的最大值
- **LeetCode 637. Average of Levels in Binary Tree** 计算每一层的平均值

对于最短路径问题，还有两道题目也是求网格结构中的最短路径，和我们讲解的距离岛屿的最远距离非常类似：

- **LeetCode 542. 01 Matrix**
- **LeetCode 994. Rotting Oranges**

还有一道在真正的图结构中求最短路径的问题：

- **LeetCode 310. Minimum Height Trees**

经过了本文的讲解，相信解决这些题目也不是难事。

## 二叉搜索树

https://leetcode-cn.com/problems/search-in-a-binary-search-tree/solution/er-cha-sou-suo-shu-zhong-de-sou-suo-python3di-gui-/



#### [103. 二叉树的锯齿形层次遍历](https://leetcode-cn.com/problems/binary-tree-zigzag-level-order-traversal/)

```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
就是一个简单的bfs加一个标志位看哪一个层次需要倒叙就可以
class Solution:
    def zigzagLevelOrder(self, root: TreeNode) -> List[List[int]]:
        res=[]#存储最终数据
        depth=0 #看什么时候来进行翻转层次数组
        if not root:
            return []
        deque1=[root]
        while deque1:
            n=len(deque1)
            level=[]
            for i in range(n):
                v=deque1.pop(0)
                level.append(v.val)
                if v.left:
                    deque1.append(v.left)
                if v.right:
                    deque1.append(v.right)
            if depth % 2 :
                    res.append(level[::-1])
            else:res.append(level)
            depth+=1
        return res
```

## 返回最右边元素

```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution:
    def rightSideView(self, root: TreeNode) -> List[int]:
        res=[]#存储最终数据
        if not root:
            return []
        deque1=[root]
        while deque1:
            n=len(deque1)
            level=[]
            for i in range(n):
                v=deque1.pop(0)
                level.append(v.val)
                if v.left:
                    deque1.append(v.left)
                if v.right:
                    deque1.append(v.right)
            res.append(level.pop())
        return res
    
    
    
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution:
    def rightSideView(self, root: TreeNode) -> List[int]:
        res=[]#存储最终数据
        if not root:
            return []
        deque1=[root]
        while deque1:
            n=len(deque1)
            level=[]
            for i in range(n):
                v=deque1.pop(0)
                if v.left:
                    deque1.append(v.left)
                if v.right:
                    deque1.append(v.right)
                if i==n-1:
                    res.append(v.val)
        return res
```

```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

DFS 注意只有当第一次depth和len(res)相同时才可以
class Solution:
    def rightSideView(self, root: TreeNode) -> List[int]:
        def dfs(root,step,res):
            if not root:return
            if depth==len(res):
                res.append(root.val)
            dfs(root.right,depth+1,res)
            dfs(root.left,depth+1,res)
        res=[]#存储最终数据
        dfs(root,0,res)
        return res
```

## 每一层最大值

```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
class Solution:
    def largestValues(self, root: TreeNode) -> List[int]:
        res=[]#存储最终数据
        if not root:
            return []
        deque1=[root]
        while deque1:
            n=len(deque1)
            max1=float('-inf')
            level=[]
            for i in range(n):
                v=deque1.pop(0)
                if v.left:
                    deque1.append(v.left)
                if v.right:
                    deque1.append(v.right)
                max1=max(max1,v.val)
            res.append(max1)
        return res
```

### DFS

![image.png](https://pic.leetcode-cn.com/fbe9f181596f62ed0bb3788032c677d9f1686bd7d36df74c8b57565e1492a5b1-image.png)

![image.png](https://pic.leetcode-cn.com/d8586a7f9459e2cbd8a11cd11ca55276052ee2fa63a399a395e71ece544a18ff-image.png)

![image.png](https://pic.leetcode-cn.com/83b7796c427d8c47c87582d56b95f9bf40cb45e99fba0164b3096a952014279c-image.png)



![image.png](https://pic.leetcode-cn.com/2a7afe9f5b1201346e54c8342f24616842742c116da90ccf5d9356fb724fa588-image.png)

```java
    public List<Integer> largestValues(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        helper(root, res, 1);
        return res;
    }

    //level表示的是第几层，集合res中的第一个数据表示的是
    // 第一层的最大值，第二个数据表示的是第二层的最大值……
    private void helper(TreeNode root, List<Integer> res, int level) {
        if (root == null)
            return;
        //如果走到下一层了直接加入到集合中
        if (level == res.size() + 1) {
            res.add(root.val);
        } else {
            //注意：我们的level是从1开始的，也就是说root
            // 是第一层，而集合list的下标是从0开始的，
            // 所以这里level要减1。
            // Math.max(res.get(level - 1), root.val)表示的
            // 是遍历到的第level层的root.val值和集合中的第level
            // 个元素的值哪个大，就要哪个。
            res.set(level - 1, Math.max(res.get(level - 1), root.val));
        }
        //下面两行是DFS的核心代码
        helper(root.left, res, level + 1);
        helper(root.right, res, level + 1);
    }

```

