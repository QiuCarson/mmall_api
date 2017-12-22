package com.carson.mmall.utils;

import com.carson.mmall.enums.CodeEnum;

public class EnumUtil {
    public static <T extends CodeEnum> T getByCode(Class<T> enumClass, Integer code) {
        for (T each : enumClass.getEnumConstants()) {
            if(each.getCode()==code){
                return each;
            }
        }
        return null;
    }
}
