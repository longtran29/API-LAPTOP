package com.springboot.laptop.repository;

import com.springboot.laptop.model.CartDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetails, Long> {
}
