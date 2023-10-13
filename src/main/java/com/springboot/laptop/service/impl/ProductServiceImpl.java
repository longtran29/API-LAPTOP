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
import com.springboot.laptop.service.AmazonS3Service;
import com.springboot.laptop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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

    private final AmazonS3Service amazonS3Service;

    static String[] HEADERs = {"Id", "Name", "Product Quantity", "Price", "Discount %", "Image", "Brand", "Category" };
    static String SHEET = "Products";
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

//        convertEntity.setPrimaryImage(cloudinaryService.uploadFile(mainImageMultipart));

        convertEntity.setImages(new HashSet<>());

        convertEntity.setPrimaryImage(amazonS3Service.uploadImage(mainImageMultipart));

        if(extraImageMultiparts != null) {

            convertEntity.getImages().addAll(Arrays.stream(extraImageMultiparts).map(multipartFile ->{
                ProductImage newImage =  new ProductImage(amazonS3Service.uploadImage(multipartFile));
                newImage.setProduct(convertEntity);
                return newImage;
            }).collect(Collectors.toList()));
        }

        return productMapper.productToProductDTO(productRepository.save(convertEntity));
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
    public void exportToExcelFile(HttpServletResponse response) throws IOException {
        try {

            Workbook book = new XSSFWorkbook();

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Sheet sheet = book.createSheet(SHEET);

            Row headerRow = sheet.createRow(0);

            AtomicInteger col = new AtomicInteger();

            Arrays.stream(HEADERs).forEach(header -> {
                Cell cell = headerRow.createCell(col.getAndIncrement());
                cell.setCellValue(header);
            });

            AtomicInteger rowIdx = new AtomicInteger(1);

            productRepository.findAll().forEach(product -> {

                Row row = sheet.createRow(rowIdx.getAndIncrement());



                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getProductQuantity());
                row.createCell(3).setCellValue(product.getOriginal_price());
                row.createCell(4).setCellValue(product.getDiscount_percent());
                row.createCell(5).setCellValue(product.getPrimaryImage());
                row.createCell(6).setCellValue(product.getBrand().getName());
                row.createCell(7).setCellValue(product.getCategory().getName());

            });
            ServletOutputStream outputStream = response.getOutputStream();
            book.write(outputStream);

        } catch(IOException ex) {

            throw ex;
        }
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

            List<ProductDetail> details = updateProduct.getDetails().stream().map(detail -> {
                ProductDetail newDetail = new ProductDetail();
                newDetail.setName(detail.getName());
                newDetail.setValue(detail.getValue());
                newDetail.setProduct(existingProduct);

                return newDetail;
            }).collect(Collectors.toList());

            existingProduct.getDetails().addAll(details);

            if(extraImageMultiparts != null) {

                existingProduct.getImages().addAll(Arrays.stream(extraImageMultiparts).map(multipartFile ->{
                    ProductImage newImage =  new ProductImage(amazonS3Service.uploadImage(multipartFile));
                    newImage.setProduct(existingProduct);
                    return newImage;
                }).collect(Collectors.toList()));
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

