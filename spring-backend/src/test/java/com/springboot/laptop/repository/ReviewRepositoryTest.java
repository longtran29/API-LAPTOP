package com.springboot.laptop.repository;

import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(true)
@Transactional
class ReviewRepositoryTest {

    @Autowired private ReviewRepository reviewRepository;

    @Test
    void findByProduct() {
    }

    @Test
    void findByProductAndCustomer() {

        ProductEntity product = new ProductEntity();
        product.setId(Long.parseLong("5"));

        UserEntity user = new UserEntity();
        user.setId(Long.parseLong("1"));

        System.out.println("comment is "+ reviewRepository.findByProductAndCustomer(product, user).getComment());

        Assertions.assertThat(reviewRepository.findByProductAndCustomer(product, user)).isNotNull();



    }
}