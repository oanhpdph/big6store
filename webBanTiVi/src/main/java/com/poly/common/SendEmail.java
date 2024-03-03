package com.poly.common;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendEmail {

    @Autowired
    private JavaMailSender emailSender;


    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("big6store@6store.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendMessageWithAttachment(byte[] attachmentData, String attachmentName,
                                          String to, String subject, String text) throws MessagingException, IOException {
        // ...

        // Tạo đối tượng MimeMessage và MimeMessageHelper
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Cấu hình thông tin cơ bản
        helper.setFrom("big6store@6store.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        // Tạo một tệp đính kèm từ
        ByteArrayDataSource dataSource = new ByteArrayDataSource(attachmentData, "application/pdf");
        helper.addAttachment(attachmentName, dataSource);

        // Gửi email
        emailSender.send(message);
    }
}
