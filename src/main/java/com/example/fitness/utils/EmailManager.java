package com.example.fitness.utils;

import com.example.fitness.dto.exception.ExceptionThrowable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(rollbackFor = Exception.class)
public class EmailManager {


    private final JavaMailSender javaMailSender;

    public EmailManager(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendEmail(String to, String subject, String text) throws ExceptionThrowable {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("fcenter974@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);

        }catch (Exception e) {
            throw new ExceptionThrowable(500,e.getMessage());
        }

    }
}
