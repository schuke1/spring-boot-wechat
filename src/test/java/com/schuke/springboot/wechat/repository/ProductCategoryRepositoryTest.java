package com.schuke.springboot.wechat.repository;

import com.schuke.springboot.wechat.pojo.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author schuke
 * @date 2019/4/22 11:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void fineOneTest() {
        ProductCategory productCategory = productCategoryRepository.getOne(1);
        System.out.println(productCategory.toString());
    }
}