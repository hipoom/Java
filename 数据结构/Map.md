# Map<K, V\>
---
* 是一个泛型接口
* 没有父类
* 子接口：SortedMap, NavigableMap等
* 实现类：HashMap, HashTable, TreeMap, LinkedHashMap等

## 简介
* 将键映射到值的对象。

## 方法
**boolean containsKey(Object key)**

**boolean containsValue(Object value)**

**V get(Object key)**

**V put(K key, V value)**

**V remove(Object key)**

**Set<K\> keySet()**

**Collection<V\> values()**

**Set<Map.Entry<K, V\>> entrySet()**  

## Map如何遍历
* 获得KeySet  
> Set<String\> keys = m.keySet();  
> Iterator<String\> it1 = keys.iterator();  

* 获得EntrySet
> Set<Map.Entry<String, Integer>> entries = m.entrySet();  
> Iterator<Map.Entry<String, Integer>> it = entries.iterator();

* 获得ValuesCollection
> Collection<Integer> values = m.values();  
> Iterator<Integer> it = values.iterator();

## 遍历大容量Map时应该怎么遍历
* 采用EntrySet的方式。
* 获得KeySet方式的缺陷：由于只是获得KeySet，要活的Value还需要执行map.get(key)才行，而在执行get()方法时，需要计算hashCode，这是一个耗时的操作。而用EntrySet则不用。

## 关于遍历时的这些Set
* AbstractMap中有两个变量，keySet和entrySet：
> transient volatile Set<K>        keySet = null;  
> transient volatile Collection<V> values = null;  
> 注意他们都被volatile修饰，但有的在子类中被隐藏了。  
这两个变量在Map的实现类中被维护着。通过map.keySet()获得的keySet只是获得了私有变量的引用。会随着Map的结构性改变而改变。


## Entry接口
### 方法
**K getKey()**  
**V getValue()**  
**V setValue(V value)**

## 参见
1. [Set](Set.md)
2. [HashMap](HashMap.md)
3. [HashTable](HashTable.md)
4. [TreeMap](TreeMap.md)
5. [LinkedHashMap](LinkedHashMap.md)