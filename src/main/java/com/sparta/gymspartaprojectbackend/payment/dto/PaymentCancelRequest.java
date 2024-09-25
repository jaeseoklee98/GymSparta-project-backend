package com.sparta.gymspartaprojectbackend.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCancelRequest {

  private Long paymentId;
  private String reason;

  // 기본 생성자
  public PaymentCancelRequest() {}

  // 생성자
  public PaymentCancelRequest(Long paymentId, String reason) {
    this.paymentId = paymentId;
    this.reason = reason;
  }
}