package com.springboot.laptop.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

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
    private List<CategoryEntity> categories = new ArrayList<>();


    //    resolve error jackson - arraylist, collection
    @JsonBackReference
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products = new ArrayList<>();


}
