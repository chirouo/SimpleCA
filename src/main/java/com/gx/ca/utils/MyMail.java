package com.gx.ca.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;


@Component
public class MyMail {
    @Autowired
    private JavaMailSender sender;
    @Value("${spring.mail.username}")
    private String from;

    public void sendFileMail(String to, String subject, String Text, String fileName) throws MessagingException {

        MimeMessage mail = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setSubject(subject);
        helper.setText(Text);
        helper.setTo(to);
        helper.setFrom("13965018720@163.com");
        File file = new File(fileName);
        helper.addAttachment(file.getName(), file);
        sender.send(mail);
    }

    public void sendSimpleMail(String to, String subject, String Text) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setSubject(subject);
        mail.setText(Text);
        mail.setTo(to);
        mail.setFrom("13965018720@163.com");
        sender.send(mail);
    }
}
