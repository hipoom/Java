package com.hipoom.holder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.hipoom.holder.TreeNode.Predication;
import com.hipoom.holder.TreeNode.TreeNodeVisitor;
import org.junit.jupiter.api.Test;

/**
 * @author ZhengHaiPeng
 * @since 2025/2/20 15:15
 */
class TreeNodeTest {

    static class TodoItem extends TreeNode<TodoItem> {

        public TodoItem(int d) {
            data = d;
        }

        public TodoItem(int d, TodoItem... children) {
            this.data = d;
            this.children.addAll(Arrays.asList(children));
        }

        int data;
        List<TodoItem> children = new ArrayList<>();

        @Override
        List<? extends TreeNode<TodoItem>> getChildren() {
            return children;
        }

        @Override
        TodoItem get() {
            return this;
        }
    }



    @Test
    public void test() {
        //        0
        //      /   \
        //     1     2
        //    / \   / \
        //   3   4 5   6
        //  /           \
        // 7             8

        TodoItem tree = new TodoItem(
            0,
            new TodoItem(
                1,
                new TodoItem(
                    3,
                    new TodoItem(
                        7
                    )
                ),
                new TodoItem(4)
            ),
            new TodoItem(
                2,
                new TodoItem(
                    5
                ),
                new TodoItem(
                    6,
                    new TodoItem(
                        8
                    )
                )
            )
        );

        List<Pair> dfsExpects = new LinkedList<>();
        dfsExpects.add(new Pair(0, 0));
        dfsExpects.add(new Pair(1, 1));
        dfsExpects.add(new Pair(2, 3));
        dfsExpects.add(new Pair(3, 7));
        dfsExpects.add(new Pair(2, 4));
        dfsExpects.add(new Pair(1, 2));
        dfsExpects.add(new Pair(2, 5));
        dfsExpects.add(new Pair(2, 6));
        dfsExpects.add(new Pair(3, 8));
        AtomicInteger index = new AtomicInteger(0);
        tree.dfs(true, (depth, node) -> {
            Pair expect = dfsExpects.get(index.getAndIncrement());
            assert expect.depth == depth;
            assert expect.value == node.data;
            return true;
        });
        System.out.println("深度遍历验证通过.");

        List<Pair> bfsExpects = new LinkedList<>();
        bfsExpects.add(new Pair(0, 0));
        bfsExpects.add(new Pair(1, 1));
        bfsExpects.add(new Pair(1, 2));
        bfsExpects.add(new Pair(2, 3));
        bfsExpects.add(new Pair(2, 4));
        bfsExpects.add(new Pair(2, 5));
        bfsExpects.add(new Pair(2, 6));
        bfsExpects.add(new Pair(3, 7));
        bfsExpects.add(new Pair(3, 8));
        index.set(0);
        tree.bfs(true, (depth, node) -> {
            Pair expect = bfsExpects.get(index.getAndIncrement());
            assert expect.depth == depth;
            assert expect.value == node.data;
            return true;
        });
        System.out.println("广度遍历验证通过.");

        tree.bfs(false, (depth, node) -> {
            System.out.println("depth = " + depth + ", value = " + node.data);
            return true;
        });

        TodoItem item = tree.findOne((depth, todoItem) -> (depth == 0));
        assert item.data == 0;

        List<TodoItem> items = tree.findAll((depth, todoItem) -> (depth == 2));
        assert items.size() == 4;
        assert items.get(0).data == 3;
        assert items.get(1).data == 4;
        assert items.get(2).data == 5;
        assert items.get(3).data == 6;
    }


    static class Pair {
        int depth;
        int value;

        public Pair(int depth, int value) {
            this.depth = depth;
            this.value = value;
        }
    }

}
