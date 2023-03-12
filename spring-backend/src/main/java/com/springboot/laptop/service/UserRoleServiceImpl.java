package com.springboot.laptop.service;

import com.springboot.laptop.model.UserRoleEntity;
import com.springboot.laptop.repository.UserRoleRepository;
import com.springboot.laptop.service.impl.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRoleEntity getUserRoleByEnumName(String name) throws Exception {
        Optional<UserRoleEntity> role = userRoleRepository.findByName(name);
        if(role != null) {
            return role.get();
        }
        else {
            throw new Exception("User role not found");
        }
    }
}
