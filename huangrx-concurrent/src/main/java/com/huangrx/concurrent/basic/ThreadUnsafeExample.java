package com.huangrx.concurrent.basic;

/**
 * @author hrenxiang
 * @since 2022-08-30 09:55:53
 */
public class ThreadUnsafeExample {

    private int cnt = 1;

    public void add() {
        cnt++;
    }

    public int get() {
        return cnt;
    }

//    public static void main(String[] args) {
//
//        ThreadUnsafeExample example = new ThreadUnsafeExample();
//
//        for (int i = 1; i < 6; i++) {
//            System.out.println("---" + example.get());
//
//            Thread thread = new Thread(example::add, Integer.toString(i));
//            thread.start();
//            System.out.println(thread.getName() + "---" + example.get());
//        }
//        System.out.println(example.get());
//    }

    public static void main(String[] args) {
        int cnt = 1;
        int bb;
        bb = cnt++;
        System.out.println(bb);
    }
}