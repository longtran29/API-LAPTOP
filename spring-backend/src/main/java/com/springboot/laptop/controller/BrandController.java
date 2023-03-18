package com.springboot.laptop.controller;


import com.springboot.laptop.exception.DuplicatedDataException;
import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.request.BrandRequestDTO;
import com.springboot.laptop.model.dto.response.ErrorCode;
import com.springboot.laptop.model.dto.response.ResponseDTO;
import com.springboot.laptop.model.dto.response.SuccessCode;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.service.BrandServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@CrossOrigin(origins = "http://localhost:3000")
public class BrandController {

    private final BrandServiceImpl brandServiceImpl;
    private final CategoryRepository categoryRepository;
    @Autowired
    public BrandController(BrandServiceImpl brandServiceImpl, CategoryRepository categoryRepository) {
        this.brandServiceImpl = brandServiceImpl;
        this.categoryRepository = categoryRepository;
    }


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
    @GetMapping("/{brandId}/categories")
    public ResponseEntity<?> getCategoriesByBrandId(@PathVariable Long brandId) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<CategoryEntity> categories = brandServiceImpl.getAllCateFromBrand(brandId);
        responseDTO.setData(categories);
        return ResponseEntity.ok(responseDTO);
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
    public ResponseEntity<?> getBrandById(@PathVariable Long brandId) {
        BrandEntity brand = brandServiceImpl.findById(brandId);
        if (brand == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(brand);
        }
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
        List<BrandEntity> listBrands = brandServiceImpl.getAll();
        return new ResponseEntity<List<BrandEntity>>(brandServiceImpl.getAll(), HttpStatus.OK );
    }

    @Operation(summary = "Tạo thương hiệu mới",
            description = "Tạo một thương hiệu mới",security = {
            @SecurityRequirement(name = "bearer-key") },
//            tags = {"Category"},
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
    public ResponseEntity<?> createBrand(@RequestBody BrandRequestDTO newBrand) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            BrandEntity brand = brandServiceImpl.createOne(newBrand);
            responseDTO.setData(brand);
            responseDTO.setSuccessCode(SuccessCode.ADD_BRAND_SUCCESS);

        } catch (DuplicatedDataException e) {
            responseDTO.setErrorCode(ErrorCode.DUPLICATED_DATA);
            return ResponseEntity.badRequest().body(responseDTO);
        }
        catch (Exception e) {
            responseDTO.setErrorCode(ErrorCode.ADD_CATEGORY_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
        return ResponseEntity.ok(responseDTO);
    }


    @Operation(summary = "Xoá thương hiệu",
            description = "Xoá một thương hiệu",security = {
            @SecurityRequirement(name = "bearer-key") },
//            tags = {"Category"},
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
    public ResponseEntity<String> deleteBrand(@PathVariable Long brandId) {
        brandServiceImpl.deleteOne(brandId);
        return new ResponseEntity<String>("Xoá thành công ",HttpStatus.NO_CONTENT );
    }

    @Operation(summary = "Cập nhật thương hiệu",
            description = "Cập nhật thương hiệu",security = {
            @SecurityRequirement(name = "bearer-key") },
//            tags = {"Category"},
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
    public ResponseEntity<?> updateBrand(@PathVariable Long brandId, @RequestBody BrandRequestDTO updateBrand ){
        ResponseDTO responseDTO = new ResponseDTO();
        BrandEntity updatedBrand;
        try {
            updatedBrand = brandServiceImpl.updateOne(brandId, updateBrand);
            responseDTO.setData(updatedBrand);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_CATEGORY_SUCCESS);
        } catch (DuplicatedDataException e) {
            responseDTO.setErrorCode(ErrorCode.DUPLICATED_DATA);
            return ResponseEntity.badRequest().body(responseDTO);
        }
        return new ResponseEntity<BrandEntity>(updatedBrand, HttpStatus.CREATED);
    }

}
