package com.springboot.laptop.model.dto;

import com.springboot.laptop.model.Address;
import com.springboot.laptop.model.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String name;
    private String phoneNumber;

    private String imgURL;
    private Boolean enabled;

    private List<Address> addresses = new ArrayList<>();


    private List<Order> orders = new ArrayList<>();
}
