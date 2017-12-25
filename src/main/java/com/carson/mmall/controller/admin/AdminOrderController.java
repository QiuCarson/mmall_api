package com.carson.mmall.controller.admin;

import com.carson.mmall.VO.OrderPageVO;
import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.dto.OrderDTO;
import com.carson.mmall.service.OrderService;
import com.carson.mmall.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manage/order")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/list.do")
    public ResultVO list(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        OrderPageVO orderPageVO = orderService.adminList(pageSize, pageNum);
        return ResultVOUtil.success(orderPageVO);
    }

    @GetMapping("/search.do")
    public ResultVO search(@RequestParam("orderNo") Long orderNo,
                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        OrderPageVO orderPageVO = orderService.adminSearch(orderNo, pageSize, pageNum);
        return ResultVOUtil.success(orderPageVO);
    }

    @GetMapping("/detail.do")
    public ResultVO detail(@RequestParam("orderNo") Long orderNo) {
        OrderDTO orderDTO = orderService.adminDetail(orderNo);
        return ResultVOUtil.success(orderDTO);
    }

    @GetMapping("/send_goods.do")
    public ResultVO sendGoods(@RequestParam("orderNo") Long orderNo) {
        OrderDTO orderDTO = orderService.adminSendGoods(orderNo);
        return ResultVOUtil.success();
    }
}
