---

title: 最大最小堆
thumbnail: true
author: Kumi
date: 2020-10-28 10:20:51
icons: [fas fa-fire red, fas fa-star green]
cover: true
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN/24.jpg
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

### [比较不错的切入点](https://leetcode-cn.com/problems/top-k-frequent-elements/solution/leetcode-di-347-hao-wen-ti-qian-k-ge-gao-pin-yuan-/)

#### java构造大根堆小根堆的方法

- 默认就是小根堆，默认是进行找最大的k个数,（a,b）-> f(a)-f(b) 还是小根堆
- 重写比较器的话就是大根堆，重写必须是（a,b）-> f(b)-f(a)这种才是大根堆

```java
//大根堆
PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((a, b) -> {
    return b - a;
});
priorityQueue.add(1);
priorityQueue.add(2);
System.out.println(priorityQueue.peek());
```



#### [一个大根堆的题目，字符出现的次数排序](https://leetcode-cn.com/problems/sort-characters-by-frequency/solution/java-top-k-5-by-donoghl-2/)

```java
class Solution {
    public String frequencySort(String s) {
        Map<Character, Integer> map = new HashMap<>();
        for (char chr : s.toCharArray()) {
            map.put(chr, map.getOrDefault(chr, 0) + 1);
        }

        PriorityQueue<Map.Entry<Character, Integer>> maxHeap = new PriorityQueue<>(
            (e1, e2) -> e2.getValue() - e1.getValue());//大根堆变成

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



#### python桶排序

```python
class Solution:
    def frequencySort(self, s: str) -> str:
        # 桶排序
        ret = []
        countFrequency = collections.defaultdict(int)
        for i in s:
            countFrequency[i] += 1
        buckets = [[] for _ in range(len(s) + 1)]
        for i in countFrequency:
            buckets[countFrequency[i]].extend(i*countFrequency[i])
        for i in buckets[::-1]:
            if(i):
                ret.extend(i)
        return ''.join(ret)
```

#### 自己版本

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
        queue=[[] for i in range(len(s)+1)]
        for key in dic:
            queue[dic[key]].extend(dic[key]*key)
        queue=queue[::-1]
        for i in range(len(queue)):
            if len(queue[i])>0:
                res.extend(queue[i])
        return "".join(res)
    
```

![image-20210107111208095](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20210107111208095.png)

以上是这个extend和append的不同，一个是按照元素一个一个弄进去，另外一个是直接进行append整个数组



### 最小K个数据

```java
class Solution {
    public int[] smallestK(int[] arr, int k) {
        if(k==0){
            return new int[0];
        }
        PriorityQueue<Integer> dic=new PriorityQueue<>((e1,e2)->e2-e1);
        for(int num:arr){
            if(dic.size()<k){
                dic.offer(num);
            }
            else{
                if(dic.peek()>num){
                dic.offer(num);
                dic.poll();
            }
            }
        }
        int[] res=new int[k];
        int inx=0;
        for(Integer num:dic){
            res[inx++]=num;

        }
        return res;
        
        

    }
}
```



### [973. 最接近原点的 K 个点](https://leetcode-cn.com/problems/k-closest-points-to-origin/)

#### 快速选择法

```java
class Solution {
    public int[][] kClosest(int[][] points, int K) {     
        // 使用快速选择（快排变形） 获取 points 数组中距离最小的 K 个点（第 4 个参数是下标，因此是 K - 1）
        return quickSelect(points, 0, points.length - 1, K - 1);
    }

    private int[][] quickSelect(int[][] points, int lo, int hi, int idx) {
        if (lo > hi) {
            return new int[0][0];
        }
        // 快排切分后，j 左边的点距离都小于等于 points[j], j 右边的点的距离都大于等于 points[j]。
        int j = partition(points, lo, hi); 
        // 如果 j 正好等于目标idx，说明当前points数组中的[0, idx]就是距离最小的 K 个元素
        if (j == idx) {
            return Arrays.copyOf(points, idx + 1);
        }
        // 否则根据 j 与 idx 的大小关系判断找左段还是右段，最后总会跳出来的，就是j==idx好不好，这些递归的目标就是排好序of inx 之前
        return j < idx? quickSelect(points, j + 1, hi, idx): quickSelect(points, lo, j - 1, idx);
    }

    private int partition(int[][] points, int lo, int hi) {
        int[] v = points[lo];
        int dist = v[0] * v[0] + v[1] * v[1];
        int i = lo, j = hi + 1;
        while (true) {
            while (++i <= hi && points[i][0] * points[i][0] + points[i][1] * points[i][1] < dist);
            while (--j >= lo && points[j][0] * points[j][0] + points[j][1] * points[j][1] > dist);
            if (i >= j) {
                break;
            }
            int[] tmp = points[i];
            points[i] = points[j];
            points[j] = tmp;
        }
        points[lo] = points[j];
        points[j] = v;
        return j;
    }
}

```



#### 最大堆方法

```java
class Solution {
    public int[][] kClosest(int[][] points, int K) {
        // 默认是小根堆，实现大根堆需要重写一下比较器。
        PriorityQueue<int[]> pq = new PriorityQueue<>((p1, p2) -> p2[0] * p2[0] + p2[1] * p2[1] - p1[0] * p1[0] - p1[1] * p1[1]);      
        for (int[] point: points) {
            if (pq.size() < K) { // 如果堆中不足 K 个，直接将当前 point 加入即可
                pq.offer(point);
            } else if (pq.comparator().compare(point, pq.peek()) > 0) { // 否则，判断当前点的距离是否小于堆中的最大距离，若是，则将堆中最大距离poll出，将当前点加入堆中。
                pq.poll();
                pq.offer(point);
            }
        }

        // 返回堆中的元素
        int[][] res = new int[pq.size()][2];
        int idx = 0;
        for (int[] point: pq) {
            res[idx++] = point;
        }
        return res;
    }
}


```

#### 这里lambda表达式也可以使用函数

```java
class Solution {
    public int[][] kClosest(int[][] points, int K) {
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a,b) -> (cal(b) - cal(a)));
        int[][] result = new int[K][2];
        for(int i = 0; i < K; i++){
            maxHeap.offer(points[i]);
        }
        for(int i = K; i < points.length; i++){
            if(cal(maxHeap.peek()) > cal(points[i])){
                maxHeap.poll();
                maxHeap.offer(points[i]);
            }
        }
        for(int i = 0; i < K; i++){
            result[i] = maxHeap.poll();
        }
        return result;
    }

    public int cal(int a[]){
        return (a[0] * a[0]) + (a[1] * a[1]);
    }
}

//还有就是需要明确这个二维数组的代码

```

