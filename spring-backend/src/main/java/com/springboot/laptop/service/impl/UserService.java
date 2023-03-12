package com.springboot.laptop.service.impl;

import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.AppClientSignUpDto;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface UserService {
    public UserEntity register(AppClientSignUpDto user) throws Exception;
    public boolean userExists(String username, String email);
    public UserEntity findUserByUserName(String username) throws Exception;
    public void sendVerificationEmail(String email, String siteURL) throws UnsupportedEncodingException, MessagingException, MessagingException, UnsupportedEncodingException;
    public UserEntity newPassword(String email, String password, String passwordConfirm);

}
