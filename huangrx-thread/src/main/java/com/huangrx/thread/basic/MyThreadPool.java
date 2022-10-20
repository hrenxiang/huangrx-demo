package com.huangrx.thread.basic;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池
 *
 * @author hrenxiang
 * @since 2022-09-26 10:40
 */
public class MyThreadPool {

    /**
     * 自定义线程池
     */
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setKeepAliveSeconds(10000);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(20);
        taskExecutor.setThreadNamePrefix("huangrx");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }

    public static void main(String[] args) {
        MyThreadPool myThreadPool = new MyThreadPool();
        // Executor 管理多个异步任务的执行，而无需程序员显式地管理线程的生命周期。
        // 这里的异步是指多个任务的执行互不干扰，不需要进行同步操作。
        Executor executor = myThreadPool.taskExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("使用了线程池来实现方法！");
            }
        });
        System.out.println("你好啊！");
    }
}
