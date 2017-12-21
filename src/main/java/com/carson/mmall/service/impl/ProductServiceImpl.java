package com.carson.mmall.service.impl;

import com.carson.mmall.VO.OrderItemVO;
import com.carson.mmall.VO.ProductListVO;
import com.carson.mmall.VO.ProductVO;
import com.carson.mmall.converter.Product2ProductListVO;
import com.carson.mmall.dataobject.Cart;
import com.carson.mmall.dataobject.Product;
import com.carson.mmall.enums.ProductStatusEnum;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.repository.ProductRepository;
import com.carson.mmall.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                log.info("orderby={}", orderby[1].toLowerCase());
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
            productPage = productRepository.findByCategoryIdAndNameLikeAndStatus(categoryId, keyword, ProductStatusEnum.IN.getCode(), pageable);
        } else if (categoryId > 0 && (keyword == null || keyword.isEmpty())) {
            productPage = productRepository.findByCategoryIdAndStatus(categoryId, ProductStatusEnum.IN.getCode(), pageable);
        } else {
            productPage = productRepository.findByNameLikeAndStatus(keyword, ProductStatusEnum.IN.getCode(), pageable);
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

    @Override
    public Product detail(Integer productId) {
        Product product = productRepository.findOne(productId);
        if (product == null) {
            throw new MmallException(ResultEnum.PRODUCT_NOT_EXISTS);
        }

        return product;
    }

    @Override
    @Transactional
    public void decreaseStock(List<Cart> cartList) {
        for (Cart cart : cartList) {
            Product product = productRepository.findOne(cart.getProductId());
            if (product == null) {
                throw new MmallException(ResultEnum.PRODUCT_NOT_EXISTS);
            }
            if (cart.getQuantity() > product.getStock()) {
                throw new MmallException(ResultEnum.PRODUCT_NOT_STOCK);
            }
            Integer stock = product.getStock() - cart.getQuantity();
            product.setStock(stock);
            productRepository.save(product);
        }
    }
}
