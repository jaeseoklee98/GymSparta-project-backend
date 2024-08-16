package com.sparta.gymspartaprojectbackend.payment.controller;

import com.sparta.gymspartaprojectbackend.payment.dto.KakaoPayApproveResponse;
import com.sparta.gymspartaprojectbackend.payment.dto.KakaoPayReadyRequest;
import com.sparta.gymspartaprojectbackend.payment.dto.KakaoPayReadyResponse;
import com.sparta.gymspartaprojectbackend.payment.service.KakaoPayService;
import com.sparta.gymspartaprojectbackend.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/kakaoPay")
public class KakaoPayController {

  private final KakaoPayService kakaoPayService;

  public KakaoPayController(KakaoPayService kakaoPayService) {
    this.kakaoPayService = kakaoPayService;
  }

  @PostMapping("/ready")
  public ResponseEntity<Map<String, String>> readyToKakaoPay(
      @RequestBody KakaoPayReadyRequest request,  // JSON RequestBody로 수정
      @AuthenticationPrincipal User user) {

    KakaoPayReadyResponse response = kakaoPayService.kakaoPayReady(user, request.getPaymentId(), request.getTrainerId());
    Map<String, String> result = new HashMap<>();
    result.put("next_redirect_pc_url", response.getNextRedirectPcUrl());
    result.put("tid", response.getTid());  // TID 추가
    return ResponseEntity.ok(result);
  }

  @PostMapping("/approve")
  public ResponseEntity<KakaoPayApproveResponse> approveKakaoPay(@AuthenticationPrincipal User user,
      @RequestParam("pg_token") String pgToken,
      @RequestParam("paymentId") Long paymentId,
      @RequestParam("tid") String tid) {  // TID 매개변수 추가
    KakaoPayApproveResponse response = kakaoPayService.kakaoPayApprove(pgToken, user.getId(), paymentId, tid);
    return ResponseEntity.ok(response);
  }
}