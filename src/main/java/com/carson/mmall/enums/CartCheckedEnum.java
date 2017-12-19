package com.carson.mmall.enums;

import lombok.Getter;

@Getter
public enum CartCheckedEnum {
    YES(1,"已勾选"),
    NO(0,"未勾选")
    ;
    private Integer code;
    private String message;

    CartCheckedEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
