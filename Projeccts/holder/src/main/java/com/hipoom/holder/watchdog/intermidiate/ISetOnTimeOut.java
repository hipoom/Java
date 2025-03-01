package com.hipoom.holder.watchdog.intermidiate;

import androidx.annotation.NonNull;

/**
 * @author ZhengHaiPeng
 * @since 1/1/25 PM6:32
 */
public interface ISetOnTimeOut {

    /**
     * 设置超时的回调。
     *
     * @param callback 到时间后要执行的任务。
     */
    @NonNull
    WatchAble doOnTimeout(@NonNull OnTimeOutCallback callback);

}
