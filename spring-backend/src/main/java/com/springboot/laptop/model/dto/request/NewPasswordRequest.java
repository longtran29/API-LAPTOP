package com.springboot.laptop.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@Builder
public class NewPasswordRequest {
    @NotBlank(message = "Vui lòng nhập password ")
    private String newPassword;
    @NotBlank(message = "Vui lòng nhập xác nhận password")
    private String retypeNewPassword;
}
