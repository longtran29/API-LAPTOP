package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @NotBlank(message = "Product name must be not empty")
    @Size(min = 3, max = 100)
    private String name;

    @NotNull(message = "You must upload image")
    @Column(name="primary_image")
    private String primaryImage;

    private boolean enabled;


    @NotNull(message = "Product must have price")
    private Float original_price;


    @NotNull(message = "Product must have price")
    private Float discount_percent;

    @JsonBackReference(value = "brand-products")
    @NotNull(message = "Product must belong to a brand")
    @ManyToOne
    @JoinColumn(name="brand_id")
    private BrandEntity brand;

    @NotBlank(message = "Product does not have description")
    @Size(min = 3, max = 30)
    private String description;

    @Column(name = "in_stock")
    private boolean inStock;


//    many instances of ProductEntity can be associated with one instance of CategoryEntity, a product belongs to
//    only one category
    @JsonBackReference(value = "category-products")
    @NotNull(message = "Product must belong to category")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @NotNull(message = "Product must have quantity")
    @Positive
    @Min(1)
    @Column(name ="product_qty")
    private Long productQuantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetail> details = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImportDetail> importDetails = new ArrayList<>();
}
