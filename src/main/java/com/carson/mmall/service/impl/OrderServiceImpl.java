package com.carson.mmall.service.impl;

import com.carson.mmall.VO.*;
import com.carson.mmall.config.CustomConfig;
import com.carson.mmall.converter.Order2OrderVO;
import com.carson.mmall.converter.OrderItem2OrderItemVO;
import com.carson.mmall.dataobject.*;
import com.carson.mmall.enums.*;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.repository.*;
import com.carson.mmall.service.CartService;
import com.carson.mmall.service.OrderService;
import com.carson.mmall.service.ProductService;
import com.carson.mmall.utils.EnumHelperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

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

        //订单的总价
        BigDecimal totalPrice = new BigDecimal(0);


        //查询所有购物车商品 产品表详细信息
        List<Product> productList = productRepository.findByIdIn(productIdList);
        for (Product product : productList) {
            for (Cart cart : cartList) {
                if (product.getId().equals(cart.getProductId())) {
                    //检查商品状态
                    if (!product.getStatus().equals(ProductStatusEnum.IN.getCode())) {
                        throw new MmallException(ResultEnum.PRODUCT_NOT_IN);
                    }
                    //检查商品库存状态
                    if (product.getStock() < cart.getQuantity()) {
                        throw new MmallException(ResultEnum.PRODUCT_NOT_STOCK);
                    }

                    //创建订单子项
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
                    OrderItem orderItemInfo = orderItemRepository.save(orderItem);

                    OrderItemVO orderItemVO = new OrderItemVO();
                    BeanUtils.copyProperties(orderItemInfo, orderItemVO);
                    orderItemVOList.add(orderItemVO);
                    //全部商品总价
                    totalPrice = totalPrice.add(oneTotalPrice);
                }
            }
        }
        //创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setPaymentType(1);
        order.setPostage(0);
        order.setPayment(totalPrice);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());

        Order orderCreate = orderRepository.save(order);
        OrderVO orderVO = new Order2OrderVO().convert(orderCreate);
        orderVO.setOrderItemVoList(orderItemVOList);

        //减库存
        productService.decreaseStock(cartList);

        //清空购物车购买的商品
        List<Integer> cartProductIdList = cartList.stream().map(e -> e.getProductId()).collect(Collectors.toList());
        cartService.deleteList(userId, cartProductIdList);

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
        OrderCartProductVO orderCartProductVO = new OrderCartProductVO();
        orderCartProductVO.setImageHost(customConfig.getImageHost());
        orderCartProductVO.setOrderItemVOList(orderItemVOList);
        orderCartProductVO.setProductTotalPrice(totalPrice);
        return orderCartProductVO;
    }

    @Override
    @Transactional
    public OrderPageVO list(Integer userId, Integer pageSize, Integer pageNum) {

        //分页从0开始
        Integer currentPage = pageNum - 1;

        Sort sort = new Sort(Sort.Direction.DESC, "id");

        Pageable pageable = new PageRequest(currentPage, pageSize, sort);

        Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);

        List<OrderPageListVO> orderPageListVOList = new ArrayList<OrderPageListVO>();
        for (Order order : orderPage) {
            OrderPageListVO orderPageListVO = new OrderPageListVO();
            BeanUtils.copyProperties(order, orderPageListVO);
            orderPageListVO.setImageHost(customConfig.getImageHost());
            //查询PaymentTypeEnum枚举对象
            PaymentTypeEnum paymentTypeEnum = EnumHelperUtil.getByIntegerTypeCode(PaymentTypeEnum.class, "getCode", order.getPaymentType());
            orderPageListVO.setPaymentTypeDesc(paymentTypeEnum.getMessage());

            //查询OrderStatusEnum枚举对象
            OrderStatusEnum orderStatusEnum = EnumHelperUtil.getByIntegerTypeCode(OrderStatusEnum.class, "getCode", order.getStatus());
            orderPageListVO.setStatusDesc(orderStatusEnum.getMessage());

            //查询收件人名字
            Shipping shipping = shippingRepository.findTopByIdAndUserId(order.getShippingId(), userId);
            orderPageListVO.setReceiverName(shipping.getReceiverName());


            //查询订单商品
            List<OrderItem> orderItemList = orderItemRepository.findByUserIdAndOrderNo(userId, order.getOrderNo());
            List<OrderItemVO> orderItemVOList = OrderItem2OrderItemVO.listConvert(orderItemList);

            orderPageListVO.setOrderItemVoList(orderItemVOList);
            orderPageListVOList.add(orderPageListVO);

        }
        OrderPageVO orderPageVO = new OrderPageVO();
        orderPageVO.setList(orderPageListVOList);

        return orderPageVO;
    }

    @Override
    public OrderPageListVO detail(Integer userId, long orderNo) {
        Order order = orderRepository.findTopByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            throw new MmallException(ResultEnum.ORDER_NOT_EXISTS);
        }
        List<OrderItem> orderItemList = orderItemRepository.findByUserIdAndOrderNo(userId, orderNo);
        List<OrderItemVO> orderItemVOList = OrderItem2OrderItemVO.listConvert(orderItemList);

        //查询收件人名字
        Shipping shipping = shippingRepository.findTopByIdAndUserId(order.getShippingId(), userId);

        OrderPageListVO orderPageListVO=new OrderPageListVO();
        BeanUtils.copyProperties(order,orderPageListVO);
        orderPageListVO.setImageHost(customConfig.getImageHost());
        orderPageListVO.setReceiverName(shipping.getReceiverName());

        return orderPageListVO;
    }

    /**
     * 生成订单号
     *
     * @return
     */
    private long generateOrderNo() {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }
}
