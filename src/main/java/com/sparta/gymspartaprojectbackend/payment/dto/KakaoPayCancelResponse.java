package com.sparta.gymspartaprojectbackend.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoPayCancelResponse {
  private String aid;
  private String tid;
  private String cid;
  private String status;
  private int canceled_amount;
  private int canceled_tax_free_amount;
}