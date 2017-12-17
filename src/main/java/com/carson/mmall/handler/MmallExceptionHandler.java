package com.carson.mmall.handler;

import com.carson.mmall.VO.ResultVO;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by qiucarson on 2017/11/25.
 */
@ControllerAdvice
@Slf4j
public class MmallExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultVO handle(Exception e) {
        if(e instanceof MmallException){
            MmallException mmallException=(MmallException) e;
            return ResultVOUtil.error(mmallException.getCode(), mmallException.getMessage());
        }else{
            log.error("【异常错误】{}",e);
            return ResultVOUtil.error(ResultEnum.UNKNOWN_ERROR);
        }

    }
}
