package com.huangrx.thread.basic;

/**
 * @author        hrenxiang
 * @since         2022-09-26 10:19:10
 */
public class MyRunnable implements Runnable {

    @Override
    public void run() {
        // ...
        System.out.println("实现Runnable接口");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new MyRunnable());
        System.out.println(thread.getPriority());
        thread.start();

        // 使用匿名内部类》》
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用资源方法，完成业务逻辑
                System.out.println("使用匿名内部类的方式使用Runnable");
            }
        }, "your thread name").start();
    }
}   