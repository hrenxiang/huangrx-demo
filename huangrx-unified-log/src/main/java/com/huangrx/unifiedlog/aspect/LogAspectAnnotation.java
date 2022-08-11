package com.huangrx.unifiedlog.aspect;

import java.lang.annotation.*;

/**
 * 避免 日志切面执行两次，带有此注解的才会执行日志切面
 *
 * 如果要使用，需要修改切点
 * 为 @Pointcut("execution(* com.folidaymall.merchant.merchantapi.web..*.*(..))
 *    && @annotation(com.folidaymall.merchant.merchantapi.aspect.LogAspectAnnotation)")
 *
 * 修改后，只有方法上添加此注解，日志切面才会执行
 * @author    hrenxiang
 * @since     2022/8/10 15:34
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogAspectAnnotation {
}
