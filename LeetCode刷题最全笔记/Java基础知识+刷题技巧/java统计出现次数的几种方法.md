---
title: Java统计出现次数
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2021-01-06 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/45.jpg
tags:
  - Java
  - Java基础
categories:
  - 语言基础
music:
 server: netease   # netease, tencent, kugou, xiami, baidu
 type: song        # song, playlist, album, search, artist
 id: 16846091      # song id / playlist id / album id / search keyword


---
### 注意integer是一个对象，所以只有null和notnull而不是0和非0

#### 方法一  java8 stream

```java
Map<Integer, Integer> counter = IntStream.of(nums).boxed().collect(Collectors.toMap(e -> e, e -> 1, Integer::sum));
//其中nums是一个整数数组


```



#### 方法二 getdefalut

```java
 map.put(chr, map.getOrDefault(chr, 0) + 1);//第一种

map.put(chr, map.get(chr)==null?1: map.get(chr)+ 1);//方法二
```



#### 方法三 普通方法

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
 
public class Dem06 {
	/**
	 * 给定一个字符串数组，数组中内容中有重复,先打印各数组中字符出现的字数
	 * 使用hashMap来设计,hashMap的键存放字符串,值统计出现的次数
	 * @param args
	 */
	public static void main(String[] args) {
		String []strs = {"张三","李四","王五","张三","李四","张三"};
		AccountUtil.printMap(AccountUtil.account(strs));
	}
 
}
 
class AccountUtil{
	/**
	 * 统计出现次数
	 * @param strs
	 * @return
	 */
	public static Map<String, Integer>account(String []strs){
		Map<String,Integer> map = new HashMap<>();
		for(int i=0;i<strs.length;i++) {
			//方法一
//			String str = strs[i];//首先取出第一个数
//			if(map.get(str) == null) {//如果取出的数之前没有取到则设置次数为1
//				map.put(str, 1);
//			}else {
//				map.put(str, map.get(str)+1);//否则，第二次取到这个字符在前面的基础上加1
//			}
			//方法二
			if(map.containsKey(strs[i])) {
				map.put(strs[i], map.get(strs[i])+1);
			}else {
				map.put(strs[i], 1);
			}
	}
		return map;
}
	/**
	 * 打印
	 * @param map
	 */
	public static void printMap(Map<String, Integer> map) {
		 Set<Entry<String, Integer>> entrys = map.entrySet();
		 for(Entry<String, Integer>entry:entrys) {
			 System.out.println(entry.getKey()+"出现的次数"+entry.getValue());
		 }
	}
	
	
}
```



### 面试题40 (最小的k个数)

```java
class Solution {
    public int[] getLeastNumbers(int[] arr, int k) {
        //使用大根堆好不好
        if (k == 0) {
        return new int[0];
         }
        PriorityQueue<Integer> queue=new PriorityQueue<>((e1,e2)->e2-e1);
        for(int num:arr){
            if(queue.size()<k){
                queue.offer(num);
            }
            else{
                if(queue.peek()>num){
                    queue.offer(num);
                    queue.poll();
                }
            }
        }
        int[] res=new int[k];
        //index=0;
        for(int i=0;i<k;i++){
            res[i]=queue.poll();
        }
        return res;
        




    }
}
```







```c++
vector<int> reverseArrayInPiece(vector<bool> &vb){
	vector<int> vi; //vi[0]开始下标vi[1]结束下标
	vector<int> p;
	int f, r;
 
	//创建新的数组1换为-1，0换为1
	for(size_t i = 0; i < vb.size(); i++){
		if(vb[i]){
			p.push_back(-1);
		}else{
			p.push_back(1);
		}
	}
	int sum = p[0], all = p[0];
	f = r = 0; //开始下标与结束下标
 
	for(int i = 1; i < vb.size(); i++){
		//如果当前元素本身，比包含当前元素的子数组之和还要大，舍弃以前的，开始下标为当前元素
		if(p[i] > p[i] + sum){ 
			f = i;
		}
		sum = max(p[i], p[i] + sum);
		//如果当前的和比前的大那更新子数组的结束下标
		if(all < sum){
			r = i;
		}
		all = max(all, sum);
	}
 
	vi.push_back(f); vi.push_back(r);
	return vi;
}


```

