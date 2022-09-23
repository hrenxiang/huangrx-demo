package com.huangrx.easyexcel.annotation;

import java.lang.annotation.*;

/**
 * @author        hrenxiang
 * @since         2022-09-23 10:20:35
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
@Documented
@Inherited
public @interface SheetName {
    String value() default "";
}