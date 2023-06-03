package com.springboot.laptop.model.enums;

public enum PaymentMethod {

    CASH("Tiền mặt"),
    CREDIT_CARD("Thẻ tín dụng");

    private final String name;

    PaymentMethod(String methodPayment) {
        this.name = methodPayment;
    }

    public static PaymentMethod getPaymentMethod(String methodName) {
        if(methodName == null || methodName.trim().length()== 0) {
            return null;
        } else {
            for(PaymentMethod method : PaymentMethod.values()) {
                if (method.name.equals(methodName)) {
                    return method;
                }
            }
            return null;
        }
    }

    public String getMethodName() {
        return this.name;
    }

}
