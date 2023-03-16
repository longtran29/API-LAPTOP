package com.springboot.laptop.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class NewPasswordRequest {
    @NotBlank(message = "Vui lòng nhập password ")
    private String password;
    @NotBlank(message = "Vui lòng nhập xác nhận password")
    private String passwordConfirm;
}
