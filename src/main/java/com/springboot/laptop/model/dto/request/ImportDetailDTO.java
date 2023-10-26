package com.springboot.laptop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportDetailDTO {

    @JsonProperty("import_product")
    private Long product;

    @JsonProperty("import_quantity")
    private Long quantity;

    @JsonProperty("import_price")
    private double price;
}
