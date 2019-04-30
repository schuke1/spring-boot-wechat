package com.schuke.springboot.wechat.service;

import com.schuke.springboot.wechat.pojo.ProductCategory;

import java.util.List;

/**
 *  商品类目
 * @author schuke
 * @date 2019/4/22 13:08
 */
public interface ProductCategoryService {

    ProductCategory fineOne(Integer categoryId);

    List <ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);

    void delete(Integer categoryId);
}
