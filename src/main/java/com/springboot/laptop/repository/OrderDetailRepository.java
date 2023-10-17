package com.springboot.laptop.repository;

import com.springboot.laptop.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetails, Long> {

}
