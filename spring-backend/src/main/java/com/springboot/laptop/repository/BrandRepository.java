package com.springboot.laptop.repository;

import com.springboot.laptop.model.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {

}
