package com.springboot.laptop.service;

import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.BrandDTO;
import com.springboot.laptop.model.dto.CategoryDTO;
import com.springboot.laptop.model.dto.request.BrandRequestDTO;

import java.util.List;

public interface BrandService {
    public Object findById(Long brandId);
    public List<BrandDTO> getAllBrand();
    public Object createOne(BrandRequestDTO createBrand) throws Exception;
    public Object deleteOne(Long brandId);
    public Object updateOne(Long brandId, BrandRequestDTO updateBrand) throws Exception;
    public List<CategoryDTO> getAllCateFromBrand(Long brandId);

    Object getProductsById(Long brandId);
}
