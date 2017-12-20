package com.carson.mmall.service.impl;

import com.carson.mmall.VO.CartProductVO;
import com.carson.mmall.VO.OrderCartProductVO;
import com.carson.mmall.VO.OrderItemVO;
import com.carson.mmall.VO.OrderVO;
import com.carson.mmall.config.CustomConfig;
import com.carson.mmall.converter.Order2OrderVO;
import com.carson.mmall.dataobject.*;
import com.carson.mmall.enums.CartCheckedEnum;
import com.carson.mmall.enums.OrderStatusEnum;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.repository.*;
import com.carson.mmall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ShippingRepository shippingRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomConfig customConfig;

    @Override
    @Transactional
    public OrderVO create(Integer userId, Integer shippingId) {
        Shipping shipping = shippingRepository.findTopByIdAndUserId(shippingId, userId);
        if (shipping == null) {
            throw new MmallException(ResultEnum.SHIPPING_NOT_EXISTS);
        }
        List<Cart> cartList = cartRepository.findByUserIdAndChecked(userId, CartCheckedEnum.YES.getCode());
        if (cartList == null) {
            throw new MmallException(ResultEnum.CART_NOT_EXISTS);
        }

        //购物车商品ID
        List<Integer> productIdList = cartList.stream().map(e -> e.getProductId()).collect(Collectors.toList());


        //生成订单号
        Long orderNo = generateOrderNo();

        List<OrderItemVO> orderItemVOList = new ArrayList<>();

        BigDecimal totalPrice = new BigDecimal(0);
        //查询所有购物车商品 产品表详细信息
        List<Product> productList = productRepository.findByIdIn(productIdList);
        for (Product product : productList) {
            for (Cart cart : cartList) {
                if (product.getId().equals(cart.getProductId())) {

                    //TODO检查商品库存状态
                    //TODO检查商品状态
                    
                    OrderItem orderItem = new OrderItem();
                    orderItem.setUserId(userId);
                    orderItem.setOrderNo(orderNo);
                    orderItem.setProductId(product.getId());
                    orderItem.setProductName(product.getName());
                    orderItem.setProductImage(product.getMainImage());
                    orderItem.setCurrentUnitPrice(product.getPrice());
                    orderItem.setQuantity(cart.getQuantity());
                    //计算单个商品的总价
                    BigDecimal oneTotalPrice = product.getPrice().multiply(new BigDecimal(cart.getQuantity()));
                    orderItem.setTotalPrice(oneTotalPrice);
                    orderItemRepository.save(orderItem);

                    OrderItemVO orderItemVO=new OrderItemVO();
                    BeanUtils.copyProperties(orderItemVO, orderItem);
                    orderItemVOList.add(orderItemVO);
                    //全部商品总价
                    totalPrice = totalPrice.add(oneTotalPrice);
                }
            }
        }
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setPayment(totalPrice);
        order.setPaymentType(1);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
        Order orderCreate=orderRepository.save(order);
        OrderVO orderVO= new Order2OrderVO().convert(orderCreate);
        orderVO.setOrderItemVoList(orderItemVOList);
        return orderVO;
    }

    @Override
    @Transactional
    public OrderCartProductVO orderCartProduct(Integer userId) {
        List<Cart> cartList = cartRepository.findByUserIdAndChecked(userId, CartCheckedEnum.YES.getCode());
        if (cartList == null) {
            throw new MmallException(ResultEnum.CART_NOT_EXISTS);
        }

        List<Integer> productIdList = cartList.stream().map(e -> e.getProductId()).collect(Collectors.toList());

        //查询所有购物车商品 产品表详细信息
        List<Product> productList = productRepository.findByIdIn(productIdList);

        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);
        for (Cart cart : cartList) {
            for (Product product : productList) {
                if (product.getId().equals(cart.getProductId())) {
                    OrderItemVO orderItemVO = new OrderItemVO();
                    orderItemVO.setProductId(product.getId());
                    orderItemVO.setProductName(product.getName());
                    orderItemVO.setProductImage(product.getMainImage());
                    orderItemVO.setCurrentUnitPrice(product.getPrice());
                    orderItemVO.setQuantity(cart.getQuantity());
                    orderItemVO.setCreateTime(cart.getCreateTime());
                    //单个商品总价
                    BigDecimal oneTotalPrice = product.getPrice().multiply(new BigDecimal(cart.getQuantity()));
                    orderItemVO.setTotalPrice(oneTotalPrice);
                    //全部商品总价
                    totalPrice = totalPrice.add(oneTotalPrice);

                    orderItemVOList.add(orderItemVO);
                }
            }
        }
        log.info("orderItemVOList={}", orderItemVOList);
        OrderCartProductVO orderCartProductVO = new OrderCartProductVO();
        orderCartProductVO.setImageHost(customConfig.getImagehost());
        orderCartProductVO.setOrderItemVOList(orderItemVOList);
        orderCartProductVO.setProductTotalPrice(totalPrice);
        log.info("customConfig={}", customConfig.getImagehost());
        return orderCartProductVO;
    }

    private long generateOrderNo() {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }
}
