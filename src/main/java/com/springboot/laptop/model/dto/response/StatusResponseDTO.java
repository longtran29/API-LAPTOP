package com.springboot.laptop.model.dto.response;

public enum StatusResponseDTO {
    EMAIL_NOT_BLANK("400", "Email không được để trống"),
    INTERNAL_SERVER("500", "Hệ thống đang bị gián đoạn! Xin vui lòng thử lại sau"),
    CATEGORY_CONFLICT_BRAND("500", "Danh mục này đang được nhãn hàng kinh doanh"),
    CATEGORY_CONFLICT_PRODUCTS("500", "Sản phẩm thuộc danh mục này đang được kinh doanh"),
    PASSWORD_NOT_MATCH("500", "Hai mật khẩu không trùng nhau"),
    USERNAME_IN_USE("409", "Tên người dùng đã tồn tại !"),

    EMAIL_IN_USE("409", "Gmail đã được sử dụng !"),

    DUPLICATED_DATA ("409", "Dữ liệu đã bị trùng !"),
    ACCOUNT_BEEN_INACTIVATED("404", "Tài khoản của bạn đã bị khoá, vui lòng liên hệ admin"),
    DATA_EMPTY("500", "Vui lòng điền dữ liệu"),
    PRODUCT_OUT_STOCK("500", "Không đủ hàng trong kho. Vui lòng liên hệ quản trị viên để được hỗ trợ"),
    ROLE_NOT_FOUND("404", "Không tồn tại quyền này trên hệ thống"),
    ORDER_CANCEL_VIOLATION("500", "Đơn hàng đã được tiến hành đi giao không thể huỷ"),
    ORDER_CANCELED_VIOLATION("500", "Đơn hàng đã được huỷ rồi"),
    ORDER_DELIVERED_VIOLATION("500", "Đơn hàng đã được giao thành công rồi"),
    CUSTOMER_NOT_FOUND("404", "Không có khách hàng này trên hệ thống"),
    CART_NOT_EXIST("500", "Không có sản phẩm nào trong giỏ hàng"),
    PRODUCT_EXISTING_IN_CART("500", "Sản phẩm này đang nằm trong giỏ hàng  của khách hàng"),

    REVIEW_REJECTION("500", "You have not purchase for this product or order has not been delivered to you !"),
    ORDER_HAS_NOT_BEEN_DELIVERED("500", "Đơn hàng chưa được giao thành công cho bạn "),
    FIELD_IS_MISSING("500", "Please prodvide the full information !"),

    PRODUCT_HAS_BEEN_LOCKED("500", "Sản phẩm đã ngừng kinh doanh"),
    INFORMATION_IS_MISSING("500", "Bạn chưa nhập đủ thông tin !"),
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
