package com.springboot.laptop.service;

import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.request.ProductDTO;
import com.springboot.laptop.model.dto.response.ProductResponseDTO;

import java.text.ParseException;
import java.util.List;

public interface ProductService {
    public Object getOneProduct(Long productId);
    public void updateStatus (Long productId, String status);

    public ProductEntity createOne(ProductDTO product) throws ParseException;

    public List<ProductResponseDTO> getActiveProduct();
    public ProductEntity updateProduct(Long productId, ProductDTO updateProduct)throws ParseException;
    public void deleteProduct(Long productId);

    public List<ProductResponseDTO> getAllProduct();

    public List<ProductEntity> getProductByCategory(String categoryName);

    public List<ProductEntity> getBestSellingProducts();
}