package com.springboot.laptop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


    private String primaryImage;

    private boolean enabled;
    private Float original_price;
    private Float discount_percent;

    @JsonBackReference(value = "brand-products")
    @ManyToOne
    @JoinColumn(name="brand_id")
    private BrandEntity brand;

//    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private boolean inStock;
    private Date creationDate;
    private Date modifiedDate;

//    many instances of ProductEntity can be associated with one instance of CategoryEntity, a product belongs to
//    only one category
    @JsonBackReference(value = "category-products")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(name ="product_qty")
    private Long productQuantity;

}
