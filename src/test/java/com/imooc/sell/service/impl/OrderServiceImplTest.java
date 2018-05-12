package com.imooc.sell.service.impl;

import com.imooc.sell.dataobject.OrderDetail;
import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Test
    public void create() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("one");
        orderDTO.setBuyerPhone("18829534050");
        orderDTO.setBuyerOpenid("daemon");
        orderDTO.setBuyerAddress("西安邮电大学");
        orderDTO.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderDTO.setOrderStatus(OrderStatusEnum.NEW.getCode());
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setProductId("100");
        orderDetail1.setProductQuantity(400);
        orderDetails.add(orderDetail1);
        OrderDetail orderDetail2 = new OrderDetail();
        orderDetail2.setProductId("101");
        orderDetail2.setProductQuantity(400);
        orderDetails.add(orderDetail2);
        orderDTO.setOrderDetails(orderDetails);
        OrderDTO result = orderService.create(orderDTO);
    }

    @Test
    public void findByOrderId() {
        OrderDTO orderDTO = orderService.findByOrderId("1526089785140888658");
    }

    @Test
    public void findByBuyerOpenid() {
        Page<OrderDTO> orderDTOPage = orderService.findByBuyerOpenid("daemon", new PageRequest(0, 10));
    }

    @Test
    public void finish() {
    }

    @Test
    public void cancel() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId("1526089785140888658");
        orderDTO.setBuyerName("one");
        orderDTO.setBuyerPhone("18829534050");
        orderDTO.setBuyerOpenid("daemon");
        orderDTO.setBuyerAddress("西安邮电大学");
        orderDTO.setOrderAmount(new BigDecimal(75));
        orderDTO.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderDTO.setOrderStatus(OrderStatusEnum.NEW.getCode());
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setProductId("100");
        orderDetail1.setProductQuantity(10);
        orderDetails.add(orderDetail1);
        OrderDetail orderDetail2 = new OrderDetail();
        orderDetail2.setProductId("101");
        orderDetail2.setProductQuantity(10);
        orderDetails.add(orderDetail2);
        orderDTO.setOrderDetails(orderDetails);
        orderService.cancel(orderDTO);
    }

    @Test
    public void paid() {
    }
}