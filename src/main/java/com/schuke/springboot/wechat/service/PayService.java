package com.schuke.springboot.wechat.service;

import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;
import com.schuke.springboot.wechat.dto.OrderDTO;

/**
 * 支付
 * @author schuke
 * @date 2019/4/17 17:33
 */
public interface PayService {

    /**
     * 发起支付
     * @param orderDTO
     * @return
     */
    PayResponse createPay(OrderDTO orderDTO);


    /**
     * 异步通知
     * @param notifyData
     * @return
     */
    PayResponse notify(String notifyData);


    /**
     * 退款
     * @param orderDTO
     * @return
     */
    RefundResponse refund(OrderDTO orderDTO);
}