package com.springboot.laptop.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
public class PaypalConfig {

    @Value("${paypal.baseUrl}")
    private String baseUrl;

    @Value("${paypal.clientId}")
    private String clientId;

    @Value("${paypal.secret}")
    private String secret;

}
