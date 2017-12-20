package com.carson.mmall.repository;

import com.carson.mmall.dataobject.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
