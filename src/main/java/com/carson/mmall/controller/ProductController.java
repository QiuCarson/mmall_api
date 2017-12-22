package com.carson.mmall.controller;

import com.carson.mmall.VO.ProductPageVO;
import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.dataobject.Product;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.service.ProductService;
import com.carson.mmall.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @GetMapping("/list.do")
    public ResultVO list(@RequestParam(value = "categoryId",required = false) Integer categoryId,
                         @RequestParam(value = "keyword",required = false) String keyword,
                         @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                         @RequestParam(value = "pageSize",defaultValue = "20") Integer pageSize,
                         @RequestParam(value = "orderBy",defaultValue = "default") String orderBy){
        if(categoryId==null && keyword==null){
            throw new MmallException(ResultEnum.PARAM_ERROR);
        }
        ProductPageVO productPageVO=productService.list(categoryId,keyword,pageNum,pageSize,orderBy);
        return ResultVOUtil.success(productPageVO);
    }

    @GetMapping("/detail.do")
    public ResultVO detail(@RequestParam("productId") Integer productId){

        Product product=productService.detail(productId);
        return ResultVOUtil.success(product);
    }
}
