package com.sparta.gymspartaprojectbackend.payment.repository;

import com.sparta.gymspartaprojectbackend.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> , PaymentRepositoryQuery {
  List<Payment> findAllByUser_Id(Long userId);
  Optional<Payment> findByUserId(Long userId);
}