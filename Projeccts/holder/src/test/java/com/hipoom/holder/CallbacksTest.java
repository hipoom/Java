package com.hipoom.holder;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import com.hipoom.function.VoidFunction0;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ZhengHaiPeng
 * @since 2024/8/3 23:11
 */
class CallbacksTest {


    @Test
    public void testAll() {
        priority();
        find();
    }

    @Test
    public void priority() {
        Callbacks<Runnable> callbacks = new Callbacks<>();

        AtomicBoolean isACalled = new AtomicBoolean(false);
        AtomicBoolean isBCalled = new AtomicBoolean(false);

        CountDownLatch latch = new CountDownLatch(2);

        Runnable a = () -> {
            // 标记 A 已经执行了
            isACalled.set(true);
            // 由于 B 的优先级大于 A，所以 A 在执行时，B 一定已经执行过了。
            assert isBCalled.get();
            latch.countDown();
        };

        Runnable b = () -> {
            // 标记 B 已经执行了
            isBCalled.set(true);
            // 由于 B 的优先级大于 A，所以 B 在执行时，A 一定还没有执行过。
            assert !isACalled.get();
            latch.countDown();
        };

        callbacks.add(1, a);
        callbacks.add(2, b);

        callbacks.notifyAll(Runnable::run);

        try {
            latch.await();
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    @Test
    public void find() {
        class Temp {
            String name;
        }

        Callbacks<Temp> callbacks = new Callbacks<>();


        Temp a = new Temp();
        a.name = "a";

        Temp b = new Temp();
        b.name = "b";

        callbacks.add(1, a);
        callbacks.add(1, b);

        List<Temp> found = callbacks.findIf(it -> it.name.equals("b"));
        assert found != null;
        assert found.size() == 1;
        assert found.get(0) == b;


        found = callbacks.findIf(it -> it.name.equals("a"));
        assert found != null;
        assert found.size() == 1;
        assert found.get(0) == a;

        List<Temp> removed = callbacks.removeIf(it -> it.name.equals("a"));
        assert removed.size() == 1;
        assert removed.get(0) == a;
    }

    @Test
    public void testCopyCallbacksThen() throws InterruptedException {
        Callbacks<Runnable> callbacks = new Callbacks<>();

        Runnable a = () -> {
            System.out.println("Task a begin --->");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Task a end <---");
        };

        Runnable b = () -> {
            System.out.println("Task b begin --->");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Task b end <---");
        };

        callbacks.add(1, a);
        callbacks.add(2, b);

        // 模拟两个线程同时 forEach，理应在 < 12 秒的时间内完成。
        new Thread(() -> {
            callbacks.forEach((c) -> {
                c.run();
                return true;
            });
        }).start();

        new Thread(() -> {
            callbacks.forEach((c) -> {
                c.run();
                return true;
            });
        }).start();

        Thread.sleep(12_000);
    }

}