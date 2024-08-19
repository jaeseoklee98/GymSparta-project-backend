package com.sparta.gymspartaprojectbackend.payment.service;

import com.sparta.gymspartaprojectbackend.payment.dto.KakaoPayApproveResponse;
import com.sparta.gymspartaprojectbackend.payment.dto.KakaoPayCancelResponse;
import com.sparta.gymspartaprojectbackend.payment.dto.KakaoPayReadyResponse;
import com.sparta.gymspartaprojectbackend.payment.dto.KakaoPayRefundRequest;
import com.sparta.gymspartaprojectbackend.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoPayService {

  private static final String HOST = "https://kapi.kakao.com";

  @Value("${kakao.pay.cid}")
  private String cid;

  @Value("${kakao.pay.secret-key}")
  private String secretKey;

  public KakaoPayReadyResponse kakaoPayReady(User user, Long paymentId, Long trainerId) {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "KakaoAK " + secretKey); // 카카오 API 키 설정
    headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

    // 결제 준비를 위한 요청 파라미터 설정
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("cid", cid);
    params.add("partner_order_id", String.valueOf(paymentId));  // paymentId 사용
    params.add("partner_user_id", String.valueOf(user.getId()));
    params.add("item_name", "상품명");  // 예: "Gym Membership"
    params.add("quantity", "1");
    params.add("total_amount", "1000");  // 예: 총 결제 금액
    params.add("tax_free_amount", "0");
    params.add("approval_url", "https://your-domain.com/payments/success");
    params.add("cancel_url", "https://your-domain.com/payments/cancel");
    params.add("fail_url", "https://your-domain.com/payments/fail");

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

    try {
      ResponseEntity<KakaoPayReadyResponse> response = restTemplate.postForEntity(
          HOST + "/v1/payment/ready",
          requestEntity,
          KakaoPayReadyResponse.class);

      return response.getBody();
    } catch (Exception e) {
      throw new RuntimeException("Failed to prepare KakaoPay payment", e);
    }
  }


  public KakaoPayApproveResponse kakaoPayApprove(String pgToken, Long userId, Long paymentId, String tid) {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "KakaoAK " + secretKey);
    headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("cid", cid);
    params.add("tid", tid);
    params.add("partner_order_id", String.valueOf(paymentId));
    params.add("partner_user_id", String.valueOf(userId));
    params.add("pg_token", pgToken);

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

    try {
      ResponseEntity<KakaoPayApproveResponse> response = restTemplate.postForEntity(
          HOST + "/v1/payment/approve",
          requestEntity,
          KakaoPayApproveResponse.class);

      return response.getBody();
    } catch (Exception e) {
      throw new RuntimeException("Failed to approve KakaoPay payment", e);
    }
  }

  public KakaoPayCancelResponse kakaoPayCancel(KakaoPayRefundRequest requestDto, String tid) {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "KakaoAK " + secretKey);
    headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

    // 결제 취소 요청 파라미터 설정
    Map<String, String> params = new HashMap<>();
    params.put("cid", cid);
    params.put("tid", tid);
    params.put("cancel_amount", String.valueOf(requestDto.getCancelAmount()));
    params.put("cancel_tax_free_amount", String.valueOf(requestDto.getCancelTaxFreeAmount()));

    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, headers);

    try {
      ResponseEntity<KakaoPayCancelResponse> response = restTemplate.postForEntity(
          HOST + "/v1/payment/cancel",
          requestEntity,
          KakaoPayCancelResponse.class);

      return response.getBody();
    } catch (Exception e) {
      throw new RuntimeException("Failed to cancel KakaoPay payment", e);
    }
  }
}