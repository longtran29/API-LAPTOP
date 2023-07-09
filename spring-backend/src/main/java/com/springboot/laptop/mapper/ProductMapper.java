package com.springboot.laptop.mapper;


import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductMapper {


    @Mapping(source = "category.id", target = "category")
    @Mapping(source = "brand.id", target = "brand")
    ProductDTO productToProductDTO(ProductEntity product);


    @Mapping(source = "category", target = "category.id")
    @Mapping(source = "brand", target = "brand.id")
    ProductEntity productDTOToProduct(ProductDTO product);
}
