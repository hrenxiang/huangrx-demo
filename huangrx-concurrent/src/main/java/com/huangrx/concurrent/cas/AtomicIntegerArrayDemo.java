package com.huangrx.concurrent.cas;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author hrenxiang
 * @since 2022-10-21 14:01:51
 */
public class AtomicIntegerArrayDemo {
    public static void main(String[] args) throws InterruptedException {
        AtomicIntegerArray array = new AtomicIntegerArray(new int[] { 0, 0 });
        System.out.println(array);
        System.out.println(array.addAndGet(1, 2));
        System.out.println(array);
        System.out.println(array.getAndSet(1, 5));
        System.out.println(array);
    }
}