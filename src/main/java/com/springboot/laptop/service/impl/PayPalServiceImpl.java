package com.springboot.laptop.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.laptop.config.PaypalConfig;
import com.springboot.laptop.model.dto.AccessTokenResponseDTO;
import com.springboot.laptop.model.dto.OrderDTO;
import com.springboot.laptop.model.dto.PayPalAppContextDTO;
import com.springboot.laptop.model.dto.PaymentLandingPage;
import com.springboot.laptop.model.dto.response.OrderedResponseDTO;
import com.springboot.laptop.model.dto.response.PaymentResponseDTO;
import com.springboot.laptop.model.enums.PaymentMethod;
import com.springboot.laptop.repository.OrderRepository;
import com.springboot.laptop.service.OrderService;
import com.springboot.laptop.service.PayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.springboot.laptop.model.dto.PayPalEndpoints.*;


@RequiredArgsConstructor
@Component
public class PayPalServiceImpl implements PayPalService {

    private final PaypalConfig paypalConfig;

    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final HttpClient httpClient =  HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();;

    private final OrderService orderService;

    public AccessTokenResponseDTO getAccessToken() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(createUrl(paypalConfig.getBaseUrl(), GET_ACCESS_TOKEN )))
                        .header(HttpHeaders.AUTHORIZATION, encodeBasicAuthorization())
                        .headers(HttpHeaders.ACCEPT_LANGUAGE, "en_US")
                        .headers(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String content = response.body();
        return objectMapper.readValue(content, AccessTokenResponseDTO.class);
    }

    private String encodeBasicAuthorization() {
        String input = paypalConfig.getClientId() + ":" + paypalConfig.getSecret();
        return "Basic " + Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }




    @Override
    public OrderedResponseDTO createOrder(OrderDTO orderDTO) throws IOException, InterruptedException {
        var appContext = new PayPalAppContextDTO();
        appContext.setReturnUrl("http://localhost:8080/api/v1/orders/checkout/success");
        appContext.setBrandName("My brand");
        orderDTO.setApplicationContext(appContext);
        var token = getAccessToken();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(createUrl(paypalConfig.getBaseUrl(), ORDER_CHECKOUT)))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken() )
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(orderDTO)))
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String content = response.body();
        return objectMapper.readValue(content, OrderedResponseDTO.class);
    }

    @Override
    public Object confirmOrder(String orderId, Long addressId, PaymentMethod paymentMethod) throws IOException, InterruptedException {
        var accessTokenDto = getAccessToken();
        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://api-m.sandbox.paypal.com/v2/checkout/orders/"+orderId+"/capture"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessTokenDto.getAccessToken())
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        var response  = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String content = response.body();
        orderService.saveOrder(addressId, paymentMethod);
        return objectMapper.readValue(content, PaymentResponseDTO.class);
    }
}
