# LinkedHashMap<K, V>
---
* 是一个泛型类
* 继承自HashMap
* 实现的接口

## 简介
* 维护着一个运行于所有条目的双重链接列表。
* 有两种顺序：
	* 插入顺序，FIFO, 默认
	* 访问顺序，类似于LRU
* 不同步的。

## LinkedHashMap.Entry<K, V>
* 继承自HashMap.Entry
* 多了Entry<K, V> before, after两个引用。但after和父类中的next有什么区别暂时还不知道。

## 参见
1. [Map](Map.md)
2. [HashMap](HashMap.md)