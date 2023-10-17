package com.springboot.laptop.model.dto.response;

import com.springboot.laptop.model.dto.BrandDTO;
import com.springboot.laptop.model.dto.CategoryDTO;
import com.springboot.laptop.model.dto.ImageDTO;
import com.springboot.laptop.model.dto.request.ProductDetailDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class ProductResponseDTO {


    private Long id;

    private String name;
    private boolean enabled;
    private Float original_price;
    private Float discount_percent;
    private String primaryImage;

    private String description;
    private boolean inStock;
    private Date creationDate;
    private Date modifiedDate;

    private List<ProductDetailDTO> details;
    private CategoryDTO category;

    private BrandDTO brand;

    private Long productQuantity;

    private Set<ImageDTO> images;
}
