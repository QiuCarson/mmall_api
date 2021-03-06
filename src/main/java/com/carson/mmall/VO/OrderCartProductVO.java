package com.carson.mmall.VO;

import com.carson.mmall.dataobject.OrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCartProductVO {
    private String imageHost;
    private BigDecimal productTotalPrice;
    @JsonProperty("orderItemVoList")
    private List<OrderItem> orderItemList;
}
