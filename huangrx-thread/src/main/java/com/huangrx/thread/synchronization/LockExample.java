package com.huangrx.thread.synchronization;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hrenxiang
 * @since 2022-10-20 16:02:45
 */
public class LockExample {

    private int i = 0;
    private final Lock lock = new ReentrantLock();

    public void func() {
        lock.lock();
        try {
            for (int j = 0; j < 100; j++) {
                i = i + 1;
            }
            System.out.println(i);
        } finally {
            // 确保释放锁，从而避免发生死锁。
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        LockExample lockExample = new LockExample();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> lockExample.func());
        executorService.execute(() -> lockExample.func());
        //System.out.println(i);
    }
}