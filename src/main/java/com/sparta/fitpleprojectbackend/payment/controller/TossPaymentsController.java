package com.sparta.fitpleprojectbackend.payment.controller;

import com.sparta.fitpleprojectbackend.payment.service.TossPaymentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/toss")
public class TossPaymentsController {

  private final TossPaymentsService tossPaymentsService;

  public TossPaymentsController(TossPaymentsService tossPaymentsService) {
    this.tossPaymentsService = tossPaymentsService;
  }

  @PostMapping("/process")
  public ResponseEntity<String> processPayment(@RequestParam String orderId, @RequestParam int amount) {
    String paymentResult = tossPaymentsService.requestPayment(orderId, amount);
    return ResponseEntity.ok(paymentResult);
  }
}