package com.springboot.laptop.service;


import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.CategoryDTO;
import com.springboot.laptop.model.dto.request.CategoryRequestDTO;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public interface CategoryService {

    public Object createOne(CategoryRequestDTO category) ;
    public CategoryDTO findById(Long categoryId);
    public List<CategoryDTO> getAll();
    public Object updateOne(Long cateId, CategoryRequestDTO updateCategory);
    public Object updateStatus(Long cateId, Boolean status);
    public Object deleteOne(Long cateId) throws DataIntegrityViolationException;
}
