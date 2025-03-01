package com.hipoom.holder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hipoom.function.Function1;
import com.hipoom.function.VoidFunction1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ZhengHaiPeng
 * @since 2024/8/3 15:50
 */
@SuppressWarnings("unused")
public class Callbacks<C> {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    /**
     * 默认优先级。
     */
    public static final int DEFAULT_PRIORITY = 0;

    /**
     * 已经添加的 callbacks.
     */
    private final TreeMap<Integer, List<C>> callbacks = new TreeMap<>();

    /**
     * 大小变化后的回调。
     */
    private Callbacks<OnSizeChangedCallback> onSizeChangedCallbacks;

    /**
     * 用于保证 {@link #onSizeChangedCallbacks} 线程安全的锁。
     */
    private final Object mutexForSizeChanged = new Object();



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 添加 callback 对象。
     */
    public void add(@Nullable C callback) {
        if (callback == null) {
            return;
        }
        add(DEFAULT_PRIORITY, callback);
    }

    /**
     * 添加 callback 对象。
     *
     * @param priority 优先级。 优先级高的，先执行；相同优先级，先添加的先执行。
     */
    public void add(int priority, @NonNull C callback) {
        // 由于 TreeMap 是按照 Key 从小到大排序，所以为了让 priority 大的先遍历到，插入时先把 priority 取负值。
        int revPriority = -priority;
        lockCallbacksThen(it -> {
            List<C> temp = it.get(revPriority);
            //noinspection Java8MapApi
            if (temp == null) {
                temp = new ArrayList<>();
                it.put(revPriority, temp);
            }
            temp.add(callback);
        });

        int size = getSize();
        tryNotifySizeChanged(size - 1, size);
    }

    /**
     * 移除某个回调。
     *
     * @param callback 希望移除的回调。
     * @return 被移除的对象。 如果是 null, 说明本来就没有添加过 callback 对象。
     */
    @SuppressWarnings("UnusedReturnValue")
    @Nullable
    public C remove(@Nullable C callback) {
        List<C> removed = removeIf(it -> it == callback);
        if (removed.isEmpty()) {
            return null;
        }
        return removed.get(0);
    }

    /**
     * 移除所有。
     */
    public void removeAll() {
        int size = getSize();
        lockCallbacksThen(TreeMap::clear);
        tryNotifySizeChanged(size, 0);
    }

    /**
     * 移除所有满足条件 predication 的回调。 并返回被移除的所有元素。
     *
     * @param predication 满足这个条件的，会被移除。由于在遍历时会 lock 住内部持有的 callbacks 对象，所以 predication 不应该执行太长时间。
     */
    @NonNull
    public List<C> removeIf(@NonNull Function1<C, Boolean> predication) {
        List<C> removed = new ArrayList<>();

        lockCallbacksThen(it -> {
            // 遍历所有的优先级
            Set<Integer> priorities = it.keySet();
            for (Integer priority : priorities) {

                // 对每一个 priority，获取其 callbacks
                List<C> callbacks = it.get(priority);
                if (callbacks != null) {

                    // 遍历所有的 callbacks
                    Iterator<C> iterator = callbacks.iterator();
                    while (iterator.hasNext()) {
                        C callback = iterator.next();

                        // 如果 callback 需要被移除，移除并添加到返回集合中
                        boolean isMatched = predication.invoke(callback);
                        if (isMatched) {
                            iterator.remove();
                            // 添加到已移除列表中
                            removed.add(callback);
                        }
                    }
                }
            }
        });

        int size = getSize();
        tryNotifySizeChanged(size + removed.size(), size);

        return removed;
    }

    /**
     * 获取所有满足条件 predication 的回调。
     */
    public List<C> findIf(@NonNull Function1<C, Boolean> predication) {
        List<C> found = new ArrayList<>();
        forEach(it -> {
            if (predication.invoke(it)) {
                found.add(it);
            }
            return true;
        });
        return found;
    }

    /**
     * 通知所有的 Callback。
     */
    public void notifyAll(@NonNull VoidFunction1<C> howNotify) {
        forEach(callback -> {
            try {
                howNotify.invoke(callback);
            } catch (Exception e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
            return true;
        });
    }

    /**
     * 遍历每一个 callback。
     * 如果 handler 返回 false，表示中断遍历。
     * handler 返回 true 表示继续遍历。
     */
    public void forEach(@NonNull Function1<C, Boolean> handler) {
        copyCallbacksThen(callbacks -> {
            if (callbacks.isEmpty()) {
                return;
            }

            // 遍历每一个 entry
            for (Entry<Integer, List<C>> entry : callbacks.entrySet()) {
                for (C callback : entry.getValue()) {
                    try {
                        boolean res = handler.invoke(callback);
                        if (!res) {
                            break;
                        }
                    } catch (Exception e) {
                        //noinspection CallToPrintStackTrace
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public int getSize() {
        AtomicInteger allSize = new AtomicInteger(0);
        lockCallbacksThen(callbacks -> {
            for (Map.Entry<Integer, List<C>> entry : callbacks.entrySet()) {
                int size = entry.getValue().size();
                allSize.addAndGet(size);
            }
        });
        return allSize.get();
    }

    public void addOnSizeChangedCallback(OnSizeChangedCallback callback) {
        synchronized (mutexForSizeChanged) {
            if (onSizeChangedCallbacks == null) {
                onSizeChangedCallbacks = new Callbacks<>();
            }
            onSizeChangedCallbacks.add(callback);
        }
    }

    public void removeOnSizeChangedCallback(OnSizeChangedCallback callback) {
        synchronized (mutexForSizeChanged) {
            if (onSizeChangedCallbacks == null) {
                return;
            }
            onSizeChangedCallbacks.remove(callback);
        }
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    /**
     * 锁住 callbacks 对象，然后执行 function 回调。
     * 适用场景： function 会很快就执行完的场景。
     */
    private void lockCallbacksThen(VoidFunction1<TreeMap<Integer, List<C>>> function) {
        synchronized (callbacks) {
            function.invoke(callbacks);
        }
    }

    /**
     * 先把 callbacks 对象复制一份，然后执行 function 回调。
     * 适用场景： function 会占用比较长时间的场景。
     */
    private void copyCallbacksThen(VoidFunction1<TreeMap<Integer, List<C>>> function) {
        // 复制一份
        TreeMap<Integer, List<C>> copy;
        synchronized (callbacks) {
            //noinspection unchecked
            copy = (TreeMap<Integer, List<C>>)callbacks.clone();
        }
        function.invoke(copy);
    }

    private void tryNotifySizeChanged(int oldSize, int newSize) {
        synchronized (mutexForSizeChanged) {
            if (onSizeChangedCallbacks == null) {
                return;
            }
            onSizeChangedCallbacks.notifyAll(it -> it.onSizeChanged(oldSize, newSize));
        }
    }



    /* ======================================================= */
    /* Inner Class                                             */
    /* ======================================================= */

    public interface HowNotify<C> {

        void onNotify(@NonNull C callback);

    }

    public interface OnSizeChangedCallback {

        void onSizeChanged(int oldSize, int newSize);

    }
}
