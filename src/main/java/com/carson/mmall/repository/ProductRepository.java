package com.carson.mmall.repository;

import com.carson.mmall.dataobject.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    List<Product> findByProductIdIn(List<Integer> productIdList);
}
