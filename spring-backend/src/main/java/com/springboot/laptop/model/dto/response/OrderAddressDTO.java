package com.springboot.laptop.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAddressDTO {


    private String address;
    private String city;
    private String country;
    private String zipcode;

    private String phoneNumber;
}
