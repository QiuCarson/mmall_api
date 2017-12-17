package com.carson.mmall.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN(0,"管理员"),
    USER(1,"普通用户")
    ;
    private Integer code;
    private String message;

    RoleEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
