package com.schuke.springboot.wechat.repository;

import com.schuke.springboot.wechat.pojo.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author schuke
 * @date 2019/4/17 17:33
 */
public interface SellerInfoRepository extends JpaRepository<SellerInfo, String> {
    SellerInfo findByOpenid(String openid);
}
