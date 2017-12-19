package com.carson.mmall.converter;

import com.carson.mmall.VO.ProductListVO;
import com.carson.mmall.dataobject.Product;

import java.util.List;
import java.util.stream.Collectors;

public class Product2ProductListVO {
    public static ProductListVO convert(Product product){
        ProductListVO productListVO=new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setName(product.getName());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        return productListVO;
    }

    public static List<ProductListVO> listConvert(List<Product> productList){
        return productList.stream().map(e->convert(e)).collect(Collectors.toList());
    }
}
