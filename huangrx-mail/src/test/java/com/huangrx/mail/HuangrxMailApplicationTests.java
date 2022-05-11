package com.huangrx.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class HuangrxMailApplicationTests {

    @Autowired
    JavaMailSenderImpl javaMailSender;

    /**
     * 发送普通邮件
     */
    @Test
    void contextLoads() {
        // 构建一个邮件对象
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 设置邮件主题
        simpleMailMessage.setSubject("这是一封测试邮件");
        // 设置邮件发送者
        simpleMailMessage.setFrom("2295701930@qq.com");
        // 设置邮件接收者，可以有多个接收者
        simpleMailMessage.setTo("2295701930@qq.com");
        // 设置邮件抄送人，可以有多个抄送人
        simpleMailMessage.setCc("2295701930@qq.com");
        // 设置隐秘抄送人，可以有多个
        simpleMailMessage.setBcc("2295701930@qq.com");
        // 设置邮件发送日期
        simpleMailMessage.setSentDate(new Date());
        // 设置邮件的正文
        simpleMailMessage.setText("这是测试邮件的正文");
        // 发送邮件
        javaMailSender.send(simpleMailMessage);
    }

    /**
     * 发送带附件的邮件
     */
    @Test
    public void sendAttachFileMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom("2295701930@qq.com");
        helper.setTo("2295701930@qq.com");
        helper.setCc("2295701930@qq.com");
        helper.setBcc("2295701930@qq.com");
        helper.setSentDate(new Date());
        helper.setText("这是测试邮件的正文");
        helper.addAttachment("iShot.jpg",new File("/Users/hrenxiang/Downloads/截图/iShot_2022-05-09_13.30.54.png"));
        javaMailSender.send(mimeMessage);
    }

    /**
     * 发送带图片资源的邮件
     */
    @Test
    public void sendImgResMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom("2295701930@qq.com");
        helper.setTo("2295701930@qq.com");
        helper.setCc("2295701930@qq.com");
        helper.setBcc("2295701930@qq.com");
        helper.setSentDate(new Date());
        helper.setText("<p>hello 大家好，这是一封测试邮件，这封邮件包含两种图片，分别如下</p><p>第一张图片：</p><img src='cid:p01'/><p>第二张图片：</p><img src='cid:p02'/>",true);
        helper.addInline("p01",new FileSystemResource(new File("/Users/hrenxiang/Downloads/截图/iShot_2022-05-09_13.30.54.png")));
        helper.addInline("p02",new FileSystemResource(new File("/Users/hrenxiang/Downloads/截图/iShot_2022-05-09_13.30.54.png")));
        javaMailSender.send(mimeMessage);
    }

    /**
     * 使用 Freemarker 作邮件模板
     */
    @Test
    public void sendFreemarkerMail() throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom("2295701930@qq.com");
        helper.setTo("2295701930@qq.com");
        helper.setCc("2295701930@qq.com");
        helper.setBcc("2295701930@qq.com");
        helper.setSentDate(new Date());
        // 构建 Freemarker 的基本配置
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        // 配置模板位置
        ClassLoader loader = HuangrxMailApplication.class.getClassLoader();
        configuration.setClassLoaderForTemplateLoading(loader, "templates");
        // 加载模板
        Template template = configuration.getTemplate("mail.ftl");
        Map<String, Object> map =  new HashMap<>();
        map.put("title", "Mail Mode");
        StringWriter out = new StringWriter();
        // 模板渲染，渲染的结果将被保存到 out 中 ，将out 中的 html 字符串发送即可
        template.process(map, out);
        helper.setText(out.toString(),true);
        javaMailSender.send(mimeMessage);
    }

    @Autowired
    TemplateEngine templateEngine;

    /**
     * 使用 Thymeleaf 作邮件模板
     */
    @Test
    public void sendThymeleafMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom("2295701930@qq.com");
        helper.setTo("2295701930@qq.com");
        helper.setCc("2295701930@qq.com");
        helper.setBcc("2295701930@qq.com");
        helper.setSentDate(new Date());
        Context context = new Context();
        context.setVariable("title", "huang rx");
        String process = templateEngine.process("mail-thymeleaf.html", context);
        helper.setText(process,true);
        javaMailSender.send(mimeMessage);
    }

}
