package com.springboot.laptop.service;


import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.repository.BrandRepository;
import com.springboot.laptop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class BrandService {
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;

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
}
