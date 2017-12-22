package com.carson.mmall.controller;

import com.carson.mmall.VO.CartVO;
import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.common.Const;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.service.CartService;
import com.carson.mmall.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/list.do")
    public ResultVO list(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);

        CartVO cartVO = cartService.cartList(userId);
        return ResultVOUtil.success(cartVO);
    }

    @GetMapping("/add.do")
    public ResultVO add(@RequestParam("productId") Integer productId,
                        @RequestParam("count") Integer count,
                        HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        CartVO cartVO = cartService.add(userId, productId, count);
        return ResultVOUtil.success(cartVO);
    }

    @GetMapping("/update.do")
    public ResultVO update(@RequestParam("productId") Integer productId,
                           @RequestParam("count") Integer count,
                           HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        CartVO cartVO = cartService.update(userId, productId, count);
        return ResultVOUtil.success(cartVO);
    }

    @GetMapping("/delete_product.do")
    public ResultVO delete(@RequestParam("productIds") String productIds, HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        List<Integer> productList = new ArrayList();
        String[] productIdArray = productIds.split(",");
        log.info("productIdArray={}",productIdArray);
        for (String productId : productIdArray) {
            Integer pid= Integer.valueOf(productId);
            productList.add(pid);
        }
        CartVO cartVO = cartService.deleteList(userId, productList);
        return ResultVOUtil.success(cartVO);
    }

    @GetMapping("/select.do")
    public ResultVO select(@RequestParam("productId") Integer productId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        CartVO cartVO = cartService.select(userId, productId);
        return ResultVOUtil.success(cartVO);
    }

    @GetMapping("/un_select.do")
    public ResultVO unselect(@RequestParam("productId") Integer productId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        CartVO cartVO = cartService.unselect(userId, productId);
        return ResultVOUtil.success(cartVO);
    }

    @GetMapping("/get_cart_product_count.do")
    public ResultVO count(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        Integer count = cartService.count(userId);
        return ResultVOUtil.success(count);
    }

    @GetMapping("/select_all.do")
    public ResultVO selectAll(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        CartVO cartVO = cartService.selectAll(userId);
        return ResultVOUtil.success(cartVO);
    }

    @GetMapping("/un_select_all.do")
    public ResultVO unSelectAll(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.SESSION_AUTH_ID);
        CartVO cartVO = cartService.unSelectAll(userId);
        return ResultVOUtil.success(cartVO);
    }
}
