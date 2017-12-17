package com.carson.mmall.exception;


import com.carson.mmall.enums.ResultEnum;
import lombok.Data;

/**
 * Created by qiucarson on 2017/11/25.
 */

@Data
public class MmallException extends RuntimeException {
    private Integer code;

    public MmallException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code=resultEnum.getCode();
    }

    public MmallException(Integer code,String message){
        super(message);
        this.code=code;
    }


}
