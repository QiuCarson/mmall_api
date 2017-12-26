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
    USERNAME_EMAIL_EXISTS(111,"邮件地址已经存在"),
    USERNAME_ROLE_ERROR(112,"权限不够"),


    PRODUCT_NOT_EXISTS(200, "商品不存在"),
    PRODUCT_NOT_IN(201, "商品不在售"),
    PRODUCT_NOT_STOCK(202, "商品库存不足"),
    PRODUCT_IMAGES_NOT_NULL(203,"文件不能为空"),
    PRODUCT_IMAGES_UPLOAD_ERROR(205,"上传失败"),
    PRODUCT_IMAGES_UPLOAD_SUCCESS(206,"上传成功"),

    SHIPPING_NOT_EXISTS(300, "地址不存在"),
    CART_NOT_EXISTS(400, "没有要下单的商品"),

    ORDER_NOT_EXISTS(500, "订单不存在"),
    ORDER_NOT_ITEM_EXISTS(501,"订单商品不存在"),
    ORDER_ALIPAY_CREATE_ERROR(502,"支付宝预下单失败"),
    ORDER_ALIPAY_UNKNOWN_ERROR(503,"系统异常，预下单状态未知"),
    ORDER_ALIPAY_STATUS_ERROR(504,"不支持的交易状态，交易返回异常"),

    CATEGORY_NOT_EXISTS(600, "栏目不存在")
    ;


    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
