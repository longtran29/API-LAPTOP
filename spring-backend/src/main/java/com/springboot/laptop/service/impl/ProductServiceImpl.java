package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.mapper.ProductMapper;
import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.ProductEntity;
//import com.springboot.laptop.model.dto.request.ProductDTO;
import com.springboot.laptop.model.dto.ProductDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.BrandRepository;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.repository.ProductRepository;
import com.springboot.laptop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    @Override
    public Object getOneProduct(Long productId)  {
        productRepository.findById(productId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND));
        return productMapper.productToProductDTO(productRepository.findById(productId).get());
    }
    @Override
    public Object updateStatus(Long productId, Boolean productStatus) {
        productRepository.findById(productId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND));
        try {
            productRepository.updateStatus(productId, productStatus);
        } catch (Exception ex) {
            throw ex;
        }
        List<ProductEntity> products =  productRepository.findAll();
        List<ProductDTO> returnedProds = new ArrayList<>();
        products.forEach(product -> returnedProds.add(productMapper.productToProductDTO(product)));
        return returnedProds;
    }

    @Override
    public List<ProductEntity> getBestSellingProducts() {
        return productRepository.findBestSellerProducts();
    }

    @Override
    public List<ProductDTO> getActiveProduct() {
        List<ProductEntity> products =  productRepository.getActiveProducts() ;
        List<ProductDTO> returnedProds = new ArrayList<>();
        products.forEach(product -> returnedProds.add(productMapper.productToProductDTO(product)));
        return returnedProds;
    }

    @Override
    public List<ProductDTO> getAllProduct() {
        return productRepository.findAll().stream().map(productMapper::productToProductDTO).collect(Collectors.toList());
    }

    @Override
    public Object createOne(ProductDTO product) throws ParseException {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if(product.getPrimaryImage().isBlank()) throw new CustomResponseException(StatusResponseDTO.IMAGE_NOT_FOUND);
        if( product.getDiscount_percent() == null || product.getName() == "" || product.getOriginal_price() == null || product.getProductQuantity() == null || product.getDescription() == "")
            throw new CustomResponseException(StatusResponseDTO.INFORMATION_IS_MISSING);
        categoryRepository.findById(Long.valueOf(product.getCategory())).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND));
        brandRepository.findById(Long.valueOf(product.getBrand())).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND));

        ProductEntity newProduct = productMapper.productDTOToProduct(product);
        newProduct.setCreationDate(dateFormat.parse(dateFormat.format(date)));
        productRepository.save(newProduct);
        return productRepository.findAll().stream().map(productMapper::productToProductDTO).collect(Collectors.toList());
    }


    @Override
    public Object updateProduct(Long productId, ProductDTO updateProduct) throws ParseException {
        try {
            ProductEntity existingProduct = productRepository.findById(productId).orElseThrow(()-> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND));
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            if(updateProduct.getDiscount_percent() > 1.0 || updateProduct.getDiscount_percent() < 0.0 || updateProduct.getProductQuantity() < 0 || updateProduct.getOriginal_price() < 0.0)
                throw new RuntimeException("Giá trị bạn nhập không hợp lệ");
            updateProduct.setId(productId);
            updateProduct.setModifiedDate(date);
            productRepository.save(productMapper.productDTOToProduct(updateProduct));
            return productRepository.findAll().stream().map(productMapper::productToProductDTO).collect(Collectors.toList());

        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Object deleteProduct(Long productId)  {
        ProductEntity existingProduct = productRepository.findById(productId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND));
        productRepository.delete(existingProduct);
        return productRepository.findAll().stream().map(productMapper::productToProductDTO).collect(Collectors.toList());
    }
}

