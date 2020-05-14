package com.yiyuclub.testmail.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
public class TestMail{
    @Autowired
    public JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    public  String fromuser;

    @Autowired
    public TemplateEngine templateEngine;

    /**
     * 简单发送（纯内容）
     * @param subject
     * @param content
     * @param touser
     * @return
     */
    @GetMapping("sendmailbysimple")
    public String sendMailBySimple(String subject,String content,String touser){
        SimpleMailMessage simpleMailMessage = null;

        try {
            simpleMailMessage = new SimpleMailMessage();
            //邮件标题
            simpleMailMessage.setSubject(subject);
            //邮件内容
            simpleMailMessage.setText(content);
            //邮件接收者
            simpleMailMessage.setTo(touser);
            //邮件时间
            simpleMailMessage.setSentDate(new Date());
            //邮件发送者
            simpleMailMessage.setFrom(fromuser);

            mailSender.send(simpleMailMessage);
        }catch (Exception e){
            return "500";
        }
        return "200";
    }

    /**
     * 发送附件与图片资源
     * @param subject
     * @param content
     * @param touser
     * @param enclosure
     * @return
     */
    @GetMapping("sendmailbyenclosure")
    public String sendMailByEnclosure(String subject,String content,String touser,String enclosure){
        MimeMessageHelper mimeMessageHelper = null;

        try {
            //创建复杂邮件对象
            MimeMessage message = mailSender.createMimeMessage();
            //MimeMessageHelper 是一个邮件配置的辅助工具类，创建时候的 true 表示构建一个 multipart message 类型的邮件
            mimeMessageHelper = new MimeMessageHelper(message, true);
            //邮件标题
            mimeMessageHelper.setSubject(subject);
            //邮件内容
            mimeMessageHelper.setText(content);
            //邮件接收者
            mimeMessageHelper.setTo(touser);
            //邮件时间
            mimeMessageHelper.setSentDate(new Date());
            //邮件发送者
            mimeMessageHelper.setFrom(fromuser);
            //添加附件，以文件展示，E:\test\test-mail\src\main\resources\static\jiagoutu.png
            mimeMessageHelper.addAttachment("jiagoutu.png",new File(enclosure));
            //添加图片资源，会显示出图片的内容
            mimeMessageHelper.addInline("jiagoutu.png",new File(enclosure));
            mailSender.send(message);
        }catch (Exception e){
            return "500";
        }
        return "200";
    }


    /**
     * 使用网页模板发送
     * @param subject
     * @param touser
     * @return
     */
    @GetMapping("sendmailbytemplate")
    public String sendMailByTemplate(String subject,String touser,String username,String code){
        MimeMessageHelper mimeMessageHelper = null;

        try {
            //使用模板处理content
            Context context = new Context();
            //设置网页参数
            context.setVariable("username",username);
            context.setVariable("requirecode",code);
            // 把网页封装为邮件内容
            String process = templateEngine.process("test-hello", context);

            //创建复杂邮件对象
            MimeMessage message = mailSender.createMimeMessage();
            //MimeMessageHelper 是一个邮件配置的辅助工具类，创建时候的 true 表示构建一个 multipart message 类型的邮件
            mimeMessageHelper = new MimeMessageHelper(message, true);
            //邮件标题
            mimeMessageHelper.setSubject(subject);
            //邮件内容
            mimeMessageHelper.setText(process,true);
            //邮件接收者
            mimeMessageHelper.setTo(touser);
            //邮件时间
            mimeMessageHelper.setSentDate(new Date());
            //邮件发送者
            mimeMessageHelper.setFrom(fromuser);



            mailSender.send(message);
        }catch (Exception e){
            return "500";
        }
        return "200";
    }

}
