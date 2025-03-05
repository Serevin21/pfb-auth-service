package com.serevin.partyforboost.service.impl;

import com.serevin.partyforboost.event.EmailActivationEvent;
import com.serevin.partyforboost.event.EmailResetPasswordEvent;
import com.serevin.partyforboost.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private String emailFrom;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendConfirmationEmail(EmailActivationEvent event) {
        String emailTo = event.getEmail();
        sendEmail(
                "Hey %s\nHere is your email confirmation code:\n%s".formatted(emailTo, event.getCode()),
                "Serevin",
                emailTo
        );
    }

    @Override
    public void sendResetPasswordEmail(EmailResetPasswordEvent event) {
        String emailTo = event.getEmail();
        sendEmail(
                "Hey %s\nHere is your reset password code:\n%s".formatted(emailTo, event.getCode()),
                "Serevin",
                emailTo
        );
    }

    private void sendEmail(String text, String subject, String emailTo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(emailTo);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Value("${spring.mail.username}")
    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

}
