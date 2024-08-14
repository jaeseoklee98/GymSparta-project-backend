package com.sparta.gymspartaprojectbackend.payment.controller;

import com.sparta.gymspartaprojectbackend.payment.service.KakaoPayService;
import java.util.HashMap;
import java.util.Map;
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
  public ResponseEntity<Map<String, String>> readyToKakaoPay() {
    String redirectUrl = kakaoPayService.kakaoPayReady();
    Map<String, String> response = new HashMap<>();
    response.put("next_redirect_pc_url", redirectUrl);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/approve")
  public ResponseEntity<String> approveKakaoPay(@RequestParam("pg_token") String pgToken) {
    String response = kakaoPayService.kakaoPayApprove(pgToken);
    return ResponseEntity.ok(response);
  }
}