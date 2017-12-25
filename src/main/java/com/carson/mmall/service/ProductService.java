package com.carson.mmall.service;

import com.carson.mmall.VO.ProductPageVO;
import com.carson.mmall.dataobject.Cart;
import com.carson.mmall.dataobject.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ProductPageVO list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
    Product detail(Integer productId);

    void decreaseStock(List<Cart> cartList);

    ProductPageVO adminList( Integer pageNum, Integer pageSize);

    ProductPageVO adminSearch( Integer pageNum, Integer pageSize,String productName,Integer productId);

    Map<String,String> upload(MultipartFile file);


}
