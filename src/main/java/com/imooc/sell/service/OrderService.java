package com.imooc.sell.service;

import com.imooc.sell.dto.OrderDTO;
import com.imooc.sell.vo.ResultVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface OrderService {

    OrderDTO create(OrderDTO orderDTO);

    OrderDTO findByOrderId(String orderId);

    Page<OrderDTO> findByBuyerOpenid(String openid, Pageable pageable);

    OrderDTO finish(OrderDTO orderDTO);

    OrderDTO cancel(OrderDTO orderDTO);

    OrderDTO paid(OrderDTO orderDTO);
}
