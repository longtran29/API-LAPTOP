package com.springboot.laptop.model.dto.response;

public enum StatusResponseDTO {
    EMAIL_NOT_BLANK("400", "Email không được để trống"),
    ERROR_NOT_FOUND("404", "Không tìm thấy dữ liệu"),
    INTERNAL_SERVER("500", "Hệ thống đang bị gián đoạn! Xin vui lòng thử lại sau"),

    PASSWORD_NOT_MATCH("500", "Hai mật khẩu không trùng nhau"),
    USERNAME_IN_USE("500", "Tên người dùng đã tồn tại !"),

    EMAIL_IN_USE("500", "Gmail đã được sử dụng !"),

    DUPLICATED_DATA ("500", "Dữ liệu đã bị trùng !"),
    ACCOUNT_BEEN_INACTIVATED("404", "Tài khoản của bạn đã bị khoá, vui lòng liên hệ admin"),
    DATA_EMPTY("500", "Vui lòng điền dữ liệu"),

    PRODUCT_OUT_STOCK("500", "Không đủ hàng trong kho. Vui lòng liên hệ quản trị viên để được hỗ trợ"),
    ROLE_NOT_FOUND("404", "Không tồn tại quyền này trên hệ thống"),
    ORDER_CANCEL_VIOLATION("500", "Đơn hàng đang được giao không thể huỷ"),
    ORDER_REJECTED_VIOLATION("500", "Đơn hàng bị từ chối bởi quản trị viên"),
    ORDER_CANCELED_VIOLATION("500", "Đơn hàng này đã được huỷ"),
    ORDER_DELIVERED_VIOLATION("500", "Đơn hàng đã được thanh toán rồi"),
    ORDER_SHIPPED_VIOLATION("500", "Đơn hàng này đang được shipper giao "),

    CUSTOMER_VIOLATION_EXCEPTION("500", "Khách hàng đã có đơn hàng "),
    CART_NOT_EXIST("500", "Không có sản phẩm nào trong giỏ hàng"),

    VALUE_NOT_VALID("500", "Giá tri bạn nhập không hợp lệ"),

    INFORMATION_IS_MISSING("500", "Vui lòng nhập đầy đủ dữ liệu"),
    BRAND_CONSTRAINT_EXCEPTION("500", "Thương hiệu đang kinh doanh sản phẩm  "),
    CATEGORY_VIOLATION_EXCEPTION("500", "Danh mục có sản phẩm đang được kinh doanh"),
    FAIL_AUTHENTICATION("404", "Tên đăng nhập hoặc mật khẩu sai !"),
    ADDRESS_NOT_FOUND("404", "Không tìm thấy địa chỉ này !"),

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
