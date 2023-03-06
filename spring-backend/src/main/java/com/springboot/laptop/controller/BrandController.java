package com.springboot.laptop.controller;


import com.springboot.laptop.exception.DuplicatedDataException;
import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.BrandRequestDto;
import com.springboot.laptop.model.dto.ErrorCode;
import com.springboot.laptop.model.dto.ResponseDTO;
import com.springboot.laptop.model.dto.SuccessCode;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@CrossOrigin(origins = "http://localhost:3000")
public class BrandController {

    private final BrandService brandService;
    private final CategoryRepository categoryRepository;
    @Autowired
    public BrandController(BrandService brandService, CategoryRepository categoryRepository) {
        this.brandService = brandService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<?> getBrandById(@PathVariable Long brandId) {
        BrandEntity brand = brandService.findById(brandId);
        if (brand == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(brand);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBrands() {
        List<BrandEntity> listBrands = brandService.getAll();
        return new ResponseEntity<List<BrandEntity>>(brandService.getAll(), HttpStatus.OK );
    }

    @Operation(summary = "Create a new brand", responses = {
            @ApiResponse(description = "Create new brand", responseCode = "200",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = BrandEntity.class))),
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createBrand(@RequestBody BrandRequestDto newBrand) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            BrandEntity brand = brandService.createOne(newBrand);
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

    @DeleteMapping("/{brandId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteBrand(@PathVariable Long brandId) {
        brandService.deleteOne(brandId);
        return new ResponseEntity<String>("Delete successfully",HttpStatus.NO_CONTENT );
    }

    @PutMapping("/{brandId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateBrand(@PathVariable Long brandId, @RequestBody BrandRequestDto updateBrand ){
        ResponseDTO responseDTO = new ResponseDTO();
        BrandEntity updatedBrand;
        try {
            updatedBrand = brandService.updateOne(brandId, updateBrand);
            responseDTO.setData(updatedBrand);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_CATEGORY_SUCCESS);
        } catch (DuplicatedDataException e) {
            responseDTO.setErrorCode(ErrorCode.DUPLICATED_DATA);
            return ResponseEntity.badRequest().body(responseDTO);
        }
        return new ResponseEntity<BrandEntity>(updatedBrand, HttpStatus.CREATED);
    }

}
