package com.huangrx.concurrent.cas;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hrenxiang
 * @since 2022-10-21 11:01:07
 */
public class AtomicIntegerDemo {
    private int i = 0;

    /**
     * 使用AtomicInteger 方法就不用加synchronized了，底层是基于 CAS实现的
     */
    private AtomicInteger j = new AtomicInteger(0);

    public int add() {
        i = i + 1;
        //System.out.println(i);
        System.out.println(j.addAndGet(1));
        return i;
    }

    public static void main(String[] args) {
        AtomicIntegerDemo test = new AtomicIntegerDemo();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(test::add);
            executorService.execute(test::add);
            executorService.execute(test::add);
            executorService.execute(test::add);
            executorService.execute(test::add);
            executorService.execute(test::add);
            executorService.execute(test::add);
            executorService.execute(test::add);
            executorService.execute(test::add);
            executorService.execute(test::add);
        }
    }
}