package com.imooc.sell;

import com.imooc.sell.dataobject.ProductCategory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleTest {

    @Test
    public void test(){
        List<ProductCategory> productCategories = new ArrayList<>();
        productCategories.add(new ProductCategory("饮料", 1));
        productCategories.add(new ProductCategory("雪糕", 2));
        System.out.println(productCategories);

        System.out.println("=======================");
        productCategories.get(0).setCreateTime(new Date());
        System.out.println(productCategories);

        System.out.println("========================");
        for (ProductCategory productCategory : productCategories){
            productCategory.setCategoryId(1);
        }
        System.out.println(productCategories);
    }

}
