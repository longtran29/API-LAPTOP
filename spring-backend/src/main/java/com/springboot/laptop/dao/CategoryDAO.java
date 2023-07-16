package com.springboot.laptop.dao;

import com.springboot.laptop.model.CategoryEntity;

import java.util.List;

public interface CategoryDAO {

    List<CategoryEntity> findAllCate();

    CategoryEntity getById(Long id);


    CategoryEntity findCategoryByName(String name);

    CategoryEntity saveCategory(CategoryEntity category);

    CategoryEntity updateCate(CategoryEntity category);

    void deleteCateById(Long id);
}
