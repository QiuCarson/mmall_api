package com.carson.mmall.VO;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderItemVO {
    private Integer orderNo;
    private Integer productId;
    private String productName;
    private String productImage;
    private BigDecimal currentUnitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Date createTime;
}

