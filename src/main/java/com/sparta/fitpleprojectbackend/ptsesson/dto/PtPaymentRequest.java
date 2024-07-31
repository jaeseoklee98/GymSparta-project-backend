package com.sparta.fitpleprojectbackend.ptsesson.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PaymentType;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PtTimes;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PtPaymentRequest {

  @NotNull(message = "트레이너 ID는 필수 항목입니다.")
  private Long trainerId;

  @NotNull(message = "PT 횟수는 필수 항목입니다.")
  private Long userId;

  @NotNull(message = "결제 타입은 필수 항목입니다.")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private PtTimes ptTimes;

  @NotNull(message = "결제 금액은 필수 항목입니다.")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private PaymentType paymentType;

  private boolean isMembership;

  public PtPaymentRequest() {
  }

  public PtPaymentRequest(Long trainerId, Long userId, PtTimes ptTimes, PaymentType paymentType,
      boolean isMembership) {
    this.trainerId = trainerId;
    this.userId = userId;
    this.ptTimes = ptTimes;
    this.paymentType = paymentType;
    this.isMembership = isMembership;
  }
}
