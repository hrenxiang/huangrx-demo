package com.huangrx.lombok.entity;

import lombok.Value;

/**
 * @Value：用在类上，是@Data的不可变形式，相当于为属性添加final声明，只提供getter方法，而不提供setter方法
 *
 * @author hrenxiang
 * @since 2022-04-25 1:54 PM
 */
@Value
public class Teacher extends Person {
    String salary;
}
