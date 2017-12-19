package com.carson.mmall.repository;

import com.carson.mmall.dataobject.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    List<Product> findByIdIn(List<Integer> productIdList);

    Page<Product> findByCategoryIdAndNameLike(Integer categoryId, String keyword, Pageable pageable);

    Page<Product> findByCategoryId(Integer categoryId,Pageable pageable);

    Page<Product> findByNameLike(String keyword,Pageable pageable);

}
