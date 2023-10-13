package com.springboot.laptop.repository;

import com.springboot.laptop.model.Customer;
import com.springboot.laptop.model.ProductEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(true)
@Transactional
class ReviewRepositoryTest {

    @Autowired private ReviewRepository reviewRepository;

    @Test
    void findByProduct() {
    }
//
//    @Test
//    void findByProductAndCustomer() {
//
//        ProductEntity product = new ProductEntity();
//        product.setId(Long.parseLong("5"));
//
//        Customer user = new Customer();
//        user.setId(Long.parseLong("1"));
//
//        System.out.println("comment is "+ reviewRepository.findByProductAndCustomer(product, user).getComment());
//
//        Assertions.assertThat(reviewRepository.findByProductAndCustomer(product, user)).isNotNull();
//
//
//
//    }
}