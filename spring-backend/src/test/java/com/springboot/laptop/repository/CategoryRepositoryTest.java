//package com.springboot.laptop.repository;
//
//import com.springboot.laptop.model.CategoryEntity;
//import com.springboot.laptop.service.CategoryService;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Rollback;
//
//import javax.transaction.Transactional;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(false)
//class CategoryRepositoryTest {
//
//    @Autowired private CategoryRepository categoryRepository;
//
//    @Autowired private CategoryService categoryService;
//
//    @Test
//    void findByName() {
//
//        CategoryEntity cate1  =categoryRepository.findByNameIgnoreCase("Dien thoai Di dong 2023").get();
//        System.out.println("value " + cate1.getName() + " " + cate1.getId());
//        Assertions.assertThat(cate1).isNotNull();
//    }
//
//
//
//
//}