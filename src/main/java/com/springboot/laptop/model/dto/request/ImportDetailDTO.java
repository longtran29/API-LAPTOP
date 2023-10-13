package com.springboot.laptop.model.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportDetailDTO {

    private Long product;

    private Long quantity;

    private double price;
}
