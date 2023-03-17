package com.springboot.laptop.repository;


import com.springboot.laptop.model.UserCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<UserCart, Long> {



    // annotation - delete cart and associated cart details
    @Transactional
    void deleteById(Long id);


}
