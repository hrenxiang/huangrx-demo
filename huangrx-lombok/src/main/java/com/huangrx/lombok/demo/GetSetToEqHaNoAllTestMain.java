package com.huangrx.lombok.demo;

import com.huangrx.lombok.entity.Person;

import java.util.ArrayList;
import java.util.Date;

/**
 * ' @Getter/@Setter '：用在属性上，再也不用自己手写setter和getter方法了，还可以指定访问范围
 * ' @ToString '：用在类上，可以自动覆写toString方法，当然还可以加其他参数，例如@ToString(exclude=”id”)排除id属性，或者@ToString(callSuper=true, includeFieldNames=true)调用父类的toString方法，包含所有属性
 * ' @EqualsAndHashCode '：用在类上，自动生成equals方法和hashCode方法
 * ' @NoArgsConstructor ', @RequiredArgsConstructor and @AllArgsConstructor：用在类上，自动生成无参构造和使用所有参数的构造函数以及把所有@NonNull属性作为参数的构造函数，如果指定staticName = “of”参数，同时还会生成一个返回类对象的静态工厂方法，比使用构造函数方便很多
 *
 * ' @Data '：注解在类上，相当于同时使用了@ToString、@EqualsAndHashCode、@Getter、@Setter和@RequiredArgsConstrutor这些注解，对于POJO类十分有用
 * @author hrenxiang
 * @since 2022-04-25 2:21 PM
 */
public class GetSetToEqHaNoAllTestMain {
    public static void main(String[] args) {
        Person person = new Person();

        person.setAddress("hahaha");
        //person.setName("test"); // java: setName(java.lang.String) 在 com.huangrx.lombok.entity.Person 中是 private 访问控制
        person.setAge(12);
        person.setBirthday(new Date());

        System.out.println(person);


        Person person1 = new Person("hahaha","深圳",11,new Date(),new ArrayList<>());
        System.out.println(person1);
        Person wxc = Person.of("wxc");
        System.out.println(wxc);


    }
}
