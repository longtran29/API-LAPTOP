package com.springboot.laptop.service;

import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.ProductDTO;
import com.springboot.laptop.model.dto.response.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

public interface ProductService {
    public Object getOneProduct(Long productId);

    public Object createOne(ProductDTO product, MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts) throws ParseException;

    public List<ProductResponseDTO> getActiveProduct();
    public Object updateProduct(Long productId, ProductDTO updateProduct, MultipartFile primaryImage, MultipartFile[] extraImages) throws Exception;
    public Object deleteProduct(Long productId);


    public List<ProductResponseDTO> getAllProduct();

    Object updateStatus(Long productId, boolean productStatus);

    public List<ProductEntity> getBestSellingProducts();
}
