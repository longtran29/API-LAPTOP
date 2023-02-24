package com.springboot.laptop.repository;

import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {

    Optional<BrandEntity> findByName(String brandName);
}
