package com.huangrx.dingmessage;

import com.huangrx.dingmessage.robot.client.DingTalkRobotClient;
import com.huangrx.dingmessage.robot.entity.DingTalkResponse;
import com.huangrx.dingmessage.robot.entity.LinkMessage;
import com.huangrx.dingmessage.robot.entity.MarkdownMessage;
import com.huangrx.dingmessage.robot.entity.TextMessage;
import com.sun.org.glassfish.external.statistics.annotations.Reset;
import com.sun.tools.jdi.BooleanValueImpl;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@SpringBootTest
class HuangrxDingmessageApplicationTests {

    @Value("${huang.ren:567}")
    private Integer number;

    @Autowired
    private DingTalkRobotClient dingTalkRobotClient;

    @Test
    void testText() {
        TextMessage textMessage = new TextMessage();
        textMessage.setContent("别睡了，起来卷");
        textMessage.setAtMobiles(new String[]{"17674002832"});
        dingTalkRobotClient.sendTextMessage(textMessage);
    }

    @Test
    void testMarkdown() {
        MarkdownMessage markdownMessage = new MarkdownMessage();
        String[] strings = new String[1];
        strings[0] = "13598519369";
        markdownMessage.setAtMobiles(strings);
        markdownMessage.setTitle("兄弟们！！！");
        markdownMessage.setText("####  陈佳欣\n" +
                "> ![图片](http://img.duoziwang.com/2021/04/07291624709732.jpg)\n" +
                "> ###### 具体情况请点击 [你很帅，最棒！！！](https://lh1.hetaousercontent.com/img/d103c940ce7b6cc3.jpg?thumbnail=true) \n");

        dingTalkRobotClient.sendMarkdownMessage(markdownMessage);
    }

    @Test
    void testValue() {
        System.out.println(number);

        System.out.println(15 & 2);
    }

    @Test
    void testMarkdownMessage() {
        MarkdownMessage markdownMessage = new MarkdownMessage();
        markdownMessage.setIsAtAll(Boolean.TRUE);
        markdownMessage.setTitle("同学！！！");
        markdownMessage.setText("####  同学你好，同学你在嘛！！！\n" +
                "> 同学你准备好了吗？同学你足够勇敢吗？我们是否能够起飞呢！！！\n\n" +
                "> ![图片](http://5b0988e595225.cdn.sohucs.com/images/20191008/5a3e3957a9394837ad9afcf87a54c5a1.png)");

        dingTalkRobotClient.sendMarkdownMessage(markdownMessage);
    }

    @Test
    void test() {
        LinkMessage linkMessage = new LinkMessage();
        linkMessage.setMessageUrl("https://www.baidu.com");
        linkMessage.setPicUrl("https://www.bbc.com/zhongwen/simp/chinese-news-62388263");
        linkMessage.setText("你好，你的百度！ 不，是你的百度！");
        linkMessage.setTitle("百度是吗？");
        for (int i = 0; i <= 20; i++) {
            dingTalkRobotClient.sendLinkMessage(linkMessage);
        }
    }

    public static String 小芝签名;
    public static String 小芝;
    public static String 小黎签名;
    public static String 小黎;
    public static String 小艾签名;
    public static String 小艾;
    public static String 小洁签名;
    public static String 小洁;
    public static String 小晚签名;
    public static String 小晚;

    static {
        //小芝签名：
        小芝签名 = "SEC0eb67a0433b1190d0a17d4cc654590a3d1eb403a31aa7e68c5138d764cdfa910";
        小芝 = "https://oapi.dingtalk.com/robot/send?access_token=8e0aa9b9a485f5879e310b166f3367db25b7ed122f7a5d36bb2fd39e5cdff66d";

        //小黎签名：
        小黎签名 = "SECc2312e0a5a77a5dcbb4878be7d98702d3dcd33326008209e1ce47d72a0b3e347";
        小黎 = "https://oapi.dingtalk.com/robot/send?access_token=e96861e2c7151c777343a2bcd5cf64d63e1ba8476ae1f05cf2e0abc2ed8d359f";

        //小艾签名：
        小艾签名 = "SEC4f8009e971edcec0ed04327b39ac18a3b5bef14aa1fb9e9199ca770b58cee243";
        小艾 = "https://oapi.dingtalk.com/robot/send?access_token=c85dca090319954d90ca01920b43776fa1549e6b5a814ad3917c6d910650595c";

        //小洁签名：
        小洁签名 = "SEC8b5aac7d5b01b18edcb5a865db4196cd9f3c818e0bb7911b8f2e0dac860e9258";
        小洁 = "https://oapi.dingtalk.com/robot/send?access_token=381c6636b9e33af755d310f997ec667e00728c6a54791cc15828d9345ad1da24";

        //小晚签名：
        小晚签名 = "SEC2afceaa44f3c985b1c19c24ea9f2a8dd64b41d8fedcae95212a0e91a81952c02";
        小晚 = "https://oapi.dingtalk.com/robot/send?access_token=a59f1e66ec69b4f13ddb4b8ee99dc94ac5ae19fbe0a0558f14635f01a9715a21";

        Long timestamp = System.currentTimeMillis();

        小芝 += "&timestamp=" + timestamp + "&sign=" + getSign(小芝签名, timestamp);
        小黎 += "&timestamp=" + timestamp + "&sign=" + getSign(小黎签名, timestamp);
        小艾 += "&timestamp=" + timestamp + "&sign=" + getSign(小艾签名, timestamp);
        小洁 += "&timestamp=" + timestamp + "&sign=" + getSign(小洁签名, timestamp);
        小晚 += "&timestamp=" + timestamp + "&sign=" + getSign(小晚签名, timestamp);
    }

    private static String getSign(String secret, Long timestamp) {
        try {
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), StandardCharsets.UTF_8.toString());
            return sign;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        LinkMessage linkMessage = new LinkMessage();
        linkMessage.setMessageUrl("https://www.baidu.com");
        linkMessage.setPicUrl("https://lh1.hetaousercontent.com/img/acdac092cbd73d44.jpg?thumbnail=true");
        linkMessage.setText("你好，你的百度！ 不，是你的百度！");
        linkMessage.setTitle("百度是吗？");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<>(linkMessage.toMessageMap(), headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(小芝, entity, DingTalkResponse.class);
        restTemplate.postForObject(小黎, entity, DingTalkResponse.class);
        restTemplate.postForObject(小艾, entity, DingTalkResponse.class);
        restTemplate.postForObject(小洁, entity, DingTalkResponse.class);
        restTemplate.postForObject(小晚, entity, DingTalkResponse.class);
    }

}