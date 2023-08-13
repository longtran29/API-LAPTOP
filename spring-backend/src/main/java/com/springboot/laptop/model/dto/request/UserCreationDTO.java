package com.springboot.laptop.model.dto.request;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UserCreationDTO {

    private String username;

    private String password;

    private String email;


    private String name;

    private String phoneNumber;

    private MultipartFile imgURL;

    private List<UserRoleDTO> roles;

    private Boolean enabled;
}

