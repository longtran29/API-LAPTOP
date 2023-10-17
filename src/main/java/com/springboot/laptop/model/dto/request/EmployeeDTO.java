package com.springboot.laptop.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
@Getter
@Setter
public class EmployeeDTO {


    private String name;


    private String username;


    private String password;

    private String email;

    private String phoneNumber;

    private String role;


    private boolean status_account;
}
