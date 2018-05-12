package com.imooc.sell.controller;

import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exceptions.SellException;
import com.imooc.sell.form.OrderForm;
import com.imooc.sell.service.OrderService;
import com.imooc.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/create")
    public ResultVO<Map<String, String>> create(
            @Valid OrderForm orderForm,
            BindingResult bindingResult){

        // 1.表单校验
        if (bindingResult.hasErrors()){
            log.error("【创建订单】参数错误：{}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        return null;
    }

}
