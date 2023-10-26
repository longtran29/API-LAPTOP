package com.springboot.laptop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateInformationDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("phone_number")
    private String phoneNumber;
}
