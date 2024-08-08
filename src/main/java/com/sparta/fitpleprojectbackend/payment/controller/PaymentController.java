package com.sparta.fitpleprojectbackend.payment.controller;

import com.sparta.fitpleprojectbackend.exception.CustomException;
import com.sparta.fitpleprojectbackend.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

  private final PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  /**
   * 환불 요청을 처리합니다.
   *
   * @param paymentId 환불할 결제의 ID
   * @return 환불 처리 결과 메시지
   */
  @PostMapping("/refund")
  public ResponseEntity<String> processRefund(@RequestParam Long paymentId) {
    try {
      paymentService.processRefund(paymentId);
      return ResponseEntity.ok("Refund processed successfully");
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(e.getErrorType().getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while processing the refund");
    }
  }
}