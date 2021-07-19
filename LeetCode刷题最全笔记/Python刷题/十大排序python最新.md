
---
title: Python排序算法
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2020-03-10 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/60.jpg
tags:
  - Python
categories:
  - Python刷题
music:
 server: netease   # netease, tencent, kugou, xiami, baidu
 type: song        # song, playlist, album, search, artist
 id: 16846091      # song id / playlist id / album id / search keyword

---

[python版这个比较合理简单](https://leetcode-cn.com/problems/sort-an-array/solution/python-shi-xian-de-shi-da-jing-dian-pai-xu-suan-fa/)



[Java版本](https://blog.csdn.net/MobiusStrip/article/details/83785159?depth_1-utm_source=distribute.pc_relevant.none-task&utm_source=distribute.pc_relevant.none-task)



[比较严谨的版本](https://leetcode-cn.com/problems/sort-an-array/solution/fu-xi-ji-chu-pai-xu-suan-fa-java-by-liweiwei1419/)



## 冒泡排序

**基本原理**

比较类排序算法。算法描述如下(假设是升序排序)：

- 比较相邻的元素，如果第一个元素比第二个大，就交换它们；
- 对每一对相邻元素做同样的工作，从开始第一对到结尾的最后一对，这样在最后的元素应该会是最大的数；
- 针对所有的元素重复以上的步骤，除了最后已经选出的有序元素；
- 持续对剩下的无序元素重复上面的步骤，直到排序完成。

**算法时间复杂度**
 ![[公式]](https://www.zhihu.com/equation?tex=O%28n%5E2%29) 

**算法实现**

```text
'''冒泡排序'''
def BubbleSort(array):
    length = len(array)
    for i in range(length):
        for j in range(length-i-1):
            if array[j] > array[j+1]: array[j+1], array[j] = array[j], array[j+1] 
    return array
```

**测试效果**

![img](https://pic4.zhimg.com/v2-9ea9fa477047900ae7a206b717bbf67b_r.jpg)

## 选择排序

**基本原理**

比较类排序算法。算法描述如下(假设是升序排序)：

- 首先在未排序序列中找到最小元素，存放到排序序列的起始位置；
- 再从剩余未排序元素中继续寻找最小元素，然后放到已排序序列的末尾；
- 重复第二步，直到所有元素均排序完毕。

**算法时间复杂度**

![[公式]](https://www.zhihu.com/equation?tex=O%28n%5E2%29)

**算法实现**

```text
'''选择排序'''
def SelectionSort(array):
    length = len(array)
    for i in range(length-1):
        idx_min = i
        for j in range(i+1, length):
            if array[j] < array[idx_min]:
                idx_min = j
        array[i], array[idx_min] = array[idx_min], array[i]
    return array
```

**测试效果**

![img](https://pic3.zhimg.com/v2-67fbf0c59614dd435ed99e70d4015c6a_r.jpg)

## 插入排序

**基本原理**

比较类排序算法。算法描述如下(假设是升序排序)：

- 从第一个元素开始，该元素可以认为已经被排序；
- 取出下一个元素，在已经排序的元素序列中从后向前扫描；
- 如果该元素(已排序)大于新元素，将该元素移到下一位置；
- 重复第三步，直到找到已排序的元素小于或等于新元素的位置；
- 将新元素插入到该位置；
- 重复第二到第五步，直到排序完成。

**算法时间复杂度**

![[公式]](https://www.zhihu.com/equation?tex=O%28n%5E2%29) 

**算法实现**

```text
'''插入排序'''
def InsertionSort(array):
    length = len(array)
    for i in range(1, length):
        pointer, cur = i - 1, array[i]
        while pointer >= 0 and array[pointer] > cur:
            array[pointer+1] = array[pointer]
            pointer -= 1
        array[pointer+1] = cur
    return array
```

**测试效果**

![img](https://pic4.zhimg.com/v2-fc6709e984d05d5590d87a4a88a6d333_r.jpg)

## 希尔排序

**基本原理**

比较类排序算法。其基本思想是把数据按下标的一定增量分组，对每组使用直接插入排序算法排序，随着增量逐渐减少，每组包含的数越来越多，当增量减至1时，整个文件恰被分成一组，算法终止。算法描述如下(假设是升序排序)：

- 选择一个增量序列  ![[公式]](https://www.zhihu.com/equation?tex=t_1%2C+t_2%2C...%2Ct_k) ，  ![[公式]](https://www.zhihu.com/equation?tex=t_k%3D1) ；
- 按增量序列个数k，对序列进行k次排序；
- 每次排序，根据对应的增量 ![[公式]](https://www.zhihu.com/equation?tex=t_i) ，将待排序列分割成若干长度为m的子序列，分别对各子序列进行直接插入排序。

**算法时间复杂度**

![[公式]](https://www.zhihu.com/equation?tex=O%28n%5E%7B1.3%7D%29) 

**算法实现**

```text
'''希尔排序'''
def ShellSort(array):
    length = len(array)
    gap = length // 2
    while gap > 0:
        for i in range(gap, length):
            j, cur = i, array[i]
            while (j - gap >= 0) and (cur < array[j - gap]):
                array[j] = array[j - gap]
                j = j - gap
            array[j] = cur
        gap = gap // 2
    return array
```

**测试效果**

![img](https://pic3.zhimg.com/v2-5f9fb8934bf225377cbdbeba4722f54e_r.jpg)

## 归并排序

**基本原理**

比较类排序算法。该算法采用了分治法的思想，将已有序的子序列合并，得到完全有序的序列。算法描述如下(假设是升序排序)：

- 把长度为n的输入序列分为两个长度为 ![[公式]](https://www.zhihu.com/equation?tex=%5Cfrac%7Bn%7D%7B2%7D) 的子序列；
- 对这两个子序列分别采用归并排序；
- 将两个排序好的子序列合并成一个最终的排序序列。

**算法时间复杂度**

![[公式]](https://www.zhihu.com/equation?tex=O%28nlog_2n%29) 

**算法实现**

```text
'''数组合并'''
def Merge(array_1, array_2):
    result = []
    while array_1 and array_2:
        if array_1[0] < array_2[0]:
            result.append(array_1.pop(0))
        else:
            result.append(array_2.pop(0))
    if array_1:
        result += array_1
    if array_2:
        result += array_2
    return result

'''归并排序'''
def MergeSort(array):
    if len(array) < 2: return array
    pointer = len(array) // 2
    left = array[:pointer]
    right = array[pointer:]
    return Merge(MergeSort(left), MergeSort(right))
```

**测试效果**

![img](https://pic1.zhimg.com/v2-3934d4a7b52e44a93444ce3f5c711f70_r.jpg)

## 快速排序

**基本原理**

比较类排序算法。基本思想是通过一次排序将待排序数据分隔成独立的两部分，其中一部分数据均比另一部分的数据小。然后分别对这两部分数据继续进行排序，直到整个序列有序。算法描述如下(假设是升序排序)：

- 从数列中挑出一个元素，称为“基准”；
- 重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆放在基准的后面(相同的数可以到任一边)；
- 分别对步骤二中的两个子序列再使用快速排序；
- 重复上述步骤，直到排序完成。

**算法时间复杂度**

![[公式]](https://www.zhihu.com/equation?tex=O%28nlog_2n%29)

**算法实现**

```text
'''快速排序'''
def QuickSort(array, left, right):
    if left >= right:
        return array
    pivot, i, j = array[left], left, right
    while i < j:
        while i < j and array[j] >= pivot:
            j -= 1
        array[i] = array[j]
        while i < j and array[i] <= pivot:
            i += 1
        array[j] = array[i]
    array[j] = pivot
    QuickSort(array, left, i-1)
    QuickSort(array, i+1, right)
    return array
```

**测试效果**

![img](https://pic3.zhimg.com/v2-364fb2c972d6fae47443c2c6e7682bfa_r.jpg)

## 堆排序

**基本原理**

比较类排序算法。算法描述如下(假设是升序排序)：

- 将初始待排序序列 ![[公式]](https://www.zhihu.com/equation?tex=R_1%2CR_2%2C...%2CR_n) 构建成大顶堆，此堆为初始的无序区；
- 将堆顶元素 ![[公式]](https://www.zhihu.com/equation?tex=R%5B1%5D) 和最后一个元素 ![[公式]](https://www.zhihu.com/equation?tex=R%5Bn%5D) 交换，此时得到新的无序区 ![[公式]](https://www.zhihu.com/equation?tex=R_1%2CR_2%2C...%2CR_%7Bn-1%7D) 和新的有序区 ![[公式]](https://www.zhihu.com/equation?tex=R_n) ，且满足：  ![[公式]](https://www.zhihu.com/equation?tex=R%5B1%2C2%2C...%2Cn-1%5D+%5Cleq+R%5Bn%5D) ；
- 由于交换后新的堆顶 ![[公式]](https://www.zhihu.com/equation?tex=R%5B1%5D) 可能违反堆的性质，因此需要将当前无序区  ![[公式]](https://www.zhihu.com/equation?tex=R_1%2CR_2%2C...%2CR_%7Bn-1%7D) 调整为新堆，然后再次将 ![[公式]](https://www.zhihu.com/equation?tex=R%5B1%5D) 与无序区最后一个元素交换，得到新的无序区 ![[公式]](https://www.zhihu.com/equation?tex=R_1%2CR_2%2C...%2CR_%7Bn-2%7D)  和新的有序区 ![[公式]](https://www.zhihu.com/equation?tex=R_%7Bn-1%7D%2CR_%7Bn-2%7D) 。不断重复此过程直到有序区的元素个数为 n-1 ，则整个排序过程完成。

**算法时间复杂度**

![[公式]](https://www.zhihu.com/equation?tex=O%28nlog_2n%29) 

**算法实现**

```text
'''堆化'''
def heapify(array, length, i):
    largest = i
    left = 2 * i + 1
    right = 2 * i + 2
    if left < length and array[largest] < array[left]:
        largest = left
    if right < length and array[largest] < array[right]:
        largest = right
    if largest != i:
        array[i], array[largest] = array[largest], array[i]
        heapify(array, length, largest)

'''堆排序'''
def HeapSort(array):
    length = len(array)
    for i in range(length, -1, -1):
        heapify(array, length, i)
    for i in range(length-1, 0, -1):
        array[i], array[0] = array[0], array[i]
        heapify(array, i, 0)
    return array
```

**测试效果**

![img](https://pic2.zhimg.com/v2-5766e372a8f19978d44eefc996b4be35_r.jpg)

## 计数排序

**基本原理**

非比较类排序算法。算法描述如下(假设是升序排序)：

- 找出待排序的数组中最大和最小的元素；
- 统计数组中每个值为i的元素出现的次数，存入数组C的第i项；
- 对所有的计数累加(从C中的第一个元素开始，每一项和前一项相加)；
- 反向填充目标数组，将每个元素i放在新数组的第C[i]项，每放一个元素就将C[i]减去1。

**算法时间复杂度**

![[公式]](https://www.zhihu.com/equation?tex=O%28n%2Bk%29) 

**算法实现**

```text
'''计数排序(假设都是0/正整数)'''
def CountingSort(array):
    length = len(array)
    max_value = max(array)
    count = [0 for _ in range(max_value+1)]
    output = [0 for _ in range(length)]
    for i in range(length):
        count[array[i]] += 1
    for i in range(1, len(count)):
        count[i] += count[i-1]
    for i in range(length):
        output[count[array[i]]-1] = array[i]
        count[array[i]] -= 1
    return output
```

**测试效果**

![img](https://pic3.zhimg.com/v2-b7ec76a5dc9a6e8008379c216d987772_r.jpg)

## 桶排序

**基本原理**

非比较类排序算法。算法描述如下(假设是升序排序)：

- 设置一个定量的数组当作空桶集合；
- 遍历输入数据，并且把数据一个个放到对应的桶里去(即在每个空桶放一定数值范围的数据)；
- 对每个非空的桶进行排序；
- 从不是空的桶里把排好序的数据拼接起来。

**算法时间复杂度**

![[公式]](https://www.zhihu.com/equation?tex=O%28n%2Bk%29) 

**算法实现**

```text
'''桶排序(假设都是整数)'''
def BucketSort(array):
    max_value, min_value, length = max(array), min(array), len(array)
    buckets = [0 for _ in range(min_value, max_value+1)]
    for i in range(length):
        buckets[array[i]-min_value] += 1
    output = []
    for i in range(len(buckets)):
        if buckets[i] != 0:
            output += [i+min_value] * buckets[i]
    return output
```

**测试效果**

![img](https://pic2.zhimg.com/v2-a3259be456770305ae21e088fe0af63d_r.jpg)

## 基数排序

**基本原理**

非比较类排序算法。其实就是先按最低位排序，然后按照高位排序，直到最高位。算法描述如下(假设是升序排序)：

- 取得数组中的最大数，并取得其位数；
- arr为原始数组，从最低位开始取每个位组成的基数数组；
- 对基数进行计数排序(利用计数排序适用于小范围数的特点)。

**算法时间复杂度**

![[公式]](https://www.zhihu.com/equation?tex=O%28n%2Ak%29) 

**算法实现**

```text
'''基数排序(假设都是整数)'''
def RadixSort(array):
    max_value = max(array)
    num_digits = len(str(max_value))
    for i in range(num_digits):
        buckets = [[] for k in range(10)]
        for j in array:
            buckets[int(j / (10 ** i)) % 10].append(j)
        output = [m for bucket in buckets for m in bucket]
    return output
```

**测试效果**

![img](https://pic3.zhimg.com/v2-0bd720c78594c228ef06fda5677fbefe_r.jpg)