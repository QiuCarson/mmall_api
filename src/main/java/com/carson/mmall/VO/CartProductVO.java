package com.carson.mmall.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartProductVO {
    private Integer id;

    private Integer userId;

    private Integer quantity;

    private String productName;

    private String productSubtitle;

    private String productMainImage;

    private BigDecimal productPrice;

    private Integer productStatus;

    private BigDecimal productTotalPrice;

    private Integer productStock;

    private Integer productChecked;

    private String limitQuantity;
}
