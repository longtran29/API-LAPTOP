package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;

import com.springboot.laptop.mapper.CategoryMapper;
import com.springboot.laptop.mapper.ProductMapper;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.CategoryDTO;
import com.springboot.laptop.model.dto.request.CategoryRequestDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    @Override
    public Object createOne(CategoryRequestDTO category) {
        if (categoryRepository.findByName(category.getName().replaceAll("\\s+", " ")).isPresent())
            throw new CustomResponseException(StatusResponseDTO.DUPLICATED_DATA);
        CategoryDTO saveCate = new CategoryDTO();
        saveCate.setEnabled(true);
        saveCate.setName(category.getName());
        saveCate.setImageUrl(category.getImageUrl());
        saveCate.setCreatedTimestamp(new Date());
        return categoryRepository.save(categoryMapper.dtoCateToEntity(saveCate));
    }

    @Override
    public CategoryDTO findById(Long categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND));
        return categoryMapper.cateEntityToDTO(categoryRepository.findById(categoryId).get());
    }

    @Override
    public List<CategoryDTO> getAll() {
        List<CategoryDTO> returnedCates = new ArrayList<>();
        categoryRepository.findAll().forEach(cate -> returnedCates.add(categoryMapper.cateEntityToDTO(cate)));
        return returnedCates;
    }

    @Override
    public Object updateOne(Long cateId, CategoryRequestDTO updateCategory) {

        categoryRepository.findById(cateId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND));
        Optional<CategoryEntity> existingCategory = categoryRepository.findByName(updateCategory.getName().replaceAll("\\s+", " "));
        if (existingCategory.isPresent() && !Objects.equals(existingCategory.get().getId(), cateId)) {
            throw new CustomResponseException(StatusResponseDTO.DUPLICATED_DATA);
        } else {
            if (categoryRepository.findById(cateId).isPresent()) {
                CategoryEntity cateUpdate = categoryRepository.findById(cateId).get();
                cateUpdate.setName(updateCategory.getName());
                cateUpdate.setEnabled(updateCategory.getEnabled());
                cateUpdate.setImageUrl(updateCategory.getImageUrl());
                cateUpdate.setModifiedTimestamp(new Date());
                return categoryRepository.save(cateUpdate);
            } else {
                throw new RuntimeException("Không tìm thấy danh mục này");
            }

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
        if (existingCategory.getProducts().size() > 0)
            throw new CustomResponseException(StatusResponseDTO.CATEGORY_CONFLICT_PRODUCTS);
        if (existingCategory.getBrands().size() > 0)
            throw new CustomResponseException(StatusResponseDTO.CATEGORY_CONFLICT_BRAND);
        categoryRepository.delete(existingCategory);
        return "Delete successfully";
    }

}