package com.sparta.gymspartaprojectbackend.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoPayReadyResponse {
  private String tid;
  private String nextRedirectPcUrl;
  private String nextRedirectMobileUrl;
}