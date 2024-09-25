package com.sparta.gymspartaprojectbackend.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoPayRefundRequest {
  private int cancelAmount;
  private int cancelTaxFreeAmount;
}