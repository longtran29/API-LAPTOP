package com.springboot.laptop.service;

import com.springboot.laptop.model.Import;
import com.springboot.laptop.model.dto.request.ImportRequestDTO;
import net.sf.jasperreports.engine.JRException;

public interface ImportService {

    Import createImport(ImportRequestDTO importRequestDTO);

    Object getAllImport();

}
