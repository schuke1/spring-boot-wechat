package com.schuke.springboot.wechat.service.impl;

import com.schuke.springboot.wechat.converter.OrderMaster2OrderDTOConverter;
import com.schuke.springboot.wechat.dto.CartDTO;
import com.schuke.springboot.wechat.dto.OrderDTO;
import com.schuke.springboot.wechat.enums.OrderStatusEnum;
import com.schuke.springboot.wechat.enums.PayStatusEnum;
import com.schuke.springboot.wechat.enums.ResultEnum;
import com.schuke.springboot.wechat.exception.SellException;
import com.schuke.springboot.wechat.pojo.Order;
import com.schuke.springboot.wechat.pojo.OrderDetail;
import com.schuke.springboot.wechat.pojo.ProductInfo;
import com.schuke.springboot.wechat.repository.OrderDetailRepository;
import com.schuke.springboot.wechat.repository.OrderRepository;
import com.schuke.springboot.wechat.service.*;
import com.schuke.springboot.wechat.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author schuke
 * @date 2019/4/24 10:26
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductInfoService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PayService payService;

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private WebSocket webSocket;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

        //1. 查询商品（数量, 价格）
        for (OrderDetail orderDetail: orderDTO.getOrderDetailList()) {
            ProductInfo productInfo =  productService.findOne(orderDetail.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            //2. 计算订单总价
            orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity()).add(orderAmount));

            //订单详情入库
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo, orderDetail);

            orderDetailRepository.save(orderDetail);
        }

        //3. 写入订单数据库
        Order order = new Order();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, order);
        order.setOrderAmount(orderAmount);
        order.setOrderStatus(OrderStatusEnum.NEW.getCode());
        order.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        orderRepository.save(order);


        //扣除库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);

        //发送websocket消息
        webSocket.sendMessage(orderDTO.getOrderId());

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        if (orderId != null) {
			 Order order = orderRepository.getOne(orderId);
			if (order == null) {
				throw new SellException(ResultEnum.ORDER_NOT_EXIST);
			}

			List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
			if (CollectionUtils.isEmpty(orderDetailList)) {
				throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
			}

			OrderDTO orderDTO = new OrderDTO();
			BeanUtils.copyProperties(order, orderDTO);
			orderDTO.setOrderDetailList(orderDetailList);

			return orderDTO;
        }
        return null;
    }

    @Override
    public Page<OrderDTO> findAll(Pageable pageable) {
        Page <Order> orders = orderRepository.findAll(pageable);
        List <OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orders.getContent());

        return new PageImpl <>(orderDTOList, pageable, orders.getTotalElements());
    }

    @Override
    public Page <OrderDTO> findUserList(String buyerOpenid, Pageable pageable) {

        Page <Order> orders = orderRepository.findByBuyerOpenid(buyerOpenid, pageable);

        List <OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orders.getContent());

        return new PageImpl <>(orderDTOList, pageable, orders.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        Order order = new Order();

        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.CANCEL)) {
            log.error("【取消订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, order);
        Order updateResult = orderRepository.save(order);

        if (updateResult == null) {
            log.error("【取消订单】更新失败, order={}", order);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        //返回库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单】订单中无商品详情, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }

        List <CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());

        productService.increaseStock(cartDTOList);

        //如果已支付, 需要退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            payService.refund(orderDTO);
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finsh(OrderDTO orderDTO) {
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【完结订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        Order updateResult = orderRepository.save(order);
        if (updateResult == null) {
            log.error("【完结订单】更新失败, order={}", order);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }


        //推送微信消息
        pushMessageService.orderStatus(orderDTO);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {

        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【订单支付完成】订单支付状态不正确, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        //修改订单状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);

        Order updateResult = orderRepository.save(order);
        if (updateResult == null) {
            log.error("【订单支付完成】更新失败, order={}", order);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }
}
