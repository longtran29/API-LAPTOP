package com.springboot.laptop.service;

import com.springboot.laptop.model.dto.request.UserCreationDTO;
import com.springboot.laptop.model.dto.response.AccountResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccountService extends AppUser {

    List<AccountResponseDTO> getAllAccount();

    Object createUserForPrivilege(UserCreationDTO userCreation, MultipartFile imgUser);


    Object updateAccount(Long customerId, UserCreationDTO userCreation, MultipartFile imageUser);
}
