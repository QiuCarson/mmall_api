package com.carson.mmall.repository;

import com.carson.mmall.dataobject.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByIdIn(List<Integer> productIdList);

    Page<Product> findByCategoryIdAndNameLikeAndStatus(Integer categoryId, String keyword, Integer status, Pageable pageable);

    Page<Product> findByCategoryIdAndStatus(Integer categoryId, Integer status, Pageable pageable);

    Page<Product> findByNameLikeAndStatus(String keyword, Integer status, Pageable pageable);

}
