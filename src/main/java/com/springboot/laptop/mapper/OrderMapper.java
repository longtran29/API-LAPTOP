package com.springboot.laptop.mapper;

import com.springboot.laptop.model.Order;
import com.springboot.laptop.model.OrderDetails;
import com.springboot.laptop.model.dto.OrderDetailsDTO;
import com.springboot.laptop.model.dto.response.OrderResponseDTO;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {

    OrderResponseDTO orderToDTO(Order order);

    Order orderDTOToOrder(OrderResponseDTO orderDTO);
}
