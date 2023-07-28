package com.springboot.laptop.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Builder
public class ProductDetailDTO {

    private Long id;

    private String name;

    private String value;
}
