package com.springboot.laptop.service.impl;

import com.springboot.laptop.model.Invoice;
import com.springboot.laptop.repository.InvoiceRepository;
import com.springboot.laptop.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    @Override
    public Invoice createInvoice() {

        Invoice invoice = new Invoice();

        return invoiceRepository.save(invoice);
    }
}
