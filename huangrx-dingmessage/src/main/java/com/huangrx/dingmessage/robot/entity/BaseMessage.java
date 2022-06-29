package com.huangrx.dingmessage.robot.entity;


import com.huangrx.dingmessage.robot.type.MessageType;

import java.io.Serializable;
import java.util.Map;

/**
 * 请求消息的抽象类
 *
 * @author    hrenxiang
 * @since     2022/6/27 13:33
 */
public abstract class BaseMessage implements Serializable {

    private static final long serialVersionUID = -1544859129228200946L;

    public BaseMessage() {
        init();
    }

    /**
     * 消息类型
     */
    protected MessageType msgtype;

    public MessageType getMsgtype() {
        return msgtype;
    }

    /**
     * 初始化MmessageType方法
     */
    protected abstract void init();

    /**
     * 返回Message对象组装出来的Map对象，供后续JSON序列化
     * @return Map
     */
    public abstract Map toMessageMap();

}
