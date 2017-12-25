package com.carson.mmall.controller.admin;

import com.carson.mmall.VO.ProductPageVO;
import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.converter.ProductForm2ProductConvert;
import com.carson.mmall.dataobject.Product;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.form.ProductForm;
import com.carson.mmall.service.ProductService;
import com.carson.mmall.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

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

    @GetMapping("/set_sale_status.do")
    public ResultVO setSaleStatus(@RequestParam("productId") Integer productId,
                                  @RequestParam("status") Integer status){
        Product product=productService.setSaleStatus(productId,status);
        return ResultVOUtil.success();

    }

    @GetMapping("/save.do")
    public ResultVO save(@Valid ProductForm productForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new MmallException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }
        Product product= ProductForm2ProductConvert.convert(productForm);
        productService.adminSave(product);

        return ResultVOUtil.success();
    }


    @PostMapping("richtext_img_upload.do")
    public Map<String ,String> richtextImgUpload(@PathVariable("upload_file") MultipartFile upload_file){
        Map<String ,String> map=productService.richtextUpload(upload_file);
        return map;
    }

}
