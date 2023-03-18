package com.springboot.laptop.model.enums;

public enum OrderStatus {
    NEW("Đặt hàng"),
    PROCESSING("Đang xử lý"),
    SHIPPED("Đang ship"),
    DELIVERED("Đã hoàn thành"),
    CANCELED("Đã huỷ");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
