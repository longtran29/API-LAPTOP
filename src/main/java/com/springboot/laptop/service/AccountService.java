package com.springboot.laptop.service;

import com.springboot.laptop.model.dto.request.UserCreationDTO;
import com.springboot.laptop.model.dto.response.AccountResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccountService {

    List<AccountResponseDTO> getAllAccount();

    Object createUserForPrivilege(UserCreationDTO userCreation, MultipartFile imgUser);

    Object updateStatus(Long customerId, String status);
}
