package com.hipoom.holder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hipoom.function.VoidFunction1;
import com.hipoom.function.VoidFunction2;
import com.hipoom.function.VoidFunction3;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * A Object Holder.
 * Use to observe the process of assigning a value to an object.
 *
 * @author ZhengHaiPeng
 * @since 2025/2/23 14:10
 */
@SuppressWarnings("unused")
public class Holder<T> {

    /* ======================================================= */
    /* Fields */
    /* ======================================================= */

    private boolean isAssigned = false;

    private T value = null;

    /**
     * When calling the {@link #doAfterAssigned} method, if it has not been assigned yet,
     * the callback will be saved in this list.
     * After the first assignment, the callbacks saved in this list will be executed sequentially,
     * and then this list will be clear.
     */
    private final Callbacks<AssignedCallback<T>> doAfterAssignedCallbacks = new Callbacks<>();

    /**
     * The callbacks after the object is assigned a value.
     * This list will be later than {@link #doAfterAssignedCallbacks}.
     */
    private final Callbacks<OnDidSetValueListener<T>> onDidSetValueListeners = new Callbacks<>();


    /**
     * The callbacks when retrieving the object.
     */
    private final Callbacks<OnWillGetValueListener<T>> onWillGetValueListeners = new Callbacks<>();



    /* ======================================================= */
    /* Constructors or Instance Creator */
    /* ======================================================= */

    public Holder() {
    }

    public Holder(T object) {
        this.value = object;
        this.isAssigned = true;
    }



    /* ======================================================= */
    /* Public Methods */
    /* ======================================================= */

    public synchronized boolean isAssigned() {
        return isAssigned;
    }

    public synchronized void set(@Nullable T obj) {
        T oldValue = this.value;
        this.value = obj;

        // notify all pending callbacks
        if (!isAssigned) {
            doAfterAssignedCallbacks.notifyAll(callback -> callback.onAssigned(this, obj));
            doAfterAssignedCallbacks.removeAll();
            isAssigned = true;
        }

        // notify all callbacks for retrieving
        onDidSetValueListeners.notifyAll(listener -> listener.onDidSetValue(this, oldValue, obj));
    }

    public synchronized T get() {
        // notify all callbacks for assigning
        onWillGetValueListeners.notifyAll(listener -> listener.onWillGetValue(this, this.value));
        return this.value;
    }

    /**
     * If the object has already been assigned a value, the callback will be executed immediately.
     * Otherwise, execute after the first assignment.
     */
    public synchronized void doAfterAssigned(@NonNull AssignedCallback<T> callback) {
        if (isAssigned) {
            callback.onAssigned(this, this.value);
        }
        doAfterAssignedCallbacks.add(callback);
    }

    /**
     * If the object has already been assigned a value, the callback will be executed immediately.
     * Otherwise, execute after the first assignment.
     */
    public synchronized void doAfterAssigned(@NonNull VoidFunction1<T> callback) {
        doAfterAssigned((holder, object) -> callback.invoke(object));
    }

    /**
     * If the object has already been assigned a value, the callback will be executed immediately.
     * Otherwise, execute after the first assignment.
     */
    public synchronized void doAfterAssigned(@NonNull Runnable callback) {
        doAfterAssigned((holder, object) -> callback.run());
    }

    /**
     * If the object is not null, the callback will be execute immediately.
     * Otherwise, the callback will not be execute.
     * It should be noted that the {@link #onWillGetValueListeners} will be executed first.
     * So, if the object originally has no value, but is assigned a value in {@link #onWillGetValueListeners},
     * the callback will also be executed.
     */
    public void doIfNotNull(@NonNull VoidFunction1<T> callback) {
        T temp = get();
        if (temp == null) {
            return;
        }
        callback.invoke(temp);
    }

    public synchronized void addOnDidSetListener(@NonNull OnDidSetValueListener<T> listener) {
        onDidSetValueListeners.add(listener);
    }

    public synchronized void removeOnDidSetListener(@NonNull OnDidSetValueListener<T> listener) {
        onDidSetValueListeners.remove(listener);
    }

    public synchronized void addOnWillGetListener(@NonNull OnWillGetValueListener<T> listener) {
        onWillGetValueListeners.add(listener);
    }

    public synchronized void removeOnWillGetListener(@NonNull OnWillGetValueListener<T> listener) {
        onWillGetValueListeners.remove(listener);
    }

    /**
     * Execute after both objects are assigned value.
     */
    public static <A, B> void doAllAssigned(
            @NonNull Holder<A> a,
            @NonNull Holder<B> b,
            @NonNull VoidFunction2<A, B> onAllAssigned
    ) {
        AtomicInteger waitCount = new AtomicInteger(2);
        Runnable onOneAssigned = () -> {
            int temp = waitCount.decrementAndGet();
            if (temp == 0) {
                onAllAssigned.invoke(a.get(), b.get());
            }
        };
        a.doAfterAssigned(onOneAssigned);
        b.doAfterAssigned(onOneAssigned);
    }


    /**
     * Execute after three objects are assigned value.
     */
    public static <A, B, C> void doAllAssigned(
            @NonNull Holder<A> a,
            @NonNull Holder<B> b,
            @NonNull Holder<C> c,
            @NonNull VoidFunction3<A, B, C> onAllAssigned
    ) {
        AtomicInteger waitCount = new AtomicInteger(3);
        Runnable onOneAssigned = () -> {
            int temp = waitCount.decrementAndGet();
            if (temp == 0) {
                onAllAssigned.invoke(a.get(), b.get(), c.get());
            }
        };
        a.doAfterAssigned(onOneAssigned);
        b.doAfterAssigned(onOneAssigned);
        c.doAfterAssigned(onOneAssigned);
    }



    /* ======================================================= */
    /* Inner Class */
    /* ======================================================= */

    /**
     * The callback after the object assigned.
     */
    public interface AssignedCallback<T> {

        /**
         * @param object the value.
         */
        void onAssigned(@NonNull Holder<T> holder, @Nullable T object);

    }

    public interface OnDidSetValueListener<T> {

        /**
         * Execute in the thread corresponding to {@link Holder#set(Object)}.
         */
        void onDidSetValue(@NonNull Holder<T> holder, @Nullable T oldValue, @Nullable T newValue);

    }

    public interface OnWillGetValueListener<T> {

        /**
         * Execute in the thread corresponding to {@link Holder#get()}.
         */
        void onWillGetValue(@NonNull Holder<T> holder, @Nullable T value);

    }

}
