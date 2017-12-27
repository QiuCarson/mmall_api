package com.carson.mmall.service.impl;


import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.carson.mmall.VO.OrderCartProductVO;
import com.carson.mmall.VO.OrderPageVO;
import com.carson.mmall.config.CustomConfig;
import com.carson.mmall.converter.Order2OrderDTOConvert;
import com.carson.mmall.dataobject.*;
import com.carson.mmall.dto.OrderDTO;
import com.carson.mmall.enums.CartCheckedEnum;
import com.carson.mmall.enums.OrderStatusEnum;
import com.carson.mmall.enums.ProductStatusEnum;
import com.carson.mmall.enums.ResultEnum;
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

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
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
    private ProductService productService;
    @Autowired
    private CustomConfig customConfig;

    @Autowired
    private CartService cartService;

    // 支付宝当面付2.0服务
    private static AlipayTradeService   tradeService;

    static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

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
        List<OrderDTO> orderDTOList = Order2OrderDTOConvert.listConvert(orderPage.getContent());

        List<OrderDTO> orderDTOListNew = new ArrayList<OrderDTO>();

        for (OrderDTO orderDTO : orderDTOList) {
            //orderDTO数据添加
            orderDTO = getOrderDTOInfo(orderDTO);

            orderDTOListNew.add(orderDTO);

        }

        OrderPageVO orderPageVO = PageUtil.getPage(OrderPageVO.class, orderPage);
        orderPageVO.setList(orderDTOListNew);

        return orderPageVO;
    }

    @Override
    public OrderDTO detail(Integer userId, long orderNo) {
        Order order = orderRepository.findTopByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            throw new MmallException(ResultEnum.ORDER_NOT_EXISTS);
        }
        OrderDTO orderDTO = Order2OrderDTOConvert.convert(order);

        //orderDTO数据添加
        orderDTO = getOrderDTOInfo(orderDTO);

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
        OrderDTO orderDTO = Order2OrderDTOConvert.convert(order);
        return orderDTO;
    }


    @Override
    public String pay(Integer userId, long orderNo) {
        //这里接入支付宝生成支付二维码
        Order order = orderRepository.findTopByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            throw new MmallException(ResultEnum.ORDER_NOT_EXISTS);
        }
        List<OrderItem> orderItemList=orderItemRepository.findByUserIdAndOrderNo(userId,orderNo);
        if(orderItemList==null){
            throw new MmallException(ResultEnum.ORDER_NOT_ITEM_EXISTS);
        }
        Integer totalItem=0;

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = String.valueOf(orderNo);

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = customConfig.getAlipaySubject();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";


        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        for(OrderItem orderItem: orderItemList){
            Long price=orderItem.getCurrentUnitPrice().multiply(new BigDecimal(1000)).longValue();
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getId().toString(),
                    orderItem.getProductName(), price, orderItem.getQuantity());
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods);

            totalItem+=orderItem.getQuantity();

        }
        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品"+String.valueOf(totalItem)+"件共"+order.getPayment().toString()+"元";



        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(customConfig.getAlipayNotifyUrl())//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);



        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();

                // 需要修改为运行机器上的路径
                String filePath = String.format(customConfig.getAlipayQrPath()+"/qr-%s.png",
                        response.getOutTradeNo());
                String filename=String.format("/qr-%s.png",
                        response.getOutTradeNo());
                log.info("filePath:" + filePath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                return customConfig.getImageHost()+filename;


            case FAILED:
                log.error("支付宝预下单失败!!!");
                throw new MmallException(ResultEnum.ORDER_ALIPAY_CREATE_ERROR);

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                throw new MmallException(ResultEnum.ORDER_ALIPAY_UNKNOWN_ERROR);

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                throw new MmallException(ResultEnum.ORDER_ALIPAY_STATUS_ERROR);

        }

    }

    @Override
    public String alipayCallback(HttpServletRequest request) {
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]: valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        try {
            //验证签名
            boolean flag = AlipaySignature.rsaCheckV1(params, Configs.getAlipayPublicKey(), "utf-8", "RSA2");
            if(flag){
                if("TRADE_SUCCESS".equals(params.get("trade_status"))){
                    log.info("支付宝异步回调成功 params={}",params.toString());
                    //付款金额
                    String amount = params.get("buyer_pay_amount");
                    //商户订单号
                    String out_trade_no = params.get("out_trade_no");
                    //支付宝交易号
                    String trade_no = params.get("trade_no");
                    Order order=orderRepository.findTopByOrderNo(Long.valueOf(out_trade_no));
                    if(order.getStatus()!= OrderStatusEnum.NO_PAY.getCode()){
                        log.error("支付宝异步回调 订单号已经修改 params={}",params.toString());
                        return "success";
                    }
                    order.setStatus(OrderStatusEnum.YES_PAY.getCode());
                    Date currentTime=new Date(System.currentTimeMillis());
                    order.setPaymentTime(currentTime);
                    orderRepository.save(order);
                    return "success";

                }
            }
        } catch (AlipayApiException e) {
            log.error("支付宝异步回调 验证回调异常 e={}",e.toString());
            return "fail";
        }
        return "fail";
    }

    @Override
    public Boolean queryOrderPayStatus(Integer userId, long orderNo) {
        Order order = orderRepository.findTopByUserIdAndOrderNo(userId, orderNo);
        Boolean orderStatus = false;
        if (order == null) {
            throw new MmallException(ResultEnum.ORDER_NOT_EXISTS);
        }
        if (order.getStatus() == OrderStatusEnum.YES_PAY.getCode()) {
            orderStatus = true;
        }
        return orderStatus;
    }

    /**
     * OrderDTO 数据填充
     *
     * @param orderDTO
     * @return
     */
    public OrderDTO getOrderDTOInfo(OrderDTO orderDTO) {
        orderDTO.setImageHost(customConfig.getImageHost());

        log.info("orderDTO={}", orderDTO);
        //查询PaymentTypeEnum枚举对象 支付状态
        orderDTO.setPaymentTypeDesc(orderDTO.getPaymentTypeEnum().getMessage());

        //查询OrderStatusEnum枚举对象 订单状态
        orderDTO.setStatusDesc(orderDTO.getOrderStatusEnum().getMessage());

        //查询收件人名字
        Shipping shipping = shippingRepository.findTopByIdAndUserId(orderDTO.getShippingId(), orderDTO.getUserId());
        if (shipping != null) {
            orderDTO.setReceiverName(shipping.getReceiverName());
            orderDTO.setShipping(shipping);
        }

        //查询订单商品
        List<OrderItem> orderItemList = orderItemRepository.findByUserIdAndOrderNo(orderDTO.getUserId(), orderDTO.getOrderNo());
        orderDTO.setOrderItemList(orderItemList);
        return orderDTO;
    }

    @Override
    public OrderPageVO adminList(Integer pageSize, Integer pageNum) {
        //分页从0开始
        Integer currentPage = pageNum - 1;

        Sort sort = new Sort(Sort.Direction.DESC, "id");

        Pageable pageable = new PageRequest(currentPage, pageSize, sort);

        Page<Order> orderPage = orderRepository.findAll(pageable);
        List<OrderDTO> orderDTOList = Order2OrderDTOConvert.listConvert(orderPage.getContent());

        List<OrderDTO> orderDTOListNew = new ArrayList<OrderDTO>();

        for (OrderDTO orderDTO : orderDTOList) {
            //orderDTO数据添加
            orderDTO = getOrderDTOInfo(orderDTO);

            orderDTOListNew.add(orderDTO);

        }

        OrderPageVO orderPageVO = PageUtil.getPage(OrderPageVO.class, orderPage);
        orderPageVO.setList(orderDTOListNew);
        return orderPageVO;
    }

    @Override
    public OrderPageVO adminSearch(Long orderNo, Integer pageSize, Integer pageNum) {
        //分页从0开始
        Integer currentPage = pageNum - 1;

        Sort sort = new Sort(Sort.Direction.DESC, "id");

        Pageable pageable = new PageRequest(currentPage, pageSize, sort);

        Page<Order> orderPage = orderRepository.findByOrderNo(orderNo, pageable);
        List<OrderDTO> orderDTOList = Order2OrderDTOConvert.listConvert(orderPage.getContent());

        List<OrderDTO> orderDTOListNew = new ArrayList<OrderDTO>();

        for (OrderDTO orderDTO : orderDTOList) {
            //orderDTO数据添加
            orderDTO = getOrderDTOInfo(orderDTO);

            orderDTOListNew.add(orderDTO);

        }

        OrderPageVO orderPageVO = PageUtil.getPage(OrderPageVO.class, orderPage);
        orderPageVO.setList(orderDTOListNew);
        return orderPageVO;
    }

    @Override
    public OrderDTO adminDetail(Long orderNo) {
        Order order = orderRepository.findTopByOrderNo(orderNo);
        if (order == null) {
            throw new MmallException(ResultEnum.ORDER_NOT_EXISTS);
        }
        OrderDTO orderDTO = Order2OrderDTOConvert.convert(order);

        //orderDTO数据添加
        orderDTO = getOrderDTOInfo(orderDTO);

        return orderDTO;
    }

    @Override
    public OrderDTO adminSendGoods(Long orderNo) {
        Order order = orderRepository.findTopByOrderNo(orderNo);
        if (order == null) {
            throw new MmallException(ResultEnum.ORDER_NOT_EXISTS);
        }
        order.setStatus(OrderStatusEnum.SEND.getCode());
        Date nowTime = new Date(System.currentTimeMillis());
        order.setSendTime(nowTime);
        orderRepository.save(order);
        OrderDTO orderDTO = Order2OrderDTOConvert.convert(order);
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
