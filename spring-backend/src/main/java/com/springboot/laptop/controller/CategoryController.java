package com.springboot.laptop.controller;


import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.CategoryDTO;
import com.springboot.laptop.model.dto.request.CategoryRequestDTO;
import com.springboot.laptop.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryServiceImpl;

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
        return ResponseEntity.ok().body(categoryServiceImpl.findById(categoryId));
    }

    @Operation(summary = "Tạo danh mục mới",
            description = "Tạo một danh mục mới",
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
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public Object createCategory(@RequestBody CategoryRequestDTO categoryDto) {
        return ResponseEntity.ok().body(categoryServiceImpl.createOne(categoryDto));
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
        return ResponseEntity.ok().body(categoryServiceImpl.getAll());
    }

    @Operation(summary = "Cập nhật danh mục",
            description = "Cập nhật danh mục",
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
    @SecurityRequirement(name = "bearerAuth")
    public Object updateCate(@PathVariable Long cateId, @RequestBody CategoryRequestDTO category )  {
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
    @PutMapping("/status/{cateId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateStatus(@PathVariable Long cateId, @RequestBody Boolean cate_status ) {
        // note : not using operator "=="
//        Boolean category_status = "true".equalsIgnoreCase(cate_status) ? true : false;
        return ResponseEntity.ok().body(categoryServiceImpl.updateStatus(cateId, cate_status));

    }


    @Operation(summary = "Xoá danh mục ",
            description = "Xoá danh mục",
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
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteCategory(@PathVariable Long cateId) throws Exception {
        return ResponseEntity.ok().body(categoryServiceImpl.deleteOne(cateId));
    }

    @GetMapping("/products/{categoryId}")
    public ResponseEntity<?> getProducts(@PathVariable Long categoryId ){
        return ResponseEntity.ok().body(categoryServiceImpl.getProductsById(categoryId));
    }
}

