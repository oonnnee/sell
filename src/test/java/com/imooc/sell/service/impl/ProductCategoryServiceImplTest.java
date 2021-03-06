package com.imooc.sell.service.impl;

import com.imooc.sell.dataobject.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryServiceImplTest {

    private static final String L = "["+ProductCategoryServiceImplTest.class.getName()+"] ";

    @Autowired
    private ProductCategoryServiceImpl productCategoryService;

    @Test
    public void findByCategoryTypeIn(){
        List<ProductCategory> productCategories = productCategoryService.findByCategoryTypeIn(Arrays.asList(1, 2, 3, 1));
        assertTrue(L+"findByCategoryTypeIn", productCategories != null);
    }

    @Test
    public void findOne(){
        ProductCategory productCategory = productCategoryService.findOne(100);
        System.out.println(productCategory);
    }

    @Test
    public void findAll(){
        List<ProductCategory> productCategories = productCategoryService.findAll();
        System.out.println(productCategories);
    }

    @Test
    @Transactional
    public void save(){

    }

}