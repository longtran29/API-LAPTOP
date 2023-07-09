package com.springboot.laptop.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;

@Getter
@Setter
@Builder
public class ProductDTO {

    private Long id;

    private String name;
    private String primaryImage;
    private String alias;
    private boolean enabled;
    private Float original_price;
    private Float discount_percent;

    private String description;
    private boolean inStock;
    private Date creationDate;
    private Date modifiedDate;

    private Long category;

    private Long brand;

    private Long productQuantity;
}
