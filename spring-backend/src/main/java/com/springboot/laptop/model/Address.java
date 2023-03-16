package com.springboot.laptop.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseEntity {

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    private String address;
    private String city;
    private String country;
    private String zipcode;

    private String phoneNumber;

    @OneToMany(mappedBy = "address")
    List<Order> orders = new ArrayList<>();

}
