package com.carson.mmall.repository;

import com.carson.mmall.dataobject.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findTopByUserId(Integer userId);

    Page<Order> findByUserId(Integer userId, Pageable pageable);

    Order findTopByUserIdAndOrderNo(Integer userId,Long orderNo);
}
