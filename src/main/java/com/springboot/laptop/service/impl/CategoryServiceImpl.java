package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;

import com.springboot.laptop.mapper.CategoryMapper;
import com.springboot.laptop.mapper.ProductMapper;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.CategoryDTO;
import com.springboot.laptop.model.dto.request.CategoryRequestDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.service.AmazonS3Service;
import com.springboot.laptop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final AmazonS3Service amazonS3Service;

    static String[] HEADERs = { "Id", "Name", "Enabled", "Image" };
    static String SHEET = "Category_List";

    @Override
    public Object createOne(CategoryRequestDTO category, MultipartFile multipartFile) {
        if (categoryRepository.findByName(category.getName().replaceAll("\\s+", " ")).isPresent())
            throw new CustomResponseException(StatusResponseDTO.DUPLICATED_DATA);
        CategoryDTO saveCate = new CategoryDTO();
        saveCate.setEnabled(true);
        saveCate.setName(category.getName());
        saveCate.setImageUrl(amazonS3Service.uploadImage(multipartFile));
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
    public Object updateOne(Long cateId, CategoryRequestDTO updateCategory, MultipartFile multipartFile) {

        categoryRepository.findById(cateId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND));
        Optional<CategoryEntity> existingCategory = categoryRepository.findByName(updateCategory.getName().replaceAll("\\s+", " "));
        if (existingCategory.isPresent() && !Objects.equals(existingCategory.get().getId(), cateId)) {
            throw new CustomResponseException(StatusResponseDTO.DUPLICATED_DATA);
        } else {
            CategoryEntity cateUpdate = categoryRepository.findById(cateId).get();

            if(!multipartFile.isEmpty()) cateUpdate.setImageUrl(amazonS3Service.uploadImage(multipartFile));
            cateUpdate.setName(updateCategory.getName());
            cateUpdate.setEnabled(updateCategory.getEnabled());
            cateUpdate.setModifiedTimestamp(new Date());
            return categoryRepository.save(cateUpdate);
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

    @Override
    public void exportToExcelFile(HttpServletResponse response) throws IOException {

       try {

           Workbook book = new XSSFWorkbook();

           ByteArrayOutputStream  out = new ByteArrayOutputStream();

           Sheet sheet = book.createSheet(SHEET);

           Row headerRow = sheet.createRow(0);

           AtomicInteger col = new AtomicInteger();

           Arrays.stream(HEADERs).forEach(header -> {
               Cell cell = headerRow.createCell(col.getAndIncrement());
               cell.setCellValue(header);
           });

           AtomicInteger rowIdx = new AtomicInteger(1);

           categoryRepository.findAll().forEach(category -> {

               Row row = sheet.createRow(rowIdx.getAndIncrement());


               row.createCell(0).setCellValue(category.getId());
               row.createCell(1).setCellValue(category.getName());
               row.createCell(2).setCellValue(category.getImageUrl());
               row.createCell(3).setCellValue(category.getEnabled());

           });
           ServletOutputStream outputStream = response.getOutputStream();
           book.write(outputStream);

       } catch(IOException ex) {

            throw ex;
       }
    }

}