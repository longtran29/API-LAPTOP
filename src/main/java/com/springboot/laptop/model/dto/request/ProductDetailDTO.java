package com.springboot.laptop.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class ProductDetailDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String value;
}
