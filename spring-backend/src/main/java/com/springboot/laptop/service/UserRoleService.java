package com.springboot.laptop.service;

import com.springboot.laptop.model.UserRoleEntity;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRoleEntity getUserRoleByEnumName(UserRoleEnum roleEnum) throws Exception {
        Optional<UserRoleEntity> role = userRoleRepository.findByRole(roleEnum);
        if(role != null) {
            return role.get();
        }
        else {
            throw new Exception("User role not found");
        }
    }
}
