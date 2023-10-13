package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="cart_detail")
@NoArgsConstructor
@AllArgsConstructor
public class CartDetails extends  BaseEntity{

    @Min(1)
    private Long quantity;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="cart_id", referencedColumnName = "id")
    private UserCart userCart;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private ProductEntity product;

}
