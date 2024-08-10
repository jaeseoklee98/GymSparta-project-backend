package com.sparta.fitpleprojectbackend.payment.dto;

import com.sparta.fitpleprojectbackend.payment.enums.PaymentStatus;
import com.sparta.fitpleprojectbackend.payment.enums.PaymentType;
import lombok.Getter;

@Getter
public class PaymentUpdateRequest {

  private final PaymentStatus status;
  private final Double amount;
  private final PaymentType paymentType;

  public PaymentUpdateRequest(PaymentStatus status, Double amount, PaymentType paymentType) {
    this.status = status;
    this.amount = amount;
    this.paymentType = paymentType;
  }
}