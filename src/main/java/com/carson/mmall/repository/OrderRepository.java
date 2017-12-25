package com.carson.mmall.repository;

import com.carson.mmall.dataobject.Order;
import com.carson.mmall.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Page<Order> findByUserId(Integer userId, Pageable pageable);

    Order findTopByUserIdAndOrderNo(Integer userId,Long orderNo);


    Page<Order> findByOrderNo(Long orderNo, Pageable pageable);

    Order findTopByOrderNo(Long orderNo);
}
