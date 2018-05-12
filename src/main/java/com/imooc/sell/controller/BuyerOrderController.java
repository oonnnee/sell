package com.imooc.sell.controller;

import com.imooc.sell.converter.OrderForm2OrderDTOConverter;
import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exceptions.SellException;
import com.imooc.sell.form.OrderForm;
import com.imooc.sell.service.BuyerService;
import com.imooc.sell.service.OrderService;
import com.imooc.sell.utils.ResultVOUtil;
import com.imooc.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    @PostMapping("/create")
    public ResultVO<Map<String, String>> create(
            @Valid OrderForm orderForm,
            BindingResult bindingResult){

        /*------------ 1.表单校验 -------------*/
        if (bindingResult.hasErrors()){
            log.error("【创建订单】参数错误：{}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        /*------------ 2.由orderForm获得orderDTO -------------*/
        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);

        /*------------ 3.生成订单 -------------*/
        OrderDTO result = orderService.create(orderDTO);

        /*------------ 4.生成vo -------------*/
        Map<String, String> map = new HashMap<>();
        map.put("orderId", result.getOrderId());

        return ResultVOUtil.success(map);
    }


    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(
            @RequestParam(value = "openid") String openid,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ){
        Page<OrderDTO> orderDTOPage = orderService.findByBuyerOpenid(openid, new PageRequest(page, size));
        for (OrderDTO orderDTO : orderDTOPage.getContent()){
            orderDTO.setOrderDetails(null);
        }
        return ResultVOUtil.success(orderDTOPage.getContent());
    }

    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(
            @RequestParam(value = "openid") String openid,
            @RequestParam(value = "orderId") String orderId
    ){
        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtil.success(orderDTO);
    }

    @PostMapping("/cancel")
    public ResultVO cancel(
            @RequestParam(value = "openid") String openid,
            @RequestParam(value = "orderId") String orderId
    ){
        buyerService.cancelOrder(openid, orderId);
        return ResultVOUtil.success();
    }
}
