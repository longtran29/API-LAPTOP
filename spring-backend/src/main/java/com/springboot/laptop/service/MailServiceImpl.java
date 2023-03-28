package com.springboot.laptop.service;

import com.springboot.laptop.model.dto.request.OrderInfoMail;
import com.springboot.laptop.model.dto.request.ResetPasswordMailDTO;
import com.springboot.laptop.model.dto.response.OrderCompleted;
import com.springboot.laptop.service.impl.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender sender;
    @Override
    public CompletableFuture<Void> sendWForgotPassword(ResetPasswordMailDTO payload) throws MessagingException {

        MimeMessage mm = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mm, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                helper.setFrom(payload.getFrom());
                helper.setTo(payload.getTo());
                helper.setText(payload.getText(), true);
                helper.setSubject(payload.getSubject());
                sender.send(mm);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<Void> send4OrderInfo(OrderInfoMail orderInfo) throws MessagingException {
        MimeMessage mm = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mm, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                helper.setFrom(orderInfo.getFrom());
                helper.setTo(orderInfo.getTo());
                helper.setText(orderInfo.getText(), true);
                helper.setSubject(orderInfo.getSubject());
                sender.send(mm);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return future;
    }
}
