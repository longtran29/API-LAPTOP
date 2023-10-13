package com.springboot.laptop.model.enums;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum OrderStatus {
    NEW("Chờ xác nhận"),
    CONFIRM("Xác nhận đơn hàng"),
    SHIPPED("Được giao cho đơn vị vận chuyển giao hàng"),
    ON_THE_WAY("Đang trên đường đến địa chỉ người nhận"),
    DELIVERED("Đã thanh toán"),
    CANCELED("Đã huỷ");

    private final String name;

    public static OrderStatus valueOfCode(String blahCode) throws IllegalArgumentException {
        OrderStatus blah = Arrays.stream(OrderStatus.values())
                .filter(val -> val.name().equals(blahCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unable to resolve order status: " + blahCode));

        return blah;
    }

    public static OrderStatus getStatus(String statusName) {
        if(statusName == null || statusName.trim().length()== 0) {
            return null;
        } else {
            for(OrderStatus orderStatus : OrderStatus.values()) {
                if (orderStatus.name.equals(statusName)) {
                    return orderStatus;
                }
            }
            return null;
        }
    }

    public static boolean checkExists(String updatedStatus) {
        return Arrays.stream(OrderStatus.values()).anyMatch(status -> status.getName().equals(updatedStatus));
    }
    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
