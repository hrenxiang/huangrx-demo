package com.huangrx.dingmessage.robot.exception;

/**
 * 自定义报错
 *
 * @author    hrenxiang
 * @since     2022/6/27 13:28
 */
public class DingTalkException extends RuntimeException {

    private static final long serialVersionUID = -2968643330035174935L;

    /**
     * 错误码
     */
    private Integer errCode;

    public DingTalkException() {
        super();
    }

    public DingTalkException(Integer errCode, String errorMsg) {
        super(errorMsg);
        this.errCode = errCode;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public String getErrorMsg() {
        return this.getMessage();
    }

}
