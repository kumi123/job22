## 快手为何频繁考察这道Leetcode里没有的题

原创 一个搬砖的胖子 [一个搬砖的胖子](javascript:void(0);) *2月24日*

收录于话题

\#补充题

14个

## 前言

这几天在汇总牛客上快手面经所涉及的算法题，遇到了一道Leetcode上找不到的题目。

起初我并没在意，但后来发现好几篇快手的面经都考了这道题。

**我之前汇总了将近一千篇的牛客面经，都没有见过此题，但快手却频繁考察，此事必有蹊跷。**

我开始对这三篇面经分析，这三篇面经分别出现在8月、9月和10月。其中，2篇是后端开发面经，1篇是算法岗面经。

似乎面经之间没有太多关联，但有个共同点是，**他们都是在一面中遇到的题**！

![图片](https://mmbiz.qpic.cn/mmbiz_png/oD5ruyVxxVGBCSSXSQ9zXTEIeoht9YapF7aEuKUaQh6xN8cS6FwX6s3U1c2g1ibb23ov9KcA63QX7H3avrRu8Lg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)涉及数组小和问题的3篇面经

这道题目不是Leetcode题，居然能收到这么多面试官的欢迎？此题肯定有出处。经过调查发现，这道题是《程序员代码面试指南》的一道题。

![图片](data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==)位于《程序员代码面试指南》第8章

调查还没结束，我发现分享其中一篇面经的同学在下边的回复中**提到了面试的部门**!

![图片](data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==)10月-后端面经

我做了大胆的猜测，另一篇后端面经大概率也是平台研发部。于是我去私聊那篇面经的楼主。果不其然，被我猜中了。

![图片](data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==)8月-后端面经

至于另一篇算法岗的面经，按照文中叙述“面试面的搜索算法岗”，推测是一位做搜索相关算法的面试官，我也私聊进行了确认。

![图片](data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==)9月-算法面经

看到这，大家先不用慌，大多数面试官考察算法题还是以Leetcode为主，少数面试官可能会在其他地方找题，这些题目我都会在CodeTop里补充。

![图片](https://mmbiz.qpic.cn/mmbiz_png/oD5ruyVxxVGBCSSXSQ9zXTEIeoht9YapLyGtA3BtEpTbqhemGwtueibd1uicVMAKWo2zvTewlOc5Hblhs7GZWk6A/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)CodeTop按公司/部门/岗位查看高频题

至此，我完成了本题的调查。为防止之后可能还会有其他部门或者其他公司考察本题，我们一起来学习下。

# 题目描述

在一个数组中，每一个数左边比当前数小的数累加起来，叫做这个数组的小和。求一个数组的小和。

例子：

[1,3,4,2,5]

1左边比1小的数，没有；

3左边比3小的数，1；

4左边比4小的数，1、3；

2左边比2小的数，1；

5左边比5小的数，1、3、4、2；

所以小和为1+1+3+1+1+3+4+2=16

要求时间复杂度O(NlogN)，空间复杂度O(N)

# 题目分析

最容易想到的做法是遍历一遍数组，对于每个元素计算前面比它小的数的和，累加即可得出结果，时间复杂度是O(N²)。

本题的更好的算法是借助 「归并排序」的思路。smallSum([1,3,4,2,5])实际就等于smallSum([1,3,4])+smallSum([2,5])+c。之所以还有个c，是因为左半段数组中可能存在比右半段数组小的元素，这个不能遗漏。通过归并排序的merge过程，我们可以很方便计算这个c。

在merge时，左段数组L和右段数组R都是有序的了。结合下边的示意图，当L[i]<=R[j]时，表示L[i]比R[j]~R[r]的元素都要小。因此c需加上j及以后元素的个数*L[i]，即c+=(r-j+1) * L[i]。大概思路就是这样，下面附上C++版的参考代码。![图片](https://mmbiz.qpic.cn/mmbiz_png/oD5ruyVxxVGBCSSXSQ9zXTEIeoht9YapV2L36xB0JowvsGjyeDZvjftaK8VrJuNqBibOSMgoxFWYe8ibdrVqkXcg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

计算c的示意图

ps：我在牛客上找到了本题的评测链接，点击文末阅读全文即可直达。

# 参考代码

```java


import java.util.Scanner;

public class Main {
    private static long merge_sort(int[] nums,int[] tmp, int l, int r) {
        if (l >= r) return 0;//先判断
        int mid = l + (r - l) / 2;
        //其实本质上就是归并排序 加上一个临时数组来存储 先两边排序
        long res = merge_sort(nums, tmp, l, mid) + merge_sort(nums,tmp,mid + 1, r);

        int k = 0, i = l, j = mid + 1;
        //这里是为了把排序好的结果重新组合到临时数组当中
        while (i <= mid && j <= r) {
            if(nums[i] <= nums[j]) {
                res += (r - j + 1) * nums[i];
                tmp[k++] = nums[i++];//该补哪一个

            } else {
                // nums[i] > nums[j]
                tmp[k++] = nums[j++];
                // 当前左边的元素都大于nums[j]
                //[l, mid] [mid + 1,  r]

            }
        }

        while (i <= mid) tmp[k++] = nums[i++];
        while (j <= r) tmp[k++] = nums[j++];

        for (int i1 = l, j1 = 0; i1 <= r ; i1++, j1++) {
            nums[i1] = tmp[j1];
        }
        return res;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] arr = new int[n];
        int[] tmp = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }
        long sum = merge_sort(arr,tmp,0, n - 1);
        System.out.println(sum);
    }
}
```

