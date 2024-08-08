package com.sparta.fitpleprojectbackend.payment.repository;

import com.sparta.fitpleprojectbackend.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}