# HashTable
---
* 是一个类
* 父类：Dictionary<K, V>
* 实现的接口：Map<K, V>

## 简介
* 初始容量为11

## 与HashMap的区别
1. HashMap继承自AbstractMap，HashTable继承自Dictionary
2. HashTable方法时同步的，HashMap方法不是。
3. HashTable中，Key和Value都不能为null；HashMap可以。  
	>在HashMap中，null可以作为键，这样的键只有一个；可以有一个或多个键所对应的值为null。当get()方法返回null值时，即可以表示 HashMap中没有该键，也可以表示该键所对应的值为null。因此，在HashMap中不能由get()方法来判断HashMap中是否存在某个键， 而应该用containsKey()方法来判断。
4. 哈希值的使用不同，HashTable直接使用对象的hashCode。而HashMap重新计算hash值。
5. Hashtable和HashMap它们两个内部实现方式的数组的初始大小和扩容的方式。HashTable中hash数组默认大小是11，增加的方式是 old*2+1。HashMap中hash数组的默认大小是16，而且一定是2的指数。

## 子类Properties
用于存储一些配置文件

## 参见
1. [Map](Map.md)
2. [HashMap](HashMap.md)