package com.springboot.laptop.model.dto.response;

import com.springboot.laptop.model.Address;
import com.springboot.laptop.model.UserCart;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.UserRoleEntity;

import java.util.List;

public class UserResponseDTO {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String phoneNumber;
    private List<UserRoleEntity> roles;
    private UserCart cart;
    private List<Address> addresses;

    public UserResponseDTO(UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        this.roles = user.getRoles();
        this.cart = user.getCart();
        this.addresses = user.getAddresses();
    }


}
