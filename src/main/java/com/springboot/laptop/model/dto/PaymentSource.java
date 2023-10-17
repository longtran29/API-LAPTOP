package com.springboot.laptop.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentSource implements Serializable {

    private Paypal paypal;
}
