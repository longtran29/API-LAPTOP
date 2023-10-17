package com.springboot.laptop.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResetPasswordMailDTO {
    private String html;
    private String from;
    private String to;
    private String text;
    private String subject;
}
