package com.schuke.springboot.wechat.repository;

import com.schuke.springboot.wechat.pojo.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 * @author schuke
 * @date 2019/4/22 18:00
 */
public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {

    List<ProductInfo> findByProductStatus(Integer productStatus);

}
