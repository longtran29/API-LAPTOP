package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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


    @JsonBackReference("order_detail_order")
    @ManyToOne
    @JoinColumn(name="order_id", referencedColumnName = "id")
    private Order order;


    @JsonBackReference("order_detail_product")
    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private ProductEntity product;

    private Long quantity;
    private float total;

}
