package com.springboot.laptop.controller;


import com.springboot.laptop.exception.DeleteDataFail;
import com.springboot.laptop.exception.ResourceNotFoundException;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.CategoryRequestDto;
import com.springboot.laptop.model.dto.ErrorCode;
import com.springboot.laptop.model.dto.ResponseDTO;
import com.springboot.laptop.model.dto.SuccessCode;
import com.springboot.laptop.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Operation(summary = "Create a new category", responses = {
            @ApiResponse(description = "Create new category success", responseCode = "200",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = CategoryEntity.class))),
//            @ApiResponse(description = "User not found",responseCode = "409",content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequestDto categoryDto) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        System.out.println("User principal in post cate " + SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            CategoryEntity newOne = new CategoryEntity(categoryDto.getName(), categoryDto.getEnabled());
            CategoryEntity newCate = categoryService.createOne(newOne);
            responseDTO.setData(newCate);
            responseDTO.setSuccessCode(SuccessCode.ADD_CATEGORY_SUCCESS);
        } catch (Exception e){
            throw new Exception(" " + ErrorCode.ADD_CATEGORY_ERROR);
        }

        return ResponseEntity.ok(responseDTO);
//        System.out.println("Category request body  is " + category.getName());
//        CategoryEntity newOne = new CategoryEntity(category.getName(), category.getEnabled());
//        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createOne(newOne));
    }


    @Operation(
            summary = "Get all category")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllCategories() {
        List<CategoryEntity> listCates = categoryService.getAll();
        return new ResponseEntity<List<CategoryEntity>>(listCates, HttpStatus.OK );
    }

    @Operation(summary = "Update Category by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated",
                    content = @Content(schema = @Schema(implementation = CategoryEntity.class))),
            @ApiResponse(responseCode = "401", description = "User is unauthorized"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
    })
    @PutMapping("/{cateId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CategoryEntity> updateStatus(@PathVariable Long cateId, @RequestBody CategoryEntity cate ) throws Exception {
        System.out.println("Category name " + cate.getName() + cate.getId());
        CategoryEntity cateUpdated = categoryService.updateOne(cateId, cate);
        return new ResponseEntity<CategoryEntity>(cateUpdated, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete a category",
            description = "Provide an category id to delete"
    )
    @DeleteMapping("/{cateId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO> deleteBrand(@PathVariable Long cateId) throws ResourceNotFoundException, DeleteDataFail {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            CategoryEntity delCategory = categoryService.deleteOne(cateId);
            responseDTO.setData(delCategory);
            responseDTO.setSuccessCode(SuccessCode.DELETE_CATEGORY_SUCCESS);
        } catch (Exception ex) {
            throw new DeleteDataFail(""+ ErrorCode.DELETE_CATEGORY_ERROR);
        }
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.NO_CONTENT );
    }

}
