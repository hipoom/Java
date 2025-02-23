package com.hipoom.holder;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author ZhengHaiPeng
 * @since 2025/2/20 14:51
 */
@SuppressWarnings("unused")
public abstract class TreeNode<T> {

    /* ======================================================= */
    /* Public Methods                                          */
    /* ======================================================= */

    abstract List<? extends TreeNode<T>> getChildren();

    abstract T get();

    /**
     * 广度优先遍历以当前节点为根节点的树。
     *
     * @param needRoot 是否需要访问根节点。 如果是 false, 则会跳过当前节点。
     * @param visitor 返回 true 继续遍历下一个, 返回 false 停止遍历。
     */
    public void bfs(boolean needRoot, TreeNodeVisitor<T> visitor) {
        Queue<TreeNode<T>> queue = new LinkedList<>();
        queue.offer(this);

        int currentDepth = 0;

        while (!queue.isEmpty()) {
            int layerSize = queue.size();

            for (int i = 0; i < layerSize; i++) {
                TreeNode<T> node = queue.poll();
                if (node == null) {
                    continue;
                }

                // 把当前节点的子节点，加入到遍历序列中
                List<? extends TreeNode<T>> children = node.getChildren();
                if (children != null) {
                    for (TreeNode<T> child : children) {
                        queue.offer(child);
                    }
                }

                // 如果不需要遍历根节点，忽略
                if (!needRoot && node == this) {
                    continue;
                }

                // 回调外部
                boolean needContinue = visitor.onVisit(currentDepth, node.get());
                if (!needContinue) {
                    return;
                }
            }

            currentDepth++;
        }
    }

    /**
     * 深度优先遍历以当前节点为根节点的树。
     *
     * @param needRoot 是否需要访问根节点。 如果是 false, 则会跳过当前节点。
     * @param visitor 返回 true 继续遍历下一个, 返回 false 停止遍历。
     */
    public void dfs(boolean needRoot, TreeNodeVisitor<T> visitor) {
        dfs(0, needRoot, visitor);
    }

    /**
     * 寻找满足条件的数据。
     */
    public T findOne(Predication<T> predication) {
        List<T> holder = new LinkedList<>();
        bfs(true, (depth, data) -> {
            boolean isMatched = predication.isMatch(depth, data);
            if (isMatched) {
                holder.add(data);
                return false;
            }
            return true;
        });

        if (holder.isEmpty()) {
            return null;
        }

        return holder.get(0);
    }

    public List<T> findAll(Predication<T> predication) {
        List<T> holder = new LinkedList<>();
        bfs(true, (depth, data) -> {
            boolean isMatched = predication.isMatch(depth, data);
            if (isMatched) {
                holder.add(data);
            }
            return true;
        });
        return holder;
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    /**
     * 深度优先遍历以当前节点为根节点的树。
     *
     * @param needRoot 是否需要访问根节点。 如果是 false, 则会跳过当前节点。
     * @param visitor 返回 true 继续遍历下一个, 返回 false 停止遍历。
     */
    private void dfs(int baseDepth, boolean needRoot, TreeNodeVisitor<T> visitor) {
        if (needRoot) {
            boolean needContinue = visitor.onVisit(baseDepth, get());
            if (!needContinue) {
                return;
            }
        }

        List<? extends TreeNode<T>> children = getChildren();
        for (TreeNode<T> child : children) {
            child.dfs(baseDepth + 1, true, visitor);
        }
    }



    /* ======================================================= */
    /* Inner Class                                             */
    /* ======================================================= */

    public interface TreeNodeVisitor<Data> {

        /**
         * @param depth 相对于根节点的深度。 其中，根节点的深度是 0.
         *
         * @return true: 继续往下遍历； false: 结束遍历。
         */
        boolean onVisit(int depth, Data data);

    }

    public interface Predication<Data> {

        boolean isMatch(int depth, Data data);

    }
}