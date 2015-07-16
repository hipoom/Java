# Iterator<E\>
---
## 什么是结构上的修改
结构上的修改是指任何添加或删除一个或多个元素的操作，或者显式调整底层数组的大小；仅仅设置元素的值不是结构上的修改。

## 快速失败
在创建迭代器之后，除非通过迭代器自身的 remove 或 add 方法从结构上对列表进行修改，否则在任何时间以任何方式对列表进行结构上的修改，迭代器都会抛出 ConcurrentModificationException。因此，面对并发的修改，迭代器很快就会完全失败，而不是冒着在将来某个不确定时间发生任意不确定行为的风险。

注意，迭代器的快速失败行为无法得到保证，因为一般来说，不可能对是否出现不同步并发修改做出任何硬性保证。快速失败迭代器会尽最大努力抛出 ConcurrentModificationException。因此，为提高这类迭代器的正确性而编写一个依赖于此异常的程序是错误的做法：迭代器的快速失败行为应该仅用于检测 bug。

### 快速失败是如何检测的
每个类(如ArrayList, LinkedList, Vector等)有一个私有变量：**modCount**。  
modCount记录了该对象进行结构性修改的次数。  
在迭代器中，如ArrayList和Vector的私有类Itr中，以及LinkedList的私有类ListItr中，都有一个变量**expectedModCount**和一个方法**checkForComodification()**  
迭代器在创建的时候，expectedModCount 等于对象的 modCount。  
每次在checkForComodification()方法中检查的时候，就判断呢modCount和expectedModCount是都相等，如果不相等，抛出异常 **ConcurrentModificationException**

## 方法
1. hasNext()
2. next()
3. remove()
	* 在remove()方法调用前必须先至少调用过一次next()方法。

# ListIterator<E\>
---
## 光标位置
ListIterator的光标位置不指向任何一个元素，指向的是两个元素之间的位置。

## 和Iterator的对比
* 多了三个方法,可以实现逆序遍历。：
	* set()
	* hasPrevious()
	* previous()  
> 逆序遍历：  
>   ListIterator<E> it = list.listIterator(list.size())  
>   while(it.hasPrevious()){  
>   E e = it.previous();  
>   }  
> 即可实现从最后一个元素开始遍历。  

* **关于Set()方法**
	* Iterator没有set()方法；
	* ListIterator的set()方法使用时必须先调用next()或previous()方法。

## ListIterator的子类
1. 用于LinkedList的ListItr类，是一个私有类。
2. 用于ArrayList的ListItr类，继承实现了Iterator的Itr类（也是ArrayList的一个内部类），再实现了ListIterator接口。
3. 用于Vector的和用于ArrayList的ListItr类类似。但Vector的迭代器的**方法中**用了同步锁Synchronized，锁了当前对象。