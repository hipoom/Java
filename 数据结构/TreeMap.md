# TreeMap
---
* 是一个类
* 父类：AbstractMap
* 实现的接口：NavigableMap，SortedMap， Map

## 简介
* 基于红黑树（Red-Black tree）的 NavigableMap 实现。  
* 该映射根据其键的自然顺序进行排序，或者根据创建映射时提供的 Comparator 进行排序，具体取决于使用的构造方法。   
* 此实现为 containsKey、get、put 和 remove 操作提供受保证的 log(n) 时间开销。
* 非同步的。
* 迭代器快速失败的。

## 关于SortedMap
与HashMap的散列存储不同，SortedMap有特定的存储顺序（基于自然排序或由比较器决定）。  
提供了获取最大值，最小值，大于/小于某个值的子Map等方法。

## 关于NavigableMap
继承了SortedMap，按大小顺序存储。  
同时提供了最近匹配导航算法。

## 参见
1. [Map](Map.md)
2. [红黑树](Red_Black_Tree.md)
3. [关于红黑树的博客1](http://blog.csdn.net/chenssy/article/details/26668941) 注：*该博客有些地方有问题，仅作参考*
4. [关于红黑树的博客2](http://www.cnblogs.com/xuqiang/archive/2011/05/16/2047001.html)