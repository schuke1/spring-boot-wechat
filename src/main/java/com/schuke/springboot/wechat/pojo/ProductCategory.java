package com.schuke.springboot.wechat.pojo;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author schuke
 * @date 2019/4/22 10:59
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class ProductCategory {

    /**
     * 类目id
     */
    @Id
    @GeneratedValue
    private Integer categoryId;

    /**
     * 类目名字
     */
    private String categoryName;

    /**
     * 类目编号，类别
     */
    private Integer categoryType;


    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryType = categoryType;
        this.categoryName = categoryName;
    }
}
