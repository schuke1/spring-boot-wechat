package com.schuke.springboot.wechat.utils;

import com.schuke.springboot.wechat.enums.CodeEnum;

/**
 * @author schuke
 * @date 2019/4/17 17:33
 */
public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
