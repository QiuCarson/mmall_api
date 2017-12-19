package com.carson.mmall.service;

import com.carson.mmall.VO.ProductVO;

public interface ProductService {
    ProductVO list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
}
