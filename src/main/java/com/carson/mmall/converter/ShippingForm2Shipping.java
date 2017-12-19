package com.carson.mmall.converter;

import com.carson.mmall.dataobject.Shipping;
import com.carson.mmall.form.ShippingForm;

public class ShippingForm2Shipping {
    public static Shipping convert(ShippingForm shippingForm){
        Shipping shipping=new Shipping();
        shipping.setReceiverName(shippingForm.getReceiverName());
        shipping.setReceiverProvince(shippingForm.getReceiverProvince());
        shipping.setReceiverCity(shippingForm.getReceiverCity());
        shipping.setReceiverPhone(shippingForm.getReceiverPhone());
        shipping.setReceiverAddress(shippingForm.getReceiverAddress());
        shipping.setReceiverZip(shippingForm.getReceiverZip());
        return shipping;
    }
}
