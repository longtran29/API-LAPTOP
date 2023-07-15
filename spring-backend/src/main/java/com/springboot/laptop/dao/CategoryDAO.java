package com.springboot.laptop.dao;

import com.springboot.laptop.model.CategoryEntity;

public interface CategoryDAO {

    CategoryEntity findById(Long cateId);
}
