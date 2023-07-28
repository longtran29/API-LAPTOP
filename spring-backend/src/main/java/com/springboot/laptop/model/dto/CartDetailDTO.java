package com.springboot.laptop.model.dto;


import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.response.ProductResponseDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CartDetailDTO {

    private Long id;

    private Long quantity;

    private ProductResponseDTO product;
}
