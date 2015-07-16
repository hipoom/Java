# Collection<E\><A NAME="Collection"> </a>
---
* 是一个超级接口
* 继承的接口：Iterable
* 子接口：List、Set、Queue等

## 简介
* Collection表示一组对象，是层次结构的根接口。  
* 该接口没有具体的直接实现，但提供了更具体的子接口，如Set、List等。  

## 方法
**retainAll(Collection<? extends E\> c)**：保留，交运算  
**addAll(Collection<? extends E\> c)**：添加，并运算  
**removeAll(Collection<? extends E\> c)**：移除，减运算  

## 参见
1. [List](List.md)
2. [Set](Set.md)
3. [Queue](Queue.md)