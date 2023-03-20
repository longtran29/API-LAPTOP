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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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


    @Operation(summary = "Lấy 1 danh mục",
            description = "Lấy ra 1 danh muc",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId) {
        CategoryEntity category = categoryServiceImpl.findById(categoryId);
        if (category == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(category);
        }
    }

    @Operation(summary = "Tạo danh mục mới",
            description = "Tạo một danh mục mới",security = {
            @SecurityRequirement(name = "bearer-key") },
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
    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequestDTO categoryDto) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        System.out.println("User principal in post cate " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("Authorities " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
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



    @Operation(summary = "Lấy tất cả danh mục",
            description = "Tất cả danh mục",
            responses = {
                    @ApiResponse(description = "Lấy thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<CategoryEntity> listCates = categoryServiceImpl.getAll();
        return new ResponseEntity<List<CategoryEntity>>(listCates, HttpStatus.OK );
    }

    @Operation(summary = "Cập nhật danh mục",
            description = "Cập nhật danh mục",security = {
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
    @PutMapping("/{cateId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateCate(@PathVariable Long cateId, @RequestBody CategoryEntity cate )  {
        ResponseDTO responseDTO = new ResponseDTO();
        CategoryEntity updatedCate;
        try {
            updatedCate = categoryServiceImpl.updateOne(cateId, cate);
            responseDTO.setData(updatedCate);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_CATEGORY_SUCCESS);
        } catch (DuplicatedDataException e) {
            log.error(" "  +  e.getMessage());
            responseDTO.setErrorCode(ErrorCode.DUPLICATED_DATA);
            return ResponseEntity.badRequest().body(responseDTO);
        }
        return new ResponseEntity<CategoryEntity>(updatedCate, HttpStatus.CREATED);
    }


    @Operation(summary = "Cập nhật trạng thái",
            description = "Cập nhật trạng thái",
            responses = {
                    @ApiResponse(description = "Thành công", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CategoryEntity.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    @PutMapping("/{cateId}/{cate_status}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateStatus(@PathVariable Long cateId, @PathVariable String cate_status ) {
        // note : not using operator "=="
        Boolean category_status =cate_status.equalsIgnoreCase("enabled");
        categoryServiceImpl.updateStatus(cateId, category_status);
        return new ResponseEntity<String>("Update status successfully",HttpStatus.OK);
    }


    @Operation(summary = "Xoá danh mục ",
            description = "Xoá danh mục",security = {
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
            responseDTO.setData("Không xoá được vì danh mục của hãng");
            responseDTO.setErrorCode(ErrorCode.DATA_INTEGRITY_VIOLATION_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
            responseDTO.setData("Lỗi trong quá trình xoá");
            responseDTO.setErrorCode(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

}