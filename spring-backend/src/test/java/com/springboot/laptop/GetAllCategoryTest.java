package com.springboot.laptop;


import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.repository.CategoryRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {PostgresContainerInitializer.class})
public class GetAllCategoryTest {
    @Autowired
    private MockMvc mvc;

    @Autowired private CategoryRepository categoryRepository;

    @BeforeEach
    void addNewCategory() {
        categoryRepository.deleteAllInBatch(); // delete all cates


        List<CategoryEntity> listCates = new ArrayList<>();

        listCates.add(new CategoryEntity("Dien thoai hay"));
        listCates.add(new CategoryEntity("Laptop hay"));
        listCates.add(new CategoryEntity("Xiaomi hay"));
        listCates.add(new CategoryEntity("Mẫu Iphone 11"));

        categoryRepository.saveAll(listCates);

    }


    @Test
    void getAllCategories() throws Exception {

    mvc.perform(MockMvcRequestBuilders.get("/api/v1/categories"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(4)))

        ;
    }
}
