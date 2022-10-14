package com.huangrx.thread.basic;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author        hrenxiang
 * @since         2022-09-26 10:26:12
 */
public class MyCallable implements Callable<String> {
    @Override
    public String call() throws InterruptedException {
        Thread.sleep(10000);
        return "实现了callable 接口！";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> ft = new FutureTask<>(new MyCallable());
        Thread thread = new Thread(ft);
        thread.start();
        System.out.println("------" + ft.get());
        System.out.println("------" + 123);
    }
}