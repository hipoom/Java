# NavigableSet
---
* 泛型接口
* 父类接口：SortedSet等
* 实现类：TreeSet

## 简介
扩展的 SortedSet，具有了为给定搜索目标报告最接近匹配项的导航方法。方法 lower、floor、ceiling 和 higher 分别返回小于、小于等于、大于等于、大于给定元素的元素，如果不存在这样的元素，则返回 null。可以按升序或降序访问和遍历 NavigableSet。descendingSet 方法返回 set 的一个视图，该视图表示的所有关系方法和方向方法都是逆向的。升序操作和视图的性能很可能比降序操作和视图的性能要好。此外，此接口还定义了 pollFirst 和 pollLast 方法，它们返回并移除最小和最大的元素（如果存在），否则返回 null。subSet、headSet 和 tailSet 方法与名称相似的 SortedSet 方法的不同之处在于：可以接受用于描述是否包括（或不包括）下边界和上边界的附加参数。

## 方法


**Iterator<E\> descendingIterator()**  
以降序返回在此 set 的元素上进行迭代的迭代器。

**NavigableSet<E\> descendingSet()**  
返回此 set 中所包含元素的逆序视图。

**E floor(E e)**  
返回此 set 中**小于等于**给定元素的最大元素；如果不存在这样的元素，则返回 null。

**E lower(E e)**  
返回此 set 中**严格小于**给定元素的最大元素；如果不存在这样的元素，则返回 null。 

**E ceiling(E e)**  
返回此 set 中**大于等于**给定元素的最小元素；如果不存在这样的元素，则返回 null。 

**E higher(E e)**  
返回此 set 中**严格大于**给定元素的最小元素；如果不存在这样的元素，则返回 null。

**E pollFirst()**  
获取并移除第一个（最低）元素；如果此 set 为空，则返回 null。
 
**E pollLast()**   
获取并移除最后一个（最高）元素；如果此 set 为空，则返回 null。 

## 参见
1. [Collection](Collection.md)
2. [Set](Set.md)
3. [SortedSet](SortedSet.md)
4. [TreeSet](TreeSet.md)