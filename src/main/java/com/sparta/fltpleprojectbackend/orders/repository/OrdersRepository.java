package com.sparta.fltpleprojectbackend.orders.repository;

import com.sparta.fltpleprojectbackend.orders.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
