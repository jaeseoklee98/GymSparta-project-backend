package com.sparta.gymspartaprojectbackend.payment.dto;

import com.sparta.gymspartaprojectbackend.payment.entity.Payment;
import com.sparta.gymspartaprojectbackend.payment.enums.PaymentStatus;
import com.sparta.gymspartaprojectbackend.payment.enums.PaymentType;
import com.sparta.gymspartaprojectbackend.payment.enums.PtTimes;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PaymentResponse {

  private final Long paymentId;
  private final Long trainerId;
  private final Long userId;
  private final Long productId;
  private final PtTimes ptTimes;
  private final PaymentType paymentType;
  private final double amount;
  private final PaymentStatus paymentStatus;
  private final LocalDateTime paymentDate;
  private final LocalDateTime expiryDate;
  private final boolean isMembership;

  public PaymentResponse(Long paymentId, Long trainerId, Long userId, Long productId, PtTimes ptTimes, PaymentType paymentType, double amount, PaymentStatus paymentStatus, LocalDateTime paymentDate, LocalDateTime expiryDate, boolean isMembership) {
    this.paymentId = paymentId;
    this.trainerId = trainerId;
    this.userId = userId;
    this.productId = productId;
    this.ptTimes = ptTimes;
    this.paymentType = paymentType;
    this.amount = amount;
    this.paymentStatus = paymentStatus;
    this.paymentDate = paymentDate;
    this.expiryDate = expiryDate;
    this.isMembership = isMembership;
  }

  public static PaymentResponse fromEntity(Payment payment) {
    return new PaymentResponse(
        payment.getPaymentId(),
        payment.getTrainer() != null ? payment.getTrainer().getId() : null,
        payment.getUser().getId(),
        payment.getProduct() != null ? payment.getProduct().getId() : null,
        payment.getPtTimes(),
        payment.getPaymentType(),
        payment.getAmount(),
        payment.getPaymentStatus(),
        payment.getPaymentDate(),
        payment.getExpiryDate(),
        payment.isMembership()
    );
  }
}