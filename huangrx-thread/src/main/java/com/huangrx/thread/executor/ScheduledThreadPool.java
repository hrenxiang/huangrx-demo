package com.huangrx.thread.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 调度即将执行的任务的线程池
 *
 * @author hrenxiang
 * @since 2022-10-20 14:27
 */
public class ScheduledThreadPool {

    public static void main(String[] args) {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

        AtomicInteger num = new AtomicInteger(0);

        for (int i = 0; i < 15; i++) {

            // 调用 ScheduledExecutorService的 schedule方法，可以指定延迟时间
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " ScheduledThreadPool --> 调度即将执行的任务的线程池 --> 1 -->" + num.addAndGet(1));
                }
                // 延迟10秒后 执行run方法
            }, 10000, TimeUnit.MILLISECONDS);
        }

        for (int i = 0; i < 15; i++) {
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " ScheduledThreadPool --> 调度即将执行的任务的线程池 --> 2 -->" + num.addAndGet(1));
                }
                // 延迟两秒后执行run方法
            }, 2000, TimeUnit.MILLISECONDS);
        }
    }
}
