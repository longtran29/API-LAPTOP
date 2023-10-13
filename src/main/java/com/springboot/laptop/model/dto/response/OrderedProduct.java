package com.springboot.laptop.model.dto.response;

import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderedProduct {

    private ProductDTO product;

    private Long quantity;


}
