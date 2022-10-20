package com.huangrx.thread.interrupt;

/**
 * run 方法不抛出 InterruptedException 异常，不能使用  thread.interrupt();直接结束
 * 但是，调用thread.interrupt()会给线程打上一个线程中断的标记
 *
 * @author hrenxiang
 * @since 2022-10-20 15:18:37
 */
public class InterruptedExample {

    private static class MyThread2 extends Thread {
        @Override
        public void run() {
            while (!interrupted()) {
                System.out.println("InterruptedExample");
            }
            System.out.println("Thread end");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread2 = new MyThread2();
        thread2.start();
        thread2.interrupt();
        System.out.println("Main run");
    }
}