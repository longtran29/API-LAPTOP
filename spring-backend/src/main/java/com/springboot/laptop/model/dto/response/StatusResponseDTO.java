package com.springboot.laptop.model.dto.response;

public enum StatusResponseDTO {
    EMAIL_NOT_BLANK("400", "Email không được để trống"),
    ERROR_NOT_FOUND("404", "Không tìm thấy dữ liệu"),
    INTERNAL_SERVER("500", "Hệ thống đang bị gián đoạn! Xin vui lòng thử lại sau"),

    DUPLICATED_DATA ("500", "Dữ liệu đã bị trùng !"),
    ACCOUNT_BEEN_INACTIVATED("404", "Tài khoản của bạn đã bị khoá, vui lòng liên hệ admin"),
    DATA_EMPTY("500", "Vui lòng điền dữ liệu"),

    ORDER_CANCEL_VIOLATION("500", "Đơn hàng đang được giao không thể huỷ"),
    ORDER_REJECTED_VIOLATION("500", "Đơn hàng bị từ cối bởi bán"),

    CUSTOMER_VIOLATION_EXCEPTION("500", "Khách hàng đã có đơn hàng "),


    BRAND_CONSTRAINT_EXCEPTION("500", "Hãng sản xuất có danh mục đang được kính doanh"),
    CATEGORY_VIOLATION_EXCEPTION("500", "Danh mục có sản phẩm đang được kinh doanh"),
    FAIL_AUTHENTICATION("404", "Tên đăng nhập hoặc mật khẩu sai !"),

    RESET_PASSWORD_FAILED("400", "Đổi mật khẩu không thành công"),

    CATEGORY_NOT_FOUND("404", "Không tìm thấy danh mục này"),
    USER_NOT_FOUND("404", "Không tìm thấy khách hàng này"),
    BRAND_NOT_FOUND("404", "Không tìm thấy hãng sản xuất"),
    PRODUCT_NOT_FOUND("404", "Không tìm thấy sản phẩm"),
    ORDER_NOT_FOUND("404", "Không tìm thấy đơn hàng này"),
    CART_NOT_FOUND("404", "Không tìm thấy giỏ hàng "),
    PRODUCT_VIOLATION_EXCEPTION("500", "Sản phẩm trong đơn hàng ")
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
