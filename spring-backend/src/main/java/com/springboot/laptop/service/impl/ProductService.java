package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.ResourceNotFoundException;
import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.ProductDTO;
import com.springboot.laptop.model.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    public ProductEntity getOneProduct(Long productId) throws ResourceNotFoundException;
    public void updateStatus (Long productId, Boolean status);

    public ProductEntity createOne(ProductDTO product);

    public List<ProductResponseDTO> getAll();
    public ProductEntity updateProduct(Long productId, ProductDTO updateProduct) throws ResourceNotFoundException;
    public boolean deleteProduct(Long productId) throws ResourceNotFoundException;
}
