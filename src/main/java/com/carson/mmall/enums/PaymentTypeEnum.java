package com.carson.mmall.enums;

import lombok.Getter;

@Getter
public enum PaymentTypeEnum implements CodeEnum{
    PAY_ONLINE(1, "在线支付");
    private Integer code;
    private String message;

    PaymentTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
