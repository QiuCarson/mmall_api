package com.carson.mmall.service;

import com.carson.mmall.VO.OrderItemVO;
import com.carson.mmall.VO.ProductVO;
import com.carson.mmall.dataobject.Cart;
import com.carson.mmall.dataobject.Product;

import java.util.List;

public interface ProductService {
    ProductVO list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
    Product detail(Integer productId);

    void decreaseStock(List<Cart> cartList);
}
