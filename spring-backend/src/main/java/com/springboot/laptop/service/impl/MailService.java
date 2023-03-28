package com.springboot.laptop.service.impl;

import com.springboot.laptop.model.dto.request.OrderInfoMail;
import com.springboot.laptop.model.dto.request.ResetPasswordMailDTO;
import com.springboot.laptop.model.dto.response.OrderCompleted;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.concurrent.CompletableFuture;

@Service
public interface MailService {

    public CompletableFuture<Void> sendWForgotPassword(ResetPasswordMailDTO payload) throws MessagingException;

    public CompletableFuture<Void> send4OrderInfo(OrderInfoMail order) throws MessagingException;
}
