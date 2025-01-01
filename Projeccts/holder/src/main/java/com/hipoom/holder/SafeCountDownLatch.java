package com.hipoom.holder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;

/**
 * 这个类是避免在使用时 CountDownLatch 不注意，在错误的线程发起了等待，导致线程死锁。
 *
 * @author ZhengHaiPeng
 * @since 2024/11/3 23:58
 */
public class SafeCountDownLatch {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    private final CountDownLatch latch;



    /* ======================================================= */
    /* Constructors or Instance Creator                        */
    /* ======================================================= */

    public SafeCountDownLatch(int count) {
        latch = new CountDownLatch(count);
    }



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    public void countDown() {
        latch.countDown();
    }

    /**
     * 等待最多 maxWaitingMills 毫秒。
     * 如果超时，回调 onTimeoutCallback，并且返回值会返回 false.
     *
     * @param maxWaitingMills 最多等待时长，如果超时了，
     * @param onTimeoutCallback 如果超时还没有回调，
     */
    public boolean tryWait(int maxWaitingMills, @Nullable Runnable onTimeoutCallback) {
        final long begin = System.currentTimeMillis();
        while (latch.getCount() >= 0) {
            // 剩余需要等待的时间
            long timeout = maxWaitingMills - (System.currentTimeMillis() - begin);
            try {
                // isReach0 表示计数是否到达 0 了，如果是，则表示不是因为超时。
                boolean isReach0 = latch.await(timeout, TimeUnit.MILLISECONDS);
                if (isReach0) {
                    return true;
                } else {
                    try {
                        if (onTimeoutCallback != null) {
                            onTimeoutCallback.run();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
