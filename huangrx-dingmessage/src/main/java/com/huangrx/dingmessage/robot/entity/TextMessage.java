package com.huangrx.dingmessage.robot.entity;

import com.huangrx.dingmessage.robot.type.MessageType;
import org.springframework.util.StringUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * 文本请求消息
 *
 * @author    hrenxiang
 * @since     2022/6/27 12:48
 */
public class TextMessage extends BaseMessage {

    private static final long serialVersionUID = 593514453612444707L;

    /**
     * 文本消息的具体内容
     */
    private String content;

    /**
     * 可以通过群成员的绑定手机号来艾特具体的群成员
     */
    private String[] atMobiles;

    /**
     * 是否艾特所有人
     * 也可以设置isAtAll=true来艾特所有人
     */
    private boolean isAtAll;

    public TextMessage() {
    }

    public TextMessage(String content) {
        this.content = content;
    }

    public TextMessage(String content, String[] atMobiles) {
        this.content = content;
        this.atMobiles = atMobiles;
    }

    public TextMessage(String content, boolean isAtAll) {
        this.content = content;
        this.isAtAll = isAtAll;
    }

    @Override
    public Map toMessageMap() {

        if (StringUtils.isEmpty(this.content) || !MessageType.text.equals(msgtype)) {
            throw new IllegalArgumentException("please check the necessary parameters!");
        }

        HashMap<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("msgtype", this.msgtype);

        HashMap<String, String> textItems = new HashMap<>(8);
        textItems.put("content", this.content);
        resultMap.put("text", textItems);

        HashMap<String, Object> atItems = new HashMap<>(8);
        atItems.put("atMobiles", this.atMobiles);
        atItems.put("isAtAll", this.isAtAll);
        resultMap.put("at", atItems);

        return resultMap;
    }

    @Override
    protected void init() {
        this.msgtype = MessageType.text;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getAtMobiles() {
        return atMobiles;
    }

    public void setAtMobiles(String[] atMobiles) {
        this.atMobiles = atMobiles;
    }

    public boolean getIsAtAll() {
        return isAtAll;
    }

    public void setIsAtAll(boolean isAtAll) {
        this.isAtAll = isAtAll;
    }
}
