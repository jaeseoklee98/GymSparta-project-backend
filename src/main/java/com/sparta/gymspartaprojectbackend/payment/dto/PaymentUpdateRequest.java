package com.sparta.gymspartaprojectbackend.payment.dto;

import com.sparta.gymspartaprojectbackend.payment.enums.PaymentStatus;
import com.sparta.gymspartaprojectbackend.payment.enums.PaymentType;
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