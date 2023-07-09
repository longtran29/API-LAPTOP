package com.springboot.laptop.model.dto;

import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.ProductEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BrandDTO {

    public BrandDTO() {

    }

    private String name;


    private Date creationDate;
    private Date modifiedDate;

    private List<CategoryEntity> categories = new ArrayList<>();


    private List<ProductEntity> products = new ArrayList<>();
}
