package com.huangrx.concurrent.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BoundedBuffer {
    final Lock lock = new ReentrantLock();
    // condition 依赖于 lock 来产生
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[100];
    int putptr, takeptr, count;

    // 生产
    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.await();  // 队列已满，等待，直到 not full 才能继续生产
            }
            items[putptr] = x;
            if (++putptr == items.length) {
                putptr = 0;
            }
            ++count;
            notEmpty.signal(); // 生产成功，队列已经 not empty 了，发个通知出去
        } finally {
            lock.unlock();
        }
    }

    // 消费
    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await(); // 队列为空，等待，直到队列 not empty，才能继续消费
            }
            Object x = items[takeptr];
            if (++takeptr == items.length) {
                takeptr = 0;
            }
            --count;
            notFull.signal(); // 被我消费掉一个，队列 not full 了，发个通知出去
            return x;
        } finally {
            lock.unlock();
        }
    }

//    public static void main(String[] args) {
//        BoundedBuffer x = new BoundedBuffer();
//        try {
//            x.put(1);
//            x.take();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }

    final boolean acquireQueued() {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
            }
        } finally {

        }
    }


    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Oops! I'm interrupted!");
            }
        }, "thread1");

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("I will interrupt thread1!");
            thread1.interrupt();
            System.out.println("Thread1 interruption done!");
        }, "thread2");
        thread1.start();
        thread2.start();
    }
}