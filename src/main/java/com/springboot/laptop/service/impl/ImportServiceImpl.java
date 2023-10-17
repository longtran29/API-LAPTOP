package com.springboot.laptop.service.impl;

import com.springboot.laptop.exception.CustomResponseException;
import com.springboot.laptop.model.*;
import com.springboot.laptop.model.dto.request.ImportDetailDTO;
import com.springboot.laptop.model.dto.request.ImportRequestDTO;
import com.springboot.laptop.model.dto.response.StatusResponseDTO;
import com.springboot.laptop.repository.*;
import com.springboot.laptop.service.ImportService;
import com.springboot.laptop.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private final InvoiceRepository invoiceRepository;

    private final InvoiceService invoiceService;

    private final ImportRepository importRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final ImportDetailRepository importDetailRepository;


    @Override
    public Import createImport(ImportRequestDTO importRequestDTO) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new CustomResponseException(StatusResponseDTO.USER_NOT_FOUND));
        Invoice newInvoice = invoiceService.createInvoice();

        Import newImport = new Import();


        newImport.setImport_Date(new Date());
        newImport.setNote(importRequestDTO.getNote());
        newImport.setInvoice(invoiceRepository.findById(newInvoice.getId()).orElseThrow(() -> new RuntimeException("No found invoice")));

        newImport.setAccount(user);

        newImport = importRepository.save(newImport);

        List<ImportDetail> importDetails = new ArrayList<>();

        for (ImportDetailDTO detail: importRequestDTO.getImportDetails()
             ) {
            ImportDetail importDetail = new ImportDetail();
            importDetail.setImport_id(newImport);
            importDetail.setProduct(productRepository.findById(detail.getProduct()).orElseThrow(() -> new RuntimeException("No founded")));
            importDetail.setQuantity(detail.getQuantity());
            importDetail.setPrice(detail.getPrice());

            importDetails.add(importDetailRepository.save(importDetail));
        }

        newImport.setImportDetails(importDetails);

        return importRepository.save(newImport);
    }

    @Override
    public Object getAllImport() {
        return importRepository.findAll();
    }
//
//    private JasperReport loadTemplate() throws JRException {
////        File file = File.createTempFile("my_import", ".pdf");
//
//        InputStream inputStream = getClass().getResourceAsStream(template_path);
//        final JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
//
//        return JasperCompileManager.compileReport(jasperDesign);
//
//
//    }
//    @Override
//    public void exportReport() throws JRException {
//
////        String basePath = "D:\\edge_download";
////
////        List<ImportDetail> imports = importDetailRepository.getAllImports();
////
////        JasperReport jasperReport = loadTemplate();
////
////        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(imports);
////
////        Map<String, Object> parameters = new HashMap<>();
////
////        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
////
////        JasperExportManager.exportReportToPdfFile(jasperPrint, basePath + "\\employees.pdf");
//
//
//    }
}
