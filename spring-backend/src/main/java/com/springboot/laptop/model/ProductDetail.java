package com.springboot.laptop.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product_details")
@Getter
@Setter
public class ProductDetail  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
