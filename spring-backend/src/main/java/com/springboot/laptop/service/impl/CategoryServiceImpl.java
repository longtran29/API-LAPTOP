package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;

import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.request.CategoryRequestDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Object createOne(CategoryRequestDTO category) {
        CategoryEntity newCategory;
        if (categoryRepository.findByName(category.getName().replaceAll("\\s+", " ")).isPresent()) {
            throw new CustomResponseException(StatusResponseDTO.DUPLICATED_DATA);
        } else {
            newCategory = CategoryEntity.builder().name(category.getName()).enabled(category.getEnabled()).build();
            return categoryRepository.save(newCategory);
        }
    }

    @Override
    public CategoryEntity findById(Long categoryId) {
        if(!categoryRepository.findById(categoryId).isPresent()) {
            throw new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND);
        }
        return categoryRepository.findById(categoryId).get();
    }

    @Override
    public List<CategoryEntity> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Object updateOne(Long cateId, CategoryRequestDTO updateCategory) {

        if (!categoryRepository.findById(cateId).isPresent()) {
            throw new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND);
        }
        Optional<CategoryEntity> existingCategory = categoryRepository.findByName(updateCategory.getName().replaceAll("\\s+", " "));
        if (existingCategory.isPresent() && existingCategory.get().getId() != cateId) {
            throw new CustomResponseException(StatusResponseDTO.DUPLICATED_DATA);
        } else {
            CategoryEntity cateUpdate = categoryRepository.findById(cateId).get();
            cateUpdate.setName(updateCategory.getName());
            cateUpdate.setEnabled(updateCategory.getEnabled());
            return categoryRepository.save(cateUpdate);
        }

    }

    @Override
    public void updateStatus(Long cateId, Boolean status) {
        if (!categoryRepository.findById(cateId).isPresent()) {
            throw new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND);
        } else {
            categoryRepository.updateStatus(cateId, status);
        }
    }

    @Override
    public void deleteOne(Long cateId) throws Exception {
        CategoryEntity foundCategory = categoryRepository.findById(cateId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND));
        categoryRepository.delete(foundCategory);
    }
}