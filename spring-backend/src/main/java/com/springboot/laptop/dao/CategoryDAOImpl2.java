package com.springboot.laptop.dao;

import com.springboot.laptop.model.CategoryEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;


@Component
public class CategoryDAOImpl2 implements CategoryDAO {

    private final JdbcTemplate jdbcTemplate;

    public CategoryDAOImpl2(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CategoryEntity getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM categories WHERE id = ?", getRowMapper(), id);
    }

    @Override
    public CategoryEntity findCategoryByName(String name) {
        return null;
    }

    @Override
    public CategoryEntity saveCategory(CategoryEntity category) {
        jdbcTemplate.update("INSERT INTO categories(category_name) VALUES (?)", category.getName());
        Long categoryId = jdbcTemplate.queryForObject("SELECT currval(pg_get_serial_sequence('categories', 'id'));", Long.class);

        System.out.println("Value id is " + categoryId);
        return this.getById(categoryId);
    }

    @Override
    public CategoryEntity updateCate(CategoryEntity category) {
        return null;
    }

    @Override
    public void deleteCateById(Long id) {

    }

    public RowMapper<CategoryEntity> getRowMapper() {
        return new CategoryMapper();
    }
}
