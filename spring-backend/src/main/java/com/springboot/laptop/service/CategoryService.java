package com.springboot.laptop.service;

import com.springboot.laptop.exception.ResourceNotFoundException;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.ErrorCode;
import com.springboot.laptop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service

@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryEntity createOne(CategoryEntity category) {
        return this.categoryRepository.save(category);
    }

    public List<CategoryEntity> getAll() {
        return categoryRepository.findAll();
    }
    public CategoryEntity updateOne(Long cateId, CategoryEntity updateCategory) {
        CategoryEntity cateUpdate = categoryRepository.findById(cateId).get();
        cateUpdate.setName(updateCategory.getName());
        System.out.println("Cate update status "+cateUpdate.getEnabled());
        if(updateCategory.getEnabled() != cateUpdate.getEnabled()){
            categoryRepository.updateProductStatus(cateId, !cateUpdate.getEnabled());
        }
        System.out.println("Status update is " + cateUpdate.getEnabled());
        return cateUpdate;
    }

    public CategoryEntity deleteOne(Long cateId) throws ResourceNotFoundException {
        CategoryEntity foundCategory = categoryRepository.findById(cateId).orElseThrow(() -> new ResourceNotFoundException(""+ ErrorCode.FIND_CATEGORY_ERROR));
        categoryRepository.delete(foundCategory);
        return foundCategory;

    }
}
