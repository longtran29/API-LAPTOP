//package com.springboot.laptop;
//
//import com.springboot.laptop.model.CategoryEntity;
//import com.springboot.laptop.repository.CategoryRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.ApplicationContextInitializer;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.support.TestPropertySourceUtils;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import javax.transaction.Transactional;
//
//@SpringBootTest
////@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  //  de-active in-memory database
//@Testcontainers
////@Rollback(value = true)
////@ContextConfiguration(initializers = CategoryRepositoryTest.DataSourceInitializer.class)
//@TestPropertySource(properties = {
//        "spring.datasource.url=jdbc:tc:postgresql:12.9-alpine:///databasename"
//})
//public class CategoryRepositoryTest {
//
//    @Autowired private CategoryRepository categoryRepository;
//
////    @Container
////    private static final PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:12.9-alpine");
////
////
////    public static class DataSourceInitializer
////            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
////
////        @Override
////        public void initialize(ConfigurableApplicationContext applicationContext) {
////            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
////                    applicationContext,
////                    "spring.datasource.url=" + database.getJdbcUrl(),
////                    "spring.datasource.username=" + database.getUsername(),
////                    "spring.datasource.password=" + database.getPassword()
////            );
////        }
////    }
////
////    @BeforeAll
////    static void beforeAll() {
////        database.start();
////    }
////
////    @AfterAll
////    static void afterAll() {
////        database.close();
////    }
//
//    @Test
////    @Transactional
//    void addNewCategory() {
//
//        var topRestaurant = new CategoryEntity("Café Java10");
//        CategoryEntity saveCategory = categoryRepository.save(topRestaurant);
//        Assertions.assertThat(saveCategory).isNotNull();
//    }
//}
