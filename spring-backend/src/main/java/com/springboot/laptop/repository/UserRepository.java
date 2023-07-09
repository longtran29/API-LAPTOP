package com.springboot.laptop.repository;

import com.springboot.laptop.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("UPDATE UserEntity p SET p.enabled=?2 WHERE p.id = ?1")
    @Modifying
    public UserEntity updateStatus(Long id, Boolean enabled);
}
