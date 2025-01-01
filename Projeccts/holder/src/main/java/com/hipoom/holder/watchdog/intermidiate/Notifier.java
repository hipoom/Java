package com.hipoom.holder.watchdog.intermidiate;

/**
 * @author ZhengHaiPeng
 * @since 1/1/25 PM6:32
 */
public interface Notifier {

    /**
     * 取消看门狗到时间的回调。
     *
     * @return true: 取消时，并没有执行过回调；  false: 取消时，已经执行过回调了。
     * 例如，当 超时时间 设置的是 100ms, 而到 150ms 时，才调用 cancel(). 在这种情况，当 100ms 时，已经执行过回调了，
     * 所以在 cancel() 时，其实没有真的 cancel，此时会返回 false.
     */
    boolean cancel();

}
