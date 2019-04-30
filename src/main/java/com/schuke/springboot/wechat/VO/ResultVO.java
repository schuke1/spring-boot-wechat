package com.schuke.springboot.wechat.VO;

import lombok.Data;


/**
 * http请求返回的最外层对象
 * @author schuke
 * @date 2019/4/17 17:33
 */
@Data
public class ResultVO<T> {

    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 具体内容. */
    private T data;
}
