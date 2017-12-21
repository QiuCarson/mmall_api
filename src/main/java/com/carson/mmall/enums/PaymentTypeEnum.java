package com.carson.mmall.enums;

import lombok.Getter;

@Getter
public enum PaymentTypeEnum {
    PAY_ONLINE(1, "在线支付");
    private Integer code;
    private String message;

    PaymentTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static PaymentTypeEnum getPaymentType(int value) {
        for (PaymentTypeEnum paymentTypeEnum : PaymentTypeEnum.values()) {
            if (value == paymentTypeEnum.getCode()) {
                return paymentTypeEnum;
            }
        }
        return null;
    }
}
