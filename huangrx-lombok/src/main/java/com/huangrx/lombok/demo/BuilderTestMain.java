package com.huangrx.lombok.demo;

import com.huangrx.lombok.entity.Adult;

/**
 * ' @Builder '：用在类、构造器、方法上，为你提供复杂的builder APIs，让你可以像如下方式一样调用Person.builder().name("Adam Savage").city("San Francisco").job("Mythbusters").job("Unchained Reaction").build();
 * ' @Singular ' ：可以为集合类型的参数或字段生成一种特殊的方法, 它采用修改列表中一个元素而不是整个列表的方式，可以是增加一个元素，也可以是删除一个元素。
 *
 * @author hrenxiang
 * @since 2022-04-25 2:21 PM
 */
public class BuilderTestMain {
    public static void main(String[] args) {
        Adult build = Adult.builder()
                .age(10)
                .cd("q")
                .cd("e")
                .cd("w")
                .name("zxc")
                .build();
        System.out.println(build);
    }
}
