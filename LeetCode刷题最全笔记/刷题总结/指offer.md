---

title: offer刷题
thumbnail: true
author: Kumi
date: 2020-03-14 22:20:51
icons: [fas fa-fire red, fas fa-star green]
cover: true
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN/17.jpg
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

## 连续整数数列问题

![image.png](https://pic.leetcode-cn.com/b7bbf8306beaf1f05af3f46d33846a9f54543d74894ddcf81bf3e1e712dbabce-image.png)

![image.png](https://pic.leetcode-cn.com/652fac6fe71a55076fad3550487de0574616521e0e7ea93d96e0694f0afda358-image.png)

![image-20200820173512357](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200820173512357.png)

```java
class Solution:
    def findContinuousSequence(self, target: int) -> List[List[int]]:
        # 初始化窗口指针和输出列表
        i, j, res = 1,2, []

        # 滑动窗口的右边界不能超过target的中值
        while j <= target//2 + 1:
            # 计算当前窗口内数字之和
            cur_sum = sum(list(range(i,j+1)))
            # 若和小于目标，右指针向右移动，扩大窗口
            if cur_sum < target:
                j += 1
            # 若和大于目标，左指针向右移动，减小窗口
            elif cur_sum > target:
                i += 1
            # 相等就把指针形成的窗口添加进输出列表中
            # 别忘了，这里还要继续扩大寻找下一个可能的窗口哦
            else:
                res.append(list(range(i,j+1)))
                # 这里用j+=1，i+=1，i+=2都可以的
                j += 1
        
        return res


```

![image-20200820173553074](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200820173553074.png)

![image-20200820173616820](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200820173616820.png)

![image-20200820173837983](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200820173837983.png)

```python 
class Solution:
    def findContinuousSequence(self, target: int):
        # 创建输出列表
        res = []

        # y不能超过target的中值,即y<=target//2 + 1,range函数左开右闭,所以这里是+2
        for y in range(1,target//2 + 2):
            # 应用我们的求根公式
            x = (1/4 + y**2 + y - 2 * target) ** (1/2) + 0.5
            # 我们要确保x不能是复数，且x必须是整数
            if type(x) != complex and x - int(x) == 0:
                res.append(list(range(int(x),y+1)))
        
        return res

```

![image-20200820173913583](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200820173913583.png)

![image-20200820173933993](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/image-20200820173933993.png)

```python
class Solution:
    def findContinuousSequence(self, target: int) -> List[List[int]]:
        # 我们的间隔从1开始
        i, res = 1, []
        
        # 根据上面的条件1，限定i的大小，即间隔的范围
        while i*(i+1)/2 < target:
            # 根据条件2，如果x不为整数则扩大间隔
            if not (target - i*(i+1)/2) % (i+1):
                # 如果两个条件都满足，代入公式求出x即可，地板除//会把数改成float形式，用int()改回来
                x = int((target - i*(i+1)/2) // (i+1))
                # 反推出y，将列表填入输出列表即可
                res.append(list(range(x,x+i+1)))
            # 当前间隔判断完毕，检查下一个间隔
            i += 1

        # 由于间隔是从小到大，意味着[x,y]列表是从大到小的顺序放入输出列表res的，所以反转之
        return res[::-1]


```

