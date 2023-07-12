package com.springboot.laptop.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandEntity extends BaseEntity {

    @NotEmpty(message = "Brand name must be not empty")
    private String name;


    //    @JsonManagedReference(value="brands-categories")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name="categories_brands",
            joinColumns = @JoinColumn(name="brand_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="category_id", referencedColumnName = "id")
    )
    @JsonIgnoreProperties("brands")
    private List<CategoryEntity> categories = new ArrayList<>();

    //    resolve error jackson - arraylist, collection
    @JsonManagedReference(value = "brand-products")
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products = new ArrayList<>();

}
