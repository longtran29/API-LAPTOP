package com.springboot.laptop.model.dto.response;

import com.springboot.laptop.model.Address;
import com.springboot.laptop.model.OrderDetails;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.enums.OrderStatus;
import lombok.*;

import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long id;

    private UserResponseDTO user;

    private List<OrderDetailResponseDTO> orderDetails;

    private LocalDateTime orderDate;

    private float total;


    // trạng thái đơn hàng hiện tại
    private Object status;
    private Object statusName;

    private AddressResponseDTO address;


}
