package com.schuke.springboot.wechat.service.impl;

import com.schuke.springboot.wechat.pojo.ProductCategory;
import com.schuke.springboot.wechat.repository.ProductCategoryRepository;
import com.schuke.springboot.wechat.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author schuke
 * @date 2019/4/22 13:11
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository categoryRepository;

    @Override
    public ProductCategory fineOne(Integer categoryId) {
        if (categoryId != null) {
            return categoryRepository.getOne(categoryId);
        }
        return null;
    }

    @Override
    public List<ProductCategory> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List <ProductCategory> findByCategoryTypeIn(List <Integer> categoryTypeList) {
        return categoryRepository.findAllById(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return  categoryRepository.save(productCategory);
    }


    @Override
    public void delete(Integer categoryId) {
        categoryRepository.deleteById(categoryId);
    }

}
