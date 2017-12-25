package com.carson.mmall.controller.admin;

import com.carson.mmall.VO.ProductPageVO;
import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.config.CustomConfig;
import com.carson.mmall.dataobject.Product;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.service.ProductService;
import com.carson.mmall.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/manage/product")
public class AdminProductController {
    @Autowired
    private ProductService productService;


    @GetMapping("/list.do")
    public ResultVO list(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                         @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        ProductPageVO productPageVO=productService.adminList(pageNum,pageSize);
        return ResultVOUtil.success(productPageVO);
    }
    @GetMapping("/search.do")
    public ResultVO search(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                         @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                           @RequestParam(value = "productName",required = false) String productName,
                           @RequestParam(value = "productId",required = false) Integer productId){
        ProductPageVO productPageVO=productService.adminSearch(pageNum,pageSize,productName,productId);
        return ResultVOUtil.success(productPageVO);
    }

    @PostMapping("/upload.do")
    public ResultVO upload(@PathVariable("upload_file") MultipartFile upload_file){
        Map<String ,String> map=productService.upload(upload_file);
        return ResultVOUtil.success(map);
    }

    @GetMapping("/detail.do")
    public ResultVO detail(@RequestParam("productId") Integer productId){
        Product product=productService.detail(productId);
        return ResultVOUtil.success(product);
    }

}
