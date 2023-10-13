package com.springboot.laptop.model.dto.response;


import com.springboot.laptop.model.dto.ProductDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OrderDetailResponseDTO {

    private ProductDTO product;

    private Long quantity;

    private float total;
}
