package com.springboot.laptop.model.dto;


import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.ProductEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private Long id;

    private String name;

    private Boolean enabled;

    private String imageUrl;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTimestamp;

//    private Collection<ProductEntity> products;
//
//    private Collection<BrandEntity> brand;

}
