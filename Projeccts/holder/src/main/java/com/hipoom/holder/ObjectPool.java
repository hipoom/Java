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

   private final int maxCount;



   /* ======================================================= */
   /* Constructors or Instance Creator                        */
   /* ======================================================= */
   
   /**
    * @param creator 当调用了 {@link #obtain()} 时，如果缓存池中没有其他对象了，则会通过这个 creator 创建新对象。
    *                这个 creator 会在调用 {@link #obtain()} 的线程执行；
    *                这个 creator可能会被多个线程同时调用。
    */
   public ObjectPool(int maxCount, @NonNull Function0<T> creator) {
      this.creator = creator;
      this.maxCount = maxCount;
   }



   /* ======================================================= */
   /* Public Methods                                          */
   /* ======================================================= */

   /**
    * 获取一个新的对象。
    * 如果缓存池中有，那么就从缓存池中获取，否则，调用 {@link #creator} 新建一个。
    */
   public T obtain() {
      synchronized (objects) {
         if (!objects.isEmpty()) {
            return objects.poll();
         }
      }
      return creator.invoke();
   }

   /**
    * 回收一个对象。
    * 如果当前缓存池没有满，则回收到缓存池中，否则放弃回收。
    */
   public void recycle(@Nullable T obj) {
      if (obj == null) {
         return;
      }
      synchronized (objects) {
         // 如果当前已经回收了超过限制数量的对象，就不再回收了。
         if (objects.size() >= maxCount) {
            return;
         }
         objects.offer(obj);
      }
   }

}