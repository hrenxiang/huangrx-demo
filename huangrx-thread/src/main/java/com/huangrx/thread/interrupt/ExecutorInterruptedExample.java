package com.huangrx.thread.interrupt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * executor中断操作
 *
 * @author hrenxiang
 * @since 2022-10-20 15:25
 */
public class ExecutorInterruptedExample {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        // 中断所有线程
//        executorService.execute(() -> {
//            try {
//                Thread.sleep(2000);
//                System.out.println("Thread run");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        executorService.shutdownNow();
//        System.out.println("Main run");

        // 只中断一个线程 先使用 executorService的 submit方法提交一个线程，会返回一个future对象，调用future的cancel方法即可
        Future<?> future = executorService.submit(() -> {
            System.out.println("Thread run");
        });
        future.cancel(true);
        System.out.println("Main run");
    }
}
