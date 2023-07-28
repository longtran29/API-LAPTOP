package com.springboot.laptop.model.dto;

import com.springboot.laptop.model.dto.request.ProductDetailDTO;
import lombok.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Data
public class ProductDTO {

    public  ProductDTO () {}

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
    private Long category;

    private Long brand;

    private Long productQuantity;

    private List<ImageDTO> images;

    public boolean isEmpty() {
        return (Objects.equals(this.name, "") || Objects.equals(this.description, "") || this.original_price == null || this.discount_percent == null ||
                this.productQuantity == null );

    }
}
