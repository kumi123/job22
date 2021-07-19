---

title: 题解双指针
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2020-06-13 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/13.jpg
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

# Leetcode 题解 - 双指针
* [Leetcode 题解 - 双指针](#leetcode-题解---双指针)
    * [1. 有序数组的 Two Sum](#1-有序数组的-two-sum)
    * [2. 两数平方和](#2-两数平方和)
    * [3. 反转字符串中的元音字符](#3-反转字符串中的元音字符)
    * [4. 回文字符串](#4-回文字符串)
    * [5. 归并两个有序数组](#5-归并两个有序数组)
    * [6. 判断链表是否存在环](#6-判断链表是否存在环)
    * [7. 最长子序列](#7-最长子序列)



双指针主要用于遍历数组，两个指针指向不同的元素，从而协同完成任务。

## 1. 有序数组的 Two Sum

167\. Two Sum II - Input array is sorted (Easy)

[Leetcode](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/description/) / [力扣](https://leetcode-cn.com/problems/two-sum-ii-input-array-is-sorted/description/)

```html
Input: numbers={2, 7, 11, 15}, target=9
Output: index1=1, index2=2
```

题目描述：在有序数组中找出两个数，使它们的和为 target。

使用双指针，一个指针指向值较小的元素，一个指针指向值较大的元素。指向较小元素的指针从头向尾遍历，指向较大元素的指针从尾向头遍历。

- 如果两个指针指向元素的和 sum == target，那么得到要求的结果；
- 如果 sum \> target，移动较大的元素，使 sum 变小一些；
- 如果 sum \< target，移动较小的元素，使 sum 变大一些。

数组中的元素最多遍历一次，时间复杂度为 O(N)。只使用了两个额外变量，空间复杂度为  O(1)。

<div align="center"> <img src="https://cs-notes-1256109796.cos.ap-guangzhou.myqcloud.com/437cb54c-5970-4ba9-b2ef-2541f7d6c81e.gif" width="200px"> </div><br>

```java
public int[] twoSum(int[] numbers, int target) {
    if (numbers == null) return null;//加一个判断
    int i = 0, j = numbers.length - 1;
    while (i < j) {
        int sum = numbers[i] + numbers[j];
        if (sum == target) {
            return new int[]{i + 1, j + 1};//直接来进行输出好不好
        } else if (sum < target) {
            i++;
        } else {
            j--;
        }
    }
    return null;//如果跳出来那么就应该是没有解决方案，返回的应该是null 因为int[] 是一个对象
}
```

## 2. 两数平方和

633\. Sum of Square Numbers (Easy)

[Leetcode](https://leetcode.com/problems/sum-of-square-numbers/description/) / [力扣](https://leetcode-cn.com/problems/sum-of-square-numbers/description/)

```html
Input: 5
Output: True
Explanation: 1 * 1 + 2 * 2 = 5
```

题目描述：判断一个非负整数是否为两个整数的平方和。

可以看成是在元素为 0\~target 的有序数组中查找两个数，使得这两个数的平方和为 target，如果能找到，则返回 true，表示 target 是两个整数的平方和。

本题和 167\. Two Sum II - Input array is sorted 类似，只有一个明显区别：一个是和为 target，一个是平方和为 target。本题同样可以使用双指针得到两个数，使其平方和为 target。

本题的关键是右指针的初始化，实现剪枝，从而降低时间复杂度。设右指针为 x，左指针固定为 0，为了使 0<sup>2</sup> + x<sup>2</sup> 的值尽可能接近 target，我们可以将 x 取为 sqrt(target)。

因为最多只需要遍历一次 0\~sqrt(target)，所以时间复杂度为 O(sqrt(target))。又因为只使用了两个额外的变量，因此空间复杂度为 O(1)。

- [x] ==sqrt之后需要进行取整操作(int) Math.sqrt(target)==
- [x] ==注意这个时候因为可能是两个相同数的平方和，所以可以进行while(l<=r)==

```java
 public boolean judgeSquareSum(int target) {
     if (target < 0) return false;
     int i = 0, j = (int) Math.sqrt(target);
     while (i <= j) {
         int powSum = i * i + j * j;
         if (powSum == target) {
             return true;
         } else if (powSum > target) {
             j--;
         } else {
             i++;
         }
     }
     return false;
 }
```

## 3. 反转字符串中的元音字符

345\. Reverse Vowels of a String (Easy)



##### python方法

```python
class Solution:
    #str类型是 可以 直接查询 in 和 not in的，转换为 list 只是为了方便修改数据，因为在python中，str类型是不可变的
    def reverseVowels(self, s: str) -> str:
        l,r=0,len(s)-1
        dic=["a","e","i","o","u","A","E","I","O","U"]
        s=list(s)#必须要转化成这种格式
        while l<r:
            if s[l] in dic and s[r] in dic:
                s[l],s[r]=s[r],s[l]
                l+=1
                r-=1
            elif s[l] in dic and s[r] not in dic :
                r-=1
            elif not s[l] in dic  and s[r] in dic:
                l+=1
            else:
                l+=1
                r-=1
        return "".join(s)#列表转 字符串
```

```python 
#这样的判断基本是比较合理的
class Solution:
    #str类型是 可以 直接查询 in 和 not in的，转换为 list 只是为了方便修改数据，因为在python中，str类型是不可变的
    def reverseVowels(self, s: str) -> str:
        l,r=0,len(s)-1
        dic=["a","e","i","o","u","A","E","I","O","U"]
        s=list(s)#必须要转化成这种格式
        while l<r:
            if s[l] not in dic:
                l+=1                
            elif s[r] not in dic:
                r-=1
            elif s[l] in dic and s[r] in dic:#可以直接用else来进行代替
                s[l],s[r]=s[r],s[l]
                l+=1
                r-=1
        return "".join(s)
```



[Leetcode](https://leetcode.com/problems/reverse-vowels-of-a-string/description/) / [力扣](https://leetcode-cn.com/problems/reverse-vowels-of-a-string/description/)

```html
Given s = "leetcode", return "leotcede".
```

<div align="center"> <img src="https://cs-notes-1256109796.cos.ap-guangzhou.myqcloud.com/a7cb8423-895d-4975-8ef8-662a0029c772.png" width="400px"> </div><br>

使用双指针，一个指针从头向尾遍历，一个指针从尾到头遍历，当两个指针都遍历到元音字符时，交换这两个元音字符。

为了快速判断一个字符是不是元音字符，我们将全部元音字符添加到集合 HashSet 中，从而以 O(1) 的时间复杂度进行该操作。

- 时间复杂度为 O(N)：只需要遍历所有元素一次
- 空间复杂度 O(1)：只需要使用两个额外变量

```java
class Solution {
    public String reverseVowels(String s) {
        //首先使用HashSet来进行存储数组问题
        HashSet<Character> dic=new HashSet<>(Arrays.asList('a','e','i','o','u','A','E','I','O','U'));
        int l=0,r=s.length()-1;
        char[] res=new char[s.length()];
        while(l<=r){//这里是需要进行把当前的单词移动到res当中，因此是<=
            char cl=s.charAt(l);
            char cr=s.charAt(r);
            if(!dic.contains(cl)){
                res[l++]=cl;
            }
            else if(!dic.contains(cr)){
                res[r--]=cr;
            }
            else if(dic.contains(cr)&&dic.contains(cl)){
                res[l++]=cr;
                res[r--]=cl;
            }

        }
        return new String(res);
        

    }
}
```



<div align="center"> <img src="https://cs-notes-1256109796.cos.ap-guangzhou.myqcloud.com/ef25ff7c-0f63-420d-8b30-eafbeea35d11.gif" width="400px"> </div><br>

```java
private final static HashSet<Character> vowels = new HashSet<>(
        Arrays.asList('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'));

public String reverseVowels(String s) {
    if (s == null) return null;
    int i = 0, j = s.length() - 1;
    char[] result = new char[s.length()];
    while (i <= j) {
        char ci = s.charAt(i);
        char cj = s.charAt(j);
        if (!vowels.contains(ci)) {
            result[i++] = ci;
        } else if (!vowels.contains(cj)) {
            result[j--] = cj;
        } else {
            result[i++] = cj;
            result[j--] = ci;
        }
    }
    return new String(result);
}
```

## 4. 回文字符串

680\. Valid Palindrome II (Easy)

[Leetcode](https://leetcode.com/problems/valid-palindrome-ii/description/) / [力扣](https://leetcode-cn.com/problems/valid-palindrome-ii/description/)

```html
Input: "abca"
Output: True
Explanation: You could delete the character 'c'.
```

题目描述：可以删除一个字符，判断是否能构成回文字符串。

所谓的回文字符串，是指具有左右对称特点的字符串，例如 "abcba" 就是一个回文字符串。



因此双指针判断不是回文字符串是比较easy的，while(l<r)中间不会输出错误那么到头就是回文的字符串



使用双指针可以很容易判断一个字符串是否是回文字符串：令一个指针从左到右遍历，一个指针从右到左遍历，这两个指针同时移动一个位置，每次都判断两个指针指向的字符是否相同，如果都相同，字符串才是具有左右对称性质的回文字符串。

<div align="center"> <img src="https://cs-notes-1256109796.cos.ap-guangzhou.myqcloud.com/fcc941ec-134b-4dcd-bc86-1702fd305300.gif" width="250px"> </div><br>

本题的关键是处理删除一个字符。在使用双指针遍历字符串时，如果出现两个指针指向的字符不相等的情况，我们就试着删除一个字符，再判断删除完之后的字符串是否是回文字符串。

在判断是否为回文字符串时，我们不需要判断整个字符串，因为左指针左边和右指针右边的字符之前已经判断过具有对称性质，所以只需要判断中间的子字符串即可。

在试着删除字符时，我们既可以删除左指针指向的字符，也可以删除右指针指向的字符。



#### python  匿名函数

```python
class Solution:
    def validPalindrome(self, s: str) -> bool:
        ispara=lambda x:x==x[::-1]
        l,r=0,len(s)-1
        while l<r:
            if s[l]!=s[r]:
                return ispara(s[l:r]) or ispara(s[l+1:r+1])
            l+=1
            r-=1
        return True
```



#### java函数



```java
class Solution {
    public boolean validPalindrome(String s) {
        //使用一个子函数就是
        int l=0,r=s.length()-1;
        while(l<r){
            if(s.charAt(l)!=s.charAt(r)){
                return isPara(s,l,r-1)||isPara(s,l+1,r);
            }
            l++;
            r--;
            
        }
        return true;//说明没有输出，就是直接就是回文字符串


    }
    public boolean isPara(String s,int l,int r){
        while(l<r){
            if(s.charAt(l)!=s.charAt(r)){
                return false;
            }
            l++;
            r--;
        }
        return true;
    }
}
```



```java
class Solution {
    public boolean validPalindrome(String s) {
        //使用一个子函数就是
        //int l=0,r=s.length()-1;
        for(int i=0,j=s.length()-1;i<j;i++,j--){
            if(s.charAt(i)!=s.charAt(j)){
                return isPara(s,i,j-1)||isPara(s,i+1,j);
            }
            
        }
        return true;//说明没有输出，就是直接就是回文字符串


    }
    public boolean isPara(String s,int l,int r){
        while(l<r){
            if(s.charAt(l)!=s.charAt(r)){
                return false;
            }
            l++;
            r--;
            //可以简化成为这个s.charAt(l++)!=s.charAt(r--) 
        }
        return true;
    }
}
```



<div align="center"> <img src="https://cs-notes-1256109796.cos.ap-guangzhou.myqcloud.com/db5f30a7-8bfa-4ecc-ab5d-747c77818964.gif" width="300px"> </div><br>

```java
public boolean validPalindrome(String s) {
    for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
        if (s.charAt(i) != s.charAt(j)) {
            return isPalindrome(s, i, j - 1) || isPalindrome(s, i + 1, j);
        }
    }
    return true;
}

private boolean isPalindrome(String s, int i, int j) {
    while (i < j) {
        if (s.charAt(i++) != s.charAt(j--)) {
            return false;
        }
    }
    return true;
}
```

## 5. 归并两个有序数组

88\. Merge Sorted Array (Easy)

[Leetcode](https://leetcode.com/problems/merge-sorted-array/description/) / [力扣](https://leetcode-cn.com/problems/merge-sorted-array/description/)

```html
Input:
nums1 = [1,2,3,0,0,0], m = 3
nums2 = [2,5,6],       n = 3

Output: [1,2,2,3,5,6]
```

题目描述：把归并结果存到第一个数组上。

需要从尾开始遍历，否则在 nums1 上归并得到的值会覆盖还未进行归并比较的值。

#### 两个点



```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        //简单来说就是需要从后边进行确定分析并且
        int len=m+n-1,len1=m-1,len2=n-1;
        while(len1>=0&&len2>=0){//当两着都大于0时候
            nums1[len--]=nums1[len1]>=nums2[len2]?nums1[len1--]:nums2[len2--];

        }
        if(len2>=0){//因为是减了，所以等于零也可以
            System.arraycopy(nums2,0,nums1,0,len2+1);//赋值复制函数要明确
        }
        

    }
}

```



```java
public void merge(int[] nums1, int m, int[] nums2, int n) {
    int index1 = m - 1, index2 = n - 1;
    int indexMerge = m + n - 1;
    while (index1 >= 0 || index2 >= 0) {
        if (index1 < 0) {
            nums1[indexMerge--] = nums2[index2--];
        } else if (index2 < 0) {
            nums1[indexMerge--] = nums1[index1--];
        } else if (nums1[index1] > nums2[index2]) {
            nums1[indexMerge--] = nums1[index1--];
        } else {
            nums1[indexMerge--] = nums2[index2--];
        }
    }
}
```

## 6. 判断链表是否存在环

141\. Linked List Cycle (Easy)

[Leetcode](https://leetcode.com/problems/linked-list-cycle/description/) / [力扣](https://leetcode-cn.com/problems/linked-list-cycle/description/)

使用双指针，一个指针每次移动一个节点，一个指针每次移动两个节点，如果存在环，那么这两个指针一定会相遇。

```java
public boolean hasCycle(ListNode head) {
    if (head == null) {
        return false;
    }
    ListNode l1 = head, l2 = head.next;
    while (l1 != null && l2 != null && l2.next != null) {
        if (l1 == l2) {
            return true;
        }
        l1 = l1.next;
        l2 = l2.next.next;
    }
    return false;
}
```

## 7. 最长子序列

524\. Longest Word in Dictionary through Deleting (Medium)

[Leetcode](https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/description/) / [力扣](https://leetcode-cn.com/problems/longest-word-in-dictionary-through-deleting/description/)

```
Input:
s = "abpcplea", d = ["ale","apple","monkey","plea"]

Output:
"apple"
```

题目描述：删除 s 中的一些字符，使得它构成字符串列表 d 中的一个字符串，找出能构成的最长字符串。如果有多个相同长度的结果，返回字典序的最小字符串。

通过删除字符串 s 中的一个字符能得到字符串 t，可以认为 t 是 s 的子序列，我们可以使用双指针来判断一个字符串是否为另一个字符串的子序列。

```java
public String findLongestWord(String s, List<String> d) {
    String longestWord = "";
    for (String target : d) {
        int l1 = longestWord.length(), l2 = target.length();
        if (l1 > l2 || (l1 == l2 && longestWord.compareTo(target) < 0)) {
            continue;
        }
        if (isSubstr(s, target)) {
            longestWord = target;
        }
    }
    return longestWord;
}

private boolean isSubstr(String s, String target) {
    int i = 0, j = 0;
    while (i < s.length() && j < target.length()) {
        if (s.charAt(i) == target.charAt(j)) {
            j++;
        }
        i++;
    }
    return j == target.length();
}
```





#### [这个题解真的很棒](https://leetcode-cn.com/problems/longest-word-in-dictionary-through-deleting/solution/kan-si-bu-fu-za-qi-shi-zhen-de-bu-fu-za-by-uygn9i8/)

### 自己写的

```java
class Solution {
    public String findLongestWord(String s, List<String> d) {
       String res="";
       for(String std_d:d){
           int j=0;
           for(int i=0;i<s.length();i++){
               if(j<std_d.length()){
                if(std_d.charAt(j)==s.charAt(i)){
                   j++;
                   if(j==std_d.length()){
                       if(std_d.length()>res.length()||(std_d.length()==res.length()&&std_d.compareTo(res)<0)) res=std_d;
                   }
               }


               }

               
           }
       }
       return res;
        
    }
}

```

### 标准的结果

```java
class Solution {
    public String findLongestWord(String s, List<String> d) {
        String str="";
        for(String sstr:d){
            for(int i=0,j=0;i<s.length()&&j<sstr.length();i++){
                if(s.charAt(i)==sstr.charAt(j)) j++;
                if(j==sstr.length()){
                    if(sstr.length()>str.length()||(sstr.length()==str.length()&&str.compareTo(sstr)>0))  str=sstr;
                }
            }
        }
        return str;
        
    }
}


```

```python
class Solution:
    def findLongestWord(self, s: str, d: List[str]) -> str:
        res=""
        for std_d in d:#对于每一个字符串
            j=0#尽头都在0
            for i in range(len(s)):#把主字符串进行比较，如果小字符串能够到达尾部那说明可以纳入更新范围
                if j<len(std_d):
                    if s[i]==std_d[j]:
                        j+=1
                        if j==len(std_d):#把主字符串进行比较，如果小字符串能够到达尾部那说明可以纳入更新范围
                            if len(std_d)>len(res) or (len(std_d)==len(res) and std_d<res):
                                res=std_d
                            #更新条件就是长度大于或者是长度相等但是字典序小
        return res
```

