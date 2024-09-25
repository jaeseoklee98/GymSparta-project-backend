package com.sparta.gymspartaprojectbackend.cart.repository;

import com.sparta.gymspartaprojectbackend.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
  Optional<Cart> findByUser_Id(Long userId);
}