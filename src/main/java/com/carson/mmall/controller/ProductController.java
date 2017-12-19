package com.carson.mmall.controller;

import com.carson.mmall.VO.ProductVO;
import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.dataobject.Product;
import com.carson.mmall.service.ProductService;
import com.carson.mmall.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @GetMapping("/list.do")
    public ResultVO list(@RequestParam(value = "categoryId",defaultValue = "0",required = false) Integer categoryId,
                         @RequestParam(value = "keyword",required = false) String keyword,
                         @RequestParam(value = "pageNum",required = false,defaultValue = "0") Integer pageNum,
                         @RequestParam(value = "pageSize",defaultValue = "20",required = false) Integer pageSize,
                         @RequestParam(value = "orderBy",defaultValue = "default",required = false) String orderBy){
        ProductVO productVO=productService.list(categoryId,keyword,pageNum,pageSize,orderBy);
        return ResultVOUtil.success(productVO);
    }

    @GetMapping("/detail.do")
    public ResultVO detail(@RequestParam("productId") Integer productId){

        Product product=productService.detail(productId);
        return ResultVOUtil.success(product);
    }
}
