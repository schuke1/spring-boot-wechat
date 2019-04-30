package com.schuke.springboot.wechat.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.schuke.springboot.wechat.dto.OrderDTO;
import com.schuke.springboot.wechat.enums.ResultEnum;
import com.schuke.springboot.wechat.exception.SellException;
import com.schuke.springboot.wechat.form.OrderForm;
import com.schuke.springboot.wechat.pojo.OrderDetail;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
/**
 * @author schuke
 * @date 2019/4/17 17:33
 */
@Slf4j
public class OrderForm2OrderDTOConverter {

    public static OrderDTO convert(OrderForm orderForm) {
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(),
                    new TypeToken<List<OrderDetail>>() {
                    }.getType());
        } catch (Exception e) {
            log.error("【对象转换】错误, string={}", orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }
}
