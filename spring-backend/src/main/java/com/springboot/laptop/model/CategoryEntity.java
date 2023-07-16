package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CategoryEntity category = (CategoryEntity) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
