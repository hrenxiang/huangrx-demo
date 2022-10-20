package com.huangrx.thread.collaboration;

/**
 * 现在两个线程，
 * 可以操作初始值为零的一个变量，
 * 实现一个线程对该变量加1，一个线程对该变量减1，
 * 交替，来10轮。
 * <p>
 * 笔记：Java里面如何进行工程级别的多线程编写
 * 1 多线程编程模板（套路）-----上
 * 1.1  线程    操作    资源类
 * 1.2  高内聚  低耦合
 * 2 多线程编程模板（套路）-----中
 * 2.1  判断
 * 2.2  干活
 * 2.3  通知
 * 3 多线程编程模板（套路）-----下
 * 防止虚假唤醒（while）
 *
 * @author hrenxiang
 * @since 2022-10-20 17:46:02
 */
public class NotifyWaitDemo {

    public static void main(String[] args) {
        ShareDataOne shareDataOne = new ShareDataOne();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    shareDataOne.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "AAA").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    shareDataOne.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "BBB").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    shareDataOne.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "CCC").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    shareDataOne.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "DDD").start();
    }

}

class ShareDataOne {
    private Integer number = 0;

    /**
     * 增加1
     */
    public synchronized void increment() throws InterruptedException {
        // 1. 判断
        while (number != 0) {
            this.wait();
        }

        // 2. 干活
        number++;
        System.out.println(Thread.currentThread().getName() + ": " + number);

        // 3. 通知
        this.notifyAll();
    }

    /**
     * 减少1
     */
    public synchronized void decrement() throws InterruptedException {
        // 1. 判断
        while (number != 1) {
            this.wait();
        }

        // 2. 干活
        number--;
        System.out.println(Thread.currentThread().getName() + ": " + number);

        // 3. 通知
        this.notifyAll();
    }
}