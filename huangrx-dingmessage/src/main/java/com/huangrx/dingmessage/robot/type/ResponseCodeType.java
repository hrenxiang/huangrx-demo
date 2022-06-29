package com.huangrx.dingmessage.robot.type;

/**
 * 自定义接口返回类型
 *
 *  @author    hrenxiang
 * @since     2022/6/27 12:56
 */
public enum ResponseCodeType {

    /**
     * 消息发送成功
     */
    OK(0),
    /**
     * 自定义相应码，非钉钉返回码值
     */
    UNKNOWN(9999);

    private final Integer value;

    ResponseCodeType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
