package com.springboot.laptop.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportDetailExportDTO {

    private Long id;

    private String name;

    private double price;

    private Long quantity;

    private Long product_id;
}
