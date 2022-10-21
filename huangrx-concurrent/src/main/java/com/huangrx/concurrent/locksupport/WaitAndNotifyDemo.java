package com.huangrx.concurrent.locksupport;

/**
 * wait notify notifyAll
 *
 * @author hrenxiang
 * @since 2022-10-21 14:50:37
 */
public class WaitAndNotifyDemo {
    public static void main(String[] args) throws InterruptedException {
        MyThread myThread = new MyThread();
        synchronized (myThread) {
            try {
                myThread.start();
                System.out.println("before wait");
                // 阻塞主线程
                myThread.wait();
                System.out.println("after wait");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MyThread extends Thread {

    @Override
    public void run() {
        synchronized (this) {
            try {
                System.out.println("before notify");
                Thread.sleep(3000);
                notify();
                System.out.println("after notify");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}