package com.sparta.fitpleprojectbackend.ptsesson.controller;

import com.sparta.fitpleprojectbackend.common.CommonResponse;
import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.exception.CustomException;
import com.sparta.fitpleprojectbackend.ptsesson.dto.PtInformationRequest;
import com.sparta.fitpleprojectbackend.ptsesson.dto.PtPaymentRequest;
import com.sparta.fitpleprojectbackend.ptsesson.dto.PtTotalAmountRequest;
import com.sparta.fitpleprojectbackend.ptsesson.dto.PtTotalAmountResponse;
import com.sparta.fitpleprojectbackend.ptsesson.entity.PtInfomation;
import com.sparta.fitpleprojectbackend.ptsesson.entity.PtPayment;
import com.sparta.fitpleprojectbackend.ptsesson.enums.PtTimes;
import com.sparta.fitpleprojectbackend.ptsesson.repository.PtInformationRepository;
import com.sparta.fitpleprojectbackend.ptsesson.service.PtPaymentHttpTestService;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fitpleprojectbackend.trainer.repository.TrainerRepository;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pt-payments/test")
public class PtPaymentHttpTestController {

  private static final Logger logger = LoggerFactory.getLogger(PtPaymentController.class);


  private final PtPaymentHttpTestService ptPaymentHttpTestService;
  private final PtInformationRepository ptInformationRepository;
  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;

  public PtPaymentHttpTestController(PtPaymentHttpTestService ptPaymentHttpTestService,
      PtInformationRepository ptInformationRepository,
      TrainerRepository trainerRepository,
      UserRepository userRepository) {
    this.ptPaymentHttpTestService = ptPaymentHttpTestService;
    this.ptInformationRepository = ptInformationRepository;
    this.trainerRepository = trainerRepository;
    this.userRepository = userRepository;
  }

  /**
   * 트레이너와 유저 검증 [클라이언트 구입 요청 ~ 결제창 반환]
   *
   * @param trainerId 트레이너 ID
   * @param userId    유저 ID
   * @return 검증 결과
   */
  @GetMapping("/validate/{trainerId}/{userId}")
  public ResponseEntity<CommonResponse<String>> validateTrainerAndUser(@PathVariable Long trainerId,
      @PathVariable Long userId) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Long currentUserId = ((UserDetailsImpl) authentication.getPrincipal()).getUserId();

      if (!currentUserId.equals(userId)) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new CommonResponse<>(HttpStatus.FORBIDDEN.value(), "사용자 권한이 없습니다.", null));
      }

      ptPaymentHttpTestService.validateTrainerAndUser(trainerId, userId);

      return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), "트레이너와 유저 검증 완료", null));
    } catch (CustomException e) {

      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(new CommonResponse<>(e.getErrorType().getHttpStatus().value(),
              e.getErrorType().getMessage(), null));
    } catch (Exception e) {

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new CommonResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
              "", null));
    }
  }

  /**
   * 결제 요청 정보 임시 저장 [클라이언트 구입 요청 ~ 결제창 반환]
   *
   * @param request 결제 요청 정보
   * @return 결제 정보 저장 결과
   */
  @PostMapping("/payment-information")
  public ResponseEntity<CommonResponse<String>> savePtInformation(
      @RequestBody PtInformationRequest request) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
        logger.warn("잘못된 인증입니다.");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new CommonResponse<>(HttpStatus.UNAUTHORIZED.value(), "잘못된 인증입니다.", null));
      }

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      Long currentUserId = userDetails.getUserId();
      logger.info("현재 사용자 ID:", currentUserId);

      if (!currentUserId.equals(request.getUserId())) {
        logger.warn("사용자 권한이 없습니다: 현재 사용자 ID = , 요청된 사용자 ID = ", currentUserId,
            request.getUserId());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new CommonResponse<>(HttpStatus.FORBIDDEN.value(), "사용자 권한이 없습니다.", null));
      }

      ptPaymentHttpTestService.savePtInformation(request);

      return ResponseEntity.ok(
          new CommonResponse<>(HttpStatus.OK.value(), "결제 정보가 저장되었습니다.", null));
    } catch (CustomException e) {

      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(new CommonResponse<>(e.getErrorType().getHttpStatus().value(),
              e.getErrorType().getMessage(), null));
    } catch (Exception e) {

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new CommonResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
              "결제 준비 중 오류가 발생했습니다.", null));
    }
  }

  /**
   * 중복 PT 예약을 체크 [클라이언트 구입 요청 ~ 결제창 반환]
   *
   * @param trainerId 트레이너 ID
   * @param userId    유저 ID
   * @return 중복 예약 체크 결과
   */
  @GetMapping("/check-duplicate/{trainerId}/{userId}")
  public ResponseEntity<CommonResponse<String>> checkDuplicatePt(@PathVariable Long trainerId,
      @PathVariable Long userId) {
    try {
      ptPaymentHttpTestService.checkDuplicatePt(trainerId, userId);

      return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), "중복 예약이 없습니다.", null));
    } catch (CustomException e) {

      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(new CommonResponse<>(e.getErrorType().getHttpStatus().value(),
              e.getErrorType().getMessage(), null));
    }
  }

  /**
   * 결제 페이지 조회 [클라이언트 구입 요청 ~ 결제창 반환]
   *
   * @param ptInformationId 결제 정보 ID
   * @return 결제 페이지 메시지
   */
  @GetMapping("/paymentpage/{ptInformationId}")
  public ResponseEntity<CommonResponse<String>> showPaymentPage(
      @PathVariable Long ptInformationId) {
    try {
      PtInfomation ptInformation = ptInformationRepository.findById(ptInformationId)
          .orElseThrow(() -> new CustomException(ErrorType.PAYMENT_NOT_FOUND));

      String completeMessage = ptPaymentHttpTestService.showPaymentPage(ptInformation);

      return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), completeMessage, null));
    } catch (CustomException e) {

      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(new CommonResponse<>(e.getErrorType().getHttpStatus().value(),
              e.getErrorType().getMessage(), null));
    }
  }

  /**
   * PT 횟수 선택과 총액 계산 [횟수 선택과 총액 계산 ~ API 결제]
   *
   * @param request 선택된 PT 횟수
   * @return 선택된 PT 횟수 객체
   */
  @PostMapping("/select-PtTimes")
  public ResponseEntity<CommonResponse<PtTotalAmountResponse>> selectPtTimes(
      @RequestBody PtTotalAmountRequest request) {

    PtTimes ptTimes = ptPaymentHttpTestService.selectPtTimes(request.getSelectedTimes());
    double totalAmount = request.getTrainerPrice() * ptTimes.getTimes();
    PtTotalAmountResponse response = new PtTotalAmountResponse(ptTimes.name(), ptTimes.getTimes(),
        totalAmount);

    return ResponseEntity.ok(
        new CommonResponse<>(HttpStatus.OK.value(), "PT 횟수와 총액 계산 완료", response));
  }

  /**
   * API 요청 전 결제 정보 저장 [클라이언트 결제 요청]
   *
   * @param request 결제 요청 정보
   * @return 저장된 결제 정보
   */
  @PostMapping("/save-payment")
  public ResponseEntity<CommonResponse<String>> savePtPayment(
      @RequestBody PtPaymentRequest request) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new CommonResponse<>(HttpStatus.UNAUTHORIZED.value(), "잘못된 인증입니다.", null));
      }

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      Long currentUserId = userDetails.getUserId();
      logger.info("현재 사용자 ID: ", currentUserId);

      if (!currentUserId.equals(request.getUserId())) {
        logger.warn("사용자 권한이 없습니다: 현재 사용자 ID = , 요청된 사용자 ID = ", currentUserId,
            request.getUserId());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new CommonResponse<>(HttpStatus.FORBIDDEN.value(), "", null));
      }

      PtPayment savedPayment = ptPaymentHttpTestService.SavePayment(request, currentUserId,
          request.getAmount());

      return ResponseEntity.ok(
          new CommonResponse<>(HttpStatus.OK.value(), "결제 정보가 저장되었습니다.", null));

    } catch (CustomException e) {

      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(new CommonResponse<>(e.getErrorType().getHttpStatus().value(),
              e.getErrorType().getMessage(), null));

    } catch (Exception e) {

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new CommonResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
              "결제 준비 중 오류가 발생했습니다.", null));
    }
  }

//  /**
//   * 결제 정보 검증 및 상태 업데이트
//   *
//   * @param paymentId 결제 ID
//   * @return 승인된 결제 정보
//   */
//  @PutMapping("/approve/{paymentId}")
//  public ResponseEntity<PtPayment> approvePayment(@PathVariable Long paymentId) {
//    PtPayment approvedPayment = ptPaymentHttpTestService.approvePayment(paymentId);
//
//    return ResponseEntity.ok(approvedPayment);
//  }

  /**
   * 결제를 완료하고 최종 결제 정보를 저장
   *
   * @param request 결제 요청 정보
   * @param userId  유저 ID
   * @return 저장된 결제 정보
   * @throws CustomException 결제 처리 중 오류 발생 시
   */
  @PostMapping("/complete")
  public ResponseEntity<PtPayment> completePayment(@RequestBody PtPaymentRequest request,
      @RequestParam Long userId) {
    try {
      PtPayment ptPayment = ptPaymentHttpTestService.completePayment(request, userId);
      return ResponseEntity.ok(ptPayment);
    } catch (CustomException e) {

      return ResponseEntity.status(e.getErrorType().getHttpStatus()).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

//  /**
//   * 결제 정보의 일관성을 검증
//   *
//   * @param ptPayment 결제 정보
//   * @return 상태 메시지
//   * @throws CustomException 결제 정보가 일치하지 않는 경우
//   */
//  @PostMapping("/check")
//  public ResponseEntity<String> verifyPayment(@RequestBody PtPayment ptPayment) {
//    try {
//      ptPaymentHttpTestService.verifyPayment(ptPayment);
//      return ResponseEntity.ok("결제 정보가 일치합니다.");
//    } catch (CustomException e) {
//      return ResponseEntity.status(e.getErrorType().getHttpStatus()).body(e.getMessage());
//    }
//  }

  /**
   * 결제 정보를 UserPt에 저장
   *
   * @param ptPayment 결제 정보
   * @return 상태 메시지
   */
  @PostMapping("/save-UserPt")
  public ResponseEntity<String> savePaymentToUserPt(@RequestBody PtPayment ptPayment) {
    try {
      ptPaymentHttpTestService.savePaymentToUserPt(ptPayment);
      return ResponseEntity.ok("결제 정보가 UserPt에 저장되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 정보 저장 중 오류 발생");
    }
  }

  /**
   * 결제 완료 페이지
   *
   * @param ptPayment 결제 정보
   * @return 결제 완료 메시지
   */
  @PostMapping("/completePage")
  public ResponseEntity<String> paymentCompletePage(@RequestBody PtPayment ptPayment) {
    try {
      String message = ptPaymentHttpTestService.PaymentCompletePage(ptPayment);
      return ResponseEntity.ok(message);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 완료 페이지 생성 중 오류 발생");
    }
  }

  /**
   * 결제를 완료하고 최종 결제 정보를 저장하며 결제 완료 페이지를 생성
   *
   * @param request 결제 요청 정보
   * @param userId  유저 ID
   * @return 결제 완료 메시지
   */
  @PostMapping("/all-complete")
  public ResponseEntity<String> completeAllPayment(@RequestBody PtPaymentRequest request,
      @RequestParam Long userId) {
    try {

      PtPayment ptPayment = ptPaymentHttpTestService.completePayment(request, userId);

      ptPaymentHttpTestService.savePaymentToUserPt(ptPayment);

      String message = ptPaymentHttpTestService.PaymentCompletePage(ptPayment);

      logger.info("결제 완료: 사용자 ID = , 요청 정보 = ", userId, request);
      return ResponseEntity.ok(message);
    } catch (CustomException e) {
      logger.error("결제 처리 중 오류 발생: ", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (Exception e) {
      logger.error("예상치 못한 오류 발생: ", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 처리 중 오류가 발생했습니다.");
    }
  }
}

