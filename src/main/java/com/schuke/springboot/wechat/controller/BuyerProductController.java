package com.schuke.springboot.wechat.controller;

import com.schuke.springboot.wechat.VO.ProductInfoVO;
import com.schuke.springboot.wechat.VO.ProductVO;
import com.schuke.springboot.wechat.VO.ResultVO;
import com.schuke.springboot.wechat.pojo.ProductCategory;
import com.schuke.springboot.wechat.pojo.ProductInfo;
import com.schuke.springboot.wechat.service.ProductCategoryService;
import com.schuke.springboot.wechat.service.ProductInfoService;
import com.schuke.springboot.wechat.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 买家商品
 * @author schuke
 * @date 2019/4/29 17:15
 */
@RequestMapping("/buyer/product")
@Controller
@Slf4j
public class BuyerProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;


    /**
     * 展示商品
     * @return
     */
    @RequestMapping("/list")
    public ResultVO list() {
        //1.查询所有上架的商品
        List <ProductInfo> productInfoList = productInfoService.findUpAll();
        //2. 查询类目(一次性查询)
        //精简方法(java8, lambda)
        List <Integer> categoryTypeList = productInfoList.stream()
                .map(e -> e.getProductCategoryType()).collect(Collectors.toList());

        List <ProductCategory> productCategoryList = productCategoryService.findByCategoryTypeIn(categoryTypeList);

        //3. 数据拼装
        List <ProductVO> productVOList = new ArrayList <>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List <ProductInfoVO> productInfoVOList = new ArrayList <>();
            for (ProductInfo productInfo : productInfoList) {
                ProductInfoVO productInfoVO = new ProductInfoVO();
                BeanUtils.copyProperties(productInfo, productInfoVO);
                productInfoVOList.add(productInfoVO);
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return ResultVOUtil.success(productVOList);
    }
}
