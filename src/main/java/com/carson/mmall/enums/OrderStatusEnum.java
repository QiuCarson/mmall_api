package com.carson.mmall.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements CodeEnum{
    CACLE(0,"已取消"),
    NO_PAY(10,"未付款"),
    YES_PAY(20,"已付款"),
    SEND(40,"已发货"),
    SUCCESS(50,"交易成功"),
    CLOSE(60,"交易关闭")
    ;
    private Integer code;
    private String message;

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
