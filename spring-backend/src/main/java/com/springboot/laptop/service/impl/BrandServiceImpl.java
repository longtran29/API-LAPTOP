package com.springboot.laptop.service.impl;


import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.request.BrandRequestDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.BrandRepository;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.repository.UserRepository;
import com.springboot.laptop.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

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
    public Object findById(Long brandId) {
        try {
            return brandRepository.findById(brandId).get();
        }  catch (NoSuchElementException ex) {
            throw new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND);
        }  catch (Exception ex) {
            throw new CustomResponseException(StatusResponseDTO.INTERNAL_SERVER);
        }
    }

    @Override
    public List<BrandEntity> getAll() {
        // we also can try -catch here
        return brandRepository.findAll();
    }


    @Override
    public BrandEntity createOne(BrandRequestDTO newbrand)  {
        BrandEntity foundBrand = null;
        if(newbrand.getBrandName().isEmpty() || newbrand.getCateIds().size() <1) throw new CustomResponseException(StatusResponseDTO.DATA_EMPTY);
        if(brandRepository.findByName(newbrand.getBrandName()).isPresent()) {
            foundBrand = brandRepository.findByName(newbrand.getBrandName()).get();
        }
        if (foundBrand != null) throw new CustomResponseException(StatusResponseDTO.DUPLICATED_DATA);
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
    public Object deleteOne(Long brandId) {
            if(brandRepository.findById(brandId).isPresent()) {
                try {
                    BrandEntity brand = brandRepository.findById(brandId).get();
                    brandRepository.delete(brand);
                    return "Xoá thành công";
                } catch (NoSuchElementException ex) {
                    throw new NoSuchElementException();
                }
                catch (Exception ex) {
                    throw new CustomResponseException(StatusResponseDTO.BRAND_CONSTRAINT_EXCEPTION);
                }
            } else {
                throw new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND);
            }

    }

    @Override
    public BrandEntity updateOne(Long brandId, BrandRequestDTO updateBrand) {
        BrandEntity brand = brandRepository.findById(brandId).get();
        if(!brandRepository.findById(brandId).isPresent()) {
            throw new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND);
        }

        BrandEntity foundBrand;
        if(brandRepository.findByName(updateBrand.getBrandName()).isPresent()) {
            foundBrand = brandRepository.findByName(updateBrand.getBrandName()).get();
            if(foundBrand.getId() != brandId) {
                throw new CustomResponseException(StatusResponseDTO.DUPLICATED_DATA);
            }
        }
        List<CategoryEntity> listCate = new ArrayList<>();

        for(Long i : updateBrand.getCateIds()) {
            CategoryEntity refCate = categoryRepository.findById(i).get();
            listCate.add(refCate);
        }
        brand.setCategories(listCate);
        brand.setName(updateBrand.getBrandName());
        brand.setModifiedDate(new Date());
        brandRepository.saveAndFlush(brand);

//        reference to exist list Categories
//        List<CategoryEntity> listCate = brand.getCategories();
        return brand;
    }

    @Override
    public List<CategoryEntity> getAllCateFromBrand(Long brandId) {
        if(!brandRepository.findById(brandId).isPresent()) {
            throw new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND);
        } else {
            return brandRepository.findCategoriesByBrandId(brandId);
        }
    }

}
