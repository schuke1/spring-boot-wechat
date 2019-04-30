package com.schuke.springboot.wechat.repository;

import com.schuke.springboot.wechat.pojo.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author schuke
 * @date 2019/4/22 11:12
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {

    List <ProductCategory> findByCategoryTypeIn(List <Integer> categoryTypeList);

}
