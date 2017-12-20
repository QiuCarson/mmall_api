package com.carson.mmall.dataobject;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Slf4j
@Data
@Table(name = "mmall_order")
@DynamicUpdate
public class Order {
    @Id
    @GeneratedValue
    private Integer id;
    private Long orderNo;
    private Integer userId;
    private Integer shippingId;
    private BigDecimal payment;
    private Integer paymentType;
    private Integer postage;
    private Integer status;
    private Date paymentTime;
    private Date sendTime;
    private Date endTime;
    private Date closeTime;
    private Date createTime;
    private Date updateTime;
}


