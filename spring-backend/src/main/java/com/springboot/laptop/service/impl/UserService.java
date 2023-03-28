package com.springboot.laptop.service.impl;

import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.request.AppClientSignUpDTO;
import com.springboot.laptop.model.dto.request.NewPasswordRequest;
import com.springboot.laptop.model.dto.request.ResetPasswordDTO;
import com.springboot.laptop.model.dto.request.UserRequestDTO;
import com.springboot.laptop.model.dto.response.ResponseDTO;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface UserService {
    public UserEntity register(AppClientSignUpDTO user) throws Exception;
    public boolean userExists(String username, String email);
    public UserEntity findUserByUserName(String username);
    public ResponseDTO sendVerificationEmail(String email) throws IOException;
    public UserEntity newPassword(NewPasswordRequest newPasswordRequest);
    public UserEntity updateInformation(UserRequestDTO userRequestDTO);
    public Object resetPassword(ResetPasswordDTO payload) throws Exception;
}
