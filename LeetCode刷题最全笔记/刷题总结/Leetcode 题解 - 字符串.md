
---

title: 题解字符串
thumbnail: true
author: Kumi
icons: [fas fa-fire red, fas fa-star green]
cover: true
date: 2020-02-21 22:20:51
mathjax: true
top_meta: true
bottom_meta: true
headimg: https://cdn.jsdelivr.net/gh/kumi123/CDN2/16.jpg
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
# Leetcode 题解 - 字符串
<!-- GFM-TOC -->
* [Leetcode 题解 - 字符串](#leetcode-题解---字符串)
    * [1. 字符串循环移位包含](#1-字符串循环移位包含)
    * [2. 字符串循环移位](#2-字符串循环移位)
    * [3. 字符串中单词的翻转](#3-字符串中单词的翻转)
    * [4. 两个字符串包含的字符是否完全相同](#4-两个字符串包含的字符是否完全相同)
    * [5. 计算一组字符集合可以组成的回文字符串的最大长度](#5-计算一组字符集合可以组成的回文字符串的最大长度)
    * [6. 字符串同构](#6-字符串同构)
    * [7. 回文子字符串个数](#7-回文子字符串个数)
    * [8. 判断一个整数是否是回文数](#8-判断一个整数是否是回文数)
    * [9. 统计二进制字符串中连续 1 和连续 0 数量相同的子字符串个数](#9-统计二进制字符串中连续-1-和连续-0-数量相同的子字符串个数)
<!-- GFM-TOC -->


## 1. 字符串循环移位包含

[编程之美 3.1](#)

```html
s1 = AABCD, s2 = CDAA
Return : true
```

给定两个字符串 s1 和 s2，要求判定 s2 是否能够被 s1 做循环移位得到的字符串包含。

s1 进行循环移位的结果是 s1s1 的子字符串，因此只要判断 s2 是否是 s1s1 的子字符串即可。

## 2. 字符串循环移位

[编程之美 2.17](#)

```html
s = "abcd123" k = 3
Return "123abcd"
```

将字符串向右循环移动 k 位。

将 abcd123 中的 abcd 和 123 单独翻转，得到 dcba321，然后对整个字符串进行翻转，得到 123abcd。

## 3. 字符串中单词的翻转

[程序员代码面试指南](#)

```html
s = "I am a student"
Return "student a am I"
```

将每个单词翻转，然后将整个字符串翻转。

## 4. 两个字符串包含的字符是否完全相同

242\. Valid Anagram (Easy)

[Leetcode](https://leetcode.com/problems/valid-anagram/description/) / [力扣](https://leetcode-cn.com/problems/valid-anagram/description/)

```html
s = "anagram", t = "nagaram", return true.
s = "rat", t = "car", return false.
```

可以用 HashMap 来映射字符与出现次数，然后比较两个字符串出现的字符数量是否相同。

由于本题的字符串只包含 26 个小写字符，因此可以使用长度为 26 的整型数组对字符串出现的字符进行统计，不再使用 HashMap。



```java
public boolean isAnagram(String s, String t) {
    int[] cnts = new int[26];
    for (char c : s.toCharArray()) {
        cnts[c - 'a']++;
    }
    for (char c : t.toCharArray()) {
        cnts[c - 'a']--;
    }
    for (int cnt : cnts) {
        if (cnt != 0) {
            return false;
        }
    }
    return true;
}
```

## 5. 计算一组字符集合可以组成的回文字符串的最大长度

409\. Longest Palindrome (Easy)

[Leetcode](https://leetcode.com/problems/longest-palindrome/description/) / [力扣](https://leetcode-cn.com/problems/longest-palindrome/description/)

```html
Input : "abccccdd"
Output : 7
Explanation : One longest palindrome that can be built is "dccaccd", whose length is 7.
```

使用长度为 256 的整型数组来统计每个字符出现的个数，每个字符有偶数个可以用来构成回文字符串。

因为回文字符串最中间的那个字符可以单独出现，所以如果有单独的字符就把它放到最中间。

![image-20210221155749062](C:\Users\kumi\AppData\Roaming\Typora\typora-user-images\image-20210221155749062.png)

```java
public int longestPalindrome(String s) {
    int[] cnts = new int[256];
    for (char c : s.toCharArray()) {
        cnts[c]++;
    }
    int palindrome = 0;
    for (int cnt : cnts) {
        palindrome += (cnt / 2) * 2;
    }
    if (palindrome < s.length()) {
        palindrome++;   // 这个条件下 s 中一定有单个未使用的字符存在，可以把这个字符放到回文的最中间
    }
    return palindrome;
}










//自己的方法 https://leetcode-cn.com/problems/longest-palindrome/solution/javade-2chong-shi-xian-fang-fa-by-sweetiee/

class Solution {
    public int longestPalindrome(String s) {
        //这个题本质上就是用来构造好不好，所以要记录每一种数据出现的次数
        //因为每一个个数只能取偶数次，因为回文字符串只能有一个奇数，那就是放在最中间的
        int[] res= new int[58];
        for(char ss:s.toCharArray()){
            res[ss-'A']++;
        }
        int ans=0;
        for(int count:res){
            ans+=count-(count%2);//如果某一个字母出现了偶数个，那么就是取本身。如果是奇数 那么应该减去1
        }
        return (ans<s.length())?ans+1:ans;
    }
}
```

#### 一次遍历

```java
class Solution {
    public boolean isAnagram(String s, String t) {
        //一开始想用hashmap，但是还是麻烦一些，尤其在比较减少的时候，
        //直接用26长度数组，加加减减最后只要不为0 那么就说明是可以的
        //if(s==null||t==null||s.length()==0||t.length()==0) return false;
        if(s.length()!=t.length()) return false;
        //下边就是长度都一样的情况
        int[] res=new int[26];
        for(int j=0;j<s.length();j++){
            res[s.charAt(j)-'a']++;
            res[t.charAt(j)-'a']--;

        }
        for(int i=0;i<26;i++){
            if(res[i]!=0) return false;
        }
        return true;

    }
}
```



## 6. 字符串同构

205\. Isomorphic Strings (Easy)



### 比较代码,弄成一个这个list 用equal比较

```java
class Solution {
    public boolean isIsomorphic(String s, String t) {
        //满足某一种映射关系就可以，用00 11 这个表示看一下
 List<Integer> res1=new ArrayList<Integer>();
        List<Integer> res2=new ArrayList<Integer>();
        HashMap<Character,Integer> map1=new HashMap<>();
        HashMap<Character,Integer> map2=new HashMap<>();
        int flag1=0,flag2=0;
        for(int i=0;i<s.length();i++){
            Character tem=s.charAt(i);
            if(!map1.containsKey(tem)){
                flag1++;
                map1.put(tem,flag1);
            }
            res1.add(map1.get(tem));

        }
        for(int j=0;j<s.length();j++){
            Character tem1=t.charAt(j);
            if(!map2.containsKey(tem1)){
                flag2++;
                map2.put(tem1,flag2);
            }
            res2.add(map2.get(tem1));
        }
        return res1.equals(res2);
    }
}
```



[Leetcode](https://leetcode.com/problems/isomorphic-strings/description/) / [力扣](https://leetcode-cn.com/problems/isomorphic-strings/description/)

```html
Given "egg", "add", return true.
Given "foo", "bar", return false.
Given "paper", "title", return true.
```

记录一个字符上次出现的位置，如果两个字符串中的字符上次出现的位置一样，那么就属于同构。

```java
public boolean isIsomorphic(String s, String t) {
    int[] preIndexOfS = new int[256];
    int[] preIndexOfT = new int[256];
    for (int i = 0; i < s.length(); i++) {
        char sc = s.charAt(i), tc = t.charAt(i);
        if (preIndexOfS[sc] != preIndexOfT[tc]) {
            return false;
        }
        preIndexOfS[sc] = i + 1;
        preIndexOfT[tc] = i + 1;
    }
    return true;
}



class Solution {
    public boolean isIsomorphic(String s, String t) {
        //满足某一种映射关系就可以，用00 11 这个表示看一下
        char[] aa=s.toCharArray();
        char[] bb=t.toCharArray();
        for(int i=0;i<aa.length;i++){
            if(s.indexOf(aa[i])!=t.indexOf(bb[i])) return false;
        }
        //只要每一个位置上第一次出现位置相同，就可以保证整个相同
        return true;

    }
}
```

## 7. 回文子字符串个数

647\. Palindromic Substrings (Medium)

#### 动态规划

```java
class Solution {
    public int countSubstrings(String s) {
        //还是使用这个dp方法进行求解
        //dp[i][j]=true代表从i到j的字符串之中是回文串，如果是false那么就是不是
        //注意i<j 所以需要明确初始化dp=boolean 二维
        //递推关系 只有s[i]==s[j]相等的条件下，如果dp[i+1][j-1]为true，就可以
        //还有一种条件 s[i]==s[j]相等 但是j-i<=2也是可以的 说明是aa或者是aba这种
        boolean[][] dp = new boolean[s.length()][s.length()];//全部是false
        int ans=0;
        for(int j=0;j<s.length();j++){
            for(int i=0;i<=j;i++){
                if((s.charAt(i)==s.charAt(j))&&(j-i<=2||dp[i+1][j-1]==true)){
                    //这里有一个特点，就是j-i<=2才可以，因为一开始j-1可能会超出界限
                    dp[i][j]=true;
                    ans++;
                }
            }
        }
        return ans;


    }
}
```





[Leetcode](https://leetcode.com/problems/palindromic-substrings/description/) / [力扣](https://leetcode-cn.com/problems/palindromic-substrings/description/)

```html
Input: "aaa"
Output: 6
Explanation: Six palindromic strings: "a", "a", "a", "aa", "aa", "aaa".
```

从字符串的某一位开始，尝试着去扩展子字符串。

```java
class Solution {
   private int cnt = 0;//用来计算最终的结果

    public int countSubstrings(String s) {
        //思路就是很清晰，不放过每一个中心，单个字母往外扩展 双个字母往外扩展
        //不断的遍历
        for(int i=0;i<s.length();i++){
            extendSubstrings(s,i,i);
            extendSubstrings(s,i,i+1);//下边函数有保证，超过直接弄出来
            
        }
        return cnt;
    
}
    private void extendSubstrings(String s, int start, int end) {
        //要用while 因为一定是有延续性的好不好
        while(start>=0&&end<s.length()&&(s.charAt(start)==s.charAt(end))){
            start--;
            end++;
            cnt++;

        }
    
}

    }
```

## 8. 判断一个整数是否是回文数

9\. Palindrome Number (Easy)

[Leetcode](https://leetcode.com/problems/palindrome-number/description/) / [力扣](https://leetcode-cn.com/problems/palindrome-number/description/)



### [题解](https://leetcode-cn.com/problems/palindrome-number/solution/dong-hua-hui-wen-shu-de-san-chong-jie-fa-fa-jie-ch/)



```java
class Solution {
    public boolean isPalindrome(int x) {
        //先用第一种方法好不好，就是每一次都取出这个首尾的数字然后进行比较

        //为了求最高位的数据，需要不断的试探，找到是几位
        //java 当中/就是取这个整的操作
        if(x<0) return false;
        int div=1;//要从div=1开始 如果是个数数就是可以了直接满足
        while(x/div>=10) div*=10;//这样的话x/div就可以得到这个最高位数据了
        while(x>0){
            int left=x%10;
            int right=x/div;
            if(left!=right) return false;
            x=(x%div)/10;//12354如何变成235 需要12354%div=2354 再求这个2354/10 得到235
            div=div/100;
            //现在要求这个变化后的x
           

        }
        return true;



    }
}
```



要求不能使用额外空间，也就不能将整数转换为字符串进行判断。

将整数分成左右两部分，右边那部分需要转置，然后判断这两部分是否相等。

```java
public boolean isPalindrome(int x) {
    if (x == 0) {
        return true;
    }
    if (x < 0 || x % 10 == 0) {
        return false;
    }
    int right = 0;
    while (x > right) {
        right = right * 10 + x % 10;
        x /= 10;
    }
    return x == right || x == right / 10;
}



class Solution {
    public boolean isPalindrome(int x) {
        //这种就是不断的把右边的数据倒置然后和左边剩下的数据进行比较
        //x不断的x/10 right 从1变成right=right*10+x%10
        //当x<=right的时候说明过了
        //如果是奇数个，那么x==right 如果是偶数个 right/10==x 可以表明是回文数
        if(x==0)return true;
        if(x%10==0||x<0)return false;

        //以上是无法顾忌的情况
        int right=0;
        while(x>right){
            right=right*10+x%10;
            x=x/10;  
        }
        return x==right||x==right/10;
        

        
        



    }
}
```

## 9. 统计二进制字符串中连续 1 和连续 0 数量相同的子字符串个数

696\. Count Binary Substrings (Easy)

[Leetcode](https://leetcode.com/problems/count-binary-substrings/description/) / [力扣](https://leetcode-cn.com/problems/count-binary-substrings/description/)

```html
Input: "00110011"
Output: 6
Explanation: There are 6 substrings that have equal number of consecutive 1's and 0's: "0011", "01", "1100", "10", "0011", and "01".
```

```java
public int countBinarySubstrings(String s) {
    int preLen = 0, curLen = 1, count = 0;
    for (int i = 1; i < s.length(); i++) {
        if (s.charAt(i) == s.charAt(i - 1)) {
            curLen++;
        } else {
            preLen = curLen;
            curLen = 1;
        }

        if (preLen >= curLen) {
            count++;
        }
    }
    return count;
}
```
