package com.springboot.laptop.dao;

import com.springboot.laptop.model.CategoryEntity;

public interface CategoryDAO {

//    CategoryEntity findById(Long cateId);

    CategoryEntity getById(Long id);


    CategoryEntity findCategoryByName(String name);

    CategoryEntity saveCategory(CategoryEntity category);

    CategoryEntity updateCate(CategoryEntity category);

    void deleteCateById(Long id);
}
