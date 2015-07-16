# SortedSet
---
* 是一个泛型接口
* 父接口：Set
* 子接口：NavigableSet
* 间接实现类：TreeSet

## 简介
* 进一步提供关于元素的总体排序的Set。
* 关于比较器是在实现类的构造方法中提供的。

## 方法
**SortedSet<E> subSet(E fromElement, E toElement)**  
提取子集  

**E first()**  
返回第一个(最低)元素  

**E last()**  
返回最后一个(最高)元素  

**SortedSet<E\> headSet(E toElement)**  
返回此 set 的部分视图，其元素严格小于 toElement。  

**SortedSet<E\> tailSet(E fromElement)**  
返回此 set 的部分视图，其元素大于等于 fromElement。

## 参见
1. [Collection](Collection.md)
2. [Set](Set.md)