package com.sparta.fitpleprojectbackend.ptsesson.repository;

import com.sparta.fitpleprojectbackend.ptsesson.entity.PtPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PtPaymentRepository extends JpaRepository<PtPayment, Long> {

}
