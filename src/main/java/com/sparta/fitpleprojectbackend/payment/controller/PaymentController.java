package com.sparta.fitpleprojectbackend.payment.controller;

import com.sparta.fitpleprojectbackend.exception.CustomException;
import com.sparta.fitpleprojectbackend.payment.dto.PaymentRequest;
import com.sparta.fitpleprojectbackend.payment.dto.PaymentUpdateRequest;
import com.sparta.fitpleprojectbackend.payment.dto.PtTotalAmountRequest;
import com.sparta.fitpleprojectbackend.payment.dto.PtTotalAmountResponse;
import com.sparta.fitpleprojectbackend.payment.entity.Payment;
import com.sparta.fitpleprojectbackend.payment.enums.PaymentType;
import com.sparta.fitpleprojectbackend.payment.enums.PtTimes;
import com.sparta.fitpleprojectbackend.payment.service.PaymentService;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

  private final PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  /**
   * 결제 요청 처리 및 결제 페이지 반환
   *
   * @param request 결제 요청 정보
   * @return 결제 정보 출력 메시지
   */
  @PostMapping("/process")
  @Transactional
  public ResponseEntity<String> processPayment(@RequestBody PaymentRequest request) {
    try {
      Payment payment = paymentService.savePayment(request);
      return ResponseEntity.ok("Payment processed successfully");

    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(e.getErrorType().getMessage());

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while processing the payment");
    }
  }

  /**
   * PT 횟수 선택과 총액 계산 [횟수 선택과 총액 계산 ~ API 결제]
   *
   * @param request 선택된 PT 횟수
   * @return 선택된 PT 횟수 객체
   */
  @PostMapping("/select-PtTimes")
  public ResponseEntity<PtTotalAmountResponse> selectPtTimes(@RequestBody PtTotalAmountRequest request) {
    try {
      PtTimes ptTimes = paymentService.selectPtTimes(request.getSelectedTimes());
      double totalAmount = request.getTrainerPrice() * ptTimes.getTimes();

      PtTotalAmountResponse response = new PtTotalAmountResponse(ptTimes.name(), ptTimes.getTimes(), totalAmount);
      return ResponseEntity.ok(response);

    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null);
    }
  }

  /**
   * API 요청 전 결제 정보 저장 [횟수 선택과 총액 계산 ~ API 결제]
   *
   * @param request 결제 요청 정보
   * @return 저장된 결제 정보
   */
  @PostMapping("/save-payment")
  public ResponseEntity<String> savePayment(@RequestBody PaymentRequest request) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
      }

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      Long currentUserId = userDetails.getUserId();

      if (!currentUserId.equals(request.getUserId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
      }

      Payment savedPayment = paymentService.savePayment(request);
      return ResponseEntity.ok("Payment saved successfully");

    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(e.getErrorType().getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null);
    }
  }

  /**
   * 결제 승인 및 상태 업데이트
   *
   * @param paymentId 결제 ID
   * @param paymentType 결제 수단
   * @return 승인된 결제 정보
   */
  @PutMapping("/{paymentId}")
  public ResponseEntity<Payment> approvePayment(@PathVariable Long paymentId, @RequestParam PaymentType paymentType) {
    try {
      Payment approvedPayment = paymentService.approvePayment(paymentId, paymentType);
      return ResponseEntity.ok(approvedPayment);
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null);
    }
  }

  /**
   * 결제 갱신 요청 처리
   *
   * @param paymentId 갱신할 결제의 ID
   * @param updateRequest 결제 갱신 요청 정보
   * @return 갱신된 결제 정보
   */
  @PutMapping("/update/{paymentId}")
  @Transactional
  public ResponseEntity<Payment> updatePayment(@PathVariable Long paymentId, @RequestBody PaymentUpdateRequest updateRequest) {
    try {
      Payment updatedPayment = paymentService.updatePayment(paymentId, updateRequest);
      return ResponseEntity.ok(updatedPayment);
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null);
    }
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