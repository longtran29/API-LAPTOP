package com.springboot.laptop.controller;


import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.request.BrandRequestDTO;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.service.BrandService;
import com.springboot.laptop.service.impl.BrandServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/brands")
public class BrandController {

    private final BrandService brandServiceImpl;


    @Operation(summary = "Các danh mục theo thương hiệu",
            description = "Danh mục theo thương hiệu",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @GetMapping("/categories/{brandId}")
    public ResponseEntity<?> getCategoriesByBrandId(@PathVariable Long brandId) {
        return ResponseEntity.ok().body(brandServiceImpl.getAllCateFromBrand(brandId));
    }

    @Operation(summary = "Lấy ra thương hiệu theo mã",
            description = "Lấy thương hiệu theo mã",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })

    @GetMapping("/{brandId}")
    public Object getBrandById(@PathVariable Long brandId) {
      return brandServiceImpl.findById(brandId);
    }

    @Operation(summary = "Các thương hiệu",
            description = "Các thương hiệu",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @GetMapping
    public ResponseEntity<?> getAllBrands() {
        return ResponseEntity.ok().body(brandServiceImpl.getAllBrand());
    }

    @Operation(summary = "Tạo thương hiệu mới",
            description = "Tạo một thương hiệu mới",
            responses = {
                    @ApiResponse(description = "Tạo thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public Object createBrand(@RequestBody BrandRequestDTO newBrand) throws Exception {
        return brandServiceImpl.createOne(newBrand);
    }


    @Operation(summary = "Xoá thương hiệu",
            description = "Xoá một thương hiệu",
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
    @DeleteMapping("/{brandId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteBrand(@PathVariable Long brandId) {
        return ResponseEntity.ok().body(brandServiceImpl.deleteOne(brandId));
    }

    @Operation(summary = "Cập nhật thương hiệu",
            description = "Cập nhật thương hiệu",
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
    @PutMapping("/{brandId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateBrand(@PathVariable Long brandId, @RequestBody BrandRequestDTO updateBrand ) throws Exception {
        return ResponseEntity.ok().body(brandServiceImpl.updateOne(brandId, updateBrand));
    }

    @GetMapping("/products/{brandId}")
    public ResponseEntity<?> getProducts(@PathVariable Long brandId ){
        return ResponseEntity.ok().body(brandServiceImpl.getProductsById(brandId));
    }


}
