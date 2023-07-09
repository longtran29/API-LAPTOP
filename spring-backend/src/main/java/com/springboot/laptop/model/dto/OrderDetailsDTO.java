package com.springboot.laptop.model.dto;

import com.springboot.laptop.model.ProductEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class OrderDetailsDTO {

    private ProductEntity product;

    private Long quantity;
    private float total;
}
