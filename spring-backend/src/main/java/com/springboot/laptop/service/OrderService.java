package com.springboot.laptop.service;

import com.springboot.laptop.model.Order;
import com.springboot.laptop.model.dto.OrderDTO;
import com.springboot.laptop.model.dto.request.ChangeStatusDTO;
import com.springboot.laptop.model.dto.request.OrderRequestDTO;
import com.springboot.laptop.model.dto.response.OrderCompleted;
import com.springboot.laptop.model.dto.response.OrderResponseDTO;
import com.springboot.laptop.model.enums.PaymentMethod;

import java.io.IOException;
import java.util.List;

public interface OrderService {

    public List<OrderResponseDTO> getOrders();

    public Object cancelOrders(String orderId);
    public OrderResponseDTO changeStatus(ChangeStatusDTO changeStatusDTO);
    public List<OrderResponseDTO> getUserOrders();

    public Order findById(String orderId);

    Order saveOrder(Long addressId, PaymentMethod paymentMethod);

    public Object checkout(OrderRequestDTO orderRequest) throws IOException, InterruptedException;

    public Object sendMail4Order(OrderCompleted order);

    public Object getOrderDetails(String orderId) ;

}
