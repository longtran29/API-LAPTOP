package com.springboot.laptop.mapper;


import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.CategoryDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {

    CategoryEntity dtoCateToEntity(CategoryDTO category);

    CategoryDTO cateEntityToDTO(CategoryEntity category);
}
