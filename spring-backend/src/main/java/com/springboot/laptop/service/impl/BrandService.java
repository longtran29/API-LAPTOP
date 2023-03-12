package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.DuplicatedDataException;
import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.BrandRequestDto;

import java.util.List;

public interface BrandService {
    public BrandEntity findById(Long brandId);
    public List<BrandEntity> getAll();
    public BrandEntity createOne(BrandRequestDto newbrand) throws DuplicatedDataException;
    public void deleteOne(Long brandId);
    public BrandEntity updateOne(Long brandId, BrandRequestDto updateBrand) throws DuplicatedDataException;
    public List<CategoryEntity> getAllCateFromBrand(Long brandId);
}
