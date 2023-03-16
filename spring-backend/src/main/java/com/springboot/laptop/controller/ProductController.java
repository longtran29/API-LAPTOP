package com.springboot.laptop.controller;


import com.springboot.laptop.exception.DeleteDataFail;
import com.springboot.laptop.exception.ResourceNotFoundException;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.request.ProductDTO;
import com.springboot.laptop.model.dto.response.ErrorCode;
import com.springboot.laptop.model.dto.response.ProductResponseDTO;
import com.springboot.laptop.model.dto.response.ResponseDTO;
import com.springboot.laptop.model.dto.response.SuccessCode;
import com.springboot.laptop.service.CloudinaryService;
import com.springboot.laptop.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductServiceImpl productServiceImpl;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public ProductController(ProductServiceImpl productServiceImpl, CloudinaryService cloudinaryService) {
        this.productServiceImpl = productServiceImpl;
        this.cloudinaryService = cloudinaryService;
    }

    // check authority base on SecurityContextHolder
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO product) {
        try {
            ResponseDTO responseDTO = new ResponseDTO();
            ProductEntity createdProduct = productServiceImpl.createOne(product);
            responseDTO.setData(createdProduct);
            return ResponseEntity.ok().body(responseDTO);
        } catch (StackOverflowError e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{productId}")
    @PreAuthorize("permitAll")
    public ResponseEntity<?> getOneProduct(@PathVariable Long productId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            ProductResponseDTO updateProduct = new ProductResponseDTO();
            updateProduct = updateProduct.convertToDto(productServiceImpl.getOneProduct(productId));
            responseDTO.setData(updateProduct);

        }catch (Exception ex) {
            responseDTO.setData("Khong tim thay san pham nao voi ma " + productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/upload")
    public String uploadFile(@Param("file") MultipartFile file) {
        System.out.println("Da vao uploadFile");
        String url = cloudinaryService.uploadFile(file);
        return url;
    }
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return new ResponseEntity<List<ProductResponseDTO>>(productServiceImpl.getAll(), HttpStatus.OK);
    }
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO> deleteProduct(@PathVariable("productId") Long productId) throws DeleteDataFail {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            boolean delProduct = productServiceImpl.deleteProduct(productId);
            responseDTO.setData(delProduct);
            responseDTO.setSuccessCode(SuccessCode.DELETE_PRODUCT_SUCCESS);
        }
        catch (Exception ex){
            throw new DeleteDataFail(""+ ErrorCode.DELETE_PRODUCT_ERROR);
        }

        return ResponseEntity.ok().body(responseDTO);

    }

    @Operation(summary = "Update a product", responses = {
            @ApiResponse(description = "Update product", responseCode = "200",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = CategoryEntity.class)))    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO product) throws ResourceNotFoundException {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            ProductEntity updateProduct = productServiceImpl.updateProduct(productId, product);
            responseDTO.setData(updateProduct);
            return ResponseEntity.ok().body(responseDTO);

        } catch (ResourceNotFoundException ex) {
            responseDTO.setErrorCode(ErrorCode.NOT_FOUND_EXCEPTION);
            responseDTO.setData(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }
    }

    @PutMapping("/{cateId}/{cate_status}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateStatus(@PathVariable Long cateId, @PathVariable String cate_status ) {
        // note : not using operator "=="
        Boolean category_status =cate_status.equalsIgnoreCase("enabled");
        productServiceImpl.updateStatus(cateId, category_status);
        return new ResponseEntity<String>("Update status successfully",HttpStatus.OK);
    }

}
