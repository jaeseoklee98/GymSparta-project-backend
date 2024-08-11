package com.sparta.fitpleprojectbackend.payment.controller;

import com.sparta.fitpleprojectbackend.payment.service.KakaoPayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakaoPay")
public class KakaoPayController {

  private final KakaoPayService kakaoPayService;

  public KakaoPayController(KakaoPayService kakaoPayService) {
    this.kakaoPayService = kakaoPayService;
  }

  @PostMapping("/ready")
  public ResponseEntity<String> readyToKakaoPay() {
    String response = kakaoPayService.kakaoPayReady();
    return ResponseEntity.ok(response);
  }

  @PostMapping("/approve")
  public ResponseEntity<String> approveKakaoPay(@RequestParam("pg_token") String pgToken) {
    String response = kakaoPayService.kakaoPayApprove(pgToken);
    return ResponseEntity.ok(response);
  }
}