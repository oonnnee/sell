package com.imooc.sell.handler;

import com.imooc.sell.exceptions.SellException;
import com.imooc.sell.vo.ResultVO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SellExceptionHandler {

    @ResponseBody
    @ExceptionHandler(SellException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResultVO handleSellException(SellException e){
        return new ResultVO(e.getCode(), e.getMessage());
    }

}
