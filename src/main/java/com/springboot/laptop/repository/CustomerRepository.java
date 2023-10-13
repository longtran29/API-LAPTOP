package com.springboot.laptop.repository;

import com.springboot.laptop.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsernameIgnoreCase(String username);

    Optional<Customer> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("UPDATE Customer p SET p.enabled=?2 WHERE p.id = ?1")
    @Modifying(clearAutomatically=true)
    public void updateStatus(Long id, Boolean enabled);

}
