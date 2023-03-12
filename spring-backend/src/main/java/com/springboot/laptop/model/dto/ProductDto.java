package com.springboot.laptop.model.dto;

import com.springboot.laptop.model.BrandEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String name;
    private String primaryImage;
    private String alias;
    private boolean enabled;
    private Float original_price;
    private Float discount_percent;

    private Long brandId;

    private Long productQty;

    private Long categoryId;
    private String description;
    private boolean inStock;

}
