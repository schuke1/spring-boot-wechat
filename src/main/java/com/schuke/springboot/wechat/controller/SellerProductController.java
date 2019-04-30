package com.schuke.springboot.wechat.controller;

import com.schuke.springboot.wechat.enums.ProductStatusEnum;
import com.schuke.springboot.wechat.exception.SellException;
import com.schuke.springboot.wechat.form.ProductForm;
import com.schuke.springboot.wechat.pojo.ProductCategory;
import com.schuke.springboot.wechat.pojo.ProductInfo;
import com.schuke.springboot.wechat.service.ProductCategoryService;
import com.schuke.springboot.wechat.service.ProductInfoService;
import com.schuke.springboot.wechat.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 卖家端商品
 * @author schuke
 * @date 2019/4/29 10:36
 */
@RequestMapping("/seller/product")
@Controller
@Slf4j
public class SellerProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;


    /**
     * list
     * @param pageNum
     * @param pageSize
     * @param map
     * @return
     */
    public ModelAndView list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                             Map <String, Object> map) {
        PageRequest pageRequest = new PageRequest(pageNum - 1, pageSize);
        Page <ProductInfo> productInfos = productInfoService.findAllByPage(pageRequest);
        map.put("productInfos", productInfos);
        map.put("currentPage", pageNum);
        map.put("pageSize", pageSize);
        return new ModelAndView("product/list", map);
    }


    /**
     * 商品上架
     * @param productId
     * @param map
     * @return
     */
    @RequestMapping("/on_sale")
    public ModelAndView onSale(@RequestParam(value = "prodcutId") String productId,
                               Map <String, Object> map) {

        try {
            if (!StringUtils.isEmpty(productId)) {
                productInfoService.onSale(productId);
            }
        } catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/product/list");
            return new ModelAndView("common/error", map);
        }
        map.put("url", "/sell/seller/product/list");
        return new ModelAndView("common/success", map);
    }


    /**
     * 商品下架
     * @param productId
     * @param map
     * @return
     */
    @RequestMapping("/off_sale")
    public ModelAndView offSale(@RequestParam(value = "productId") String productId,
                                Map <String, Object> map) {
        try {
            if (!StringUtils.isEmpty(productId)) {
                productInfoService.offSale(productId);
            }
        } catch (Exception e) {
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/product/list");
            return new ModelAndView("common/error", map);
        }
        map.put("url", "/sell/seller/product/list");
        return new ModelAndView("common/success", map);
    }


    /**
     * 某个类目的下所有子类
     * @param productId
     * @param map
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(@RequestParam(value = "productId") String productId,
                              Map <String, Object> map) {
        if (!StringUtils.isEmpty(productId)) {
            ProductInfo productInfo = productInfoService.findOne(productId);
            map.put("productInfo", productInfo);
        }

        //查询所有的类目
        List<ProductCategory> categoryList = productCategoryService.findAll();
        map.put("categoryList", categoryList);

        return new ModelAndView("product/index", map);
    }

    /**
     * 保存/更新
     * @param productForm
     * @param bindingResult
     * @param map
     * @return
     */
    @RequestMapping("/save")
    public ModelAndView save(@Valid ProductForm productForm,
                             BindingResult bindingResult,
                             Map <String, Object> map) {
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/sell/seller/product/index");
            return new ModelAndView("common/error", map);
        }

        ProductInfo productInfo = new ProductInfo();

        //如果id为空，则是新增
        try {
            if (!StringUtils.isEmpty(productForm.getProductId())) {
                productInfo = productInfoService.findOne(productForm.getProductId());
            } else {
                productForm.setProductId(KeyUtil.genUniqueKey());
            }
            BeanUtils.copyProperties(productForm, productInfo);
            productInfoService.save(productInfo);
        } catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/product/index");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/sell/seller/product/list");
        return new ModelAndView("common/success", map);
    }
}
