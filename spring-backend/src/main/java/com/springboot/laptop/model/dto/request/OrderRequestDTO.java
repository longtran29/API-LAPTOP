package com.springboot.laptop.model.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.laptop.model.dto.OrderDTO;
import com.springboot.laptop.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private OrderDTO orderDTO;

    @JsonProperty("address_id")
    private Long addressId;

    @JsonProperty("method_payment")
    private PaymentMethod methodPayment;
}
