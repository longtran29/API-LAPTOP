package com.springboot.laptop.model.dto.response;

import com.springboot.laptop.model.Address;
import com.springboot.laptop.model.UserCart;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.UserRoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String phoneNumber;
}
