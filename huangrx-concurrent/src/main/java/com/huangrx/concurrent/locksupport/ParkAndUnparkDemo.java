package com.huangrx.concurrent.locksupport;

import java.util.concurrent.locks.LockSupport;

/**
 * park unpark
 *
 * @author hrenxiang
 * @since 2022-10-21 14:55:01
 */
public class ParkAndUnparkDemo {
    public static void main(String[] args) {
        MyThread2 myThread = new MyThread2(Thread.currentThread());
        myThread.start();
        System.out.println(123);
        // 获取许可
        LockSupport.park("ParkAndUnparkDemo");
        System.out.println("before park");
        System.out.println("after park");
    }
}


class MyThread2 extends Thread {
    private Object object;

    public MyThread2(Object object) {
        this.object = object;
    }

    @Override
    public void run() {
        System.out.println("before unpark");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 获取blocker
        System.out.println("Blocker info " + LockSupport.getBlocker((Thread) object));
        // 释放许可
        LockSupport.unpark((Thread) object);
        // 休眠500ms，保证先执行park中的setBlocker(t, null);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 再次获取blocker
        System.out.println("Blocker info " + LockSupport.getBlocker((Thread) object));

        System.out.println("after unpark");
    }
}