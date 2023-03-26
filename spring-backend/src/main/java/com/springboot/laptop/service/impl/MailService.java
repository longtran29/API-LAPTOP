package com.springboot.laptop.service.impl;

import com.springboot.laptop.model.dto.request.ResetPasswordMailDTO;

import javax.mail.MessagingException;
import java.util.concurrent.CompletableFuture;

public interface MailService {

    public CompletableFuture<Void> sendWForgotPassword(ResetPasswordMailDTO payload) throws MessagingException;
}
