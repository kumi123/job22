---

title: 排序算法
thumbnail: true
author: Kumi
date: 2021-11-28 22:20:51
icons: [fas fa-fire red, fas fa-star green]
cover: true
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN/20.jpg
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
# Leetcode 题解 - 排序
<!-- GFM-TOC -->
* [Leetcode 题解 - 排序](#leetcode-题解---排序)
    * [快速选择](#快速选择)
    * [堆](#堆)
        * [1. Kth Element](#1-kth-element)
    * [桶排序](#桶排序)
        * [1. 出现频率最多的 k 个元素](#1-出现频率最多的-k-个元素)
        * [2. 按照字符出现次数对字符串排序](#2-按照字符出现次数对字符串排序)
    * [荷兰国旗问题](#荷兰国旗问题)
        * [1. 按颜色进行排序](#1-按颜色进行排序)
<!-- GFM-TOC -->


## 快速选择

用于求解   **Kth Element**   问题，也就是第 K 个元素的问题。

可以使用快速排序的 partition() 进行实现。需要先打乱数组，否则最坏情况下时间复杂度为 O(N<sup>2</sup>)。

## 堆

用于求解   **TopK Elements**   问题，也就是 K 个最小元素的问题。使用最小堆来实现 TopK 问题，最小堆使用大顶堆来实现，大顶堆的堆顶元素为当前堆的最大元素。实现过程：不断地往大顶堆中插入新元素，当堆中元素的数量大于 k 时，移除堆顶元素，也就是当前堆中最大的元素，剩下的元素都为当前添加过的元素中最小的 K 个元素。插入和移除堆顶元素的时间复杂度都为 log<sub>2</sub>N。

堆也可以用于求解 Kth Element 问题，得到了大小为 K 的最小堆之后，因为使用了大顶堆来实现，因此堆顶元素就是第 K 大的元素。

快速选择也可以求解 TopK Elements 问题，因为找到 Kth Element 之后，再遍历一次数组，所有小于等于 Kth Element 的元素都是 TopK Elements。

可以看到，快速选择和堆排序都可以求解 Kth Element 和 TopK Elements 问题。

### 1. Kth Element

215\. Kth Largest Element in an Array (Medium)

[Leetcode](https://leetcode.com/problems/kth-largest-element-in-an-array/description/) / [力扣](https://leetcode-cn.com/problems/kth-largest-element-in-an-array/description/)

```text
Input: [3,2,1,5,6,4] and k = 2
Output: 5
```

题目描述：找到倒数第 k 个的元素。

**排序**  ：时间复杂度 O(NlogN)，空间复杂度 O(1)

```java
public int findKthLargest(int[] nums, int k) {
    Arrays.sort(nums);
    return nums[nums.length - k];
}
```

**堆**  ：时间复杂度 O(NlogK)，空间复杂度 O(K)。

```java
public int findKthLargest(int[] nums, int k) {
    PriorityQueue<Integer> pq = new PriorityQueue<>(); // 小顶堆
    for (int val : nums) {
        pq.add(val);
        if (pq.size() > k)  // 维护堆的大小为 K
            pq.poll();
    }
    return pq.peek();
}
```

**快速选择**  ：时间复杂度 O(N)，空间复杂度 O(1)

```java
public int findKthLargest(int[] nums, int k) {
    k = nums.length - k;
    int l = 0, h = nums.length - 1;
    while (l < h) {
        int j = partition(nums, l, h);
        if (j == k) {
            break;
        } else if (j < k) {
            l = j + 1;
        } else {
            h = j - 1;
        }
    }
    return nums[k];
}

private int partition(int[] a, int l, int h) {
    int i = l, j = h + 1;
    while (true) {
        while (a[++i] < a[l] && i < h) ;
        while (a[--j] > a[l] && j > l) ;
        if (i >= j) {
            break;
        }
        swap(a, i, j);
    }
    swap(a, l, j);
    return j;
}

private void swap(int[] a, int i, int j) {
    int t = a[i];
    a[i] = a[j];
    a[j] = t;
}
```

## 桶排序

### 1. 出现频率最多的 k 个元素

347\. Top K Frequent Elements (Medium)

[Leetcode](https://leetcode.com/problems/top-k-frequent-elements/description/) / [力扣](https://leetcode-cn.com/problems/top-k-frequent-elements/description/)

```html
Given [1,1,1,2,2,3] and k = 2, return [1,2].
```

设置若干个桶，每个桶存储出现频率相同的数。桶的下标表示数出现的频率，即第 i 个桶中存储的数出现的频率为 i。

把数都放到桶之后，从后向前遍历桶，最先得到的 k 个数就是出现频率最多的的 k 个数。





#### 自己的写法 [参考](https://leetcode-cn.com/problems/top-k-frequent-elements/solution/4-chong-fang-fa-miao-sha-topkji-shu-pai-xu-kuai-pa/)



```java
//基于桶排序求解「前 K 个高频元素」
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        HashMap<Integer,Integer> res = new HashMap();
        for(int num : nums){
            if (res.containsKey(num)) {
               res.put(num, res.get(num) + 1);
             } else {
                res.put(num, 1);
             }
        }

        PriorityQueue<Integer> pri=new PriorityQueue<>((a,b)->res.get(a)-res.get(b));
        for(Integer key:res.keySet()){
            if(pri.size()<k){
                pri.offer(key);
            }
            else{
                if(res.get(key)>res.get(pri.peek())){
                    pri.poll();
                    pri.offer(key);
                }
            }
        }
        int[] resd=new int[k];
        int index=k-1;
        for(int num:pri){
            resd[index--]=num; 

        }
        return resd;

        
    }
}


```

#### 作者的写法

```java
//基本自己写法


//基于桶排序求解「前 K 个高频元素」
class Solution{
public int[] topKFrequent(int[] nums, int k) {
    //首先使用一个数据统计出现的次数
    HashMap<Integer,Integer> dic = new HashMap<>();
    for(int num:nums){
        dic.put(num,dic.getOrDefault(num,0)+1);
    }
    //然后使用一个list数组来进行次数的排序统计问题
    List<Integer>[] bucket =new ArrayList[nums.length+1];//设置一个水桶，表征数据出现的次数,下标是出现次数，元素是当前出现次数对应的元素
    for(int num:dic.keySet()){
        int inx=dic.get(num);
        if(bucket[inx]==null) bucket[inx]=new ArrayList<>();
        else bucket[inx].add(num);
    }
    List<Integer> topk=new ArrayList<>();
    for(int i=bucket.length-1;i>=0&&topk.size()<k;i--){
        if(bucket[i]==null){
            continue;
        }
        if(bucket[i].size()<=k-topk.size()){
            topk.addAll(bucket[i]);
        }
        else if(bucket[i].size()>topk.size()){
            topk.addAll(bucket[i].subList(0,k-topk.size()));

        }
    }
    int res[]=new int[k];
    for(int i=0;i<k;i++){
        res[i]=topk.get(i);
    }
    return res;
    //然后按照倒叙进行弄进去就可以，比较数据个数和k的大小问题

}
}







public int[] topKFrequent(int[] nums, int k) {
    Map<Integer, Integer> frequencyForNum = new HashMap<>();
    for (int num : nums) {
        frequencyForNum.put(num, frequencyForNum.getOrDefault(num, 0) + 1);
    }
    List<Integer>[] buckets = new ArrayList[nums.length + 1];
    for (int key : frequencyForNum.keySet()) {
        int frequency = frequencyForNum.get(key);
        if (buckets[frequency] == null) {
            buckets[frequency] = new ArrayList<>();
        }
        buckets[frequency].add(key);
    }
    List<Integer> topK = new ArrayList<>();
    for (int i = buckets.length - 1; i >= 0 && topK.size() < k; i--) {
        if (buckets[i] == null) {
            continue;
        }
        if (buckets[i].size() <= (k - topK.size())) {
            topK.addAll(buckets[i]);
        } else {
            topK.addAll(buckets[i].subList(0, k - topK.size()));
        }
    }
    int[] res = new int[k];
    for (int i = 0; i < k; i++) {
        res[i] = topK.get(i);
    }
    return res;
}
```

### 2. 按照字符出现次数对字符串排序

451\. Sort Characters By Frequency (Medium)

[Leetcode](https://leetcode.com/problems/sort-characters-by-frequency/description/) / [力扣](https://leetcode-cn.com/problems/sort-characters-by-frequency/description/)

#### 桶排序

```java
class Solution {
    public String frequencySort(String s) {
//使用桶排序来做
    //首先hashmap统计个数出现的
    HashMap<Character,Integer> dic=new HashMap<>();
    for(Character ss:s.toCharArray()){
        dic.put(ss,dic.getOrDefault(ss,0)+1);
    }
    //然后建立桶排序的数组
    List<Character>[] bucket = new ArrayList[s.length()+1];
    for(Character ss:dic.keySet()){
        int frency=dic.get(ss);
        if(bucket[frency]==null) bucket[frency]=new ArrayList<>();
        else bucket[frency].add(ss);
    }
    //取出来
    StringBuilder res=new StringBuilder();
    for(int i=bucket.length-1;i>0;i--){
        if(bucket[i]==null) continue;
        for (Character pp:bucket[i]){
            for(int j=0;j<i;j++){
                res.append(pp);
            }
        }
        }
    }
    return res.toString();

       
    }
}
```

```python
class Solution:
    def frequencySort(self, s: str) -> str:
        #保存结果
        res=[]
        #首先统计出现次数
        dic=collections.defaultdict(int)
        for ss in s:
            dic[ss]+=1
        #建立一个以下标为出现频率的数组
        queue=[[] for i in range(len(s)+1)]#下标就是这个代表出现的次数
        for key in dic:
            queue[dic[key]].extend(dic[key]*key)
        queue=queue[::-1]
        for i in range(len(queue)):
            if len(queue[i])>0:
                res.extend(queue[i])
        return "".join(res)
```



```html
Input:
"tree"

Output:
"eert"

Explanation:
'e' appears twice while 'r' and 't' both appear once.
So 'e' must appear before both 'r' and 't'. Therefore "eetr" is also a valid answer.
```

```java
public String frequencySort(String s) {
    Map<Character, Integer> frequencyForNum = new HashMap<>();
    for (char c : s.toCharArray())
        frequencyForNum.put(c, frequencyForNum.getOrDefault(c, 0) + 1);

    List<Character>[] frequencyBucket = new ArrayList[s.length() + 1];
    for (char c : frequencyForNum.keySet()) {
        int f = frequencyForNum.get(c);
        if (frequencyBucket[f] == null) {
            frequencyBucket[f] = new ArrayList<>();
        }
        frequencyBucket[f].add(c);
    }
    StringBuilder str = new StringBuilder();
    for (int i = frequencyBucket.length - 1; i >= 0; i--) {
        if (frequencyBucket[i] == null) {
            continue;
        }
        for (char c : frequencyBucket[i]) {
            for (int j = 0; j < i; j++) {
                str.append(c);
            }
        }
    }
    return str.toString();
}
```
#### 最大堆最小堆




## 荷兰国旗问题

荷兰国旗包含三种颜色：红、白、蓝。

有三种颜色的球，算法的目标是将这三种球按颜色顺序正确地排列。它其实是三向切分快速排序的一种变种，在三向切分快速排序中，每次切分都将数组分成三个区间：小于切分元素、等于切分元素、大于切分元素，而该算法是将数组分成三个区间：等于红色、等于白色、等于蓝色。

<div align="center"> <img src="https://cs-notes-1256109796.cos.ap-guangzhou.myqcloud.com/7a3215ec-6fb7-4935-8b0d-cb408208f7cb.png"/> </div><br>


### 1. 按颜色进行排序

75\. Sort Colors (Medium)

[Leetcode](https://leetcode.com/problems/sort-colors/description/) / [力扣](https://leetcode-cn.com/problems/sort-colors/description/)



```html
Input: [2,0,2,1,1,0]
Output: [0,0,1,1,2,2]
```

题目描述：只有 0/1/2 三种颜色。

#### 弱智最大堆

```java
class Solution {
    public void sortColors(int[] nums) {
        //使用堆排序实时，待会可以用桶排序实时
        PriorityQueue<Integer> queue=new PriorityQueue<>();
        for(int num:nums){
            queue.offer(num);
        }
        for(int i=0;i<nums.length;i++){
            nums[i]=queue.poll();
        }

    }
}
```



#### [双指针交换](https://leetcode-cn.com/problems/sort-colors/solution/python-guan-fang-da-an-jie-xi-by-hardcandy/)

##### 灵魂是

```python 
class Solution:
    def sortColors(self, nums: List[int]) -> None:
        """
        Do not return anything, modify nums in-place instead.
        """
        p0, curr, p2 = 0, 0, len(nums) - 1
        while curr <= p2:#因为p2是2的位置的边界，所以只要不超过它就可以
            if nums[curr] == 0: # 为0，做出决策
                nums[p0], nums[curr] = nums[curr], nums[p0]
                p0 += 1
                curr += 1
            elif nums[curr] == 2:   # 为2，做出决策 因为这个指针的遍历顺序是从左右的，所以在与左边交换之后，是不需要进行对交换之后的数据进行判断的因为前边的数据已经判断过了，而对右边交换之后的数据是需要判断的，比如说这个2交换过来了变成0，如果i++的话，0的位置就不对了，所以需要再次判断一下当前值的情况。所以指针不移动;
                nums[p2], nums[curr] = nums[curr], nums[p2]
                p2 -= 1
            else:                   # 为1，做出决策
                curr += 1


```

```java
class Solution {
    public void sortColors(int[] nums) {
        int p0=0,p2=nums.length-1,cur=0;
        while(cur<=p2){
            if(nums[cur]==0){
                swap(nums,p0,cur);
                cur++;
                p0++;
            }
            else if(nums[cur]==2){
                swap(nums,p2,cur);
                p2--;
            }
            else cur++;
        }
        


    }
    public void swap(int[]nums,int i,int j){
        int tem=nums[i];
        nums[i]=nums[j];
        nums[j]=tem;
    }
}
```



#### 牛逼的方法

```java
public void sortColors(int[] nums) {
    int zero = -1, one = 0, two = nums.length;
    while (one < two) {
        if (nums[one] == 0) {
            swap(nums, ++zero, one++);
        } else if (nums[one] == 2) {
            swap(nums, --two, one);
        } else {
            ++one;
        }
    }
}

private void swap(int[] nums, int i, int j) {
    int t = nums[i];
    nums[i] = nums[j];
    nums[j] = t;
}
```

### [按照字符串出现的次序来进行排序](https://leetcode-cn.com/problems/sort-characters-by-frequency/)

![image-20210106202723558](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210106202723558.png)

```java
class Solution {
    public String frequencySort(String s) {
        Map<Character, Integer> map = new HashMap<>();
        for (char chr : s.toCharArray()) {
            map.put(chr, map.getOrDefault(chr, 0) + 1);
        }

        PriorityQueue<Map.Entry<Character, Integer>> maxHeap = new PriorityQueue<>(
            (e1, e2) -> e2.getValue() - e1.getValue());

        maxHeap.addAll(map.entrySet());

        StringBuilder sortedString = new StringBuilder(s.length());
        while (!maxHeap.isEmpty()) {
            Map.Entry<Character, Integer> entry = maxHeap.poll();
            for (int i = 0; i < entry.getValue(); i++){
                sortedString.append(entry.getKey());
            }
        }
        return sortedString.toString();
    }
}
```

#### 自己的写法

```java
class Solution {
    public String frequencySort(String s) {
        //思路就是建立一个大根堆来排序数据，然后利用一个hashmap来存储数据，最后来进行字符串的拼接就可以
        HashMap<Character,Integer> dic = new HashMap<>();
        for(Character ss:s.toCharArray()){
            if(dic.get(ss)!=null){//因为integer是一个对象啊，是包装类
                dic.put(ss,dic.get(ss)+1);
            }
            else dic.put(ss,1);
        }
        PriorityQueue<Character> queue =new PriorityQueue<>((e1,e2)->dic.get(e2)-dic.get(e1));
        StringBuilder res=new StringBuilder();
        queue.addAll(dic.keySet());
        while(queue.size()>0){
            Character tem=queue.poll();
            for(int i=0;i<dic.get(tem);i++){
                res.append(tem);
            }


        }
        return res.toString();
       
    }
}
```

