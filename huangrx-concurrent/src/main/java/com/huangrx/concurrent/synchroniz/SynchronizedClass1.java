package com.huangrx.concurrent.synchroniz;

/**
 * 类锁，同步代码块锁同一个对象
 *
 * @author hrenxiang
 * @since 2022-10-20 18:16:29
 */
public class SynchronizedClass1 implements Runnable {
    static SynchronizedClass1 instence1 = new SynchronizedClass1();
    static SynchronizedClass1 instence2 = new SynchronizedClass1();

    @Override
    public void run() {
        // 所有线程需要的锁都是同一把
        synchronized(SynchronizedClass1.class){
            System.out.println("我是线程" + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "结束");
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(instence1);
        Thread t2 = new Thread(instence2);
        t1.start();
        t2.start();
    }
}