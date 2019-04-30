package com.schuke.springboot.wechat.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author schuke
 * @date 2019/4/17 17:33
 */
public class JsonUtil {

    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }
}
