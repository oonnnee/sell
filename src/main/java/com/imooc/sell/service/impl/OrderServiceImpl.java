package com.imooc.sell.service.impl;

import com.imooc.sell.dataobject.OrderDetail;
import com.imooc.sell.dataobject.OrderMaster;
import com.imooc.sell.dataobject.ProductInfo;
import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.enums.OrderStatusEnum;
import com.imooc.sell.enums.PayStatusEnum;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exceptions.SellException;
import com.imooc.sell.repository.OrderDetailRepository;
import com.imooc.sell.repository.OrderMasterRepository;
import com.imooc.sell.service.OrderService;
import com.imooc.sell.service.ProductInfoService;
import com.imooc.sell.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductInfoService productInfoService;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        // 1.准备
        String orderId = KeyUtil.genUniqueKey();
        List<OrderDetail> orderDetails = orderDTO.getOrderDetails();

        // 1.生成订单主表
        OrderMaster orderMaster = new OrderMaster();

        orderDTO.setOrderId(orderId);
        BigDecimal amount = BigDecimal.ZERO;

        for (OrderDetail orderDetail : orderDTO.getOrderDetails()){
            ProductInfo productInfo = productInfoService.findOne(orderDetail.getProductId());
            BeanUtils.copyProperties(productInfo, orderDetail);
            amount = orderDetail.getProductPrice()
                        .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                        .add(amount);
        }
        orderDTO.setOrderAmount(amount);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMasterRepository.save(orderMaster);

        // 2.生成订单详情表
        for (OrderDetail orderDetail : orderDTO.getOrderDetails()){
            ProductInfo productInfo = productInfoService.findOne(orderDetail.getProductId());
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            orderDetailRepository.save(orderDetail);

            // 3.减库存
            productInfoService.decreaseStock(orderDetail.getProductId(), orderDetail.getProductQuantity());
        }

        return orderDTO;
    }

    @Override
    public OrderDTO findByOrderId(String orderId) {
        // 1.查询订单主表
        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);

        // 2.查询订单详情表
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);

        // 3.数据拼装
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetails(orderDetails);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findByBuyerOpenid(String openid, Pageable pageable) {
        // 1.查询订单主表
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(openid, pageable);

        // 2.根据订单主表查询订单详情
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (OrderMaster orderMaster : orderMasterPage.getContent()){
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(orderMaster, orderDTO);
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderMaster.getOrderId());
            orderDTO.setOrderDetails(orderDetails);
            orderDTOS.add(orderDTO);
        }

        return new PageImpl<OrderDTO>(orderDTOS, pageable, orderMasterPage.getTotalPages());
    }

    @Override
    public OrderDTO finish(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO cancel(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO paid(OrderDTO orderDTO) {
        return null;
    }
}
