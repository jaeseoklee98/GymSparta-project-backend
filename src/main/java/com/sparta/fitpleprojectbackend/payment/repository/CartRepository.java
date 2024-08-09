package com.sparta.fitpleprojectbackend.payment.repository;

import com.sparta.fitpleprojectbackend.payment.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
  Optional<Cart> findByUserUserId(Long userId);
}