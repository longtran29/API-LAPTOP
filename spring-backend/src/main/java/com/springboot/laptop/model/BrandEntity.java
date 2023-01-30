package com.springboot.laptop.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandEntity extends BaseEntity {

    private String name;


    private Date creationDate;
    private Date modifiedDate;

    @ManyToMany
    @JoinTable(
            name="brands_categories",
            joinColumns = @JoinColumn(name="brand_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="category_id", referencedColumnName = "id")
    )
    private Set<CategoryEntity> categories = new HashSet<>();



}
