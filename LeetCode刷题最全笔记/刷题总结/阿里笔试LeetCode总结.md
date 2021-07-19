---

title: 阿里笔试题
thumbnail: true
author: Kumi
date: 2020-02-04 22:20:51
icons: [fas fa-fire red, fas fa-star green]
cover: true
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN/21.jpg
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

https://leetcode-cn.com/circle/article/DUJeUx/

![image-20200910093618029](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200910093618029.png)

# [数字反转](https://leetcode-cn.com/problems/reverse-words-in-a-string/solution/151-fan-zhuan-zi-fu-chuan-li-de-dan-ci-shuang-zh-2/)



- 倒序遍历字符串 s ，记录单词左右索引边界 i , j；
- 每确定一个单词的边界，则将其添加至单词列表 res ；
- 最终，将单词列表拼接为字符串，并返回即可。

```python
class Solution:
    def reverseWords(self, s: str) -> str:
        s=s.strip()#去掉首尾空格
        i=j=len(s)-1#显然是从后边进行遍历
        res=[]#存储遍历之后的字符串
        while i>=0:
            while i>=0 and s[i]!=' ':#遇到空格
                i-=1
            res.append(s[i+1:j+1])
            while i>=0 and s[i]==' ':#
                i-=1
            j=i
        return ' '.join(res)

```

### 思路二

因为s.split()可以将空格隔开的字符串弄成list形式

```python
s1 = "we are family"#中间一个空格
s2 = "we  are  family"#中间两个空格
s3 = "we   are   family"#中间三个空格
s4 = "we    are    family"#中间四个空格

s1 = s1.split()
s2 = s2.split()
s3 = s3.split()
s4 = s4.split()

print(s1)#['we', 'are', 'family']
print(s2)#['we', 'are', 'family']
print(s3)#['we', 'are', 'family']
print(s4)#['we', 'are', 'family']
```

```python
class Solution:
    def reverseWords(self, s: str) -> str:
        s=s.strip()#去掉首尾空格
        s=s.split()
        s.reverse()
        return ' '.join(s)
```



### 删除链表重复元素

```python
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None

class Solution:
    def deleteDuplicates(self, head: ListNode) -> ListNode:
        cur=head
        while(cur and cur.next):#如果当前元素和后边元素都不为空，那么就进行数据的迭代
            if cur.val == cur.next.val:#满足条件就进行断开连接，同时看看下下个元素和当前元素是否是相同的
                cur.next=cur.next.next
            else:cur=cur.next#如果不是那么就增加
        return head
```

```python
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None
#快慢指针
class Solution:
    def deleteDuplicates(self, head: ListNode) -> ListNode:
        if not head or not head.next:#链表为空或者是链表下一个数据为空 即至多有一个数据，那直接返回当前head就可以
            return head
        slow=head
        fast=head.next
        while slow and slow.next: #slow 相当于重复值的开头 不断地判断 改变链接
            if slow.val==fast.val:
                slow.next=fast.next
                fast=fast.next
            else:#如果不同了，需要整体向前移动
                slow=slow.next
                fast=fast.next
        return head
```

# 删除重复元素2

## 题目描述

给定一个排序链表，删除所有含有重复数字的节点，只保留原始链表中 没有重复出现 的数字。

示例 1:
输入: `1->2->3->3->4->4->5`
输出: `1->2->5`

示例 2:
输入: `1->1->1->2->3`
输出: `2->3`

## 解法一

有时候当我们拿到一道题时，如果不能立马想到比较好的解决方案，不妨先用"笨"一点的方式去做一下，"笨"的方案虽然效率不高，但是实现起来简单，也容易想到，为后面再做优化也起到了一个铺垫的效果。

这里要求的是去重，那简单。用一个哈希表记录每个值出现的频率就可以了。
具体做法如下:

1. 遍历链表，将每个节点的值放到哈希表中，哈希表的key就是节点的值，value是这个值出现的频率
2. 遍历哈希表，将所有频率==1的key放到集合中
3. 对集合进行排序
4. 遍历集合，然后不断创建新的链表节点

当然这里可以优化一下，比如使用`LinkedHashMap`或者`OrderedDict`这样的数据结构，可以省去排序环节。

java代码:

```java
class Solution {
    public ListNode deleteDuplicates(ListNode head) {
        if(head==null || head.next==null) {
            return head;
        }
        //用哈希表记录每个节点值的出现频率
        HashMap<Integer,Integer> cache = new HashMap<Integer,Integer>();
        ArrayList<Integer> arr = new ArrayList<Integer>();
        ListNode p = head;
        while(p!=null) {
            int val = p.val;
            if(cache.containsKey(val)) {
                cache.put(val,cache.get(val)+1);
            } else {
                cache.put(val,1);
            }
            p = p.next;
        }
        //将所有只出现一次的值放到arr中，之后再对这个arr排序
        for(Integer k : cache.keySet()) {
            if(cache.get(k)==1) {
                arr.add(k);
            }
        }
        Collections.sort(arr);
        ListNode dummy = new ListNode(-1);
        p = dummy;
        //创建长度为arr.length长度的链表，依次将arr中的值赋给每个链表节点
        for(Integer i : arr) {
            ListNode tmp = new ListNode(i);
            p.next = tmp;
            p = p.next;
        }
        return dummy.next;
    }
}
```

python代码:

```java
class Solution(object):
    def deleteDuplicates(self, head):
        if not (head and head.next):
            return head
        # 用哈希表记录每个节点值的出现频率
        d = dict()
        p = head
        arr = []
        while p:
            val = p.val
            d[val] = d.setdefault(val,0)+1
            p = p.next
        # 将所有只出现一次的值放到arr中，之后再对这个arr排序
        for k in d:
            if d[k]==1:
                arr.append(k)
        arr = sorted(arr)
        dummy = ListNode(-1)
        p = dummy
        # 创建长度为len(arr)长度的链表，依次将arr中的值赋给每个链表节点
        for i in arr:
            tmp = ListNode(i)
            p.next = tmp
            p = p.next
        return dummy.next
```

## 解法二

这里我们使用双指针的方式，定义`a`，`b`两个指针。
考虑到一些边界条件，比如`1->1->1->2`这种情况，需要把开头的几个`1`给去掉，我们增加一个**哑结点**，方便边界处理。

#### 注意这里b.next and b因为之中出现了取值，所以不能为空，否则就会有取值错误的情况出现

初始的两个指针如下:

- 将`a`指针指向哑结点
- 将`b`指针指向`head`(哑结点的下一个节点)

如果`a`指向的值**不等于**`b`指向的值，则两个指针都前进一位
否则，就单独移动`b`，`b`不断往前走，直到`a`指向的值**不等于**`b`指向的值。

注意，这里不是直接比较`a.val==b.val`，这么比较不对，因为**初始**的时候，`a`指向的是哑结点，所以比较逻辑应该是这样：

```
a.next.val == b.next.val
```

当两个指针指向的值相等时，`b`不断往前移动，这里是通过一个`while`循环判断的，因为要过滤掉`1->2->2->2->3`重复的`2`。
那么整个逻辑就是两个`while`，但时间复杂度不是O(N^2)，而是O(N)，空间上也只是常数级别。

具体执行的动态如如下:

![img](https://mmbiz.qpic.cn/mmbiz_gif/smWnh5qQwsXn0naFvLibViarHhJYGKNLgrQXutIRVUe1kqm0kj5IoLC9Re9Ozjgmian52a5HuYjlaJHSOUKicYOhVg/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)

动态图中如果某一步执行太快没看清，可以看下面的静态图，由于没法用幻灯片演示，这里我将每一步的执行过程都整合到一起了。点击文章最下方的【阅读原文】有幻灯片版本的演示(需要在电脑端才能正常显示):  



![img](https://mmbiz.qpic.cn/mmbiz_jpg/smWnh5qQwsXn0naFvLibViarHhJYGKNLgrIm1DBmDPUuDcAGTQM4wuibbibfhvW10FqTH7CuT8MBiaSZWrYyba9ZKsA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



java代码:

```java
class Solution {
    public ListNode deleteDuplicates(ListNode head) {
        if(head==null || head.next==null) {
            return head;
        }
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode a = dummy;
        ListNode b = head;
        while(b!=null && b.next!=null) {
            //初始化的时a指向的是哑结点，所以比较逻辑应该是a的下一个节点和b的下一个节点
            if(a.next.val!=b.next.val) {
                a = a.next;
                b = b.next;
            }
            else {
                //如果a、b指向的节点值相等，就不断移动b，直到a、b指向的值不相等 
                while(b!=null && b.next!=null && a.next.val==b.next.val) {
                    b = b.next;
                }
                a.next = b.next;
                b = b.next;
            }
        }
        return dummy.next;
    }
}
```

python代码:

```python
class Solution(object):
    def deleteDuplicates(self, head):
        if not (head and head.next):
            return head
        dummy = ListNode(-1)
        dummy.next = head
        a = dummy
        b = head
        while b and b.next:
            # 初始化的时a指向的是哑结点，所以比较逻辑应该是a的下一个节点和b的下一个节点
            if a.next.val!=b.next.val:
                a = a.next
                b = b.next
            else:
                # 如果a、b指向的节点值相等，就不断移动b，直到a、b指向的值不相等 
                while b and b.next and a.next.val==b.next.val:
                    b = b.next
                a.next = b.next
                b = b.next
        return dummy.next
```

## 解法三

解法三和解法二的代码实现很类似，区别是
解法二初始化的时候`b`指针指向的是`head`
而解法三初始化的时候`b`指针指向的是`head.next`

所以判断两个指针指向的节点值是否相等时，解法三是这么做的:

```
a.next.val == b.val
```

当两个指针指向的值不同时，`a`和`b`指针都是前移一位
当两个指针指向的值相同时，解法二和解法三也略有不同
主要体现在`while`循环后面的几句
此外`b`指针还需要考虑边界条件，当循环结束后`b`指针可能会指向空，所以不能直接`b=b.next`，需要判断一下边界，这里请查看代码，并配合动态/静态图方便理解。

时间复杂度和空间复杂度，解法二和解法三都是一样的。

动画演示如下:

![img](https://mmbiz.qpic.cn/mmbiz_gif/smWnh5qQwsXn0naFvLibViarHhJYGKNLgrnpnHmDia7TGliaC8luekua17AgIf73ov9icXViaf61Rdac891xibUiba01KQ/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)

下面是静态长图:

![img](https://mmbiz.qpic.cn/mmbiz_jpg/smWnh5qQwsXn0naFvLibViarHhJYGKNLgr69O2dHKBZqWAfZZk19gUUMsDwSFFRicM7cdWFpgiaraUR19pkTITWZNA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



代码实现中还有一个小细节，外层的`while`是这么写的

```
while(b!=null)
```

如果写成

```
while(b!=null && b.next!=null)
```

这就不对了，没法处理`1->1`这种情况。

java代码:

```java
class Solution {
    public ListNode deleteDuplicates(ListNode head) {
        if(head==null || head.next==null) {
            return head;
        }
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode a = dummy;
        ListNode b = head.next;
        while(b!=null) {
            if(a.next.val!=b.val) {
                a = a.next;
                b = b.next;
            }
            else {
                while(b!=null && a.next.val==b.val) {
                    b = b.next;
                }
                //这里的去重跟解法二有点差别，解法二的是
                //a.next = b.next
                a.next = b;
                //b指针在while中判断完后，可能指向了null，这里需要处理边界问题
                b = (b==null) ? null : b.next;
            }
        }
        return dummy.next;
    }
}
```

python代码:

```python
class Solution(object):
    def deleteDuplicates(self, head):
        if not (head and head.next):
            return head
        dummy = ListNode(-1)
        dummy.next = head
        a = dummy
        b = head.next
        while b:
            if a.next.val!=b.val:
                a = a.next
                b = b.next
            else:
                while b and a.next.val==b.val:
                    b = b.next
                # 这里的去重跟解法二有点差别，解法二的是
                # a.next = b.next
                a.next = b
                # b指针在while中判断完后，可能指向了null，这里需要处理边界问题
                b = b.next if b else None
        return dummy.next
```

(全文完)

### 最多可以参加的会议

给你一个数组 events，其中 events[i] = [startDayi, endDayi] ，表示会议 i 开始于 startDayi ，结束于 endDayi 。

你可以在满足 startDayi <= d <= endDayi 中的任意一天 d 参加会议 i 。注意，一天只能参加一个会议。

请你返回你可以参加的 最大 会议数目。

![img](https://assets.leetcode-cn.com/aliyun-lc-upload/uploads/2020/02/16/e1.png)

输入：events = [[1,2],[2,3],[3,4]]
输出：3
解释：你可以参加所有的三个会议。
安排会议的一种方案如上图。
第 1 天参加第一个会议。
第 2 天参加第二个会议。
第 3 天参加第三个会议。
示例 2：

输入：events= [[1,2],[2,3],[3,4],[1,2]]
输出：4
示例 3：

输入：events = [[1,4],[4,4],[2,2],[3,4],[1,1]]
输出：4
示例 4：

输入：events = [[1,100000]]
输出：1
示例 5：

输入：events = [[1,1],[1,2],[1,3],[1,4],[1,5],[1,6],[1,7]]
输出：7

#### 暴力解法加标记 （复杂度太高，超时）

- 按照结束时间来排序会议
- 对于每一个会议来讲，从开始时间到结束时间进行遍历，如果有满足条件的day那么就加入visted ,否则就没有

```python
class Solution:
    def maxEvents(self,events):
        events.sort(key=lambda x:x[1])
        visited = set()
        for s,e in events:
            for day in range(s,e+1):
                if day not in visited:
                    visited.add(day)
                    break
        return len(visited)
```

#### 最小堆求解 （贪心算法）

https://blog.csdn.net/weixin_36372879/article/details/84573144 最小堆解释python

- 对于每一天来讲，我们肯定要参加的是能够参加的并且结束时间最早的会议
- 这样不断遍历天数，就可以得到所有的解

```python
class Solution:
    def maxEvents(self, events: List[List[int]]) -> int:
        max_day = 0#存储所有会议最大结束天数
        import collections
        import heapq#得到最小堆
        start_map = collections.defaultdict(list)#其实就是一个是键值为list的字典
        for event in events:#字典的key 是会议开始时间，value是相应的结束时间
            start_map[event[0]].append(event[1])
            max_day = max(max_day, event[1])
        cnt = 0 #最多可以参加个数
        heap = []#用来存储最小堆之后的排序值
        for i in range(1, max_day + 1):#对于每一天来讲
            while heap and heap[0] < i:#首先要把结束时间早于当天的会议排出堆
                heapq.heappop(heap)
            if i in start_map:#把今天开始的会议加入堆之中
                while start_map[i]:
                    heapq.heappush(heap, start_map[i].pop())
            if heap:#如果堆不为空
                cnt += 1#就可以取出当前结束时间最小的会议就可以
                heapq.heappop(heap)
        return cnt
```



### 子孙节点的最大从差距 LeetCode 1026

## 注意这个函数的定义，如果在类内（而不是单纯的在一个函数内部）需要加上self  自己调用自己也需要加上self 如果在一个函数内部定义的，那么不需要进行定义

![image-20200912151944359](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20200912151944359.png)

```python
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution:
    res=0 #存储最终数据
    def dfs(self,root,maxv,minv):
        if not root:
            self.res=max(abs(maxv-minv),self.res)
            return 
        maxv=max(root.val,maxv)
        minv=min(root.val,minv)
        self.dfs(root.left,maxv,minv)
        self.dfs(root.right,maxv,minv)
        #显然maxv和minV代表的是一条路走到黑之后的最大值和最小值
    def maxAncestorDiff(self, root: TreeNode) -> int:
        if not root:
            return 0
        self.dfs(root,0,100001)
        return self.res
```

### 另外一种写法

```python
class Solution:
    res=0 #存储最终数据
    def maxAncestorDiff(self, root: TreeNode) -> int:
        def dfs(root,maxv,minv):
            if not root:
                self.res=max(abs(maxv-minv),self.res)
                return 
            maxv=max(root.val,maxv)
            minv=min(root.val,minv)
            dfs(root.left,maxv,minv)
            dfs(root.right,maxv,minv)
        #显然maxv和minV代表的是一条路走到黑之后的最大值和最小值
        if not root:
            return 0
        dfs(root,0,100001)
        return self.res
```

## 链表排序

![image-20200912160600998](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200912160600998.png)

![Picture2.png](https://pic.leetcode-cn.com/8c47e58b6247676f3ef14e617a4686bc258cc573e36fcf67c1b0712fa7ed1699-Picture2.png)

```python
class Solution:
    def sortList(self, head: ListNode) -> ListNode:
        if not head or not head.next: return head # termination. #如果为空或者是只有一个节点直接返回
        # cut the LinkedList at the mid index.
        slow, fast = head, head.next#为了求出中间节点
        while fast and fast.next:
            fast, slow = fast.next.next, slow.next
        mid, slow.next = slow.next, None # save and cut.完全切断
        #得到的的head和mid就是两段的开始
        left, right = self.sortList(head), self.sortList(mid)
        #以上的递归就是求出排序好之后的两段开头
        
        #一下是两段的具体排序方法
        h = res = ListNode(0)#虚拟节点
        while left and right:#看看谁大谁小
            if left.val < right.val: h.next, left = left, left.next
            else: h.next, right = right, right.next
            h = h.next
        h.next = left if left else right#剩下的链接上
        return res.next#返回拍号的数据

```



##  第二种方式

```python
class Solution:
    def sortList(self, head: ListNode) -> ListNode:

        def merge(l1, l2):
            p = dummy = ListNode(-1)
            while l1 and l2:
                if l1.val <= l2.val:
                    p.next = l1; l1 = l1.next
                else:
                    p.next = l2; l2 = l2.next
                p = p.next
            p.next = l1 or l2
            return dummy.next
        
        if not head or not head.next:
            return head
        slow, fast = head, head.next
        while fast and fast.next:
            slow = slow.next
            fast = fast.next.next
        mid = slow.next
        slow.next = None
        return merge(self.sortList(head), self.sortList(mid))


```



## k个链表的排序问题

###  动态数组存储排序重新生成链表

```python
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None

class Solution:
    def mergeKLists(self, lists: List[ListNode]) -> ListNode:
        if not lists or len(lists) == 0:
            return None
        import heapq
        all_vals = []
        for l in lists:
            while l:
                all_vals.append(l.val)
                l = l.next
        all_vals.sort()
        dummy = ListNode(None)
        cur = dummy
        for i in all_vals:
            temp_node = ListNode(i)
            cur.next = temp_node
            cur = temp_node

        return dummy.next

```

### 最小堆

![2.jpg](https://pic.leetcode-cn.com/6a29e6a27232b5d42201b57c3ae9b256293b87a291f981c8a0f06e88e50c4379-2.jpg)

```python
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None

class Solution:
    def mergeKLists(self, lists: List[ListNode]) -> ListNode:
        if not lists or len(lists) == 0:
            return None
        import heapq
        heap = []
        # 首先 for 嵌套 while 就是将所有元素都取出放入堆中
        for node in lists:
            while node:
                heapq.heappush(heap, node.val)
                node = node.next
        dummy = ListNode(None)
        cur = dummy
        # 依次将堆中的元素取出(因为是小顶堆，所以每次出来的都是目前堆中值最小的元素），然后重新构建一个列表返回
        while heap:
            temp_node = ListNode(heappop(heap))
            cur.next = temp_node
            cur = temp_node
        return dummy.next

```

### 分治法

![6.jpg](https://pic.leetcode-cn.com/88d261465f1f21288dd23cef2f059297f5d053fc19805458a47ae1b05f3c0703-6.jpg)

```python
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None

class Solution:
    def merge(self, node_a, node_b):
        dummy = ListNode(None)
        cursor_a, cursor_b, cursor_res = node_a, node_b, dummy
        while cursor_a and cursor_b:  # 对两个节点的 val 进行判断，直到一方的 next 为空
            if cursor_a.val <= cursor_b.val:
                cursor_res.next = ListNode(cursor_a.val)
                cursor_a = cursor_a.next
            else:
                cursor_res.next = ListNode(cursor_b.val)
                cursor_b = cursor_b.next
            cursor_res = cursor_res.next
        # 有一方的next的为空，就没有比较的必要了，直接把不空的一边加入到结果的 next 上
        if cursor_a:
            cursor_res.next = cursor_a
        if cursor_b:
            cursor_res.next = cursor_b
        return dummy.next

    def mergeKLists(self, lists: List[ListNode]) -> ListNode:
        length = len(lists)

        # 边界情况
        if length == 0:
            return None
        if length == 1:
            return lists[0]

        # 分治
        mid = length // 2
        return self.merge(self.mergeKLists(lists[:mid]), self.mergeKLists(lists[mid:length]))


```

### 贪心算法+优先队列

##### 思路

每一次入堆的是链表第一个元素，通过索引每一次再加

![截图_202009121714001SS](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/截图_202009121714001SS.gif)

![image-20200912173513117](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200912173513117.png)

```python
# Python3 下的代码
from typing import List
import heapq

class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None


class Solution:
    def mergeKLists(self, lists: List[ListNode]) -> ListNode:
        l = []
        size = len(lists)

        for index in range(size):
            # 针对一些特殊的测试用例，有的链表可能是空链表
            if lists[index]:
                heapq.heappush(l, (lists[index].val, index))

        dummy_node = ListNode(-1)
        cur = dummy_node

        while l:
            _, index = heapq.heappop(l)

            # 定位到此时应该出列的那个链表的头结点
            head = lists[index]
            # 开始“穿针引线”
            cur.next = head
            cur = cur.next
            # 同样不要忘记判断到链表末尾结点的时候
            if head.next:
                # 刚刚出列的那个链表的下一个结点成为新的链表头结点加入优先队列
                heapq.heappush(l, (head.next.val, index))
                # 切断刚刚出列的那个链表的头结点引用
                lists[index] = head.next
                head.next = None
        return dummy_node.next


```

