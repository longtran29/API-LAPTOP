package com.springboot.laptop.service;

import com.springboot.laptop.exception.ResourceNotFoundException;
import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.response.ErrorCode;
import com.springboot.laptop.model.dto.request.ProductDTO;
import com.springboot.laptop.model.dto.response.ProductResponseDTO;
import com.springboot.laptop.repository.BrandRepository;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.repository.ProductRepository;
import com.springboot.laptop.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public ProductEntity getOneProduct(Long productId) throws ResourceNotFoundException {
        try {
             ProductEntity product = productRepository.findById(productId).get();
             return product;
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Khong tim thay san pham voi id " + productId);
        }
    }

    @Override
    public void updateStatus (Long productId, Boolean status) {
        productRepository.updateStatus(productId, status);
    }
    @Override
    public ProductEntity createOne(ProductDTO product) {
        CategoryEntity category = categoryRepository.findById(Long.valueOf(product.getCategoryId())).get();
        BrandEntity brand =  brandRepository.findById(Long.valueOf(product.getBrandId())).get();
        System.out.println("Result is " + category.getName() + " " + brand.getName());
        ProductEntity newProduct = new ProductEntity();
        newProduct.setName(product.getName());
        newProduct.setPrimaryImage(product.getPrimaryImage());
        newProduct.setAlias(product.getAlias());
        newProduct.setEnabled(product.isEnabled());
        newProduct.setOriginal_price(product.getOriginal_price());
        newProduct.setDiscount_percent(product.getDiscount_percent());
        newProduct.setBrand(brand);
        newProduct.setCategory(category);
        newProduct.setDescription(product.getDescription());
        newProduct.setInStock(product.isInStock());
        newProduct.setCreationDate(new Date());
        newProduct.setModifiedDate(new Date());
        newProduct.setProductQuantity(product.getProductQty());

        return productRepository.save(newProduct);
    }

    @Override
    public List<ProductResponseDTO> getAll() {
        List<ProductEntity> products =  productRepository.findAll() ;
        return new ProductResponseDTO().convertProdDto(products);
    }
    @Override
    public ProductEntity updateProduct(Long productId, ProductDTO updateProduct) throws ResourceNotFoundException {
        ProductEntity existingProduct = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("No product found with id " + productId));

        if(updateProduct.getName() != null) existingProduct.setName(updateProduct.getName());
        if(updateProduct.getDescription() != null) existingProduct.setDescription(updateProduct.getDescription());
        if(updateProduct.getOriginal_price() != null) existingProduct.setOriginal_price(updateProduct.getOriginal_price());
        if(updateProduct.getDiscount_percent() != null) existingProduct.setDiscount_percent(updateProduct.getDiscount_percent());
        if(updateProduct.getCategoryId() != null) existingProduct.setCategory(categoryRepository.findById(updateProduct.getCategoryId()).get());
        if(updateProduct.getBrandId() != null) existingProduct.setBrand(brandRepository.findById(updateProduct.getBrandId()).get());
        if(updateProduct.getPrimaryImage() != null)existingProduct.setPrimaryImage(updateProduct.getPrimaryImage());
        if(updateProduct.getProductQty() != null) existingProduct.setProductQuantity(updateProduct.getProductQty());
        productRepository.save(existingProduct);

        return existingProduct;
    }

    @Override
    public boolean deleteProduct(Long productId) throws ResourceNotFoundException {
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(""+ ErrorCode.FIND_PRODUCT_ERROR));
        this.productRepository.delete(product);
        return true;
    }
}

