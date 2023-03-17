package com.springboot.laptop.model.dto.response;

import com.springboot.laptop.model.Address;
import com.springboot.laptop.model.OrderDetails;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private UserEntity user;

    private List<OrderDetails> orderDetails;

    private LocalDateTime orderDate;

    private float total;

    @Enumerated
    OrderStatus orderStatus;

    private Address address;


}
