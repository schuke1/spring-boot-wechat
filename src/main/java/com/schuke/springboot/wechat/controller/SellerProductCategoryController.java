package com.schuke.springboot.wechat.controller;

import com.schuke.springboot.wechat.exception.SellException;
import com.schuke.springboot.wechat.form.CategoryForm;
import com.schuke.springboot.wechat.pojo.ProductCategory;
import com.schuke.springboot.wechat.service.ProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 卖家类目
 * @author schuke
 * @date 2019/4/28 14:16
 */
@RequestMapping("/seller/category")
@Controller
@Slf4j
public class SellerProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;


    /**
     * 所有的类目信息
     * @param map
     * @return
     */
    @GetMapping("/categortList")
    public ModelAndView categortList(Map <String, Object> map) {
        List <ProductCategory> productCategoryList = productCategoryService.findAll();
        map.put("productCategoryList", productCategoryList);
        return new ModelAndView("category/categortList", map);
    }

    /**
     * 根据 id 展示类目
     * @param map
     * @param categoryId
     * @return
     */
    public ModelAndView index(Map <String, Object> map, @RequestParam(value = "categoryId", required = false)Integer categoryId) {

        if (categoryId != null) {
            ProductCategory productCategory = productCategoryService.fineOne(categoryId);
            map.put("productCategory", productCategory);
        }
        return new ModelAndView("category/index",map);
    }


    /**
     * 保存/更新
     * @param form
     * @param bindingResult
     * @param map
     * @return
     */
    @PostMapping("/save")
    public ModelAndView save(@Valid CategoryForm form,
                             BindingResult bindingResult,
                             Map<String, Object> map) {
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/sell/seller/category/index");
            return new ModelAndView("common/error", map);
        }

        ProductCategory productCategory = new ProductCategory();
        try {
            if (form.getCategoryId() != null) {
                productCategory = productCategoryService.fineOne(form.getCategoryId());
            }
            BeanUtils.copyProperties(form, productCategory);
            productCategoryService.save(productCategory);
        } catch (SellException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/category/index");
            return new ModelAndView("common/error", map);
        }

        map.put("url", "/sell/seller/category/list");
        return new ModelAndView("common/success", map);
    }
}
