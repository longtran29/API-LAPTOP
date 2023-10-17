package com.springboot.laptop.model.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandRequestDTO {

    private String brandName;
    private List<Long> cateIds = new ArrayList<>();

}
