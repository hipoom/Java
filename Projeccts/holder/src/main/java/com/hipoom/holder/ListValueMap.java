package com.hipoom.holder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 我们经常会遇到这种场景：
 * 我们需要一个 Map， Map 的 value 是一个 List。
 * 我们每次添加元素时，是往 value 的 list 中添加一个元素。
 * 如果 list 不存在，则新建一个 list，再把 item 添加到 list 中。
 * 像这样：
 * <p>
 *          Map<String, List<String>> map = new HashMap<>();
 *          List<String> list = map.get("key");
 *          if (list == null) {
 *               list = new ArrayList<>();
 *               map.put("key", list);
 *          }
 *          list.add("value");
 * <p>
 * 这个类，就是为了解决这种情况，有了这个类，可以这样实现：
 * <p>
 *          ListValueMap<String, String> map = new ListValueMap();
 *
 * @author ZhengHaiPeng
 * @since 2025/1/12 15:32
 */
public class ListValueMap<KEY, VALUE_ITEM> extends HashMap<KEY, List<VALUE_ITEM>> {

    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    /**
     * 将 item 添加到 key 对应的列表中。
     * 如果 key 对应的 value 不存在，则会在内部添加一个 {@code ArrayList<VALUE_ITEM>}.
     *
     * 这不是一个线程安全的方法。
     */
    public void insert(@Nullable KEY key, @Nullable VALUE_ITEM item) {
        List<VALUE_ITEM> list = get(key);
        if (list == null) {
            list = new ArrayList<>();
            list.add(item);
            put(key, list);
            return;
        }
        list.add(item);
    }

}
