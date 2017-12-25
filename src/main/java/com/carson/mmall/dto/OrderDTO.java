package com.carson.mmall.dto;

import com.carson.mmall.VO.ShippingVO;
import com.carson.mmall.dataobject.OrderItem;
import com.carson.mmall.dataobject.Shipping;
import com.carson.mmall.enums.OrderStatusEnum;
import com.carson.mmall.enums.PaymentTypeEnum;
import com.carson.mmall.utils.EnumUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private Integer id;

    private Long orderNo;

    private Integer userId;

    private Integer shippingId;

    private BigDecimal payment;

    private Integer paymentType;

    private String paymentTypeDesc;

    private Integer postage;

    private Integer status;

    private String statusDesc;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    private Date createTime;

    @JsonProperty("orderItemVoList")
    private List<OrderItem> orderItemList;

    private String imageHost;

    private String receiverName;

    @JsonProperty("shippingVo")
    private Shipping shipping;

    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum() {
        return EnumUtil.getByCode(OrderStatusEnum.class, status);
    }

    @JsonIgnore
    public PaymentTypeEnum getPaymentTypeEnum() {
        return EnumUtil.getByCode(PaymentTypeEnum.class, paymentType);
    }
}
