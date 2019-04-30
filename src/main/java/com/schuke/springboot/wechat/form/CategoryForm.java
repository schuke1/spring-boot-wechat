package com.schuke.springboot.wechat.form;

import lombok.Data;

/**
 * @author schuke
 * @date 2019/4/17 17:33
 */
@Data
public class CategoryForm {

    private Integer categoryId;

    /** 类目名字. */
    private String categoryName;

    /** 类目编号. */
    private Integer categoryType;
}
