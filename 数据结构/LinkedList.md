# LinkedList<E\>
---
* 是一个类
* 实现的接口：List,Collection,Iterable,Serializable,Clonedable, *Deque*, *Queue*
* 子类：没有子类

## 简介
* 它是链表
* 它还是队列、双端队列
* 它还可以用作堆栈
* 和ArrayList一样，不具有线程安全性

## 关于添加元素
**boolean add(E e)**  
添加到链表末尾

**void add(int index, E e)**  
添加到指定位置

**boolean addAll(int index, Collection<? extends E> c)**  

**boolean addAll(Collection<? extends E> c)**

**addFirst(E e)**
添加到链表的开头

**addLast(E e)**
添加到链表的末尾


## 关于它的迭代器
**iterator()**  
普通的迭代器

**descendingIterator()**  
逆向顺序的迭代器, ArrayList没有这个方法。

**listIterator()**  
ListIterator的迭代器, 

**listIterator(index)**  
从某个位置开始的ListIterator迭代器


## 关于如何实现
* 每个结点都是一个Node对象。
	* Node是LinkedList的一个私有静态内部类，一个Node对象包含当前结点的值，上一个结点，下一个结点。
		> E item;  
        > Node<E> next;  
        > Node<E> prev;  
* 它具有一个头结点和尾结点
	* transient Node<E> first;
	* transient Node<E> last;
* 

## 方法
### 它有基本的List的性质
* E get(int index)
* E set(int index, E element)
* add(int index, E element)
* E remove(int index)
### 它具有栈的性质,还是双向的…
* push()
* E poll()
* E pollFirst()  
* E pollLast()
* E peek()
* E peekFirst()  
* E peekLast()
### 它具有双向队列的性质
* E getFirst()
* E getLast()
* E removeFirst()
* E removeLast()
* addFirst(E e)
* addLast(E e)


## 参见
1. [Collection](Collection.md)
2. [List<E\>](List.md)
3. [ArrayList](ArrayList.md)
4. [Vector](Vector.md)
