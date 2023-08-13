package com.springboot.laptop.repository;

import com.springboot.laptop.model.ProductEntity;
import com.springboot.laptop.model.ReviewEntity;
import com.springboot.laptop.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
        List<ReviewEntity> findByProduct(ProductEntity product);

        ReviewEntity findByProductAndCustomer(ProductEntity product, UserEntity user);
}
