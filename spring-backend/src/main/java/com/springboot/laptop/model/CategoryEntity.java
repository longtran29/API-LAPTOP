package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class CategoryEntity extends  BaseEntity  {

    @Column(nullable = false, name = "category_name")
    @NotEmpty(message = "Category name must be not empty")
    private String name;

    @Column(name="enabled")
    private Boolean enabled;

    @NotEmpty(message = "Image must be not empty")
    private String imageUrl;

    public CategoryEntity(String name) {
        this.name = name;
    }

//    resolve error jackson - arraylist, collection
    @JsonManagedReference(value = "category-products")
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<ProductEntity> products;

    @JsonIgnoreProperties("categories")
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Collection<BrandEntity> brands;

    public CategoryEntity(String name) {
        this.name = name;
    }

}
