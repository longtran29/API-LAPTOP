package com.springboot.laptop.model.dto;


import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.ProductEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Builder
public class CategoryDTO {
    private Long id;

    private String name;

    private Boolean enabled;
    private String imageUrl;

    private Collection<ProductEntity> products = new ArrayList<>();

    private Collection<BrandEntity> brands = new ArrayList<>();

}
