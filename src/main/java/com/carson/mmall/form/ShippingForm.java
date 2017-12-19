package com.carson.mmall.form;

import lombok.Data;

@Data
public class ShippingForm {
    private Integer id;

    private String receiverName;

    private String receiverProvince;

    private String receiverCity;

    private String receiverPhone;

    private String receiverAddress;

    private String receiverZip;
}
