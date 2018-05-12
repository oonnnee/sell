package com.imooc.sell.service.impl;

import com.imooc.sell.converter.OrderDTO2OrderMasterConverter;
import com.imooc.sell.converter.OrderMaster2OrderDTOConverter;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
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
        /*------------ 1.准备 -------------*/
        // 生成orderId
        String orderId = KeyUtil.genUniqueKey();
        // 订单详情
        if (orderDTO == null){
            throw new SellException(ResultEnum.ORDER_EMPTY);
        }
        List<OrderDetail> orderDetails = orderDTO.getOrderDetails();
        if (orderDetails==null || orderDetails.size()==0){
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        // 计算订单金额
        BigDecimal orderAmount = BigDecimal.ZERO;
        for (OrderDetail orderDetail : orderDetails){
            ProductInfo productInfo = productInfoService.findOne(orderDetail.getProductId());
            if (productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderAmount = orderDetail.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);
        }
        // 填充orderDTO
        orderDTO.setOrderId(orderId);
        orderDTO.setOrderAmount(orderAmount);
        orderDTO.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderDTO.setPayStatus(PayStatusEnum.WAIT.getCode());
        for (OrderDetail orderDetail : orderDetails){
            ProductInfo productInfo = productInfoService.findOne(orderDetail.getProductId());
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetail.setOrderId(orderId);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
        }


        /*------------ 2.订单主表入库 -------------*/
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMasterRepository.save(orderMaster);

        /*------------ 3.订单详情入库 -------------*/
        for (OrderDetail orderDetail : orderDetails){
            orderDetailRepository.save(orderDetail);
        }

        /*------------ 4.减库存 -------------*/
        for (OrderDetail orderDetail : orderDetails){
            productInfoService.decreaseStock(orderDetail.getProductId(), orderDetail.getProductQuantity());
        }

        return orderDTO;
    }

    @Override
    public OrderDTO findByOrderId(String orderId) {
        // 1.查询订单主表
        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if (orderMaster == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        // 2.查询订单详情表
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetails)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        // 3.数据拼装
        OrderDTO orderDTO = OrderMaster2OrderDTOConverter.convert(orderMaster);
        orderDTO.setOrderDetails(orderDetails);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findByBuyerOpenid(String openid, Pageable pageable) {
        // 1.查询订单主表
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(openid, pageable);
        if (orderMasterPage==null || CollectionUtils.isEmpty(orderMasterPage.getContent())){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        // 2.根据订单主表查询订单详情
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (OrderMaster orderMaster : orderMasterPage.getContent()){
            OrderDTO orderDTO = OrderMaster2OrderDTOConverter.convert(orderMaster);
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderMaster.getOrderId());
            if (CollectionUtils.isEmpty(orderDetails)){
                throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
            }
            orderDTO.setOrderDetails(orderDetails);
            orderDTOS.add(orderDTO);
        }

        return new PageImpl<OrderDTO>(orderDTOS, pageable, orderMasterPage.getTotalPages());
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【完结订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISH.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【完结订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        /*------------ 1.判断订单状态 -------------*/
        if (orderDTO.getOrderStatus() != OrderStatusEnum.NEW.getCode()){
            log.error("【取消订单】{}, orderId={}, orderStatus={}", ResultEnum.ORDER_STATUS_ERROR.getMessage(),
                    orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        /*------------ 2.修改订单状态 -------------*/
        OrderMaster orderMaster = OrderDTO2OrderMasterConverter.convert(orderDTO);
        orderMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if (result==null || result.getOrderStatus()!=OrderStatusEnum.CANCEL.getCode()){
            log.error("【取消订单】{}", ResultEnum.ORDER_UPDATE_FAIL.getMessage());
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        /*------------ 3.返还库存 -------------*/
        List<OrderDetail> orderDetails = orderDTO.getOrderDetails();
        if (CollectionUtils.isEmpty(orderDetails)){
            throw new SellException((ResultEnum.ORDERDETAIL_NOT_EXIST));
        }
        for (OrderDetail orderDetail : orderDetails){
            productInfoService.increaseStock(orderDetail.getProductId(), orderDetail.getProductQuantity());
        }

        /*------------ 4.如果已支付，退款 -------------*/
        if (orderDTO.getPayStatus() == PayStatusEnum.PAID.getCode()){
            //TODO 退款
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【订单支付完成】订单支付状态不正确, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.PAID.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【订单支付完成】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }
}
