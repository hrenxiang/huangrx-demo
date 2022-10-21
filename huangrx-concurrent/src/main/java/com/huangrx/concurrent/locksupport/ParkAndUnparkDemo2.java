package com.huangrx.concurrent.locksupport;

import java.util.concurrent.locks.LockSupport;

/**
 * park 和 unpark顺序不正确
 *
 * @author hrenxiang
 * @since 2022-10-21 15:01:21
 */
public class ParkAndUnparkDemo2 {
    public static void main(String[] args) {
        MyThread3 myThread = new MyThread3(Thread.currentThread());
        myThread.start();
        try {
            // 主线程睡眠3s
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("before park");
        // 获取许可
        LockSupport.park("ParkAndUnparkDemo");
        System.out.println("after park");
    }
}

class MyThread3 extends Thread {
    private Object object;

    public MyThread3(Object object) {
        this.object = object;
    }

    @Override
    public void run() {
        System.out.println("before unpark");        
        // 释放许可
        LockSupport.unpark((Thread) object);
        System.out.println("after unpark");
    }
}