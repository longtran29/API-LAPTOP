package com.springboot.laptop.repository;


import com.springboot.laptop.model.UserCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<UserCart, Long> {

}
