package com.carson.mmall.service.impl;

import com.carson.mmall.VO.ProductPageVO;
import com.carson.mmall.dataobject.Cart;
import com.carson.mmall.dataobject.Product;
import com.carson.mmall.enums.ProductStatusEnum;
import com.carson.mmall.enums.ResultEnum;
import com.carson.mmall.exception.MmallException;
import com.carson.mmall.repository.ProductRepository;
import com.carson.mmall.service.ProductService;
import com.carson.mmall.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductPageVO list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
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
        log.info("pageSize={}",pageSize);
        Pageable pageable = new PageRequest(currentPage, pageSize, sort);

        Page<Product> productPage = new PageImpl(new ArrayList<Product>());
        if (categoryId < 1 && !keyword.isEmpty()) {
            productPage = productRepository.findByCategoryIdAndNameLikeAndStatus(categoryId, keyword, ProductStatusEnum.IN.getCode(), pageable);
        } else if (categoryId > 0 && (keyword == null || keyword.isEmpty())) {
            productPage = productRepository.findByCategoryIdAndStatus(categoryId, ProductStatusEnum.IN.getCode(), pageable);
        } else {
            productPage = productRepository.findByNameLikeAndStatus(keyword, ProductStatusEnum.IN.getCode(), pageable);
        }


        List<Product> productList = productPage.getContent().stream().map(e -> delProductFile(e)).collect(Collectors.toList());


        ProductPageVO productPageVO = new ProductPageVO();

        productPageVO.setProductList(productList);

        productPageVO.setOrderBy(orderBy);
        //分页处理
        Integer totalPage = productPage.getTotalPages();
        //是否有上一页
        productPageVO.setHasPreviousPage(productPage.hasPrevious());
        //上一页
        Integer prePage=0;
        if(productPage.hasPrevious()){
            prePage=productPage.previousPageable().getPageNumber();
        }
        productPageVO.setPrePage(prePage+1);
        //是否有下一页
        productPageVO.setHasNextPage(productPage.hasNext());
        //下一页
        Integer nextPage=0;
        if(productPage.hasNext()) {
            nextPage = productPage.nextPageable().getPageNumber();
        }
        productPageVO.setNextPage(nextPage+1);
        //总的页码数
        productPageVO.setPages(productPage.getTotalPages());
        //当期页码
        productPageVO.setPageNum(productPage.getNumber()+1);

        return productPageVO;
    }

    /**
     * 删除商品库存
     *
     * @param product
     * @return
     */
    private Product delProductFile(Product product) {
        product.setStock(0);
        return product;
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
