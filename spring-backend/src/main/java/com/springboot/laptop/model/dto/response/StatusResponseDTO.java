package com.springboot.laptop.model.dto.response;

public enum StatusResponseDTO {
    EMAIL_NOT_BLANK("400", "Email không được để trống"),
    ERROR_NOT_FOUND("404", "Không tìm thấy dữ liệu"),
    INTERNAL_SERVER("500", "Hệ thống đang bị gián đoạn! Xin vui lòng thử lại sau");


    private final String code;
    private final String message;


    StatusResponseDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return this.code; }
    public String getMessage() { return this.message;}
}
