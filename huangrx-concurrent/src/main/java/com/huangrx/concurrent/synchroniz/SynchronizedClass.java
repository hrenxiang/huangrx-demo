package com.huangrx.concurrent.synchroniz;

/**
 * 类锁
 *
 * @author hrenxiang
 * @since 2022-10-20 18:14:25
 */
public class SynchronizedClass implements Runnable {
    static SynchronizedClass instance1 = new SynchronizedClass();
    static SynchronizedClass instance2 = new SynchronizedClass();

    @Override
    public void run() {
        method();
    }

    /**
     * synchronized用在静态方法上，默认的锁就是 当前类的class对象
     */
    public synchronized static void method() {
        System.out.println("我是线程" + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "结束");
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(instance1, "0");
        Thread t2 = new Thread(instance2, "1");
        t1.start();
        t2.start();
    }
}