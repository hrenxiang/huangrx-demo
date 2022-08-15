package com.huangrx.mystruct.converter.rule;

public class CustomMapping {
    static final String[] SEX = {"女", "男", "未知"};
    public static String sexName(Integer sex) {
        try {
            return SEX[sex];
        } catch (Exception e) {
            assert SEX != null;
            return SEX[2];
        }
    }
    public static String ageLevel(Integer age) {
        if (age < 18) {
            return "少年";
        } else if (age < 30) {
            return "青年";
        } else if (age < 60) {
            return "中年";
        } else {
            return "老年";
        }
    }
}