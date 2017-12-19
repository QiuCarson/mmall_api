package com.carson.mmall.service.impl;

import com.carson.mmall.VO.ProductListVO;
import com.carson.mmall.VO.ProductVO;
import com.carson.mmall.converter.Product2ProductListVO;
import com.carson.mmall.dataobject.Product;
import com.carson.mmall.repository.ProductRepository;
import com.carson.mmall.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductVO list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        //分页从0开始
        Integer currentPage = pageNum - 1;

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        String[] orderby = orderBy.split("_");
        String orderByField = "";

        if (orderby.length == 2) {
            //判断字段
            if (orderby[0].equals("price")) {
                orderByField = "price";
            }
            if (!orderByField.isEmpty()) {
                log.info("orderby={}",orderby[1].toLowerCase());
                if (orderby[1].toLowerCase().equals("asc")) {
                    sort = new Sort(Sort.Direction.ASC, orderByField);
                } else {
                    sort = new Sort(Sort.Direction.DESC, orderByField);
                }
            }
        }
        Pageable pageable = new PageRequest(currentPage, pageSize, sort);

        Page<Product> productPage = new PageImpl(new ArrayList<Product>());
        if (categoryId < 1 && !keyword.isEmpty()) {
            productPage = productRepository.findByCategoryIdAndNameLike(categoryId, keyword, pageable);
        } else if (categoryId > 0 && (keyword == null || keyword.isEmpty())) {
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            productPage = productRepository.findByNameLike(keyword, pageable);
        }
        ProductVO productVO = new ProductVO();
        Integer totalPage = productPage.getTotalPages();

        List<ProductListVO> productListVOList = Product2ProductListVO.listConvert(productPage.getContent());
        productVO.setList(productListVOList);
        productVO.setPageNum(pageNum);
        productVO.setPageSize(pageSize);
        productVO.setSize(totalPage);
        productVO.setOrderBy(orderBy);

        return productVO;
    }
}
