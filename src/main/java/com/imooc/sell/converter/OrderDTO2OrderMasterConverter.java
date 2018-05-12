package com.imooc.sell.converter;

import com.imooc.sell.dataobject.OrderMaster;
import com.imooc.sell.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO2OrderMasterConverter {

    public static OrderMaster convert(OrderDTO orderDTO){
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        return orderMaster;
    }

    public static List<OrderMaster> convert(List<OrderDTO> orderDTOS){
        return orderDTOS.stream().map(e->convert(e)).collect(Collectors.toList());
    }

}
