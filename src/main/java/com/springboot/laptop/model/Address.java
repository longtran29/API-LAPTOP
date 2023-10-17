package com.springboot.laptop.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore             // prevent json bind when get the user information
    private Customer customer;

    @NotEmpty(message = "Address must be not empty")
    private String address;

    @NotEmpty(message = "City must be not empty")
    private String city;

    @NotEmpty(message = "Country must be not empty")
    private String country;

    @NotEmpty(message = "Zipcode must be not empty")
    private String zipcode;

    @Column(name="phone_number", nullable = false)
    private String phoneNumber;

    //    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "address")
    List<Order> orders = new ArrayList<>();


}
