package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity extends BaseEntity {

    @Column(nullable = false, name = "category_name")
    private String name;

    @Column(name="enabled")
    private Boolean enabled;


//    resolve error jackson - arraylist, collection
    @JsonBackReference("category_product")
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<ProductEntity> products;

    public CategoryEntity(String name, Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

}
