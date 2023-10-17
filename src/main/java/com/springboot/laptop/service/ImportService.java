package com.springboot.laptop.service;

import com.springboot.laptop.model.Import;
import com.springboot.laptop.model.dto.request.ImportRequestDTO;

public interface ImportService {

    Import createImport(ImportRequestDTO importRequestDTO);

    Object getAllImport();

}
