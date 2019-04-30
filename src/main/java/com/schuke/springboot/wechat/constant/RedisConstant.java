package com.schuke.springboot.wechat.constant;

/**
 * redis常量
 * @author schuke
 * @date 2019/4/17 17:33
 */
public interface RedisConstant {

    String TOKEN_PREFIX = "token_%s";

    Integer EXPIRE = 7200; //2小时
}
