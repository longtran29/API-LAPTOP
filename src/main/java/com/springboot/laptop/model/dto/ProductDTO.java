package com.springboot.laptop.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.laptop.model.dto.request.ProductDetailDTO;
import lombok.*;


import javax.validation.constraints.NotBlank;
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

    /*
    * Accept ProductDTO - create new product
    *
    * */

    public  ProductDTO () {}
    private String name;

    private boolean enabled;
    private Float original_price;
    private Float discount_percent;

    private String description;
    private boolean inStock;

    private List<ProductDetailDTO> details;
    private Long category;

    private Long brand;

    private Long productQuantity;

    @JsonIgnore
    public boolean isEmpty() {
        return (Objects.equals(this.name, "") || Objects.equals(this.description, "") || this.original_price == null || this.discount_percent == null ||
                this.productQuantity == null );

    }
}
