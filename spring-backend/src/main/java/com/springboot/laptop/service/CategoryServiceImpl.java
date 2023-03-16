package com.springboot.laptop.service;

import com.springboot.laptop.exception.DuplicatedDataException;
import com.springboot.laptop.exception.ResourceNotFoundException;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.response.ErrorCode;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.service.impl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryEntity createOne(CategoryEntity category) throws DuplicatedDataException {
        CategoryEntity foundCate = null;
        if(categoryRepository.findByName(category.getName()).isPresent()) {
            foundCate = categoryRepository.findByName(category.getName()).get();
        }
        if (foundCate != null) throw new DuplicatedDataException("Duplicated data");
        else
            return this.categoryRepository.save(category);
    }
    @Override
    public CategoryEntity findById(Long categoryId) {
        return categoryRepository.findById(categoryId).get();
    }

    @Override
    public List<CategoryEntity> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity updateOne(Long cateId, CategoryEntity updateCategory) throws DuplicatedDataException {
        CategoryEntity foundCate;
        if(categoryRepository.findByName(updateCategory.getName()).isPresent()) {
            foundCate = categoryRepository.findByName(updateCategory.getName()).get();
            if(foundCate.getId() != cateId) {
                throw new DuplicatedDataException("Duplicated data");
            }
        }
        CategoryEntity cateUpdate = categoryRepository.findById(cateId).get();
        cateUpdate.setName(updateCategory.getName());
        if(updateCategory.getEnabled() != cateUpdate.getEnabled()){
            categoryRepository.updateStatus(cateId, !cateUpdate.getEnabled());
        }
        return cateUpdate;
    }

    @Override
    public void updateStatus(Long cateId, Boolean status) {
        categoryRepository.updateStatus(cateId, status);
    }

    @Override
    public CategoryEntity deleteOne(Long cateId) throws ResourceNotFoundException {
        CategoryEntity foundCategory = categoryRepository.findById(cateId).orElseThrow(() -> new ResourceNotFoundException(""+ ErrorCode.FIND_CATEGORY_ERROR));
        categoryRepository.delete(foundCategory);
        return foundCategory;

    }
}