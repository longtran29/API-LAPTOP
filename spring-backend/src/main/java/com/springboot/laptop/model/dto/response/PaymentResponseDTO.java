package com.springboot.laptop.model.dto.response;

import com.springboot.laptop.model.dto.LinkDTO;
import com.springboot.laptop.model.dto.PaymentSource;
import com.springboot.laptop.model.dto.PurchaseUnit;
import lombok.Data;

import java.util.List;

@Data
public class PaymentResponseDTO {

    private String id;
    private String status;
    private PaymentSource payment_source;
    private List<PurchaseUnit> purchase_units;
    private List<LinkDTO> links;
}
