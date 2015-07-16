# Vector<E\>
---
* 是一个类，*一个线程安全的ArrayList*
* 实现的接口：List, Collection, Iterable, Serializable, Cloneable, *RandomAccess*  
* 子类：Stack

## 它为什么线程安全
在源码中，Vector的所有方法都加了同步锁Synchronized。  
例如**public synchronized boolean add(E e)**方法。
也有一些方法没有加同步锁，比如：  
**public boolean contains(Object o)**  
**public int indexOf(Object o)**  
**public void add(int index, E element)**

## 它并非绝对线程安全
Vector安全的原因在于它对方法加了锁。  
当一个操作只调用一个方法时，该操作是原子操作，是线程安全的；  
当一个操作包含了多个原子操作时，这个操作就不再是原子操作了，当然也就不安全了。
例如：  
> if(!vector.containsAll(vector2)){  
>	vector.addAll(vector2);  
> }  

单独看containsAll() 和 addAll() 都有锁，线程安全。
但拼在一起就不是了。有可能在执行完if语句后，时间片给了另一个线程。完全有可能在另一个线程中vector被改变了。  
解决办法是将整个if语句全锁起来，作为一个原子操作。  
**Vector的方法都具有 synchronized 关键修饰，单独执行时都是线程同步的。但对于复合操作，Vector 仍然需要进行同步处理。** 

## 参见
1. [Collection](Collection.md)
2. [List<E\>](List.md)
3. [LinkedList](LinkedList.md)
4. [ArrayList](ArrayList.md)
5. [Vector](Vector.md)