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
public class ProductEntity extends BaseEntity {

    private String name;
    private String primaryImage;
    private String alias;
    private boolean enabled;
    private Float original_price;
    private Float discount_percent;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="brand_id")
    private BrandEntity brand;
    private String description;
    private boolean inStock;
    private Date creationDate;
    private Date modifiedDate;

//    many instances of ProductEntity can be associated with one instance of CategoryEntity, a product belongs to
//    only one category
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(name ="product_qty")
    private Long productQuantity;

}
