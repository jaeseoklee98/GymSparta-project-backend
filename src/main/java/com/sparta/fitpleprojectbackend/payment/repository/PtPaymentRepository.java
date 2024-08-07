package com.sparta.fitpleprojectbackend.payment.repository;

import com.sparta.fitpleprojectbackend.payment.entity.PtPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PtPaymentRepository extends JpaRepository<PtPayment, Long> {

}
