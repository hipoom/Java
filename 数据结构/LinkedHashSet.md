# LinkedHashSet
---
* 是一个泛型类 
* 继承的父类HashSet

## 简介
* 相比HashSet，LinkedHashSet是有序的。  
* 相比HashSet内部由一个HashMap实例支持，LinkedHashSet内部是由一个LinkedHashMap实例在控制。其他都一样。
* **注意：**LinkedHashSet的构造方法时依托于HashSet的，耦合性极高。HashSet专门为LinkedHashSet保留了一个构造方法。参见LinkedHashSet的源码。

## 参见
1. [Collection](Collection.md)
2. [Set](Set.md)
3. [HashSet](HashSet.md)
4. [LinkedHashMap](LinkedHashMap.md)