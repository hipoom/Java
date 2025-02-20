# holder

这个库包含一些常用的数据结构或者小功能的封装。

## 1. WatchDog
使用方法：
```
// 添加一个看门狗，并得到 notifier.
val notifier = WatchDog.watch(1000) {
    // 超过 1000 毫秒后会执行的逻辑。
}

// 取消看门狗
notifier.cancel();
```

## 2. Callbacks
这是一个封装的线程安全的列表，通常用于保存各种 listener、 callback.


## 3. ListValueMap
这是一个 Value 是列表的 Map.


## 4. ObjectPool
对象池。


## 5. SafeCountDownLatch
这个类是避免在使用时 CountDownLatch 不注意，在错误的线程发起了等待，导致线程死锁。
所以把 wait() 去掉了，只保留 tryWait(long timeout, Runnable onTimeoutCallback);