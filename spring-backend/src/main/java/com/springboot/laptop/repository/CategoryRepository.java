package com.springboot.laptop.repository;

import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository  extends JpaRepository<CategoryEntity, Long> {

    @Query("UPDATE CategoryEntity c SET c.enabled=?2 WHERE c.id = ?1")
    @Modifying
    public void updateStatus(Long id, Boolean enabled);

    Optional<CategoryEntity> findByName(String cateName);

}
