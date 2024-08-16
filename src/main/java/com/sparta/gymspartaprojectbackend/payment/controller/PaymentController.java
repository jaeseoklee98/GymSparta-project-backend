package com.sparta.gymspartaprojectbackend.payment.controller;

import com.sparta.gymspartaprojectbackend.exception.CustomException;
import com.sparta.gymspartaprojectbackend.payment.dto.*;
import com.sparta.gymspartaprojectbackend.payment.entity.Payment;
import com.sparta.gymspartaprojectbackend.payment.enums.PaymentStatus;
import com.sparta.gymspartaprojectbackend.payment.enums.PaymentType;
import com.sparta.gymspartaprojectbackend.payment.service.PaymentService;
import com.sparta.gymspartaprojectbackend.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/payments")  // 새로운 엔드포인트
public class PaymentController {

  private final PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping("/process")
  public ResponseEntity<Long> processPayment(@RequestBody PaymentRequest request) {
    try {
      Payment payment = paymentService.savePayment(request);
      return ResponseEntity.ok(payment.getPaymentId()); // paymentId 반환
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null);
    }
  }

  @PostMapping("/cancel")
  public ResponseEntity<String> cancelPayment(@RequestBody PaymentCancelRequest request) {
    try {
      paymentService.cancelPayment(request);
      return ResponseEntity.ok("Payment cancelled successfully");
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(e.getErrorType().getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while cancelling the payment");
    }
  }

  @GetMapping("/{paymentId}")
  public ResponseEntity<Payment> getPaymentById(@PathVariable Long paymentId) {
    try {
      Payment payment = paymentService.getPaymentById(paymentId);
      return ResponseEntity.ok(payment);
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null);
    }
  }

  @GetMapping("/user")
  public ResponseEntity<List<Payment>> getPaymentsByUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    try {
      List<Payment> payments = paymentService.getPaymentsByUser(userDetails.getUser().getId());
      return ResponseEntity.ok(payments);
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null);
    }
  }
}