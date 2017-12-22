package com.carson.mmall.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CartDTO {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;
    @JsonProperty("productChecked")
    private Integer checked;

    @JsonProperty("productName")
    private String name;
    @JsonProperty("productSubtitle")
    private String subtitle;
    @JsonProperty("productMainImage")
    private String mainImage;
    @JsonProperty("productPrice")
    private BigDecimal price;
    @JsonProperty("productStatus")
    private Integer status;

    private BigDecimal productTotalPrice;
    @JsonProperty("productStock")
    private Integer stock;

    private String limitQuantity;

    private Date createTime;
}
