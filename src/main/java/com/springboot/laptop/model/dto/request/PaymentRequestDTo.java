package com.springboot.laptop.model.dto.request;

import com.springboot.laptop.model.dto.response.CartResponseDTO;

import java.util.List;

public class PaymentRequestDTo {

    private float totalPrice;
    private String currency;

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
