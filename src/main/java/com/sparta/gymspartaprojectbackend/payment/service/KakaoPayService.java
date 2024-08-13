package com.sparta.gymspartaprojectbackend.payment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoPayService {

  private static final String HOST = "https://kapi.kakao.com";

  @Value("${kakao.pay.client-id}")
  private String clientId;

  @Value("${kakao.pay.client-secret}")
  private String clientSecret;

  @Value("${kakao.pay.secret-key}")
  private String secretKey;

  public String kakaoPayReady() {

    RestTemplate restTemplate = new RestTemplate();

    // 카카오페이 요청 헤더 설정
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "KakaoAK " + "PRD441A04CE52647D4E94FFA919FD67242007B40");
    headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

    // 카카오페이 요청 파라미터 설정
    Map<String, String> params = new HashMap<>();
    params.put("cid", "TC0ONETIME"); // 테스트용 CID
    params.put("partner_order_id", "1001"); // 주문 번호
    params.put("partner_user_id", "user1"); // 사용자 ID
    params.put("item_name", "item_name"); // 상품명
    params.put("quantity", "1"); // 상품 수량
    params.put("total_amount", "1000"); // 총 금액
    params.put("vat_amount", "200"); // 부가세
    params.put("tax_free_amount", "0"); // 비과세 금액
    params.put("approval_url", "https://yoursite.com/kakaoPaySuccess"); // 결제 성공 시 리다이렉트 URL
    params.put("cancel_url", "https://yoursite.com/kakaoPayCancel"); // 결제 취소 시 리다이렉트 URL
    params.put("fail_url", "https://yoursite.com/kakaoPayFail"); // 결제 실패 시 리다이렉트 URL

    HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

    // 카카오페이 API 호출
    ResponseEntity<String> response = restTemplate.exchange(
        HOST + "/v1/payment/ready",
        HttpMethod.POST,
        request,
        String.class
    );

    // 결제 준비 응답 데이터 반환
    return response.getBody();
  }

  public String kakaoPayApprove(String pgToken) {

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "KakaoAK " + "PRD441A04CE52647D4E94FFA919FD67242007B40");
    headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

    Map<String, String> params = new HashMap<>();
    params.put("cid", "TC0ONETIME"); // 테스트용 CID
    params.put("tid", "T1234567890123456789"); // 결제 고유 번호
    params.put("partner_order_id", "1001");
    params.put("partner_user_id", "user1");
    params.put("pg_token", pgToken); // 사용자 결제 승인 후 받은 pg_token

    HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

    ResponseEntity<String> response = restTemplate.exchange(
        HOST + "/v1/payment/approve",
        HttpMethod.POST,
        request,
        String.class
    );

    return response.getBody();
  }
}