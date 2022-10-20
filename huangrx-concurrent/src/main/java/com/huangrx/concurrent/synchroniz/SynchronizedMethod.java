package com.huangrx.concurrent.synchroniz;

/**
 * Synchronized 普通同步方法形式
 *
 * @author hrenxiang
 * @since 2022-10-20 18:12:16
 */
public class SynchronizedMethod implements Runnable {
    static SynchronizedMethod instence = new SynchronizedMethod();

    @Override
    public void run() {
        method();
    }

    public synchronized void method() {
        System.out.println("我是线程" + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "结束");
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(instence, "0");
        Thread t2 = new Thread(instence, "1");
        t1.start();
        t2.start();
    }
}