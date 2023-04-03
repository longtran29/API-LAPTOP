package com.springboot.laptop.model.dto.response;

public enum StatusResponseDTO {
    EMAIL_NOT_BLANK("400", "Email không được để trống"),
    ERROR_NOT_FOUND("404", "Không tìm thấy dữ liệu"),
    INTERNAL_SERVER("500", "Hệ thống đang bị gián đoạn! Xin vui lòng thử lại sau"),

    DUPLICATED_DATA ("500", "Dữ liệu đã bị trùng !"),
    DATA_EMPTY("500", "Dữ liệu trống"),
    ACCOUNT_BEEN_INACTIVATED("404", "Tài khoản của bạn đã bị cấm, vui lòng liên hệ admin"),

    BRAND_CONSTRAINT_EXCEPTION("500", "Không thể xoá bởi vì chứa danh mục"),
    FAIL_AUTHENTICATION("404", "Tên đăng nhập hoặc mật khẩu sai !"),

    RESET_PASSWORD_FAILED("400", "Đổi mật khẩu không thành công"),

    CATEGORY_NOT_FOUND("404", "Không tìm thấy danh mục này"),
    USER_NOT_FOUND("404", "Không tìm thấy khách hàng này"),
    BRAND_NOT_FOUND("404", "Không tìm thấy hãng sản xuất"),
    PRODUCT_NOT_FOUND("404", "Không tìm thấy sản phẩm")
        ;


    private final String code;
    private final String message;


    StatusResponseDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return this.code; }
    public String getMessage() { return this.message;}
}
