package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetails extends  BaseEntity{

    private Long quantity;

    private LocalDateTime addDate;


    private LocalDateTime modifyDate;

    @JsonBackReference("cart_detail_user_cart")
    @ManyToOne
    @JoinColumn(name="cart_id", referencedColumnName = "id")
    private UserCart userCart;


    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private ProductEntity product;

}
