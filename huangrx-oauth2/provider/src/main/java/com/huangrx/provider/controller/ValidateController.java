package com.huangrx.provider.controller;

import cn.hutool.core.util.RandomUtil;
import com.huangrx.provider.domain.response.BaseResponse;
import com.huangrx.provider.exception.ApiException;
import com.huangrx.provider.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author    hrenxiang
 * @since     2022/6/10 21:20
 */
@Slf4j
@RestController
public class ValidateController {

    private final SmsService smsService;

    public ValidateController(SmsService smsService) {
        this.smsService = smsService;
    }

    static class FormUtils {
        public static boolean isMobile(String mobile) {
            return Pattern.matches("^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[189]))\\d{8}$", mobile);
        }

        public static String createCode(Integer len) {
            return RandomUtil.randomString(len);
        }
    }

    @GetMapping("/code/sms")
    public BaseResponse<String> sendSmsCode(@RequestParam("mobile") String mobile) {
        //校验手机号是否合法
        if(StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)){
            return BaseResponse.failed("手机号不合法！");
        }

        //发送短信验证码
        try {
            String code = FormUtils.createCode(6);
            smsService.send(mobile, code);
        } catch (Exception e) {
            throw new ApiException("验证码发送失败！");
        }

        return BaseResponse.success("验证码发送成功！");
    }

    @GetMapping("/login/mobile")
    public BaseResponse<String> loginByMobile(@RequestParam("mobile") String mobile, @RequestParam("code") String code) {
        return null;
    }


}