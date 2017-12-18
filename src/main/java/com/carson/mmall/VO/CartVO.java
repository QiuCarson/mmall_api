package com.carson.mmall.VO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartVO {
    private Boolean allChecked;

    private BigDecimal cartTotalPrice;

    private List<CartProductVO> cartProductVoList;
}
