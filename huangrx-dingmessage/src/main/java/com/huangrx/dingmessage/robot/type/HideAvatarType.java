package com.huangrx.dingmessage.robot.type;

/**
 * ActionCard消息是否隐藏头像枚举值
 *
 * @author    hrenxiang
 * @since     2022/6/27 12:55
 */
public enum HideAvatarType {

    /**
     * 发消息的时候，隐藏机器人头像
     */
    HIDE("隐藏", "1"),

    /**
     * 发消息的时候，显示机器人头像
     */
    UNHIDE("不隐藏，正常显示", "0");

    private final String comment;

    private final String value;

    HideAvatarType(String comment, String value) {
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
