package com.springboot.laptop.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AddressResponseDTO {

    private String address;
    private String city;
    private String country;
    private String zipcode;

    private String phoneNumber;

}
