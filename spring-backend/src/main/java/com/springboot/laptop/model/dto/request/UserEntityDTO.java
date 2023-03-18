package com.springboot.laptop.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntityDTO {

    private String username;
    private String email;
    private String name;
    private String phoneNumber;
}
