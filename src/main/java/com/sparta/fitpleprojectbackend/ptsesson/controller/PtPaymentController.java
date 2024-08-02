package com.sparta.fitpleprojectbackend.ptsesson.controller;

import com.sparta.fitpleprojectbackend.exception.CustomException;
import com.sparta.fitpleprojectbackend.ptsesson.dto.PtInformationRequest;
import com.sparta.fitpleprojectbackend.ptsesson.dto.PtPaymentRequest;
import com.sparta.fitpleprojectbackend.ptsesson.dto.PtTotalAmountRequest;
import com.sparta.fitpleprojectbackend.ptsesson.dto.PtTotalAmountResponse;
import com.sparta.fitpleprojectbackend.ptsesson.entity.PtInfomation;
import com.sparta.fitpleprojectbackend.ptsesson.entity.PtPayment;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PaymentType;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PtTimes;
import com.sparta.fitpleprojectbackend.ptsesson.repository.PtInformationRepository;
import com.sparta.fitpleprojectbackend.ptsesson.service.PtPaymentService;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fitpleprojectbackend.trainer.repository.TrainerRepository;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pt-payments")
public class PtPaymentController {

  private PtPaymentService ptPaymentService;
  private final PtInformationRepository ptInformationRepository;
  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;

  public PtPaymentController(PtPaymentService ptPaymentService,
      PtInformationRepository ptInformationRepository,
      TrainerRepository trainerRepository,
      UserRepository userRepository) {
    this.ptPaymentService = ptPaymentService;
    this.ptInformationRepository = ptInformationRepository;
    this.trainerRepository = trainerRepository;
    this.userRepository = userRepository;
  }


  /**
   * 결제 요청 처리 및 결제 페이지 반환
   *
   * @param request 결제 요청 정보
   * @return 결제 정보 출력 메시지
   */
  @PostMapping("/process")
  @Transactional
  public ResponseEntity<String> processPayment(@RequestBody PtInformationRequest request) {
    try {
      PtInfomation ptInformation = ptPaymentService.savePtInformation(request);

      ptPaymentService.checkDuplicatePt(request.getTrainerId(), request.getUserId());

      String completeMessage = ptPaymentService.showPaymentPage(ptInformation);

      return ResponseEntity.ok(completeMessage);

    } catch (CustomException e) {

      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(e.getErrorType().getMessage());

    } catch (Exception e) {

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null);
    }
  }

  /**
   * PT 횟수 선택과 총액 계산 [횟수 선택과 총액 계산 ~ API 결제]
   *
   * @param request 선택된 PT 횟수
   * @return 선택된 PT 횟수 객체
   */
  @PostMapping("/select-PtTimes")
  public ResponseEntity<PtTotalAmountResponse> selectPtTimes(
      @RequestBody PtTotalAmountRequest request) {
    try {
      PtTimes ptTimes = ptPaymentService.selectPtTimes(request.getSelectedTimes());

      double totalAmount = request.getTrainerPrice() * ptTimes.getTimes();

      PtTotalAmountResponse response = new PtTotalAmountResponse(ptTimes.name(), ptTimes.getTimes(),
          totalAmount);

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
  public ResponseEntity<String> savePtInformation(@RequestBody PtPaymentRequest request) {
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

      PtPayment savedPayment = ptPaymentService.SavePayment(request, currentUserId,
          request.getAmount());
      return ResponseEntity.ok(null);

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
   * @param ptPaymentId 결제 ID
   * @param paymentType 결제 수단
   * @return 승인된 결제 정보
   */
  @PutMapping("/{ptPaymentId}")
  public ResponseEntity<PtPayment> approvePayment(
      @PathVariable Long ptPaymentId,
      @RequestParam PaymentType paymentType) {

    PtPayment approvedPayment = ptPaymentService.approvePayment(ptPaymentId, paymentType);
    return ResponseEntity.ok(approvedPayment);
  }
}




