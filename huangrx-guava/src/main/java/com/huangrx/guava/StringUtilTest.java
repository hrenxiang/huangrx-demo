package com.huangrx.guava;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 字符串处理
 *
 * @author hrenxiang
 * @since 2022-05-24 8:52 PM
 */
public class StringUtilTest {

    /**
     * 警告：joiner实例总是不可变的。
     * 用来定义joiner目标语义的配置方法总会返回一个新的joiner实例
     * 这使得joiner实例都是线程安全的，你可以将其定义为static final常量。
     */
    private static final Joiner JOINER = Joiner.on(",");

    @Test
    public void joiner() {

        // 将空字符串转换为输入的默认值
        Joiner joiner = Joiner.on(",").useForNull("d");
        String str = joiner.join("a", "b", "c", null);
        System.out.println(str);

        // 跳过空字符串，不进行拼接
        Joiner joiner1 = Joiner.on(",").skipNulls();
        String str1 = joiner1.join("a", "b", "c", null);
        System.out.println(str1);

        // 如果不用上述两个方法 避免null，则拼接时会报空指针异常

        // 直接拼接list
        Joiner joiner2 = Joiner.on(" | ");
        String str2 = joiner2.join(Arrays.asList("1", "2", "3"));
        System.out.println(str2);
    }

    @Test
    public void splitter() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> splits = splitter.split("a,b,c,d,e");
        splits.forEach(System.out::println);

        // 结果 ””, “a”, “”, “b”， 只忽略了尾部的空字符串
        Iterable<String> splits1 = splitter.split(",a,,b,");
        AtomicInteger i = new AtomicInteger(1);
        splits1.forEach(s -> {
            System.out.println(i);
            i.getAndIncrement();
            System.out.println(s);
        });
        System.out.println(i);

        Iterable<String> splits2 = Splitter.on(',')
                // 移除结果字符串的前导空白和尾部空白
                .trimResults()
                // 从结果中自动忽略空字符串
                .omitEmptyStrings()
                .split("foo,bar,,   qux");
        splits2.forEach(System.out::println);
    }

    @Test
    public void charMatcher() {
        String s = "a b c 1 3 4 9 0 pp f";
        String s1 = CharMatcher.javaIsoControl().removeFrom(s);
        System.out.println(s1);

        // removeFrom: 清除字符串中的abc三个字符
        CharMatcher charMatcher = CharMatcher.anyOf("abc");
        System.out.println(charMatcher.removeFrom("abdceafgabc"));

        // retainFrom: 只保留字符匹配器中的字符
        charMatcher = CharMatcher.is('a');
        System.out.println(charMatcher.retainFrom("abcdefga"));
    }
}
