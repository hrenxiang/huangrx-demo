package com.huangrx.sms.service;

/**
 * @author    hrenxiang
 * @since     2022/5/10 12:10 PM
 */
public interface SmsService {
    /**
     * 发送短信
     *
     * @param mobile 手机号
     * @param checkCode 验证码
     * @throws Exception 异常
     */
    void send(String mobile) throws Exception;
}