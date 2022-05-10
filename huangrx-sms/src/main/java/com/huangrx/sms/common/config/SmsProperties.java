package com.huangrx.sms.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author    hrenxiang
 * @since     2022/5/10 12:08 PM
 *
 * 注意prefix要写到最后一个 "." 符号之前
 */
@Data
@Component
@ConfigurationProperties(prefix="aliyun.sms")
public class SmsProperties {
    public String appSecret;
    public String appCode;
    public String host;
    public String path;
    public String method;
    public String templateId;
}