package com.springboot.laptop.model.enums;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum OrderStatus {
    NEW("Chờ xác nhận"),
    SHIPPED("Đang giao hàng"),

    REJECTED ("Từ chối"),
    DELIVERED("Đã thanh toán"),
    CANCELED("Đã huỷ");



    private final String name;

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
