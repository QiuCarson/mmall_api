package com.carson.mmall.converter;

import com.carson.mmall.dataobject.Shipping;
import com.carson.mmall.form.ShippingForm;
import org.springframework.beans.BeanUtils;

public class ShippingForm2ShippingConvert {
    public static Shipping convert(ShippingForm shippingForm){
        Shipping shipping=new Shipping();
        BeanUtils.copyProperties(shippingForm,shipping);
        return shipping;
    }
}
