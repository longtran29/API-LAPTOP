package com.springboot.laptop.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address extends BaseEntity {

//    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity user;

    private String address;
    private String city;
    private String country;
    private String zipcode;

    private String phoneNumber;

    @JsonManagedReference
    @OneToMany(mappedBy = "address")
    List<Order> orders = new ArrayList<>();

}
