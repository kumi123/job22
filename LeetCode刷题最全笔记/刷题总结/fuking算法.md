---

title: fucking算法模板学习
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2020-02-09 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN/19.jpg
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

# 二叉树算法的设计的**总路线：明确一个节点要做的事情，然后剩下的事抛给框架。**



```c++
void traverse(TreeNode root) {
    // root 需要做什么？在这做。
    // 其他的不用 root 操心，抛给框架
    traverse(root.left);
    traverse(root.right);
}
```



举两个简单的例子体会一下这个思路，热热身。



**1. 如何把二叉树所有的节点中的值加一？**



```
void plusOne(TreeNode root) {
    if (root == null) return;
    root.val += 1;

    plusOne(root.left);
    plusOne(root.right);
}
```



**2. 如何判断两棵二叉树是否完全相同？**



![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4kYKfbiaZML90k61V2sr7UjHtefSwUbU5SUIpEq1FZWOIvyTqrSAlSuYEtNKx0rkPKHNLrOyAib6d9Q/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



借助框架，上面这两个例子不难理解吧？如果可以理解，那么所有二叉树算法你都能解决。



下面实现 BST 的基础操作：判断 BST 的合法性、增、删、查。其中「删」和「判断合法性」略微复杂。



**二叉搜索树**（Binary Search Tree，简称 BST）是一种很常用的的二叉树。它的定义是：一个二叉树中，任意节点的值要大于左子树**所有**节点的值，且要小于右边子树的**所有**节点的值。



如下就是一个符合定义的 BST：



![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4kYKfbiaZML90k61V2sr7UjHcUibnS7mshtKjicN1xlPWHvXHRrjxbsnfOLU62JEru7Svncvf3Ycs06w/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



**零、判断 BST 的合法性**



这里是有坑的哦，我们按照刚才的思路，每个节点自己要做的事不就是比较自己和左右孩子吗？看起来应该这样写代码：



![img](https://mmbiz.qpic.cn/mmbiz_jpg/map09icNxZ4kYKfbiaZML90k61V2sr7UjHyfpLIF2H91qsAfDSwgmPOVnqMxHyCP77M85bUOSX9GeKtwwXyTDEIQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



但是这个算法出现了错误，BST 的每个节点应该要小于右边子树的所有节点，下面这个二叉树显然不是 BST，但是我们的算法会把它判定为 BST。



![img](https://mmbiz.qpic.cn/mmbiz_jpg/map09icNxZ4kYKfbiaZML90k61V2sr7UjHm87CnWrvjPfiauU6Piaiafno0qQNJzmiceZxIxhxibGC4PtuKnN4cMIibpicA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



出现错误，不要慌张，框架没有错，一定是某个细节问题没注意到。我们重新看一下 BST 的定义，root 需要做的，不仅仅是和左右子节点比较，而是要和左子树和右子树的**所有**节点比较。怎么办，鞭长莫及啊！



这种情况，我们可以使用辅助函数，增加函数参数列表，**在参数中携带额外信息**，请看正确的代码：



![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4kYKfbiaZML90k61V2sr7UjHCkK2GhTmkOIqbzrOK4gF4fUHQHvSgn4iazA1zlaScfrDSZKySQF3NiaA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



这样，root 可以对整棵左子树和右子树进行约束，根据定义，root 才真正完成了它该做的事，所以这个算法是正确的。



![img](https://mmbiz.qpic.cn/mmbiz_jpg/map09icNxZ4kYKfbiaZML90k61V2sr7UjHWXibGb2m08XMl2ArY5TOlf7TZicY1ms5Lfo7VrkicOHsqL21uCmLlibQxw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



**一、在 BST 中查找一个数是否存在**



根据我们的总路线，可以这样写代码：



![img](data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==)



这样写完全正确，充分证明了你的框架性思维已经养成。现在你可以考虑一点细节问题了：如何充分利用信息，把 BST 这个“左小右大”的特性用上？



很简单，其实不需要递归地搜索两边，类似二分查找思想，可以根据 target 和 root.val 的大小比较，就能排除一边。我们把上面的思路稍稍改动：



![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4kYKfbiaZML90k61V2sr7UjHAeBLtjsC0JmkmkXRZbC46rVXjNjDY0N1uCHjPia2JNic254T77taDOibQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



于是，我们对原始框架进行改造，抽象出一套**针对 BST 的遍历框架**：



```
void BST(TreeNode root, int target) {
    if (root.val == target)
        // 找到目标，做点什么
    if (root.val < target) 
        BST(root.right, target);
    if (root.val > target)
        BST(root.left, target);
}
```



**二、在 BST 中插入一个数**



对数据结构的操作无非遍历 + 访问，遍历就是「找」，访问就是「改」。具体到这个问题，插入一个数，就是先找到插入位置，然后进行插入操作。



上一个问题，我们总结了 BST 中的遍历框架，就是解决了「找」的问题。直接套框架，加上「改」的操作即可。



一旦涉及「改」，函数就要返回 TreeNode 类型，并且对递归调用的返回值进行接收。



![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4kYKfbiaZML90k61V2sr7UjHP9Igic0YaMqNEyeic10ibnsZFSVDE5YnUEs1iaabZPe7HpBZPjcV0OcIoQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



**三、在 BST 中删除一个数**



这个问题稍微复杂，不过你有框架指导，难不住你。跟插入操作类似，先「找」再「改」，先把框架写出来再说：



![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4kYKfbiaZML90k61V2sr7UjHTxsUiaPE2R2vAibMvGny6BKJxe2ZWOv79oHqKZR8IHtR1nlibLTqrIiaEw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

找到目标节点了，比方说是节点 A，如何删除这个节点，这是难点。因为删除节点的同时不能破坏 BST 的性质。有三种情况，用图片来说明。



**情况 1**：A 恰好是末端节点，两个子节点都为空，那么它可以当场去世。



![img](data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==)

图片来自 www.leetcode.com



```
if (root.left == null && root.right == null)
    return null;
```



**情况 2**：A 只有一个非空子节点，那么它要让这个孩子接替自己的位置。



![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4kYKfbiaZML90k61V2sr7UjHoGmhsmUFRDnSrgJ0ppf0aAricmJLkqSnB1NnNxdc99CIRWreiaBUIaFA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)图片来自 www.leetcode.com



```
// 排除了情况 1 之后
if (root.left == null) return root.right;
if (root.right == null) return root.left;
```



**情况 3**：A 有两个子节点，麻烦了，为了不破坏 BST 的性质，A 必须找到左子树中最大的那个节点，或者右子树中最小的那个节点来接替自己。两种策略是类似的，我们以第二种方式讲解。



![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4kYKfbiaZML90k61V2sr7UjHjTwnAF1qAzRTeTUg4czpvxD8OgQvgiaIz4fw3PwkrzBH7icO8btsgBVw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

图片来自 www.leetcode.com



```c++
if (root.left != null && root.right != null) {
    // 找到右子树的最小节点
    TreeNode minNode = getMin(root.right);
    // 把 root 改成 minNode
    root.val = minNode.val;
    // 转而去删除 minNode
    root.right = deleteNode(root.right, minNode.val);
}
```



三种情况分析完毕，简化一下，填入框架：



![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4kYKfbiaZML90k61V2sr7UjHWah90VymC7DlfKEjPiahVzfYVvezpOdZnyHM4yB8wL4bsgz0UMbsyVA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



删除操作就完成了。注意一下，这个删除操作并不完美，因为我们最好不要像 root.val = minNode.val 这样通过修改节点内部的数据来改变节点，而是通过一系列略微复杂的链表操作交换 root 和 minNode 两个节点。



因为具体应用中，val 域可能会很大，修改起来很耗时，而链表操作无非改一改指针，而不会去碰内部数据。



但这里忽略这个细节，旨在突出 BST 删除操作的思路，以及借助框架逐层细化问题的思维方式。



**四、最后总结**



通过这篇文章，你学会了如下几个技巧：



\1. 二叉树算法设计的总路线：把当前节点要做的事做好，其他的交给递归框架，不用当前节点操心。



\2. 如果当前节点会对下面的子节点有整体性影响，可以通过辅助函数加长参数列表，借助函数参数传递信息。这就是递归函数传递信息的常用方式。



\3. 在二叉树框架之上，扩展出一套 BST 遍历框架：



```c++
void BST(TreeNode root, int target) {
    if (root.val == target)
        // 找到目标，做点什么
    if (root.val < target) 
        BST(root.right, target);
    if (root.val > target)
        BST(root.left, target);
}
```



\4. 掌握了 BST 的基本操作，包括判断 BST 的合法性以及 BST 中的增、删、查操作。

# 排序数组和链表原地修改重复数据

我们知道对于数组来说，在尾部插入、删除元素是比较高效的，时间复杂度是 O(1)，但是如果在中间或者开头插入、删除元素，就会涉及数据的搬移，时间复杂度为 O(N)，效率较低。

所以对于一般处理数组的算法问题，我们要尽可能只对数组尾部的元素进行操作，以避免额外的时间复杂度。

这篇文章讲讲如何对一个有序数组去重，先看下题目：

![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4kjKDY327Vjgh9xjMKhdRwkpMc8yECrN67efS23St1iabEyH68FCsos3RyrgiaNwfMia6OqqzBxWDNBg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

显然，由于数组已经排序，所以重复的元素一定连在一起，找出它们并不难，但如果毎找到一个重复元素就立即删除它，就是在数组中间进行删除操作，整个时间复杂度是会达到 O(N^2)。而且题目要求我们原地修改，也就是说不能用辅助数组，空间复杂度得是 O(1)。

其实，**对于数组相关的算法问题，有一个通用的技巧：要尽量避免在中间删除元素，那我就****先****想办法把这个元素换到最后去**。

这样的话，最终待删除的元素都拖在数组尾部，一个一个 pop 掉就行了，每次操作的时间复杂度也就降低到 O(1) 了。

按照这个思路呢，又可以衍生出解决类似需求的通用方式：双指针技巧。具体一点说，应该是快慢指针。

我们让慢指针`slow`走左后面，快指针`fast`走在前面探路，找到一个不重复的元素就告诉`slow`并让`slow`前进一步。

这样当`fast`指针遍历完整个数组`nums`后，**`nums[0..slow]`就是不重复元素，之后的所有元素都是重复元素**。

![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4kjKDY327Vjgh9xjMKhdRwkXNWK1M1RuP04RjpdVEqe3UJ4koJ7yiaibCdibposGicY0ZxDMKhN0R6cCg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

看下算法执行的过程：

![img](https://mmbiz.qpic.cn/mmbiz_gif/map09icNxZ4kjKDY327Vjgh9xjMKhdRwkNrHlatFV4e3gVBNhQz8w4AdWzJQjZbiahEGcq8Bua5vam4ab6TY5OnA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)

再简单扩展一下，如果给你一个有序链表，如何去重呢？其实和数组是一模一样的，唯一的区别是把数组赋值操作变成操作指针而已：

![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4kjKDY327Vjgh9xjMKhdRwkU4y2mY8jWaOGJXgm2qvHv3IWoQ8XVcdhqtiaiciajO6Y4MDr84yll7E2w/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



对于链表去重，算法执行的过程是这样的：

![img](https://mmbiz.qpic.cn/mmbiz_gif/map09icNxZ4kjKDY327Vjgh9xjMKhdRwkCU5OeeFaodzyERZwzVwAAU8DhJpcZDK3uddUsRyBibBG5ics8Wm0Vsyw/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)



# 判断子序列的问题

二分查找本身不难理解，难在巧妙地运用二分查找技巧。对于一个问题，你可能都很难想到它跟二分查找有关，比如前文 [最长递增子序列](http://mp.weixin.qq.com/s?__biz=MzU0MDg5OTYyOQ==&mid=2247484232&idx=1&sn=21234a9e4db908f438e1cb2e8c7ffff4&chksm=fb33630acc44ea1c91027bff20e9902e20e4269d54f3c178dc1e07f344d48d7ff1a4ca48ba39&scene=21#wechat_redirect) 就借助一个纸牌游戏衍生出二分查找解法。

今天再讲一道巧用二分查找的算法问题：如何判定字符串`s`是否是字符串`t`的子序列（可以假定`s`长度比较小，且`t`的长度非常大）。举两个例子：

s = "abc", t = "**a**h**b**gd**c**", return true.

s = "axc", t = "ahbgdc", return false.

题目很容易理解，而且看起来很简单，但很难想到这个问题跟二分查找有关吧？

### 一、问题分析

首先，一个很简单的解法是这样的：

```
bool isSubsequence(string s, string t) {
    int i = 0, j = 0;
    while (i < s.size() && j < t.size()) {
        if (s[i] == t[j]) i++;
        j++;
    }
    return i == s.size();
}
```

其思路也非常简单，利用双指针`i, j`分别指向`s, t`，一边前进一边匹配子序列：

![img](https://mmbiz.qpic.cn/mmbiz_gif/map09icNxZ4mnbVC00EzwNpKWVLic67v8fp4AviccRT7S8XlynThhhyUxhB2X63YwDSCyq2ZWcGo6cQS0HhxKiaziaQ/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)

读者也许会问，这不就是最优解法了吗，时间复杂度只需 O(N)，N 为`t`的长度。

是的，如果仅仅是这个问题，这个解法就够好了，**不过这个问题还有 follow up**：

如果给你一系列字符串`s1,s2,...`和字符串`t`，你需要判定每个串`s`是否是`t`的子序列（可以假定`s`相对较短，`t`很长）。

```
boolean[] isSubsequence(String[] sn, String t);
```

你也许会问，这不是很简单吗，还是刚才的逻辑，加个 for 循环不就行了？

可以，但是此解法处理每个`s`时间复杂度仍然是 O(N)，而如果巧妙运用二分查找，可以将时间复杂度降低，大约是 O(MlogN)，M 为 s 的长度。由于 N 相对 M 大很多，所以后者效率会更高。

### 二、二分思路

二分思路主要是对`t`进行预处理，用一个字典`index`将每个字符出现的索引位置按顺序存储下来（对于 ASCII 字符，可以用大小为 256 的数组充当字典）：

![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4kgs1l9MrcAfJI2cWHicmeQ23qGGL5lhSWujKicYK9hrS4KpkRichZmkhGgaibN1wZdibcc16tlXIk4OCQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

![img](https://mmbiz.qpic.cn/mmbiz_jpg/map09icNxZ4mnbVC00EzwNpKWVLic67v8fDxiaDiaM0k6eykBn75gbkQibF4Q31etH3Pqr8Ed0UN7bvvyXmWKXfewnQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

比如对于这个情况，匹配了 "ab"，应该匹配 "c" 了：

![img](https://mmbiz.qpic.cn/mmbiz_jpg/map09icNxZ4mnbVC00EzwNpKWVLic67v8fDib0nUdqLOlGhic4U5bCLZZDpd04c1Y3PT2Y2bfRf7fbBauiabyI4Vg0g/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

按照之前的解法，我们需要`j`线性前进扫描字符 "c"。但现在借助`index`中记录的信息，**可以二分搜索`index[c]`中比 j 大的那个索引**，在上图的例子中，就是在`[0,2,6]`中搜索比 4 大的那个索引：

![img](https://mmbiz.qpic.cn/mmbiz_jpg/map09icNxZ4mnbVC00EzwNpKWVLic67v8feHfGzevvxqKzdVxZUcia0vSOZZDrGhIlDaC7LP1WdhjxcdribFGYTyvQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

这样就可以快速得到下一个 "c" 的索引 6。现在的问题就是，如何用二分查找计算那个恰好比 4 大的索引呢？答案是，寻找左侧边界的二分搜索就可以做到。

### 三、再谈二分查找

在前文 [二分查找算法详解](http://mp.weixin.qq.com/s?__biz=MzU0MDg5OTYyOQ==&mid=2247484090&idx=1&sn=5635cf1c4fd8a8570b63c7ae9b4304c2&chksm=fb3362f8cc44ebee0a19a4cfba7f2e13923e05f47e15f2e99a1f42b01aeee83b946aceac3d4c&scene=21#wechat_redirect) 中，详解了如何正确写出三种二分查找算法的细节。二分查找返回目标值`val`的索引，对于搜索**左侧边界**的二分查找，有一个特殊性质：

**当`val`不存在时，得到的索引恰好是比`val`大的最小元素索引**。

什么意思呢，就是说如果在数组`[0,1,3,4]`中搜索元素 2，算法会返回索引 2，也就是元素 3 的位置，元素 3 就是数组中大于 2 的最小元素。所以我们可以利用二分搜索避免线性扫描。

![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4kgs1l9MrcAfJI2cWHicmeQ2RC2TvWKdHDXNA2DLdTC3Isibt1XkSJmN5fviaIywQ3IFdbWTzdlbic6Iw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

以上就是搜索左侧边界的二分查找，等会儿会用到，其中的细节可以参见 [二分查找算法详解](http://mp.weixin.qq.com/s?__biz=MzU0MDg5OTYyOQ==&mid=2247484090&idx=1&sn=5635cf1c4fd8a8570b63c7ae9b4304c2&chksm=fb3362f8cc44ebee0a19a4cfba7f2e13923e05f47e15f2e99a1f42b01aeee83b946aceac3d4c&scene=21#wechat_redirect)，这里不再赘述。

### 四、代码实现

这里以处理单个字符串`s`为例，对于多个字符串`s`，把预处理部分单独抽出来即可。

![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4mnbVC00EzwNpKWVLic67v8fmpribXsLDBIR06WYFvJFsN9mNyNRyxGMJQYgwRkicxg3Vaiba7LeLIp9Q/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

算法执行的过程是这样的：

![img](https://mmbiz.qpic.cn/mmbiz_gif/map09icNxZ4kgs1l9MrcAfJI2cWHicmeQ2vYLPqePH28go8Hoc69qBNHk8eYucunvgAciaKRiaGpgGKJOHupJevPpg/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)

可见借助二分查找，算法的效率是可以大幅提升的：预处理时需要 O(N) 时间，每次匹配子序列的时间是 O(MlogN)，比之前每次匹配都要 O(N) 的时间要高效得多。

当然，如果只需要判断一个 s 是否是 t 的子序列，是不需要二分查找的，一开始的 O(N) 解法就是最好的，因为虽然二分查找解法处理每个 s 只需要 O(MlogN)，但是还需要 O(N) 时间构造 index 字典预处理，所以处理单个 s 时没有必要。

以上就是二分查找技巧判定子序列的全部内容，希望你能有所收获。



# 接雨水

接雨水这道题目挺有意思，在面试题中出现频率还挺高的，本文就来步步优化，讲解一下这道题。

这道题目实际上出自 LeetCode：

![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4lmgyqnVCwsKWoNqycr9VIiccD3uyAzxKSyERIrKcz5cABcnX4knzvvxs4Vw2yMlUHvC5R5Amk1IyA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

题目来自 leetcode-cn.com

就是用一个数组表示一个条形图，问你这个条形图最多能接多少水。

```
int trap(int[] height);
```

下面就来由浅入深介绍暴力解法 -> 备忘录解法 -> 双指针解法，在 O(N) 时间 O(1) 空间内解决这个问题。

### 一、核心思路

我第一次看到这个问题，无计可施，完全没有思路，相信很多朋友跟我一样。所以对于这种问题，我们不要想整体，而应该去想局部；就像之前的文章处理字符串问题，不要考虑如何处理整个字符串，而是去思考应该如何处理每一个字符。

这么一想，可以发现这道题的思路其实很简单。具体来说，仅仅对于位置 i，能装下多少水呢？

![img](https://mmbiz.qpic.cn/mmbiz_jpg/map09icNxZ4lmgyqnVCwsKWoNqycr9VIicicQU7S1NtnoZ76WH0DqxaibTvxt0qjicWFXicQyOVxh4FbJibXeicEQKjZVg/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

能装 2 格水。为什么恰好是两格水呢？因为 height[i] 的高度为 0，而这里最多能盛 2 格水，2-0=2。

为什么位置 i 最多能盛 2 格水呢？因为，位置 i 能达到的水柱高度和其左边的最高柱子、右边的最高柱子有关，我们分别称这两个柱子高度为`l_max`和`r_max`；**位置 i 最大的水柱高度就是`min(l_max, r_max)`。**

更进一步，对于位置 i，能够装的水为：

```
water[i] = min(
               # 左边最高的柱子
               max(height[0..i]),  
               # 右边最高的柱子
               max(height[i..end]) 
            ) - height[i]
```

![img](https://mmbiz.qpic.cn/mmbiz_jpg/map09icNxZ4lmgyqnVCwsKWoNqycr9VIicGJzNpto4yia0wUKF5pwdNjFR38lIDEZ0gJm5eHLPul7MxXPmlkfmjjA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

![img](https://mmbiz.qpic.cn/mmbiz_jpg/map09icNxZ4lmgyqnVCwsKWoNqycr9VIicbMFOu7hcgPyZWicbSZ2zVM58boo2HupvFY2zeBA8V7eyd6NjKLFGlCw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

这就是本问题的核心思路，我们可以简单写一个暴力算法：

```python
class Solution:
    def trap(self, height: List[int]) -> int:
        ans = 0
        for i in range(len(height)):
            max_left, max_right = 0,0
            # 寻找 max_left
            for j in range(0,i):
                max_left = max(max_left,height[j])
            # 寻找 max_right
            for j in range(i,len(height)):
                max_right = max(max_right,height[j])
            if min(max_left,max_right) > height[i]:
                ans += min(max_left,max_right) - height[i]
        
        return ans

```

![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4lmgyqnVCwsKWoNqycr9VIicRXaqWtBd8J3hnUya4hyuPkYnGlfCarAblIwDePBNlUHLmtOXnUk25Q/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

有之前的思路，这个解法应该是很直接粗暴的，时间复杂度 O(N^2)，空间复杂度 O(1)。但是很明显这种计算`r_max`和`l_max`的方式非常笨拙，一般的优化方法就是备忘录。

### 二、备忘录优化

之前的暴力解法，不是在每个位置 i 都要计算`r_max`和`l_max`吗？我们直接把结果都缓存下来，别傻不拉几的每次都遍历，这时间复杂度不就降下来了嘛。

我们开两个数组`r_max`和`l_max`充当备忘录，**`l_max[i]`表示位置 i 左边最高的柱子高度，`r_max[i]`表示位置 i 右边最高的柱子高度。**预先把这两个数组计算好，避免重复计算：

```python
class Solution:
    def trap(self, height: List[int]) -> int:
        # 边界条件
        if not height: return 0
        n = len(height)
        maxleft = [0] * n
        maxright = [0] * n
        ans = 0
        # 初始化
        maxleft[0] = height[0]
        maxright[n-1] = height[n-1]
        # 设置备忘录，分别存储左边和右边最高的柱子高度
        for i in range(1,n):
            maxleft[i] = max(height[i],maxleft[i-1])
        for j in range(n-2,-1,-1):
            maxright[j] = max(height[j],maxright[j+1])
        # 一趟遍历，比较每个位置可以存储多少水
        for i in range(n):
            if min(maxleft[i],maxright[i]) > height[i]:
                ans += min(maxleft[i],maxright[i]) - height[i]
        return ans

```

![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4lmgyqnVCwsKWoNqycr9VIicTqb4KGs55MS2ZTMYMyibLebwegBbCFsVAITm0CxtfxRibsofUgoeiceyQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

这个优化其实和暴力解法差不多，就是避免了重复计算，把时间复杂度降低为 O(N)，已经是最优了，但是空间复杂度是 O(N)。下面来看一个精妙一些的解法，能够把空间复杂度降低到 O(1)。

### 三、双指针解法

这种解法的思路是完全相同的，但在实现手法上非常巧妙，我们这次也不要用备忘录提前计算了，而是用双指针**边走边算**，节省下空间复杂度。

首先，看一部分代码：

```
int trap(vector<int>& height) {
    int n = height.size();
    int left = 0, right = n - 1;

    int l_max = height[0];
    int r_max = height[n - 1];

    while (left <= right) {
        l_max = max(l_max, height[left]);
        r_max = max(r_max, height[right]);
        left++; right--;
    }
}
```

对于这部分代码，请问`l_max`和`r_max`分别表示什么意义呢？

很容易理解，**`l_max`是`height[0..left]`中最高柱子的高度，`r_max`是`height[right..end]`的最高柱子的高度**。

明白了这一点，直接看解法：

```python
class Solution:
    def trap(self, height: List[int]) -> int:
        # 边界条件
        if not height: return 0
        n = len(height)

        left,right = 0, n - 1  # 分别位于输入数组的两端
        maxleft,maxright = height[0],height[n - 1]
        ans = 0

        while left <= right:
            maxleft = max(height[left],maxleft)
            maxright = max(height[right],maxright)
            if maxleft < maxright:
                ans += maxleft - height[left]
                left += 1
            else:
                ans += maxright - height[right]
                right -= 1

        return ans


```

![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4lmgyqnVCwsKWoNqycr9VIicxwvGebERJ9Z1icBDjXJo6wWx7MQ01uzcvsoH1yib3bo1GLlKqI5H02bQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

你看，其中的核心思想和之前一模一样，换汤不换药。但是细心的读者可能会发现次解法还是有点细节差异：

之前的备忘录解法，`l_max[i]`和`r_max[i]`代表的是`height[0..i]`和`height[i..end]`的最高柱子高度。

```
ans += min(l_max[i], r_max[i]) - height[i];
```

![img](https://mmbiz.qpic.cn/mmbiz_jpg/map09icNxZ4lmgyqnVCwsKWoNqycr9VIicKjU7YKwOd3b8qwDpGOgLRVC3ZO6qsLtWuMOmZPES28wm1MH0jdY4cg/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

但是双指针解法中，`l_max`和`r_max`代表的是`height[0..left]`和`height[right..end]`的最高柱子高度。比如这段代码：

```
if (l_max < r_max) {
    ans += l_max - height[left];
    left++; 
} 
```

![img](https://mmbiz.qpic.cn/mmbiz_jpg/map09icNxZ4lmgyqnVCwsKWoNqycr9VIicREzTjbnC3uibluLffjwicHgib0xPqh5jIQ37DpxQqaibpcMuNH5AKjP2Bw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

此时的`l_max`是`left`指针左边的最高柱子，但是`r_max`并不一定是`left`指针右边最高的柱子，这真的可以得到正确答案吗？

其实这个问题要这么思考，我们只在乎`min(l_max, r_max)`。对于上图的情况，我们已经知道`l_max < r_max`了，至于这个`r_max`是不是右边最大的，不重要，**重要的是`height[i]`能够装的水只和`l_max`有关。**

![img](https://mmbiz.qpic.cn/mmbiz_jpg/map09icNxZ4lmgyqnVCwsKWoNqycr9VIicpqPibKpfoEYzwEx0padysIW7RgQVvaouscVU4Y2LsUqGAa6tG9r4icyA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



对于 l_max > r_max 的情况也是类似的。

## **4、单调栈 时间O(N) 空间O(N)**

单调栈是本文想要重点说明的一个方法～

因为本题是一道典型的单调栈的应用题。

**简单来说就是当前柱子如果小于等于栈顶元素，说明形不成凹槽，则将当前柱子入栈；反之若当前柱子大于栈顶元素，说明形成了凹槽，于是将栈中小于当前柱子的元素pop出来，将凹槽的大小累加到结果中。**

关于凹槽的理解，我们可以先看一下gif图：

![img](https://picb.zhimg.com/v2-0cde604155d1786395d77f33e580914a_b.webp)

通过上图可以发现，遍历到某些柱子的时候，会由于和之前的某个柱子形成凹槽，接住雨水。



这道题目可以用单调栈来做。**单调栈就是比普通的栈多一个性质，即维护栈内元素单调。**

比如当前某个单调递减的栈的元素从栈底到栈顶分别是：[10, 9, 8, 3, 2]，如果要入栈元素5，需要把栈顶元素pop出去，直到满足单调递减为止，即先变成[10, 9, 8]，再入栈5，就是[10, 9, 8, 5]。



## **演示：**

我们为这道题演示一下`[4, 3, 1, 0, 1, 2, 4]`是怎么接雨水的，下图是最终的接雨水效果，蓝色部分是雨水。

![img](https://pic3.zhimg.com/v2-5ee6617c728e418c5db9228a3ef64502_r.jpg)

接下来我们开始演示，下面的图示中，最上方是每个柱子的高度。左侧是单调栈的元素。图中有红色边框的柱子是存在于单调栈里的元素。



**Step 1:** 遍历到下图中箭头所指向的位置时，栈内元素是[4, 3, 1, 0]。由于当前柱体的高度为1大于栈顶元素0，那就可以接住雨水。接住雨水的量的高度是栈顶元素和左右两边形成的高度差的min。宽度是1。

![img](https://pic2.zhimg.com/v2-3f7f3f0362d2262050c319efd066fcdf_r.jpg)





**Step 2:** 遍历到下图中箭头所指向的位置时，栈内元素是[4, 3, 1, 1]。由于当前的柱体的高度为2大于栈顶元素1，那就可以接住雨水。由于栈顶元素有相等的情况，所以可以把1全都pop出去，变成[4, 3]。此时形成的新凹槽的高度是此时的栈顶元素3和当前高度为2的柱体的高度的min再减去先前的栈顶元素1，新凹槽的宽度是此时栈顶元素的位置和当前高度为2的柱体的位置的距离。可以算出来此次接住的雨水是1 * 3。

![img](https://pic3.zhimg.com/v2-db71eb2475e55bc4ebbfc5f1ba7571ef_r.jpg)





**Step 3:** 遍历到下图中箭头所指向的位置时，栈内元素是 [4, 3, 2]。由于当前的柱体的高度为4大于栈顶元素2，把 2 pop出来，栈顶元素3所在位置和当前高为4的柱体可以接住雨水，雨水量是 1 * 4。

![img](https://pic2.zhimg.com/v2-5a80d376a13f277edf7d9b951597204d_r.jpg)



**Step 4:** 由于上步的栈顶元素3仍然小于当前的柱体高度4，因此pop出3。新栈顶元素4所在位置和当前高为4的柱体可以接住雨水，雨水量是 1 * 5。

![img](https://pic4.zhimg.com/v2-f2b7d15705441a64cf4bea6e48e11925_r.jpg)

这样每个部分的雨水量都可以算出来，加在一起就可以了。由于每个柱体最多入栈出栈一次，所以时间复杂度 O(N)。



代码就很简单了，具体见注释～

```java
class Solution {
    public int trap(int[] height) {
        Stack<Integer> stack = new Stack<>();
        int res = 0;
        // 遍历每个柱体
        for (int i = 0; i < height.length; i++) {
           while (!stack.isEmpty() && height[stack.peek()] < height[i]) {
                int bottomIdx = stack.pop();
                // 如果栈顶元素一直相等，那么全都pop出去，只留第一个。
                while (!stack.isEmpty() && height[stack.peek()] == height[bottomIdx]) {
                    stack.pop();
                }
                if (!stack.isEmpty()) {
                    // stack.peek()是此次接住的雨水的左边界的位置，右边界是当前的柱体，即i。
                    // Math.min(height[stack.peek()], height[i]) 是左右柱子高度的min，减去height[bottomIdx]就是雨水的高度。
                    // i - stack.peek() - 1 是雨水的宽度。
                    res += (Math.min(height[stack.peek()], height[i]) - height[bottomIdx]) * (i - stack.peek() - 1);
                }
            }
            stack.push(i);
        }
        return res;
    }
}
```

最后，为了加强对单调栈的理解，还可以去做一下：84. 柱状图中最大的矩形



# 单调栈

栈（stack）是很简单的一种数据结构，先进后出的逻辑顺序，符合某些问题的特点，比如说函数调用栈。



单调栈实际上就是栈，只是利用了一些巧妙的逻辑，使得每次新元素入栈后，栈内的元素都保持有序（单调递增或单调递减）。



听起来有点像堆（heap）？不是的，单调栈用途不太广泛，只处理一种典型的问题，叫做 Next Greater Element。本文用讲解单调队列的算法模版解决这类问题，并且探讨处理「循环数组」的策略。



首先，讲解 Next Greater Number 的原始问题：给你一个数组，返回一个等长的数组，对应索引存储着下一个更大元素，如果没有更大的元素，就存 -1。不好用语言解释清楚，直接上一个例子：



给你一个数组 [2,1,2,4,3]，你返回数组 [4,2,4,-1,-1]。



解释：第一个 2 后面比 2 大的数是 4; 1 后面比 1 大的数是 2；第二个 2 后面比 2 大的数是 4; 4 后面没有比 4 大的数，填 -1；3 后面没有比 3 大的数，填 -1。



这道题的暴力解法很好想到，就是对每个元素后面都进行扫描，找到第一个更大的元素就行了。但是暴力解法的时间复杂度是 O(n^2)。



这个问题可以这样抽象思考：把数组的元素想象成并列站立的人，元素大小想象成人的身高。这些人面对你站成一列，如何求元素「2」的 Next Greater Number 呢？很简单，如果能够看到元素「2」，那么他后面可见的第一个人就是「2」的 Next Greater Number，因为比「2」小的元素身高不够，都被「2」挡住了，第一个露出来的就是答案。



![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4m0R7ibYasslicsCB3k0kk0BOpGAbwAh1xpoN1LOKvWjjZx3KTxH3TQ08IFdLdSlJlYuLGtJrLBt9Lg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



这个情景很好理解吧？带着这个抽象的情景，先来看下代码。



```c++
vector<int> nextGreaterElement(vector<int>& nums) {
    vector<int> ans(nums.size()); // 存放答案的数组
    stack<int> s;
    for (int i = nums.size() - 1; i >= 0; i--) { // 倒着往栈里放 ，从后边开始入栈，因为栈是存储着比当前元素大的数据，如果不满足条件那么就出栈
        while (!s.empty() && s.top() <= nums[i]) { // 判定个子高矮 如果不比当前的数据大的话那么就弹出
            s.pop(); // 矮个起开，反正也被挡着了。。。
        }
        ans[i] = s.empty() ? -1 : s.top(); // 这个元素身后的第一个高个就是当前的栈顶元素
        s.push(nums[i]); // 进队，接受之后的身高判定吧！元素进栈进行判断
    }
    return ans;
}
```



这就是单调队列解决问题的模板。for 循环要从后往前扫描元素，因为我们借助的是栈的结构，倒着入栈，其实是正着出栈。while 循环是把两个“高个”元素之间的元素排除，因为他们的存在没有意义，前面挡着个“更高”的元素，所以他们不可能被作为后续进来的元素的 Next Great Number 了。



这个算法的时间复杂度不是那么直观，如果你看到 for 循环嵌套 while 循环，可能认为这个算法的复杂度也是 O(n^2)，但是实际上这个算法的复杂度只有 O(n)。



分析它的时间复杂度，要从整体来看：总共有 n 个元素，每个元素都被 push 入栈了一次，而最多会被 pop 一次，没有任何冗余操作。所以总的计算规模是和元素规模 n 成正比的，也就是 O(n) 的复杂度。



现在，你已经掌握了单调栈的使用技巧，来一个简单的变形来加深一下理解。



给你一个数组 T = [73, 74, 75, 71, 69, 72, 76, 73]，这个数组存放的是近几天的天气气温（这气温是铁板烧？不是的，这里用的华氏度）。你返回一个数组，计算：对于每一天，你还要至少等多少天才能等到一个更暖和的气温；如果等不到那一天，填 0 。



举例：给你 T = [73, 74, 75, 71, 69, 72, 76, 73]，你返回 [1, 1, 4, 2, 1, 1, 0, 0]。



解释：第一天 73 华氏度，第二天 74 华氏度，比 73 大，所以对于第一天，只要等一天就能等到一个更暖和的气温。后面的同理。



你已经对 Next Greater Number 类型问题有些敏感了，这个问题本质上也是找 Next Greater Number，只不过现在不是问你 Next Greater Number 是多少，而是问你当前距离 Next Greater Number 的距离而已。



相同类型的问题，相同的思路，直接调用单调栈的算法模板，稍作改动就可以啦，直接上代码把。



```c++
vector<int> dailyTemperatures(vector<int>& T) {
    vector<int> ans(T.size());
    stack<int> s; // 这里放元素索引，而不是元素，这时候也是一样，栈按顺序存储着当前元素后边比当前元素大的数据的索引
    for (int i = T.size() - 1; i >= 0; i--) {
        while (!s.empty() && T[s.top()] <= T[i]) { //不满足就弹出栈
            s.pop();
        }
        ans[i] = s.empty() ? 0 : (s.top() - i); // 得到索引间距
        s.push(i); // 加入索引，而不是元素
    }
    return ans;
}
```



单调栈讲解完毕。下面开始另一个重点：**如何处理「循环数组」**。



同样是 Next Greater Number，现在假设给你的数组是个环形的，如何处理？



给你一个数组 [2,1,2,4,3]，你返回数组 [4,2,4,-1,4]。拥有了环形属性，最后一个元素 3 绕了一圈后找到了比自己大的元素 4 。



![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4m0R7ibYasslicsCB3k0kk0BOcjoO1w8DM7Reib59JniaXMSf4TB7KW98Dkq6biawEoiaynMV3LnqY5Iz7Q/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



首先，计算机的内存都是线性的，没有真正意义上的环形数组，但是我们可以模拟出环形数组的效果，一般是通过 % 运算符求模（余数），获得环形特效：



```c++
int[] arr = {1,2,3,4,5};
int n = arr.length, index = 0;
while (true) {
    print(arr[index % n]);
    index++;
}
```



回到 Next Greater Number 的问题，增加了环形属性后，问题的难点在于：这个 Next 的意义不仅仅是当前元素的右边了，有可能出现在当前元素的左边（如上例）。



明确问题，问题就已经解决了一半了。我们可以考虑这样的思路：将原始数组“翻倍”，就是在后面再接一个原始数组，这样的话，按照之前“比身高”的流程，每个元素不仅可以比较自己右边的元素，而且也可以和左边的元素比较了。



![img](https://mmbiz.qpic.cn/mmbiz_png/map09icNxZ4m0R7ibYasslicsCB3k0kk0BOpnkMMbH694fmtpUNgypvZzztIgg9VKmmAQY9DtC9mVO2qnJLJRJBXA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



怎么实现呢？你当然可以把这个双倍长度的数组构造出来，然后套用算法模板。但是，我们可以不用构造新数组，而是利用循环数组的技巧来模拟。直接看代码吧：



```c++
vector<int> nextGreaterElements(vector<int>& nums) {
    int n = nums.size();
    vector<int> res(n); // 存放结果
    stack<int> s;
    // 假装这个数组长度翻倍了，注意最后比较的是整个右边的数据，相当于环形的了
    for (int i = 2 * n - 1; i >= 0; i--) {
        while (!s.empty() && s.top() <= nums[i % n])
            s.pop();
        res[i % n] = s.empty() ? -1 : s.top();
        s.push(nums[i % n]);
    }
    return res;
}
```



至此，你已经完全掌握了单调栈的设计方法，学会解决 Next Greater Number 这类问题，并且能够应付循环数组了。