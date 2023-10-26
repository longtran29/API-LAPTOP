package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;

import com.springboot.laptop.mapper.CategoryMapper;
import com.springboot.laptop.mapper.ProductMapper;
import com.springboot.laptop.model.CategoryEntity;
import com.springboot.laptop.model.dto.CategoryDTO;
import com.springboot.laptop.model.dto.request.CategoryRequestDTO;
import com.springboot.laptop.model.dto.response.ResponseObject;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.CategoryRepository;
import com.springboot.laptop.service.AmazonS3Service;
import com.springboot.laptop.service.CategoryService;
import com.springboot.laptop.utils.StringUtility;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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

    private final StringUtility stringUtility = new StringUtility();

    static String[] HEADERs = { "Id", "Name", "Enabled", "Image" };
    static String SHEET = "Category_List";

    @Override
    public Object createOne(CategoryRequestDTO category, MultipartFile multipartFile) throws Exception {
        if(!StringUtils.hasText(category.getName())) throw new RuntimeException(String.valueOf(StatusResponseDTO.CATEGORY_NAME_MUST_PROVIDED));
        if(multipartFile.isEmpty()) throw new RuntimeException(String.valueOf(StatusResponseDTO.IMAGE_MUST_PROVIDED));
        if (categoryRepository.findByName(stringUtility.removeExtraSpace(category.getName())).isPresent())
            throw new CustomResponseException(StatusResponseDTO.CATEGORY_CONFLICT_NAME);
        CategoryDTO saveCate = new CategoryDTO();
        saveCate.setEnabled(true);
        saveCate.setName(stringUtility.removeExtraSpace(category.getName()));
        saveCate.setImageUrl(amazonS3Service.uploadImage(multipartFile));
        saveCate.setCreatedTimestamp(new Date());
        return categoryRepository.save(categoryMapper.dtoCateToEntity(saveCate));
    }

    @Override
    public CategoryDTO findById(Long categoryId) {
        CategoryEntity existingCate = categoryRepository.findById(categoryId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND));
        return categoryMapper.cateEntityToDTO(existingCate);
    }

    @Override
    public List<CategoryDTO> getAll() {
        List<CategoryDTO> listCate = new ArrayList<>();
        categoryRepository.findAll().forEach(cate -> listCate.add(categoryMapper.cateEntityToDTO(cate)));
        return listCate;
    }

    @Override
    public Object updateOne(Long cateId, CategoryRequestDTO updateCategory, MultipartFile multipartFile) throws Exception {
        if(!StringUtils.hasText(updateCategory.getName())) throw new RuntimeException(String.valueOf(StatusResponseDTO.CATEGORY_NAME_MUST_PROVIDED));
        Optional<CategoryEntity> existingCategory = categoryRepository.findByName(stringUtility.removeExtraSpace(updateCategory.getName()));
        if (existingCategory.isPresent() && !Objects.equals(existingCategory.get().getId(), cateId)) {
            throw new CustomResponseException(StatusResponseDTO.CATEGORY_CONFLICT_NAME);
        } else {
            CategoryEntity cateUpdate = categoryRepository.findById(cateId).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.CATEGORY_NOT_FOUND));

            if(multipartFile != null) {
                cateUpdate.setImageUrl(amazonS3Service.uploadImage(multipartFile));
            }
            cateUpdate.setName(stringUtility.removeExtraSpace(updateCategory.getName()));
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

        return new ResponseObject("Delete successfully");
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