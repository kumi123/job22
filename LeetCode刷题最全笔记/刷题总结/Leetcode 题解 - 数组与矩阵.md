
---

title: 题解数组与矩阵
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2020-02-25 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/10.jpg
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
# Leetcode 题解 - 数组与矩阵
<!-- GFM-TOC -->
* [Leetcode 题解 - 数组与矩阵](#leetcode-题解---数组与矩阵)
    * [1. 把数组中的 0 移到末尾](#1-把数组中的-0-移到末尾)
    * [2. 改变矩阵维度](#2-改变矩阵维度)
    * [3. 找出数组中最长的连续 1](#3-找出数组中最长的连续-1)
    * [4. 有序矩阵查找](#4-有序矩阵查找)
    * [5. 有序矩阵的 Kth Element](#5-有序矩阵的-kth-element)
    * [6. 一个数组元素在 [1, n] 之间，其中一个数被替换为另一个数，找出重复的数和丢失的数](#6-一个数组元素在-[1-n]-之间，其中一个数被替换为另一个数，找出重复的数和丢失的数)
    * [7. 找出数组中重复的数，数组值在 [1, n] 之间](#7-找出数组中重复的数，数组值在-[1-n]-之间)
    * [8. 数组相邻差值的个数](#8-数组相邻差值的个数)
    * [9. 数组的度](#9-数组的度)
    * [10. 对角元素相等的矩阵](#10-对角元素相等的矩阵)
    * [11. 嵌套数组](#11-嵌套数组)
    * [12. 分隔数组](#12-分隔数组)
<!-- GFM-TOC -->


## 1. 把数组中的 0 移到末尾

283\. Move Zeroes (Easy)

[Leetcode](https://leetcode.com/problems/move-zeroes/description/) / [力扣](https://leetcode-cn.com/problems/move-zeroes/description/)

```html
For example, given nums = [0, 1, 0, 3, 12], after calling your function, nums should be [1, 3, 12, 0, 0].
```

```java
class Solution {
    public void moveZeroes(int[] nums) {
        //你是真的傻，找出不为0的，然后填写上去，之后就是填写0就可
        int  ind=0;
        for(int num:nums){
            if(num!=0){
                nums[ind++]=num;
            }
        }
        for(int j=ind;j<nums.length;j++){
            nums[j]=0;
        }

    }
}


public void moveZeroes(int[] nums) {
    int idx = 0;
    for (int num : nums) {
        if (num != 0) {
            nums[idx++] = num;
        }
    }
    while (idx < nums.length) {
        nums[idx++] = 0;
    }
}
```

## 2. 改变矩阵维度

566\. Reshape the Matrix (Easy)

[Leetcode](https://leetcode.com/problems/reshape-the-matrix/description/) / [力扣](https://leetcode-cn.com/problems/reshape-the-matrix/description/)

```html
Input:
nums =
[[1,2],
 [3,4]]
r = 1, c = 4

Output:
[[1,2,3,4]]

Explanation:
The row-traversing of nums is [1,2,3,4]. The new reshaped matrix is a 1 * 4 matrix, fill it row by row by using the previous list.
```

```java
public int[][] matrixReshape(int[][] nums, int r, int c) {
    int m = nums.length, n = nums[0].length;
    if (m * n != r * c) {
        return nums;
    }
    int[][] reshapedNums = new int[r][c];
    int index = 0;
    for (int i = 0; i < r; i++) {
        for (int j = 0; j < c; j++) {
            reshapedNums[i][j] = nums[index / n][index % n];
            index++;
        }
    }
    return reshapedNums;
}


//这种做法没什么问题
class Solution {
    public int[][] matrixReshape(int[][] nums, int r, int c) {
        //这个就是基本的题目啦，本质上弄清楚这个下标所在的位置在哪里就好啦
        int m=nums.length;//行数
        int n=nums[0].length;//列数
        if(r*c!=m*n) return nums;
        int[][] res=new int[r][c];
        //下边来进行填充的时候就是位置对了就可
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                int ind=i*r+j;
                res[i][j]=nums[ind/n][ind%n];//注意就是这个好不好，因为ind是从0开始的

            }
        }
        return res;


    }
}
```

## 3. 找出数组中最长的连续 1

485\. Max Consecutive Ones (Easy)

[Leetcode](https://leetcode.com/problems/max-consecutive-ones/description/) / [力扣](https://leetcode-cn.com/problems/max-consecutive-ones/description/)

```java
public int findMaxConsecutiveOnes(int[] nums) {
    int max = 0, cur = 0;
    for (int x : nums) {
        cur = x == 0 ? 0 : cur + 1;
        max = Math.max(max, cur);
    }
    return max;
}

//自己的方法 https://leetcode-cn.com/problems/max-consecutive-ones/solution/java-485-zui-da-lian-xu-1de-ge-shu-hua-dong-chuang/ 第一种方法

class Solution {
    public int findMaxConsecutiveOnes(int[] nums) {
        
        
       
        int count=0;//第一就是每一次遇到1就进行统计 //每一次遇到0 归零 
        int maxed=0; //并且比较统计过的1的个数的最大值
        for(int i=0;i<nums.length;i++){
            if(nums[i]==1) count++;
            else{
                maxed=Math.max(count,maxed);
                count=0;
            }
        }
        //这个是只有不为1的时候才能进行更新
        //所以需要考虑这个最后的几个数是连续的1
        return (maxed>count)?maxed:count;

    }
}
```
### 更为巧妙的方法

#### [保存最后一个0所在的位置](https://leetcode-cn.com/problems/max-consecutive-ones/solution/yi-ci-bian-li-bao-cun-yu-dao-de-zui-hou-z25k1/)

```java
class Solution {
    public int findMaxConsecutiveOnes(int[] nums) {
        int index=-1;//保存每一个序列当中最后一个0所在的位置 相当于保存了滑动窗口的左边界
        //这样如果遇到1的话就直接下标减去这个左边界并且比较最大值就可以啦
        int maxed=0;
        for(int i=0;i<nums.length;i++){
            if(nums[i]==0) index=i;
            else{
                maxed=Math.max(maxed,i-index);

            }
        }
        return maxed;
    }
}
```




## 4. 有序矩阵查找（==这种一定一定一定注意，遍历的方向要注意，两个方向一定是一个增大，一个减小，好不好==）

240\. Search a 2D Matrix II (Medium)

[Leetcode](https://leetcode.com/problems/search-a-2d-matrix-ii/description/) / [力扣](https://leetcode-cn.com/problems/search-a-2d-matrix-ii/description/)

```html
[
   [ 1,  5,  9],
   [10, 11, 13],
   [12, 13, 15]
]
```

```java
public boolean searchMatrix(int[][] matrix, int target) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return false;
    int m = matrix.length, n = matrix[0].length;
    int row = 0, col = n - 1;
    while (row < m && col >= 0) {
        if (target == matrix[row][col]) return true;
        else if (target < matrix[row][col]) col--;
        else row++;
    }
    return false;
}


//找准方向

class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        //这种一定一定一定注意，遍历的方向要注意，两个方向一定是一个增大，一个减小，好不好
        //如果两个方向的单调性相同，这是完全不可以的
        //这个可以从右上角来搜索
        //感觉直接进行搜索就可以啦
        int m=matrix.length,n=matrix[0].length;
        int i=0,j=n-1;//查询的初始坐标，从右上角
        while(i<m&&j>-1){
            if(matrix[i][j]<target){
                i++;    
            }
            else if(matrix[i][j]>target){
                j--;

            }
            else return true;
        }

    return false;    
    }
    
}
```

## 5. 有序矩阵的 Kth Element

378\. Kth Smallest Element in a Sorted Matrix ((Medium))

[Leetcode](https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/description/) / [力扣](https://leetcode-cn.com/problems/kth-smallest-element-in-a-sorted-matrix/description/)

```html
matrix = [
  [ 1,  5,  9],
  [10, 11, 13],
  [12, 13, 15]
],
k = 8,

return 13.
```

解题参考：[Share my thoughts and Clean Java Code](https://leetcode-cn.com/problems/kth-smallest-element-in-a-sorted-matrix/discuss/85173)

二分查找解法：

```java
public int kthSmallest(int[][] matrix, int k) {
    int m = matrix.length, n = matrix[0].length;
    int lo = matrix[0][0], hi = matrix[m - 1][n - 1];
    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        int cnt = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n && matrix[i][j] <= mid; j++) {
                cnt++;
            }
        }
        if (cnt < k) lo = mid + 1;
        else hi = mid - 1;
    }
    return lo;
}
```

==堆解法：==

```java
class Solution {
    public int kthSmallest(int[][] matrix, int k) {
        //直接用堆来做就完事儿了,找最小值，必然用的是最大堆
        int m=matrix.length;
        int n=matrix[0].length;
        if(k<0||k>(m*n)||matrix==null) return -1;
        PriorityQueue<Integer> queue=new PriorityQueue<>((e1,e2)->e2-e1);//最大堆
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(queue.size()<k) queue.offer(matrix[i][j]);
                else{
                    if(queue.peek()>=matrix[i][j]){
                        queue.offer(matrix[i][j]);
                        queue.poll();
                    }
                }
            }
        }
        return queue.peek();  

    }
}
```

## 6. 一个数组元素在 [1, n] 之间，其中一个数被替换为另一个数，找出重复的数和丢失的数

645\. Set Mismatch (Easy)

[Leetcode](https://leetcode.com/problems/set-mismatch/description/) / [力扣](https://leetcode-cn.com/problems/set-mismatch/description/)

```html
Input: nums = [1,2,2,4]
Output: [2,3]
```

```html
Input: nums = [1,2,2,4]
Output: [2,3]
```

最直接的方法是先对数组进行排序，这种方法时间复杂度为 O(NlogN)。本题可以以 O(N) 的时间复杂度、O(1) 空间复杂度来求解。

主要思想是通过交换数组元素，使得数组上的元素在正确的位置上。



```java
//就简单的用数组模拟哈希表存储
class Solution {
    public int[] findErrorNums(int[] nums) {
        //最easy的方法，但是比较弱智，就是使用哈希表
        //首先申请同等大小的数组来存储
        int[] store=new int[nums.length];
        int[] res=new int[2];
        for(int num:nums){
            store[num-1]++;
        }

        for(int i=0;i<store.length;i++){
            if(store[i]==2) res[0]=i+1;
            if(store[i]==0) res[1]=i+1;
        }
        return res;

    }
}
```



```java
public int[] findErrorNums(int[] nums) {
    for (int i = 0; i < nums.length; i++) {
        while (nums[i] != i + 1 && nums[nums[i] - 1] != nums[i]) {
            swap(nums, i, nums[i] - 1);
        }
    }
    for (int i = 0; i < nums.length; i++) {
        if (nums[i] != i + 1) {
            return new int[]{nums[i], i + 1};
        }
    }
    return null;
}

private void swap(int[] nums, int i, int j) {
    int tmp = nums[i];
    nums[i] = nums[j];
    nums[j] = tmp;
}
```

## 7. 找出数组中重复的数，数组值在 [1, n] 之间

287\. Find the Duplicate Number (Medium)

[Leetcode](https://leetcode.com/problems/find-the-duplicate-number/description/) / [力扣](https://leetcode-cn.com/problems/find-the-duplicate-number/description/)

#### 使用数组当做哈希表存储（使用额外的空间）

```java
class Solution {
    public int findDuplicate(int[] nums) {
        //肯定有很多种解法。用数组存储就可以
        int[] res=new int[nums.length-1];
        int result=0;
        for(int num:nums){
            res[num-1]++;
        }
        for(int i=0;i<res.length;i++){
            if(res[i]>=2) {
                result=i+1;
                break;
            }
        }
        return result;
    }
}
```

要求不能修改数组，也不能使用额外的空间。

二分查找解法：

```java
public int findDuplicate(int[] nums) {
     int l = 1, h = nums.length - 1;
     while (l <= h) {
         int mid = l + (h - l) / 2;
         int cnt = 0;
         for (int i = 0; i < nums.length; i++) {
             if (nums[i] <= mid) cnt++;
         }
         if (cnt > mid) h = mid - 1;
         else l = mid + 1;
     }
     return l;
}
```

双指针解法，类似于有环链表中找出环的入口：

#### [环形链表](https://leetcode-cn.com/problems/linked-list-cycle-ii/solution/linked-list-cycle-ii-kuai-man-zhi-zhen-shuang-zhi-/)



![image-20210301153155452](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210301153155452.png)

![image-20210301153217694](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210301153217694.png)

```json
public class Solution {
    public ListNode detectCycle(ListNode head) {
        //思路很清晰，一定注意数学的分析
        //快慢指针
        ListNode fast=head;
        ListNode slow=head;
        //这里判断条件不能是fast！=slow 因为一开始一样啊
        while(true){//需要进行快速遍历和慢速遍历得到相遇的点并且跳出来
            if(fast==null||fast.next==null) return null;//说明没有相遇点
            slow=slow.next;
            fast=fast.next.next;//如果有环那么一定会不为空
            if(slow==fast) break;
        }
        fast=head;
        while(slow!=fast){
            slow=slow.next;
            fast=fast.next;
        }
        return fast;

        
    }
}














//这是环形链表的问题
var detectCycle = function (head) {
  let slow = head;
  let fast = head;
  while (fast) {
    if (fast.next == null) { // fast.next走出链表了，说明无环
      return null;
    }
    slow = slow.next;        // 慢指针走一步
    fast = fast.next.next;   // 慢指针走一步
    if (slow == fast) {      // 首次相遇
      fast = head;           // 让快指针回到头节点
      while (true) {         // 开启循环，让快慢指针相遇
        if (slow == fast) {  // 相遇，在入环处
          return slow;
        }
        slow = slow.next;
        fast = fast.next;    // 快慢指针都走一步
      }
    }
  }
  return null;
};


```



#### 使用快慢指针求解

![image-20210301160338756](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210301160338756.png)

![image-20210301160405457](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210301160405457.png)

![image-20210301160431354](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210301160431354.png)

```java
class Solution {
    public int findDuplicate(int[] nums) {
        //我们可以把这个数组做成一个链表，这样如果存在重复数字的话，就一定会产生环形链表
        //这样按照环形链表的思路来做就可以啦
        //出发点一样就可以
        int fast=0;
        int slow=0;
        
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

## 8. 数组相邻差值的个数

667\. Beautiful Arrangement II (Medium)

[Leetcode](https://leetcode.com/problems/beautiful-arrangement-ii/description/) / [力扣](https://leetcode-cn.com/problems/beautiful-arrangement-ii/description/)

```html
Input: n = 3, k = 2
Output: [1, 3, 2]
Explanation: The [1, 3, 2] has three different positive integers ranging from 1 to 3, and the [2, 1] has exactly 2 distinct integers: 1 and 2.
```

题目描述：数组元素为 1\~n 的整数，要求构建数组，使得相邻元素的差值不相同的个数为 k。

让前 k+1 个元素构建出 k 个不相同的差值，序列为：1 k+1 2 k 3 k-1 ... k/2 k/2+1.

```java
public int[] constructArray(int n, int k) {
    int[] ret = new int[n];
    ret[0] = 1;
    for (int i = 1, interval = k; i <= k; i++, interval--) {
        ret[i] = i % 2 == 1 ? ret[i - 1] + interval : ret[i - 1] - interval;
    }
    for (int i = k + 1; i < n; i++) {
        ret[i] = i + 1;
    }
    return ret;
}
```

## 9. 数组的度

697\. Degree of an Array (Easy)

[Leetcode](https://leetcode.com/problems/degree-of-an-array/description/) / [力扣](https://leetcode-cn.com/problems/degree-of-an-array/description/)

```html
Input: [1,2,2,3,1,4,2]
Output: 6
```

题目描述：数组的度定义为元素出现的最高频率，例如上面的数组度为 3。要求找到一个最小的子数组，这个子数组的度和原数组一样。

### [思路](https://leetcode-cn.com/problems/degree-of-an-array/solution/xiang-xi-fen-xi-ti-yi-yu-si-lu-jian-ji-d-nvdy/)

```java
class Solution {
    public int findShortestSubArray(int[] nums) {
        //至少要找到最大频数代表的数之后（使用一个hash)，然后进行数据的最左边和最右边长度(使用哈希来保存最左边和最右边)
        //注意三个哈希表可以通过一个遍历来完成
        HashMap<Integer,Integer> map_count=new HashMap<>();
        HashMap<Integer,Integer> map_left=new HashMap<>();
        HashMap<Integer,Integer> map_right=new HashMap<>();
        int res=0;//存储长度
        for(int i=0;i<nums.length;i++){
            map_count.put(nums[i],map_count.getOrDefault(map_count.get(nums[i]),0)+1);//计数操作
            if(!map_left.containsKey(nums[i])) map_left.put(nums[i],i);//只是统计最左边的左边索引
            map_right.put(nums[i],i);//最右边的话就直接进行覆盖就可以啦

        }
        //最大的度
        int maxed=0;
       Object[] res1= map_count.values().toArray();
       Arrays.sort(res1);
       maxed=(int)res1[res1.length-1];
        //进行遍历
        for(int key:map_count.keySet()){
            if(map_count.get(key)==maxed){
                res=Math.max(res,map_right.get(key)-map_left.get(key)+1);
            }
        }
        return res;


    }
}
```



```java
public int findShortestSubArray(int[] nums) {
    Map<Integer, Integer> numsCnt = new HashMap<>();
    Map<Integer, Integer> numsLastIndex = new HashMap<>();
    Map<Integer, Integer> numsFirstIndex = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
        int num = nums[i];
        numsCnt.put(num, numsCnt.getOrDefault(num, 0) + 1);
        numsLastIndex.put(num, i);
        if (!numsFirstIndex.containsKey(num)) {
            numsFirstIndex.put(num, i);
        }
    }
    int maxCnt = 0;
    for (int num : nums) {
        maxCnt = Math.max(maxCnt, numsCnt.get(num));
    }
    int ret = nums.length;
    for (int i = 0; i < nums.length; i++) {
        int num = nums[i];
        int cnt = numsCnt.get(num);
        if (cnt != maxCnt) continue;
        ret = Math.min(ret, numsLastIndex.get(num) - numsFirstIndex.get(num) + 1);
    }
    return ret;
}












```

## 10. 对角元素相等的矩阵

766\. Toeplitz Matrix (Easy)

[Leetcode](https://leetcode.com/problems/toeplitz-matrix/description/) / [力扣](https://leetcode-cn.com/problems/toeplitz-matrix/description/)



### [easy 版 只比较每一个数据右下角](https://leetcode-cn.com/problems/toeplitz-matrix/solution/pan-duan-mei-ge-yuan-su-he-ta-de-you-xia-x3fi/)

```java
class Solution {
    public boolean isToeplitzMatrix(int[][] matrix) {
        //只要比较值和右下角是否相同
        for(int i=0;i<matrix.length-1;i++){
            for(int j=0;j<matrix[0].length-1;j++){
                if(matrix[i][j]!=matrix[i+1][j+1]) return false;
            }
        }
        return true;

    }
}
```



```html
1234
5123
9512

In the above grid, the diagonals are "[9]", "[5, 5]", "[1, 1, 1]", "[2, 2, 2]", "[3, 3]", "[4]", and in each diagonal all elements are the same, so the answer is True.
```

```java
public boolean isToeplitzMatrix(int[][] matrix) {
    for (int i = 0; i < matrix[0].length; i++) {
        if (!check(matrix, matrix[0][i], 0, i)) {
            return false;
        }
    }
    for (int i = 0; i < matrix.length; i++) {
        if (!check(matrix, matrix[i][0], i, 0)) {
            return false;
        }
    }
    return true;
}

private boolean check(int[][] matrix, int expectValue, int row, int col) {
    if (row >= matrix.length || col >= matrix[0].length) {
        return true;
    }
    if (matrix[row][col] != expectValue) {
        return false;
    }
    return check(matrix, expectValue, row + 1, col + 1);
}
```

## 11. 嵌套数组

565\. Array Nesting (Medium)

[Leetcode](https://leetcode.com/problems/array-nesting/description/) / [力扣](https://leetcode-cn.com/problems/array-nesting/description/)

```html
Input: A = [5,4,0,3,1,6,2]
Output: 4
Explanation:
A[0] = 5, A[1] = 4, A[2] = 0, A[3] = 3, A[4] = 1, A[5] = 6, A[6] = 2.

One of the longest S[K]:
S[0] = {A[0], A[5], A[6], A[2]} = {5, 6, 2, 0}
```

题目描述：S[i] 表示一个集合，集合的第一个元素是 A[i]，第二个元素是 A[A[i]]，如此嵌套下去。求最大的 S[i]。

```java
public int arrayNesting(int[] nums) {
    int max = 0;
    for (int i = 0; i < nums.length; i++) {
        int cnt = 0;
        for (int j = i; nums[j] != -1; ) {
            cnt++;
            int t = nums[j];
            nums[j] = -1; // 标记该位置已经被访问
            j = t;

        }
        max = Math.max(max, cnt);
    }
    return max;
}
```

## 12. 分隔数组

769\. Max Chunks To Make Sorted (Medium)

[Leetcode](https://leetcode.com/problems/max-chunks-to-make-sorted/description/) / [力扣](https://leetcode-cn.com/problems/max-chunks-to-make-sorted/description/)

```html
Input: arr = [1,0,2,3,4]
Output: 4
Explanation:
We can split into two chunks, such as [1, 0], [2, 3, 4].
However, splitting into [1, 0], [2], [3], [4] is the highest number of chunks possible.
```

题目描述：分隔数组，使得对每部分排序后数组就为有序。

```java
public int maxChunksToSorted(int[] arr) {
    if (arr == null) return 0;
    int ret = 0;
    int right = arr[0];
    for (int i = 0; i < arr.length; i++) {
        right = Math.max(right, arr[i]);
        if (right == i) ret++;
    }
    return ret;
}
```
