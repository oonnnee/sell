package com.imooc.sell.repository;

import com.imooc.sell.dataobject.OrderDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {
    private static final String L = "["+OrderDetailRepositoryTest.class.getName()+"] ";

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    @Transactional
    public void basic() {
        OrderDetail orderDetail = new OrderDetail();

        orderDetail.setDetailId("123");
        orderDetail.setOrderId("1234");
        orderDetail.setProductId("12345");
        orderDetail.setProductName("芒果冰");
        orderDetail.setProductPrice(new BigDecimal(4));
        orderDetail.setProductQuantity(4);
        orderDetail.setProductIcon("http://www.xxx.com");

        OrderDetail result = null;

        result = orderDetailRepository.save(orderDetail);
        assertTrue(L+"save", result != null);

        orderDetail.setProductName("oonnnee");
        result = orderDetailRepository.save(orderDetail);
        assertTrue(L+"update", result.getProductName().equals("oonnnee"));

        result = orderDetailRepository.findOne("123");
        assertTrue(L+"findOne", result != null);

        orderDetailRepository.delete("123");
        result = orderDetailRepository.findOne("123");
        assertTrue(L+"delete", result == null);
    }
}