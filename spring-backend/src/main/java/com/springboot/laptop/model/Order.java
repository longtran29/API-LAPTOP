package com.springboot.laptop.model;

import com.springboot.laptop.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order extends  BaseEntity{

    @ManyToOne
    @JoinColumn(name= "user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetails = new ArrayList<>();

    private String address;

    private LocalDateTime orderDate;

    private float total;

    @Enumerated
    OrderStatus orderStatus;

}
