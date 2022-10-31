package com.huangrx.concurrent.blockingqueue;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * ArrayBlockingQueue
 *
 * @author hrenxiang
 * @since 2022-10-28 10:49
 */
public class ArrayBlockingQueueDemo implements Runnable {

    @Override
    public void run() {

    }

    public static void main(String[] args) {

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3, true, Arrays.asList("9","8"));

        for (int i = 0; i < 1; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + "   " + queue.take());
                        System.out.println(Thread.currentThread().getName() + "   " + "take end");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, "take-thread-" + i).start();

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(3000);
//                        queue.put("1");
//                        System.out.println("put 1");
//                        queue.put("2");
//                        System.out.println("put 2");
//                        queue.put("3");
//                        System.out.println("put 3");
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }, "take-thread").start();
        }

    }
}
