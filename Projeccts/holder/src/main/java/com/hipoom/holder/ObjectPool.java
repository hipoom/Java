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

   public ObjectPool(int maxCount, @NonNull Function0<T> creator) {
      this.creator = creator;
      this.maxCount = maxCount;
   }



   /* ======================================================= */
   /* Public Methods                                          */
   /* ======================================================= */

   /**
    * 获取一个新的对象。
    * 如果缓存池中有，那么就从缓存池中获取，否则，调用 creator 新建一个。
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
