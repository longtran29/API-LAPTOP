//package com.springboot.laptop;
//
//import org.jetbrains.annotations.NotNull;
//import org.springframework.context.ApplicationContextInitializer;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.test.context.support.TestPropertySourceUtils;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//
//public class PostgresContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//
//    private static final PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:12.9-alpine");
//
//    static {
//        database.start();
//    }
//    @Override
//    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
//        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
//                    applicationContext,
//                    "spring.datasource.url=" + database.getJdbcUrl(),
//                    "spring.datasource.username=" + database.getUsername(),
//                    "spring.datasource.password=" + database.getPassword()
//            );
//
//    }
//}
