# HashMap<K, V\>
---
* 是一个类
* 父类：AbstractMap<K, V>
* 实现的接口：Map<K, V>

## 简介
* 默认初始容量16， 默认加载因子0.75

## 元素是如何保存的？
* 当我们往hashmap中put元素的时候，先根据key的hash值得到这个元素在数组中的位置（即下标），然后就可以把这个元素放到对应的位置中了。如果这个元素所在的位子上已经存放有其他元素了，那么在同一个位子上的元素将以链表的形式存放，新加入的放在链头，最先加入的放在链尾。
* 链地址法
* [参见](http://blog.csdn.net/vking_wang/article/details/14166593)

## V put(K k, V v) 方法 
* int hash = hash(key.hashCode());
* int index = indexFor(hash, table.length);
* 遍历
	* 如果key已存在，替换为新value；
	* 如果不存在，添加
* 如果 key 已经存在，返回原来的值
* 如果 key 之前没有存在，返回null

## Entry的结构
K key  
V value  
Entry<K, V> next  
int hash  

### 构造方法
Entry(int h, K k, V v, Entry<K,V> n) {  
	value = v;  
	next = n;  
	key = k;  
	hash = h;  
}  


## 参见
1. [Map](Map.md)
2. [HashTable](HashTable.md)
