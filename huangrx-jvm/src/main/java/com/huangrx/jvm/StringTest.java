package com.huangrx.jvm;

/**
 * @author hrenxiang
 * @since 2022-11-08 09:49
 */
public class StringTest {

    public static void main(String[] args) {
        String a = "hello world";
//        String b = "hello world";
//        System.out.println(a == b);
        String c = new String("hello world");
        String d = new String("hello world");

        System.out.println(c == d);
    }
}
