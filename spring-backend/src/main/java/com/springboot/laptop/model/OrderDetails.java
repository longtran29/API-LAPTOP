package com.springboot.laptop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails extends  BaseEntity {

    @ManyToOne
    @JoinColumn(name="order_id", referencedColumnName = "id")
    private Order order;


    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private ProductEntity product;

    private int quantity;
    private float productPrice;

    private float total;




}
