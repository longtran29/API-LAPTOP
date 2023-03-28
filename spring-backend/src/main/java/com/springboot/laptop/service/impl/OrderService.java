package com.springboot.laptop.service.impl;

import com.springboot.laptop.model.Order;
import com.springboot.laptop.model.dto.request.ChangeStatusDTO;
import com.springboot.laptop.model.dto.request.OrderRequestDTO;
import com.springboot.laptop.model.dto.response.OrderCompleted;
import com.springboot.laptop.model.dto.response.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    public List<OrderResponseDTO> getOrders();

    public Order cancelOrders(Long orderId);
    public Order changeStatus(ChangeStatusDTO changeStatusDTO);
    public List<OrderResponseDTO> getUserOrders();

    public Order findById(Long orderId);

    public Order checkout(OrderRequestDTO orderRequest);

    public Object sendMail4Order(OrderCompleted order);

}
