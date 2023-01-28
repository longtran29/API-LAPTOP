package com.springboot.laptop.service;


import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.repository.BrandRepository;
import com.springboot.laptop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository, UserRepository userRepository) {
        this.brandRepository = brandRepository;
        this.userRepository = userRepository;
    }

    public List<BrandEntity> getAll() {
        return brandRepository.findAll();
    }

    public BrandEntity createOne(BrandEntity brand) {
        brand.setCreationDate(new Date());
        brand.setModifiedDate(new Date());
//        brand.setAddedBy(userRepository.findByUsername(principal.getName()).get());
        return brandRepository.save(brand);
    }

    public void deleteOne(Long brandId) {
        brandRepository.delete(brandRepository.findById(brandId).get());
    }

    public BrandEntity updateOne(Long brandId, String name) throws Exception {
        Optional<BrandEntity> brand = brandRepository.findById(brandId);
        if(brand.isEmpty()) {
            throw  new Exception("No brand name with id "+ brandId + " found! ");
        }
        BrandEntity brandData = brand.get();
        brandData.setName(name);
        brandData.setModifiedDate(new Date());
        brandRepository.saveAndFlush(brandData);
        return brandData;
    }

}
