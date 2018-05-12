package com.imooc.sell.repository;

import com.imooc.sell.dataobject.OrderMaster;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    private static final String L = "["+OrderMasterRepositoryTest.class.getName()+"] ";

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Test
    @Transactional
    public void basic() {
        OrderMaster orderMaster = new OrderMaster();

        orderMaster.setOrderId("123");
        orderMaster.setBuyerOpenid("wxid123");
        orderMaster.setBuyerName("one");
        orderMaster.setBuyerAddress("西安邮电大学");
        orderMaster.setBuyerPhone("18829534050");
        orderMaster.setOrderAmount(new BigDecimal(27.5));

        OrderMaster result = null;

        result = orderMasterRepository.save(orderMaster);
        assertTrue(L+"save", result != null);

        orderMaster.setBuyerName("oonnnee");
        result = orderMasterRepository.save(orderMaster);
        assertTrue(L+"update", result.getBuyerName().equals("oonnnee"));

        result = orderMasterRepository.findOne("123");
        assertTrue(L+"findOne", result != null);

        orderMasterRepository.delete("123");
        result = orderMasterRepository.findOne("123");
        assertTrue(L+"delete", result == null);
    }

    @Test
    public void findByBuyerOpenid(){
        PageRequest pageRequest = new PageRequest(0, 10);
        Page<OrderMaster> orderMasters = orderMasterRepository.findByBuyerOpenid("daemon", pageRequest);
    }
}