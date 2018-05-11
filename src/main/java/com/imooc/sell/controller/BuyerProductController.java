package com.imooc.sell.controller;

import com.imooc.sell.dataobject.ProductCategory;
import com.imooc.sell.dataobject.ProductInfo;
import com.imooc.sell.service.ProductCategoryService;
import com.imooc.sell.service.ProductInfoService;
import com.imooc.sell.vo.ResultVO;
import com.imooc.sell.vo.buyer.ProductCategoryVO;
import com.imooc.sell.vo.buyer.ProductInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/list")
    public ResultVO list(){
        // 1.获取所有已上架商品
        List<ProductInfo> productInfos = productInfoService.findUPProducts();

        // 2.根据已上架商品查询所有商品类别
        List<Integer> categoryTypes = productInfos.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        List<ProductCategory> productCategories = productCategoryService.findByCategoryTypeIn(categoryTypes);

        // 3.数据拼装
        List<ProductCategoryVO> productCategoryVOS = new ArrayList<>();
        for (ProductCategory productCategory : productCategories){
            ProductCategoryVO productCategoryVO = new ProductCategoryVO();
            productCategoryVO.setCategoryName(productCategory.getCategoryName());
            productCategoryVO.setCategoryType(productCategory.getCategoryType());
            List<ProductInfoVO> productInfoVOS = new ArrayList<>();
            for (ProductInfo productInfo : productInfos){
                if (productInfo.getCategoryType() == productCategory.getCategoryType()){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOS.add(productInfoVO);
                }
            }
            productCategoryVO.setFoods(productInfoVOS);
            productCategoryVOS.add(productCategoryVO);
        }

        // 4.返回
        return  ResultVO.success(productCategoryVOS);
    }

}
