package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.model.UserRoleEntity;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.repository.UserRoleRepository;
import com.springboot.laptop.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleEntity getUserRoleByEnumName(UserRoleEnum name) throws Exception {
        return userRoleRepository.findByName(name).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.ROLE_NOT_FOUND));
    }
}
