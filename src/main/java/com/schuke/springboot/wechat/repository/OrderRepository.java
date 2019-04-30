package com.schuke.springboot.wechat.repository;

import com.schuke.springboot.wechat.pojo.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 主订单表
 *
 * @author schuke
 * @date 2019/4/24 10:01
 */
public interface OrderRepository extends JpaRepository <Order, String> {

    Page <Order> findByBuyerOpenid(String buyerOpendid, Pageable pageable);
}
