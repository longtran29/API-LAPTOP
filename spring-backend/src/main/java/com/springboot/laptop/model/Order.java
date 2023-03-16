package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

// named the table to  prevent the reversed keyword "order ...by" when querying
@Table(name="orders")
public class Order extends  BaseEntity{


    @JsonIgnoreProperties("user")
    @ManyToOne
    @JoinColumn(name= "user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetails = new ArrayList<>();

    private LocalDateTime orderDate;

    private float total;

    @Enumerated
    OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Address address;

}
