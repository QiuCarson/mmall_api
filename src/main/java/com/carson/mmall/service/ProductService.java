package com.carson.mmall.service;

import com.carson.mmall.VO.ProductVO;
import com.carson.mmall.dataobject.Product;

public interface ProductService {
    ProductVO list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
    Product detail(Integer productId);
}
