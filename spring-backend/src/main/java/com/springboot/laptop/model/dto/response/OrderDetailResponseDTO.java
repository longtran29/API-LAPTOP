package com.springboot.laptop.model.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OrderDetailResponseDTO {

    private ProductResponseDTO product;

    private Long quantity;

    private float total;
}
