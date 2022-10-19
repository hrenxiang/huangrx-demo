package com.huangrx.dingmessage.official.event;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author        hrenxiang
 * @since         2022-08-22 11:31:09
 */
@Slf4j
@Component
public class DingTalkEventListener {

    public static final Charset UTF_8 = StandardCharsets.UTF_8;

    @Async
    @EventListener
    public void onDingTalkEvent(DingTalkEvent event) {
        String serverUrl = this.getWebhook(event.getUrl(), event.getAccessToken(), event.getSecret(), event.getSecretEnable());
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(event.getMsgType().getType());
        switch (event.getMsgType()) {
            case TEXT:
                OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
                text.setContent(event.getSource());
                request.setText(text);
                break;
            case LINK:
                request.setLink(event.getSource());
                break;
            case ACTION_CARD:
                request.setActionCard(event.getSource());
                break;
            case FEED_CARD:
                request.setFeedCard(event.getSource());
                break;
            default:
                // default markdown
                request.setMsgtype("markdown");
                request.setMarkdown(event.getSource());
                break;
        }
        if (StringUtils.isNotBlank(event.getAt())) {
            request.setAt(event.getAt());
        }
        try {
            // 使用json转成string
            log.info("DingTalk robot send request url: {}, data: {}", serverUrl, request);
            DingTalkClient client = new DefaultDingTalkClient(serverUrl);
            OapiRobotSendResponse response = client.execute(request);
            // 使用json转成string
            log.info("DingTalk robot send response data: {}", response);

        } catch (ApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Gets webhook.
     * access_token、secret_token、secretEnable参数均由外部传入
     *
     * @param accessToken the access token
     * @return the webhook
     */
    public String getWebhook(String urlPrefix, String accessToken, String secret, boolean secretEnable) {
        String url = urlPrefix + "?access_token=" + accessToken;
        //若启用加签加上时间戳跟签名串
        if (secretEnable) {
            Long timestamp = System.currentTimeMillis();
            url += "&timestamp=" + timestamp
                    + "&sign=" + getSign(secret, timestamp);
        }
        log.debug("The url contains sign is {}", url);
        return url;
    }

    /**
     * 参考：<a href="https://ding-doc.dingtalk.com/doc#/serverapi2/qf2nxq/9e91d73c">计算签名</a>
     *
     * @param secret    密钥，机器人安全设置页面，加签一栏下面显示的SEC开头的字符
     * @param timestamp 当前时间戳，毫秒级单位
     * @return 根据时间戳计算后的签名信息
     */
    private static String getSign(String secret, Long timestamp) {
        try {
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(UTF_8));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), UTF_8.toString());
            log.debug("【发送钉钉群消息】获取到签名sign = {}", sign);
            return sign;
        } catch (Exception e) {
            log.error("【发送钉钉群消息】计算签名异常，errMsg = {}", e.getMessage(), e);
            return null;
        }
    }

}
