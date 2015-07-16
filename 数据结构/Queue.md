# Queue<E\>
---
* 是一个泛型接口
* 父接口：Collection
* 子接口：Deque

## 简介
* 是个队列
* 插入、提取、检查操作都存在两种形式，一种在操作失败时返回特殊值，一种抛出异常。

## 方法
1. **boolean add(E e)和 boolean offer(E e)** 添加元素  
	失败时，add()抛出异常，offer()返回false  

2. **E element() 和 E peek()** 获取但不移除  
	失败时，element()抛出异常, peek()返回null

3. **E remove() 和 E poll()** 获取并移除  
	失败时，remove()抛出异常, poll()返回null

## 子类介绍
### Deque
* 双端队列
* 可以实现队列，也可以用作栈

## 参见
1. [Collection](Collection.md)
2. [List](List.md)
3. [Set](Set.md)