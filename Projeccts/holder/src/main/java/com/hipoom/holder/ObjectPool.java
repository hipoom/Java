package com.hipoom.holder;

import java.util.LinkedList;
import java.util.Queue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import kotlin.jvm.functions.Function0;

/**
 * @author ZhengHaiPeng
 * @since 2024/8/3 23:39
 */
public class ObjectPool<T> {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    private final Queue<T> objects = new LinkedList<>();

    private final Function0<T> creator;



    /* ======================================================= */
    /* Constructors or Instance Creator                        */
    /* ======================================================= */

    public ObjectPool(@NonNull Function0<T> creator) {
        this.creator = creator;
    }



    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    public T obtain() {
        synchronized (objects) {
            if (objects.isEmpty()) {
                return creator.invoke();
            }

            return objects.poll();
        }
    }

    public void recycle(@Nullable T obj) {
        if (obj == null) {
            return;
        }
        synchronized (objects) {
            objects.offer(obj);
        }
    }

}
