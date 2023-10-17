package com.springboot.laptop.service.impl;


import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.mapper.BrandMapper;
import com.springboot.laptop.mapper.CategoryMapper;
import com.springboot.laptop.mapper.ProductMapper;
import com.springboot.laptop.model.BrandEntity;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.BrandDTO;
import com.springboot.laptop.model.dto.CategoryDTO;
import com.springboot.laptop.model.dto.request.BrandRequestDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.BrandRepository;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final BrandMapper brandMapper;

    @Override
    public Object findById(Long brandId) {
            return brandRepository.findById(brandId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND));
    }

    @Override
    public List<BrandDTO> getAllBrand() {
        return brandRepository.findAll().stream().map(brandMapper::entityToDTO).collect(Collectors.toList());
    }


    @Override
    public Object createOne(BrandRequestDTO newbrand)  {
        if(brandRepository.findByName(newbrand.getBrandName()).isPresent())
            throw new CustomResponseException(StatusResponseDTO.DUPLICATED_DATA);
        if(newbrand.getBrandName().isEmpty() || newbrand.getCateIds().size() <1) throw new CustomResponseException(StatusResponseDTO.DATA_EMPTY);

        BrandEntity brand = new BrandEntity();
        brand.setName(newbrand.getBrandName());
        for(Long i : newbrand.getCateIds()) {
            CategoryEntity setCategory = categoryRepository.findById(i).get();
            brand.getCategories().add(setCategory);
        }
        brand.setCreatedTimestamp(new Date());
        return brandMapper.entityToDTO(brandRepository.save(brand));
    }

    @Override
    public Object deleteOne(Long brandId) {
    BrandEntity brand = brandRepository.findById(brandId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND));
        try {
            brandRepository.delete(brand);
        }
        catch (Exception ex) {
            throw new CustomResponseException(StatusResponseDTO.BRAND_CONSTRAINT_EXCEPTION);
        }
        return "Delete successfully";
    }

    @Override
    public Object updateOne(Long brandId, BrandRequestDTO updateBrand) {
        BrandEntity brand = brandRepository.findById(brandId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND));
        BrandEntity existedBrand = null;
        if( brandRepository.findByName(updateBrand.getBrandName()).isPresent())
        {
            existedBrand =  brandRepository.findByName(updateBrand.getBrandName()).get();
            if(existedBrand.getId() != brandId)
                throw new CustomResponseException(StatusResponseDTO.DUPLICATED_DATA);
        }brand.setCategories(updateBrand.getCateIds().stream().map(cateId -> categoryRepository.findById(cateId).get()).collect(Collectors.toList()));
        brand.setName(updateBrand.getBrandName());
        brand.setModifiedTimestamp(new Date());
        return brandMapper.entityToDTO(brandRepository.saveAndFlush(brand));
    }

    @Override
    public List<CategoryDTO> getAllCateFromBrand(Long brandId) {
        brandRepository.findById(brandId).orElseThrow(() ->  new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND));
        return brandRepository.findById(brandId).get().getCategories().stream().map(categoryMapper::cateEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public Object getProductsById(Long brandId) {
        // return the value if present
        BrandEntity brand = brandRepository.findById(brandId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND));
        // using stream() before call forEach() / map() methods to loop through list -> execute add() method
        return brand.getProducts().stream().map(productMapper::productToProductDTO).collect(Collectors.toList());
    }

}
