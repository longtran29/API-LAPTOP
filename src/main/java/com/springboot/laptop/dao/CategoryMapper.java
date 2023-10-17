package com.springboot.laptop.dao;

import com.springboot.laptop.model.CategoryEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryMapper implements RowMapper<CategoryEntity> {


    @Override
    public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

        CategoryEntity category = new CategoryEntity();
        category.setName(rs.getString("category_name"));
        category.setImageUrl(rs.getString("imageurl"));
        category.setId(rs.getLong("id"));
        return category;
    }
}
