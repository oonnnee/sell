package com.imooc.sell.service;

import com.imooc.sell.dataobject.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductInfoService {

    List<ProductInfo> findUPProducts();

    ProductInfo findOne(String productId);

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    ProductInfo increaseStock(String productId, Integer quantity);

    ProductInfo decreaseStock(String productId, Integer quantity);
}
