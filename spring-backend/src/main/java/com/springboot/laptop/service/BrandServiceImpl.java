package com.springboot.laptop.service;


import com.springboot.laptop.exception.DuplicatedDataException;
import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.request.BrandRequestDTO;
import com.springboot.laptop.repository.BrandRepository;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.service.impl.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.brandRepository = brandRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public BrandEntity findById(Long brandId) {
        return brandRepository.findById(brandId).get();
    }

    @Override
    public List<BrandEntity> getAll() {
        return brandRepository.findAll();
    }

    @Override
    public BrandEntity createOne(BrandRequestDTO newbrand) throws DuplicatedDataException {
        BrandEntity foundBrand = null;
        if(brandRepository.findByName(newbrand.getBrandName()).isPresent()) {
            foundBrand = brandRepository.findByName(newbrand.getBrandName()).get();
        }
        if (foundBrand != null) throw new DuplicatedDataException("Duplicated data");
        else {
            BrandEntity brand = new BrandEntity();
            brand.setName(newbrand.getBrandName());
            for(Long i : newbrand.getCateIds()) {
                CategoryEntity setCategory = categoryRepository.findById(i).get();
                brand.getCategories().add(setCategory);
            }
            brand.setCreationDate(new Date());
            brand.setModifiedDate(new Date());
            return brandRepository.save(brand);
        }
    }

    @Override
    public void deleteOne(Long brandId) {
        brandRepository.delete(brandRepository.findById(brandId).get());
    }

    @Override
    public BrandEntity updateOne(Long brandId, BrandRequestDTO updateBrand) throws DuplicatedDataException {
        BrandEntity brand = brandRepository.findById(brandId).get();

        BrandEntity foundBrand;
        if(brandRepository.findByName(updateBrand.getBrandName()).isPresent()) {
            foundBrand = brandRepository.findByName(updateBrand.getBrandName()).get();
            if(foundBrand.getId() != brandId) {
                throw new DuplicatedDataException("Duplicated data");
            }
        }
        List<CategoryEntity> listCate = new ArrayList<>();

        for(Long i : updateBrand.getCateIds()) {
            CategoryEntity refCate = categoryRepository.findById(i).get();
            listCate.add(refCate);
        }
        brand.setCategories(listCate);
        System.out.println("Brand name update " + updateBrand.getBrandName());
        brand.setName(updateBrand.getBrandName());
        brand.setModifiedDate(new Date());
        brandRepository.saveAndFlush(brand);

//        reference to exist list Categories
//        List<CategoryEntity> listCate = brand.getCategories();
        return brand;
    }

    @Override
    public List<CategoryEntity> getAllCateFromBrand(Long brandId) {
        return brandRepository.findCategoriesByBrandId(brandId);
    }

}
