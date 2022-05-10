package com.huangrx.sms.service.impl;

import com.alibaba.fastjson.JSON;
import com.huangrx.sms.common.config.SmsProperties;
import com.huangrx.sms.service.SmsService;
import com.huangrx.sms.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
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

    private final SmsProperties smsProperties;

    public SmsServiceImpl(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    @Override
    public void send(String mobile) throws Exception {
        Map<String, String> headers = new HashMap<>(1);
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + smsProperties.appCode);
        Map<String, String> query = new HashMap<>(1);
        query.put("mobile", mobile);
        query.put("param", "code:"+ 1);
        query.put("tpl_id", smsProperties.templateId);
        Map<String, String> body = new HashMap<>(1);

        HttpResponse response = HttpUtils.doPost(smsProperties.host, smsProperties.path, smsProperties.method, headers, query, body);
        System.out.println(response.toString());
        String data = EntityUtils.toString(response.getEntity(), "UTF-8");
        HashMap map = JSON.parseObject(data, HashMap.class);
        Object code = map.get("return_code");

        if ("10001".equals(code)) {
            log.error("手机号码格式错误");
        }

        if (!"00000".equals(code)) {
            log.error("短信发送失败 " + " - code: " + code);
        }
    }
}
