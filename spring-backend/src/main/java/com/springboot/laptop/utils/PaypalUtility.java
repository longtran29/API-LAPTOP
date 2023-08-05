package com.springboot.laptop.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.laptop.config.PaypalConfig;
import com.springboot.laptop.model.dto.AccessTokenResponseDTO;
import com.springboot.laptop.repository.OrderRepository;
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

import static com.springboot.laptop.model.dto.PayPalEndpoints.GET_ACCESS_TOKEN;
import static com.springboot.laptop.model.dto.PayPalEndpoints.createUrl;


@Component
@RequiredArgsConstructor
public class PaypalUtility {

    private final PaypalConfig paypalConfig;

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient =  HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();;


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

    public String encodeBasicAuthorization() {
        String input = paypalConfig.getClientId() + ":" + paypalConfig.getSecret();
        return "Basic " + Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

}
