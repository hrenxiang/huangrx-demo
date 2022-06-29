package com.huangrx.dingmessage.robot.type;

/**
 * ActionCard消息按钮布局枚举值
 *
 * @author    hrenxiang
 * @since     2022/6/27 12:55
 */
public enum ButtonOrientationType {
    /**
     * 水平布局
     */
    HORIZONTAL("水平布局", "1"),
    /**
     * 垂直布局
     */
    VERTICAL("垂直布局", "0");

    private final String comment;

    private final String value;

    ButtonOrientationType(String comment, String value) {
        this.comment = comment;
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public String getValue() {
        return value;
    }
}
