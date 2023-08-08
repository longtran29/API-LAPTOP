package com.springboot.laptop.model.dto.request;

import lombok.Data;

@Data
public class UpdateInformationDTO {

    private String username;
    private String email;
    private String name;
    private String phoneNumber;

    private String imgURL;
}
