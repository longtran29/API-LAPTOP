package com.springboot.laptop.model.dto;

public enum PayPalEndpoints {
    GET_ACCESS_TOKEN("/v1/oauth2/token"),
    ORDER_CHECKOUT("/v2/checkout/orders");

    private final String path;


    PayPalEndpoints(String path) {
        this.path = path;
    }

    public static String createUrl(String baseUrl, PayPalEndpoints payPalEndpoint) {
        return baseUrl + payPalEndpoint.path;
    }


}
