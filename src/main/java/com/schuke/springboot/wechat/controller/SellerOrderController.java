package com.schuke.springboot.wechat.controller;

import com.schuke.springboot.wechat.dto.OrderDTO;
import com.schuke.springboot.wechat.enums.ResultEnum;
import com.schuke.springboot.wechat.exception.SellException;
import com.schuke.springboot.wechat.service.OrderService;
import com.schuke.springboot.wechat.service.PayService;
import com.schuke.springboot.wechat.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 卖家订单
 * @author schuke
 * @date 2019/4/29 15:03
 */
@RequestMapping("/seller/order")
@Controller
@Slf4j
public class SellerOrderController {

    @Autowired
    private OrderService orderService;


    /**
     * 某个买家的所有订单
     * @param buyerOpenid
     * @param pageNum
     * @param pageSize
     * @param map
     * @return
     */
    @GetMapping("/listByOpenid")
    public ModelAndView listByOpenid(@RequestParam(value = "buyerOpenid") String buyerOpenid,
                                     @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                     Map <String, Object> map) {
        if (StringUtils.isEmpty(buyerOpenid)) {
            map.put("msg", ResultEnum.LOGIN_FAIL.getMessage());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error");
        }
        PageRequest pageRequest = new PageRequest(pageNum - 1, pageSize);
        Page<OrderDTO> orderDTOS = orderService.findUserList(buyerOpenid,pageRequest);
        map.put("orderDTOS", orderDTOS);
        map.put("currentPage", pageNum);
        map.put("pageSize", pageSize);
        return new ModelAndView("order/listByOpenid", map);
    }

    /**
     * 订单详情
     * @param orderId
     * @param map
     * @return
     */
    @GetMapping("/orderByOrderId")
    public ModelAndView orderDetail(@RequestParam(value = "orderId") String orderId,
                                    Map <String, Object> map) {
        OrderDTO orderDTO = new OrderDTO();
        try {
            orderDTO = orderService.findOne(orderId);
        }catch (SellException e) {
            log.error("【卖家端查询订单详情】发生异常{}", e);
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error", map);
        }

        map.put("orderDTO", orderDTO);
        return new ModelAndView("order/detail", map);
    }


    /**
     * 查询所有订单
     * @param pageNum
     * @param pageSize
     * @param map
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "pageNum") Integer pageNum,
                             @RequestParam(value = "pageSize") Integer pageSize,
                             Map <String, Object> map) {
        PageRequest pageRequest = new PageRequest(pageNum - 1, pageSize);
        Page <OrderDTO> orderDTOS = orderService.findAll(pageRequest);
        map.put("orderDTOS", orderDTOS);
        map.put("currentPage", pageNum);
        map.put("pageSize", pageSize);
        return new ModelAndView("order/list", map);
    }


    /**
     * 完结订单
     * @param orderId
     * @param map
     * @return
     */
    @GetMapping("/finish")
    public ModelAndView finishOrder(@RequestParam(value = "orderId") String orderId,
                                   Map <String, Object> map) {
        try {
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderService.finsh(orderDTO);
        } catch (SellException e) {
            log.error("【卖家端完结订单】发生异常{}", e);
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error", map);
        }
        map.put("msg", ResultEnum.ORDER_FINISH_SUCCESS.getMessage());
        map.put("url", "/sell/seller/order/list");
        return new ModelAndView("common/success");
    }


    /**
     * 取消订单
     * @param orderId
     * @param map
     * @return
     */
    @GetMapping("/cancel")
    public ModelAndView cancel(@RequestParam("orderId") String orderId,
                               Map <String, Object> map) {
        try {
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderService.cancel(orderDTO);
        } catch (SellException e) {
            log.error("【卖家端取消订单】发生异常{}", e);
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error", map);
        }
        map.put("msg", ResultEnum.ORDER_CANCEL_SUCCESS.getMessage());
        map.put("url", "/sell/seller/order/list");
        return new ModelAndView("common/success");
    }
}
