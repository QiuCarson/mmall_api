package com.carson.mmall.repository;

import com.carson.mmall.dataobject.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
