package com.springboot.laptop.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandEntity extends BaseEntity {

    private String name;


    private Date creationDate;
    private Date modifiedDate;



//    @JsonManagedReference(value="brands-categories")
@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name="categories_brands",
            joinColumns = @JoinColumn(name="brand_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="category_id", referencedColumnName = "id")
//            uniqueConstraints={@UniqueConstraint(columnNames={"brand_id", "category_id"})}
    )
    @JsonIgnoreProperties("brands")
    private List<CategoryEntity> categories = new ArrayList<>();




    //    resolve error jackson - arraylist, collection
    @JsonManagedReference(value = "brand-products")
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products = new ArrayList<>();

}
