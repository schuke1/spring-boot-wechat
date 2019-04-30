package com.schuke.springboot.wechat.service;

import com.schuke.springboot.wechat.dto.CartDTO;
import com.schuke.springboot.wechat.pojo.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品
 *
 * @author schuke
 * @date 2019/4/23 11:50
 */
public interface ProductInfoService {
    ProductInfo findOne(String productId);

    //查询所有在架的商品
    List <ProductInfo> findUpAll();

    Page<ProductInfo> findAllByPage(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    //加库存
    void increaseStock(List<CartDTO> cartDTOList);

    //减库存
    void decreaseStock(List<CartDTO> cartDTOList);

    //上架
    ProductInfo onSale(String productId);

    //下架
    ProductInfo offSale(String productId);
}
