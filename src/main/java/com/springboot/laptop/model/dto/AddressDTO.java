package com.springboot.laptop.model.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressDTO {
    private Long id;

    private String address;
    private String city;
    private String country;
    private String zipcode;

    private String phoneNumber;

}
