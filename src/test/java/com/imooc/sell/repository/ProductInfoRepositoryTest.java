package com.imooc.sell.repository;

import com.imooc.sell.dataobject.ProductInfo;
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
public class ProductInfoRepositoryTest {

    private static final String L = "["+ProductInfoRepositoryTest.class.getName()+"] ";

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Test
    @Transactional
    public void basic() {
        ProductInfo productInfo = new ProductInfo();

        productInfo.setProductId("123");
        productInfo.setProductName("芒果冰");
        productInfo.setProductPrice(new BigDecimal(4));
        productInfo.setProductStock(1000);
        productInfo.setProductIcon("http://www.xxx.com");
        productInfo.setCategoryType(1);
        productInfo.setProductDescription("好吃美味");

        ProductInfo result = null;

        result = productInfoRepository.save(productInfo);
        assertTrue(L+"save", result != null);

        productInfo.setCategoryType(2);
        result = productInfoRepository.save(productInfo);
        assertTrue(L+"update", result.getCategoryType() == 2);

        result = productInfoRepository.findOne("123");
        assertTrue(L+"findOne", result != null);

        productInfoRepository.delete("123");
        result = productInfoRepository.findOne("123");
        assertTrue(L+"delete", result == null);
    }
}