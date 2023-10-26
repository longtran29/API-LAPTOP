package com.springboot.laptop.service;

import com.springboot.laptop.model.Invoice;
import com.springboot.laptop.model.dto.request.ImportDetailDTO;

import java.util.List;

public interface InvoiceService {

    Invoice createInvoice(List<ImportDetailDTO> details);
}
