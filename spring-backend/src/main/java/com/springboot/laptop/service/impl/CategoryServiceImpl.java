package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;

import com.springboot.laptop.mapper.CategoryMapper;
import com.springboot.laptop.mapper.ProductMapper;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.CategoryDTO;
import com.springboot.laptop.model.dto.ProductDTO;
import com.springboot.laptop.model.dto.request.CategoryRequestDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final EntityManager entityManager;

    @Override
    public Object createOne(CategoryRequestDTO category) {
        if(categoryRepository.findByName(category.getName().replaceAll("\\s+", " ")).isPresent())
            throw new CustomResponseException(StatusResponseDTO.DUPLICATED_DATA);
        CategoryEntity createdCategory = new CategoryEntity();
        createdCategory.setEnabled(true);
        createdCategory.setName(category.getName());
        createdCategory.setImageUrl(category.getImageUrl());
        categoryRepository.save(createdCategory);
        return categoryRepository.findAll();
    }

    @Override
    public CategoryDTO findById(Long categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND));
        return categoryMapper.cateEntityToDTO(categoryRepository.findById(categoryId).get());
    }

    @Override
    public List<CategoryDTO> getAll() {
        List<CategoryDTO> returnedCates = new ArrayList<>();
        categoryRepository.findAll().stream().forEach(cate -> returnedCates.add(categoryMapper.cateEntityToDTO(cate)));
        return returnedCates;
    }

    @Override
    public Object updateOne(Long cateId, CategoryRequestDTO updateCategory) {

        categoryRepository.findById(cateId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND));
        Optional<CategoryEntity> existingCategory = categoryRepository.findByName(updateCategory.getName().replaceAll("\\s+", " "));
        if (existingCategory.isPresent() && existingCategory.get().getId() != cateId) {
            throw new CustomResponseException(StatusResponseDTO.DUPLICATED_DATA);
        } else {
            CategoryEntity cateUpdate = categoryRepository.findById(cateId).get();
            cateUpdate.setName(updateCategory.getName());
            cateUpdate.setEnabled(updateCategory.getEnabled());
            cateUpdate.setImageUrl(updateCategory.getImageUrl());
            categoryRepository.save(cateUpdate);
            return categoryRepository.findAll();
        }

    }

    @Override
    @Transactional
    public Object updateStatus(Long cateId, Boolean status) {
        categoryRepository.findById(cateId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND));
        try {
            categoryRepository.updateStatus(cateId, status);
        } catch (Exception ex) {
            throw ex;
        }
        return categoryRepository.findAll();
    }

    @Override
    public Object deleteOne(Long cateId) throws DataIntegrityViolationException {
        CategoryEntity existingCategory = categoryRepository.findById(cateId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND));
        if(existingCategory.getProducts().size() > 0) throw new CustomResponseException(StatusResponseDTO.CATEGORY_CONFLICT_PRODUCTS);
        if(existingCategory.getBrands().size() > 0) throw new CustomResponseException(StatusResponseDTO.CATEGORY_CONFLICT_BRAND);
        categoryRepository.delete(existingCategory);
        return categoryRepository.findAll();
    }

    @Override
    public Object getProductsById(Long categoryId) {
        CategoryEntity existingCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND));
        List<ProductDTO> returnedProds = new ArrayList<>();
        // using stream() before call forEach() methods to loop through list and can call add() method on an object
        existingCategory.getProducts().stream().forEach(product -> returnedProds.add(productMapper.productToProductDTO(product)));
        return returnedProds;
    }
}