package com.sparta.fitpleprojectbackend.payment.repository;

import com.sparta.fitpleprojectbackend.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> , PaymentRepositoryQuery {
  List<Payment> findAllByUser_Id(Long userId);
}