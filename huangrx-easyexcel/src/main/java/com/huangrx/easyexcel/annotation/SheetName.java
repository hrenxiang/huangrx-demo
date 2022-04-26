package com.huangrx.easyexcel.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
@Documented
@Inherited
public @interface SheetName {
    String value() default "";
}