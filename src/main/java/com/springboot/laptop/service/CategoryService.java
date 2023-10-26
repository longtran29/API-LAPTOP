package com.springboot.laptop.service;


import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.CategoryDTO;
import com.springboot.laptop.model.dto.request.CategoryRequestDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface CategoryService {

    public Object createOne(CategoryRequestDTO category, MultipartFile multipartFile) throws Exception;
    public CategoryDTO findById(Long categoryId);
    public List<CategoryDTO> getAll();
    Object updateOne(Long cateId, CategoryRequestDTO updateCategory,MultipartFile multipartFile) throws Exception;
    public Object updateStatus(Long cateId, Boolean status);
    public Object deleteOne(Long cateId) throws DataIntegrityViolationException;

    public void exportToExcelFile(HttpServletResponse response) throws IOException;
}
