package com.springboot.laptop.model.dto.response;


import com.springboot.laptop.model.ProductEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {
    private Long prod_id;
    private String primaryImage;

    private String name;
    private boolean enabled;

    private boolean inStock;
    private float original_price;
    private Long brandId;

    private float discount_percent;

    private Long categoryId;

    private Long productQuantity;

    private String description;


    public ProductResponseDTO convertToDto(ProductEntity product) {
        return ProductResponseDTO.builder()
                .prod_id(product.getId())
                .name(product.getName())
                .enabled(product.isEnabled())
                .original_price(product.getOriginal_price())
                .brandId(product.getBrand().getId())
                .primaryImage(product.getPrimaryImage())
                .discount_percent(product.getDiscount_percent())
                .categoryId(product.getCategory().getId())
                .enabled(product.isEnabled())
                .inStock(product.isInStock())
                .description(product.getDescription())
                .productQuantity(product.getProductQuantity())
                .build();
    }
    public List<ProductResponseDTO> convertProdDto(List<ProductEntity> productList) {
        List<ProductResponseDTO> listProdResponse = new ArrayList<>();
        productList.forEach(prod -> {
            listProdResponse.add(this.convertToDto(prod));
        });
        return listProdResponse;
    }


}
