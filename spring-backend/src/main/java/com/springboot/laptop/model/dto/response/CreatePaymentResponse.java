package com.springboot.laptop.model.dto.response;

public class CreatePaymentResponse {

    private String publishableKey;
    private String clientSecret;

    public String getPublishableKey() {
        return publishableKey;
    }
    public CreatePaymentResponse(String publishableKey, String clientSecret) {
        this.publishableKey = publishableKey;
        this.clientSecret = clientSecret;
    }

    public void setPublishableKey(String publishableKey) {
        this.publishableKey = publishableKey;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
