package com.carson.mmall.VO;

import com.carson.mmall.dto.CartDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartVO {
    private Boolean allChecked;

    private BigDecimal cartTotalPrice;

    @JsonProperty("cartProductVoList")
    private List<CartDTO> cartProductVOList;
}
