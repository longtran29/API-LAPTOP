package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.DuplicatedDataException;
import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.request.BrandRequestDTO;

import java.util.List;

public interface BrandService {
    public Object findById(Long brandId);
    public List<BrandEntity> getAll();
    public Object createOne(BrandRequestDTO newbrand) ;
    public Object deleteOne(Long brandId);
    public BrandEntity updateOne(Long brandId, BrandRequestDTO updateBrand) throws DuplicatedDataException;
    public List<CategoryEntity> getAllCateFromBrand(Long brandId);
}
