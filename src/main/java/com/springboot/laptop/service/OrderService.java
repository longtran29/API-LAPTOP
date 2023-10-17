package com.springboot.laptop.service;

import com.springboot.laptop.model.Order;
import com.springboot.laptop.model.dto.OrderDTO;
import com.springboot.laptop.model.dto.request.ChangeStatusDTO;
import com.springboot.laptop.model.dto.request.OrderRequestDTO;
import com.springboot.laptop.model.dto.request.PaypalCheckoutDTO;
import com.springboot.laptop.model.dto.response.OrderCompleted;
import com.springboot.laptop.model.dto.response.OrderResponseDTO;
import com.springboot.laptop.model.enums.PaymentMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface OrderService {

    public List<OrderResponseDTO> getOrders();

    public Object cancelOrders(String orderId);
    public OrderResponseDTO changeStatus(ChangeStatusDTO changeStatusDTO);
    public List<OrderResponseDTO> getUserOrders();

    public Order findById(String orderId);

    Order saveOrder(Long addressId, PaymentMethod paymentMethod);

    public Object checkoutCOD(OrderRequestDTO orderRequest) throws IOException, InterruptedException;

    public Object sendMail4Order(OrderCompleted order);

    public Object getOrderDetails(String orderId) ;

    Object checkoutPaypal(PaypalCheckoutDTO paypalCheckout) throws IOException, InterruptedException;

    Object checkoutVnpay(HttpServletRequest req) throws UnsupportedEncodingException;
}
