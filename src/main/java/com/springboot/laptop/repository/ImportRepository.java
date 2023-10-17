package com.springboot.laptop.repository;

import com.springboot.laptop.model.Import;
import com.springboot.laptop.model.ImportDetail;
import com.springboot.laptop.model.dto.response.ImportDetailExportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportRepository extends JpaRepository<Import, Long> {


}
