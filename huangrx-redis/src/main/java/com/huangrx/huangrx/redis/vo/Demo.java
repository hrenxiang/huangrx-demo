package com.huangrx.huangrx.redis.vo;

import lombok.Data;

/**
 * 实体
 *
 * @author hrenxiang
 * @since 2022-04-24 10:16 PM
 */
@Data
public class Demo {
    private final String sex = "1";

    private String value1;
    private String value2;
    private String isBoy;

    public void setIsBoy(String isBoy) {
        if (sex.equals(isBoy)) {
            this.isBoy = "男";
        } else {
            this.isBoy = "女";
        }
    }
}
