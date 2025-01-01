package com.hipoom.holder.watchdog;

import androidx.annotation.NonNull;

import com.hipoom.holder.ObjectPool;
import com.hipoom.holder.watchdog.intermidiate.ISetOnTimeOut;
import com.hipoom.holder.watchdog.intermidiate.ISetWaitingMills;
import com.hipoom.holder.watchdog.intermidiate.Notifier;
import com.hipoom.holder.watchdog.intermidiate.OnTimeOutCallback;
import com.hipoom.holder.watchdog.intermidiate.WatchAble;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ZhengHaiPeng
 * @since 1/1/25 PM6:32
 */
public class WatchDog implements ISetWaitingMills, ISetOnTimeOut, WatchAble, Notifier, Runnable {

    /* ======================================================= */
    /* Static Fields                                           */
    /* ======================================================= */
    
    /**
     * 对象池。
     */
    private static final ObjectPool<WatchDog> sRecycledDogs = new ObjectPool<>(30, WatchDog::new);

    /**
     * 这个线程池，只有一个线程，用于添加延迟任务。
     * 禁止往这个线程池中添加耗时任务。
     */
    private static final ScheduledExecutorService sExecutor = new ScheduledThreadPoolExecutor(1, r -> new Thread(r, "WatchDog"));

    /**
     * 回调线程的编号。
     */
    private static final AtomicInteger sIndex = new AtomicInteger(0);

    /**
     * 回调所执行在的线程池。
     */
    private static final ExecutorService sCallbackExecutor = Executors.newCachedThreadPool(r -> {
        String name = "WatchDog-CallbackThread" + sIndex.incrementAndGet();
        return new Thread(r, name);
    });



    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    /**
     * 超时的回调。
     */
    private OnTimeOutCallback mOnTimeOutCallback = null;

    /**
     * 超时时间。
     */
    private long mTimeout;

    /**
     * 标记当前是否正在 watch.
     */
    private boolean mIsWatching = false;

    /**
     * 超时检测任务提交到线程池后的 Future.
     * 如果在超时时间到达之前，{@link #cancel()} 方法触发了，那么就会调用 mTimeOutFuture 的 cancel 方法。
     */
    private ScheduledFuture<?> mTimeOutFuture;



    /* ======================================================= */
    /* Constructors or Instance Creator                        */
    /* ======================================================= */

    private WatchDog() {}

    @NonNull
    public static ISetWaitingMills obtain() {
        return sRecycledDogs.obtain();
    }



    /* ======================================================= */
    /* Static Public Methods                                   */
    /* ======================================================= */

    /**
     * 等待 maxWaitingMills 毫秒后，如果没有被取消，则在特定的线程池中执行 callback.
     * 这个函数的返回值，是个 Notifier 对象，可以在超时前，调用 {@link Notifier#cancel()} 方法取消。
     *
     * @param maxWaitingMills 最大等待时长。
     * @param callback 超时后的回调，这个回调会执行在一个特定的线程池中。
     * @return notifier 对象，你可以通过 {@link Notifier#cancel()} 方法取消 callback.
     */
    @NonNull
    public static Notifier watch(long maxWaitingMills, @NonNull OnTimeOutCallback callback) {
        return obtain().setMaxWaitingMills(maxWaitingMills).doOnTimeout(callback).watch();
    }



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    @NonNull
    @Override
    public ISetOnTimeOut setMaxWaitingMills(long mills) {
        this.mTimeout = mills;
        return this;
    }

    @NonNull
    @Override
    public WatchAble doOnTimeout(@NonNull OnTimeOutCallback callback) {
        this.mOnTimeOutCallback = callback;
        return this;
    }

    /**
     * 开始监听。
     */
    @Override
    public Notifier watch() {
        if (mIsWatching) {
            throw new IllegalStateException("当前 WatchDog 已经调用过 watch() 方法了，请不要重复调用.");
        }

        // 标记为正在监视
        mIsWatching = true;

        // 在队列中添加超时检测的任务
        mTimeOutFuture = sExecutor.schedule(this, this.mTimeout, TimeUnit.MILLISECONDS);

        return this;
    }

    /**
     * 超过时间还没有取消，会执行到这个方法。
     */
    @Override
    public void run() {
        // 在回调线程池中回调，避免阻塞定时线程。
        sCallbackExecutor.submit(() -> {
            OnTimeOutCallback tCatch = mOnTimeOutCallback;
            if (tCatch != null) {
                tCatch.onTimeOut();
            }
            recycle();
        });
    }

    @Override
    public boolean cancel() {
        boolean isSuccess = mTimeOutFuture.cancel(false);
        recycle();
        return isSuccess;
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private void recycle() {
        this.mOnTimeOutCallback = null;
        this.mIsWatching = false;
        this.mTimeOutFuture = null;
        sRecycledDogs.recycle(this);
    }

}
