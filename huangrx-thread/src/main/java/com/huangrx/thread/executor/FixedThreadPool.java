package com.huangrx.thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定线程数的线程池<br/>
 * 1、拥有固定线程数 <br/>
 * 2、如果没有任务执行，线程会一直等待 <br/>
 * 3、线程池大小可以自定义，但是最大固定大小是 Integer.MAX_VALUE<br/>
 *
 * @author hrenxiang
 * @since 2022-10-20 14:20
 */
public class FixedThreadPool {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(Integer.MAX_VALUE);

        AtomicInteger num = new AtomicInteger(0);

        for (int i = 0; i < 15; i++) {

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " FixedThreadPool --> 固定线程数的线程池 --> 1 -->" + num.addAndGet(1));
                }
            });
        }

        for (int i = 0; i < 15; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " FixedThreadPool --> 固定线程数的线程池 --> 2 -->" + num.addAndGet(1));
                }
            });
        }
    }
}
