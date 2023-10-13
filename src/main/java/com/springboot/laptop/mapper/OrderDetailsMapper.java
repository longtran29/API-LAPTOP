package com.springboot.laptop.mapper;


import com.springboot.laptop.model.OrderDetails;
import com.springboot.laptop.model.dto.OrderDetailsDTO;
import org.mapstruct.Mapper;

@Mapper
public interface OrderDetailsMapper {


    OrderDetailsDTO orderDetailToDTO(OrderDetails orderDetail);

    OrderDetails DTOToOrderDetails(OrderDetailsDTO orderDetail);
}
