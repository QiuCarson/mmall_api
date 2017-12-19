package com.carson.mmall.service.impl;

import com.carson.mmall.VO.CartProductVO;
import com.carson.mmall.VO.CartVO;
import com.carson.mmall.common.Const;
import com.carson.mmall.converter.Cart2CartProductVO;
import com.carson.mmall.dataobject.Cart;
import com.carson.mmall.dataobject.Product;
import com.carson.mmall.enums.CartCheckedEnum;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.repository.CartRepository;
import com.carson.mmall.repository.ProductRepository;
import com.carson.mmall.service.CartService;
import com.carson.mmall.utils.BigDecimalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public CartVO cartList(Integer userId) {
        List<Cart> cartList = cartRepository.findByUserId(userId);

        List<Integer> productIdList = cartList.stream().map(e -> e.getProductId()).collect(Collectors.toList());

        //对象转换
        List<CartProductVO> cartProductVOList = Cart2CartProductVO.listConvert(cartList);

        //查询所有购物车商品 产品表详细信息
        List<Product> productList = productRepository.findByIdIn(productIdList);

        //商品总价
        BigDecimal prceTotal = new BigDecimal(0);
        Boolean allChecked = true;
        for (CartProductVO cartProductVO : cartProductVOList) {
            for (Product product : productList) {
                if (product.getId().equals(cartProductVO.getProductId())) {
                    //计算单个商品总价
                    BigDecimal productPriceTotal = BigDecimalUtil.priceFormat(product.getPrice().multiply(new BigDecimal(cartProductVO.getQuantity())));
                    //如果勾选，就计算到商品总价
                    if (cartProductVO.getProductChecked() == CartCheckedEnum.YES.getCode()) {
                        //计算商品总价
                        prceTotal = prceTotal.add(productPriceTotal);
                    } else {
                        //是否全部选中
                        if (allChecked) {
                            allChecked = false;
                        }
                    }


                    String limitQuantity = Const.LIMIT_NUM_SUCCESS;
                    //判断购物车数量是否超过库存
                    if (cartProductVO.getQuantity() > product.getStock()) {
                        limitQuantity = Const.LIMIT_NUM_FAIL;
                        //更新购物车个购买数量为0
                        Cart cart = cartRepository.findOne(cartProductVO.getId());
                        cart.setQuantity(0);
                        cartRepository.save(cart);
                    }
                    cartProductVO.setUserId(userId);
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductSubtitle(product.getSubtitle());
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductTotalPrice(productPriceTotal);
                    cartProductVO.setProductStock(product.getStock());
                    cartProductVO.setLimitQuantity(limitQuantity);
                }
            }
        }
        CartVO cartVO = new CartVO();
        cartVO.setCartProductVoList(cartProductVOList);
        cartVO.setCartTotalPrice(prceTotal);
        cartVO.setAllChecked(allChecked);

        return cartVO;
    }

    @Override
    @Transactional
    public CartVO add(Integer userId, Integer productId, Integer quantity) {

        Product product = productRepository.findOne(productId);
        if (product == null) {
            throw new MmallException(ResultEnum.PRODUCT_NOT_EXISTS);
        }
        if (product.getStock() < quantity) {
            quantity = 0;
        }
        Cart cart = cartRepository.findTopByUserIdAndProductId(userId, productId);
        cart.setQuantity(quantity);
        cart.setChecked(CartCheckedEnum.YES.getCode());
        cart.setProductId(productId);
        cart.setUserId(userId);
        cartRepository.save(cart);
        return cartList(userId);
    }

    @Override
    @Transactional
    public CartVO update(Integer userId, Integer productId, Integer quantity) {
        Cart cart = cartRepository.findTopByUserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new MmallException(ResultEnum.PRODUCT_NOT_EXISTS);
        }
        cart.setQuantity(quantity);
        cartRepository.save(cart);
        return cartList(userId);
    }

    @Override
    @Transactional
    public CartVO delete(Integer userId, String productIds) {
        String[] productIdArray = productIds.split(",");
        for (String productId : productIdArray) {
            Cart cart = cartRepository.findTopByUserIdAndProductId(userId, new Integer(Integer.parseInt(productId)));
            cartRepository.delete(cart);
        }
        return cartList(userId);
    }

    @Override
    @Transactional
    public CartVO select(Integer userId, Integer productId) {
        Cart cart = cartRepository.findTopByUserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new MmallException(ResultEnum.PRODUCT_NOT_EXISTS);
        }
        cart.setChecked(CartCheckedEnum.YES.getCode());
        cartRepository.save(cart);
        return cartList(userId);
    }

    @Override
    @Transactional
    public CartVO unselect(Integer userId, Integer productId) {
        Cart cart = cartRepository.findTopByUserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new MmallException(ResultEnum.PRODUCT_NOT_EXISTS);
        }
        cart.setChecked(CartCheckedEnum.NO.getCode());
        cartRepository.save(cart);
        return cartList(userId);
    }

    @Override
    @Transactional
    public Integer count(Integer userId) {
        List<Cart> cartList =cartRepository.findByUserId(userId);
        Integer count=0;
        for (Cart cart:cartList){
            count+=cart.getQuantity();
        }
        return count;
    }

    @Override
    @Transactional
    public CartVO selectAll(Integer userId) {
        List<Cart> cartList =cartRepository.findByUserId(userId);
        for(Cart cart:cartList){
            cart.setChecked(CartCheckedEnum.YES.getCode());
            cartRepository.save(cart);
        }
        return cartList(userId);
    }

    @Override
    @Transactional
    public CartVO unSelectAll(Integer userId) {
        List<Cart> cartList =cartRepository.findByUserId(userId);
        for(Cart cart:cartList){
            cart.setChecked(CartCheckedEnum.NO.getCode());
            cartRepository.save(cart);
        }
        return cartList(userId);
    }
}
