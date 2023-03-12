package com.springboot.laptop.repository;

import com.springboot.laptop.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("UPDATE ProductEntity p SET p.enabled=?2 WHERE p.id = ?1")
    @Modifying
    public void updateStatus(Long id, Boolean enabled);
}
