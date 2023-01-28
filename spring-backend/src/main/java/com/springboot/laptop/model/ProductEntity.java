package com.springboot.laptop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity extends BaseEntity {

    private String name;
    private String primaryImage;
    private String alias;
    private boolean enabled;
    private Float original_price;
    private Float discount_percent;

    @ManyToOne
    @JoinColumn(name="brand_id")
    private BrandEntity brand;
    private String description;
    private boolean inStock;
    private Date creationDate;
    private Date modifiedDate;

}
