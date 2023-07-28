package com.springboot.laptop.model.dto;

import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.ProductEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    private Long id;

    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTimestamp;

//    private List<CategoryEntity> categories = new ArrayList<>();
//
//    private List<ProductEntity> products = new ArrayList<>();
}
