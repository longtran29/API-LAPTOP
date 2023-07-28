package com.springboot.laptop.mapper;


import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.ProductDTO;
import com.springboot.laptop.model.dto.response.ProductResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductMapper {


//    @Mapping(source = "category.id", target = "category")
//    @Mapping(source = "brand.id", target = "brand")
    ProductResponseDTO productToProductDTO(ProductEntity product);


    //    @Mapping(source = "category", target = "category.id")
//    @Mapping(source = "brand", target = "brand.id")
    ProductEntity productDTOToProduct(ProductResponseDTO product);


    default ProductEntity convertProductDTO(ProductDTO product) {
        if (product == null) {
            return null;
        } else {
            ProductEntity.ProductEntityBuilder productEntity = ProductEntity.builder();
            productEntity.name(product.getName());
            productEntity.primaryImage(product.getPrimaryImage());
            productEntity.enabled(product.isEnabled());
            productEntity.original_price(product.getOriginal_price());
            productEntity.discount_percent(product.getDiscount_percent());
            productEntity.description(product.getDescription());
            productEntity.inStock(product.isInStock());
            productEntity.productQuantity(product.getProductQuantity());
            return productEntity.build();
        }
    }
}

