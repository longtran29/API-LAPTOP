package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity extends  BaseEntity  {

    @NotEmpty(message = "Product name must be not empty")
    private String name;

    @NotEmpty(message = "You must upload image")
    @Column(name="primary_image")
    private String primaryImage;

    private boolean enabled;

    private Float original_price;

    private Float discount_percent;

    @JsonBackReference(value = "brand-products")
    @ManyToOne
    @JoinColumn(name="brand_id")
    private BrandEntity brand;

    private String description;

    @Column(name = "in_stock")
    private boolean inStock;


//    many instances of ProductEntity can be associated with one instance of CategoryEntity, a product belongs to
//    only one category
    @JsonBackReference(value = "category-products")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(name ="product_qty")
    private Long productQuantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetail> details = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>();
}
