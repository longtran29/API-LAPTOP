package com.springboot.laptop.model.dto.request;

import com.springboot.laptop.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChangeStatusDTO {

    private Long orderId;
    private String status;
}
