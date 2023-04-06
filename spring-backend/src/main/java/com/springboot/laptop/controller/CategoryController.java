package com.springboot.laptop.controller;


import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.request.CategoryRequestDTO;
import com.springboot.laptop.model.dto.response.ResponseDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.service.impl.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setData((categoryServiceImpl.findById(categoryId)));
            return  ResponseEntity.ok().body(responseDTO);
        } catch (CustomResponseException ex) {
            responseDTO.setData(ex.getReason());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
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
    public Object createCategory(@RequestBody CategoryRequestDTO categoryDto) {
        return categoryServiceImpl.createOne(categoryDto);
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
    public Object updateCate(@PathVariable Long cateId, @RequestBody CategoryEntity category )  {
        return categoryServiceImpl.updateOne(cateId, category);
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
    public Object updateStatus(@PathVariable Long cateId, @PathVariable String cate_status ) {
        // note : not using operator "=="
        Boolean category_status =cate_status.equalsIgnoreCase("enabled");
        return categoryServiceImpl.updateStatus(cateId, category_status);
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
    public ResponseEntity<?> deleteCategory(@PathVariable Long cateId) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            categoryServiceImpl.deleteOne(cateId);
            responseDTO.setData("Xoá thành công");
        } catch (DataIntegrityViolationException ex) {
            throw new CustomResponseException(StatusResponseDTO.CATEGORY_VIOLATION_EXCEPTION);
        }
        return ResponseEntity.ok().body(responseDTO);
    }
}

