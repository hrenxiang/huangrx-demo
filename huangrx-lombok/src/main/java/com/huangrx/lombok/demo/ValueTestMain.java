package com.huangrx.lombok.demo;

import com.huangrx.lombok.entity.Teacher;

/**
 * ' @Value '：用在类上，是@Data的不可变形式，相当于为属性添加final声明，只提供getter方法，而不提供setter方法
 *
 * @author hrenxiang
 * @since 2022-04-25 2:07 PM
 */
public class ValueTestMain {
    public static void main(String[] args) {
        Teacher teacher = new Teacher("wxc");
        System.out.println(teacher);
        // teacher.setSalary() 没有setter方法
    }
}
