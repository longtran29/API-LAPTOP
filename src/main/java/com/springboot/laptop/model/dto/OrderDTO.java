package com.springboot.laptop.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class OrderDTO implements Serializable {

    private OrderIntent intent;

    @JsonProperty("purchase_units")
    private List<PurchaseUnit> purchaseUnit;

    @JsonProperty("payment_source")
    private PaymentSource paymentSource;

    @JsonProperty("application_context")
    private PayPalAppContextDTO applicationContext;

}
