package com.carson.mmall.service.impl;

import com.carson.mmall.VO.*;
import com.carson.mmall.config.CustomConfig;
import com.carson.mmall.converter.Order2OrderDTOConvert;
import com.carson.mmall.dataobject.*;
import com.carson.mmall.dto.OrderDTO;
import com.carson.mmall.enums.*;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.repository.*;
import com.carson.mmall.service.CartService;
import com.carson.mmall.service.OrderService;
import com.carson.mmall.service.ProductService;
import com.carson.mmall.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
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
    public OrderDTO create(Integer userId, Integer shippingId) {
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

        List<OrderItem> orderItemList = new ArrayList<>();

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

                    orderItemList.add(orderItemInfo);
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
        order.setPostage(0);
        order.setPayment(totalPrice);

        Order orderCreate = orderRepository.save(order);
        OrderDTO orderDTO = Order2OrderDTOConvert.convert(orderCreate);
        orderDTO.setOrderItemList(orderItemList);

        //减库存
        productService.decreaseStock(cartList);

        //清空购物车购买的商品
        List<Integer> cartProductIdList = cartList.stream().map(e -> e.getProductId()).collect(Collectors.toList());
        cartService.deleteList(userId, cartProductIdList);

        return orderDTO;
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

        List<OrderItem> orderItemList = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);
        for (Cart cart : cartList) {
            for (Product product : productList) {
                if (product.getId().equals(cart.getProductId())) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(product.getId());
                    orderItem.setProductName(product.getName());
                    orderItem.setProductImage(product.getMainImage());
                    orderItem.setCurrentUnitPrice(product.getPrice());
                    orderItem.setQuantity(cart.getQuantity());
                    orderItem.setCreateTime(cart.getCreateTime());
                    //单个商品总价
                    BigDecimal oneTotalPrice = product.getPrice().multiply(new BigDecimal(cart.getQuantity()));
                    orderItem.setTotalPrice(oneTotalPrice);
                    //全部商品总价
                    totalPrice = totalPrice.add(oneTotalPrice);

                    orderItemList.add(orderItem);
                }
            }
        }
        OrderCartProductVO orderCartProductVO = new OrderCartProductVO();
        orderCartProductVO.setImageHost(customConfig.getImageHost());
        orderCartProductVO.setOrderItemList(orderItemList);
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
        List<OrderDTO> orderDTOList=Order2OrderDTOConvert.listConvert(orderPage.getContent());

        List<OrderDTO> orderDTOListNew = new ArrayList<OrderDTO>();

        for (OrderDTO orderDTO : orderDTOList) {
            //orderDTO数据添加
            orderDTO=getOrderDTOInfo(orderDTO);

            orderDTOListNew.add(orderDTO);

        }

        OrderPageVO orderPageVO = PageUtil.getPage(OrderPageVO.class,orderPage);
        orderPageVO.setList(orderDTOListNew);

        return orderPageVO;
    }

    @Override
    public OrderDTO detail(Integer userId, long orderNo) {
        Order order = orderRepository.findTopByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            throw new MmallException(ResultEnum.ORDER_NOT_EXISTS);
        }
        OrderDTO orderDTO=Order2OrderDTOConvert.convert(order);

        //orderDTO数据添加
        orderDTO=getOrderDTOInfo(orderDTO);

        return orderDTO;
    }

    @Override
    public OrderDTO cancel(Integer userId, long orderNo) {
        Order order = orderRepository.findTopByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            throw new MmallException(ResultEnum.ORDER_NOT_EXISTS);
        }
        order.setStatus(OrderStatusEnum.CACLE.getCode());
        orderRepository.save(order);
        OrderDTO orderDTO=Order2OrderDTOConvert.convert(order);
        return orderDTO;
    }

    /**
     * OrderDTO 数据填充
     * @param orderDTO
     * @return
     */
    public OrderDTO getOrderDTOInfo(OrderDTO orderDTO){
        orderDTO.setImageHost(customConfig.getImageHost());

        log.info("orderDTO={}",orderDTO);
        //查询PaymentTypeEnum枚举对象 支付状态
        orderDTO.setPaymentTypeDesc(orderDTO.getPaymentTypeEnum().getMessage());

        //查询OrderStatusEnum枚举对象 订单状态
        orderDTO.setStatusDesc(orderDTO.getOrderStatusEnum().getMessage());

        //查询收件人名字
        Shipping shipping = shippingRepository.findTopByIdAndUserId(orderDTO.getShippingId(), orderDTO.getUserId());
        orderDTO.setReceiverName(shipping.getReceiverName());
        orderDTO.setShipping(shipping);

        //查询订单商品
        List<OrderItem> orderItemList = orderItemRepository.findByUserIdAndOrderNo(orderDTO.getUserId(), orderDTO.getOrderNo());
        orderDTO.setOrderItemList(orderItemList);
        return orderDTO;
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
