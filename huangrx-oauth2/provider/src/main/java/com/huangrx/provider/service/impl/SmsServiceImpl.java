package com.huangrx.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.huangrx.provider.domain.sms.SmsProperties;
import com.huangrx.provider.service.RedisService;
import com.huangrx.provider.service.SmsService;
import com.huangrx.provider.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import com.huangrx.provider.domain.redis.CacheConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hrenxiang
 * @since 2022-05-10 12:21 PM
 */
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Value("${validation.code.expiration}")
    private Integer codeExpiration;

    private final SmsProperties smsProperties;
    private final RedisService redisService;

    public SmsServiceImpl(SmsProperties smsProperties, RedisService redisService) {
        this.smsProperties = smsProperties;
        this.redisService = redisService;
    }

    @Override
    public void send(String mobile, String code) throws Exception {
        Map<String, String> headers = new HashMap<>(1);
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + smsProperties.appCode);
        Map<String, String> query = new HashMap<>(1);
        query.put("mobile", mobile);
        query.put("param", "code:"+ code);
        query.put("tpl_id", smsProperties.templateId);
        Map<String, String> body = new HashMap<>(1);

        HttpResponse response = HttpUtils.doPost(smsProperties.host, smsProperties.path, smsProperties.method, headers, query, body);
        System.out.println(response.toString());
        String data = EntityUtils.toString(response.getEntity(), "UTF-8");
        HashMap map = JSON.parseObject(data, HashMap.class);
        Object returnCode = map.get("return_code");

        if ("00000".equals(returnCode)) {
            // 如果验证码正确返回，添加到redis中
            String codeKey = CacheConstants.generateKey(CacheConstants.TEST_KEY, mobile);
            redisService.set(codeKey, code, codeExpiration);
        }

        if ("10001".equals(returnCode)) {
            log.error("手机号码格式错误");
        }

        if (!"00000".equals(returnCode)) {
            log.error("短信发送失败 " + " - returnCode: " + returnCode);
        }
    }
}
