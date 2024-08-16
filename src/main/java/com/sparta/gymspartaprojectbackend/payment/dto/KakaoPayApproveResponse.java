package com.sparta.gymspartaprojectbackend.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoPayApproveResponse {
  private String aid;
  private String tid;
  private String cid;
  private String sid;
  private String partner_order_id;
  private String partner_user_id;
  private String payment_method_type;
}