package com.huangrx.jvm;

import java.lang.String;

/**
 * @author hrenxiang
 * @since 2022-11-07 15:16
 */
public class Math {

    private static final long ID = 1L;

    private static int m;
    private int c;

    public int inc() {
        return m + c;
    }

    public static void main(String[] args) {
//        m = 1;
//        Math math = new Math();
//        math.setC(1);
//        int inc = math.inc();
//        System.out.println(inc);
        ClassLoader classLoader = Math.class.getClassLoader();
        System.out.println(classLoader);
        System.out.println(classLoader.getParent());
        System.out.println(classLoader.getParent().getParent());
        System.out.println(String.class.getClassLoader());
        try {
            Class<?> aClass = classLoader.loadClass("java.lang.String");
            System.out.println(aClass.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void setC(int c) {
        this.c = c;
    }
}
