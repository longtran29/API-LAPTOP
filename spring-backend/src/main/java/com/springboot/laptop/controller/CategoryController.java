package com.springboot.laptop.controller;


import com.springboot.laptop.exception.DeleteDataFail;
import com.springboot.laptop.exception.DuplicatedDataException;
import com.springboot.laptop.exception.ResourceNotFoundException;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.request.CategoryRequestDTO;
import com.springboot.laptop.model.dto.response.ErrorCode;
import com.springboot.laptop.model.dto.response.ResponseDTO;
import com.springboot.laptop.model.dto.response.SuccessCode;
import com.springboot.laptop.service.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryServiceImpl categoryServiceImpl;

    @Autowired
    public CategoryController(CategoryServiceImpl categoryServiceImpl) {
        this.categoryServiceImpl = categoryServiceImpl;
    }


    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId) {
        CategoryEntity category = categoryServiceImpl.findById(categoryId);
        if (category == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(category);
        }
    }

    @Operation(summary = "Create a new category", responses = {
            @ApiResponse(description = "Create new category success", responseCode = "200",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = CategoryEntity.class))),
//            @ApiResponse(description = "User not found",responseCode = "409",content = @Content)
    })
    @PostMapping("/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequestDTO categoryDto) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        System.out.println("User principal in post cate " + SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            CategoryEntity newOne = new CategoryEntity(categoryDto.getName(), categoryDto.getEnabled());
            CategoryEntity newCate = categoryServiceImpl.createOne(newOne);
            responseDTO.setData(newCate);
            responseDTO.setSuccessCode(SuccessCode.ADD_CATEGORY_SUCCESS);
        } catch (DuplicatedDataException e) {
            log.error("Error while creating a new category: ", e);
            responseDTO.setErrorCode(ErrorCode.DUPLICATED_DATA);
            return ResponseEntity.badRequest().body(responseDTO);
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            log.error("Error while creating a new category: ", e);
            responseDTO.setErrorCode(ErrorCode.ADD_CATEGORY_ERROR);
            return ResponseEntity.badRequest().body(responseDTO);
        } catch (Exception e) {
            log.error("Unexpected error while creating a new category: ", e);
            responseDTO.setErrorCode(ErrorCode.ADD_CATEGORY_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
        return ResponseEntity.ok(responseDTO);
    }


//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PreAuthorize("permitAll")
    @Operation(
            summary = "Get all category")
    @GetMapping
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllCategories() {
        List<CategoryEntity> listCates = categoryServiceImpl.getAll();
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
//    @PostAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateCate(@PathVariable Long cateId, @RequestBody CategoryEntity cate )  {
        ResponseDTO responseDTO = new ResponseDTO();
        CategoryEntity updatedCate;
        try {
            updatedCate = categoryServiceImpl.updateOne(cateId, cate);
            responseDTO.setData(updatedCate);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_CATEGORY_SUCCESS);
        } catch (DuplicatedDataException e) {
            log.error("Error while creating a new category: ", e);
            responseDTO.setErrorCode(ErrorCode.DUPLICATED_DATA);
            return ResponseEntity.badRequest().body(responseDTO);
        }
        return new ResponseEntity<CategoryEntity>(updatedCate, HttpStatus.CREATED);
    }


    @Operation(summary = "Update Status Cate by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated",
                    content = @Content(schema = @Schema(implementation = CategoryEntity.class))),
            @ApiResponse(responseCode = "401", description = "User is unauthorized"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
    })
    @PutMapping("/{cateId}/{cate_status}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateStatus(@PathVariable Long cateId, @PathVariable String cate_status ) {
        // note : not using operator "=="
        Boolean category_status =cate_status.equalsIgnoreCase("enabled");
        categoryServiceImpl.updateStatus(cateId, category_status);
        return new ResponseEntity<String>("Update status successfully",HttpStatus.OK);
    }


    @Operation(
            summary = "Delete a category",
            description = "Provide an category id to delete"
    )
    @DeleteMapping("/{cateId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO> deleteCategory(@PathVariable Long cateId) throws ResourceNotFoundException, DeleteDataFail {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            CategoryEntity delCategory = categoryServiceImpl.deleteOne(cateId);
            responseDTO.setData(delCategory);
            responseDTO.setSuccessCode(SuccessCode.DELETE_CATEGORY_SUCCESS);
        } catch (DataIntegrityViolationException ex) {
            System.err.println(ex.getMessage());
            responseDTO.setData("Error foreign key constraint referenced by other table");
            responseDTO.setErrorCode(ErrorCode.DATA_INTEGRITY_VIOLATION_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
            responseDTO.setData("Lỗi trong quá trình xoá");
            responseDTO.setErrorCode(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

}