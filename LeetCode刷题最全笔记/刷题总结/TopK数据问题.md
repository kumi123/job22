---

title: top-k问题
thumbnail: true
author: Kumi
date: 2021-01-22 22:10:51
icons: [fas fa-fire red, fas fa-star green]
cover: true
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN/22jpg
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

## TopK问题

```java
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        // 使用字典，统计每个元素出现的次数，元素为键，元素出现的次数为值
        HashMap<Integer,Integer> map = new HashMap();
        for(int num : nums){
            if (map.containsKey(num)) {
               map.put(num, map.get(num) + 1);
             } else {
                map.put(num, 1);
             }
        }
        // 遍历map，用最小堆保存频率最大的k个元素
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return map.get(a) - map.get(b);
            }
        });
        for (Integer key : map.keySet()) {
            if (pq.size() < k) {
                pq.add(key);
            } else if (map.get(key) > map.get(pq.peek())) {
                pq.remove();
                pq.add(key);
            }
        }
        // 取出最小堆中的元素
        int res[]=new int[k];
        int i=0;
        while (!pq.isEmpty()) {
            res[i++]=pq.remove();
        }
        for(int j=0;j<res.length/2;j++){
            int temp=res[j];
            res[j]=res[res.length-1-j];
            res[res.length-1-j]=temp;
        }
        return res;
    }
}
```







```python
class Solution:
    def topKFrequent(self, nums: List[int], k: int) -> List[int]:
        dic={}
        for num in nums:
            if num not in dic:
                dic[num]=1
            else:dic[num]+=1
        res=[[0] for i in range(len(nums))]
        for num in dic:
            res[dic[num]].append(num)
        res1=[]
        for i in range(len(res)-1,-1,-1):
            if len(res1)==k:break
            if len(res[i])==0:continue
            res1+=res[i]
        return res1

```

