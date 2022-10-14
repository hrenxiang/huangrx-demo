package com.huangrx.thread.basic;

/**
 * @author        hrenxiang
 * @since         2022-09-26 10:32:00
 */
public class MyThread extends Thread {
    @Override
    public void run() {
        // ...
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("继承了Thread类，而thread类其实实现了runnable接口");
    }

    public static void main(String[] args) throws InterruptedException {
        MyThread thread = new MyThread();
        thread.setName("我是继承了Thread类的线程实现方式！！");
        thread.start();
        //Thread.sleep(10000);

        // stop is deprecated，suggest using interrupt
        //thread.stop();
        //thread.interrupt();
        System.out.println(currentThread().getName());
    }
}