package com.huangrx.dingmessage.robot.entity;

/**
 * 钉钉返回的消息体，可以根据errcode和errmsg
 *
 * @author    hrenxiang
 * @since     2022/6/27 13:38
 */
public class DingTalkResponse {

    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "DingTalkResponse{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}