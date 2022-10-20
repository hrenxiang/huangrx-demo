package com.huangrx.thread.interrupt;

/**
 * 中断方法
 *
 * @author hrenxiang
 * @since 2022-10-20 15:11
 */
public class InterruptedExceptionExample {

    private static class MyThread1 extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                System.out.println("Thread run");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new MyThread1();
        thread1.start();
        thread1.interrupt();
        System.out.println("Main run");
    }
}
