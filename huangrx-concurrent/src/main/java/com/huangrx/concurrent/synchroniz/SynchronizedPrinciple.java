package com.huangrx.concurrent.synchroniz;

/**
 * 原理
 *
 * @author hrenxiang
 * @since 2022-10-21 10:00
 */
public class SynchronizedPrinciple {

    Object object = new Object();
    public void method1() {
        synchronized (object) {

        }
        method2();
    }

    private synchronized static void method2() {

    }
}
