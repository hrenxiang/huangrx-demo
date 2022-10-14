package com.huangrx.aop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HuangrxAopApplicationTests {

    @Autowired
    @Qualifier(value = "calculator1")
    private Calculator calculator;

    @Test
    void contextLoads() {

        calculator.add(10,20);

        System.out.println("\n");
    }

//    public static void main(String[] args) {
//        int i = 0;
//        Integer i2 = 0;
//        StringBuilder s = new StringBuilder("0");
//        char[] c = {'a', 'b', 'c'};
//        change(i);
//        change2(i2);
//        change3(s);
//        change4(c[0]);
//        System.out.println(i);
//        System.out.println(i2);
//        System.out.println(s);
//        System.out.println(c[0]);
//    }
//
//    private static void change(int i2) {
//        i2 = 1;
//    }
//    private static void change2(Integer i2) {
//        i2 = 1;
//    }
//    private static void change3(StringBuilder i2) {
//        i2 = new StringBuilder("1");
//    }
//
//    private static void change4(char c) {
//        c = 'h';
//    }

//    public static void main(String args[]){
//        String str="good";
//        char[] ch={'a','b','c'};
//        swap(str,ch);
//        System.out.println("after swap str="+str);
//        System.out.println("after swap ch[0]="+ch[1]);
//
//    }
//
//    public static void swap(String str,char[] ch){
//        str="changed";
//        ch[0]='g';
//    }

    public static void main(String[] args) {
        int i = 1;
        i = i++;
        System.out.println(i);
        int j = i++;
        System.out.println(i);
        int k = i + ++i * i++;
        System.out.println("i = "+i);
        System.out.println("j = "+j);
        System.out.println("k = "+k);
    }

}
