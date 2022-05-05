package com.dppojo.service.impl;

import com.dppojo.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    //发送人
    @Value("${spring.mail.username}")
    private String from;
    //标题
    @Value("${email.subject}")
    private String subject;
    //正文
    @Value("${email.context.prefix}")
    private String contextPrefix;
    @Value("${email.context.suffix}")
    private String contextSuffix;

    @Override
    public void sendMail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(contextPrefix + code + contextSuffix);
        javaMailSender.send(message);
    }
}

















