package com.huangrx.thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 单线程执行器 <br/>
 * 只有一个线程的线程池,所有提交的任务是顺序执行
 *
 * @author hrenxiang
 * @since 2022-10-20 13:57
 */
public class SingleThreadExecutor {

    public static void main(String[] args) {
        // 报红不影响运行，建议手动创建线程池
        ExecutorService executor = Executors.newSingleThreadExecutor();

        AtomicInteger num = new AtomicInteger(0);

        for (int i = 0; i < 5; i++) {

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " SingleThreadExecutor --> 单线程执行器 --> 1 -->" + num.addAndGet(1));
                }
            });
        }

        for (int i = 0; i < 5; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " SingleThreadExecutor --> 单线程执行器 --> 2 -->" + num.addAndGet(1));
                }
            });
        }
    }
}
