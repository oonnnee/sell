package com.imooc.sell.service.impl;

import com.imooc.sell.dataobject.ProductInfo;
import com.imooc.sell.repository.ProductInfoRepositoryTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoServiceImplTest {

    private static final String L = "["+ProductInfoServiceImplTest.class.getName()+"] ";

    @Autowired
    private ProductInfoServiceImpl productInfoService;

    @Test
    public void findUPProducts() {
        List<ProductInfo> productInfos = productInfoService.findUPProducts();
        assertTrue(L+"findByProductStatus", productInfos!=null && productInfos.size()>0);
    }
}