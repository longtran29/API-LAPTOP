package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderDetails extends  BaseEntity {


//    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="order_id", referencedColumnName = "id")
    private Order order;


//    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private ProductEntity product;

    @Min(1)
    private Long quantity;

    private float total;

}
