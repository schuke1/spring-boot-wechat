package com.schuke.springboot.wechat.service;

import com.schuke.springboot.wechat.dto.OrderDTO;

/**
 * 买家
 * @author schuke
 * @date 2019/4/25 15:25
 */
public interface BuyerService {

    /**
     * 查询一个订单
     * @param openid
     * @param orderId
     * @return
     */
    OrderDTO findOrderOne(String openid, String orderId);

    /**
     * 取消订单
     * @param openid
     * @param orderId
     * @return
     */
    OrderDTO cancelOrder(String openid, String orderId);
}
