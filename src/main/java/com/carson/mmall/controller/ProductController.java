package com.carson.mmall.controller;

import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.utils.ResultVOUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    @PostMapping("/list.do")
    public ResultVO list(@RequestBody Map<String,String> reqMap){
        return ResultVOUtil.success();
    }
}
