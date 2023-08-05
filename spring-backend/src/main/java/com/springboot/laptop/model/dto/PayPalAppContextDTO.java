package com.springboot.laptop.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayPalAppContextDTO {
    @JsonProperty("brand_name")
    private String brandName;
    @JsonProperty("landing_page")
    private PaymentLandingPage landingPage;
    @JsonProperty("return_url")
    private String returnUrl;
    @JsonProperty("cancel_url")
    private String cancelUrl;
    @JsonProperty("payment_method_selected")
    private String paymentMethod;

    @JsonProperty("payment_method_preference")
    private String paymentMethodReference;

}
