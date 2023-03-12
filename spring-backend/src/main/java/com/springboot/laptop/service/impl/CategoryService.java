package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.DuplicatedDataException;
import com.springboot.laptop.exception.ResourceNotFoundException;
import com.springboot.laptop.model.CategoryEntity;

import java.util.List;

public interface CategoryService {

    public CategoryEntity createOne(CategoryEntity category) throws DuplicatedDataException;
    public CategoryEntity findById(Long categoryId);
    public List<CategoryEntity> getAll();
    public CategoryEntity updateOne(Long cateId, CategoryEntity updateCategory) throws DuplicatedDataException;
    public void updateStatus(Long cateId, Boolean status);
    public CategoryEntity deleteOne(Long cateId) throws ResourceNotFoundException;
}
