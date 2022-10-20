package com.huangrx.thread.synchronization;

/**
 * synchronized 实现同步
 *
 * @author hrenxiang
 * @since 2022-10-20 15:32
 */
public class SynchronizedExample {

    /**
     * 普通方法
     */
    public void func () {
        System.out.println("func --> ");
    }

    /**
     * 普通同步方法 锁是当前实例对象
     */
    public synchronized void func1 () {
        System.out.println("func1 --> ");
    }

    /**
     * 静态同步方法 锁是当前类的Class对象
     */
    public synchronized static void fun2() {
        // ...
    }

    /**
     * 同步代码块 锁是Synchonized括号里配置的对象
     */
    public void func3() {
        synchronized (this) {
            // ...
        }
    }


}
