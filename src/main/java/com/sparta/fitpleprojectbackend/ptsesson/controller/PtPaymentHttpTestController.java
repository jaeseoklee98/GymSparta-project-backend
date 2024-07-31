package com.sparta.fitpleprojectbackend.ptsesson.controller;

import com.sparta.fitpleprojectbackend.common.CommonResponse;
import com.sparta.fitpleprojectbackend.enums.ErrorType;
import com.sparta.fitpleprojectbackend.exception.CustomException;
import com.sparta.fitpleprojectbackend.ptsesson.dto.PtInformationRequest;
import com.sparta.fitpleprojectbackend.ptsesson.dto.UserPtRequest;
import com.sparta.fitpleprojectbackend.ptsesson.entity.PtInfomation;
import com.sparta.fitpleprojectbackend.ptsesson.repository.PtInformationRepository;
import com.sparta.fitpleprojectbackend.ptsesson.service.PtPaymentHttpTestService;
import com.sparta.fitpleprojectbackend.security.UserDetailsImpl;
import com.sparta.fitpleprojectbackend.trainer.repository.TrainerRepository;
import com.sparta.fitpleprojectbackend.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ptpayments/test")
public class PtPaymentHttpTestController {

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
   * 결제 요청 정보 임시 저장
   *
   * @param request 결제 요청 정보
   * @return 상태 코드
   */
  @PostMapping("/paymentinformation")
  public ResponseEntity<String> savePtInformation(@RequestBody PtInformationRequest request) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Long currentUserId = ((UserDetailsImpl) authentication.getPrincipal()).getUserId();

      if (!currentUserId.equals(request.getUserId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("사용자 권한이 없습니다.");
      }

      ptPaymentHttpTestService.savePtInformation(request);
      return ResponseEntity.status(HttpStatus.OK).body("결제 준비 완료");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 준비 중 오류가 발생했습니다.");
    }
  }

  /**
   * 트레이너와 유저 검증 클라이언트 구입 요청 ~ 결제창 반환
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
    }
  }

  /**
   * 중복 PT 예약을 체크 클라이언트 구입 요청 ~ 결제창 반환
   *
   * @param request 트레이너 ID
   * @param request    유저 ID
   * @return 중복 여부
   */
  @PostMapping("/duplicatecheck")
  public ResponseEntity<CommonResponse<String>> checkDuplicatePt(@RequestBody UserPtRequest request) {
    try {
      ptPaymentHttpTestService.checkDuplicatePts(request.getTrainerId(), request.getUserId());
      return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), "중복 예약이 없습니다.", null));
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(new CommonResponse<>(e.getErrorType().getHttpStatus().value(),
              e.getErrorType().getMessage(), null));
    }
  }

  /**
   * 결제 페이지 조회 클라이언트 구입 요청 ~ 결제창 반환
   *
   * @param ptInfomationId 결제 정보 ID
   * @return 결제 페이지 정보가 포함된 응답
   */
  @GetMapping("/paymentpage/{ptInfomationId}")
  public ResponseEntity<CommonResponse<String>> showPaymentPage(@PathVariable Long ptInfomationId) {
    try {
      PtInfomation ptInformation = ptInformationRepository.findById(ptInfomationId)
          .orElseThrow(() -> new CustomException(ErrorType.PAYMENT_NOT_FOUND));
      String paymentPage = ptPaymentHttpTestService.showPaymentPage(ptInformation);

      return ResponseEntity.ok(
          new CommonResponse<>(HttpStatus.OK.value(), "결제 페이지 생성 성공", paymentPage));
    } catch (CustomException e) {
      return ResponseEntity.status(e.getErrorType().getHttpStatus())
          .body(new CommonResponse<>(e.getErrorType().getHttpStatus().value(),
              e.getErrorType().getMessage(), null));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest()
          .body(new CommonResponse<>(HttpStatus.BAD_REQUEST.value(), "요청 처리 중 오류가 발생했습니다.", null));
    }
  }
}