package com.schuke.springboot.wechat.service;

import com.schuke.springboot.wechat.dto.OrderDTO;

/**
 * 推送消息
 * @author schuke
 * @date 2019/4/17 17:33
 */
public interface PushMessageService {

    /**
     * 订单状态变更消息
     * @param orderDTO
     */
    void orderStatus(OrderDTO orderDTO);
}