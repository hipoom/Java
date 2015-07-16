# Stack<E\>
---
* 父类：Vector
* 子类：没有子类

## 简介
* 是栈
* 新的方法中，push()、empty()不是同步方法，其他全是。

## 方法
1. **public E push(E item)**  入栈  
2. **public synchronized E peek()** 查询栈顶元素  
	栈为空，抛StackEmptyException
3. **public synchronized E pop()**  出栈  
	先调用peek(), 再removeElementAt()。  
4. **public synchronized int search(Object o)**  查询对象的索引。  
	内部调用 lastIndexOf(o)。最后入栈的元素index=1。

## 其他
使用迭代器时，是从最先加入的元素开始迭代，即从栈底往栈顶迭代。

## 参见
1. [Collection](Collection.md)
2. [List<E\>](List.md)
3. [Vector](Vector.md)