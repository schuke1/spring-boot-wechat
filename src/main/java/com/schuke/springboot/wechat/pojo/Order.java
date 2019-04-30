package com.schuke.springboot.wechat.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * @author schuke
 * @date 2019/4/23 15:27
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {


    /**
     * 订单id
     */
    @Id
    private String orderId;

    /**
     * 订单总金额
     */
    private BigDecimal orderAmount;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 买家名字
     */
    private String buyerName;

    /**
     * 买家手机
     */
    private String buyerPhone;

    /**
     * 买家地址
     */
    private String buyerAddress;


    /**
     * 买家Openid
     */
    private String buyerOpenid;

    /**
     * 支付状态，默认未支付
     */
    private Integer payStatus;
}