package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Setter
@Getter
@Table(name="order_detail")
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
