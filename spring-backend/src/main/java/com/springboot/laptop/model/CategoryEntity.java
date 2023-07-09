package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "category_name")
    private String name;

    @Column(name="enabled")
    private Boolean enabled;

    private String imageUrl;


//    resolve error jackson - arraylist, collection
    @JsonManagedReference(value = "category-products")
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<ProductEntity> products;



//    @ManyToMany(cascade = {CascadeType.MERGE})
//    @JoinTable(
//            name="categories_brands",
//            joinColumns = @JoinColumn(name="category_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name="brand_id", referencedColumnName = "id"),
//
//            uniqueConstraints={@UniqueConstraint(columnNames={"category_id", "brand_id"})}
//    )
    @JsonIgnoreProperties("categories")
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Collection<BrandEntity> brands;


}
