package com.huangrx.thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 缓存线程池 <br/>
 * 1、不按顺序<br/>
 * 2、会复用老的未删除的线程池，也会创建新的线程<br/>
 * 3、线程最大数量是Integer.MAX_VALUE<br/>
 * 4、最大存活时间 60s，如果60s内没用过会删除<br/><br/>
 *
 * @author hrenxiang
 * @since 2022-10-20 14:04
 */
public class CachedThreadPool {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        AtomicInteger num = new AtomicInteger(0);

        for (int i = 0; i < 5; i++) {

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " CachedThreadPool --> 缓存线程池 --> 1 -->" + num.addAndGet(1));
                }
            });
        }

        for (int i = 0; i < 5; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " CachedThreadPool --> 缓存线程池 --> 2 -->" + num.addAndGet(1));
                }
            });
        }
    }
}
