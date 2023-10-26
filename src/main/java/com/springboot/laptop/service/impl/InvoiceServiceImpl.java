package com.springboot.laptop.service.impl;

import com.springboot.laptop.model.Invoice;
import com.springboot.laptop.model.dto.request.ImportDetailDTO;
import com.springboot.laptop.repository.InvoiceRepository;
import com.springboot.laptop.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    @Override
    public Invoice createInvoice(List<ImportDetailDTO> details) {

        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(new Date());

        float totalFee = 0;
        for (ImportDetailDTO detail: details
             ) {
            totalFee += (float) (detail.getPrice() * detail.getQuantity());
        }
        invoice.setTotal_amount(totalFee);

        return invoiceRepository.save(invoice);
    }
}
