package com.carson.mmall.repository;

import com.carson.mmall.dataobject.Shipping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRepository extends JpaRepository<Shipping,Integer> {
    Shipping findTopByIdAndUserId(Integer id,Integer userId);


    Page<Shipping> findByUserId(Integer userId,Pageable pageable);
}
