package com.huangrx.lombok.entity;

import lombok.Data;
import lombok.ToString;

/**
 * Student
 *
 * @author hrenxiang
 * @since 2022-04-25 1:54 PM
 */
@Data
@ToString(callSuper = true)
public class Student extends Person {
    private String stuNo;
    private String classNo;
}
