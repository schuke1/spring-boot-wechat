package com.schuke.springboot.wechat.service;

import com.schuke.springboot.wechat.pojo.SellerInfo;

/**
 * 卖家端
 * @author schuke
 * @date 2019/4/25 11:59
 */
public interface SellerInfoService {

    /**
     * 通过openid查询卖家端信息
     * @param openid
     * @return
     */
    SellerInfo findSellerInfoByOpenid(String openid);
}
