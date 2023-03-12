package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.ResourceNotFoundException;
import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.ProductDto;
import com.springboot.laptop.model.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {
    public ProductEntity getOneProduct(Long productId) throws ResourceNotFoundException;
    public void updateStatus (Long productId, Boolean status);

    public ProductEntity createOne(ProductDto product);

    public List<ProductResponseDto> getAll();
    public ProductEntity updateProduct(Long productId, ProductDto updateProduct) throws ResourceNotFoundException;
    public boolean deleteProduct(Long productId) throws ResourceNotFoundException;
}
