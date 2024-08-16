package com.sparta.gymspartaprojectbackend.payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.gymspartaprojectbackend.payment.enums.PaymentType;
import com.sparta.gymspartaprojectbackend.payment.enums.ProductType;
import com.sparta.gymspartaprojectbackend.payment.enums.PtTimes;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PaymentRequest {

  private final Long trainerId;

  @NotNull(message = "유저 ID는 필수 항목입니다.")
  private final String userId;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private final PtTimes ptTimes;

  @NotNull(message = "결제 수단은 필수 항목입니다.")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private final PaymentType paymentType;

  @NotNull(message = "결제 금액은 필수 항목입니다.")
  private final double amount;

  @NotNull(message = "상품 타입은 필수 항목입니다.")
  private ProductType productType;


  private final boolean isMembership;

  public PaymentRequest(Long trainerId, String userId, PtTimes ptTimes, ProductType productType, PaymentType paymentType, double amount, boolean isMembership) {
    this.trainerId = trainerId;
    this.userId = userId;
    this.ptTimes = ptTimes;
    this.paymentType = paymentType;
    this.productType = productType;
    this.amount = amount;
    this.isMembership = isMembership;
  }
}