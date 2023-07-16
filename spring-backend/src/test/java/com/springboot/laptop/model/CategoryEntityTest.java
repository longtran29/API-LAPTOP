package com.springboot.laptop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryEntityTest {

    @Test
    void testEquals() {

        CategoryEntity cate1 = new CategoryEntity();
        cate1.setId(1L);

        CategoryEntity cate2 = new CategoryEntity();
        cate2.setId(1L);

        assert cate1.equals(cate2);
    }

    @Test
    void testNotEquals() {

        CategoryEntity cate1 = new CategoryEntity();
        cate1.setId(1L);

        CategoryEntity cate2 = new CategoryEntity();
        cate2.setId(3L);

        assertFalse(cate1.equals(cate2));
    }
}