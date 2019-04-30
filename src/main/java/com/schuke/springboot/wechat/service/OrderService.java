package com.schuke.springboot.wechat.service;

import com.schuke.springboot.wechat.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author schuke
 * @date 2019/4/24 10:06
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param orderDTO
     * @return
     */
    OrderDTO createOrder(OrderDTO orderDTO);


    /**
     * 查询某个订单
     * @param orderId
     * @return
     */
    OrderDTO findOne(String orderId);


    /**
     * 查询所有订单
     *
     * @param pageable
     * @return
     */
    Page <OrderDTO> findAll(Pageable pageable);

    /**
     * 查询某个用户的所有订单
     * @param buyerOpenid
     * @param pageable
     * @return
     */
    Page<OrderDTO> findUserList(String buyerOpenid, Pageable pageable);

    /**
     * 取消订单
     *
     * @param orderDTO
     * @return
     */
    OrderDTO cancel(OrderDTO orderDTO);


    /**
     * 完结订单
     * @param orderDTO
     * @return
     */
    OrderDTO finsh(OrderDTO orderDTO);

    /**
     * 支付订单
     * @param orderDTO
     * @return
     */
    OrderDTO paid(OrderDTO orderDTO);
}
