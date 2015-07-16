# List<E\>
---
* 是一个接口
* 继承的接口：Collection
* 间接继承的接口：Iterable
* 实现类：ArrayList、LinkedList、Vector等

## 简介
* List是**有序的Collection**。  
  - 可以对每个元素的插入位置进行精确控制。
  - 可以根据索引访问元素。    
* 允许重复元素
* 有自己的迭代器 ListIterator
* 如果元素包含自身，equals() 和 hashCode() 不再是良定义的


## 方法
**boolean add(E e)**: 添加到末尾  
**void add(int index, E e)**: 添加到指定位置  

**E set(int index, E e)**: 设置指定位置的元素,返回一个E  
**get(int index)**: 获得指定位置的元素  

**Iterator iterator()**   
**ListIterator listIterator()**  
**ListIterator listIterator(int index)**  

**int indexOf(E e)**  
**int lastIndexOf(E e)**

**List<E> subList(int fromIndex, int toIndex)**

## 参见
1. [Collection](Collection.md) 
2. [Set](Set.md)
3. [Queue](Queue.md)
4. [ArrayList](ArrayList.md)
5. [LinkedList](LinkedList.md)
6. [Vector](Vector.md)
7. [Itetator](Iterator.md)
