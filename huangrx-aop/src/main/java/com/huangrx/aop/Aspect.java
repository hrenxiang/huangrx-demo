package com.huangrx.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@org.aspectj.lang.annotation.Aspect
@Component
public class Aspect {
    @Before(value = "execution(public int com.huangrx.aop.CalculatorPureImpl.*(..))")
    public void beforeLog(){
        System.out.println("[AOP前置通知] 方法开始了");
    }

    @After(value = "execution(public int com.huangrx.aop.CalculatorPureImpl.*(..))")
    public void afterLog(){
        System.out.println("[AOP后置通知] 方法最终结束了");
    }

    @AfterReturning(value = "execution(public int com.huangrx.aop.CalculatorPureImpl.*(..))")
    public void afterReturningLog(){
        System.out.println("[AOP正常运行结束] 方法结束了");
    }

    @AfterThrowing(value = "execution(public int com.huangrx.aop.CalculatorPureImpl.*(..))")
    public void afterThrowingLog(){
        System.out.println("[AOP运行异常结束了] 方法结束了了");
    }
}