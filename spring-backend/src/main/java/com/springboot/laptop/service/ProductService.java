package com.springboot.laptop.service;

import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.ProductDTO;

import java.text.ParseException;
import java.util.List;

public interface ProductService {
    public Object getOneProduct(Long productId);

    public Object createOne(ProductDTO product) throws ParseException;

    public List<ProductDTO> getActiveProduct();
    public Object updateProduct(Long productId, ProductDTO updateProduct)throws ParseException;
    public Object deleteProduct(Long productId);


    public List<ProductDTO> getAllProduct();

    Object updateStatus(Long productId, Boolean productStatus);

    public List<ProductEntity> getBestSellingProducts();
}
