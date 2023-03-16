package com.springboot.laptop.model.dto.response;


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
public class ProductResponseDTO {
    private Long prod_id;
    private String primaryImage;

    private String prodName;
    private boolean enabled;

    private boolean inStock;
    private float original_price;
    private Long brandId;

    private float discount;

    private Long categoryId;

    private String description;

    private Long productQty;


    public ProductResponseDTO convertToDto(ProductEntity product) {
        ProductResponseDTO prodResponse = new ProductResponseDTO();
        prodResponse.setProd_id(product.getId());
        prodResponse.setProdName(product.getName());
        prodResponse.setEnabled(product.isEnabled());
        prodResponse.setOriginal_price(product.getOriginal_price());
        prodResponse.setBrandId(product.getBrand().getId());
        prodResponse.setPrimaryImage(product.getPrimaryImage());
        prodResponse.setDiscount(product.getDiscount_percent());
        prodResponse.setCategoryId(product.getCategory().getId());
        prodResponse.setEnabled(product.isEnabled());
        prodResponse.setInStock(product.isInStock());
        prodResponse.setDescription(product.getDescription());
        prodResponse.setProductQty(product.getProductQuantity());
        return prodResponse;
    }
    public List<ProductResponseDTO> convertProdDto(List<ProductEntity> productList) {
        List<ProductResponseDTO> listProdResponse = new ArrayList<>();
        productList.forEach(prod -> {
            listProdResponse.add(this.convertToDto(prod));
        });
        return listProdResponse;
    }


}
