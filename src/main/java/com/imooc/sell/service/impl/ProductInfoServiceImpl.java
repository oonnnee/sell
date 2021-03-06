package com.imooc.sell.service.impl;

import com.imooc.sell.dataobject.ProductInfo;
import com.imooc.sell.enums.ProductStatusEnum;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.exceptions.SellException;
import com.imooc.sell.repository.ProductInfoRepository;
import com.imooc.sell.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public List<ProductInfo> findUPProducts() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public ProductInfo findOne(String productId) {
        return productInfoRepository.findOne(productId);
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }

    @Override
    public ProductInfo increaseStock(String productId, Integer quantity) {
        ProductInfo productInfo = productInfoRepository.findOne(productId);
        productInfo.setProductStock(productInfo.getProductStock()+quantity);
        return productInfoRepository.save(productInfo);
    }

    @Override
    public ProductInfo decreaseStock(String productId, Integer quantity) {
        ProductInfo productInfo = productInfoRepository.findOne(productId);
        int leave = productInfo.getProductStock()-quantity;
        if (leave < 0){
            throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
        }
        productInfo.setProductStock(leave);
        return productInfoRepository.save(productInfo);
    }
}
