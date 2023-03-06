package com.springboot.laptop.model.dto;


import com.springboot.laptop.model.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long prod_id;
    private String primaryImage;

    private String prodName;
    private boolean enabled;
    private float original_price;
    private String brandName;

    private float discount;

    private String categoryName;


    public ProductResponseDto convertToDto(ProductEntity product) {
        ProductResponseDto prodResponse = new ProductResponseDto();
        prodResponse.setProd_id(product.getId());
        prodResponse.setProdName(product.getName());
        prodResponse.setEnabled(product.isEnabled());
        prodResponse.setOriginal_price(product.getOriginal_price());
        prodResponse.setBrandName(product.getBrand().getName());
        prodResponse.setPrimaryImage(product.getPrimaryImage());
        prodResponse.setDiscount(product.getDiscount_percent());
        prodResponse.setCategoryName(product.getCategory().getName());
        return prodResponse;
    }
    public List<ProductResponseDto> convertProdDto(List<ProductEntity> productList) {
        List<ProductResponseDto> listProdResponse = new ArrayList<>();
        productList.forEach(prod -> {
            listProdResponse.add(this.convertToDto(prod));
        });
        return listProdResponse;
    }


}
