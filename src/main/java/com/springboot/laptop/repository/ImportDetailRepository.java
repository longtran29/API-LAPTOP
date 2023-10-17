package com.springboot.laptop.repository;

import com.springboot.laptop.model.ImportDetail;
import com.springboot.laptop.model.dto.response.ImportDetailExportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportDetailRepository extends JpaRepository<ImportDetail, Long> {

    @Query(value = "select products.name, price, quantity, import_details.id, import, product_id from import_details  inner join products on import_details.product_id = products.id where import = 14", nativeQuery = true)
    List<ImportDetail> getAllImports();

}
