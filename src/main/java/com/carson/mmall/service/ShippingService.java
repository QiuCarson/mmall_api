package com.carson.mmall.service;

import com.carson.mmall.VO.ShippingVO;
import com.carson.mmall.dataobject.Shipping;

public interface ShippingService {
    Shipping add(Shipping shipping);

    void del(Integer userId,Integer shippingId);

    Shipping update(Shipping shipping);

    Shipping select(Integer userId,Integer shippingId);

    ShippingVO list(Integer userId,Integer pageNum,Integer pageSize);
}
