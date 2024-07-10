package com.hamlet.World.Bank.service.Impl;

import com.hamlet.World.Bank.payload.request.EmailDetails;
import com.hamlet.World.Bank.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    //get the value of the username
    @Value("${spring.mail.username}")
    private String sendEmail;


    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sendEmail);
            simpleMailMessage.setText(emailDetails.getMessageBody());
            simpleMailMessage.setTo(emailDetails.getRecipient());
            simpleMailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new RuntimeException(e);
        }

    }
}
