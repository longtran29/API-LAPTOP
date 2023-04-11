package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.request.ProductDTO;
import com.springboot.laptop.model.dto.response.ProductResponseDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.BrandRepository;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.repository.ProductRepository;
import com.springboot.laptop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    private final CategoryRepository categoryRepository;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Object getOneProduct(Long productId)  {
        if(!productRepository.findById(productId).isPresent()) {
            throw new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND);
        }
        else {
            ProductResponseDTO updateProduct = new ProductResponseDTO();
            updateProduct = updateProduct.convertToDto(productRepository.findById(productId).get());
            return updateProduct;
        }

    }
    @Override
    public void updateStatus (Long productId, String productStatus) {
        Boolean status = productStatus.equalsIgnoreCase("enabled");
            try {
                if(!productRepository.findById(productId).isPresent()) throw new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND);
            }
            catch (HttpServerErrorException.InternalServerError ex) {
                throw new CustomResponseException(StatusResponseDTO.INTERNAL_SERVER);
            }
        productRepository.updateStatus(productId, status);
    }
    @Override
    public ProductEntity createOne(ProductDTO product) throws ParseException {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            CategoryEntity category;
            if(categoryRepository.findById(Long.valueOf(product.getCategoryId())).isPresent()) {
                category = categoryRepository.findById(Long.valueOf(product.getCategoryId())).get();
            } else {
                throw new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND);
            }

            BrandEntity brand;
            if(brandRepository.findById(Long.valueOf(product.getBrandId())).isPresent()) {
                brand =  brandRepository.findById(Long.valueOf(product.getBrandId())).get();
            } else {
                throw new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND);
            }
            System.out.println("Result is " + category.getName() + " " + brand.getName());
            ProductEntity newProduct =ProductEntity.builder()
                    .name(product.getName())
                    .primaryImage(product.getPrimaryImage())
                    .alias(product.getAlias())
                    .enabled(true)
                    .original_price(product.getOriginal_price())
                    .discount_percent(product.getDiscount_percent())
                    .brand(brand)
                    .category(category)
                    .description(product.getDescription())
                    .inStock(product.isInStock())
                    .creationDate(dateFormat.parse(dateFormat.format(date)))
                    .productQuantity(product.getProductQty())
                    .build();

        return productRepository.save(newProduct);
    }

    @Override
    public List<ProductEntity> getProductByCategory(String categoryName) {
        CategoryEntity category;
        if(!categoryRepository.findByName(categoryName).isPresent())
            throw new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND);
        else {
            category = categoryRepository.findByName(categoryName).get();
            return (List<ProductEntity>) category.getProducts();
        }

    }

    @Override
    public List<ProductEntity> getBestSellingProducts() {
        return productRepository.findBestSellerProducts();
    }

    @Override
    public List<ProductResponseDTO> getActiveProduct() {
        List<ProductEntity> products =  productRepository.getActiveProducts() ;
        return new ProductResponseDTO().convertProdDto(products);
    }

    @Override
    public List<ProductResponseDTO> getAllProduct() {
        List<ProductEntity> products =  productRepository.findAll();
        return new ProductResponseDTO().convertProdDto(products);
    }
    @Override
    public ProductEntity updateProduct(Long productId, ProductDTO updateProduct) throws ParseException {
        ProductEntity existingProduct = productRepository.findById(productId).orElseThrow(()-> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND));
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if(updateProduct.getName() != null) existingProduct.setName(updateProduct.getName());
        if(updateProduct.getDescription() != null) existingProduct.setDescription(updateProduct.getDescription());
        if(updateProduct.getOriginal_price() != null) existingProduct.setOriginal_price(updateProduct.getOriginal_price());
        if(updateProduct.getDiscount_percent() != null) existingProduct.setDiscount_percent(updateProduct.getDiscount_percent());
        if(updateProduct.getCategoryId() != null) existingProduct.setCategory(categoryRepository.findById(updateProduct.getCategoryId()).get());
        if(updateProduct.getBrandId() != null) existingProduct.setBrand(brandRepository.findById(updateProduct.getBrandId()).get());
        if(updateProduct.getPrimaryImage() != null)existingProduct.setPrimaryImage(updateProduct.getPrimaryImage());
        if(updateProduct.getProductQty() != null) existingProduct.setProductQuantity(updateProduct.getProductQty());
        existingProduct .setInStock((updateProduct.isInStock()));
        existingProduct.setModifiedDate(dateFormat.parse(dateFormat.format(date)));
        productRepository.save(existingProduct);

        return existingProduct;
    }

    @Override
    public void deleteProduct(Long productId)  {
        ProductEntity existingProduct = productRepository.findById(productId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND));
        productRepository.delete(existingProduct);
    }
}

