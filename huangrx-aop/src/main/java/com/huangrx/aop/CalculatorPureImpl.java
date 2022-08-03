package com.huangrx.aop;

import org.springframework.stereotype.Component;

@Component(value="calculator1")
public class CalculatorPureImpl implements Calculator {
    @Override
    public void abc() {
        System.out.println("abc");
    }

    @Override
    public int add(int i, int j) {
        int result = i + j;
        System.out.println("方法内部 result = " + result);
        return result;
    }

    @Override
    public int sub(int i, int j) {
        return 0;
    }

    @Override
    public int mul(int i, int j) {
        return 0;
    }

    @Override
    public int div(int i, int j) {
        return 0;
    }
}