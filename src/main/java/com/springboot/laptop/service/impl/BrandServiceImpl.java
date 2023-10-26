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
import com.springboot.laptop.utils.StringUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final BrandMapper brandMapper;
    private final StringUtility stringUtility = new StringUtility();

    @Override
    public Object findById(Long brandId) {
            return brandRepository.findById(brandId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND));
    }

    @Override
    public List<BrandDTO> getAllBrand() {
        return brandRepository.findAll().stream().map(brandMapper::entityToDTO).collect(Collectors.toList());
    }


    @Override
    public Object createOne(BrandRequestDTO newbrand) throws Exception {
        if(!StringUtils.hasText(newbrand.getBrandName())) throw new RuntimeException(String.valueOf(StatusResponseDTO.BRAND_NAME_MUST_PROVIDED));
        if(brandRepository.findByNameIgnoreCase(stringUtility.removeExtraSpace(newbrand.getBrandName())).isPresent())
            throw new CustomResponseException(StatusResponseDTO.BRAND_CONFLICT_NAME);
        if(newbrand.getBrandName().isEmpty() || newbrand.getCateIds().size() < 1) throw new CustomResponseException(StatusResponseDTO.DATA_EMPTY);

        BrandEntity brand = new BrandEntity();
        brand.setName(stringUtility.removeExtraSpace(newbrand.getBrandName()));


        newbrand.getCateIds().forEach(i -> {
            CategoryEntity setCategory = categoryRepository.findById(i).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND));
            brand.getCategories().add(setCategory);
        });
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
    public Object updateOne(Long brandId, BrandRequestDTO updateBrand) throws Exception {
        if(!StringUtils.hasText(updateBrand.getBrandName())) throw new RuntimeException(String.valueOf(StatusResponseDTO.BRAND_NAME_MUST_PROVIDED));
        BrandEntity brand = brandRepository.findById(brandId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND));
        BrandEntity existedBrand ;
        if( brandRepository.findByNameIgnoreCase(stringUtility.removeExtraSpace(updateBrand.getBrandName())).isPresent())
        {
            existedBrand =  brandRepository.findByNameIgnoreCase(updateBrand.getBrandName().replaceAll("\\s{2,}", " ").toLowerCase().trim()).get();
            if(!Objects.equals(existedBrand.getId(), brandId))
                throw new CustomResponseException(StatusResponseDTO.BRAND_CONFLICT_NAME);
        }brand.setCategories(updateBrand.getCateIds().stream().map(cateId -> categoryRepository.findById(cateId).orElseThrow(()-> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND))).collect(Collectors.toList()));
        brand.setName(stringUtility.removeExtraSpace(updateBrand.getBrandName()));
        brand.setModifiedTimestamp(new Date());
        return brandMapper.entityToDTO(brandRepository.saveAndFlush(brand));
    }

    @Override
    public List<CategoryDTO> getAllCateFromBrand(Long brandId) {
        BrandEntity existingBrand = brandRepository.findById(brandId).orElseThrow(() ->  new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND));
        return existingBrand.getCategories().stream().map(categoryMapper::cateEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public Object getProductsById(Long brandId) {
        BrandEntity brand = brandRepository.findById(brandId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.BRAND_NOT_FOUND));
        return brand.getProducts().stream().map(productMapper::productToProductDTO).collect(Collectors.toList());
    }

}
