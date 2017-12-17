package com.carson.mmall.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResultVO<T> {
    @JsonProperty("status")
    private Integer code;

    @JsonProperty("msg")
    private String message;

    private T data;
}
