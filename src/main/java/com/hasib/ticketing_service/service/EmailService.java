package com.hasib.ticketing_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
      SimpleMailMessage message = new SimpleMailMessage();

      message.setFrom("hasibul.hoque@businessaccelerate.com.bd");
      message.setTo(to);
      message.setSubject(subject);
      message.setText(body);

      javaMailSender.send(message);
      System.out.println("Mail sent successfully");
    }
}
