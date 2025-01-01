package com.hipoom.holder.watchdog.intermidiate;

/**
 * 到时间还没有移除时的回调。
 */
public interface OnTimeOutCallback {

    /**
     * 如果超时还没有移除警报，就会回调这个方法。
     * 这个方法执行的线程，是内部一个特定的线程。
     */
    void onTimeOut();

}
