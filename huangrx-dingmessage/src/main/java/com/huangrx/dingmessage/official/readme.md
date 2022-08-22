官方有提供封装好的sdk，com.aliyun:alibaba-dingtalk-service-sdk

自定义一个 发布消息的 事件基类，以方便我们使用 spring的事件监听机制去发送钉钉消息

```java
package com.huangrx.dingmessage.official.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

public class DingTalkEvent extends ApplicationEvent {

    private static final long serialVersionUID = -5105204374136563482L;

    @Getter
    private final String source;

    @Getter
    private final String url;

    @Getter
    @Setter
    private String secret;

    @Getter
    @Setter
    private DingTalkMessageType msgType;

    @Getter
    @Setter
    private String at;

    @Getter
    @Setter
    private Boolean secretEnable;

    @Getter
    @Setter
    private String accessToken;

    public DingTalkEvent(String source, String url) {
        super(source);
        this.source = source;
        this.url = url;
    }

    public DingTalkEvent(String source, String url, String secret, DingTalkMessageType msgType, String at, Boolean secretEnable, String accessToken) {
        super(source);
        this.source = source;
        this.url = url;
        this.secret = secret;
        this.msgType = msgType;
        this.at = at;
        this.secretEnable = secretEnable;
        this.accessToken = accessToken;
    }

}

```

```java
/**
 * 定义消息类型，目前有文本、链接、MarkDown、跳转卡片、消息卡片五种枚举值
 *
 * @author    hrenxiang
 * @since     2022/6/27 12:56
 */
public enum DingTalkMessageType {

    /**
     * 文本类型
     */
    TEXT("text"),

    /**
     * 链接类型
     */
    LINK("link"),

    /**
     * MarkDown类型
     */
    MARKDOWN("markdown"),

    /**
     * 跳转卡片类型
     */
    ACTION_CARD("actionCard"),

    /**
     * 消息卡片类型
     */
    FEED_CARD("feedCard");

    @Getter
    private final String type;

    DingTalkMessageType(String type) {
        this.type = type;
    }

}
```

```java
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
            log.info("DingTalk robot send request url: {}, data: {}");
            DingTalkClient client = new DefaultDingTalkClient(serverUrl);
            OapiRobotSendResponse response = client.execute(request);
            log.info("DingTalk robot send response data: {}");

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

```

```java
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HuangrxTest {
    @Resource
    private ApplicationContext applicationContext;
    @Test
    void testLinkEvent() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setText("百度");
        link.setTitle("nihao");
        link.setMessageUrl("http://www.baidu.com");
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setIsAtAll(true);

        DingTalkEvent event = new DingTalkEvent(objectMapper.writeValueAsString(link),
                "https://oapi.dingtalk.com/robot/send",
                "SECbe637f368b9c88c80d5f1866703448e6ec8addc141588745a567bb0e744e6790",
                DingTalkMessageType.LINK,
                objectMapper.writeValueAsString(at),
                Boolean.TRUE,
                "96d63fd283ecd9f90600ae18bf2b57fafe3fdf1e7293e430ac98143193b367b0");
        applicationContext.publishEvent(event);
    }

    @Test
    void testTextEvent() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        List<String> list = new ArrayList<>();
        list.add("15637613303");
        at.setAtMobiles(list);

        DingTalkEvent event = new DingTalkEvent("我的田田宝贝！我很喜欢你哦，要加油哦！",
                "https://oapi.dingtalk.com/robot/send",
                "SECbe637f368b9c88c80d5f1866703448e6ec8addc141588745a567bb0e744e6790",
                DingTalkMessageType.TEXT,
                objectMapper.writeValueAsString(at),
                Boolean.TRUE,
                "96d63fd283ecd9f90600ae18bf2b57fafe3fdf1e7293e430ac98143193b367b0");
        applicationContext.publishEvent(event);
    }
}
```