package com.springboot.laptop.model.dto;


import com.springboot.laptop.model.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandRequestDto {

    private String brandName;
    private List<Long> cateIds = new ArrayList<>();

}
