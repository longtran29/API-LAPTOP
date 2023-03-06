package com.springboot.laptop.controller;


import com.springboot.laptop.exception.DeleteDataFail;
import com.springboot.laptop.exception.ResourceNotFoundException;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.dto.*;
import com.springboot.laptop.service.CloudinaryService;
import com.springboot.laptop.service.ProductService;
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

    private final ProductService productService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public ProductController(ProductService productService, CloudinaryService cloudinaryService) {
        this.productService = productService;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createProduct(@RequestBody ProductDto product) {
        try {
            ResponseDTO responseDTO = new ResponseDTO();
            ProductEntity createdProduct = productService.createOne(product);
            responseDTO.setData(createdProduct);
            return ResponseEntity.ok().body(responseDTO);
        } catch (StackOverflowError e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/upload")
    public String uploadFile(@Param("file") MultipartFile file) {
        System.out.println("Da vao uploadFile");
        String url = cloudinaryService.uploadFile(file);
        return url;
    }
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return new ResponseEntity<List<ProductResponseDto>>(productService.getAll(), HttpStatus.OK);
    }
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO> deleteProduct(@PathVariable("productId") Long productId) throws DeleteDataFail {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            boolean delProduct = productService.deleteProduct(productId);
            responseDTO.setData(delProduct);
            responseDTO.setSuccessCode(SuccessCode.DELETE_PRODUCT_SUCCESS);
        } catch (Exception e){
            throw new DeleteDataFail(""+ ErrorCode.DELETE_PRODUCT_ERROR);
        }

        return ResponseEntity.ok().body(responseDTO);

    }

    @Operation(summary = "Update a product", responses = {
            @ApiResponse(description = "Update product", responseCode = "200",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = CategoryEntity.class)))    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDto product) throws ResourceNotFoundException {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            ProductEntity updateProduct = productService.updateProduct(productId, product);
            responseDTO.setData(updateProduct);
            return ResponseEntity.ok().body(responseDTO);

        } catch (ResourceNotFoundException ex) {
            responseDTO.setErrorCode(ErrorCode.NOT_FOUND_EXCEPTION);
            responseDTO.setData(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        }
    }

}
