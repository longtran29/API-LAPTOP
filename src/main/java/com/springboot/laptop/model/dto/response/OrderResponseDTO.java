package com.springboot.laptop.model.dto.response;

import com.springboot.laptop.model.dto.AddressDTO;
import com.springboot.laptop.model.dto.OrderDetailsDTO;
import com.springboot.laptop.model.dto.UserDTO;
import com.springboot.laptop.model.enums.OrderStatus;
import com.springboot.laptop.model.enums.PaymentMethod;
import lombok.*;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {

    private String id;

    private UserDTO user;

    private List<OrderDetailsDTO> orderDetails;

    private Date orderDate;

    private float total;


//    // trạng thái đơn hàng hiện tại
//    private Object status;
//    private Object statusName;

    private Date createdTimestamp;

    private OrderStatus orderStatus;


    private PaymentMethod methodPayment;

    private AddressDTO address;


}
