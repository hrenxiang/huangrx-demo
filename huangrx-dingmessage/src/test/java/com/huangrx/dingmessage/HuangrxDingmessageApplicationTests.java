package com.huangrx.dingmessage;

import com.huangrx.dingmessage.robot.client.DingTalkRobotClient;
import com.huangrx.dingmessage.robot.entity.MarkdownMessage;
import com.huangrx.dingmessage.robot.entity.TextMessage;
import com.sun.tools.jdi.BooleanValueImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

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
        markdownMessage.setIsAtAll(Boolean.TRUE);
        markdownMessage.setTitle("仕星同学，仕星同学，仕星同学！！！");
        markdownMessage.setText("####  仕星同学，下班了，我们下班了！！！\n" +
                "> 一寸光阴一寸金，寸金难买寸光阴！\n\n" +
                "> 吹响胜利的号角，让我们化悲痛为力量，继续努力！\n" +
                "> 白洞，白色的明天等待着我们，come on！！！\n" +
                "> ![图片](http://5b0988e595225.cdn.sohucs.com/images/20191008/5a3e3957a9394837ad9afcf87a54c5a1.png)\n"  +
                "> ###### 具体情况请点击 [你很帅，最棒！！！](https://lh1.hetaousercontent.com/img/d103c940ce7b6cc3.jpg?thumbnail=true) \n");
        dingTalkRobotClient.sendMarkdownMessage(markdownMessage);
    }

    @Test
    void testValue() {
        System.out.println(number);
    }

}