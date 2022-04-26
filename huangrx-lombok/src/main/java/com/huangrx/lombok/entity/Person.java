package com.huangrx.lombok.entity;

import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * person
 *
 * @author hrenxiang
 * @since 2022-04-25 1:54 PM
 */
@ToString(exclude = "age")
@EqualsAndHashCode
/**
 * 无参构造
 */
@NoArgsConstructor
/**
 * @NonNull属性作为参数的构造函数
 */
@RequiredArgsConstructor(staticName = "of")
/**
 * 所有参数的构造函数
 */
@AllArgsConstructor
public class Person {
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    @NonNull
    private String name;
    @Getter
    @Setter
    private String address;
    @Getter
    @Setter
    private int age;
    @Getter
    @Setter
    private Date birthday;
    @Getter
    @Setter
    private List<String> tmp;
}
