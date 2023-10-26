package com.springboot.laptop.service;

import com.springboot.laptop.model.Customer;
import com.springboot.laptop.model.dto.AddressDTO;
import com.springboot.laptop.model.dto.UserDTO;
import com.springboot.laptop.model.dto.request.*;
import com.springboot.laptop.model.jwt.JwtRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface AppUserService {
    public Object register(AppClientSignUpDTO user) throws Exception;
    public boolean userExists(String username, String email);

    public Customer findUserByUserName(String username);
    public Object sendVerificationEmail(String email) throws IOException;
    public Object updatePassword(NewPasswordRequest newPasswordRequest);
    public Customer updateInformation(UpdateInformationDTO userRequestDTO, MultipartFile image) throws Exception;
    public Object resetPassword(ResetPasswordDTO payload) throws Exception;
    public Object updateStatus(Long customerId, String status);
    public Object deleteCustomer(Long customerId);
    public Object getUserInformation();

    public Object getUserOrders();

    public Object logoutUser(HttpServletRequest request, HttpServletResponse response);

    public Object authenticateUser(JwtRequest jwtRequest);
    public Customer addNewAddress(AddressDTO requestAddress);

}
