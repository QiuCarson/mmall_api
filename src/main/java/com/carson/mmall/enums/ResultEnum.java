package com.carson.mmall.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    SUCCESS(0, "success"),
    UNKNOWN_ERROR(1, "未知错误"),
    PARAM_ERROR(2, "参数错误"),
    USERNAME_NOT_AUTH(10, "用户未登录,无法获取当前用户信息"),


    USERNAME_NOT_EXISTS(100, "用户不存在"),
    PASSWORD_ERROR(101, "密码错误"),
    USERNAME_EXISTS(102, "用户已经存在"),
    PASSWORD_NOT_EQUALS_ERROR(103, "两个密码不一样"),
    USERNAME_NOT_QUESTION(104, "该用户未设置找回密码问题"),
    QUESTION_ERROR(105, "问题错误"),
    ANSWER_ERROR(106, "问题答案错误"),
    TOKEN_ERROR(107, "token已经失效"),
    OLD_PASSWORD_ERROR(108, "旧密码输入错误"),
    LOGOUT_ERROR(109, "登出失败"),
    LONGIN_NOT_AUTH(110, "用户没有登录"),


    PRODUCT_NOT_EXISTS(200, "商品不存在"),
    PRODUCT_NOT_IN(201, "商品不在售"),
    PRODUCT_NOT_STOCK(202, "商品库存不足"),

    SHIPPING_NOT_EXISTS(300, "地址不存在"),
    CART_NOT_EXISTS(400, "没有要下单的商品"),

    ORDER_NOT_EXISTS(500, "订单不存在")
    ;


    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
