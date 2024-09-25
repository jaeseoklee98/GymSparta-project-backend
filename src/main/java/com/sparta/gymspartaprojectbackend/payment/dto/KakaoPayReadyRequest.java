package com.sparta.gymspartaprojectbackend.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoPayReadyRequest {
  private Long paymentId;
  private Long trainerId;
  private double amount;
  private String productType;
  private String paymentType;
}