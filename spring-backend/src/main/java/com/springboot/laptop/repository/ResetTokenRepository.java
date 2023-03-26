package com.springboot.laptop.repository;

import com.springboot.laptop.model.ResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetTokenEntity, Long> {


}
