package com.imooc.sell.repository;

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
public class ProductCategoryRepositoryTest {

    private static final String L = "["+ProductCategoryRepositoryTest.class.getName()+"] ";

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    @Transactional
    public void basic() throws InterruptedException {
        ProductCategory productCategory = new ProductCategory("饮料", 1);
        ProductCategory result = null;

        result = productCategoryRepository.save(productCategory);
        assertTrue(L+"save", result != null);

        productCategory.setCategoryType(2);
        result = productCategoryRepository.save(productCategory);
        assertTrue(L+"update", result.getCategoryType() == 2);

        result = productCategoryRepository.findOne(result.getCategoryId());
        assertTrue(L+"findOne", result != null);

        productCategoryRepository.delete(result.getCategoryId());
        result = productCategoryRepository.findOne(result.getCategoryId());
        assertTrue(L+"delete", result == null);
    }

    @Test
    public void findByCategoryTypeIn(){
        List<ProductCategory> productCategories = productCategoryRepository.findByCategoryTypeIn(Arrays.asList(1, 2, 3, 1));
        assertTrue(L+"findByCategoryTypeIn", productCategories != null);
    }
}