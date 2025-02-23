package com.hipoom.holder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hipoom.function.Function0;

/**
 * @author ZhengHaiPeng
 * @since 2025/2/23 14:39
 */
public class Lazy {

    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    public static <T> Holder<T> by(@NonNull Function0<T> creator) {
        Holder<T> holder = new Holder<>();
        holder.addOnWillGetListener(new Holder.OnWillGetValueListener<T>() {

            @Override
            public void onWillGetValue(@NonNull Holder<T> holder, @Nullable T value) {
                if (holder.isAssigned()) {
                    return;
                }

                holder.set(creator.invoke());
                holder.removeOnWillGetListener(this);
            }
        });
        return holder;
    }

}
