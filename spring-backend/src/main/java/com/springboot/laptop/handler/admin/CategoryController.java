package com.springboot.laptop.handler.admin;


import com.springboot.laptop.exception.DeleteDataFail;
import com.springboot.laptop.exception.ResourceNotFoundException;
import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.ErrorCode;
import com.springboot.laptop.model.dto.ResponseDTO;
import com.springboot.laptop.model.dto.SuccessCode;
import com.springboot.laptop.service.BrandService;
import com.springboot.laptop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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


    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryEntity category) {
        System.out.println("Category request body  is " + category.getName());
        CategoryEntity newCate = categoryService.createOne(category);
        return new ResponseEntity<CategoryEntity>(newCate, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<CategoryEntity> listCates = categoryService.getAll();
        return new ResponseEntity<List<CategoryEntity>>(listCates, HttpStatus.OK );
    }

    @PutMapping("/{cateId}")
    public ResponseEntity<CategoryEntity> updateStatus(@PathVariable Long cateId, @RequestBody CategoryEntity cate ) throws Exception {
        System.out.println("Category name " + cate.getName() + cate.getId());
        CategoryEntity cateUpdated = categoryService.updateOne(cateId, cate);
        return new ResponseEntity<CategoryEntity>(cateUpdated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{cateId}")
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
