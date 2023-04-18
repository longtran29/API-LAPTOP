package com.springboot.laptop.service;


import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.request.CategoryRequestDTO;

import java.util.List;

public interface CategoryService {

    public Object createOne(CategoryRequestDTO category) ;
    public CategoryEntity findById(Long categoryId);
    public List<CategoryEntity> getAll();
    public Object updateOne(Long cateId, CategoryEntity updateCategory);
    public Object updateStatus(Long cateId, Boolean status);
    public void deleteOne(Long cateId) throws Exception;
}
