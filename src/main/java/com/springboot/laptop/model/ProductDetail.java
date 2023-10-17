package com.springboot.laptop.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "product_details")
@Getter
@Setter
@NoArgsConstructor
public class ProductDetail  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Detail name must not be empty")
    private String name;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Detail value must be provided")
    private String value;

    public ProductDetail(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
