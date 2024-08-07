package com.sparta.fitpleprojectbackend.payment.dto;

import com.sparta.fitpleprojectbackend.payment.enums.PaymentStatus;
import com.sparta.fitpleprojectbackend.payment.enums.PaymentType;
import com.sparta.fitpleprojectbackend.payment.enums.PtTimes;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PtPaymentResponse {

  private final Long id;

  private final Long trainerId;

  private final Long userId;

  private final PtTimes ptTimes;

  private final PaymentType paymentType;

  private final double amount;

  private final PaymentStatus paymentStatus;

  private final LocalDateTime paymentDate;

  private final LocalDateTime expiryDate;

  private final boolean isMembership;

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
