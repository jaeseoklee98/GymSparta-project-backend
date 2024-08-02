package com.sparta.fitpleprojectbackend.ptsesson.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PaymentType;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PtTimes;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PtPaymentRequest {

  @NotNull(message = "트레이너 ID는 필수 항목입니다.")
  private  final Long trainerId;

  @NotNull(message = "유저 ID는 횟수는 필수 항목입니다.")
  private final Long userId;

  @NotNull(message = "피티 횟수는 필수 항목입니다.")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private final PtTimes ptTimes;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private final PaymentType paymentType;

  @NotNull(message = "결제 금액은 필수 항목입니다.")
  private final double amount;

  private final boolean isMembership;

  public PtPaymentRequest(Long trainerId, Long userId, PtTimes ptTimes, PaymentType paymentType, double amount,
      boolean isMembership) {
    this.trainerId = trainerId;
    this.userId = userId;
    this.ptTimes = ptTimes;
    this.paymentType = paymentType;
    this.amount = amount;
    this.isMembership = isMembership;
  }
}
