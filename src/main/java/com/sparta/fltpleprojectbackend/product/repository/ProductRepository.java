package com.sparta.fltpleprojectbackend.product.repository;

import com.sparta.fltpleprojectbackend.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>{
}
