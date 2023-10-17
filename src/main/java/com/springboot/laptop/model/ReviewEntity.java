package com.springboot.laptop.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(length = 300, nullable = false)
    @Size(min = 3, max = 40)
    @NotBlank(message  = "You must comment for the review")
    private String comment;

    @Positive
    @Range(min = 1, max = 5)
    private int rating;

    @Column(name="review_time",nullable = false)
    private Date reviewTime;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

//    @ManyToOne
//    @JoinColumn(name = "customer_id")
//    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // them truong orderCode để 1 customer có thể review product nhiều lần dựa theo mã đơn hàng
}
