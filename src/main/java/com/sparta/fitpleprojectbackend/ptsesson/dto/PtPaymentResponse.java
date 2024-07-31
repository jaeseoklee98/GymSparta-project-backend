package com.sparta.fitpleprojectbackend.ptsesson.dto;

import com.sparta.fitpleprojectbackend.ptsesson.enums.PaymentStatus;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PaymentType;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PtTimes;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PtPaymentResponse {

  private Long id;

  private Long trainerId;

  private Long userId;

  private PtTimes ptTimes;

  private PaymentType paymentType;

  private double amount;

  private PaymentStatus paymentStatus;

  private LocalDateTime paymentDate;

  private LocalDateTime expiryDate;

  private boolean isMembership;

  public PtPaymentResponse(Long id, Long trainerId, Long userId, PtTimes ptTimes,
      PaymentType paymentType, double amount, PaymentStatus paymentStatus,
      LocalDateTime paymentDate, LocalDateTime expiryDate, boolean isMembership) {
    this.id = id;
    this.trainerId = trainerId;
    this.userId = userId;
    this.ptTimes = ptTimes;
    this.paymentType = paymentType;
    this.amount = amount;
    this.paymentStatus = paymentStatus;
    this.paymentDate = paymentDate;
    this.expiryDate = expiryDate;
    this.isMembership = isMembership;
  }
}
