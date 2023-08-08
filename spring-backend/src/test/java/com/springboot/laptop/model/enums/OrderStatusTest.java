package com.springboot.laptop.model.enums;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @Test
    public void checkOrderStatus() {
        OrderStatus status = OrderStatus.valueOfCode("DELIVEREDD");

        Assertions.assertThat(status).isNotNull();

    }

}