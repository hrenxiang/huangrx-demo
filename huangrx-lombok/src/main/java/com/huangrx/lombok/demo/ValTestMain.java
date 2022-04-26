package com.huangrx.lombok.demo;

import lombok.val;

import java.util.HashSet;

/**
 * val：用在局部变量前面，相当于将变量声明为 final
 *
 * @author hrenxiang
 * @since 2022-04-25 2:07 PM
 */
public class ValTestMain {

    public static void main(String[] args) {
        val sets = new HashSet<String>();
        //Cannot assign a value to final variable 'sets'
        //sets = new HashSet<String>();
        sets.add("hrx");
        System.out.println(sets.contains("hrx"));
    }

}
