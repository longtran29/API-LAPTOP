package com.springboot.laptop.service;

import com.springboot.laptop.model.Address;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.dto.AddressDTO;
import com.springboot.laptop.model.dto.UserDTO;
import com.springboot.laptop.model.dto.request.*;
import com.springboot.laptop.model.jwt.JwtRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserService {
    public UserDTO register(AppClientSignUpDTO user) throws Exception;
    public boolean userExists(String username, String email);

    Object createUserForPrivilege(UserCreationDTO userCreation) throws Exception;

    public UserEntity findUserByUserName(String username);
    public Object sendVerificationEmail(String email) throws IOException;
    public Object updatePassword(NewPasswordRequest newPasswordRequest);
    public UserEntity updateInformation(UpdateInformationDTO userRequestDTO) throws Exception;
    public Object resetPassword(ResetPasswordDTO payload) throws Exception;
    public Object updateStatus(Long customerId, String status);
    public Object deleteCustomer(Long customerId);

    public List<UserDTO> getAll();

    public Object getUserInformation();

    public Object logoutUser(HttpServletRequest request, HttpServletResponse response);

    public Object authenticateUser(JwtRequest jwtRequest);
    public UserEntity addNewAddress(AddressDTO requestAddress);

}
