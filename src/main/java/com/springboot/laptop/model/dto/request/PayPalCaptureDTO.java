package com.springboot.laptop.model.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.laptop.model.enums.PaymentMethod;
import lombok.Data;

@Data
public class PayPalCaptureDTO {

    @JsonProperty("order_id")
    private String orderID;

    @JsonProperty("address_id")
    private Long addressID;

//    @JsonProperty("payment_method")
//    private PaymentMethod paymentMethod;
}
