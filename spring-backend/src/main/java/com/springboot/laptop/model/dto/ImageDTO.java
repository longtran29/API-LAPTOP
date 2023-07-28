package com.springboot.laptop.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ImageDTO {

    private Long id;

    private String imageProduct;
}
