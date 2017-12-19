package com.carson.mmall.controller;

import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.VO.ShippingVO;
import com.carson.mmall.common.Const;
import com.carson.mmall.converter.ShippingForm2Shipping;
import com.carson.mmall.dataobject.Shipping;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.form.ShippingForm;
import com.carson.mmall.service.ShippingService;
import com.carson.mmall.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @GetMapping("/add.do")
    public ResultVO add(@Valid ShippingForm shippingForm, BindingResult bindingResult, HttpSession session) {

        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        if (userId < 1) {
            throw new MmallException(ResultEnum.USERNAME_NOT_AUTH);
        }
        if (bindingResult.hasErrors()) {
            throw new MmallException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        Shipping shipping = ShippingForm2Shipping.convert(shippingForm);

        shipping.setUserId(userId);

        shipping = shippingService.add(shipping);

        Map<String, Integer> map = new HashMap<>();
        map.put("shippingId", shipping.getId());
        return ResultVOUtil.success(map);
    }

    @GetMapping("/del.do")
    public ResultVO del(@RequestParam("shippingId") Integer shippingId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        if (userId < 1) {
            throw new MmallException(ResultEnum.USERNAME_NOT_AUTH);
        }
        shippingService.del(userId, shippingId);
        return ResultVOUtil.success();
    }

    @GetMapping("/update.do")
    public ResultVO update(@Valid ShippingForm shippingForm, BindingResult bindingResult, HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        if (userId < 1) {
            throw new MmallException(ResultEnum.USERNAME_NOT_AUTH);
        }
        Shipping shipping = ShippingForm2Shipping.convert(shippingForm);
        shipping.setUserId(userId);

        shippingService.update(shipping);
        return ResultVOUtil.success();
    }

    @GetMapping("/select.do")
    public ResultVO select(@RequestParam("shippingId") Integer shippingId, HttpSession session){
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        if (userId < 1) {
            throw new MmallException(ResultEnum.USERNAME_NOT_AUTH);
        }
        Shipping shipping=shippingService.select(userId,shippingId);
        return ResultVOUtil.success(shipping);
    }

    @GetMapping("/list.do")
    public ResultVO list(@RequestParam("pageNum") Integer pageNum,
                         @RequestParam("pageSize") Integer pageSize,
                         HttpSession session){
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        if (userId < 1) {
            throw new MmallException(ResultEnum.USERNAME_NOT_AUTH);
        }
        ShippingVO shippingVO=shippingService.list(userId,pageNum,pageSize);
        return ResultVOUtil.success(shippingVO);
    }
}
