package com.schuke.springboot.wechat.repository;

import com.schuke.springboot.wechat.pojo.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author schuke
 * @date 2019/4/17 17:33
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    List<OrderDetail> findByOrderId(String orderId);
}
