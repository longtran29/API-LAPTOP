package com.springboot.laptop.controller;


import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.request.CategoryRequestDTO;
import com.springboot.laptop.model.dto.request.ProductDTO;
import com.springboot.laptop.model.dto.response.*;
import com.springboot.laptop.service.impl.CloudinaryService;
import com.springboot.laptop.service.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductServiceImpl productServiceImpl;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public ProductController(ProductServiceImpl productServiceImpl, CloudinaryService cloudinaryService) {
        this.productServiceImpl = productServiceImpl;
        this.cloudinaryService = cloudinaryService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Thêm sản phẩm ",
            description = "Thêm mới sản phẩm vào cơ sở dữ liệu",
            responses = {
                    @ApiResponse(description = "Thêm thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    // check authority base on SecurityContextHolder
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public Object createProduct(@RequestBody ProductDTO product) throws ParseException {
        return productServiceImpl.createOne(product);

    }

    @Operation(summary = "Lấy 1 sản phẩm ",
            description = "Trả về 1 sản phẩm theo mã ",security = {
            @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(description = "thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @GetMapping("/{productId}")
    @PreAuthorize("permitAll")
    public Object getOneProduct(@PathVariable Long productId) {
       return productServiceImpl.getOneProduct(productId);
    }

    @Operation(summary = "Tải ảnh lên",
            description = "Tải ảnh lên cloud",
            responses = {
                    @ApiResponse(description = "Tải thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PostMapping("/upload")
    public String uploadFile(@Param("file") MultipartFile file) {
        System.out.println("Da vao uploadFile");
        String url = cloudinaryService.uploadFile(file);
        return url;
    }

    @GetMapping("/best-seller")
    public ResponseEntity<?> bestSellerProducts() {
        return ResponseEntity.ok().body(productServiceImpl.getBestSellingProducts());
    }

    @Operation(summary = "Danh sách sản phẩm",
            description = "Lấy ra danh sách các sản phẩm",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @GetMapping
    public ResponseEntity<?> getActiveProducts() {
        return new ResponseEntity<List<ProductResponseDTO>>(productServiceImpl.getActiveProduct(), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/active")
    public ResponseEntity<?> getAllProducts() {
        return new ResponseEntity<List<ProductResponseDTO>>(productServiceImpl.getAllProduct(), HttpStatus.OK);
    }

    @PostMapping("/product_in_category")
    public ResponseEntity<?> getProductByCategory(@RequestBody CategoryRequestDTO category) {
        return ResponseEntity.ok().body(productServiceImpl.getProductByCategory(category.getName()));
    }

    @Operation(summary = "Xoá sản phẩm",
            description = "Xoá sản phẩm",
            responses = {
                    @ApiResponse(description = "Xoá thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public Object deleteProduct(@PathVariable("productId") Long productId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            productServiceImpl.deleteProduct(productId);
            responseDTO.setData("Xoá thành công");
            responseDTO.setSuccessCode(SuccessCode.DELETE_PRODUCT_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } catch (DataIntegrityViolationException ex) {
            throw new CustomResponseException(StatusResponseDTO.PRODUCT_VIOLATION_EXCEPTION);
        }
    }

    @Operation(summary = "Cập nhật sản phẩm ",
            description = "Cập nhật các thông tin sản pẩm",security = {
            @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(description = "Cập nhật thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{productId}")
    @SecurityRequirement(name = "bearerAuth")
    public Object updateProduct(@PathVariable Long productId, @RequestBody ProductDTO product) throws ParseException {
        return productServiceImpl.updateProduct(productId, product);
    }

    @Operation(summary = "Cập nhật trạng thái ",
            description = "Cập nhật trạng thái của sản phẩm",
            responses = {
                    @ApiResponse(description = "Cập nhật thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PutMapping("/{productId}/{status}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateStatus(@PathVariable Long productId, @PathVariable String status ) {
        // note : not using operator "=="
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            productServiceImpl.updateStatus(productId, status);
            responseDTO.setData("Cập nhật trang thái thành công !");
            responseDTO.setSuccessCode(SuccessCode.UPDATE_PRODUCT_SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (CustomResponseException ex ) {
            responseDTO.setData(ex.getReason());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
