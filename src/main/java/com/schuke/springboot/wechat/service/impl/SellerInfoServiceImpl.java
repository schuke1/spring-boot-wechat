package com.schuke.springboot.wechat.service.impl;

import com.schuke.springboot.wechat.pojo.SellerInfo;
import com.schuke.springboot.wechat.repository.SellerInfoRepository;
import com.schuke.springboot.wechat.service.SellerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author schuke
 * @date 2019/4/25 15:16
 */
@Service
@Slf4j
public class SellerInfoServiceImpl implements SellerInfoService {

    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        if (openid != null) {
            return sellerInfoRepository.findByOpenid(openid);
        }
        return null;
    }
}
