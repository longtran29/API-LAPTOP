package com.springboot.laptop.model.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDTO {

    private String name;

    private Boolean enabled = true;

    private String imageUrl;
}
