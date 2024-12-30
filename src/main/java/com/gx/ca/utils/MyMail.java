package com.gx.ca.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MyMail {
    @Autowired
    private JavaMailSender sender;
    @Value("${spring.mail.username}")
    private String from;
    public void sendSimpleMail(String to, String subject, String Text) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setSubject(subject);
        mail.setText(Text);
        mail.setTo(to);
        mail.setFrom("13965018720@163.com");
        sender.send(mail);
    }
}
