package com.springboot.laptop.model.dto;

import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.response.ProductResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class OrderDetailsDTO {

    private ProductResponseDTO product;

    private Long quantity;
    private float total;
}
