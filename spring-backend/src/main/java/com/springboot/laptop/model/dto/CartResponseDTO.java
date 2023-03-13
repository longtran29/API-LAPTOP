package com.springboot.laptop.model.dto;

import com.springboot.laptop.model.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {

    private ProductEntity product;
    private Long quantity;
}
