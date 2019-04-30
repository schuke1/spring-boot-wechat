package com.schuke.springboot.wechat.converter;

import com.schuke.springboot.wechat.dto.OrderDTO;
import com.schuke.springboot.wechat.pojo.Order;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author schuke
 * @date 2019/4/17 17:33
 */
public class OrderMaster2OrderDTOConverter {

    public static OrderDTO convert(Order orderMaster) {

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }

    public static List<OrderDTO> convert(List<Order> orderMasterList) {
        return orderMasterList.stream().map(e ->
                convert(e)
        ).collect(Collectors.toList());
    }
}
