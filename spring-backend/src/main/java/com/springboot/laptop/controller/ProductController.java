package com.springboot.laptop.controller;


import com.springboot.laptop.exception.CustomResponseException;
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
@CrossOrigin(origins = "http://localhost:3000")
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
            description = "Thêm mới sản phẩm vào cơ sở dữ liệu",security = {
            @SecurityRequirement(name = "bearer-key") },
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
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO product) {
        ResponseDTO responseDTO = null;
        try {
            responseDTO = new ResponseDTO();
            ProductEntity createdProduct = productServiceImpl.createOne(product);
            responseDTO.setData(createdProduct);
            return ResponseEntity.ok().body(responseDTO);
        } catch (CustomResponseException ex) {
//            System.out.println("Execption is " + ex.getReason());
            responseDTO.setData(ex.getReason());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Lấy 1 sản phẩm ",
            description = "Trả về 1 sản phẩm theo mã ",security = {
            @SecurityRequirement(name = "bearer-key") },
//            tags = {"Category"},
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
    public ResponseEntity<?> getAllProducts() {
        return new ResponseEntity<List<ProductResponseDTO>>(productServiceImpl.getAll(), HttpStatus.OK);
    }

    @Operation(summary = "Xoá sản phẩm",
            description = "Xoá sản phẩm",security = {
            @SecurityRequirement(name = "bearer-key") },
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
    public ResponseEntity<ResponseDTO> deleteProduct(@PathVariable("productId") Long productId) throws DeleteDataFail {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            ProductEntity delProduct = productServiceImpl.deleteProduct(productId);
            responseDTO.setData(delProduct);
            responseDTO.setSuccessCode(SuccessCode.DELETE_PRODUCT_SUCCESS);
        }
        catch (DataIntegrityViolationException ex) {
            responseDTO.setData("Sản phẩm trong đơn đặt hàng không thể xoá");
            responseDTO.setErrorCode(ErrorCode.DATA_INTEGRITY_VIOLATION_ERROR);
        }catch (CustomResponseException ex) {
            responseDTO.setData(ex.getReason());
        }
        return ResponseEntity.ok()
                .body(responseDTO);
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
        } catch (ParseException e) {
            // exception parse new Date()
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "Cập nhật trạng thái ",
            description = "Cập nhật trạng thái của sản phẩm",security = {
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
    @PutMapping("/{productId}/{status}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateStatus(@PathVariable Long productId, @PathVariable String status ) {
        // note : not using operator "=="
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            productServiceImpl.updateStatus(productId, status);
            return ResponseEntity.status(HttpStatus.OK).body("Cập nhật trạng thái thành công");

        } catch (CustomResponseException ex ) {
            responseDTO.setData(ex.getReason());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
