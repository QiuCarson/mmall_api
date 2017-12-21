package com.carson.mmall.repository;

import com.carson.mmall.dataobject.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByUserIdAndOrderNo(Integer userId, Long orderNo);
}
