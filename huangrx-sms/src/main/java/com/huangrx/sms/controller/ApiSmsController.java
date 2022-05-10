package com.huangrx.sms.controller;

import com.huangrx.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

/**
 * @author    hrenxiang
 * @since     2022/5/10 12:52 PM
 */
@RestController
@CrossOrigin
@Slf4j
public class ApiSmsController {

    private final SmsService smsService;

    public ApiSmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @GetMapping("send/{mobile}")
    public String getCode(@PathVariable String mobile) throws Exception {

        //校验手机号是否合法
        if(StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)){
            return "手机号不合法";
        }

        //发送短信
        smsService.send(mobile);

        return "短信发送成功";
    }

    static class FormUtils {
        public static boolean isMobile(String mobile) {
            return Pattern.matches("^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[189]))\\d{8}$", mobile);
        }
    }
}