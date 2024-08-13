package com.sparta.gymspartaprojectbackend.payment.controller;

import com.sparta.gymspartaprojectbackend.exception.CustomException;
import com.sparta.gymspartaprojectbackend.payment.dto.PaymentRequest;
import com.sparta.gymspartaprojectbackend.payment.dto.PaymentResponse;
import com.sparta.gymspartaprojectbackend.payment.dto.PaymentUpdateRequest;
import com.sparta.gymspartaprojectbackend.payment.dto.PtTotalAmountRequest;
import com.sparta.gymspartaprojectbackend.payment.dto.PtTotalAmountResponse;
import com.sparta.gymspartaprojectbackend.payment.entity.Payment;
import com.sparta.gymspartaprojectbackend.payment.enums.PaymentStatus;
import com.sparta.gymspartaprojectbackend.payment.enums.PaymentType;
import com.sparta.gymspartaprojectbackend.payment.enums.PtTimes;
import com.sparta.gymspartaprojectbackend.payment.service.PaymentService;
import com.sparta.gymspartaprojectbackend.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import java.util.List;
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
   * @return 결제 처리 결과 메시지
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
   * PT 횟수 선택과 총액 계산
   *
   * @param request 선택된 PT 횟수 및 트레이너 가격 정보
   * @return 선택된 PT 횟수 및 총액 정보
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
   * 결제 정보 저장
   *
   * @param request 결제 요청 정보
   * @return 저장된 결제 정보 메시지
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
   * 환불 요청 처리
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

  /**
   * 결제 상태 조회
   *
   * @param paymentId 결제 ID
   * @return 결제 상태
   */
  @GetMapping("/status/{paymentId}")
  public ResponseEntity<PaymentStatus> inquirePaymentStatus(@PathVariable Long paymentId) {
    try {
      PaymentStatus paymentStatus = paymentService.inquirePaymentStatus(paymentId);
      return ResponseEntity.ok(paymentStatus);
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null);
    }
  }

  /**
   * 결제 페이지 반환 및 결제 수단 선택 처리
   *
   * @param paymentId 결제 ID
   * @param paymentType 선택한 결제 수단
   * @return 결제 결과 메시지
   */
  @PostMapping("/{paymentId}/select-payment-type")
  public ResponseEntity<String> selectPaymentType(@PathVariable Long paymentId, @RequestParam PaymentType paymentType) {
    try {
      String paymentResult = paymentService.processPaymentWithSelectedType(paymentId, paymentType);
      return ResponseEntity.ok(paymentResult);
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(e.getErrorType().getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while processing the payment");
    }
  }

  /**
   * 결제 완료 처리 및 내역 반환
   *
   * @param paymentId 결제 ID
   * @return 결제 완료된 Payment 엔티티
   */
  @PostMapping("/complete/{paymentId}")
  public ResponseEntity<Payment> completePayment(@PathVariable Long paymentId) {
    try {
      Payment completedPayment = paymentService.completePayment(paymentId);
      return ResponseEntity.ok(completedPayment);
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null);
    }
  }

  /**
   * 유저의 결제 내역을 조회
   *
   * @param userId 유저 ID
   * @return 유저의 결제 내역 리스트
   */
  @GetMapping("/history/{userId}")
  public ResponseEntity<List<PaymentResponse>> getUserPaymentHistory(@PathVariable Long userId) {
    try {
      List<PaymentResponse> paymentHistory = paymentService.getUserPaymentHistory(userId);
      return ResponseEntity.ok(paymentHistory);
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null);
    }
  }
}