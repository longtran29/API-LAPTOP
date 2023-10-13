package com.springboot.laptop.model.dto.response;

import com.springboot.laptop.model.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {

    private ProductResponseDTO product;
    private Long quantity;
}
