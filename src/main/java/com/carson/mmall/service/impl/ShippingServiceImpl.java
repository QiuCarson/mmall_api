package com.carson.mmall.service.impl;

import com.carson.mmall.VO.ShippingVO;
import com.carson.mmall.dataobject.Shipping;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.repository.ShippingRepository;
import com.carson.mmall.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingRepository shippingRepository;

    @Override
    public Shipping add(Shipping shipping) {

        return shippingRepository.save(shipping);

    }

    @Override
    public void del(Integer userId,Integer shippingId) {
        Shipping shipping=shippingRepository.findTopByIdAndUserId(userId,shippingId);
        if(shipping==null){
            throw new MmallException(ResultEnum.SHIPPING_NOT_EXISTS);
        }
        shippingRepository.delete(shipping);
    }

    @Override
    public Shipping update(Shipping shipping) {
        Shipping shippingOld=shippingRepository.findTopByIdAndUserId(shipping.getId(),shipping.getUserId());
        if(shippingOld==null){
            throw new MmallException(ResultEnum.SHIPPING_NOT_EXISTS);
        }
        return shippingRepository.save(shipping);
    }

    @Override
    public Shipping select(Integer userId,Integer shippingId) {
        Shipping shipping=shippingRepository.findTopByIdAndUserId(userId,shippingId);
        if(shipping==null){
            throw new MmallException(ResultEnum.SHIPPING_NOT_EXISTS);
        }
        return shipping;

    }

    @Override
    public ShippingVO list(Integer userId,Integer pageNum, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        Page<Shipping> shippingPage=shippingRepository.findByUserId(userId,pageable);

        ShippingVO shippingVO=new ShippingVO();
        shippingVO.setList(shippingPage.getContent());
        return null;
    }
}
