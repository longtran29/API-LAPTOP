package com.springboot.laptop.service.impl;

import com.springboot.laptop.mapper.AccountMapper;
import com.springboot.laptop.model.Account;
import com.springboot.laptop.model.dto.request.UserCreationDTO;
import com.springboot.laptop.model.dto.response.AccountResponseDTO;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.repository.AccountRepository;
import com.springboot.laptop.service.AccountService;
import com.springboot.laptop.service.AmazonS3Service;
import com.springboot.laptop.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final AmazonS3Service amazonS3Service;

    private final UserRoleService userRoleService;
    @Override
    public List<AccountResponseDTO> getAllAccount() {
        return accountRepository.findAll().stream().map(accountMapper::convertEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public Object createUserForPrivilege(UserCreationDTO userCreation, MultipartFile image) {
        Account newUser = new Account();
        newUser.setUsername(userCreation.getUsername());
        newUser.setPassword(userCreation.getPassword());
        newUser.setEmail(userCreation.getEmail());
        newUser.setName(userCreation.getName());
        newUser.setPassword(passwordEncoder.encode(userCreation.getPassword()));
        newUser.setImgURL(amazonS3Service.uploadImage(image));
        newUser.setEnabled(true);
        newUser.setCreatedTimestamp(new Date());
        newUser.setPhoneNumber(userCreation.getPhoneNumber());
        var listRole = userCreation.getRoles().stream().map(role -> {
            try {
                return userRoleService.getUserRoleByEnumName(UserRoleEnum.valueOfCode(role.getName()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        newUser.setRoles(listRole);
        return accountRepository.save(newUser);


    }
}
