package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Setter
@Getter
@Table(name="order_detail")
@NoArgsConstructor
@AllArgsConstructor

public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

//    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="order_id", referencedColumnName = "id")
    @JsonIgnore
    private Order order;

//    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private ProductEntity product;

    @Min(1)
    private Long quantity;

    private float total;

}
