package com.springboot.laptop.service;

import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.UserRoleEntity;
import com.springboot.laptop.model.dto.AppClientSignUpDto;
import com.springboot.laptop.model.enums.UserRoleEnum;
import com.springboot.laptop.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleService = userRoleService;

    }

    public UserEntity register(AppClientSignUpDto user) throws Exception {
        UserRoleEntity userRole = this.userRoleService.getUserRoleByEnumName(UserRoleEnum.ROLE_USER);
        UserEntity appClient = new UserEntity();
        appClient.setRoles(List.of(userRole));
        appClient.setUsername(user.getUsername());
        appClient.setEmail(user.getEmail());
        appClient.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return userRepository.save(appClient);
    }

    public boolean userExists(String username, String email) {
        Optional<UserEntity> byUsername = this.userRepository.findByUsername(username);
        Optional<UserEntity> byEmail = this.userRepository.findByEmail(email);
        return byUsername.isPresent() || byEmail.isPresent();
    }

    public UserEntity findUserByUserName(String username) throws Exception {
        Optional<UserEntity> user = this.userRepository.findByUsername(username);
        if (user != null) return user.get();
        else throw new Exception("No user found");
    }

}
