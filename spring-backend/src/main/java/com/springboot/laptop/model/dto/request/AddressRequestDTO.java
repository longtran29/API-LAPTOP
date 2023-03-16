package com.springboot.laptop.model.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDTO {
    private String address;
    private String city;
    private String country;
    private String zipcode;
    private String phoneNumber;
}
