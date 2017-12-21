package com.carson.mmall.enums;

import lombok.Getter;

@Getter
public enum ProductStatusEnum {
    IN(1,"在售"),
    DOWN(2,"下架"),
    DEL(3,"删除")
    ;
    private Integer code;
    private String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
