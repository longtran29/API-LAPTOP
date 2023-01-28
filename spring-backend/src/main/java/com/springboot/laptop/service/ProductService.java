package com.springboot.laptop.service;

import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.ProductDto;
import com.springboot.laptop.model.dto.ProductResponseDto;
import com.springboot.laptop.repository.BrandRepository;
import com.springboot.laptop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    @Autowired
    public ProductService(ProductRepository productRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
    }

    public ProductEntity createOne(ProductDto product) {
        BrandEntity brand =  brandRepository.findById(Long.valueOf(product.getBrand())).get();
        ProductEntity newProduct = new ProductEntity();
        newProduct.setName(product.getName());
        newProduct.setPrimaryImage(product.getPrimaryImage());
        newProduct.setAlias(product.getAlias());
        newProduct.setEnabled(product.isEnabled());
        newProduct.setOriginal_price(product.getOriginal_price());
        newProduct.setDiscount_percent(product.getDiscount_percent());
        newProduct.setBrand(brand);
        newProduct.setDescription(product.getDescription());
        newProduct.setInStock(product.isInStock());
        newProduct.setCreationDate(new Date());
        newProduct.setModifiedDate(new Date());

        return productRepository.save(newProduct);
    }

    public List<ProductResponseDto> getAll() {
        List<ProductEntity> products =  productRepository.findAll() ;
        return new ProductResponseDto().convertProdDto(products);
    }
}

