package com.springboot.laptop.model.dto;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.springboot.laptop.model.Order;
import com.springboot.laptop.model.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class AddressDTO {


    private String address;
    private String city;
    private String country;
    private String zipcode;

    private String phoneNumber;

}
