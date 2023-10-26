package com.springboot.laptop.service;

import com.springboot.laptop.model.dto.OrderDTO;
import com.springboot.laptop.model.dto.response.OrderedResponseDTO;
import com.springboot.laptop.model.enums.PaymentMethod;

import java.io.IOException;


public interface PayPalService {


    OrderedResponseDTO createOrder(OrderDTO orderDTO) throws IOException, InterruptedException;

    Object confirmOrder(String orderId, Long addressId) throws IOException, InterruptedException;
}
