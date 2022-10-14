package com.huangrx.thread.basic;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

import java.util.*;

/**
 * s
 *
 * @author hrenxiang
 * @since 2022-09-29 10:23
 */
public class ss {

    public static void main(String[] args) {
        List<String> ecoupon = new ArrayList<>();
        List<String> giveCoupons = new ArrayList<>();

        ecoupon.add("a");
        ecoupon.add("b");
        ecoupon.add(null);
        ecoupon.add("c");
        ecoupon.add(null);
        ecoupon.add(null);

        ecoupon.forEach(System.out::println);

        String join = StringUtils.join(ecoupon, ",");
        System.out.println(join);

        String[] split = join.split(",");
        System.out.println(split.length);
        List<String> list = Arrays.asList(split);
        list.forEach(System.out::println);


    }


}
