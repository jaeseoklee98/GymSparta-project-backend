package com.sparta.gymspartaprojectbackend.payment.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

@Service
public class TossPaymentsService {

  @Value("${toss.payments.secret-key}")
  private String secretKey;

  private static final String TOSS_API_URL = "https://api.tosspayments.com/v1/payments";

  public String requestPayment(String orderId, int amount) {
    RestTemplate restTemplate = new RestTemplate();

    // 요청할 데이터 설정
    Map<String, Object> request = new HashMap<>();
    request.put("orderId", orderId);
    request.put("amount", amount);
    request.put("orderName", "Test Order");

    // Authorization 헤더 설정
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(secretKey, ""); // Basic 인증 방식으로 시크릿 키 전달
    headers.set("Content-Type", "application/json");

    // 요청 엔터티 구성
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

    // API 호출
    ResponseEntity<String> response = restTemplate.exchange(
        TOSS_API_URL,
        HttpMethod.POST,
        entity,
        String.class
    );

    if (response.getStatusCode() == HttpStatus.OK) {
      return response.getBody();
    } else {
      // Toss API에서 반환된 오류 메시지를 예외로 던지는 대신 로그에 기록하고 반환하는 구조로 변경
      String errorMessage = "결제 요청 실패: " + response.getStatusCode() + ", 이유: " + response.getBody();
      System.out.println("Toss API Error: " + errorMessage);
      return errorMessage;
    }
  }
}