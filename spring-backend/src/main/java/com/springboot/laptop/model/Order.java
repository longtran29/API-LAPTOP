package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.springboot.laptop.model.enums.OrderStatus;
import com.springboot.laptop.model.enums.PaymentMethod;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// named the table to  prevent the reversed keyword "order ...by" when querying
@Table(name="orders")
public class Order{

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "order_generator")
    @GeneratedValue(generator = "orderIdGenerator")
    @GenericGenerator(name = "orderIdGenerator",
            strategy = "com.springboot.laptop.utils.OrderGenerator")
    private String id;

    @Column(name ="created_timestamp",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTimestamp;

    @ManyToOne
    @JoinColumn(name= "user_id", referencedColumnName = "id")
    @JsonIgnore
    private UserEntity user;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetails = new ArrayList<>();

    @Temporal(TemporalType.DATE)
    @Column(name = "order_date")
    private Date orderDate;

    private float total;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    OrderStatus orderStatus;

//    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod methodPayment;

}
