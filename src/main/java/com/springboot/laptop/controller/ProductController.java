package com.springboot.laptop.controller;


import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.CategoryDTO;
import com.springboot.laptop.model.dto.ProductDTO;
//import com.springboot.laptop.model.dto.request.ProductDTO;
import com.springboot.laptop.model.dto.response.ProductResponseDTO;
import com.springboot.laptop.service.ProductService;
import com.springboot.laptop.service.impl.CloudinaryService;
import com.springboot.laptop.service.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productServiceImpl;

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
    @PostMapping(consumes = {   "multipart/form-data" })
    @SecurityRequirement(name = "bearerAuth")
    public Object createProduct(@RequestPart("product")  @Valid ProductDTO product,
        @RequestParam(value = "primaryImage", required = false) MultipartFile mainImageMultipart,
        @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts
                                ) throws ParseException {
        return ResponseEntity.ok().body(productServiceImpl.createOne(product, mainImageMultipart,extraImageMultiparts ));

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
    @GetMapping("active")
    public ResponseEntity<?> getActiveProducts() {
        return  ResponseEntity.ok().body(productServiceImpl.getActiveProduct());
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok().body(productServiceImpl.getAllProduct());
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
        return ResponseEntity.ok().body(productServiceImpl.deleteProduct(productId));
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
    @PutMapping(path = "/{productId}",consumes = {   "multipart/form-data" })
    @SecurityRequirement(name = "bearerAuth")
    public Object updateObject(@PathVariable Long productId, @RequestPart("product") ProductDTO product,
                                @RequestParam(value = "primaryImage", required = false) MultipartFile mainImageMultipart,
                                @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts
    ) throws Exception {
        return ResponseEntity.ok().body(productServiceImpl.updateProduct(productId, product, mainImageMultipart,extraImageMultiparts ));

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
    @PutMapping("/status/{productId}/{status}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateStatus(@PathVariable("productId") Long productId,@PathVariable("status") String productStats ) {
        return ResponseEntity.ok().body(productServiceImpl.updateStatus(productId, productStats));
    }

    @GetMapping("/download")
    public void getFile(HttpServletResponse response) throws IOException {
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=product_list"  + ".xlsx";
        response.setHeader(headerKey, headerValue);

        productServiceImpl.exportToExcelFile(response);
    }
}
