package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.mapper.ProductMapper;
import com.springboot.laptop.model.*;
//import com.springboot.laptop.model.dto.request.ProductDTO;
import com.springboot.laptop.model.dto.ProductDTO;
import com.springboot.laptop.model.dto.request.ProductDetailDTO;
import com.springboot.laptop.model.dto.response.ProductResponseDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.*;
import com.springboot.laptop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
    private final CloudinaryService cloudinaryService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;

    @Override
    public Object getOneProduct(Long productId)  {
        ProductEntity existingProduct =  productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm này rồi "));
        return productMapper.productToProductDTO(existingProduct);
    }

    @Override
    public Object createOne(ProductDTO product, MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts) throws ParseException {

        ProductEntity convertEntity = productMapper.convertProductDTO(product);
        CategoryEntity foundCate = categoryRepository.findById(product.getCategory()).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND));
        convertEntity.setCategory(foundCate);
        BrandEntity foundBrand = brandRepository.findById(product.getBrand()).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND));
        convertEntity.setBrand(foundBrand);
        convertEntity.setCreatedTimestamp(new Date());
        List<ProductDetailDTO> updateDetail = product.getDetails();;

        convertEntity.setDetails(new ArrayList<>());
        for (ProductDetailDTO productDetailDTO : updateDetail) {

            ProductDetail newDetail = new ProductDetail(productDetailDTO.getName(), productDetailDTO.getValue());
            newDetail.setProduct(convertEntity);
            convertEntity.getDetails().add(newDetail);
        }

        convertEntity.setPrimaryImage(cloudinaryService.uploadFile(mainImageMultipart));
        convertEntity.setImages(new HashSet<>());
        for (MultipartFile extraImageMultipart : extraImageMultiparts) {
            ProductImage newImage = new ProductImage(cloudinaryService.uploadFile(extraImageMultipart));
            newImage.setProduct(convertEntity);
            convertEntity.getImages().add(newImage);
        }
        return productRepository.save(convertEntity);
    }

    @Override
    public Object updateStatus(Long productId, String productStatus) {
        ProductEntity existingProduct = productRepository.findById(productId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND));
        if(productStatus.equals("disabled")) {
            boolean isProductInCart = false;
            // check current existing cart
            List<UserCart> listExistingCart= cartRepository.findAll();
            for (UserCart cart: listExistingCart
            ) {
                for (CartDetails cartDetail: cart.getCartDetails()
                ) {

                    if(cartDetail.getProduct().getId().equals(productId)) {
                        isProductInCart = true; break;
                    }
                }

            }
            if(isProductInCart) throw new CustomResponseException(StatusResponseDTO.PRODUCT_EXISTING_IN_CART);
        }
        existingProduct.setEnabled(productStatus.equals("enabled"));
        return productMapper.productToProductDTO(productRepository.save(existingProduct));
    }

    @Override
    public List<ProductEntity> getBestSellingProducts() {
        return productRepository.findBestSellerProducts();
    }

    @Override
    public List<ProductResponseDTO> getActiveProduct() {
     return productRepository.getActiveProducts().stream().map(productMapper::productToProductDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getAllProduct() {
        return productRepository.findAll().stream().map(productMapper::productToProductDTO).collect(Collectors.toList());
    }

    @Override
    public Object updateProduct(Long productId, ProductDTO updateProduct, MultipartFile mainImageMultipart,MultipartFile[] extraImageMultiparts) throws Exception {

        try {

            if(updateProduct.isEmpty()) throw new RuntimeException("Vui long nhap day du thong tin");

            ProductEntity existingProduct = productRepository.findById(productId).orElseThrow(() ->  new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND));

            existingProduct.setName(updateProduct.getName());
            existingProduct.setEnabled(updateProduct.isEnabled());
            existingProduct.setOriginal_price(updateProduct.getOriginal_price());
            existingProduct.setDiscount_percent(updateProduct.getDiscount_percent());
            existingProduct.setDescription(updateProduct.getDescription());
            existingProduct.setInStock(updateProduct.isInStock());
            existingProduct.setModifiedTimestamp(new Date());
            existingProduct.setProductQuantity(updateProduct.getProductQuantity());
            existingProduct.setCategory(categoryRepository.findById(updateProduct.getCategory()).get());


            if(mainImageMultipart != null) existingProduct.setPrimaryImage(cloudinaryService.uploadFile(mainImageMultipart));


            for (int i =0; i< updateProduct.getDetails().size(); i++) {

                if(updateProduct.getDetails().get(i).getId() == null) {
                    ProductDetail newDetail = new ProductDetail();
                    newDetail.setName(updateProduct.getDetails().get(i).getName());
                    newDetail.setValue(updateProduct.getDetails().get(i).getValue());
                    newDetail.setProduct(existingProduct);
                    existingProduct.getDetails().add(newDetail);

                }

            }

            if(extraImageMultiparts != null) {

                for (MultipartFile extraImageMultipart : extraImageMultiparts) {
                    ProductImage newImage = new ProductImage(cloudinaryService.uploadFile(extraImageMultipart));
                    newImage.setProduct(existingProduct);
                    existingProduct.getImages().add(newImage);
                }
            }

            return productMapper.productToProductDTO(productRepository.save(existingProduct));
        } catch (Exception ex) {
            throw new Exception(ex);
        }

    }

    @Override
    public Object deleteProduct(Long productId)  {
        ProductEntity existingProduct = productRepository.findById(productId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.PRODUCT_NOT_FOUND));
        boolean isProductInUse = orderDetailRepository.findAll().stream()
                .anyMatch(orderDetail -> orderDetail.getProduct().getId().equals(existingProduct.getId()));
        if(isProductInUse) throw new RuntimeException("Sản phẩm đang được kinh doanh");
        productRepository.delete(existingProduct);

        return "Delete successfully!";
    }
}

