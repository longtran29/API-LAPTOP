package com.springboot.laptop.model.dto.response;

public enum StatusResponseDTO {
    EMAIL_NOT_BLANK("400", "Email không được để trống"),
    INTERNAL_SERVER("500", "Hệ thống đang bị gián đoạn! Xin vui lòng thử lại sau"),
    CATEGORY_CONFLICT_BRAND("500", "Danh mục này đang được nhãn hàng kinh doanh"),

    ORDER_PROCESS_HANDLED_ERROR("500", "Đơn hàng này đã được giao xử lý cho tài khoản kha "),

    BRAND_NAME_MUST_PROVIDED("500", "Bạn phải cung cấp tên thương hiệu"),

    IMAGE_MUST_PROVIDED("500", "Bạn phải cung cấp ảnh đại diện"),
    CATEGORY_NAME_MUST_PROVIDED("500", "Bạn phải cung cấp tên danh mục"),
    CATEGORY_CONFLICT_NAME("500", "Tên danh mục đã tồn tại, vui lòng chọn tên khác"),

    BRAND_CONFLICT_NAME("500", "Tên thương hiệu đã tồn tại, vui lòng chọn tên khác"),
    CATEGORY_CONFLICT_PRODUCTS("500", "Sản phẩm thuộc danh mục này đang được kinh doanh"),
    PASSWORD_NOT_MATCH("500", "Hai mật khẩu không trùng nhau"),

    PASSWORD_NOT_PROVIDED("500", "Vui lòng nhập mật khẩu"),
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

    PRODUCT_NOT_EXISTS("500", "This product do not exist in the system"),
    REVIEW_REJECTION("500", "You have not purchase for this product or order has not been delivered to you !"),
    ORDER_HAS_NOT_BEEN_DELIVERED("500", "Đơn hàng chưa được giao thành công cho bạn "),
    FIELD_IS_MISSING("500", "Please prodvide the full information !"),

    USER_ALREADY_EXISTS("500", "Userna"),
    PASSWORD_NOT_MEET_REQUIREMENT("500", "Mật khẩu tối thiểu 6 ký tự !"),



    PRODUCT_HAS_BEEN_LOCKED("500", "Sản phẩm đã ngừng kinh doanh"),
    INFORMATION_IS_MISSING("500", "Bạn chưa nhập đủ thông tin !"),
    BRAND_CONSTRAINT_EXCEPTION("500", "Thương hiệu đang kinh doanh sản phẩm  "),
    CATEGORY_VIOLATION_EXCEPTION("500", "Danh mục có sản phẩm đang được kinh doanh"),
    FAIL_AUTHENTICATION("404", "Tên đăng nhập hoặc mật khẩu sai !"),
    ADDRESS_NOT_FOUND("404", "Không tìm thấy địa chỉ này !"),

    RESET_PASSWORD_FAILED("400", "Đổi mật khẩu không thành công"),


    CATEGORY_NOT_FOUND("404", "Không tìm thấy danh mục này"),
    USER_NOT_FOUND("404", "Không tồn tại tài khoản trên hệ thống"),
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
