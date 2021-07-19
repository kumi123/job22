---

title: 题解链表
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2020-02-19 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/23.jpg
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
# Leetcode 题解 - 链表
<!-- GFM-TOC -->
* [Leetcode 题解 - 链表](#leetcode-题解---链表)
    * [1. 找出两个链表的交点](#1-找出两个链表的交点)
    * [2. 链表反转](#2-链表反转)
    * [3. 归并两个有序的链表](#3-归并两个有序的链表)
    * [4. 从有序链表中删除重复节点](#4-从有序链表中删除重复节点)
    * [5. 删除链表的倒数第 n 个节点](#5-删除链表的倒数第-n-个节点)
    * [6. 交换链表中的相邻结点](#6-交换链表中的相邻结点)
    * [7. 链表求和](#7-链表求和)
    * [8. 回文链表](#8-回文链表)
    * [9. 分隔链表](#9-分隔链表)
    * [10. 链表元素按奇偶聚集](#10-链表元素按奇偶聚集)
<!-- GFM-TOC -->


链表是空节点，或者有一个值和一个指向下一个链表的指针，因此很多链表问题可以用递归来处理。

##  1. 找出两个链表的交点

160\. Intersection of Two Linked Lists (Easy)

[Leetcode](https://leetcode.com/problems/intersection-of-two-linked-lists/description/) / [力扣](https://leetcode-cn.com/problems/intersection-of-two-linked-lists/description/)

例如以下示例中 A 和 B 两个链表相交于 c1：

```html
A:          a1 → a2
                    ↘
                      c1 → c2 → c3
                    ↗
B:    b1 → b2 → b3
```

但是不会出现以下相交的情况，因为每个节点只有一个 next 指针，也就只能有一个后继节点，而以下示例中节点 c 有两个后继节点。

```html
A:          a1 → a2       d1 → d2
                    ↘  ↗
                      c
                    ↗  ↘
B:    b1 → b2 → b3        e1 → e2
```



要求时间复杂度为 O(N)，空间复杂度为 O(1)。如果不存在交点则返回 null。

设 A 的长度为 a + c，B 的长度为 b + c，其中 c 为尾部公共部分长度，可知 a + c + b = b + c + a。

当访问 A 链表的指针访问到链表尾部时，令它从链表 B 的头部开始访问链表 B；同样地，当访问 B 链表的指针访问到链表尾部时，令它从链表 A 的头部开始访问链表 A。这样就能控制访问 A 和 B 两个链表的指针能同时访问到交点。

如果不存在交点，那么 a + b = b + a，以下实现代码中 l1 和 l2 会同时为 null，从而退出循环。

```java
public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
    ListNode l1 = headA, l2 = headB;
    while (l1 != l2) {
        l1 = (l1 == null) ? headB : l1.next;
        l2 = (l2 == null) ? headA : l2.next;
    }
    return l1;
}
```

如果只是判断是否存在交点，那么就是另一个问题，即 [编程之美 3.6]() 的问题。有两种解法：

- 把第一个链表的结尾连接到第二个链表的开头，看第二个链表是否存在环；
- 或者直接比较两个链表的最后一个节点是否相同。

##  2. 链表反转

206\. Reverse Linked List (Easy)

[Leetcode](https://leetcode.com/problems/reverse-linked-list/description/) / [力扣](https://leetcode-cn.com/problems/reverse-linked-list/description/)

递归

```java
public ListNode reverseList(ListNode head) {
    if (head == null || head.next == null) {
        return head;
    }
    ListNode next = head.next;
    ListNode newHead = reverseList(next);
    next.next = head;
    head.next = null;
    return newHead;
}
```

头插法

```java
public ListNode reverseList(ListNode head) {
    ListNode newHead = new ListNode(-1);
    while (head != null) {
        ListNode next = head.next;
        head.next = newHead.next;
        newHead.next = head;
        head = next;
    }
    return newHead.next;
}
```

##  3. 归并两个有序的链表

21\. Merge Two Sorted Lists (Easy)

[Leetcode](https://leetcode.com/problems/merge-two-sorted-lists/description/) / [力扣](https://leetcode-cn.com/problems/merge-two-sorted-lists/description/)

```java
public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
    if (l1 == null) return l2;
    if (l2 == null) return l1;
    if (l1.val < l2.val) {
        l1.next = mergeTwoLists(l1.next, l2);
        return l1;
    } else {
        l2.next = mergeTwoLists(l1, l2.next);
        return l2;
    }
}
```

##  4. 从有序链表中删除重复节点

83\. Remove Duplicates from Sorted List (Easy)

[Leetcode](https://leetcode.com/problems/remove-duplicates-from-sorted-list/description/) / [力扣](https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list/description/)



##### [题解](https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list/solution/shan-chu-pai-xu-lian-biao-zhong-de-zhong-fu-yuan-2/)



- [x] 犯错while和if用的不熟练，应该是while
- [x] 还是推荐单指针，注意while条件是根据内部不能出现空指针

##### 单指针

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
        //特殊条件,如果为空那么就直接返回，如果只有一个节点，直接返回当前节点
        if(head==null||head.next==null) return head;
        ListNode cur=head;
       //注意没有全部绑定好不好
       while(cur!=null&&cur.next!=null){
           if(cur.val==cur.next.val){
               cur.next=cur.next.next;
           }
           else{
               cur=cur.next;
           }
       }
        return head;



    }
}
```

#### 双指针

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
        //感觉像是双指针，遇到相同的就会直接进行跳到下一个不是你的，我们的目的肯定是删除后边的多余元素
        //特殊条件,如果为空那么就直接返回，如果只有一个节点，直接返回当前节点
        if(head==null||head.next==null) return head;
        //下边因为快指针是再慢指针后边，如果相同的值，快指针往后 慢指针不动 如果不相同，那么就一起往后
       //注意没有全部绑定好不好
        ListNode fast=head.next;
        ListNode slow=head;
        while(slow.next!=null){
            if(fast.val==slow.val){
                if(fast.next==null){
                    slow.next=null;
                }
                else{
                slow.next=fast.next;
                fast=fast.next;}
            }
            else {
                fast=fast.next;
                slow=slow.next;
            }
        }
        return head;



    }
}
```



```html
Given 1->1->2, return 1->2.
Given 1->1->2->3->3, return 1->2->3.
```

```java
public ListNode deleteDuplicates(ListNode head) {
    if (head == null || head.next == null) return head;
    head.next = deleteDuplicates(head.next);
    return head.val == head.next.val ? head.next : head;
}
```

##  5. 删除链表的倒数第 n 个节点

19\. Remove Nth Node From End of List (Medium)

[Leetcode](https://leetcode.com/problems/remove-nth-node-from-end-of-list/description/) / [力扣](https://leetcode-cn.com/problems/remove-nth-node-from-end-of-list/description/)



#### 设置一个dummy，定位到前一个slow直接隔断就可以



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
    public ListNode removeNthFromEnd(ListNode head, int n) {
        //int sum=0;//其实也没必要判断出错的情况
        ListNode dummy=new ListNode(-1);
        dummy.next=head;
        ListNode slow=dummy,fast=head;
        while(n-1>0){
            fast=fast.next;
            n--;
        }
        while(fast.next!=null){
            slow=slow.next;
            fast=fast.next;
        }
        slow.next=slow.next.next;//如果删除的是第一个头部，循环进不去，slow现在等同于dummy 直接修改头结点并且返回
        return dummy.next;
    

    }
}
```



```html
Given linked list: 1->2->3->4->5, and n = 2.
After removing the second node from the end, the linked list becomes 1->2->3->5.
```

```java
public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode fast = head;
    while (n-- > 0) {
        fast = fast.next;
    }
    if (fast == null) return head.next;
    ListNode slow = head;
    while (fast.next != null) {
        fast = fast.next;
        slow = slow.next;
    }
    slow.next = slow.next.next;
    return head;
}
```

##  6. 交换链表中的相邻结点

24\. Swap Nodes in Pairs (Medium)

[Leetcode](https://leetcode.com/problems/swap-nodes-in-pairs/description/) / [力扣](https://leetcode-cn.com/problems/swap-nodes-in-pairs/description/)

```python
class Solution:
    def swapPairs(self, head: ListNode) -> ListNode:
        thead = ListNode(-1)
        thead.next = head
        c = thead
        while c.next and c.next.next:
            a, b=c.next, c.next.next
            c.next, a.next = b, b.next
            b.next = a
            c = c.next.next
        return thead.next


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
    public ListNode swapPairs(ListNode head) {
        //本质上就是两个一组两个一组来进行交换，注意就可以，用l1和l2来进行表示
        //使用一个dummy来来进行过度一下
        ListNode dummy=new ListNode(-1);
        dummy.next=head;
        //ListNode next;//存储原本两组的内部的第二个的下一个，其实就是下一组的l1，要用当前组的l1链接它
        ListNode pre=dummy;//存储上一组的l2，要用当前组的l2被他链接
        while(pre!=null&&pre.next!=null&&pre.next.next!=null){
            ListNode l1=pre.next;
            ListNode l2=pre.next.next;
            ListNode next=l2.next;
            l2.next=l1;
            l1.next=next;
            pre.next=l2;
            pre=pre.next.next;
            
        }
        return dummy.next;

    }
}
```



```html
Given 1->2->3->4, you should return the list as 2->1->4->3.
```

题目要求：不能修改结点的 val 值，O(1) 空间复杂度。

```java
public ListNode swapPairs(ListNode head) {
    ListNode node = new ListNode(-1);
    node.next = head;
    ListNode pre = node;
    while (pre.next != null && pre.next.next != null) {
        ListNode l1 = pre.next, l2 = pre.next.next;
        ListNode next = l2.next;
        l1.next = next;
        l2.next = l1;
        pre.next = l2;

        pre = l1;
    }
    return node.next;
}
```

##  7. 链表求和

445\. Add Two Numbers II (Medium)

[Leetcode](https://leetcode.com/problems/add-two-numbers-ii/description/) / [力扣](https://leetcode-cn.com/problems/add-two-numbers-ii/description/)

```html
Input: (7 -> 2 -> 4 -> 3) + (5 -> 6 -> 4)
Output: 7 -> 8 -> 0 -> 7
```

题目要求：不能修改原始链表。



#### 自己的做法

```java
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

    while(!stack1.isEmpty()||!stack2.isEmpty()||add!=0){
        int x=(stack1.isEmpty()==false)?stack1.pop():0;
        int y=(stack2.isEmpty()==false)?stack2.pop():0;
        int sum=x+y+add;
        add=sum/10;
        ListNode newnode=new ListNode(sum%10);
        newnode.next=head;
        head=newnode;

    }
    return head;
}
 }
```









```java
public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    Stack<Integer> l1Stack = buildStack(l1);
    Stack<Integer> l2Stack = buildStack(l2);
    ListNode head = new ListNode(-1);
    int carry = 0;
    while (!l1Stack.isEmpty() || !l2Stack.isEmpty() || carry != 0) {
        int x = l1Stack.isEmpty() ? 0 : l1Stack.pop();
        int y = l2Stack.isEmpty() ? 0 : l2Stack.pop();
        int sum = x + y + carry;
        ListNode node = new ListNode(sum % 10);
        node.next = head.next;
        head.next = node;
        carry = sum / 10;
    }
    return head.next;
}

private Stack<Integer> buildStack(ListNode l) {
    Stack<Integer> stack = new Stack<>();
    while (l != null) {
        stack.push(l.val);
        l = l.next;
    }
    return stack;
}
```

##  8. 回文链表

234\. Palindrome Linked List (Easy)

[Leetcode](https://leetcode.com/problems/palindrome-linked-list/description/) / [力扣](https://leetcode-cn.com/problems/palindrome-linked-list/description/)

题目要求：以 O(1) 的空间复杂度来求解。

切成两半，把后半段反转，然后比较两半是否相等。



#### 很一般方法 

- 放到栈当中呃呃，和原来的比较
- 存到数组中，敦双指针比较

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
        //存到数组中
        if(head==null||head.next==null) return true;
        List<Integer> res= new ArrayList<>();
        while(head!=null){
            res.add(head.val);
            head=head.next;
        }
        int slow=0,fast=res.size()-1;
        while(slow<fast){
            if(res.get(slow)!=res.get(fast)) return false;
            slow++;
            fast--;
        }
        return true;


    }
}
```



### [思路](https://leetcode-cn.com/problems/palindrome-linked-list/solution/dong-hua-yan-shi-234-hui-wen-lian-biao-by-user7439/)

![回文链表-动态图.gif](https://pic.leetcode-cn.com/5781a82fcd49c3ee8341f7a0dc801d4924f8e6daa994455cf348c13fd7903321-%E5%9B%9E%E6%96%87%E9%93%BE%E8%A1%A8-%E5%8A%A8%E6%80%81%E5%9B%BE.gif)

#### 自己

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
        //如果只有一个，那么就直接是回文
        if(head==null||head.next==null) return true;
        //找到中间的节点，注意这里需要一个伪节点dummy
        //在遍历找节点过程当中需要进行断开节点，把后一段的进行翻转就可以
        //以前边的节点的长度来进行遍历就可以
        //注意是前边的长度长，所以比较的时候需要以后边为准
        ListNode dummy=new ListNode(-1);
        dummy.next=head;
        ListNode fast=dummy;
        ListNode slow=dummy;
        while(fast!=null&&fast.next!=null){
            slow=slow.next;
            fast=fast.next.next;
        }
        //现在slow是在要断的前边
        ListNode cur=slow.next;//后边一段的开始节点
        slow.next=null;//断开前边节点
        ListNode newfront=dummy.next;//前边重新开始
        //下边是翻转后边的
        ListNode next;
        ListNode pre=null;
        while(cur!=null){
            next=cur.next;
            cur.next=pre;
            pre=cur;
            cur=next;   
        }
                //注意是前边的长度长，所以比较的时候需要以后边为准
        while(pre!=null){
            if(newfront.val!=pre.val) return false;
            newfront=newfront.next;
            pre=pre.next;
        }
        return true;


	}
}


```



```java
public boolean isPalindrome(ListNode head) {
    if (head == null || head.next == null) return true;
    ListNode slow = head, fast = head.next;
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
    }
    if (fast != null) slow = slow.next;  // 偶数节点，让 slow 指向下一个节点
    cut(head, slow);                     // 切成两个链表
    return isEqual(head, reverse(slow));
}

private void cut(ListNode head, ListNode cutNode) {
    while (head.next != cutNode) {
        head = head.next;
    }
    head.next = null;
}

private ListNode reverse(ListNode head) {
    ListNode newHead = null;
    while (head != null) {
        ListNode nextNode = head.next;
        head.next = newHead;
        newHead = head;
        head = nextNode;
    }
    return newHead;
}

private boolean isEqual(ListNode l1, ListNode l2) {
    while (l1 != null && l2 != null) {
        if (l1.val != l2.val) return false;
        l1 = l1.next;
        l2 = l2.next;
    }
    return true;
}
```

##  9. 分隔链表

725\. Split Linked List in Parts(Medium)

[Leetcode](https://leetcode.com/problems/split-linked-list-in-parts/description/) / [力扣](https://leetcode-cn.com/problems/split-linked-list-in-parts/description/)

```html
Input:
root = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], k = 3
Output: [[1, 2, 3, 4], [5, 6, 7], [8, 9, 10]]
Explanation:
The input has been split into consecutive parts with size difference at most 1, and earlier parts are a larger size than the later parts.
```

题目描述：把链表分隔成 k 部分，每部分的长度都应该尽可能相同，排在前面的长度应该大于等于后面的。





#### 思路比较清晰的

按照题意，首先求出链表中节点个数count：

如果count <= k，表示每个子节点存在于结果数组中；

如果k > count，把链表分为k份，每份至少有count/k个元素，未分配元素为count%k，因为未分配元素在范围为[0,k)，所以给每份元素数从前到后+1，以满足题目条件

如果8个元素分成3分，每份至少有8/3=2个元素，即[2,2,2]，剩余8%3=2个元素，分给第一份和第二份，为[3,3,2]

- [ ] 其实就是首先计算出每一个坑里边应该放多少个节点
- [ ] 然后把头结点放进去就可以啦
- [ ] 注意在哪里把节点断开

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
public ListNode[] splitListToParts(ListNode root, int k) {
    //if (root == null) return null;
    ListNode[] res = new ListNode[k];
    //求出整个链表的长度
    ListNode p = root;
    int count = 0;
    while (p != null) {
        count++;
        p = p.next;
    }
    if(count==0) return res;
    p = root;

    //1.k >= 总长度，取每个链表节点
    if (k >= count) {
        for (int i = 0; i < count; i++) {
            res[i] = new ListNode(p.val);
            p = p.next;
        }
    } else {
        //2. k < 总长度，
        int remain = count % k;
        int preCount = count / k;
        //记录每部分需要储存多少个链表元素
        int[] counts = new int[k];
        for (int i = 0; i < k; i++) {
            counts[i] = remain-- > 0 ? preCount + 1 : preCount;
        }
        //遍历链表，储存元素
        for (int i = 0; i < k; i++) {
            //获取当前部分需要的元素个数
            int num = counts[i];
            res[i] = p;
            //调整p的位置
            while (--num > 0) {
                p = p.next;
            }
            //截断链表
            ListNode tmp = p.next;
            p.next = null;
            p = tmp;
        }
    }
    return res;
}

}
```



#### 自己写的



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
    public ListNode[] splitListToParts(ListNode root, int k) {
        //首先输出是一个存储listnode的数组，其实每一组只是存储这个头结点就可以
        //要注意合适的头结点并且把后边尾巴断掉
        ListNode[] res=new ListNode[k];
        if(root==null) return res;//如果无法分割，应该返回每res，尽管每一个都可能是空的结果
        //计算总的长度，这里一般的trick就是自己新建一个节点代替root来循环，第二次循环再给指回来
        int count=0;
        ListNode p=root;
        while(p!=null){
            count++;
            p=p.next;
        }
        p=root;
        if(count>=k){
        //建立一个数组来存储每一个k节点数组应该存储的个数
        int[] nums=new int[k];
        int precount=count/k;
        int remain=count%k;
        //每一个坑里边数据个数
        for(int i=0;i<k;i++){
            nums[i]=(remain-->0)?precount+1:precount;
            res[i]=p;  
            while(nums[i]-->1){
                p=p.next;
            } 
            ListNode next=p.next;
            p.next=null;
            p=next;
        }
        }
        else{
            for(int i=0;i<count;i++){
                res[i]=p;
                ListNode next2=p.next;
                p.next=null;
                p=next2;
                
                
            }
        }
        return res;
        

    }
}
```



```java
public ListNode[] splitListToParts(ListNode root, int k) {
    int N = 0;
    ListNode cur = root;
    while (cur != null) {
        N++;
        cur = cur.next;
    }
    int mod = N % k;
    int size = N / k;
    ListNode[] ret = new ListNode[k];
    cur = root;
    for (int i = 0; cur != null && i < k; i++) {
        ret[i] = cur;
        int curSize = size + (mod-- > 0 ? 1 : 0);
        for (int j = 0; j < curSize - 1; j++) {
            cur = cur.next;
        }
        ListNode next = cur.next;
        cur.next = null;
        cur = next;
    }
    return ret;
}
```

##  10. 链表元素按奇偶聚集

328\. Odd Even Linked List (Medium)

[Leetcode](https://leetcode.com/problems/odd-even-linked-list/description/) / [力扣](https://leetcode-cn.com/problems/odd-even-linked-list/description/)



#### 自己

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
    public ListNode oddEvenList(ListNode head) {
        //真的是合理，就是分别构造偶数下标链表和奇数下标链表
        //使用的是尾插法
        ListNode evenhead=new ListNode(-1);
        ListNode oddhead=new ListNode(-1);
        ListNode eventail=evenhead;
        ListNode oddtail=oddhead;
        boolean isodd=true;
        while(head!=null){
            if(isodd){
                //尾插法尾巴移动就可以
                oddtail.next=head;
                oddtail=oddtail.next;
            }
            else{
                eventail.next=head;
                eventail=eventail.next;               
            }
            head=head.next;
            isodd=!isodd;
        }
        //现在得到了两个序列
        oddtail.next=evenhead.next;
        eventail.next=null;
        return oddhead.next;
    }
}


```



```html
Example:
Given 1->2->3->4->5->NULL,
return 1->3->5->2->4->NULL.
```

```java
public ListNode oddEvenList(ListNode head) {
    if (head == null) {
        return head;
    }
    ListNode odd = head, even = head.next, evenHead = even;
    while (even != null && even.next != null) {
        odd.next = odd.next.next;
        odd = odd.next;
        even.next = even.next.next;
        even = even.next;
    }
    odd.next = evenHead;
    return head;
}
```
