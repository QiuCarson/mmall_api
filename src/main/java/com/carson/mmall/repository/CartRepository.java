package com.carson.mmall.repository;

import com.carson.mmall.dataobject.Cart;
import com.carson.mmall.dataobject.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Integer> {

    List<Cart> findByUserId(Integer userId);

    Cart findTopByUserIdAndProductId(Integer userId,Integer productId);


}
